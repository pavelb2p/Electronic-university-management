package sigma.training.eum.tutor.exception.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sigma.training.eum.tutor.exception.InsufficientTutorAuthoritiesException;
import sigma.training.eum.tutor.exception.*;

@ControllerAdvice
public class TutorExceptionHandler {
  @ExceptionHandler(value = TutorNotFoundException.class)
  public ResponseEntity<String> throwTutorNotFoundResponse(TutorNotFoundException tutorNotFoundException) {
    return new ResponseEntity<>(tutorNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = IncorrectTutorNameException.class)
  public ResponseEntity<String> throwIncorrectTutorNameResponse(IncorrectTutorNameException incorrectTutorNameException) {
    return new ResponseEntity<>(incorrectTutorNameException.getMessage(), HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(value = IncorrectUserIdException.class)
  public ResponseEntity<String> throwIncorrectUserIdResponse(IncorrectUserIdException incorrectUserIdException) {
    return new ResponseEntity<>(incorrectUserIdException.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = IncorrectTutorStatusException.class)
  public ResponseEntity<String> throwIncorrectTutorStatusResponse(IncorrectTutorStatusException incorrectTutorStatusException) {
    return new ResponseEntity<>(incorrectTutorStatusException.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = IncorrectStatusToSuspendException.class)
  public ResponseEntity<String> throwIncorrectStatusToSuspendResponse(IncorrectStatusToSuspendException incorrectStatusToSuspendException) {
    return new ResponseEntity<>(incorrectStatusToSuspendException.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(value = ActiveCoursesInTutorException.class)
  public ResponseEntity<String> throwActiveCoursesInTutorResponse(ActiveCoursesInTutorException activeCoursesInTutorException){
    return new ResponseEntity<>(activeCoursesInTutorException.getMessage(), HttpStatus.CONFLICT);
  }
  @ExceptionHandler(value = InsufficientTutorAuthoritiesException.class)
  public ResponseEntity<String> throwInsufficientTutorAuthoritiesResponse(InsufficientTutorAuthoritiesException insufficientTutorAuthoritiesException) {
    return new ResponseEntity<>(insufficientTutorAuthoritiesException.getMessage(), HttpStatus.FORBIDDEN);
  }
}
