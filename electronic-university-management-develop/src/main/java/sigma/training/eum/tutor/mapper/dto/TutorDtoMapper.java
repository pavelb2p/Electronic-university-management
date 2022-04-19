package sigma.training.eum.tutor.mapper.dto;

import org.springframework.stereotype.Component;
import sigma.training.eum.mapperInterface.DtoMapper;
import sigma.training.eum.tutor.presentation.dto.TutorDto;
import sigma.training.eum.tutor.service.type.Tutor;
import sigma.training.eum.tutor.service.type.TutorId;
import sigma.training.eum.user.service.type.UserId;

@Component
public class TutorDtoMapper implements DtoMapper<TutorDto, Tutor> {
  public Tutor fromDto(TutorDto tutorDto) {
    return new Tutor(new TutorId(tutorDto.id()), tutorDto.status(), tutorDto.name(), new UserId(tutorDto.userId()));
  }

  public TutorDto toDto(Tutor tutor) {
    return new TutorDto(tutor.id().value(), tutor.status(), tutor.name(), tutor.userId().value());
  }
}
