package sigma.training.eum.tutor.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.training.eum.tutor.dictionary.Status;
import sigma.training.eum.tutor.persistence.entity.TutorEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface TutorRepository extends JpaRepository<TutorEntity, Long> {
  Optional<TutorEntity> findTutorEntitiesById(Long id);
  List<TutorEntity> findAllByStatus(Status status);
  Optional<TutorEntity> findTutorEntityByUserId(Long id);

}
