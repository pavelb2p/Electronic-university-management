package sigma.training.eum.student.mapper.entity;


import org.springframework.stereotype.Component;
import sigma.training.eum.mapperInterface.EntityMapper;
import sigma.training.eum.student.persistence.entity.StudentEntity;
import sigma.training.eum.student.service.type.Student;
import sigma.training.eum.student.service.type.StudentId;
import sigma.training.eum.user.service.type.UserId;

@Component
public class StudentEntityMapper implements EntityMapper<StudentEntity, Student> {
  public Student fromEntity(StudentEntity entity) {
    return new Student(new StudentId(entity.getId()), entity.getStatus(), entity.getName(), new UserId(entity.getUserId()));
  }

  public StudentEntity toEntity(Student student) {
    StudentEntity entity = new StudentEntity();
    entity.setId(student.studentId() == null ? null : student.studentId().value());
    entity.setUserId(student.userId().value());
    entity.setStatus(student.status());
    entity.setName(student.name());
    return entity;
  }
}
