package sigma.training.eum.course.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sigma.training.eum.course.exception.*;
import sigma.training.eum.course.service.CourseService;
import sigma.training.eum.course.service.type.CourseId;
import sigma.training.eum.tutor.exception.InsufficientTutorAuthoritiesException;
import sigma.training.eum.tutor.exception.TutorNotFoundException;

@RestController
@AllArgsConstructor
@Tag(name = "Course operations")
@SecurityRequirement(name = "basicAuth")
public class TutorCourseController {
  private final CourseService courseService;

  @Operation(summary = "Start a new course")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Course is created",
    content = {@Content}
    ),
    @ApiResponse(responseCode = "404", description = "Id not found",
      content = {@Content})
  })
  @PostMapping("/my-courses/{id}/start")
  public ResponseEntity<String> startCourse(@PathVariable Long id) throws IncorrectCourseStatusException, IncorrectCourseIdException, CourseNotFoundException, IncorrectCourseTimestampException, IncorrectCourseNameException, NoStudentsInCourseException {
    courseService.startCourse(new CourseId(id));
    return ResponseEntity.status(HttpStatus.OK).body(
      "Course is started successfully"
    );
  }

  @Operation(summary = "Finish course by Id")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Course is finished",
      content = {@Content}),
    @ApiResponse(responseCode = "404", description = "Course not found",
      content = {@Content})
  })
  @PostMapping("/my-courses/{id}/finish")
  public ResponseEntity<String> finishCourse(@PathVariable Long id) throws CourseNotFoundException, IncorrectCourseFinishStatusException, TutorNotFoundException, InsufficientTutorAuthoritiesException {
    courseService.finishCourse(new CourseId(id));
    return ResponseEntity.status(HttpStatus.CREATED).body(
      "Course is finished successfully!"
    );
  }
}
