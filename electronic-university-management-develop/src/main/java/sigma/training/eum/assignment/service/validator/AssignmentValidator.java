package sigma.training.eum.assignment.service.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import sigma.training.eum.assignment.exception.IncorrectAssignmentDescriptionException;
import sigma.training.eum.assignment.exception.IncorrectAssignmentIdException;
import sigma.training.eum.assignment.exception.IncorrectAssignmentStatusException;
import sigma.training.eum.assignment.exception.IncorrectAssignmentTimestampException;
import sigma.training.eum.assignment.service.type.Assignment;

@Component
@AllArgsConstructor
public class AssignmentValidator {
  public void validateNewAssignment(Assignment assignment) throws IncorrectAssignmentIdException, IncorrectAssignmentStatusException, IncorrectAssignmentTimestampException, IncorrectAssignmentDescriptionException {
    if(assignment.assignmentId() != null){
      throw new IncorrectAssignmentIdException("Assignment id can`t be set");
    }
    if(assignment.status() != null){
      throw new IncorrectAssignmentStatusException("Assignment status can`t be set");
    }
    if(assignment.description() == null){
      throw new IncorrectAssignmentDescriptionException("Assignment description can`t be null");
    }
    if(assignment.description().isEmpty()){
      throw new IncorrectAssignmentDescriptionException("Assignment description can`t be empty");
    }
    if(assignment.creationDate() != null){
      throw new IncorrectAssignmentTimestampException("Creation date cannot be set");
    }
    if(assignment.startDate() != null){
      throw new IncorrectAssignmentTimestampException("Start date cannot be set");
    }
    if(assignment.endDate() != null){
      throw new IncorrectAssignmentTimestampException("End date cannot be set");
    }
  }

  public void validateStartedAssignment(Assignment assignment) throws IncorrectAssignmentIdException, IncorrectAssignmentStatusException, IncorrectAssignmentDescriptionException, IncorrectAssignmentTimestampException {
    if(assignment.assignmentId() == null){
      throw new IncorrectAssignmentIdException("Assignment id can`t be null");
    }
    if(assignment.status() == null){
      throw new IncorrectAssignmentStatusException("Assignment status can`t be null");
    }
    if(assignment.description() == null){
      throw new IncorrectAssignmentDescriptionException("Assignment description can`t be null");
    }
    if(assignment.description().isEmpty()){
      throw new IncorrectAssignmentDescriptionException("Assignment description can`t be empty");
    }
    if(assignment.creationDate() == null){
      throw new IncorrectAssignmentTimestampException("Creation date cannot be null");
    }
    if(assignment.startDate() != null){
      throw new IncorrectAssignmentTimestampException("Start date cannot be set");
    }
    if(assignment.endDate() != null){
      throw new IncorrectAssignmentTimestampException("End date cannot be set");
    }
  }
}
