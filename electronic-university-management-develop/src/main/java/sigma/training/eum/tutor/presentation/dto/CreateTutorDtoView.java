package sigma.training.eum.tutor.presentation.dto;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "University Tutor view presentation")
public record CreateTutorDtoView(
  @Schema(description = "Tutor name") @NotNull String name,
  @Schema(description = "User id which references to this Tutor") Long userId) {
}

