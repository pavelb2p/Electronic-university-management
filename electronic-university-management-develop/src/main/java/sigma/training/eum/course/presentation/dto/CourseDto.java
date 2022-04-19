package sigma.training.eum.course.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import sigma.training.eum.course.dictionary.Status;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
@Schema(description = "University Course presentation")
public record CourseDto(
  @Schema(description = "Course id") Long id,
  @Schema(description = "Course status") Status status,
  @Schema(description = "Course name") @NotNull String name,
  @Schema(description = "Tutor id which refers to this course") @NotNull Long tutorId,
  @Schema(description = "Start date") @NotNull Timestamp startDate,
  @Schema(description = "Finish date") Timestamp finishDate,
  @Schema(description = "Create date") Timestamp createDate) {
}
