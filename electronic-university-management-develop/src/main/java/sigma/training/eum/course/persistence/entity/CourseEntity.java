package sigma.training.eum.course.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import sigma.training.eum.course.dictionary.Status;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Table(name = "course")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CourseEntity {
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column
  @Convert(converter = CourseStatusConverter.class)
  private Status status;
  @Column
  private String name;
  @Column(name = "tutor_id")
  private Long tutorId;
  @Column(name = "start_date")
  private Timestamp startDate;
  @Column(name = "end_date")
  private Timestamp endDate;
  @CreationTimestamp
  @Column(name = "create_date")
  private Timestamp createDate;
  @ElementCollection
  @CollectionTable(name = "students_courses", joinColumns = @JoinColumn(name = "course_id"))
  @Column(name = "student_id")
  private Set<Long> studentIds;
}
