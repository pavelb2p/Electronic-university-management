package sigma.training.eum.user.mapper.entity;

import org.springframework.stereotype.Component;
import sigma.training.eum.mapperInterface.EntityMapper;
import sigma.training.eum.user.persistence.entity.UserEntity;
import sigma.training.eum.user.service.type.User;
import sigma.training.eum.user.service.type.UserId;

@Component
public class UserEntityMapper implements EntityMapper<UserEntity, User> {
  public User fromEntity(UserEntity userEntity) {
    return new User(new UserId(userEntity.getId()), userEntity.getRole(), userEntity.getLogin(), userEntity.getPassword(), userEntity.isEnabled());
  }

  public UserEntity toEntity(User user) {
    UserEntity entity = new UserEntity();
    entity.setId(user.userId().value());
    entity.setLogin(user.login());
    entity.setPassword(user.password());
    entity.setRole(user.role());
    entity.setEnabled(user.isEnabled());
    return entity;
  }
}
