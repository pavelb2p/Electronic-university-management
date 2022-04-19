package sigma.training.eum.tutor.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import sigma.training.eum.course.service.CourseService;
import sigma.training.eum.tutor.dictionary.Status;
import sigma.training.eum.tutor.exception.*;
import sigma.training.eum.tutor.mapper.entity.TutorEntityMapper;
import sigma.training.eum.tutor.persistence.repository.TutorRepository;
import sigma.training.eum.tutor.service.type.Tutor;
import sigma.training.eum.tutor.service.type.TutorId;
import sigma.training.eum.tutor.service.validator.TutorValidator;
import sigma.training.eum.user.dictionary.Role;
import sigma.training.eum.user.exception.IllegalIdException;
import sigma.training.eum.user.exception.UserNotFoundException;
import sigma.training.eum.user.service.UserService;
import sigma.training.eum.user.service.type.User;
import sigma.training.eum.user.service.type.UserId;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TutorService {
  private final TutorRepository tutorRepository;
  private final TutorEntityMapper tutorEntityMapper;
  private final UserService userService;
  private final TutorValidator tutorValidator;
  private final CourseService courseService;

  public TutorService(@Lazy CourseService courseService, TutorRepository tutorRepository, TutorEntityMapper tutorEntityMapper,
                      UserService userService, TutorValidator tutorValidator){
    this.courseService = courseService;
    this.tutorRepository = tutorRepository;
    this.tutorEntityMapper = tutorEntityMapper;
    this.userService = userService;
    this.tutorValidator = tutorValidator;
  }
  public List<Tutor> getAll() {
    return tutorRepository
      .findAll()
      .stream()
      .map(tutorEntityMapper::fromEntity)
      .toList();
  }

  public List<Tutor> getAll(Status status){
    return tutorRepository
      .findAllByStatus(status).stream()
      .map(tutorEntityMapper::fromEntity).toList();
  }

  public Tutor get(TutorId id) throws TutorNotFoundException {
    if (id == null) {
      throw new IncorrectUserIdException("Id can't be null");
    }
    return tutorRepository.findTutorEntitiesById(id.value())
      .map(tutorEntityMapper::fromEntity)
      .orElseThrow(() -> new TutorNotFoundException("Tutor with id " +
        id.value() + " was not found!"));
  }

  public Tutor get(UserId id) throws TutorNotFoundException {
    if (id == null) {
      throw new IncorrectUserIdException("User id can't be null");
    }
    return tutorRepository.findTutorEntityByUserId(id.value())
      .map(tutorEntityMapper::fromEntity)
      .orElseThrow(() -> new TutorNotFoundException("Tutor with user id " +
        id.value() + " was not found!"));
  }
  public Tutor requireCurrentUserAsTutor() throws TutorNotFoundException {
    final User currentUser = userService.getCurrentUser();
    if (currentUser.role() != Role.TUTOR) throw new TutorNotFoundException(currentUser.role() + " is unexpected role.");
    try {
      return get(currentUser.userId());
    } catch (IncorrectUserIdException e) {
      throw new TutorNotFoundException(e.getMessage());
    }
  }
  @Transactional
  public Tutor create(Tutor tutor) {
    tutorValidator.validateNewTutor(tutor);
    try {
      userService.get(tutor.userId());
    } catch (UserNotFoundException | IllegalIdException e) {
      throw new IncorrectUserIdException(e.getMessage());
    }
    return tutorEntityMapper
      .fromEntity(tutorRepository.save(tutorEntityMapper.toEntity(
        new Tutor(null, Status.ACTIVE, tutor.name(), tutor.userId()))));
  }

  @Transactional
  public Tutor suspend(TutorId tutorId) throws IncorrectStatusToSuspendException, TutorNotFoundException, ActiveCoursesInTutorException{
    Tutor tutor = get(tutorId);
    if(!courseService.getAll(Optional.of(sigma.training.eum.course.dictionary.Status.STARTED), Optional.of(tutorId.value()), Optional.empty()).isEmpty()){
      throw new ActiveCoursesInTutorException("Can`t suspend a tutor with active courses");
    }
    tutorValidator.validateToSuspend(tutor);
    Tutor suspendedTutor = new Tutor(tutor.id(), Status.SUSPENDED, tutor.name(), tutor.userId());
    userService.disableUser(tutor.userId());
    return tutorEntityMapper
      .fromEntity(tutorRepository.save(tutorEntityMapper.toEntity(suspendedTutor)));
  }
}
