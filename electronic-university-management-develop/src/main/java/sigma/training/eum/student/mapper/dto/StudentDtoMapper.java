package sigma.training.eum.student.mapper.dto;

import org.springframework.stereotype.Component;
import sigma.training.eum.mapperInterface.DtoMapper;
import sigma.training.eum.student.presentation.dto.StudentDto;
import sigma.training.eum.student.service.type.Student;
import sigma.training.eum.student.service.type.StudentId;
import sigma.training.eum.user.service.type.UserId;

@Component
public class StudentDtoMapper implements DtoMapper<StudentDto, Student> {
  public Student fromDto(StudentDto studentDto) {
    return new Student(new StudentId(studentDto.id()), studentDto.status(), studentDto.name(), new UserId(studentDto.userId()));
  }

  public StudentDto toDto(Student student) {
    return new StudentDto(student.studentId().value(), student.status(), student.name(), student.userId().value());
  }
}
