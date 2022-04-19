package sigma.training.eum.user.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sigma.training.eum.user.dictionary.Role;
import sigma.training.eum.user.service.UserService;

@RestController
@AllArgsConstructor
@Tag(name = "Basic admin operations")
@SecurityRequirement(name = "basicAuth")
public class UserController {
  private final UserService userService;


  @Operation(summary = "Get a role of user")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "User role is found",
      content = {@Content(mediaType = "application/json",
        schema = @Schema(implementation = Role.class))}
    )
  }
  )
  @GetMapping("/whoami")
  @ResponseBody
  public String whoAmI() {
    return userService.getCurrentUserRole();
  }
}
