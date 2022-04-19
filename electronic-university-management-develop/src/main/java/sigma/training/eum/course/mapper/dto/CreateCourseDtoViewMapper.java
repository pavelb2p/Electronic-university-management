package sigma.training.eum.course.mapper.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import sigma.training.eum.course.presentation.dto.CreateCourseDtoView;
import sigma.training.eum.course.service.type.Course;
import sigma.training.eum.mapperInterface.DtoMapper;
import sigma.training.eum.tutor.service.type.TutorId;
@Component
@AllArgsConstructor
public class CreateCourseDtoViewMapper implements DtoMapper<CreateCourseDtoView, Course> {
  public Course fromDto (CreateCourseDtoView createCourseDtoView){
    return new Course (null,
      null,
      createCourseDtoView.name(),
      new TutorId(createCourseDtoView.tutorId()),
      null,
      null,
      null
      );
  }
  public CreateCourseDtoView toDto(Course course){
    throw new UnsupportedOperationException();
  }
}
