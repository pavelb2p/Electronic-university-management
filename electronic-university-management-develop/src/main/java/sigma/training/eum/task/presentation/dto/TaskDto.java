package sigma.training.eum.task.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import sigma.training.eum.task.dictionary.Status;

import javax.validation.constraints.NotNull;

public record TaskDto(@Schema(description = "Task id") Long id,
                      @Schema(description = "Task status") Status status,
                      @Schema(description = "Url with completed task") String completedTaskUrl,
                      @Schema(description = "Assignment id which refers to this task") @NotNull Long assignmentId,
                      @Schema(description = "Student id which refers to this task") @NotNull Long studentId,
                      @Schema(description = "Mark which student received for this task from tutor") Integer mark,
                      @Schema(description = "Return reason") String returnReason,
                      @Schema(description = "Task description") String description
) {
}
