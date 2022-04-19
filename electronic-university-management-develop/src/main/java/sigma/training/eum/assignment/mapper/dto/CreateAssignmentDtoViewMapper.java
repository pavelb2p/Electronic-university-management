package sigma.training.eum.assignment.mapper.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import sigma.training.eum.assignment.presentation.dto.CreateAssignmentDtoView;
import sigma.training.eum.assignment.service.type.Assignment;
import sigma.training.eum.course.service.type.CourseId;
import sigma.training.eum.mapperInterface.DtoMapper;

@Component
@AllArgsConstructor
public class CreateAssignmentDtoViewMapper implements DtoMapper<CreateAssignmentDtoView, Assignment> {
  public Assignment fromDto (CreateAssignmentDtoView createAssignmentDtoView){
    return new Assignment (null,
      null,
      createAssignmentDtoView.description(),
      new CourseId(createAssignmentDtoView.courseId()), null,
      null,null);
  }
  public CreateAssignmentDtoView toDto(Assignment assignment){
    throw new UnsupportedOperationException();
  }
}
