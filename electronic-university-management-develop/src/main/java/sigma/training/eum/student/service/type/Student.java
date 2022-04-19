package sigma.training.eum.student.service.type;

import sigma.training.eum.student.dictionary.Status;
import sigma.training.eum.user.service.type.UserId;

public record Student(StudentId studentId, Status status, String name, UserId userId) {
}
