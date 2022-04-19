package sigma.training.eum.tutor.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import sigma.training.eum.course.exception.CourseNotFoundException;
import sigma.training.eum.course.mapper.entity.CourseEntityMapper;
import sigma.training.eum.course.persistence.entity.CourseEntity;
import sigma.training.eum.course.persistence.repository.CourseRepository;
import sigma.training.eum.course.service.CourseService;
import sigma.training.eum.course.service.type.Course;
import sigma.training.eum.course.service.type.CourseId;
import sigma.training.eum.student.exception.StudentNotFoundException;
import sigma.training.eum.tutor.exception.*;
import sigma.training.eum.tutor.dictionary.Status;
import sigma.training.eum.tutor.mapper.entity.TutorEntityMapper;
import sigma.training.eum.tutor.persistence.entity.TutorEntity;
import sigma.training.eum.tutor.persistence.repository.TutorRepository;
import sigma.training.eum.tutor.service.type.Tutor;
import sigma.training.eum.tutor.service.type.TutorId;
import sigma.training.eum.tutor.service.validator.TutorValidator;
import sigma.training.eum.user.exception.IllegalIdException;
import sigma.training.eum.user.exception.UserNotFoundException;
import sigma.training.eum.user.service.UserService;
import sigma.training.eum.user.service.type.UserId;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {
    private static final TutorEntity TUTOR_ENTITY1 = new TutorEntity();
    private static final TutorEntity TUTOR_ENTITY2 = new TutorEntity();
    private static final TutorEntity TUTOR_ENTITY3 = new TutorEntity();

    private static final Tutor TUTOR1 = new Tutor(null, Status.ACTIVE, "Elza Carlovna", new UserId(1L));
    private static final Tutor TUTOR2 = new Tutor(new TutorId(2L), Status.SUSPENDED, "Levan Goroziya", new UserId(2L));
    private static final Tutor TUTOR3 = new Tutor(new TutorId(2L), Status.SUSPENDED, "Pavel Goroziya", null);
    private static final Tutor TUTOR4 = new Tutor(new TutorId(1L), Status.ACTIVE, "Pavlo Mit", new UserId(1L));
    private static final Course COURSE = new Course(new CourseId(1L), sigma.training.eum.course.dictionary.Status.STARTED, "name", new TutorId(2L), null, null, null);
    private static final CourseEntity COURSE_ENTITY = new CourseEntity();
    static {
        COURSE_ENTITY.setId(1L);
        COURSE_ENTITY.setStatus(sigma.training.eum.course.dictionary.Status.STARTED);
        COURSE_ENTITY.setName("name");
        COURSE_ENTITY.setTutorId(2L);

        TUTOR_ENTITY1.setId(null);
        TUTOR_ENTITY1.setStatus(Status.ACTIVE);
        TUTOR_ENTITY1.setName("Elza Carlovna");
        TUTOR_ENTITY1.setUserId(1L);

        TUTOR_ENTITY2.setId(2L);
        TUTOR_ENTITY2.setStatus(Status.SUSPENDED);
        TUTOR_ENTITY2.setName("Levan Goroziya");
        TUTOR_ENTITY2.setUserId(2L);

        TUTOR_ENTITY3.setId(1L);
        TUTOR_ENTITY3.setStatus(Status.ACTIVE);
        TUTOR_ENTITY3.setName("Pavlo Mit");
        TUTOR_ENTITY3.setUserId(1L);
    }

    public static final List<TutorEntity> TUTOR_ENTITY_LIST_1 = Collections.singletonList(TUTOR_ENTITY1);
    public static final List<TutorEntity> TUTOR_ENTITY_LIST_2 = Collections.singletonList(TUTOR_ENTITY2);
    @Mock
    TutorRepository tutorRepository;
    @InjectMocks
    private TutorService tutorService;
    @Mock
    private TutorEntityMapper tutorEntityMapper;

    @Mock
    private TutorValidator tutorValidator;

    @Mock
    private UserService userService;

    @Mock
    private CourseService courseService;

    @Test
    void shouldGetAllTutorsWithoutStatusTest() {
        when(tutorRepository.findAll()).thenReturn(TUTOR_ENTITY_LIST_1);
        when(tutorEntityMapper.fromEntity(TUTOR_ENTITY_LIST_1.get(0))).thenReturn(TUTOR1);
        List<Tutor> tutors = tutorService.getAll();
        assertNotNull(tutors);
        assertEquals(1, tutors.size());
        assertEquals(TUTOR1, tutors.get(0));
        verify(tutorRepository).findAll();
        verify(tutorEntityMapper).fromEntity(any());
    }

  @Test
  public void getAllWithStatusActive(){
    when(tutorRepository.findAllByStatus(Status.ACTIVE)).thenReturn(TUTOR_ENTITY_LIST_1);
    when(tutorEntityMapper.fromEntity(TUTOR_ENTITY_LIST_1.get(0))).thenReturn(TUTOR1);
    List<Tutor> tutorsWithStatusActive = tutorService.getAll(Status.ACTIVE);
    assertNotNull(tutorsWithStatusActive);
    assertEquals(1, tutorsWithStatusActive.size());
    assertEquals(TUTOR1, tutorsWithStatusActive.get(0));
    verify(tutorEntityMapper).fromEntity(any());
  }

  @Test
  public void getAllWithStatusSuspended(){
    when(tutorRepository.findAllByStatus(Status.SUSPENDED)).thenReturn(TUTOR_ENTITY_LIST_2);
    when(tutorEntityMapper.fromEntity(TUTOR_ENTITY_LIST_2.get(0))).thenReturn(TUTOR2);
    List<Tutor> tutorsWithStatusActive = tutorService.getAll(Status.SUSPENDED);
    assertNotNull(tutorsWithStatusActive);
    assertEquals(1, tutorsWithStatusActive.size());
    assertEquals(TUTOR2, tutorsWithStatusActive.get(0));
    verify(tutorEntityMapper).fromEntity(any());
  }

    @Test
    void allTutorIdShouldBeUniqueTest() {
        assertNotEquals(TUTOR_ENTITY1.getId(), TUTOR_ENTITY2.getId());
        assertNotEquals(TUTOR1.id(), TUTOR2.id());
    }

    @Test
    void ifTutorIdNotPresentTest() {
        when(tutorRepository.findTutorEntitiesById(228L)).thenReturn(Optional.empty());
        assertThrows(TutorNotFoundException.class,
                () -> tutorService.get(new TutorId(228L)),
                "Tutor Id not find");
        verify(tutorRepository).findTutorEntitiesById(any());
        assertEquals(Optional.empty(), tutorRepository.findTutorEntitiesById(228L));
    }

    @Test
    void tutorIdShouldNotBeNull() {
        assertThrows(IncorrectUserIdException.class,
                () -> tutorService.get((TutorId) null),
                "Id can't be null");
    }


    @Test
    @SneakyThrows({IncorrectUserIdException.class, IncorrectTutorNameException.class, IncorrectTutorStatusException.class})
    void shouldCreateTutorTest(){
        doNothing().when(tutorValidator).validateNewTutor(TUTOR1);
        when(tutorEntityMapper.fromEntity(TUTOR_ENTITY1)).thenReturn(TUTOR1);
        when(tutorEntityMapper.toEntity(TUTOR1)).thenReturn(TUTOR_ENTITY1);
        when(tutorRepository.save(TUTOR_ENTITY1)).thenReturn(TUTOR_ENTITY1);
        Tutor actualTutor = tutorService.create(TUTOR1);
        assertNotNull(actualTutor);
        assertEquals(Status.ACTIVE, actualTutor.status());
        assertEquals(TUTOR1.name(), actualTutor.name());
        assertEquals(TUTOR1.userId(), actualTutor.userId());

        verify(tutorRepository).save(any());
        verify(tutorEntityMapper).fromEntity(any());
        verify(tutorEntityMapper).toEntity(any());
    }

    @Test
    @SneakyThrows({UserNotFoundException.class, IncorrectUserIdException.class, IncorrectTutorNameException.class,IncorrectTutorStatusException.class, IllegalIdException.class})
    void createMethodCanThrowsException(){
        doNothing().when(tutorValidator).validateNewTutor(TUTOR3);

        when(userService.get(TUTOR3.userId())).thenThrow(new UserNotFoundException("User not found"));
        assertThrows(IncorrectUserIdException.class,
                () -> tutorService.create(TUTOR3));

        verify(tutorValidator).validateNewTutor(any());
        verify(userService).get(any());
    }
  @Test
  @SneakyThrows({IncorrectStatusToSuspendException.class, IncorrectUserIdException.class, TutorNotFoundException.class, ActiveCoursesInTutorException.class})
  public void suspendCorrectTutorTest(){
    doNothing().when(tutorValidator).validateToSuspend(TUTOR4);
    Tutor expected = new Tutor(new TutorId(1L), Status.SUSPENDED, TUTOR4.name(), TUTOR4.userId());
    TutorEntity suspendedTutorEntity = new TutorEntity();
    suspendedTutorEntity.setId(TUTOR4.id().value());
    suspendedTutorEntity.setStatus(Status.SUSPENDED);
    suspendedTutorEntity.setName(TUTOR4.name());
    suspendedTutorEntity.setUserId(TUTOR4.userId().value());
    when(tutorEntityMapper.toEntity(expected)).thenReturn(suspendedTutorEntity);
    when(tutorEntityMapper.fromEntity(suspendedTutorEntity)).thenReturn(expected);
    when(tutorRepository.save(suspendedTutorEntity)).thenReturn(suspendedTutorEntity);
    when(tutorRepository.findTutorEntitiesById(1L)).thenReturn(Optional.of(TUTOR_ENTITY3));
    when(tutorEntityMapper.fromEntity(TUTOR_ENTITY3)).thenReturn(TUTOR4);
    assertNotNull(expected);
    assertNotNull(suspendedTutorEntity);
    assertEquals(expected, tutorService.suspend(TUTOR4.id()));
    verify(tutorValidator).validateToSuspend(any());
    verify(tutorEntityMapper, times(2)).fromEntity(any());
    verify(tutorEntityMapper).toEntity(any());
    verify(tutorRepository).save(any());
  }
  @Test
  public void failToSuspendTutorWithActiveCourses()  {
    when(courseService.getAll(any(Optional.class), any(Optional.class), any(Optional.class))).thenReturn(Collections.singletonList(COURSE));
    when(tutorRepository.findTutorEntitiesById(2L)).thenReturn(Optional.of(TUTOR_ENTITY2));
    when(tutorEntityMapper.fromEntity(TUTOR_ENTITY2)).thenReturn(TUTOR2);
    assertThrows(ActiveCoursesInTutorException.class, () -> tutorService.suspend(TUTOR2.id()));
  }
}
