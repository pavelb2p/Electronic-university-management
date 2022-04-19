package sigma.training.eum.task.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import sigma.training.eum.assignment.exception.AssignmentNotFoundException;
import sigma.training.eum.assignment.exception.IncorrectAssignmentIdException;
import sigma.training.eum.assignment.service.AssignmentService;
import sigma.training.eum.assignment.service.type.Assignment;
import sigma.training.eum.assignment.service.type.AssignmentId;
import sigma.training.eum.course.exception.CourseNotFoundException;
import sigma.training.eum.course.service.CourseService;
import sigma.training.eum.course.service.type.Course;
import sigma.training.eum.course.service.type.CourseId;
import sigma.training.eum.student.exception.IncorrectStudentIdInTaskException;
import sigma.training.eum.student.exception.IncorrectUserIdException;
import sigma.training.eum.student.exception.StudentNotFoundException;
import sigma.training.eum.student.service.StudentService;
import sigma.training.eum.student.service.type.Student;
import sigma.training.eum.student.service.type.StudentId;
import sigma.training.eum.task.dictionary.Status;
import sigma.training.eum.task.exception.*;
import sigma.training.eum.task.mapper.entity.TaskEntityMapper;
import sigma.training.eum.task.persistence.entity.TaskEntity;
import sigma.training.eum.task.persistence.repository.TaskRepository;
import sigma.training.eum.task.service.type.Task;
import sigma.training.eum.task.service.type.TaskId;
import sigma.training.eum.tutor.exception.InsufficientTutorAuthoritiesException;
import sigma.training.eum.tutor.exception.TutorNotFoundException;
import sigma.training.eum.tutor.service.TutorService;
import sigma.training.eum.tutor.service.type.Tutor;
import sigma.training.eum.tutor.exception.TutorNotFoundException;
import sigma.training.eum.tutor.service.TutorService;
import sigma.training.eum.tutor.service.type.Tutor;
import sigma.training.eum.tutor.service.type.TutorId;
import sigma.training.eum.user.dictionary.Role;
import sigma.training.eum.user.service.UserService;
import sigma.training.eum.user.service.type.User;
import sigma.training.eum.user.service.type.UserId;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
  private static final TaskEntity TASK_ENTITY1 = new TaskEntity();
  private static final TaskEntity TASK_ENTITY2 = new TaskEntity();
  private static final TaskEntity TASK_ENTITY3 = new TaskEntity();
  private static final Task TASK1 = new Task(null, Status.STARTED, null, new AssignmentId(1L), new StudentId(1L), null, null);
  private static final Task TASK2 = new Task(new TaskId(2L), Status.STARTED, null, new AssignmentId(2L), new StudentId(2L), null, null);
  private static final Task TASK3 = new Task(new TaskId(3L), Status.INREVIEW, null, new AssignmentId(3L), new StudentId(3L), null, null);
  private static final Student STUDENT1 = new Student(new StudentId(1L), sigma.training.eum.student.dictionary.Status.ACTIVE, "Sofia", new UserId(1L));
  private static final Assignment ASSIGNMENT1 = new Assignment(new AssignmentId(1L), sigma.training.eum.assignment.dictionary.Status.CREATED, "Something", new CourseId(1L), new Timestamp(System.currentTimeMillis()), null, null);
  private static final Tutor TUTOR = new Tutor(new TutorId(1L), sigma.training.eum.tutor.dictionary.Status.ACTIVE, "Sofia", new UserId(1l));
  static {
    TASK_ENTITY1.setStatus(Status.STARTED);
    TASK_ENTITY1.setAssignmentId(1L);
    TASK_ENTITY1.setStudentId(1L);
    TASK_ENTITY2.setId(2L);
    TASK_ENTITY2.setStatus(Status.STARTED);
    TASK_ENTITY2.setAssignmentId(2L);
    TASK_ENTITY2.setStudentId(2L);
    TASK_ENTITY3.setId(3L);
    TASK_ENTITY3.setStatus(Status.INREVIEW);
    TASK_ENTITY3.setAssignmentId(3L);
    TASK_ENTITY3.setStudentId(3L);
  }
  private static final List<TaskEntity> TASK_ENTITY_LIST = Collections.singletonList(TASK_ENTITY1);
  private static final List<TaskEntity> TASK_ENTITY_LIST2 = Collections.singletonList(TASK_ENTITY3);
  private static final List<Task> TASK_LIST = Collections.singletonList(TASK1);
  private static final List<Student> STUDENT_LIST = Collections.singletonList(STUDENT1);
  private static final Set<StudentId> STUDENT_ID_SET = Collections.singleton(new StudentId(1L));
  @InjectMocks
  private TaskService taskService;
  @Mock
  private TaskRepository taskRepository;
  @Mock
  private TaskEntityMapper taskEntityMapper;
  @Mock
  private AssignmentService assignmentService;
  @Mock
  private CourseService courseService;
  @Mock
  private StudentService studentService;
  @Mock
  private UserService userService;
  @Mock
  private TutorService tutorService;
  @Test
  @SneakyThrows({TaskNotFoundException.class, IncorrectTaskIdException.class})
  public void getTest() {
    when(taskRepository.findTaskEntityById(1L)).thenReturn(Optional.of(TASK_ENTITY1));
    when(taskEntityMapper.fromEntity(TASK_ENTITY_LIST.get(0))).thenReturn(TASK1);
    Task actual = taskService.get(new TaskId(1L));
    assertNotNull(actual);
    assertEquals(TASK1, actual);
    verify(taskRepository).findTaskEntityById(1L);
    verify(taskEntityMapper).fromEntity(any());
  }
  @Test
  public void failToGetWithNullTaskId(){
    assertThrows(IncorrectTaskIdException.class, () -> taskService.get(null));
  }
  @Test
  public void failToGetWithNotExistingTaskId() {
    when(taskRepository.findTaskEntityById(1L)).thenReturn(Optional.empty());
    assertThrows(TaskNotFoundException.class, () -> taskService.get(new TaskId(1L)));
  }
  @Test
  public void getAllForTutorTest(){
    when(taskRepository.findAll(any(Specification.class))).thenReturn(TASK_ENTITY_LIST);
    when(taskEntityMapper.fromEntity(TASK_ENTITY_LIST.get(0))).thenReturn(TASK1);
    List<Task> actual = taskService.getAll(Optional.of(new TutorId(1L)), Optional.empty(), Optional.of(new AssignmentId(1L)), Optional.of(Status.STARTED));
    assertNotNull(actual);
    assertEquals(1, actual.size());
    assertEquals(TASK_LIST, actual);
  }
  @Test
  public void getAllForStudentTest(){
    when(taskRepository.findAll(any(Specification.class))).thenReturn(TASK_ENTITY_LIST);
    when(taskEntityMapper.fromEntity(TASK_ENTITY_LIST.get(0))).thenReturn(TASK1);
    List<Task> actual = taskService.getAll(Optional.empty(), Optional.of(new StudentId(1L)), Optional.empty(), Optional.of(Status.STARTED));
    assertNotNull(actual);
    assertEquals(1, actual.size());
    assertEquals(TASK_LIST, actual);
  }
  @Test
  public void createCorrectTask() throws AssignmentNotFoundException, CourseNotFoundException, IncorrectAssignmentIdException {
    when(assignmentService.get(new AssignmentId(1L))).thenReturn(ASSIGNMENT1);
    when(courseService.getStudents(new CourseId(1L))).thenReturn(STUDENT_ID_SET);
    when(taskEntityMapper.toEntity(TASK1)).thenReturn(TASK_ENTITY1);
    when(taskRepository.saveAll(TASK_ENTITY_LIST)).thenReturn(TASK_ENTITY_LIST);
    when(taskEntityMapper.fromEntity(TASK_ENTITY1)).thenReturn(TASK1);
    List<Task> actual = taskService.createTasks(new AssignmentId(1L));
    assertNotNull(actual);
    assertEquals(1, actual.size());
    assertEquals(TASK_LIST, actual);
  }
  @Test
  public void createTaskNullAssignmentId(){
    assertThrows(IncorrectAssignmentIdException.class, () -> taskService.createTasks(null));
  }
  @Test
  @SneakyThrows({StudentNotFoundException.class, IncorrectUserIdException.class})
  public void failToProgressTestWithEmptyUrl(){
    User user = new User(new UserId(2L), Role.STUDENT,"test","test", true);
    Student student = new Student(new StudentId(2L),sigma.training.eum.student.dictionary.Status.ACTIVE,"Arsen",new UserId(1L));
    when(userService.getCurrentUser()).thenReturn(user);
    when(studentService.get(new UserId(2L))).thenReturn(student);
    when(taskRepository.findTaskEntityById(2L)).thenReturn(Optional.of(TASK_ENTITY2));
    when(taskEntityMapper.fromEntity(TASK_ENTITY2)).thenReturn(TASK2);
    assertThrows(IncorrectTaskUrlException.class,()->taskService.progress(new TaskId(2L),""));
  }
  @Test
  @SneakyThrows({StudentNotFoundException.class, IncorrectUserIdException.class})
  public void failToProgressTestWithIncorrectStatus(){
    User user = new User(new UserId(2L), Role.STUDENT,"test","test", true);
    Student student = new Student(new StudentId(3L),sigma.training.eum.student.dictionary.Status.ACTIVE,"Arsen",new UserId(1L));
    when(userService.getCurrentUser()).thenReturn(user);
    when(studentService.get(new UserId(2L))).thenReturn(student);
    when(taskRepository.findTaskEntityById(3L)).thenReturn(Optional.of(TASK_ENTITY3));
    when(taskEntityMapper.fromEntity(TASK_ENTITY3)).thenReturn(TASK3);
    assertThrows(IncorrectTaskStatusException.class,()->taskService.progress(new TaskId(3L),"4545"));
  }
  @Test
  @SneakyThrows({StudentNotFoundException.class, IncorrectUserIdException.class})
  public void failToProgressWithDeniedAccess(){
    User user = new User(new UserId(2L), Role.STUDENT,"test","test", true);
    Student student = new Student(new StudentId(3L),sigma.training.eum.student.dictionary.Status.ACTIVE,"Arsen",new UserId(1L));
    when(userService.getCurrentUser()).thenReturn(user);
    when(studentService.get(new UserId(2L))).thenReturn(student);
    when(taskRepository.findTaskEntityById(2L)).thenReturn(Optional.of(TASK_ENTITY2));
    when(taskEntityMapper.fromEntity(TASK_ENTITY2)).thenReturn(TASK2);
    assertThrows(AccessDeniedException.class,()->taskService.progress(new TaskId(2L),"url"));
    verify(userService).getCurrentUser();
    verify(studentService).get((UserId) any());
    verify(taskRepository).findTaskEntityById(any());
    verify(taskEntityMapper).fromEntity(any());
  }
  @Test
  @SneakyThrows({TaskNotFoundException.class, IncorrectTaskStatusException.class, IncorrectTaskUrlException.class, IncorrectTaskIdException.class, StudentNotFoundException.class, IncorrectUserIdException.class})
  public void progressSuccessfully() throws IncorrectStudentIdInTaskException {
    User user = new User(new UserId(2L), Role.STUDENT,"test","test", true);
    Student student = new Student(new StudentId(2L),sigma.training.eum.student.dictionary.Status.ACTIVE,"Arsen",new UserId(1L));
    when(userService.getCurrentUser()).thenReturn(user);
    when(studentService.get(new UserId(2L))).thenReturn(student);
    when(taskRepository.findTaskEntityById(2L)).thenReturn(Optional.of(TASK_ENTITY2));
    when(taskEntityMapper.fromEntity(TASK_ENTITY2)).thenReturn(TASK2);
    Task updatedTask = new Task(TASK2.taskId(),Status.INREVIEW,"URL",TASK2.assignmentId(),TASK2.studentId(),TASK2.mark(), TASK2.returnReason());
    TaskEntity updatedTaskEntity = new TaskEntity();
    updatedTaskEntity.setId(2L);
    updatedTaskEntity.setStatus(Status.INREVIEW);
    updatedTaskEntity.setCompletedTaskUrl("URL");
    updatedTaskEntity.setAssignmentId(2L);
    updatedTaskEntity.setStudentId(2L);
    when(taskEntityMapper.toEntity(any(Task.class))).thenReturn(updatedTaskEntity);
    when(taskRepository.save(updatedTaskEntity)).thenReturn(updatedTaskEntity);
    when(taskEntityMapper.fromEntity(updatedTaskEntity)).thenReturn(updatedTask);
    assertEquals(updatedTask,taskService.progress(new TaskId(2L),"URL"));
  }
  @Test
  @SneakyThrows({TutorNotFoundException.class,AssignmentNotFoundException.class, CourseNotFoundException.class})
  public void failToReturnTaskWithIncorrectTutor(){
    Tutor tutor = new Tutor(new TutorId(3L), sigma.training.eum.tutor.dictionary.Status.ACTIVE,"Tutor", new UserId(2L));
    Assignment assignment = new Assignment(new AssignmentId(3L), sigma.training.eum.assignment.dictionary.Status.STARTED,"",new CourseId(1L),null,null,null);
    Course course = new Course(new CourseId(1L), sigma.training.eum.course.dictionary.Status.STARTED,"Course",new TutorId(2L),null,null,null);
    when(tutorService.requireCurrentUserAsTutor()).thenReturn(tutor);
    when(assignmentService.get(any())).thenReturn(assignment);
    when(courseService.get(any())).thenReturn(course);
    when(taskRepository.findTaskEntityById(3L)).thenReturn(Optional.of(TASK_ENTITY3));
    when(taskEntityMapper.fromEntity(TASK_ENTITY3)).thenReturn(TASK3);
    assertThrows(InsufficientTutorAuthoritiesException.class,()->taskService.returnTask(new TaskId(3L),"reason"));
  }
  @Test
  public void failToReturnTaskWithNullReturnReason(){
    assertThrows(IncorrectTaskReturnReasonException.class,()->taskService.returnTask(new TaskId(2L),null),"Return reason cannot be null!");
  }
  @Test
  public void failToReturnTaskWithEmptyReturnReason(){
    assertThrows(IncorrectTaskReturnReasonException.class,()->taskService.returnTask(new TaskId(2L),""),"Return reason cannot be empty!");
  }
  @Test
  @SneakyThrows({TaskNotFoundException.class, TutorNotFoundException.class, IncorrectTaskStatusException.class, IncorrectTaskIdException.class, IncorrectTaskReturnReasonException.class, CourseNotFoundException.class, InsufficientTutorAuthoritiesException.class, AssignmentNotFoundException.class})
  public void returnTaskSuccessfully(){
    Tutor tutor = new Tutor(new TutorId(2L), sigma.training.eum.tutor.dictionary.Status.ACTIVE,"Tutor", new UserId(2L));
    Assignment assignment = new Assignment(new AssignmentId(3L), sigma.training.eum.assignment.dictionary.Status.STARTED,"",new CourseId(1L),null,null,null);
    Course course = new Course(new CourseId(1L), sigma.training.eum.course.dictionary.Status.STARTED,"Course",new TutorId(2L),null,null,null);
    when(tutorService.requireCurrentUserAsTutor()).thenReturn(tutor);
    when(assignmentService.get(any())).thenReturn(assignment);
    when(courseService.get(any())).thenReturn(course);
    when(taskRepository.findTaskEntityById(3L)).thenReturn(Optional.of(TASK_ENTITY3));
    when(taskEntityMapper.fromEntity(TASK_ENTITY3)).thenReturn(TASK3);
    Task updatedTask = new Task(TASK3.taskId(),Status.STARTED,TASK3.completedTaskUrl(),TASK3.assignmentId(),TASK3.studentId(),TASK3.mark(),"reason");
    TaskEntity updatedTaskEntity = new TaskEntity();
    updatedTaskEntity.setId(TASK3.taskId().value());
    updatedTaskEntity.setStatus(Status.STARTED);
    updatedTaskEntity.setCompletedTaskUrl(TASK3.completedTaskUrl());
    updatedTaskEntity.setAssignmentId(TASK3.assignmentId().value());
    updatedTaskEntity.setStudentId(TASK3.studentId().value());
    updatedTaskEntity.setMark(TASK3.mark());
    updatedTaskEntity.setReturnReason(TASK3.returnReason());
    when(taskEntityMapper.toEntity(any(Task.class))).thenReturn(updatedTaskEntity);
    when(taskRepository.save(updatedTaskEntity)).thenReturn(updatedTaskEntity);
    when(taskEntityMapper.fromEntity(updatedTaskEntity)).thenReturn(updatedTask);
    assertEquals(updatedTask,taskService.returnTask(new TaskId(3L),"reason"));
  }
  @SneakyThrows({sigma.training.eum.tutor.exception.IncorrectUserIdException.class, TutorNotFoundException.class, IncorrectMarkValueException.class,
    TaskNotFoundException.class, IncorrectTaskStatusException.class, IncorrectTaskIdException.class})
  @Test
  public void correctTaskFinish(){
    User user = new User(new UserId(1L), Role.STUDENT,"test","test", true);
    Tutor tutor = new Tutor(new TutorId(3L), sigma.training.eum.tutor.dictionary.Status.ACTIVE, "Sofia", new UserId(1L));
    when(userService.getCurrentUser()).thenReturn(user);
    when(tutorService.get(new UserId(1L))).thenReturn(tutor);
    when(taskRepository.findTaskEntityById(3L)).thenReturn(Optional.of(TASK_ENTITY3));
    when(taskEntityMapper.fromEntity(TASK_ENTITY3)).thenReturn(TASK3);
    when(taskRepository.findAll(any(Specification.class))).thenReturn(TASK_ENTITY_LIST2);
    when(taskEntityMapper.fromEntity(TASK_ENTITY_LIST2.get(0))).thenReturn(TASK3);
    Task updatedTask = new Task(TASK3.taskId(),Status.INREVIEW,TASK3.completedTaskUrl(),TASK3.assignmentId(),TASK3.studentId(),50, "Reason");
    TaskEntity updatedTaskEntity = new TaskEntity();
    updatedTaskEntity.setId(3L);
    updatedTaskEntity.setStatus(Status.INREVIEW);
    updatedTaskEntity.setAssignmentId(3L);
    updatedTaskEntity.setStudentId(3L);
    updatedTaskEntity.setMark(50);
    when(taskEntityMapper.toEntity(any(Task.class))).thenReturn(updatedTaskEntity);
    when(taskRepository.save(updatedTaskEntity)).thenReturn(updatedTaskEntity);
    when(taskEntityMapper.fromEntity(updatedTaskEntity)).thenReturn(updatedTask);
    assertEquals(updatedTask, taskService.finish(new TaskId(3L), 50));
  }
  @SneakyThrows({sigma.training.eum.tutor.exception.IncorrectUserIdException.class, TutorNotFoundException.class})
  @Test
  public void failToFinishTaskWhichDoesNotBelongToThisTutor(){
    User user = new User(new UserId(1L), Role.STUDENT,"test","test", true);
    Tutor tutor = new Tutor(new TutorId(3L), sigma.training.eum.tutor.dictionary.Status.ACTIVE, "Sofia", new UserId(1L));
    when(userService.getCurrentUser()).thenReturn(user);
    when(tutorService.get(new UserId(1L))).thenReturn(tutor);
    when(taskRepository.findTaskEntityById(3L)).thenReturn(Optional.of(TASK_ENTITY3));
    when(taskEntityMapper.fromEntity(TASK_ENTITY3)).thenReturn(TASK3);
    assertThrows(IncorrectStudentIdInTaskException.class, () -> taskService.finish(new TaskId(3L), 50));
  }
  @Test
  public void failToFinishTaskWithIncorrectStatus() throws sigma.training.eum.tutor.exception.IncorrectUserIdException, TutorNotFoundException {
    User user = new User(new UserId(1L), Role.STUDENT,"test","test", true);
    Tutor tutor = new Tutor(new TutorId(3L), sigma.training.eum.tutor.dictionary.Status.ACTIVE, "Sofia", new UserId(1L));
    when(userService.getCurrentUser()).thenReturn(user);
    when(tutorService.get(new UserId(1L))).thenReturn(tutor);
    when(taskRepository.findTaskEntityById(1L)).thenReturn(Optional.of(TASK_ENTITY1));
    when(taskEntityMapper.fromEntity(TASK_ENTITY1)).thenReturn(TASK1);
    when(taskRepository.findAll(any(Specification.class))).thenReturn(TASK_ENTITY_LIST);
    when(taskEntityMapper.fromEntity(TASK_ENTITY_LIST.get(0))).thenReturn(TASK1);
    assertThrows(IncorrectTaskStatusException.class, () -> taskService.finish(new TaskId(1L), 50));
  }
  @Test
  public void failToFinishTaskWithIncorrectMark() throws sigma.training.eum.tutor.exception.IncorrectUserIdException, TutorNotFoundException {
    User user = new User(new UserId(1L), Role.STUDENT,"test","test", true);
    Tutor tutor = new Tutor(new TutorId(3L), sigma.training.eum.tutor.dictionary.Status.ACTIVE, "Sofia", new UserId(1L));
    when(userService.getCurrentUser()).thenReturn(user);
    when(tutorService.get(new UserId(1L))).thenReturn(tutor);
    when(taskRepository.findTaskEntityById(3L)).thenReturn(Optional.of(TASK_ENTITY3));
    when(taskEntityMapper.fromEntity(TASK_ENTITY3)).thenReturn(TASK3);
    when(taskRepository.findAll(any(Specification.class))).thenReturn(TASK_ENTITY_LIST2);
    when(taskEntityMapper.fromEntity(TASK_ENTITY_LIST2.get(0))).thenReturn(TASK3);
    assertThrows(IncorrectMarkValueException.class, () -> taskService.finish(new TaskId(3L), 500));
  }
}
