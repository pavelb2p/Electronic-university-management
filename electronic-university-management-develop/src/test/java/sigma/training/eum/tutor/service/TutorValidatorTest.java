package sigma.training.eum.tutor.service;


import org.junit.jupiter.api.Test;
import sigma.training.eum.tutor.dictionary.Status;
import sigma.training.eum.tutor.exception.IncorrectStatusToSuspendException;
import sigma.training.eum.tutor.exception.IncorrectTutorNameException;
import sigma.training.eum.tutor.exception.IncorrectTutorStatusException;
import sigma.training.eum.tutor.exception.IncorrectUserIdException;
import sigma.training.eum.tutor.service.type.Tutor;
import sigma.training.eum.tutor.service.type.TutorId;
import sigma.training.eum.tutor.service.validator.TutorValidator;
import sigma.training.eum.user.service.type.UserId;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TutorValidatorTest {

  private final TutorValidator tutorValidator = new TutorValidator();

  @Test
  void tutorShouldBeCorrectTest() {
    Tutor tutor = new Tutor(null, null, "Pavlo Mis", new UserId(1L));
    assertDoesNotThrow(() -> tutorValidator.validateNewTutor(tutor));
  }

  @Test
  void tutorNameShouldBeCorrect() {
    Tutor tutor = new Tutor(null, null, "Pavlo", new UserId(1L));
    assertThrows(IncorrectTutorNameException.class,
      () -> tutorValidator.validateNewTutor(tutor),
      "Incorrect tutor name!");
  }

  @Test
  void tutorNameShouldBeNotNull() {
    Tutor tutor = new Tutor(null, null, null, new UserId(1L));
    assertThrows(IncorrectTutorNameException.class,
      () -> tutorValidator.validateNewTutor(tutor),
      "Tutor name can't be null!");
  }

  @Test
  void tutorNameShouldNotBeEmptyTest() {
    Tutor tutor = new Tutor(null, null, "", new UserId(3L));
    assertThrows(IncorrectTutorNameException.class,
      () -> tutorValidator.validateNewTutor(tutor), "Tutor name can't be empty!");
  }

  @Test
  void tutorIdShouldNotBeSetTest() {
    Tutor tutor = new Tutor(new TutorId(2L), null, "Assa Mol", new UserId(11L));
    assertThrows(IncorrectUserIdException.class,
      () -> tutorValidator.validateNewTutor(tutor), "Tutor id can't be set!");
  }

  @Test
  public void tutorStatusShouldNotBeSetTest() {
    Tutor tutor = new Tutor(null, Status.ACTIVE, "Elza Carlovna", new UserId(1L));
    assertThrows(IncorrectTutorStatusException.class,
      () -> tutorValidator.validateNewTutor(tutor),
      "Tutor status can't be set!");
  }
  @Test
  public void checkCorrectTutorToSuspend(){
    Tutor tutor = new Tutor(null, Status.ACTIVE, "Arsen Ma", new UserId(1L));
    assertDoesNotThrow(() -> tutorValidator.validateToSuspend(tutor));
  }
  @Test
  public void checkIncorrectTutorToSuspend(){
    Tutor tutor = new Tutor(null, Status.SUSPENDED, "Arsen Ma", new UserId(1L));
    assertThrows(IncorrectStatusToSuspendException.class, () -> tutorValidator.validateToSuspend(tutor));
  }
}
