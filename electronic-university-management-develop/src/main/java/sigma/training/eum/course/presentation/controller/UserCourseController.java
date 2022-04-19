package sigma.training.eum.course.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sigma.training.eum.course.dictionary.Status;
import sigma.training.eum.course.mapper.dto.CourseDtoMapper;
import sigma.training.eum.course.presentation.dto.CourseDto;
import sigma.training.eum.course.service.CourseService;
import sigma.training.eum.task.exception.IncorrectUserRoleException;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Course operations")
@SecurityRequirement(name = "basicAuth")
public class UserCourseController {
  private final CourseService courseService;
  private final CourseDtoMapper courseDtoMapper;
  @Operation(summary = "Get all courses")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "List of courses is found",
      content = {@Content(mediaType = "application/json",
        schema = @Schema(implementation = CourseDto.class))})})
  @GetMapping("/my-courses")
  public List<CourseDto> getCourses(@RequestParam("status") Status status) throws IncorrectUserRoleException {
    return courseService.getCoursesByStatus(status)
      .stream()
      .map(courseDtoMapper::toDto)
      .toList();
  }
}
