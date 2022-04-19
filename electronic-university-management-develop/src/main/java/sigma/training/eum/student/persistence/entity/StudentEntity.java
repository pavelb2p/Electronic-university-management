package sigma.training.eum.student.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sigma.training.eum.student.dictionary.Status;
import sigma.training.eum.user.persistence.entity.EntityRoleAttributeConverter;

import javax.persistence.*;
import java.util.Set;

@Table(name = "student")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class StudentEntity {
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column
  @Convert(converter = StudentStatusConverter.class)
  private Status status;
  @Column
  private String name;
  @Column(name = "user_id")
  private Long userId;
  @ElementCollection
  @CollectionTable(name = "students_courses", joinColumns = @JoinColumn(name = "student_id"))
  @Column(name = "course_id")
  private Set<Long> courseIds;
}
