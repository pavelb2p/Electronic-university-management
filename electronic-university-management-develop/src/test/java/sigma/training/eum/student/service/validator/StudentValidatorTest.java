package sigma.training.eum.student.service.validator;

import org.junit.jupiter.api.Test;
import sigma.training.eum.student.dictionary.Status;
import sigma.training.eum.student.exception.IncorrectStatusToSuspendException;
import sigma.training.eum.student.exception.IncorrectStudentNameException;
import sigma.training.eum.student.exception.IncorrectStudentStatusException;
import sigma.training.eum.student.exception.IncorrectUserIdException;
import sigma.training.eum.student.service.type.Student;
import sigma.training.eum.student.service.type.StudentId;
import sigma.training.eum.user.service.type.UserId;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StudentValidatorTest {
    private final StudentValidator studentValidator = new StudentValidator();
    @Test
    public void checkCorrectNewStudentTest(){
        Student student = new Student(null,null,"Arsen Ma", new UserId(1L));
        assertDoesNotThrow(()-> studentValidator.validateNewStudent(student));
    }

    @Test
    public void checkIncorrectNewStudentNameTest(){
        Student student = new Student(null,null,"Arsen", new UserId(2L));
        assertThrows(IncorrectStudentNameException.class,
                () -> studentValidator.validateNewStudent(student),
                "Incorrect student name!");
    }
    @Test
    public void checkNullNewStudentNameTest(){
        Student student = new Student(null,null,null, new UserId(2L));
        assertThrows(IncorrectStudentNameException.class,
                () -> studentValidator.validateNewStudent(student),
                "Student name cannot be null!");
    }
    @Test
    public void checkEmptyNewStudentNameTest(){
        Student student = new Student(null,null,"", new UserId(2L));
        assertThrows(IncorrectStudentNameException.class,
                () -> studentValidator.validateNewStudent(student),
                "Student name cannot be empty!");
    }
    @Test
    public void checkIncorrectNewStudentIdTest(){
        Student student = new Student(new StudentId(1L),null,"Arsen Ma", new UserId(1L));
        assertThrows(IncorrectUserIdException.class,
                () -> studentValidator.validateNewStudent(student),
                "Student id cannot be set!");
    }
    @Test
    public void checkIncorrectNewStudentStatusTest(){
        Student student = new Student(null, Status.SUSPENDED,"Arsen Ma", new UserId(1L));
        assertThrows(IncorrectStudentStatusException.class,
                () -> studentValidator.validateNewStudent(student),
                "Student status cannot be set!");
    }
    @Test
    public void checkCorrectStudentToSuspend(){
        Student student = new Student(null, Status.ACTIVE, "Arsen Ma", new UserId(1L));
        assertDoesNotThrow(() -> studentValidator.validateToSuspend(student));
    }
    @Test
    public void checkIncorrectStudentToSuspend(){
        Student student = new Student(null, Status.SUSPENDED, "Arsen Ma", new UserId(1L));
        assertThrows(IncorrectStatusToSuspendException.class, () -> studentValidator.validateToSuspend(student));
    }
}
