package sigma.training.eum.assignment.service;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import sigma.training.eum.assignment.dictionary.Status;
import sigma.training.eum.assignment.exception.AssignmentNotFoundException;
import sigma.training.eum.assignment.exception.IncorrectAssignmentDescriptionException;
import sigma.training.eum.assignment.exception.IncorrectAssignmentStatusException;
import sigma.training.eum.assignment.mapper.entity.AssignmentEntityMapper;
import sigma.training.eum.assignment.persistence.entity.AssignmentEntity;
import sigma.training.eum.assignment.persistence.repository.AssignmentRepository;
import sigma.training.eum.assignment.persistence.repository.specification.AssignmentSpecification;
import sigma.training.eum.assignment.service.type.Assignment;
import sigma.training.eum.assignment.service.type.AssignmentId;
import sigma.training.eum.assignment.service.validator.AssignmentValidator;
import sigma.training.eum.course.exception.CourseNotFoundException;
import sigma.training.eum.course.exception.IncorrectCourseStatusException;
import sigma.training.eum.course.exception.NotExistingCourseIdException;
import sigma.training.eum.course.service.CourseService;
import sigma.training.eum.course.service.type.Course;
import sigma.training.eum.course.service.type.CourseId;
import sigma.training.eum.task.exception.IncorrectTaskStatusException;
import sigma.training.eum.task.service.TaskService;
import sigma.training.eum.task.service.type.Task;
import sigma.training.eum.tutor.exception.IncorrectUserIdException;
import sigma.training.eum.tutor.exception.InsufficientTutorAuthoritiesException;
import sigma.training.eum.tutor.exception.TutorNotFoundException;
import sigma.training.eum.tutor.service.TutorService;
import sigma.training.eum.tutor.service.type.Tutor;
import sigma.training.eum.tutor.service.type.TutorId;
import sigma.training.eum.user.service.UserService;
import sigma.training.eum.user.service.type.User;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssignmentService {
  private final AssignmentRepository assignmentRepository;
  private final AssignmentEntityMapper assignmentEntityMapper;
  private final AssignmentValidator assignmentValidator;
  private final CourseService courseService;
  private final TutorService tutorService;
  private final TaskService taskService;
  public AssignmentService(@Lazy TaskService taskService, TutorService tutorService, CourseService courseService,
                           AssignmentValidator assignmentValidator, AssignmentEntityMapper assignmentEntityMapper, AssignmentRepository assignmentRepository, UserService userService) {
    this.taskService = taskService;
    this.tutorService = tutorService;
    this.courseService = courseService;
    this.assignmentValidator = assignmentValidator;
    this.assignmentEntityMapper = assignmentEntityMapper;
    this.assignmentRepository = assignmentRepository;
  }

  public Assignment get(AssignmentId id) throws AssignmentNotFoundException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null!");
    }
    return assignmentRepository.findAssignmentEntityById(id.value())
      .map(assignmentEntityMapper::fromEntity)
      .orElseThrow(() -> new AssignmentNotFoundException("Assigment with id " + id.value() + " was not found!"));
  }

  public List<Assignment> getAll(Optional<TutorId> tutorId, Optional<CourseId> courseId, Optional<Status> status) {
    Optional<List<Long>> courseIds = tutorId.map(this::getCourseIdsList).map(list -> list.isEmpty() ? null : list);
    return assignmentRepository.findAll(assignmentFilterToCriteria(courseIds, courseId.map(CourseId::value), status))
      .stream()
      .map(assignmentEntityMapper::fromEntity).toList();
  }

  public List<Assignment> getAll(Optional<TutorId> tutorId) {
    return getAll(tutorId, Optional.empty(), Optional.empty());
  }

  private List<Long> getCourseIdsList(TutorId tutorId) {
    return
      courseService.getAll(Optional.empty(), Optional.of(tutorId.value()), Optional.empty())
        .stream()
        .map(i -> i.courseId().value())
        .collect(Collectors.toCollection(ArrayList::new));
  }

  @SneakyThrows(TutorNotFoundException.class)
  @Transactional
  public Assignment create(Assignment assignment)
    throws CourseNotFoundException, IncorrectCourseStatusException, NotExistingCourseIdException {
    assignmentValidator.validateNewAssignment(assignment);
    Course course = courseService.get(assignment.courseId());
    Tutor tutor = tutorService.requireCurrentUserAsTutor();
    if (course.status() != sigma.training.eum.course.dictionary.Status.STARTED) {
      throw new IncorrectCourseStatusException("Course status should be only Started");
    }
    if (course.tutorId().equals(tutor.id())){
      Assignment updatedAssignment = new Assignment(null, Status.CREATED, assignment.description(), assignment.courseId(), null, null, null);
      return assignmentEntityMapper.fromEntity(
        assignmentRepository.save(
          assignmentEntityMapper.toEntity(updatedAssignment)));
    }
    throw new AccessDeniedException("Access denied!");
  }

  private Specification<AssignmentEntity> assignmentFilterToCriteria(Optional<List<Long>> courseIds,
                                                                     Optional<Long> courseId,
                                                                     Optional<Status> status) {
    Specification<AssignmentEntity> specification = Specification.where(null);
    if (courseIds.isPresent()) {
      specification = specification.and(AssignmentSpecification.byCourseIdList(courseIds.get()));
    }
    if (courseId.isPresent()) {
      specification = specification.and(AssignmentSpecification.byCourseId(courseId.get()));
    }
    if (status.isPresent()) {
      specification = specification.and(AssignmentSpecification.byOrderStatus(status.get()));
    }
    return specification;
  }

  @Transactional
  public Assignment start(AssignmentId assignmentId)
    throws CourseNotFoundException, IncorrectCourseStatusException, NotExistingCourseIdException, AssignmentNotFoundException {
    Assignment assignment = get(assignmentId);
    assignmentValidator.validateStartedAssignment(assignment);
    Course course = courseService.get(assignment.courseId());
    if (course == null) {
      throw new NotExistingCourseIdException("Course id does not exist");
    } else if (course.status() != sigma.training.eum.course.dictionary.Status.STARTED) {
      throw new IncorrectCourseStatusException("Course status should be only \"STARTED\"");
    }
    Assignment updatedAssignment = new Assignment(assignment.assignmentId(), Status.STARTED, assignment.description(), assignment.courseId(), assignment.creationDate(), new Timestamp(System.currentTimeMillis()), null);
    taskService.createTasks(assignmentId);
    return assignmentEntityMapper.fromEntity(
      assignmentRepository.save(
        assignmentEntityMapper.toEntity(updatedAssignment)));
  }

  public List<Assignment> getCurrentUserAssignments(Status status) throws TutorNotFoundException {
    Tutor tutor = tutorService.requireCurrentUserAsTutor();
    return getAll(Optional.of(tutor.id()), Optional.empty(), Optional.of(status));
  }

  public Assignment finish(AssignmentId assignmentId) throws AssignmentNotFoundException, TutorNotFoundException, CourseNotFoundException, InsufficientTutorAuthoritiesException, IncorrectTaskStatusException {
    Assignment assignment = get(assignmentId);
    Tutor tutor = tutorService.requireCurrentUserAsTutor();
    Course course = courseService.get(assignment.courseId());
    if (!course.tutorId().equals(tutor.id())){
      throw new InsufficientTutorAuthoritiesException("Cannot resolve a current tutor");
    }
    if(assignment.status()!= Status.STARTED){
      throw new IncorrectAssignmentStatusException("Cannot finished an assignment which is not started!");
    }
    List<Task> tasks = taskService.getAll(Optional.of(tutor.id()),Optional.empty(),Optional.of(assignmentId), Optional.of(sigma.training.eum.task.dictionary.Status.INREVIEW));
    if(!tasks.isEmpty()){
      throw new IncorrectTaskStatusException("Cannot finish an assignment that has any \"In review\" tasks!");
    }
    Assignment updatedAssignment = new Assignment(assignment.assignmentId(), Status.FINISHED, assignment.description(), assignment.courseId(), assignment.creationDate(), assignment.startDate(), new Timestamp(System.currentTimeMillis()));
    return assignmentEntityMapper.fromEntity(
      assignmentRepository.save(
        assignmentEntityMapper.toEntity(updatedAssignment)));
  }
}
