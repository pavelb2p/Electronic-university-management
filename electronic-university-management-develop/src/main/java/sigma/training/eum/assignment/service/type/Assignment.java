package sigma.training.eum.assignment.service.type;

import sigma.training.eum.assignment.dictionary.Status;
import sigma.training.eum.course.service.type.CourseId;
import java.sql.Timestamp;

public record Assignment(AssignmentId assignmentId,
                         Status status,
                         String description,
                         CourseId courseId,
                         Timestamp creationDate,
                         Timestamp startDate,
                         Timestamp endDate) {
}
