package sigma.training.eum.student.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sigma.training.eum.student.exception.*;

@ControllerAdvice
public class StudentExceptionHandler {
  @ExceptionHandler(value = StudentNotFoundException.class)
  public ResponseEntity<String> throwStudentNotFoundResponse(StudentNotFoundException studentNotFoundException) {
    return new ResponseEntity<>(studentNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = IncorrectStudentIdException.class)
  public ResponseEntity<String> throwStudentNotFoundResponse(IncorrectStudentIdException studentNotFoundException) {
    return new ResponseEntity<>(studentNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = IncorrectStudentNameException.class)
  public ResponseEntity<String> throwIncorrectStudentNameResponse(IncorrectStudentNameException incorrectStudentNameException) {
    return new ResponseEntity<>(incorrectStudentNameException.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = IncorrectUserIdException.class)
  public ResponseEntity<String> throwIncorrectUserIdResponse(IncorrectUserIdException incorrectStudentIdException) {
    return new ResponseEntity<>(incorrectStudentIdException.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = IncorrectStudentStatusException.class)
  public ResponseEntity<String> throwIncorrectStudentStatusResponse(IncorrectStudentStatusException incorrectStudentStatusException) {
    return new ResponseEntity<>(incorrectStudentStatusException.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = IncorrectStatusToSuspendException.class)
  public ResponseEntity<String> throwIncorrectStatusToSuspendResponse(IncorrectStatusToSuspendException incorrectStatusToSuspendException) {
    return new ResponseEntity<>(incorrectStatusToSuspendException.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(value = ActiveCoursesInStudentException.class)
  public ResponseEntity<String> throwActiveCoursesInStudentResponse(ActiveCoursesInStudentException activeCoursesInStudentException) {
    return new ResponseEntity<>(activeCoursesInStudentException.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(value = IncorrectStudentIdInTaskException.class)
  public ResponseEntity<String> throwIncorrectStudentIdInTask(IncorrectStudentIdInTaskException incorrectStudentIdInTaskException) {
    return new ResponseEntity<>(incorrectStudentIdInTaskException.getMessage(), HttpStatus.BAD_REQUEST);
  }
}
