package sigma.training.eum.student.exception;

import org.springframework.security.access.AccessDeniedException;

public class IncorrectStudentIdInTaskException extends AccessDeniedException {
  public IncorrectStudentIdInTaskException(String s){
    super(s);
  }
}
