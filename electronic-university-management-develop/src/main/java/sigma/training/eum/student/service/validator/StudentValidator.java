package sigma.training.eum.student.service.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import sigma.training.eum.student.dictionary.Status;
import sigma.training.eum.student.exception.IncorrectStatusToSuspendException;
import sigma.training.eum.student.exception.IncorrectStudentNameException;
import sigma.training.eum.student.exception.IncorrectStudentStatusException;
import sigma.training.eum.student.exception.IncorrectUserIdException;
import sigma.training.eum.student.service.type.Student;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
public class StudentValidator {
  static Pattern namePattern = Pattern.compile("^[A-Z][a-z]+ [A-Z][a-z]+$");

  public void validateNewStudent(Student student) throws IncorrectStudentNameException, IncorrectUserIdException, IncorrectStudentStatusException {
    if (student.name() == null) {
      throw new IncorrectStudentNameException("Student name cannot be null!");
    }
    if (student.name().isEmpty()) {
      throw new IncorrectStudentNameException("Student name cannot be empty!");
    }
    Matcher matcher = namePattern.matcher(student.name());
    if (!matcher.matches()) {
      throw new IncorrectStudentNameException("Incorrect student name!");
    }
    if (student.studentId() != null) {
      throw new IncorrectUserIdException("Student id cannot be set!");
    }
    if (student.status() != null) {
      throw new IncorrectStudentStatusException("Student status cannot be set!");
    }
  }

  public void validateToSuspend(Student student) throws IncorrectStatusToSuspendException {
    if (student.status().equals(Status.SUSPENDED)) {
      throw new IncorrectStatusToSuspendException("Can`t suspend a student with suspended status");
    }
  }
}
