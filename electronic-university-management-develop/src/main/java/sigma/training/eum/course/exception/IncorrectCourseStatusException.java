package sigma.training.eum.course.exception;

public class IncorrectCourseStatusException extends IllegalArgumentException{
  public IncorrectCourseStatusException(String message) {
    super(message);
  }
}
