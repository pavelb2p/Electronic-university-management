package sigma.training.eum.assignment.mapper.dto;

import org.springframework.stereotype.Component;
import sigma.training.eum.assignment.presentation.dto.AssignmentDto;
import sigma.training.eum.assignment.service.type.Assignment;
import sigma.training.eum.assignment.service.type.AssignmentId;
import sigma.training.eum.course.service.type.CourseId;
import sigma.training.eum.mapperInterface.DtoMapper;

@Component
public class AssignmentDtoMapper implements DtoMapper<AssignmentDto, Assignment> {
  public Assignment fromDto(AssignmentDto assignmentDto){
    return new Assignment(new AssignmentId(assignmentDto.id()),
      assignmentDto.status(),
      assignmentDto.description(),
      new CourseId(assignmentDto.courseId()),
      assignmentDto.creationDate(),
      assignmentDto.startDate(),
      assignmentDto.finishDate());
  }
  public AssignmentDto toDto(Assignment assignment){
    return new AssignmentDto(assignment.assignmentId().value(),
      assignment.status(),
      assignment.description(),
      assignment.courseId().value(),
      assignment.creationDate(),
      assignment.startDate(),
      assignment.endDate());
  }
}
