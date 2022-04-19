package sigma.training.eum.task.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sigma.training.eum.task.dictionary.Status;

import javax.persistence.*;

@Table(name = "task")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TaskEntity {
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column
  @Convert(converter = TaskStatusConverter.class)
  private Status status;
  @Column
  private String completedTaskUrl;
  @Column(name = "assignment_id")
  private Long assignmentId;
  @Column(name = "student_id")
  private Long studentId;
  @Column(name = "mark")
  private Integer mark;
  @Column(name = "return_reason")
  private String returnReason;
}

