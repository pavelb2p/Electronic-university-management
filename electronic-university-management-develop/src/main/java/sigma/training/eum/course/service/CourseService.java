package sigma.training.eum.course.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sigma.training.eum.assignment.service.AssignmentService;
import sigma.training.eum.assignment.service.type.Assignment;
import sigma.training.eum.course.dictionary.Status;
import sigma.training.eum.course.exception.*;
import sigma.training.eum.course.mapper.entity.CourseEntityMapper;
import sigma.training.eum.course.persistence.entity.CourseEntity;
import sigma.training.eum.course.persistence.repository.CourseRepository;
import sigma.training.eum.course.persistence.repository.CourseSpecification;
import sigma.training.eum.course.service.type.Course;
import sigma.training.eum.course.service.type.CourseId;
import sigma.training.eum.course.service.validator.CourseValidator;
import sigma.training.eum.student.exception.StudentNotFoundException;
import sigma.training.eum.student.service.StudentService;
import sigma.training.eum.student.service.type.Student;
import sigma.training.eum.student.service.type.StudentId;
import sigma.training.eum.task.exception.GenericServiceException;
import sigma.training.eum.task.exception.IncorrectUserRoleException;
import sigma.training.eum.tutor.exception.IncorrectUserIdException;
import sigma.training.eum.tutor.exception.InsufficientTutorAuthoritiesException;
import sigma.training.eum.tutor.exception.TutorNotFoundException;
import sigma.training.eum.tutor.service.TutorService;
import sigma.training.eum.tutor.service.type.Tutor;
import sigma.training.eum.user.service.UserService;
import sigma.training.eum.user.service.type.User;
import sigma.training.eum.user.service.type.UserId;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;

@Service
public class CourseService {
  private final CourseRepository courseRepository;
  private final CourseEntityMapper courseEntityMapper;
  private final CourseValidator courseValidator;
  private final TutorService tutorService;
  private final StudentService studentService;
  private final UserService userService;
  private final AssignmentService assignmentService;

  public CourseService(@Lazy AssignmentService assignmentService, UserService userService, StudentService studentService,
                       TutorService tutorService, CourseValidator courseValidator, CourseEntityMapper courseEntityMapper,
                       CourseRepository courseRepository){
    this.assignmentService = assignmentService;
    this.userService = userService;
    this.studentService = studentService;
    this.tutorService = tutorService;
    this.courseValidator = courseValidator;
    this.courseEntityMapper = courseEntityMapper;
    this.courseRepository = courseRepository;
  }

