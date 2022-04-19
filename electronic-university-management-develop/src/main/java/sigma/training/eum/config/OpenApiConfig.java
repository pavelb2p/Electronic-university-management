package sigma.training.eum.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI api(
    @Value("${application.title}") String title,
    @Value("${application.description}") String description,
    @Value("${application.version}") String version) {
    return new OpenAPI()
      .components(new Components().addSecuritySchemes("basicAuth", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")
      .in(SecurityScheme.In.HEADER).name("Authorization")))
      .info(new Info().title(title).description(description).version(version));
  }

  @Bean
  public GroupedOpenApi openApiStudent() {
    return GroupedOpenApi.builder().pathsToMatch(
      "/whoami",
      "/assignable-courses/**",
      "/my-courses",
      "/my-tasks",
      "/my-tasks/{id}/progress"
    ).group("Student").build();
  }

  @Bean
  public GroupedOpenApi openApiTutor() {
    return GroupedOpenApi.builder().pathsToMatch(
      "/whoami",
      "/my-courses/{id}/finish",
      "/my-assignments/{id}/finish",
      "/my-courses",
      "/my-assignments",
      "/my-courses/{id}/start",
      "/my-assignments/{id}/start",
      "/my-tasks/{id}/return",
      "/my-tasks",
      "/my-tasks/{id}/finish",
      "/my-assignments/{id}/tasks"
    ).group("Tutor").build();
  }

  @Bean
  public GroupedOpenApi openApiAdmin() {
    return GroupedOpenApi.builder().pathsToMatch(
      "/whoami",
      "/tutors/**",
      "/students/**",
      "/courses/**",
      "/assignments/**",
      "/courses/{course-id}/remove-student",
      "/courses/{course-id}/add-student",
      "/tasks/**").group("Admin").build();
  }

}
