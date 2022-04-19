package sigma.training.eum.course.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sigma.training.eum.course.exception.CourseNotFoundException;
import sigma.training.eum.course.exception.IncorrectCourseIdException;
import sigma.training.eum.course.exception.IncorrectCourseStatusException;
import sigma.training.eum.course.exception.IncorrectCourseStudentException;
import sigma.training.eum.course.mapper.dto.CourseDtoMapper;
import sigma.training.eum.course.presentation.dto.CourseDto;
import sigma.training.eum.course.service.CourseService;
import sigma.training.eum.course.service.type.CourseId;
import sigma.training.eum.student.exception.IncorrectUserIdException;
import sigma.training.eum.student.exception.StudentNotFoundException;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Basic course operations")
@SecurityRequirement(name = "basicAuth")
public class StudentCourseController {
  private final CourseService courseService;
  private final CourseDtoMapper courseDtoMapper;
  @Operation(summary = "Get a list of assignable courses")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Found course list",
      content = {@Content(mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = CourseDto.class)))}
    )
  })
  @GetMapping("/assignable-courses")
  @ResponseBody
  public List<CourseDto> getCourses() throws IncorrectUserIdException, StudentNotFoundException {
    return courseService.getCurrentUserAssignableCourses()
      .stream()
      .map(courseDtoMapper::toDto)
      .toList();
  }
  @Operation(summary = "Assign a course to a student")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Course is assigned",
      content = {@Content}
    ),
    @ApiResponse(responseCode = "400", description = "Course is not assigned because of a bad request",
      content = {@Content}
    ),
    @ApiResponse(responseCode = "404", description = "Id not found",
      content = {@Content})
  })
  @PostMapping("/assignable-courses/{id}/assign")
  public ResponseEntity<String> assign(@PathVariable Long id) throws IncorrectCourseIdException, StudentNotFoundException, IncorrectCourseStatusException, CourseNotFoundException, IncorrectCourseStudentException, IncorrectUserIdException {
    courseService.assignCurrentUser(new CourseId(id));
    return ResponseEntity.status(HttpStatus.CREATED).body(
      "You joined the course successfully!"
    );
  }

}
