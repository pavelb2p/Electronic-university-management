package sigma.training.eum.student.presentation.dto;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import sigma.training.eum.student.dictionary.Status;

@Schema(description = "University Student presentation")
public record StudentDto(
  @Schema(description = "Student id") Long id,
  @Schema(description = "Student status") Status status,
  @Schema(description = "Student name") @NotNull String name,
  @Schema(description = "User id which references to this Student") @NotNull Long userId) {
}
