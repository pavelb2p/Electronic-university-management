package sigma.training.eum.course.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

public record CreateCourseDtoView(
  @Schema(description = "Course name") @NotNull String name,
  @Schema(description = "Tutor id which refers to this course") @NotNull Long tutorId
){
}
