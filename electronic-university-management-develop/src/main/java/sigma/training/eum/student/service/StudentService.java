package sigma.training.eum.student.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import sigma.training.eum.course.service.CourseService;
import sigma.training.eum.student.dictionary.Status;
import sigma.training.eum.student.exception.*;
import sigma.training.eum.student.mapper.entity.StudentEntityMapper;
import sigma.training.eum.student.persistence.repository.StudentRepository;
import sigma.training.eum.student.service.type.Student;
import sigma.training.eum.student.service.type.StudentId;
import sigma.training.eum.student.service.validator.StudentValidator;
import sigma.training.eum.user.exception.UserNotFoundException;
import sigma.training.eum.user.service.UserService;
import sigma.training.eum.user.service.type.UserId;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
  private final StudentRepository studentRepository;
  private final StudentEntityMapper studentEntityMapper;
  private final UserService userService;
  private final StudentValidator studentValidator;
  private final CourseService courseService;

  public StudentService(@Lazy CourseService courseService, StudentRepository studentRepository,
                        StudentEntityMapper studentEntityMapper, UserService userService, StudentValidator studentValidator) {
    this.courseService = courseService;
    this.studentRepository = studentRepository;
    this.studentEntityMapper = studentEntityMapper;
    this.userService = userService;
    this.studentValidator = studentValidator;
  }

  public List<Student> getAll() {
    return studentRepository
      .findAll().stream()
      .map(studentEntityMapper::fromEntity).toList();
  }

  public List<Student> getAll(Status status) {
    return studentRepository
      .findAllByStatus(status).stream()
      .map(studentEntityMapper::fromEntity).toList();
  }

  public Student get(StudentId id) throws StudentNotFoundException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null!");
    }
    return studentRepository.findStudentEntityById(id.value())
      .map(studentEntityMapper::fromEntity)
      .orElseThrow(() -> new StudentNotFoundException("Student with id " + id.value() + " was not found!"));
  }

  public Student get(UserId userId) throws StudentNotFoundException {
    if (userId == null) {
      throw new IncorrectUserIdException("User id cannot be null!");
    }
    return studentRepository.findStudentEntityByUserId(userId.value())
      .map(studentEntityMapper::fromEntity)
      .orElseThrow(() -> new StudentNotFoundException("Student with user id " + userId.value() + " was not found!"));
  }

  @Transactional
  public Student create(Student student) {
    studentValidator.validateNewStudent(student);
    try {
      userService.get(student.userId());
    } catch (UserNotFoundException e) {
      throw new IncorrectUserIdException(e.getMessage());
    }
    Student updatedStudent = new Student(null, Status.ACTIVE, student.name(), student.userId());
    return studentEntityMapper
      .fromEntity(studentRepository.save(studentEntityMapper.toEntity(updatedStudent)));
  }

  @Transactional
  public Student suspend(StudentId studentId) throws IncorrectStatusToSuspendException, ActiveCoursesInStudentException {
    Student student;
    try {
      student = get(studentId);
    } catch (StudentNotFoundException e) {
      throw new IncorrectStudentIdException("Incorrect student id.");
    }
    if (!courseService.getAll(Optional.of(sigma.training.eum.course.dictionary.Status.STARTED), Optional.empty(), Optional.of(studentId.value())).isEmpty()) {
      throw new ActiveCoursesInStudentException("Can`t suspend a student on an active courses");
    }
    studentValidator.validateToSuspend(student);
    Student suspendedStudent = new Student(student.studentId(), Status.SUSPENDED, student.name(), student.userId());
    userService.disableUser(student.userId());
    return studentEntityMapper
      .fromEntity(studentRepository.save(studentEntityMapper.toEntity(suspendedStudent)));
  }

}
