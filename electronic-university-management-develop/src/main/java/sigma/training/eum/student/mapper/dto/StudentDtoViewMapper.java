package sigma.training.eum.student.mapper.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import sigma.training.eum.mapperInterface.DtoMapper;
import sigma.training.eum.student.presentation.dto.StudentDtoView;
import sigma.training.eum.student.service.type.Student;
import sigma.training.eum.user.service.type.UserId;

@Component
@AllArgsConstructor
public class StudentDtoViewMapper implements DtoMapper<StudentDtoView, Student> {
  public Student fromDto(StudentDtoView createStudentDtoView) {
    return new Student(null, null, createStudentDtoView.name(), new UserId(createStudentDtoView.userId()));
  }

  public StudentDtoView toDto(Student student) {
    throw new UnsupportedOperationException();
  }
}
