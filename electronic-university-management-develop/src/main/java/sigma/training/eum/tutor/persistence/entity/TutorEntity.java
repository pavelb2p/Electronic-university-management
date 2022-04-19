package sigma.training.eum.tutor.persistence.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sigma.training.eum.tutor.dictionary.Status;

import javax.persistence.*;

@Table(name = "tutor")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TutorEntity {
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  @Convert(converter = TutorStatusAttributeConverter.class)
  private Status status;

  @Column
  private String name;

  @Column(name = "user_id")
  private Long userId;
}
