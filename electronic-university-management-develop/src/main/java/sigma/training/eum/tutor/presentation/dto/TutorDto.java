package sigma.training.eum.tutor.presentation.dto;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import sigma.training.eum.tutor.dictionary.Status;

@Schema(description = "University Tutor presentation")
public record TutorDto(
  @Schema(description = "Tutor id") Long id,
  @Schema(description = "Tutor status") Status status,
  @Schema(description = "Tutor name") @NotNull String name,
  @Schema(description = "User id which references to this Tutor") @NotNull Long userId
) {

}


