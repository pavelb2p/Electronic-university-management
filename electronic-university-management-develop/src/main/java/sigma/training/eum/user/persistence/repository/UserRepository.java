package sigma.training.eum.user.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sigma.training.eum.user.persistence.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findUserEntityByLogin(String login);

  Optional<UserEntity> findUserEntityById(Long id);
}
