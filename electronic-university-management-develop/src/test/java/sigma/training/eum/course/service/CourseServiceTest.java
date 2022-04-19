package sigma.training.eum.course.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import sigma.training.eum.assignment.service.AssignmentService;
import sigma.training.eum.assignment.service.type.Assignment;
import sigma.training.eum.assignment.service.type.AssignmentId;
import sigma.training.eum.course.dictionary.Status;
import sigma.training.eum.course.exception.*;
import sigma.training.eum.course.mapper.entity.CourseEntityMapper;
import sigma.training.eum.course.persistence.entity.CourseEntity;
import sigma.training.eum.course.persistence.repository.CourseRepository;
import sigma.training.eum.course.service.type.Course;
import sigma.training.eum.course.service.type.CourseId;
import sigma.training.eum.course.service.validator.CourseValidator;
import sigma.training.eum.student.exception.StudentNotFoundException;
import sigma.training.eum.student.service.StudentService;
import sigma.training.eum.student.service.type.Student;
import sigma.training.eum.student.service.type.StudentId;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
  private static final CourseEntity COURSE_ENTITY1 = new CourseEntity();
  private static final CourseEntity COURSE_ENTITY2 = new CourseEntity();
  private static final CourseEntity COURSE_ENTITY3 = new CourseEntity();
  private static final CourseEntity COURSE_ENTITY4 = new CourseEntity();
  private static final CourseEntity COURSE_ENTITY5 = new CourseEntity();
  private static final CourseEntity COURSE_ENTITY6 = new CourseEntity();

  private static final Course COURSE1 = new Course(new CourseId(1L), Status.CREATED, "Course", new TutorId(1L), new Timestamp(System.currentTimeMillis()), null, null);
  private static final Course COURSE2 = new Course(new CourseId(1L), Status.CREATED, "Course", null, new Timestamp(System.currentTimeMillis()), null, null);
  private static final Course COURSE3 = new Course(new CourseId(1L), Status.CREATED, "Course", new TutorId(300L), new Timestamp(System.currentTimeMillis()), null, null);
  private static final Course COURSE4 = new Course(new CourseId(1L), Status.STARTED, "Course", new TutorId(1L), new Timestamp(System.currentTimeMillis()), null, null);
  private static final Course COURSE5 = new Course(new CourseId(1L), Status.CREATED, "Course", new TutorId(1L), null, null, null);

  private static final Course COURSE_FINISHED_ASSIGNMENTS = new Course(new CourseId(1L), Status.FINISHED, "Course", new TutorId(1L), new Timestamp(System.currentTimeMillis()), null, null);

  private static final Assignment ASSIGNMENT_FINISHED1 = new Assignment(new AssignmentId(1L), sigma.training.eum.assignment.dictionary.Status.FINISHED, "Test",  new CourseId(1L), new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), null);
  private static final Assignment ASSIGNMENT_FINISHED2 = new Assignment(new AssignmentId(1L), sigma.training.eum.assignment.dictionary.Status.FINISHED, "Test",  new CourseId(1L), new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), null);
  private static final Assignment ASSIGNMENT_STARTED3 = new Assignment(new AssignmentId(1L), sigma.training.eum.assignment.dictionary.Status.CREATED, "Test",  new CourseId(1L), new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), null);


  private static final List<Assignment> FINISHED_ASSIGNMENTS_LIST = new ArrayList<>(Arrays.asList(ASSIGNMENT_FINISHED1, ASSIGNMENT_FINISHED2));
  private static final List<Assignment> NOT_ALL_FINISHED_ASSIGNMENT_LIST = new ArrayList<>(List.of(ASSIGNMENT_STARTED3));

  private static final Student STUDENT = new Student(new StudentId(1L), sigma.training.eum.student.dictionary.Status.SUSPENDED, "Student", new UserId(2L));
  private static final Student STUDENT2 = new Student(new StudentId(2L), sigma.training.eum.student.dictionary.Status.ACTIVE, "Student", new UserId(3L));
  private static final Student STUDENT3 = new Student(new StudentId(3L), sigma.training.eum.student.dictionary.Status.ACTIVE, "Student", new UserId(4L));
  private static final Student STUDENT4 = new Student(null, null, "Student", new UserId(4L));
  private static final Set<Long> STUDENT_SET = new HashSet<>();
  private static final Set<Long> STUDENT_SET2 = new HashSet<>();

  private static final List<Course> COURSE_LIST = Collections.singletonList(COURSE1);
  static{
    STUDENT_SET.add(2L);
    STUDENT_SET2.add(2L);
    COURSE_ENTITY1.setName("Course");
    COURSE_ENTITY1.setId(1L);
    COURSE_ENTITY1.setStatus(Status.CREATED);
    COURSE_ENTITY1.setTutorId(1L);
    COURSE_ENTITY1.setStartDate(new Timestamp(System.currentTimeMillis()));
    COURSE_ENTITY1.setEndDate(null);
    COURSE_ENTITY1.setCreateDate(null);
    COURSE_ENTITY1.setStudentIds(STUDENT_SET);
    COURSE_ENTITY2.setName("Course");
    COURSE_ENTITY2.setId(1L);
    COURSE_ENTITY2.setStatus(Status.STARTED);
    COURSE_ENTITY2.setTutorId(1L);
    COURSE_ENTITY2.setStartDate(new Timestamp(System.currentTimeMillis()));
    COURSE_ENTITY2.setEndDate(null);
    COURSE_ENTITY3.setName("Course");
    COURSE_ENTITY3.setId(3L);
    COURSE_ENTITY3.setStatus(Status.CREATED);
    COURSE_ENTITY3.setTutorId(1L);
    COURSE_ENTITY3.setStartDate(new Timestamp(System.currentTimeMillis()));
    COURSE_ENTITY3.setEndDate(null);
    COURSE_ENTITY3.setStudentIds(STUDENT_SET2);
    COURSE_ENTITY4.setName("Course");
    COURSE_ENTITY4.setId(3L);
    COURSE_ENTITY4.setStatus(null);
    COURSE_ENTITY4.setTutorId(1L);
    COURSE_ENTITY4.setStartDate(null);
    COURSE_ENTITY4.setEndDate(null);
    COURSE_ENTITY4.setStudentIds(STUDENT_SET2);
    COURSE_ENTITY5.setName("Course");
    COURSE_ENTITY5.setId(3L);
    COURSE_ENTITY5.setStatus(Status.CREATED);
    COURSE_ENTITY5.setTutorId(1L);
    COURSE_ENTITY5.setStartDate(null);
    COURSE_ENTITY5.setEndDate(null);
    COURSE_ENTITY5.setStudentIds(new HashSet<>());
    COURSE_ENTITY6.setName("Course");
    COURSE_ENTITY6.setId(3L);
    COURSE_ENTITY6.setStatus(Status.CREATED);
    COURSE_ENTITY6.setTutorId(1L);
    COURSE_ENTITY6.setStartDate(null);
    COURSE_ENTITY6.setEndDate(null);
    COURSE_ENTITY6.setStudentIds(STUDENT_SET);
  }
  private static final List<CourseEntity> COURSE_ENTITY_LIST = Collections.singletonList(COURSE_ENTITY1);
  @InjectMocks
  private CourseService courseService;
  @Mock
  private CourseRepository courseRepository;
  @Mock
  private CourseEntityMapper courseEntityMapper;
  @Mock
  private TutorService tutorService;
  @Mock
  private StudentService studentService;
  @Mock
  private UserService userService;
  @Mock
  private CourseValidator courseValidator;
  @Mock
  private AssignmentService assignmentService;

  @Test
  public void getAllTest() {
    when(courseRepository.getCourseEntitiesByStudentIdsContaining(1L)).thenReturn(COURSE_ENTITY_LIST);
    when(courseRepository.findAll(any(Specification.class))).thenReturn(COURSE_ENTITY_LIST);
    when(courseEntityMapper.fromEntity(COURSE_ENTITY_LIST.get(0))).thenReturn(COURSE1);
    List<Course> actual = courseService.getAll(Optional.of(Status.CREATED), Optional.of(1L), Optional.of(1L));
    assertNotNull(actual);
    assertEquals(1, actual.size());
    assertEquals(COURSE_LIST, actual);
  }
  @Test
  public void failToGetCourseByNullCourseId(){
    assertThrows(IllegalArgumentException.class,
      ()->courseService.get(null),"Id cannot be null!");
  }
  @Test
  public void failToGetCourseByIncorrectCourseId(){
    when(courseRepository.findCourseEntityById(25L))
      .thenReturn(Optional.empty());
    assertThrows(CourseNotFoundException.class,
      () -> courseService.get(new CourseId(25L)),
      "Course with id 25 was not found!");
    assertEquals(Optional.empty(),courseRepository.findCourseEntityById(25L));
    verify(courseRepository,times(2)).findCourseEntityById(any());
  }
  @Test
  @SneakyThrows(CourseNotFoundException.class)
  public void getCourseByIdSuccessfully()  {
    when(courseRepository.findCourseEntityById(1L)).thenReturn(Optional.of(COURSE_ENTITY1));
    when(courseEntityMapper.fromEntity(COURSE_ENTITY1)).thenReturn(COURSE1);
    assertEquals(courseService.get(new CourseId(1L)), COURSE1);
  }
  @Test
  @SneakyThrows({TutorNotFoundException.class, IncorrectUserIdException.class})
  public void failToCreateCourseByNullTutorId(){
    when(tutorService.get((TutorId) null)).thenThrow(IncorrectUserIdException.class);
    assertThrows(IncorrectCourseTutorException.class,()->courseService.create(COURSE2),"Cannot resolve a selected tutor!");
    verify(tutorService).get((TutorId) any());
  }
  @Test
  @SneakyThrows({TutorNotFoundException.class, IncorrectUserIdException.class})
  public void failToCreateCourseByIncorrectTutorId(){
    when(tutorService.get(new TutorId(300L))).thenThrow(TutorNotFoundException.class);
    assertThrows(IncorrectCourseTutorException.class,()->courseService.create(COURSE3),"Cannot resolve a selected tutor!");
    verify(tutorService).get((TutorId) any());
  }
  @Test
  @SneakyThrows({TutorNotFoundException.class, IncorrectUserIdException.class})
  public void failToCreateCourseByIncorrectTutorStatus(){
    Tutor tutor = new Tutor(new TutorId(1L),sigma.training.eum.tutor.dictionary.Status.SUSPENDED,"Arsen Ma", new UserId(1L));
    when(tutorService.get(new TutorId(1L))).thenReturn(tutor);
    assertThrows(IncorrectCourseTutorException.class,()->courseService.create(COURSE1),"Tutor's status should be only \"Active\"!");
    verify(tutorService).get((TutorId) any());
  }
  @SneakyThrows({IncorrectCourseStatusException.class,
    IncorrectCourseIdException.class,
    IncorrectCourseTimestampException.class,
    IncorrectCourseTutorException.class,
    IncorrectCourseNameException.class,
    TutorNotFoundException.class,
    IncorrectUserIdException.class})
  @Test
  public void createCourseSuccessfully() {
    Course updatedCourse = new Course(null, Status.CREATED, COURSE1.name(), COURSE1.tutorId(),null,null, null);
    Tutor tutor = new Tutor(new TutorId(1L),sigma.training.eum.tutor.dictionary.Status.ACTIVE,"Arsen Ma", new UserId(1L));
    CourseEntity updatedCourseEntity = new CourseEntity();
    updatedCourseEntity.setId(null);
    updatedCourseEntity.setStatus(Status.CREATED);
    updatedCourseEntity.setName(COURSE1.name());
    updatedCourseEntity.setTutorId(COURSE1.tutorId().value());
    updatedCourseEntity.setStartDate(null);
    updatedCourseEntity.setEndDate(null);
    when(tutorService.get(COURSE1.tutorId())).thenReturn(tutor);
    when(courseEntityMapper.toEntity(updatedCourse)).thenReturn(updatedCourseEntity);
    when(courseRepository.save(updatedCourseEntity)).thenReturn(updatedCourseEntity);
    when(courseEntityMapper.fromEntity(updatedCourseEntity)).thenReturn(updatedCourse);
    assertNotNull(updatedCourse);
    assertNotNull(updatedCourseEntity);
    assertEquals(courseService.create(COURSE1),updatedCourse);
    verify(tutorService).get((TutorId) any());
    verify(courseEntityMapper).toEntity(any());
    verify(courseRepository).save(any());
    verify(courseEntityMapper).fromEntity(any());
  }
  @Test
  public void failToAddStudentByNullCourseId(){
    assertThrows(IllegalArgumentException.class,()->courseService.addStudent(new StudentId(3L),null),"Course id cannot be null!");
  }
  @Test
  public void failToAddStudentByNullStudentId(){
    assertThrows(IllegalArgumentException.class,()->courseService.addStudent(null,new CourseId(3L)),"Student id cannot be null!");
  }
  @Test
  public void failToAddStudentByNonexistentCourseId(){
    when(courseRepository.findCourseEntityById(300L)).thenReturn(Optional.empty());
    assertThrows(CourseNotFoundException.class,
      ()->courseService.addStudent(new StudentId(1L),new CourseId(300L)),"Course with id 300 was not found!");
    verify(courseRepository).findCourseEntityById(any());
  }
  @Test
  @SneakyThrows(StudentNotFoundException.class)
  public void failToAddStudentByIncorrectCourseStatus(){
    when(courseRepository.findCourseEntityById(1L)).thenReturn(Optional.of(COURSE_ENTITY2));
    when(studentService.get(new StudentId(2L))).thenReturn(STUDENT2);
    when(courseEntityMapper.fromEntity(COURSE_ENTITY2)).thenReturn(COURSE4);
    assertThrows(IncorrectCourseStatusException.class,
      ()->courseService.addStudent(new StudentId(2L),new CourseId(1L)),"Course's status should be only \"Created\"!");
    verify(courseRepository).findCourseEntityById(any());
    verify(studentService).get((StudentId)any());
    verify(courseEntityMapper).fromEntity(any());
  }
  @Test
  @SneakyThrows(StudentNotFoundException.class)
  public void failToAddStudentByIncorrectStudentStatus(){
    when(courseRepository.findCourseEntityById(1L)).thenReturn(Optional.of(COURSE_ENTITY1));
    when(studentService.get(new StudentId(1L))).thenReturn(STUDENT);
    when(courseEntityMapper.fromEntity(COURSE_ENTITY1)).thenReturn(COURSE1);
    assertThrows(IncorrectCourseStudentException.class,
      ()->courseService.addStudent(new StudentId(1L),new CourseId(1L)),"Student's status should be only \"Active\"!");
    verify(courseRepository).findCourseEntityById(any());
    verify(studentService).get((StudentId) any());
    verify(courseEntityMapper).fromEntity(any());
  }
  @Test
  @SneakyThrows(StudentNotFoundException.class)
  public void failToAddStudentByRepeatingStudentId(){
    when(courseRepository.findCourseEntityById(1L)).thenReturn(Optional.of(COURSE_ENTITY1));
    when(studentService.get(new StudentId(2L))).thenReturn(STUDENT2);
    when(courseEntityMapper.fromEntity(COURSE_ENTITY1)).thenReturn(COURSE1);
    assertThrows(IncorrectCourseStudentException.class,()->courseService.addStudent(new StudentId(2L),new CourseId(1L)),"Student is already present!");
    verify(courseRepository,times(2)).findCourseEntityById(any());
    verify(studentService).get((StudentId) any());
    verify(courseEntityMapper).fromEntity(any());
  }
  @Test
  @SneakyThrows(StudentNotFoundException.class)
  public void addStudentSuccessfully(){
    when(courseRepository.findCourseEntityById(1L)).thenReturn(Optional.of(COURSE_ENTITY1));
    when(courseEntityMapper.fromEntity(COURSE_ENTITY1)).thenReturn(COURSE1);
    when(studentService.get(new StudentId(3L))).thenReturn(STUDENT3);
    assertDoesNotThrow(()->courseService.addStudent(new StudentId(3L), new CourseId(1L)));
    assertEquals(COURSE_ENTITY1.getStudentIds().size(),2);
    verify(courseRepository,times(2)).findCourseEntityById(any());
    verify(studentService).get((StudentId) any());
    verify(courseEntityMapper,times(2)).fromEntity(any());
  }
  @Test
  public void failToRemoveStudentByNullCourseId(){
    assertThrows(IllegalArgumentException.class,()->courseService.removeStudent(new StudentId(3L),null),"Course id cannot be null!");
  }
  @Test
  public void failToRemoveStudentByNullStudentId(){
    assertThrows(IllegalArgumentException.class,()->courseService.removeStudent(null,new CourseId(3L)),"Student id cannot be null!");
  }
  @Test
  public void failToRemoveStudentByNonexistentCourseId(){
    when(courseRepository.findCourseEntityById(300L)).thenReturn(Optional.empty());
    assertThrows(IncorrectCourseIdException.class,
      ()->courseService.removeStudent(new StudentId(1L),new CourseId(300L)),"Course with id 300 was not found!");
    verify(courseRepository).findCourseEntityById(any());
  }
  @Test
  public void failToRemoveNonexistentStudent(){
    when(courseRepository.findCourseEntityById(1L)).thenReturn(Optional.of(COURSE_ENTITY1));
    assertThrows(IncorrectCourseStudentException.class,()->courseService.removeStudent(new StudentId(1L),new CourseId(1L)),"Student is not present!");
    verify(courseRepository,times(2)).findCourseEntityById(any());
  }
  @Test
  public void removeStudentSuccessfully(){
    when(courseRepository.findCourseEntityById(1L)).thenReturn(Optional.of(COURSE_ENTITY3));
    assertDoesNotThrow(()->courseService.removeStudent(new StudentId(2L),new CourseId(1L)));
    verify(courseRepository,times(2)).findCourseEntityById(any());
    assertEquals(COURSE_ENTITY3.getStudentIds().size(),0);
  }
  @Test
  @SneakyThrows({sigma.training.eum.student.exception.IncorrectUserIdException.class, StudentNotFoundException.class})
  public void getAssignableCourses(){
    User testUser = new User(new UserId(1L), Role.STUDENT,"student","student", true);
    when(userService.getCurrentUser()).thenReturn(testUser);
    when(studentService.get(new UserId(1L))).thenReturn(STUDENT2);
    ArrayList<CourseEntity> list1 = new ArrayList<>(List.of(COURSE_ENTITY2, COURSE_ENTITY3));
    ArrayList<CourseEntity> list2 = new ArrayList<>(List.of(COURSE_ENTITY1, COURSE_ENTITY3));
    when(courseRepository.getCourseEntitiesByStudentIdsContaining(STUDENT2.studentId().value())).thenReturn(list1);
    when(courseRepository.findAll(any(Specification.class))).thenReturn(COURSE_ENTITY_LIST);
    when(courseEntityMapper.fromEntity(COURSE_ENTITY_LIST.get(0))).thenReturn(COURSE1);
    when(courseEntityMapper.fromEntity(COURSE_ENTITY1)).thenReturn(COURSE1);
    when(courseEntityMapper.fromEntity(COURSE_ENTITY2)).thenReturn(COURSE2);
    when(courseEntityMapper.fromEntity(COURSE_ENTITY3)).thenReturn(COURSE4);
    assertEquals(List.of(COURSE1),courseService.getCurrentUserAssignableCourses());
    verify(userService).getCurrentUser();
    verify(studentService).get((UserId) any());
    verify(courseRepository).getCourseEntitiesByStudentIdsContaining(any());
    verify(courseEntityMapper,times(3)).fromEntity(any());
  }

  @Test
  public void courseShouldHaveStudentToStart() {
    when(courseRepository.findCourseEntityById(1L)).thenReturn(Optional.of(COURSE_ENTITY5));
    when(courseEntityMapper.fromEntity(COURSE_ENTITY5)).thenReturn(COURSE5);
    assertThrows(NoStudentsInCourseException.class, () -> courseService.startCourse(new CourseId(1L)));
  }

  @Test
  public void startCourseSuccessfully() throws IncorrectCourseStatusException, IncorrectCourseIdException, CourseNotFoundException, IncorrectCourseTimestampException, NoStudentsInCourseException, IncorrectCourseNameException {
    when(courseRepository.findCourseEntityById(1L)).thenReturn(Optional.of(COURSE_ENTITY6));
    when(courseEntityMapper.fromEntity(COURSE_ENTITY6)).thenReturn(COURSE5);
    when(courseEntityMapper.toEntity(any(Course.class))).thenReturn(COURSE_ENTITY2);
    when(courseRepository.save(COURSE_ENTITY2)).thenReturn(COURSE_ENTITY2);
    when(courseEntityMapper.fromEntity(COURSE_ENTITY2)).thenReturn(COURSE4);
    assertEquals(COURSE4, courseService.startCourse(new CourseId(1L)));
  }

  @Test
  public void finishCourseWithCorrectStatus() throws IncorrectCourseFinishStatusException, CourseNotFoundException, TutorNotFoundException, InsufficientTutorAuthoritiesException {
    Tutor tutor = new Tutor(new TutorId(1L), sigma.training.eum.tutor.dictionary.Status.ACTIVE, "name,", new UserId(1L));
    CourseEntity updatedCourseEntity = new CourseEntity();
    updatedCourseEntity.setId(null);
    updatedCourseEntity.setStatus(Status.FINISHED);
    updatedCourseEntity.setName(COURSE_FINISHED_ASSIGNMENTS.name());
    updatedCourseEntity.setTutorId(COURSE_FINISHED_ASSIGNMENTS.tutorId().value());
    updatedCourseEntity.setStartDate(COURSE_ENTITY2.getStartDate());
    updatedCourseEntity.setEndDate(null);
    when(tutorService.requireCurrentUserAsTutor()).thenReturn(tutor);
    when(courseRepository.findCourseEntityById(1L)).thenReturn(Optional.of(COURSE_ENTITY2));
    when(courseEntityMapper.fromEntity(any())).thenReturn(COURSE_FINISHED_ASSIGNMENTS);
    when(courseEntityMapper.toEntity(any())).thenReturn(updatedCourseEntity);
    when(assignmentService.getAll(Optional.empty(), Optional.of(new CourseId(1L)),Optional.empty())).thenReturn(FINISHED_ASSIGNMENTS_LIST);
    when(courseRepository.save(any())).thenReturn(updatedCourseEntity);
    Course finishedCourse = courseService.finishCourse(new CourseId(1L));

    assertEquals(Status.FINISHED, finishedCourse.status());
    assertDoesNotThrow(() -> assignmentService.getAll(Optional.of(new TutorId(1L)), Optional.of(new CourseId(2L)),Optional.empty()));
  }
}

