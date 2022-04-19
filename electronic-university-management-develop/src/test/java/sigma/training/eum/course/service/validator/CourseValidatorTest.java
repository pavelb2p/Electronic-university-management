package sigma.training.eum.course.service.validator;

import org.junit.jupiter.api.Test;
import sigma.training.eum.course.dictionary.Status;
import sigma.training.eum.course.exception.IncorrectCourseIdException;
import sigma.training.eum.course.exception.IncorrectCourseNameException;
import sigma.training.eum.course.exception.IncorrectCourseStatusException;
import sigma.training.eum.course.exception.IncorrectCourseTimestampException;
import sigma.training.eum.course.service.type.Course;
import sigma.training.eum.course.service.type.CourseId;
import sigma.training.eum.tutor.service.type.TutorId;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
public class CourseValidatorTest {
  private final CourseValidator courseValidator = new CourseValidator();
  @Test
  public void checkCorrectNewCourseTest(){
    Course course = new Course(null,null,"Course",new TutorId(2L), null, null,null);
    assertDoesNotThrow(()->courseValidator.validateNewCourse(course));
  }
  @Test
  public void checkEmptyNameNewCourseTest(){
    Course course = new Course(null,null,"",new TutorId(2L), null, null,null);
    assertThrows(IncorrectCourseNameException.class,()->courseValidator.validateNewCourse(course), "Course name cannot be empty!");
  }
  @Test
  public void checkNullNameNewCourseTest(){
    Course course = new Course(null,null,null,new TutorId(2L),null,null,null);
    assertThrows(IncorrectCourseNameException.class,()->courseValidator.validateNewCourse(course), "Course name cannot be null!");
  }
  @Test
  public void checkNotNullNewCourseIdTest(){
    Course course = new Course(new CourseId(5L),null,"Course",new TutorId(2L),null,null,null);
    assertThrows(IncorrectCourseIdException.class,()->courseValidator.validateNewCourse(course), "Course id cannot be set!");
  }
  @Test
  public void checkNotNullNewCourseStatusTest(){
    Course course = new Course(null, Status.CREATED,"Course",new TutorId(2L), null, null,null);
    assertThrows(IncorrectCourseStatusException.class,()->courseValidator.validateNewCourse(course), "Course status cannot be set!");
  }
  @Test
  public void checkNotNullNewCourseStartDate(){
    Course course = new Course(null,null,"Course",new TutorId(2L), new Timestamp(System.currentTimeMillis()), null,null);
    assertThrows(IncorrectCourseTimestampException.class,()->courseValidator.validateNewCourse(course), "Start date cannot be set!");
  }
  @Test
  public void checkNotNullNewCourseEndDate(){
    Course course = new Course(null,null,"Course",new TutorId(2L), null, new Timestamp(System.currentTimeMillis()),null);
    assertThrows(IncorrectCourseTimestampException.class,()->courseValidator.validateNewCourse(course), "End date cannot be set!");
  }
  @Test
  public void beforeStartCourseStatusShouldBeSetCreated(){
    Course course = new Course(null, null,"Course",new TutorId(2L), null, null,null);
    assertThrows(IncorrectCourseStatusException.class, () -> courseValidator.validateStartCourse(course), "Course Status should be Created!");
  }
}
