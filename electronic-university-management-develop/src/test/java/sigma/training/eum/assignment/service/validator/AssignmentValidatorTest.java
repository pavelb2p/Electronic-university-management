package sigma.training.eum.assignment.service.validator;

import org.junit.jupiter.api.Test;
import sigma.training.eum.assignment.dictionary.Status;
import sigma.training.eum.assignment.exception.IncorrectAssignmentDescriptionException;
import sigma.training.eum.assignment.exception.IncorrectAssignmentIdException;
import sigma.training.eum.assignment.exception.IncorrectAssignmentStatusException;
import sigma.training.eum.assignment.exception.IncorrectAssignmentTimestampException;
import sigma.training.eum.assignment.service.type.Assignment;
import sigma.training.eum.assignment.service.type.AssignmentId;
import sigma.training.eum.course.service.type.CourseId;

import java.sql.Timestamp;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

public class AssignmentValidatorTest {
  private final AssignmentValidator assignmentValidator = new AssignmentValidator();
  @Test
  public void checkCorrectNewAssignmentTest() throws ParseException {
    Assignment assignment = new Assignment(null,null,"Assignment",new CourseId(2L), null,null, null);
    assertDoesNotThrow(()->assignmentValidator.validateNewAssignment(assignment));
  }
  @Test
  public void checkEmptyDescriptionNewAssignmentTest(){
    Assignment assignment = new Assignment(null,null,"",new CourseId(2L), null, null, null);
    assertThrows(IncorrectAssignmentDescriptionException.class,()->assignmentValidator.validateNewAssignment(assignment), "Assignment description can`t be empty");
  }
  @Test
  public void checkNullDescriptionNewAssignmentTest(){
    Assignment assignment = new Assignment(null,null,null,new CourseId(2L), null, null, null);
    assertThrows(IncorrectAssignmentDescriptionException.class,()->assignmentValidator.validateNewAssignment(assignment), "Assignment description can`t be null");
  }
  @Test
  public void checkNotNullNewAssignmentIdTest(){
    Assignment assignment = new Assignment(new AssignmentId(1L),null,"Some name",new CourseId(2L), null, null, null);
    assertThrows(IncorrectAssignmentIdException.class,()->assignmentValidator.validateNewAssignment(assignment), "Assignment id can`t be set");
  }
  @Test
  public void checkNotNullNewAssignmentStatusTest(){
    Assignment assignment = new Assignment(null, Status.CREATED,"Some name",new CourseId(2L), null, null, null);
    assertThrows(IncorrectAssignmentStatusException.class,()->assignmentValidator.validateNewAssignment(assignment), "Assignment status can`t be set");
  }
  @Test
  public void checkNotNullNewAssignmentStartDate(){
    Assignment assignment = new Assignment(null, null,"Some name",new CourseId(2L), null, new Timestamp(System.currentTimeMillis()), null);
    assertThrows(IncorrectAssignmentTimestampException.class,()->assignmentValidator.validateNewAssignment(assignment), "Start date cannot be set");
  }
  @Test
  public void checkNotNullNewAssignmentEndDate(){
    Assignment assignment = new Assignment(null, null,"Some name",new CourseId(2L), null, null, new Timestamp(System.currentTimeMillis()));
    assertThrows(IncorrectAssignmentTimestampException.class,()->assignmentValidator.validateNewAssignment(assignment), "End date cannot be set");
  }
  @Test
  public void checkNotNullNewAssignmentCreateData(){
    Assignment assignment = new Assignment(null, null,"Some name",new CourseId(2L), null, null, new Timestamp(System.currentTimeMillis()));
    assertThrows(IncorrectAssignmentTimestampException.class,()->assignmentValidator.validateNewAssignment(assignment), "Creation date cannot be set");
  }
  @Test
  public void checkCorrectStartedAssignmentTest() {
    Assignment assignment = new Assignment(new AssignmentId(2L),Status.CREATED,"Assignment",new CourseId(2L), new Timestamp(System.currentTimeMillis()),null, null);
    assertDoesNotThrow(()->assignmentValidator.validateStartedAssignment(assignment));
  }
  @Test
  public void checkEmptyDescriptionStartedAssignmentTest(){
    Assignment assignment = new Assignment(new AssignmentId(2L),Status.CREATED,"",new CourseId(2L), new Timestamp(System.currentTimeMillis()),null, null);
    assertThrows(IncorrectAssignmentDescriptionException.class,()->assignmentValidator.validateStartedAssignment(assignment), "Assignment description can`t be empty");
  }
  @Test
  public void checkNullDescriptionStartedAssignmentTest(){
    Assignment assignment = new Assignment(new AssignmentId(2L),Status.CREATED,null,new CourseId(2L), new Timestamp(System.currentTimeMillis()),null, null);
    assertThrows(IncorrectAssignmentDescriptionException.class,()->assignmentValidator.validateStartedAssignment(assignment), "Assignment description can`t be null");
  }
  @Test
  public void checkNullStartedAssignmentIdTest(){
    Assignment assignment = new Assignment(null,Status.CREATED,"Assignment",new CourseId(2L), new Timestamp(System.currentTimeMillis()),null, null);
    assertThrows(IncorrectAssignmentIdException.class,()->assignmentValidator.validateStartedAssignment(assignment), "Assignment id can`t be null");
  }
  @Test
  public void checkNullStartedAssignmentStatusTest(){
    Assignment assignment = new Assignment(new AssignmentId(2L),null,"Assignment",new CourseId(2L), new Timestamp(System.currentTimeMillis()),null, null);
    assertThrows(IncorrectAssignmentStatusException.class,()->assignmentValidator.validateStartedAssignment(assignment), "Assignment status can`t be null");
  }
  @Test
  public void checkNotNullStartedAssignmentStartDate(){
    Assignment assignment = new Assignment(new AssignmentId(2L),Status.CREATED,"Assignment",new CourseId(2L), new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()), null);
    assertThrows(IncorrectAssignmentTimestampException.class,()->assignmentValidator.validateStartedAssignment(assignment), "Start date cannot be set");
  }
  @Test
  public void checkNotNullStartedAssignmentEndDate(){
    Assignment assignment = new Assignment(new AssignmentId(2L),Status.CREATED,"Assignment",new CourseId(2L), new Timestamp(System.currentTimeMillis()),null, new Timestamp(System.currentTimeMillis()));
    assertThrows(IncorrectAssignmentTimestampException.class,()->assignmentValidator.validateStartedAssignment(assignment), "End date cannot be set");
  }
  @Test
  public void checkNullStartedAssignmentCreateDate(){
    Assignment assignment = new Assignment(new AssignmentId(2L),Status.CREATED,"Assignment",new CourseId(2L), null,null, null);
    assertThrows(IncorrectAssignmentTimestampException.class,()->assignmentValidator.validateStartedAssignment(assignment), "Creation date cannot be null");
  }
}
