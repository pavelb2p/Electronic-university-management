package sigma.training.eum.tutor.mapper.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import sigma.training.eum.mapperInterface.DtoMapper;
import sigma.training.eum.tutor.presentation.dto.CreateTutorDtoView;
import sigma.training.eum.tutor.service.type.Tutor;
import sigma.training.eum.user.service.type.UserId;

@Component
@AllArgsConstructor
public class CreateDtoViewMapper implements DtoMapper<CreateTutorDtoView, Tutor> {
  public Tutor fromDto(CreateTutorDtoView createTutorDtoView) {
    return new Tutor(null, null, createTutorDtoView.name(), new UserId(createTutorDtoView.userId()));
  }

  public CreateTutorDtoView toDto(Tutor tutor) {
    throw new UnsupportedOperationException();
  }

}
