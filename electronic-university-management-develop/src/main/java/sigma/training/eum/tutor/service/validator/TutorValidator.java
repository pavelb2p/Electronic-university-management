package sigma.training.eum.tutor.service.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import sigma.training.eum.course.service.CourseService;
import sigma.training.eum.tutor.dictionary.Status;
import sigma.training.eum.tutor.exception.*;
import sigma.training.eum.tutor.service.type.Tutor;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
public class TutorValidator {
  static Pattern namePattern = Pattern.compile("^[A-Z][a-z]+\\s[A-Z][a-z]+$");

  public void validateNewTutor(Tutor tutor) throws IncorrectTutorNameException,
    IncorrectUserIdException,
    IncorrectTutorStatusException {
    if (tutor.name() == null) throw new IncorrectTutorNameException("Tutor name can't be null!");
    if (tutor.name().isEmpty()) throw new IncorrectTutorNameException("Tutor name can't be empty!");

    Matcher matcher = namePattern.matcher(tutor.name());
    if (!matcher.matches()) throw new IncorrectTutorNameException("Incorrect tutor name!");
    if (tutor.id() != null) throw new IncorrectUserIdException("Tutor id can't be set!");
    if (tutor.status() != null) throw new IncorrectTutorStatusException("Tutor status can't be set!");
  }

  public void validateToSuspend(Tutor tutor) throws IncorrectStatusToSuspendException {
    if (tutor.status() == Status.SUSPENDED) {
      throw new IncorrectStatusToSuspendException("Can`t suspend a tutor with suspended status");
    }
  }
}
