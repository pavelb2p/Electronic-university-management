package sigma.training.eum.assignment.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sigma.training.eum.assignment.exception.*;
import sigma.training.eum.tutor.exception.TutorNotFoundException;

@ControllerAdvice
public class AssignmentExceptionHandler {
  @ExceptionHandler(value = AssignmentNotFoundException.class)
  public ResponseEntity<String> throwAssignmentNotFoundResponse(AssignmentNotFoundException assignmentNotFoundException) {
    return new ResponseEntity<>(assignmentNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
  }
  @ExceptionHandler(value = IncorrectAssignmentIdException.class)
  public ResponseEntity<String> throwIncorrectAssignmentIdResponse(IncorrectAssignmentIdException incorrectAssignmentIdException){
    return new ResponseEntity<>(incorrectAssignmentIdException.getMessage(), HttpStatus.BAD_REQUEST);
  }
  @ExceptionHandler(value = IncorrectAssignmentStatusException.class)
  public ResponseEntity<String> throwIncorrectAssignmentStatusResponse(IncorrectAssignmentStatusException incorrectAssignmentStatusException){
    return new ResponseEntity<>(incorrectAssignmentStatusException.getMessage(), HttpStatus.BAD_REQUEST);
  }
  @ExceptionHandler(value = IncorrectAssignmentTimestampException.class)
  public ResponseEntity<String> throwIncorrectAssignmentTimestampResponse(IncorrectAssignmentTimestampException incorrectAssignmentTimestampException){
    return new ResponseEntity<>(incorrectAssignmentTimestampException.getMessage(), HttpStatus.BAD_REQUEST);
  }
  @ExceptionHandler(value = IncorrectAssignmentDescriptionException.class)
  public ResponseEntity<String> throwIncorrectAssignmentDescriptionResponse(IncorrectAssignmentDescriptionException incorrectAssignmentDescriptionException){
    return new ResponseEntity<>(incorrectAssignmentDescriptionException.getMessage(), HttpStatus.BAD_REQUEST);
  }
  @ExceptionHandler(value = TutorNotFoundException.class)
  public ResponseEntity<String> throwIncorrectAssignmentDescriptionResponse(TutorNotFoundException incorrectAssignmentDescriptionException){
    return new ResponseEntity<>(incorrectAssignmentDescriptionException.getMessage(), HttpStatus.FORBIDDEN);
  }
}
