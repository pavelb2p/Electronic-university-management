package sigma.training.eum.task.service;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sigma.training.eum.assignment.exception.AssignmentNotFoundException;
import sigma.training.eum.assignment.exception.IncorrectAssignmentIdException;
import sigma.training.eum.assignment.service.AssignmentService;
import sigma.training.eum.assignment.service.type.Assignment;
import sigma.training.eum.assignment.service.type.AssignmentId;
import sigma.training.eum.course.exception.CourseNotFoundException;
import sigma.training.eum.course.service.CourseService;
import sigma.training.eum.course.service.type.Course;
import sigma.training.eum.student.exception.IncorrectStudentIdInTaskException;
import sigma.training.eum.student.exception.StudentNotFoundException;
import sigma.training.eum.student.service.StudentService;
import sigma.training.eum.student.service.type.Student;
import sigma.training.eum.student.service.type.StudentId;
import sigma.training.eum.task.dictionary.Status;
import sigma.training.eum.task.exception.*;
import sigma.training.eum.task.mapper.entity.TaskEntityMapper;
import sigma.training.eum.task.persistence.entity.TaskEntity;
import sigma.training.eum.task.persistence.repository.TaskRepository;
import sigma.training.eum.task.persistence.repository.specification.TaskSpecification;
import sigma.training.eum.task.service.type.Task;
import sigma.training.eum.task.service.type.TaskId;
import sigma.training.eum.tutor.exception.IncorrectUserIdException;
import sigma.training.eum.tutor.exception.InsufficientTutorAuthoritiesException;
import sigma.training.eum.tutor.exception.TutorNotFoundException;
import sigma.training.eum.tutor.service.TutorService;
import sigma.training.eum.tutor.service.type.Tutor;
import sigma.training.eum.tutor.service.type.TutorId;
import sigma.training.eum.user.service.UserService;
import sigma.training.eum.user.service.type.UserId;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskService {
  private final TaskRepository taskRepository;
  private final TaskEntityMapper taskEntityMapper;
  private final AssignmentService assignmentService;
  private final CourseService courseService;
  private final StudentService studentService;
  private final UserService userService;
  private final TutorService tutorService;

  @Transactional
  public List<Task> createTasks(AssignmentId assignmentId) throws AssignmentNotFoundException, CourseNotFoundException {
    if (assignmentId == null) {
      throw new IncorrectAssignmentIdException("Assignment id cannot be null");
    }
    Assignment assignment;
    try {
      assignment = assignmentService.get(assignmentId);
    } catch (AssignmentNotFoundException e) {
      throw new AssignmentNotFoundException("No assignment is found by this assignments id, so the task cannot be created");
    }
    List<TaskEntity> taskEntities = courseService.getStudents(assignment.courseId()).stream().map(StudentId::value)
      .map(id -> new Task(null, Status.STARTED, null, assignmentId, new StudentId(id), null, null))
      .map(taskEntityMapper::toEntity).toList();

    return taskRepository.saveAll(taskEntities).stream().map(taskEntityMapper::fromEntity).toList();
  }

  public Task get(TaskId id) throws TaskNotFoundException {
    if (id == null) {
      throw new IncorrectTaskIdException("Task id cannot be null!");
    }
    return taskRepository.findTaskEntityById(id.value())
      .map(taskEntityMapper::fromEntity)
      .orElseThrow(() -> new TaskNotFoundException("Task with id " + id.value() + " was not found!"));
  }

  public List<Task> getAll(Optional<TutorId> tutorId, Optional<StudentId> studentId, Optional<AssignmentId> assignmentId, Optional<Status> status) {
    Optional<List<Long>> tutorAssignmentIds =
      tutorId.isPresent()
        ? Optional.of(assignmentService.getAll(tutorId).stream().map(i -> i.assignmentId().value()).collect(Collectors.toCollection(ArrayList::new)))
        : Optional.empty();
    return taskRepository.findAll(tasksFilterToCriteria(tutorAssignmentIds, studentId.map(StudentId::value), assignmentId.map(AssignmentId::value), status))
      .stream().map(taskEntityMapper::fromEntity).toList();
  }

  private Specification<TaskEntity> tasksFilterToCriteria(Optional<List<Long>> assignmentIds,
                                                          Optional<Long> studentId,
                                                          Optional<Long> assignmentId,
                                                          Optional<Status> status) {
    Specification<TaskEntity> specification = Specification.where(null);
    if (assignmentIds.isPresent()) {
      specification = specification.and(TaskSpecification.byAssignmentIds(assignmentIds.get()));
    }
    if (assignmentId.isPresent()) {
      specification = specification.and(TaskSpecification.byOrderAssignmentId(assignmentId.get()));
    }
    if (studentId.isPresent()) {
      specification = specification.and(TaskSpecification.byOrderStudentId(studentId.get()));
    }
    if (status.isPresent()) {
      specification = specification.and(TaskSpecification.byOrderStatus(status.get()));
    }
    return specification;
  }

  @Transactional
  public Task progress(TaskId taskId, String url)
    throws TaskNotFoundException, IncorrectTaskStatusException, StudentNotFoundException {
    Student student = studentService.get(userService.getCurrentUser().userId());
    Task task = get(taskId);
    if (!task.studentId().value().equals(student.studentId().value())) {
      throw new IncorrectStudentIdInTaskException("Cannot progress current task id!");
    }
    if (task.status() != Status.STARTED) {
      throw new IncorrectTaskStatusException("Status should be only \"Started\"!");
    }
    if (url.isEmpty()) {
      throw new IncorrectTaskUrlException("Commitment URL should not be empty!");
    }
    Task updatedTask = new Task(taskId, Status.INREVIEW, url, task.assignmentId(), task.studentId(), task.mark(), task.returnReason());
    return taskEntityMapper.fromEntity(
      taskRepository.save(
        taskEntityMapper.toEntity(updatedTask)
      )
    );
  }

  @Transactional
  public Task finish(TaskId taskId, int mark)
    throws TutorNotFoundException, TaskNotFoundException, IncorrectTaskStatusException {
    Tutor tutor = tutorService.get(userService.getCurrentUser().userId());
    Task task = get(taskId);
    if (!getAll(Optional.of(tutor.id()), Optional.empty(), Optional.of(task.assignmentId()), Optional.empty()).contains(task)) {
      throw new IncorrectStudentIdInTaskException("Cannot finish current task");
    }
    if (task.status() != Status.INREVIEW) {
      throw new IncorrectTaskStatusException("Status should be only in review to finish a task");
    }
    if (mark < 0 || mark > 100) {
      throw new IncorrectMarkValueException("The value of mark can be only between 0 and 100");
    }
    Task updatedTask = new Task(taskId, Status.COMPLETED, task.completedTaskUrl(), task.assignmentId(), task.studentId(), mark, null);
    return taskEntityMapper.fromEntity(
      taskRepository.save(
        taskEntityMapper.toEntity(updatedTask)
      )
    );
  }

  public List<Task> getTasksByStatus(Optional<Status> status) throws IncorrectUserRoleException {
    UserId userId = userService.getCurrentUser().userId();
    try {
      if (userService.getCurrentUserRole().equals("tutor")) {
        return getAll(Optional.of(tutorService.get(userId).id()), Optional.empty(), Optional.empty(), status);
      }
      if (userService.getCurrentUserRole().equals("student")) {
        List<Task> tasks = getAll(Optional.empty(), Optional.of(studentService.get(userId).studentId()), Optional.empty(), status);
        return supplementTasks(tasks);
      }
    } catch (TutorNotFoundException | StudentNotFoundException | AssignmentNotFoundException e) {
      throw new GenericServiceException("Cannot resolve internal user identifiers.");
    }
    throw new IncorrectUserRoleException("User should be either student or tutor here.");
  }

  private List<Task> supplementTasks(List<Task> tasks) throws AssignmentNotFoundException {
    Map<TaskId, String> taskIdToDescriptions = new HashMap<>();
      for (Task task : tasks)
        taskIdToDescriptions.put(task.taskId(), assignmentService.get(task.assignmentId()).description());

    return tasks.stream().map(task -> task.supplementDescription(taskIdToDescriptions.get(task.taskId()))).toList();
  }

  public Task returnTask(TaskId taskId, String returnReason)
    throws TaskNotFoundException, IncorrectTaskStatusException, TutorNotFoundException, AssignmentNotFoundException, CourseNotFoundException, InsufficientTutorAuthoritiesException {
    if (returnReason == null) {
      throw new IncorrectTaskReturnReasonException("Return reason cannot be null!");
    }
    if (returnReason.isEmpty()) {
      throw new IncorrectTaskReturnReasonException("Return reason cannot be empty!");
    }
    Tutor tutor = tutorService.requireCurrentUserAsTutor();
    Task task = get(taskId);
    Assignment assignment = assignmentService.get(task.assignmentId());
    Course course = courseService.get(assignment.courseId());
    if (!tutor.id().equals(course.tutorId())) {
      throw new InsufficientTutorAuthoritiesException("Cannot resolve permissions for this tutor");
    }
    if (task.status() != Status.INREVIEW) {
      throw new IncorrectTaskStatusException("Task status can be only \"In review\"!");
    }
    Task updatedTask = new Task(taskId, Status.STARTED, task.completedTaskUrl(), task.assignmentId(), task.studentId(), task.mark(), returnReason);
    return taskEntityMapper.fromEntity(
      taskRepository.save(
        taskEntityMapper.toEntity(updatedTask)
      )
    );
  }

  public List<Task> getTasksByAssignmentId(AssignmentId assignmentId) throws TutorNotFoundException {
    return getAll(Optional.of(tutorService.get(userService.getCurrentUser().userId()).id()), Optional.empty(),
      Optional.of(assignmentId), Optional.empty());
  }
}
