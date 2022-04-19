package sigma.training.eum.student.presentation.dto;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "University Student view presentation")
public record StudentDtoView(
  @Schema(description = "Student name") @NotNull String name,
  @Schema(description = "User id which references to this Student") Long userId) {
}
