package sigma.training.eum.user.persistence.entity;

import lombok.*;
import sigma.training.eum.user.dictionary.Role;

import javax.persistence.*;

@Table(name = "user")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserEntity {
  @Id
  @Column
  private Long id;
  @Column
  private String login;
  @Column
  private String password;
  @Column
  @Convert(converter = EntityRoleAttributeConverter.class)
  private Role role;
  @Column
  private boolean isEnabled;
}
