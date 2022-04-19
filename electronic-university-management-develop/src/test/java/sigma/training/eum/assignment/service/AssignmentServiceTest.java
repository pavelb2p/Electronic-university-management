package sigma.training.eum.assignment.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import sigma.training.eum.assignment.dictionary.Status;
import sigma.training.eum.assignment.exception.*;
import sigma.training.eum.assignment.mapper.entity.AssignmentEntityMapper;
import sigma.training.eum.assignment.persistence.entity.AssignmentEntity;
import sigma.training.eum.assignment.persistence.repository.AssignmentRepository;
import sigma.training.eum.assignment.service.type.Assignment;
import sigma.training.eum.assignment.service.type.AssignmentId;
import sigma.training.eum.assignment.service.validator.AssignmentValidator;
import sigma.training.eum.course.exception.CourseNotFoundException;
import sigma.training.eum.course.exception.IncorrectCourseStatusException;
import sigma.training.eum.course.exception.NotExistingCourseIdException;
import sigma.training.eum.course.persistence.entity.CourseEntity;
import sigma.training.eum.course.service.CourseService;
import sigma.training.eum.course.service.type.Course;
import sigma.training.eum.course.service.type.CourseId;
import sigma.training.eum.student.exception.StudentNotFoundException;
import sigma.training.eum.student.service.type.StudentId;
import sigma.training.eum.task.exception.IncorrectTaskStatusException;
import sigma.training.eum.task.mapper.entity.TaskEntityMapper;
import sigma.training.eum.task.persistence.entity.TaskEntity;
import sigma.training.eum.task.persistence.repository.TaskRepository;
import sigma.training.eum.task.service.TaskService;
import sigma.training.eum.task.service.type.Task;
import sigma.training.eum.task.service.type.TaskId;
import sigma.training.eum.tutor.exception.IncorrectUserIdException;
import sigma.training.eum.tutor.exception.InsufficientTutorAuthoritiesException;
import sigma.training.eum.tutor.exception.TutorNotFoundException;
import sigma.training.eum.tutor.service.TutorService;
import sigma.training.eum.tutor.service.type.Tutor;
import sigma.training.eum.tutor.service.type.TutorId;
import sigma.training.eum.user.dictionary.Role;
import sigma.training.eum.user.service.UserService;
import sigma.training.eum.user.service.type.User;
import sigma.training.eum.user.service.type.UserId;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssignmentServiceTest {
  private static final AssignmentEntity ASSIGNMENT_ENTITY1 = new AssignmentEntity();
  private static final AssignmentEntity ASSIGNMENT_ENTITY2 = new AssignmentEntity();
  private static final CourseEntity COURSE_ENTITY1 = new CourseEntity();
  private static final Assignment ASSIGNMENT1 = new Assignment(new AssignmentId(1L), Status.CREATED, "Something", new CourseId(1L), new Timestamp(System.currentTimeMillis()), null, null);
  private static final Assignment ASSIGNMENT2 = new Assignment(new AssignmentId(2L), Status.STARTED, "Something", new CourseId(2L), new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), null);
  static{
    ASSIGNMENT_ENTITY1.setId(1L);
    ASSIGNMENT_ENTITY1.setStatus(Status.CREATED);
    ASSIGNMENT_ENTITY1.setCourseId(1L);
    ASSIGNMENT_ENTITY1.setStartDate(null);
    ASSIGNMENT_ENTITY1.setEndDate(null);
    ASSIGNMENT_ENTITY1.setCreationDate(new Timestamp(System.currentTimeMillis()));
    ASSIGNMENT_ENTITY1.setDescription("Something");
    ASSIGNMENT_ENTITY2.setId(2L);
    ASSIGNMENT_ENTITY2.setStatus(Status.STARTED);
    ASSIGNMENT_ENTITY2.setCourseId(1L);
    ASSIGNMENT_ENTITY2.setStartDate(new Timestamp(System.currentTimeMillis()));
    ASSIGNMENT_ENTITY2.setEndDate(null);
    ASSIGNMENT_ENTITY2.setCreationDate(new Timestamp(System.currentTimeMillis()));
    ASSIGNMENT_ENTITY2.setDescription("Something");
    COURSE_ENTITY1.setId(1L);
    COURSE_ENTITY1.setStatus(sigma.training.eum.course.dictionary.Status.STARTED);
    COURSE_ENTITY1.setStartDate(null);
    COURSE_ENTITY1.setEndDate(null);
    COURSE_ENTITY1.setName("Some name");
    COURSE_ENTITY1.setStatus(sigma.training.eum.course.dictionary.Status.STARTED);
    COURSE_ENTITY1.setTutorId(1L);
  }
  private static final List<AssignmentEntity> ASSIGNMENT_ENTITY_LIST = Collections.singletonList(ASSIGNMENT_ENTITY1);
  private static final List<Assignment> ASSIGNMENT_LIST = Collections.singletonList(ASSIGNMENT1);
  @InjectMocks
  private AssignmentService assignmentService;
  @Mock
  private AssignmentRepository assignmentRepository;
  @Mock
  private AssignmentEntityMapper assignmentEntityMapper;
  @Mock
  private TaskEntityMapper taskEntityMapper;
  @Mock
  private CourseService courseService;
  @Mock
  private AssignmentValidator assignmentValidator;
  @Mock
  private UserService userService;
  @Mock
  private TutorService tutorService;
  @Mock
  private TaskRepository taskRepository;
  @Mock
  private TaskService taskService;
  @Test
  @SneakyThrows({AssignmentNotFoundException.class})
  public void getTest(){
    when(assignmentRepository.findAssignmentEntityById(1L)).thenReturn(Optional.of(ASSIGNMENT_ENTITY1));
    when(assignmentEntityMapper.fromEntity(ASSIGNMENT_ENTITY_LIST.get(0))).thenReturn(ASSIGNMENT1);
    Assignment actual = assignmentService.get(new AssignmentId(1L));
    assertNotNull(actual);
    assertEquals(ASSIGNMENT1, actual);
    verify(assignmentRepository).findAssignmentEntityById(1L);
    verify(assignmentEntityMapper).fromEntity(any());
  }
  @Test
  public void failToGetWithNullAssignmentId(){
    assertThrows(IllegalArgumentException.class, () -> assignmentService.get(null));
  }
  @Test
  public void failToGetWithNotExistingAssignmentId() {
    when(assignmentRepository.findAssignmentEntityById(1L)).thenReturn(Optional.empty());
    assertThrows(AssignmentNotFoundException.class, () -> assignmentService.get(new AssignmentId(1L)));
  }
  @Test
  public void getAllTest() {
    when(assignmentRepository.findAll(any(Specification.class))).thenReturn(ASSIGNMENT_ENTITY_LIST);
    when(assignmentEntityMapper.fromEntity(ASSIGNMENT_ENTITY_LIST.get(0))).thenReturn(ASSIGNMENT1);
    List<Assignment> actual = assignmentService.getAll(Optional.of(new TutorId(1L)), Optional.of(new CourseId(1L)), Optional.of(Status.CREATED));
    assertNotNull(actual);
    assertEquals(1, actual.size());
    assertEquals(ASSIGNMENT_LIST, actual);
  }
  @Test
  @SneakyThrows({CourseNotFoundException.class, IncorrectAssignmentDescriptionException.class, IncorrectCourseStatusException.class, NotExistingCourseIdException.class, IncorrectAssignmentTimestampException.class, IncorrectAssignmentIdException.class, IncorrectAssignmentStatusException.class, TutorNotFoundException.class})
  public void createTest(){
    Assignment updatedAssignment = new Assignment(null, Status.CREATED, ASSIGNMENT1.description(), ASSIGNMENT1.courseId(),null,null, null);
    Course course = new Course(new CourseId(1L), sigma.training.eum.course.dictionary.Status.STARTED,"Some Name", new TutorId(1L), null, null, null);
    Tutor tutor = new Tutor(new TutorId(1L), sigma.training.eum.tutor.dictionary.Status.ACTIVE, "Levan Goroziya", new UserId(2L));
    AssignmentEntity updatedAssignmentEntity = new AssignmentEntity();
    updatedAssignmentEntity.setId(null);
    updatedAssignmentEntity.setStatus(Status.CREATED);
    updatedAssignmentEntity.setDescription(ASSIGNMENT1.description());
    updatedAssignmentEntity.setCourseId(ASSIGNMENT1.courseId().value());
    when(tutorService.requireCurrentUserAsTutor()).thenReturn(tutor);
    when(courseService.get(ASSIGNMENT1.courseId())).thenReturn(course);
    when(assignmentEntityMapper.toEntity(updatedAssignment)).thenReturn(updatedAssignmentEntity);
    when(assignmentRepository.save(updatedAssignmentEntity)).thenReturn(updatedAssignmentEntity);
    when(assignmentEntityMapper.fromEntity(updatedAssignmentEntity)).thenReturn(updatedAssignment);
    assertNotNull(updatedAssignment);
    assertNotNull(updatedAssignmentEntity);
    assertEquals(assignmentService.create(ASSIGNMENT1),updatedAssignment);
  }
  @Test
  @SneakyThrows({CourseNotFoundException.class, TutorNotFoundException.class})
  public void failToCreateAnAssignmentWithIncorrectTutor(){
    Course course = new Course(new CourseId(1L), sigma.training.eum.course.dictionary.Status.STARTED,"Some Name", new TutorId(1L), null, null, null);
    Tutor tutor = new Tutor(new TutorId(2L), sigma.training.eum.tutor.dictionary.Status.SUSPENDED, "Levan Goroziya", new UserId(2L));
    when(tutorService.requireCurrentUserAsTutor()).thenReturn(tutor);
    when(courseService.get(ASSIGNMENT1.courseId())).thenReturn(course);
    assertThrows(AccessDeniedException.class, ()->assignmentService.create(ASSIGNMENT1));
  }
  @Test
  public void failToCreateAnAssignmentWithInappropriateCourseStatus() throws CourseNotFoundException {
    Course course = new Course(new CourseId(1L), sigma.training.eum.course.dictionary.Status.FINISHED,"Some Name", new TutorId(1L), null, null, null);
    when(courseService.get(ASSIGNMENT1.courseId())).thenReturn(course);
    assertThrows(IncorrectCourseStatusException.class, () -> assignmentService.create(ASSIGNMENT1));
  }
  @Test
  public void failToStartAnAssignmentWithNotExistingAssignmentId() {
    when(assignmentRepository.findAssignmentEntityById(1L)).thenReturn(Optional.empty());
    assertThrows(AssignmentNotFoundException.class, () -> assignmentService.start(ASSIGNMENT1.assignmentId()));
    verify(assignmentRepository).findAssignmentEntityById(1L);
  }
  @Test
  @SneakyThrows(CourseNotFoundException.class)
  public void failToStartAnAssignmentWithNotExistingCourseId(){
    when(assignmentRepository.findAssignmentEntityById(1L)).thenReturn(Optional.of(ASSIGNMENT_ENTITY1));
    when(assignmentEntityMapper.fromEntity(ASSIGNMENT_ENTITY1)).thenReturn(ASSIGNMENT1);
    when(courseService.get(new CourseId(1L))).thenReturn(null);
    assertThrows(NotExistingCourseIdException.class, () -> assignmentService.start(ASSIGNMENT1.assignmentId()));
    verify(assignmentRepository).findAssignmentEntityById(any());
    verify(assignmentEntityMapper).fromEntity(any());
    verify(courseService).get(any());
  }
  @Test
  public void failToStartAnAssignmentWithInappropriateCourseStatus() throws CourseNotFoundException {
    Course course = new Course(new CourseId(1L), sigma.training.eum.course.dictionary.Status.FINISHED,"Some Name", new TutorId(1L), null, null, null);
    when(courseService.get(ASSIGNMENT1.courseId())).thenReturn(course);
    assertThrows(IncorrectCourseStatusException.class, () -> assignmentService.create(ASSIGNMENT1));
  }
  @Test
  @SneakyThrows({CourseNotFoundException.class, IncorrectAssignmentDescriptionException.class, IncorrectCourseStatusException.class, NotExistingCourseIdException.class, IncorrectAssignmentTimestampException.class, IncorrectAssignmentIdException.class, IncorrectAssignmentStatusException.class, AssignmentNotFoundException.class})
  public void startTest(){
    Assignment updatedAssignment = new Assignment(ASSIGNMENT1.assignmentId(), Status.STARTED, ASSIGNMENT1.description(), ASSIGNMENT1.courseId(),ASSIGNMENT1.creationDate(),null, null);
    Course course = new Course(new CourseId(1L), sigma.training.eum.course.dictionary.Status.STARTED,"Some Name", new TutorId(1L), null, null, null);
    AssignmentEntity updatedAssignmentEntity = new AssignmentEntity();
    updatedAssignmentEntity.setId(ASSIGNMENT1.assignmentId().value());
    updatedAssignmentEntity.setStatus(Status.STARTED);
    updatedAssignmentEntity.setDescription(ASSIGNMENT1.description());
    updatedAssignmentEntity.setCourseId(ASSIGNMENT1.courseId().value());
    updatedAssignmentEntity.setCreationDate(ASSIGNMENT1.creationDate());
    when(assignmentRepository.findAssignmentEntityById(1L)).thenReturn(Optional.of(ASSIGNMENT_ENTITY1));
    when(assignmentEntityMapper.fromEntity(ASSIGNMENT_ENTITY1)).thenReturn(ASSIGNMENT1);
    when(courseService.get(new CourseId(1L))).thenReturn(course);
    when(assignmentEntityMapper.toEntity(any(Assignment.class))).thenReturn(updatedAssignmentEntity);
    when(assignmentRepository.save(updatedAssignmentEntity)).thenReturn(updatedAssignmentEntity);
    when(assignmentEntityMapper.fromEntity(updatedAssignmentEntity)).thenReturn(updatedAssignment);
    assertEquals(updatedAssignment,assignmentService.start(ASSIGNMENT1.assignmentId()));
  }
  @Test
  @SneakyThrows({IncorrectUserIdException.class, TutorNotFoundException.class})
  public void getCurrentUserAssignmentsTest(){
    User testUser = new User(new UserId(1L), Role.TUTOR,"tutor","tutor", true);
    Tutor testTutor = new Tutor(new TutorId(1L), sigma.training.eum.tutor.dictionary.Status.ACTIVE,"Test Tutor", new UserId(1L));
    when(assignmentRepository.findAll(any(Specification.class))).thenReturn(ASSIGNMENT_ENTITY_LIST);
    when(assignmentEntityMapper.fromEntity(ASSIGNMENT_ENTITY_LIST.get(0))).thenReturn(ASSIGNMENT1);
    when(tutorService.requireCurrentUserAsTutor()).thenReturn(testTutor);
    List<Assignment> actual = assignmentService.getCurrentUserAssignments(Status.CREATED);
    assertEquals(1, actual.size());
    assertEquals(ASSIGNMENT_LIST, actual);
  }
  @Test
  @SneakyThrows({TutorNotFoundException.class,CourseNotFoundException.class})
  public void failToFinishAssignmentWithIncorrectTutor(){
    Tutor tutor = new Tutor(new TutorId(3L), sigma.training.eum.tutor.dictionary.Status.ACTIVE,"Tutor", new UserId(2L));
    Course course = new Course(new CourseId(2L), sigma.training.eum.course.dictionary.Status.STARTED,"Course",new TutorId(2L),null,null,null);
    when(tutorService.requireCurrentUserAsTutor()).thenReturn(tutor);
    when(assignmentRepository.findAssignmentEntityById(2L)).thenReturn(Optional.of(ASSIGNMENT_ENTITY2));
    when(assignmentEntityMapper.fromEntity(ASSIGNMENT_ENTITY2)).thenReturn(ASSIGNMENT2);
    when(courseService.get(any())).thenReturn(course);
    assertThrows(InsufficientTutorAuthoritiesException.class,()->assignmentService.finish(new AssignmentId(2L)));
  }
  @Test
  @SneakyThrows({TutorNotFoundException.class,CourseNotFoundException.class})
  public void failToFinishAssignmentWithIncorrectTaskStatus(){
    Tutor tutor = new Tutor(new TutorId(2L), sigma.training.eum.tutor.dictionary.Status.ACTIVE,"Tutor", new UserId(2L));
    Course course = new Course(new CourseId(1L), sigma.training.eum.course.dictionary.Status.STARTED,"Course",new TutorId(2L),null,null,null);
    Task task = new Task(new TaskId(1L), sigma.training.eum.task.dictionary.Status.INREVIEW, null, new AssignmentId(1L), new StudentId(3L), null, null);
    TaskEntity taskEntity = new TaskEntity();
    taskEntity.setId(3L);
    taskEntity.setStatus(sigma.training.eum.task.dictionary.Status.INREVIEW);
    taskEntity.setAssignmentId(2L);
    taskEntity.setStudentId(3L);
    when(tutorService.requireCurrentUserAsTutor()).thenReturn(tutor);
    when(assignmentRepository.findAssignmentEntityById(2L)).thenReturn(Optional.of(ASSIGNMENT_ENTITY2));
    when(assignmentEntityMapper.fromEntity(ASSIGNMENT_ENTITY2)).thenReturn(ASSIGNMENT2);
    when(courseService.get(any())).thenReturn(course);
    when(taskService.getAll(any(),any(),any(),any())).thenReturn(Collections.singletonList(task));
    assertThrows(IncorrectTaskStatusException.class,()->assignmentService.finish(new AssignmentId(2L)));
  }
  @Test
  @SneakyThrows({TutorNotFoundException.class,CourseNotFoundException.class})
  public void failToFinishAssignmentWithIncorrectAssignmentStatus() {
    Tutor tutor = new Tutor(new TutorId(2L), sigma.training.eum.tutor.dictionary.Status.ACTIVE,"Tutor", new UserId(2L));
    Course course = new Course(new CourseId(1L), sigma.training.eum.course.dictionary.Status.STARTED,"Course",new TutorId(2L),null,null,null);
    when(tutorService.requireCurrentUserAsTutor()).thenReturn(tutor);
    when(assignmentRepository.findAssignmentEntityById(2L)).thenReturn(Optional.of(ASSIGNMENT_ENTITY1));
    when(assignmentEntityMapper.fromEntity(ASSIGNMENT_ENTITY1)).thenReturn(ASSIGNMENT1);
    when(courseService.get(any())).thenReturn(course);
    assertThrows(IncorrectAssignmentStatusException.class,()->assignmentService.finish(new AssignmentId(2L)));
  }
  @Test
  @SneakyThrows({TutorNotFoundException.class,CourseNotFoundException.class, IncorrectTaskStatusException.class, InsufficientTutorAuthoritiesException.class, AssignmentNotFoundException.class})
  public void finishAssignmentSuccessfully() {
    Tutor tutor = new Tutor(new TutorId(2L), sigma.training.eum.tutor.dictionary.Status.ACTIVE,"Tutor", new UserId(2L));
    Course course = new Course(new CourseId(1L), sigma.training.eum.course.dictionary.Status.STARTED,"Course",new TutorId(2L),null,null,null);
    when(tutorService.requireCurrentUserAsTutor()).thenReturn(tutor);
    when(assignmentRepository.findAssignmentEntityById(2L)).thenReturn(Optional.of(ASSIGNMENT_ENTITY2));
    when(assignmentEntityMapper.fromEntity(ASSIGNMENT_ENTITY2)).thenReturn(ASSIGNMENT2);
    when(courseService.get(any())).thenReturn(course);
    when(taskService.getAll(any(),any(),any(),any())).thenReturn(Collections.emptyList());
    Assignment updatedAssignment = new Assignment(ASSIGNMENT2.assignmentId(),Status.FINISHED,ASSIGNMENT2.description(),ASSIGNMENT2.courseId(),ASSIGNMENT2.creationDate(),ASSIGNMENT2.startDate(),new Timestamp(System.currentTimeMillis()));
    AssignmentEntity updatedAssignmentEntity = new AssignmentEntity();
    updatedAssignmentEntity.setId(ASSIGNMENT_ENTITY2.getId());
    updatedAssignmentEntity.setStatus(Status.FINISHED);
    updatedAssignmentEntity.setDescription(ASSIGNMENT_ENTITY2.getDescription());
    updatedAssignmentEntity.setCourseId(ASSIGNMENT_ENTITY2.getCourseId());
    updatedAssignmentEntity.setCreationDate(ASSIGNMENT_ENTITY2.getCreationDate());
    updatedAssignmentEntity.setStartDate(ASSIGNMENT_ENTITY2.getStartDate());
    updatedAssignmentEntity.setEndDate(updatedAssignment.endDate());
    when(assignmentEntityMapper.toEntity(any(Assignment.class))).thenReturn(updatedAssignmentEntity);
    when(assignmentRepository.save(updatedAssignmentEntity)).thenReturn(updatedAssignmentEntity);
    when(assignmentEntityMapper.fromEntity(updatedAssignmentEntity)).thenReturn(updatedAssignment);
    assertEquals(updatedAssignment,assignmentService.finish(new AssignmentId(2L)));
  }
}
