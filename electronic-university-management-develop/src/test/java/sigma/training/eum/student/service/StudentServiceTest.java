package sigma.training.eum.student.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import sigma.training.eum.course.exception.CourseNotFoundException;
import sigma.training.eum.course.mapper.entity.CourseEntityMapper;
import sigma.training.eum.course.persistence.entity.CourseEntity;
import sigma.training.eum.course.persistence.repository.CourseRepository;
import sigma.training.eum.course.service.CourseService;
import sigma.training.eum.course.service.type.Course;
import sigma.training.eum.course.service.type.CourseId;
import sigma.training.eum.student.dictionary.Status;
import sigma.training.eum.student.exception.*;
import sigma.training.eum.student.mapper.entity.StudentEntityMapper;
import sigma.training.eum.student.persistence.entity.StudentEntity;
import sigma.training.eum.student.persistence.repository.StudentRepository;
import sigma.training.eum.student.service.type.Student;
import sigma.training.eum.student.service.type.StudentId;
import sigma.training.eum.student.service.validator.StudentValidator;
import sigma.training.eum.tutor.exception.ActiveCoursesInTutorException;
import sigma.training.eum.tutor.service.type.TutorId;
import sigma.training.eum.user.exception.IllegalIdException;
import sigma.training.eum.user.exception.UserNotFoundException;
import sigma.training.eum.user.service.UserService;
import sigma.training.eum.user.service.type.UserId;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
  private static final StudentEntity studentEntity1 = new StudentEntity();
  private static final StudentEntity studentEntity2 = new StudentEntity();
  private static final Student student1 = new Student(new StudentId(1L), Status.ACTIVE, "Arsen Ma", new UserId(1L));
  private static final Student student2 = new Student(new StudentId(2L), Status.ACTIVE, "Sofia Vy", new UserId(2L));
  private static final Student student3 = new Student(null, null, "Pavlo Mi", new UserId(3000L));
  private static final Course COURSE = new Course(new CourseId(1L), sigma.training.eum.course.dictionary.Status.STARTED, "name", new TutorId(2L), null, null, null);
  static {

    studentEntity1.setId(1L);
    studentEntity1.setStatus(Status.ACTIVE);
    studentEntity1.setName("Arsen Ma");
    studentEntity1.setUserId(1L);

    studentEntity2.setId(2L);
    studentEntity2.setStatus(Status.ACTIVE);
    studentEntity2.setName("Sofia Vy");
    studentEntity2.setUserId(2L);
  }
  public static final List<StudentEntity> STUDENT_ENTITIES_1 = Collections.singletonList(studentEntity1);
  public static final List<StudentEntity> STUDENT_ENTITIES_2 = Collections.singletonList(studentEntity2);
  @Mock
  private StudentRepository studentRepository;
  @InjectMocks
  private StudentService studentService;
  @Mock
  private StudentEntityMapper studentEntityMapper;
  @Mock
  private UserService userService;
  @Mock
  private StudentValidator studentValidator;
  @Mock
  private CourseService courseService;

  @Test
  public void getAllWithoutStatus() {
    when(studentRepository.findAll()).thenReturn(STUDENT_ENTITIES_1);
    when(studentEntityMapper.fromEntity(STUDENT_ENTITIES_1.get(0))).thenReturn(student1);
    List<Student> students = studentService.getAll();
    assertNotNull(students);
    assertEquals(1, students.size());
    assertEquals(student1, students.get(0));
    verify(studentRepository).findAll();
    verify(studentEntityMapper).fromEntity(any());
  }

  @Test
  public void getAllWithStatusActive(){
    when(studentRepository.findAllByStatus(Status.ACTIVE)).thenReturn(STUDENT_ENTITIES_1);
    when(studentEntityMapper.fromEntity(STUDENT_ENTITIES_1.get(0))).thenReturn(student1);
    List<Student> studentsWithStatusActive = studentService.getAll(Status.ACTIVE);
    assertNotNull(studentsWithStatusActive);
    assertEquals(1, studentsWithStatusActive.size());
    assertEquals(student1, studentsWithStatusActive.get(0));
    verify(studentEntityMapper).fromEntity(any());
  }

  @Test
  public void getAllWithStatusSuspended(){
    when(studentRepository.findAllByStatus(Status.SUSPENDED)).thenReturn(STUDENT_ENTITIES_2);
    when(studentEntityMapper.fromEntity(STUDENT_ENTITIES_2.get(0))).thenReturn(student2);
    List<Student> studentsWithStatusActive = studentService.getAll(Status.SUSPENDED);
    assertNotNull(studentsWithStatusActive);
    assertEquals(1, studentsWithStatusActive.size());
    assertEquals(student2, studentsWithStatusActive.get(0));
    verify(studentEntityMapper).fromEntity(any());
  }

  @Test
  public void failToGetStudentByNullStudentId() {
    assertThrows(IllegalArgumentException.class,
      () -> studentService.get((StudentId) null), "Id cannot be null!");
  }

  @Test
  public void failToGetStudentByIncorrectStudentId() {
    when(studentRepository.findStudentEntityById(25L))
      .thenReturn(Optional.empty());
    assertThrows(StudentNotFoundException.class,
      () -> studentService.get(new StudentId(25L)),
      "Student with id 25 was not found!");
    verify(studentRepository).findStudentEntityById(any());
    assertEquals(Optional.empty(), studentRepository.findStudentEntityById(25L));
  }

  @Test
  @SneakyThrows(StudentNotFoundException.class)
  public void getCorrectStudentByStudentId() {
    when(studentRepository.findStudentEntityById(2L))
      .thenReturn(Optional.of(studentEntity2));
    when(studentEntityMapper.fromEntity(studentEntity2))
      .thenReturn(student2);
    Student student = studentService.get(new StudentId(2L));
    assertNotNull(student);
    assertEquals(student2, student);
    verify(studentRepository).findStudentEntityById(any());
    verify(studentEntityMapper).fromEntity(any());
  }

  @Test
  @SneakyThrows({IncorrectUserIdException.class, IncorrectStudentNameException.class, IncorrectStudentStatusException.class, UserNotFoundException.class})
  public void failToCreateStudentByIncorrectUserId() throws IllegalIdException {
    doNothing().when(studentValidator).validateNewStudent(student3);
    when(userService.get(student3.userId())).thenThrow(new UserNotFoundException(""));
    assertThrows(IncorrectUserIdException.class, () -> studentService.create(student3));
    verify(studentValidator).validateNewStudent(any());
    verify(userService).get(any());
  }

  @Test
  @SneakyThrows({IncorrectUserIdException.class, IncorrectStudentNameException.class, IncorrectStudentStatusException.class})
  public void createCorrectStudentTest() {
    doNothing().when(studentValidator).validateNewStudent(student3);
    Student updatedStudent = new Student(null, Status.ACTIVE, student3.name(), student3.userId());
    StudentEntity updatedStudentEntity = new StudentEntity();
    updatedStudentEntity.setId(null);
    updatedStudentEntity.setStatus(Status.ACTIVE);
    updatedStudentEntity.setName(student3.name());
    updatedStudentEntity.setUserId(student3.userId().value());
    when(studentEntityMapper.toEntity(updatedStudent)).thenReturn(updatedStudentEntity);
    when(studentEntityMapper.fromEntity(updatedStudentEntity)).thenReturn(updatedStudent);
    when(studentRepository.save(updatedStudentEntity)).thenReturn(updatedStudentEntity);
    assertNotNull(updatedStudent);
    assertNotNull(updatedStudentEntity);
    assertEquals(updatedStudent, studentService.create(student3));
    verify(studentValidator).validateNewStudent(any());
    verify(studentEntityMapper).fromEntity(any());
    verify(studentEntityMapper).toEntity(any());
    verify(studentRepository).save(any());
  }

  @Test
  @SneakyThrows
  public void suspendCorrectStudentTest() {
    doNothing().when(studentValidator).validateToSuspend(student1);
    Student expected = new Student(new StudentId(1L), Status.SUSPENDED, student1.name(), student1.userId());
    StudentEntity suspendedStudentEntity = new StudentEntity();
    suspendedStudentEntity.setId(student1.studentId().value());
    suspendedStudentEntity.setStatus(Status.SUSPENDED);
    suspendedStudentEntity.setName(student1.name());
    suspendedStudentEntity.setUserId(student1.userId().value());
    when(studentEntityMapper.toEntity(expected)).thenReturn(suspendedStudentEntity);
    when(studentEntityMapper.fromEntity(suspendedStudentEntity)).thenReturn(expected);
    when(studentRepository.save(suspendedStudentEntity)).thenReturn(suspendedStudentEntity);
    when(studentRepository.findStudentEntityById(1L)).thenReturn(Optional.of(studentEntity1));
    when(studentEntityMapper.fromEntity(studentEntity1)).thenReturn(student1);

    assertNotNull(expected);
    assertNotNull(suspendedStudentEntity);
    assertEquals(expected, studentService.suspend(student1.studentId()));
    verify(studentValidator).validateToSuspend(any());
    verify(studentEntityMapper, times(2)).fromEntity(any());
    verify(studentEntityMapper).toEntity(any());
    verify(studentRepository).save(any());
  }
  @Test
  public void failToGetStudentByNullUserId() {
    assertThrows(IncorrectUserIdException.class,
      () -> studentService.get((UserId) null), "User id cannot be null!");
  }

  @Test
  public void failToGetStudentByIncorrectUserId() {
    when(studentRepository.findStudentEntityByUserId(25L))
      .thenReturn(Optional.empty());
    assertThrows(StudentNotFoundException.class,
      () -> studentService.get(new UserId(25L)),
      "Student with user id 25 was not found!");
    verify(studentRepository).findStudentEntityByUserId(any());
    assertEquals(Optional.empty(), studentRepository.findStudentEntityById(25L));
  }

  @Test
  @SneakyThrows({IncorrectUserIdException.class, StudentNotFoundException.class})
  public void getCorrectStudentByUserId(){
    when(studentRepository.findStudentEntityByUserId(2L))
      .thenReturn(Optional.of(studentEntity2));
    when(studentEntityMapper.fromEntity(studentEntity2))
      .thenReturn(student2);
    Student student = studentService.get(new UserId(2L));
    assertNotNull(student);
    assertEquals(student2, student);
    verify(studentRepository).findStudentEntityByUserId(any());
    verify(studentEntityMapper).fromEntity(any());
  }
  @Test
  public void failToSuspendStudentWithActiveCourses() {
    when(courseService.getAll(any(Optional.class), any(Optional.class), any(Optional.class))).thenReturn(Collections.singletonList(COURSE));
    when(studentRepository.findStudentEntityById(2L)).thenReturn(Optional.of(studentEntity2));
    when(studentEntityMapper.fromEntity(studentEntity2)).thenReturn(student2);
    assertThrows(ActiveCoursesInStudentException.class, () -> studentService.suspend(student2.studentId()));
  }
}
