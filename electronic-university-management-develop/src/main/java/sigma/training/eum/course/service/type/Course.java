package sigma.training.eum.course.service.type;

import sigma.training.eum.course.dictionary.Status;
import sigma.training.eum.tutor.service.type.TutorId;

import java.sql.Timestamp;

public record Course(CourseId courseId,
                     Status status,
                     String name,
                     TutorId tutorId,
                     Timestamp startDate,
                     Timestamp endDate,
                     Timestamp createDate) {
}
