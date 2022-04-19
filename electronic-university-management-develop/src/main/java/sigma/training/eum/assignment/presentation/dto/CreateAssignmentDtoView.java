package sigma.training.eum.assignment.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

public record CreateAssignmentDtoView(@Schema(description = "Course id which refers to this assignment") @NotNull Long courseId,
                                      @Schema(description = "Description which is set to this assignment") String description) {
}
