package sigma.training.eum.tutor.mapper.entity;

import org.springframework.stereotype.Component;
import sigma.training.eum.mapperInterface.EntityMapper;
import sigma.training.eum.tutor.persistence.entity.TutorEntity;
import sigma.training.eum.tutor.service.type.Tutor;
import sigma.training.eum.tutor.service.type.TutorId;
import sigma.training.eum.user.service.type.UserId;

@Component
public class TutorEntityMapper implements EntityMapper<TutorEntity, Tutor> {
  public Tutor fromEntity(TutorEntity tutorEntity) {
    return new Tutor(new TutorId(tutorEntity.getId()),
      tutorEntity.getStatus(),
      tutorEntity.getName(),
      new UserId(tutorEntity.getUserId()));
  }

  public TutorEntity toEntity(Tutor tutor) {
    TutorEntity tutorEntity = new TutorEntity();
    tutorEntity.setId(tutor.id() == null ? null : tutor.id().value());
    tutorEntity.setStatus(tutor.status());
    tutorEntity.setName(tutor.name());
    tutorEntity.setUserId(tutor.userId().value());
    return tutorEntity;
  }

}
