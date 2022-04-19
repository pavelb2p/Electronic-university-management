package sigma.training.eum.assignment.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import sigma.training.eum.assignment.dictionary.Status;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public record AssignmentDto(@Schema(description = "Assignment id") Long id,
                            @Schema(description = "Assignment status") Status status,
                            @Schema(description = "Assignment description") String description,
                            @Schema(description = "Course id which refers to this assignment") @NotNull Long courseId,
                            @Schema(description = "Creation date") @NotNull Timestamp creationDate,
                            @Schema(description = "Start date")  Timestamp startDate,
                            @Schema(description = "Finish date") Timestamp finishDate) {
}