  public Course get(CourseId id) throws CourseNotFoundException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null!");
    }
    return courseRepository.findCourseEntityById(id.value())
      .map(courseEntityMapper::fromEntity)
      .orElseThrow(() -> new CourseNotFoundException("Course with id " + id.value() + " was not found!"));
  }

  public Set<StudentId> getStudents(CourseId id) throws CourseNotFoundException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null!");
    }
    return courseRepository.findCourseEntityById(id.value())
      .map(CourseEntity::getStudentIds)
      .orElseThrow(() -> new CourseNotFoundException("Course with id " + id.value() + " was not found!"))
      .stream()
      .map(StudentId::new)
      .collect(Collectors.toSet());
  }

  public List<Course> getCourses(StudentId id) {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null!");
    }
    return courseRepository.getCourseEntitiesByStudentIdsContaining(id.value())
      .stream()
      .map(courseEntityMapper::fromEntity)
      .toList();
  }

  @Transactional
  public Course addStudent(StudentId studentId, CourseId courseId)
    throws CourseNotFoundException, StudentNotFoundException, IncorrectCourseStatusException, IncorrectCourseStudentException {
    if (courseId == null) {
      throw new IllegalArgumentException("Course id cannot be null!");
    }
    if (studentId == null) {
      throw new IllegalArgumentException("Student id cannot be null!");
    }
    CourseEntity courseEntity = courseRepository.findCourseEntityById(courseId.value())
      .orElseThrow(() -> new CourseNotFoundException("Course with id " + courseId.value() + " was not found!"));
    Student student = studentService.get(studentId);
    if (courseEntityMapper.fromEntity(courseEntity).status() != Status.CREATED) {
      throw new IncorrectCourseStatusException("Course's status should be only \"Created\"!");
    }
    if (student.status() != sigma.training.eum.student.dictionary.Status.ACTIVE) {
      throw new IncorrectCourseStudentException("Student's status should be only \"Active\"!");
    }
    Set<StudentId> updatedStudentIds = getStudents(courseId);
    if (!updatedStudentIds.add(studentId)) {
      throw new IncorrectCourseStudentException("Student is already present!");
    }
    courseEntity.setStudentIds(updatedStudentIds.stream().map(StudentId::value).collect(Collectors.toSet()));
    return courseEntityMapper.fromEntity(
      courseRepository.save(courseEntity)
    );
  }

  @Transactional
  public Course removeStudent(StudentId studentId, CourseId courseId)
    throws CourseNotFoundException, IncorrectCourseStudentException {
    if (courseId == null) {
      throw new IllegalArgumentException("Course id cannot be null!");
    }
    if (studentId == null) {
      throw new IllegalArgumentException("Student id cannot be null!");
    }
    CourseEntity courseEntity = courseRepository.findCourseEntityById(courseId.value())
      .orElseThrow(() -> new IncorrectCourseIdException("Course with id " + courseId.value() + " was not found!"));
    Set<StudentId> updatedStudentIds = getStudents(courseId);
    if (!updatedStudentIds.remove(studentId)) {
      throw new IncorrectCourseStudentException("Student is not present!");
    }
    courseEntity.setStudentIds(updatedStudentIds.stream().map(StudentId::value).collect(Collectors.toSet()));
    return courseEntityMapper.fromEntity(
      courseRepository.save(courseEntity)
    );
  }

  @Transactional
  public Course create(Course course) throws IncorrectCourseTutorException {
    courseValidator.validateNewCourse(course);
    try {
      Tutor tutor = tutorService.get(course.tutorId());
      if (tutor == null) {
        throw new TutorNotFoundException("Tutor with this is does not exist");
      }
      if (tutor.status() != sigma.training.eum.tutor.dictionary.Status.ACTIVE) {
        throw new IncorrectCourseTutorException("Tutor's status should be only \"Active\"!");
      }
    } catch (IncorrectUserIdException | TutorNotFoundException e) {
      throw new IncorrectCourseTutorException("Cannot resolve a selected tutor!");
    }
    Course updatedCourse = new Course(null, Status.CREATED, course.name(), course.tutorId(), null, null, null);
    return courseEntityMapper.fromEntity(
      courseRepository.save(
        courseEntityMapper.toEntity(updatedCourse)));
  }

  @Transactional
  public Course startCourse(CourseId courseId)
    throws IncorrectCourseStatusException, CourseNotFoundException, NoStudentsInCourseException {
    Course course = get(courseId);
    courseValidator.validateStartCourse(course);
    if (getStudents(courseId).isEmpty()) {
      throw new NoStudentsInCourseException("No students in this course");
    }
    Course updatedStartCourse = new Course(course.courseId(), Status.STARTED, course.name(),
      course.tutorId(), new Timestamp(System.currentTimeMillis()), null, course.createDate());

    CourseEntity courseEntity = courseEntityMapper.toEntity(updatedStartCourse);
    courseEntity.setStudentIds(getStudents(courseId).stream().map(StudentId::value).collect(Collectors.toSet()));
    return courseEntityMapper.fromEntity(
      courseRepository.save(courseEntity));
  }

  @Transactional
  public Course finishCourse(CourseId courseId) throws CourseNotFoundException, IncorrectCourseFinishStatusException, TutorNotFoundException, InsufficientTutorAuthoritiesException {
    Course course = get(courseId);

    List<Assignment> assignments = assignmentService.getAll(Optional.empty(),
      Optional.of(courseId),
      Optional.empty());
      if(!tutorService.requireCurrentUserAsTutor().id().equals(course.tutorId())){
        throw new InsufficientTutorAuthoritiesException("Tutor with this Id can't finish this course");
      }
      if(assignments.stream().allMatch(i-> i.status().equals(sigma.training.eum.assignment.dictionary.Status.FINISHED))){
      Course updatedCourse = new Course(course.courseId(), Status.FINISHED, course.name(),
        course.tutorId(), course.startDate(), new Timestamp(System.currentTimeMillis()),
        course.createDate());
      return courseEntityMapper.fromEntity(
        courseRepository.save(
          courseEntityMapper.toEntity(updatedCourse)
        )
      );
    }
    throw new IncorrectCourseFinishStatusException("Not all assignments are finished");
  }


  public List<Course> getCurrentUserAssignableCourses() throws StudentNotFoundException {
    User user = userService.getCurrentUser();
    Student student = studentService.get(user.userId());
    List<Course> studentsCourses = courseRepository.getCourseEntitiesByStudentIdsContaining(student.studentId().value())
      .stream().map(courseEntityMapper::fromEntity).collect(toCollection(ArrayList::new));

    List<Course> createdCourses = new ArrayList<>(getAll(Optional.of(Status.CREATED), Optional.empty(), Optional.empty()));
    createdCourses.removeAll(studentsCourses);
    return createdCourses;
  }

  public List<Course> getAll(Optional<Status> status, Optional<Long> tutorId, Optional<Long> studentId) {
    List<Course> all = courseRepository.findAll(courseFilterToCriteria(status, tutorId))
      .stream()
      .map(courseEntityMapper::fromEntity)
      .collect(toCollection(ArrayList::new));
    if (studentId.isPresent()) {
      all.retainAll(getCourses(new StudentId(studentId.get())));
    }
    return all;
  }

  @Transactional
  public Course assignCurrentUser(CourseId courseId)
    throws StudentNotFoundException, IncorrectCourseStatusException, CourseNotFoundException, IncorrectCourseStudentException {
    User user = userService.getCurrentUser();
    Student student = studentService.get(user.userId());
    return addStudent(student.studentId(), courseId);
  }

  private Specification<CourseEntity> courseFilterToCriteria(Optional<Status> status,
                                                             Optional<Long> tutorId) {
    Specification<CourseEntity> specification = Specification.where(null);
    if (status.isPresent()) {
      specification = specification.and(CourseSpecification.byOrderStatus(status.get()));
    }
    if (tutorId.isPresent()) {
      specification = specification.and(CourseSpecification.byTutorId(tutorId.get()));
    }
    return specification;
  }

  public List<Course> getCoursesByStatus(Status status) throws IncorrectUserRoleException {
    UserId userId = userService.getCurrentUser().userId();
    try {
      switch (userService.getCurrentUserRole()) {
        case "tutor":
          return getAll(Optional.of(status), Optional.of(tutorService.get(userId).id().value()), Optional.empty());
        case "student":
          getAll(Optional.of(status), Optional.empty(), Optional.of(studentService.get(userId).studentId().value()));
        default:
          throw new IncorrectUserRoleException("User should be either student or tutor here");
      }
    } catch (TutorNotFoundException | IncorrectUserIdException | StudentNotFoundException | sigma.training.eum.student.exception.IncorrectUserIdException e) {
      throw new GenericServiceException("Cannot resolve internal user identifiers");
    }
  }
}
