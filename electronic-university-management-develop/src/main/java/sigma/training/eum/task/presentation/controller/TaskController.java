package sigma.training.eum.task.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sigma.training.eum.assignment.service.type.AssignmentId;
import sigma.training.eum.course.exception.CourseNotFoundException;
import sigma.training.eum.student.exception.StudentNotFoundException;
import sigma.training.eum.student.service.type.StudentId;
import sigma.training.eum.task.dictionary.Status;
import sigma.training.eum.assignment.presentation.dto.AssignmentDto;
import sigma.training.eum.task.exception.IncorrectTaskIdException;
import sigma.training.eum.task.exception.TaskNotFoundException;
import sigma.training.eum.task.mapper.dto.TaskDtoMapper;
import sigma.training.eum.task.presentation.dto.TaskDto;
import sigma.training.eum.task.service.TaskService;
import sigma.training.eum.task.service.type.TaskId;
import sigma.training.eum.tutor.service.type.TutorId;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Tag(name = "Task operations")
@SecurityRequirement(name = "basicAuth")
public class TaskController {
  private final TaskService taskService;
  private final TaskDtoMapper taskDtoMapper;
  @Operation(summary = "Get a list of all tasks")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Found tasks list",
      content = {@Content(mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = TaskDto.class)))}
    )
  })
  @GetMapping("/tasks")
  @ResponseBody
  public List<TaskDto > getAssignments(@RequestParam(required = false, name = "tutor-id") Optional<Long> tutorId,
                                            @RequestParam(required = false, name = "student-id") Optional<Long> studentId,
                                            @RequestParam(required = false, name = "assignment-id") Optional<Long> assignmentId,
                                            @RequestParam(required = false, name= "status") Optional<Status> status) {

    return taskService.getAll(tutorId.map(TutorId::new), studentId.map(StudentId::new), assignmentId.map(AssignmentId::new), status)
      .stream()
      .map(taskDtoMapper::toDto)
      .toList();
  }
  @Operation(summary = "Get a task by Id")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Task is found",
      content = {@Content(mediaType = "application/json",
        schema = @Schema(implementation = AssignmentDto.class))}
    ),
    @ApiResponse(responseCode = "404", description = "Task is not found",
      content = {@Content})
  }
  )
  @GetMapping("/tasks/{id}")
  @ResponseBody
  public TaskDto getById(@PathVariable("id") Long id) throws TaskNotFoundException, IncorrectTaskIdException {
    return taskDtoMapper.toDto(
      taskService.get(
        new TaskId(id)));
  }
}
