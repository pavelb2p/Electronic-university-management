package sigma.training.eum.assignment.presentation.controller;

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
import sigma.training.eum.assignment.dictionary.Status;
import sigma.training.eum.assignment.exception.AssignmentNotFoundException;
import sigma.training.eum.assignment.mapper.dto.AssignmentDtoMapper;
import sigma.training.eum.assignment.mapper.dto.CreateAssignmentDtoViewMapper;
import sigma.training.eum.assignment.presentation.dto.AssignmentDto;
import sigma.training.eum.assignment.presentation.dto.CreateAssignmentDtoView;
import sigma.training.eum.assignment.service.AssignmentService;
import sigma.training.eum.assignment.service.type.AssignmentId;
import sigma.training.eum.course.exception.CourseNotFoundException;
import sigma.training.eum.course.exception.NotExistingCourseIdException;
import sigma.training.eum.task.exception.IncorrectTaskStatusException;
import sigma.training.eum.tutor.exception.InsufficientTutorAuthoritiesException;
import sigma.training.eum.tutor.exception.TutorNotFoundException;
import sigma.training.eum.tutor.service.TutorService;
import sigma.training.eum.user.service.UserService;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Tutor assignment operations")
@SecurityRequirement(name = "basicAuth")
public class TutorAssignmentController {
  private final AssignmentService assignmentService;
  private final AssignmentDtoMapper assignmentDtoMapper;
  private final CreateAssignmentDtoViewMapper createAssignmentDtoViewMapper;
  private final TutorService tutorService;
  private final UserService userService;
  @Operation(summary = "Get a list of all assignments by status")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Found assignment list",
      content = {@Content(mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = AssignmentDto.class)))}
    )
  })
  @GetMapping("/my-assignments")
  public List<AssignmentDto> getAssignments(@RequestParam("status") Status status) throws TutorNotFoundException {
    return assignmentService.getCurrentUserAssignments(status)
      .stream()
      .map(assignmentDtoMapper::toDto)
      .toList();
  }
  @Operation(summary = "Start an assignment")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Assignment is started",
      content = {@Content}
    ),
    @ApiResponse(responseCode = "400", description = "Assignment is not started because of a bad request",
      content = {@Content}
    ),
    @ApiResponse(responseCode = "404", description = "Id not found",
      content = {@Content})
  })
  @PostMapping("/my-assignments/{id}/start")
  public ResponseEntity<String> startAssignment(@PathVariable Long id)
    throws NotExistingCourseIdException, CourseNotFoundException, AssignmentNotFoundException {
    assignmentService.start(
      new AssignmentId(id)
    );
    return ResponseEntity.status(HttpStatus.OK)
      .body("Assignment is started successfully!");
  }
  @Operation(summary = "Finish an assignment")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Assignment is finished",
      content = {@Content}
    ),
    @ApiResponse(responseCode = "400", description = "Assignment is not finished because of a bad request",
      content = {@Content}
    ),
    @ApiResponse(responseCode = "404", description = "Id not found",
      content = {@Content})
  })
  @PostMapping("/my-assignments/{id}/finish")
  public ResponseEntity<String> finishAssignment(@PathVariable Long id)
    throws CourseNotFoundException, AssignmentNotFoundException, TutorNotFoundException, IncorrectTaskStatusException, InsufficientTutorAuthoritiesException {
    assignmentService.finish(
      new AssignmentId(id)
    );
    return ResponseEntity.status(HttpStatus.OK)
      .body("Assignment is finished successfully!");
  }
  @Operation(summary = "Create a new assignment")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Assignment is created",
      content = {@Content}
    ),
    @ApiResponse(responseCode = "400", description = "Assignment is not created because of a bad request",
      content = {@Content}),
    @ApiResponse(responseCode = "404", description = "Id not found",
      content = {@Content})
  })
  @PostMapping("/my-assignments")
  public ResponseEntity<String> create(@RequestBody CreateAssignmentDtoView createAssignmentDtoView) throws NotExistingCourseIdException, CourseNotFoundException {
      assignmentService.create(
        createAssignmentDtoViewMapper.fromDto(createAssignmentDtoView)
      );
      return ResponseEntity.status(HttpStatus.CREATED)
        .body("Assignment is created successfully!");
  }
}
