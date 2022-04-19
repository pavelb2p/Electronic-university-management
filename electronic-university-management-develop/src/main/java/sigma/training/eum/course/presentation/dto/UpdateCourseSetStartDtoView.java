package sigma.training.eum.course.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import sigma.training.eum.course.dictionary.Status;
import sigma.training.eum.course.service.type.Course;

public record UpdateCourseSetStartDtoView(@Schema (description = "Course id") Course courseId,
                                          @Schema(description = "Course Status") Status status) {
}
