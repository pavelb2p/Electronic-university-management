package sigma.training.eum.task.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sigma.training.eum.task.exception.*;

@ControllerAdvice
public class TaskExceptionHandler {
  @ExceptionHandler(value = TaskNotFoundException.class)
  public ResponseEntity<String> throwTaskNotFoundResponse(TaskNotFoundException taskNotFoundException) {
    return new ResponseEntity<>(taskNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
  }
  @ExceptionHandler(value = GenericServiceException.class)
  public ResponseEntity<String> throwGenericServiceResponse(GenericServiceException genericServiceException){
    return new ResponseEntity<>(genericServiceException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
  @ExceptionHandler(value = IncorrectUserRoleException.class)
  public ResponseEntity<String> throwIncorrectUserRoleResponse(IncorrectUserRoleException incorrectUserRoleException){
    return new ResponseEntity<>(incorrectUserRoleException.getMessage(), HttpStatus.FORBIDDEN);
  }
  @ExceptionHandler(value = IncorrectTaskIdException.class)
  public ResponseEntity<String> throwIncorrectTaskIdResponse(IncorrectTaskIdException incorrectTaskIdException) {
    return new ResponseEntity<>(incorrectTaskIdException.getMessage(), HttpStatus.BAD_REQUEST);
  }
  @ExceptionHandler(value = IncorrectTaskStatusException.class)
  public ResponseEntity<String> throwIncorrectTaskStatusResponse(IncorrectTaskStatusException incorrectTaskStatusException) {
    return new ResponseEntity<>(incorrectTaskStatusException.getMessage(), HttpStatus.BAD_REQUEST);
  }
  @ExceptionHandler(value = IncorrectTaskUrlException.class)
  public ResponseEntity<String> throwIncorrectTaskUrlResponse(IncorrectTaskUrlException incorrectTaskUrlException) {
    return new ResponseEntity<>(incorrectTaskUrlException.getMessage(), HttpStatus.BAD_REQUEST);
  }
  @ExceptionHandler(value = IncorrectTaskReturnReasonException.class)
  public ResponseEntity<String> throwIncorrectTaskReturnReasonResponse(IncorrectTaskReturnReasonException incorrectTaskReturnReasonException) {
    return new ResponseEntity<>(incorrectTaskReturnReasonException.getMessage(), HttpStatus.BAD_REQUEST);
  }
  @ExceptionHandler(value = IncorrectMarkValueException.class)
  public ResponseEntity<String> throwIncorrectMarkValueResponse(IncorrectMarkValueException incorrectMarkValueException){
    return new ResponseEntity<>(incorrectMarkValueException.getMessage(), HttpStatus.BAD_REQUEST);
  }
}
