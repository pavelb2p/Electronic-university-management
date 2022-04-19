package sigma.training.eum.user.exception.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sigma.training.eum.user.exception.UserNotFoundException;

@ControllerAdvice
public class UserExceptionHandler {
  @ExceptionHandler(value = UserNotFoundException.class)
  public ResponseEntity<String> throwIncorrectIdException(UserNotFoundException exception) {
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
  }
}
