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
import sigma.training.eum.assignment.exception.*;
import sigma.training.eum.assignment.mapper.dto.AssignmentDtoMapper;
import sigma.training.eum.assignment.mapper.dto.CreateAssignmentDtoViewMapper;
import sigma.training.eum.assignment.presentation.dto.AssignmentDto;
import sigma.training.eum.assignment.presentation.dto.CreateAssignmentDtoView;
import sigma.training.eum.assignment.service.AssignmentService;
import sigma.training.eum.assignment.service.type.AssignmentId;
import sigma.training.eum.course.exception.CourseNotFoundException;
import sigma.training.eum.course.exception.IncorrectCourseIdException;
import sigma.training.eum.course.exception.IncorrectCourseStatusException;
import sigma.training.eum.course.exception.NotExistingCourseIdException;
import sigma.training.eum.course.service.type.CourseId;
import sigma.training.eum.student.exception.StudentNotFoundException;
import sigma.training.eum.tutor.exception.TutorNotFoundException;
import sigma.training.eum.tutor.service.type.TutorId;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Tag(name = "Assignment operations")
@SecurityRequirement(name = "basicAuth")
public class AssignmentController {
  private final AssignmentService assignmentService;
  private final AssignmentDtoMapper assignmentDtoMapper;
  private final CreateAssignmentDtoViewMapper createAssignmentDtoViewMapper;
  @Operation(summary = "Get a list of all assignments")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Found assignment list",
      content = {@Content(mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = AssignmentDto.class)))}
    )
  })
  @GetMapping("/assignments")
  @ResponseBody
  public List<AssignmentDto> getAssignments(@RequestParam(required = false, name = "tutor-id") Optional<Long> tutorId,
                                            @RequestParam (required = false, name = "course-id") Optional<Long> courseId,
                                            @RequestParam(required = false, name= "status") Optional<Status> status) {

    return assignmentService.getAll(tutorId.map(TutorId::new), courseId.map(CourseId::new), status)
      .stream()
      .map(assignmentDtoMapper::toDto)
      .toList();
  }

  @Operation(summary = "Get an assigment by Id")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Assignment is found",
      content = {@Content(mediaType = "application/json",
        schema = @Schema(implementation = AssignmentDto.class))}
    ),
    @ApiResponse(responseCode = "404", description = "Assignment is not found",
      content = {@Content})
  }
  )
  @GetMapping("/assignments/{id}")
  @ResponseBody
  public AssignmentDto getById(@PathVariable("id") Long id) throws AssignmentNotFoundException {
    return assignmentDtoMapper.toDto(
      assignmentService.get(
        new AssignmentId(id)));
  }
}
