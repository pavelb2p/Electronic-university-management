package sigma.training.eum.course.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sigma.training.eum.course.exception.*;

@ControllerAdvice
public class CourseExceptionHandler {
  @ExceptionHandler(value = CourseNotFoundException.class)
  public ResponseEntity<String> throwCourseNotFoundResponse(CourseNotFoundException courseNotFoundException) {
    return new ResponseEntity<>(courseNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = IncorrectCourseIdException.class)
  public ResponseEntity<String> throwIncorrectCourseIdResponse(IncorrectCourseIdException incorrectCourseIdException) {
    return new ResponseEntity<>(incorrectCourseIdException.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = IncorrectCourseNameException.class)
  public ResponseEntity<String> throwIncorrectCourseNameResponse(IncorrectCourseNameException incorrectCourseNameException) {
    return new ResponseEntity<>(incorrectCourseNameException.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = IncorrectCourseStatusException.class)
  public ResponseEntity<String> throwIncorrectCourseStatusResponse(IncorrectCourseStatusException incorrectCourseStatusException) {
    return new ResponseEntity<>(incorrectCourseStatusException.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = IncorrectCourseTutorException.class)
  public ResponseEntity<String> throwIncorrectCourseTutorResponse(IncorrectCourseTutorException incorrectCourseTutorException) {
    return new ResponseEntity<>(incorrectCourseTutorException.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = IncorrectCourseTimestampException.class)
  public ResponseEntity<String> throwIncorrectCourseTimestampResponse(IncorrectCourseTimestampException incorrectCourseTimestampException) {
    return new ResponseEntity<>(incorrectCourseTimestampException.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = IncorrectCourseStudentException.class)
  public ResponseEntity<String> throwIncorrectCourseTimestampResponse(IncorrectCourseStudentException incorrectCourseStudentException) {
    return new ResponseEntity<>(incorrectCourseStudentException.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = NotExistingCourseIdException.class)
  public ResponseEntity<String> throwNotExistingCourseIdResponse(NotExistingCourseIdException notExistingCourseIdException) {
    return new ResponseEntity<>(notExistingCourseIdException.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = NoStudentsInCourseException.class)
  public ResponseEntity<String> throwNotStudentsInCourseResponse(NoStudentsInCourseException noStudentsInCourseException) {
    return new ResponseEntity<>(noStudentsInCourseException.getMessage(), HttpStatus.NOT_FOUND);
  }
  @ExceptionHandler(value = IncorrectCourseFinishStatusException.class)
  public ResponseEntity<String> throwIncorrectCourseFinishStatusException(IncorrectCourseFinishStatusException incorrectCourseFinishStatusException){
    return new ResponseEntity<>(incorrectCourseFinishStatusException.getMessage(), HttpStatus.BAD_REQUEST);
  }
}
