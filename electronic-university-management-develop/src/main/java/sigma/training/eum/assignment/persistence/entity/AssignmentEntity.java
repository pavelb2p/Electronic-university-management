package sigma.training.eum.assignment.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import sigma.training.eum.assignment.dictionary.Status;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "assignment")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AssignmentEntity {
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column
  @Convert(converter = AssignmentStatusConverter.class)
  private Status status;
  @Column
  private String description;
  @Column(name = "course_id")
  private Long courseId;
  @CreationTimestamp
  @Column(name = "creation_date")
  private Timestamp creationDate;
  @Column(name = "start_date")
  private Timestamp startDate;
  @Column(name = "end_date")
  private Timestamp endDate;
}
