package sigma.training.eum.task.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import sigma.training.eum.assignment.exception.AssignmentNotFoundException;
import sigma.training.eum.assignment.service.type.AssignmentId;
import sigma.training.eum.course.exception.CourseNotFoundException;
import sigma.training.eum.student.exception.IncorrectUserIdException;
import sigma.training.eum.student.exception.StudentNotFoundException;
import sigma.training.eum.task.dictionary.Status;
import sigma.training.eum.task.exception.*;
import sigma.training.eum.task.mapper.dto.TaskDtoMapper;
import sigma.training.eum.task.presentation.dto.FinishTaskDtoView;
import sigma.training.eum.task.presentation.dto.ProgressTaskDtoView;
import sigma.training.eum.task.presentation.dto.ReturnTaskDtoView;
import sigma.training.eum.task.presentation.dto.TaskDto;
import sigma.training.eum.task.service.TaskService;
import sigma.training.eum.task.service.type.TaskId;
import sigma.training.eum.tutor.exception.InsufficientTutorAuthoritiesException;
import sigma.training.eum.tutor.exception.TutorNotFoundException;

import java.util.List;
import java.util.Optional;


@RestController
@AllArgsConstructor
@Tag(name = "Task operations")
@SecurityRequirement(name = "basicAuth")
public class UserTaskController {
  private final TaskService taskService;
  private final TaskDtoMapper taskDtoMapper;
  @Operation(summary = "Get all tasks by status")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "List of all tasks",
      content = {@Content(mediaType = "application/json",
        schema = @Schema(implementation = TaskDto.class))})})
  @GetMapping("/my-tasks")
  public List<TaskDto> getTasks(@RequestParam(value = "status", required = false) Optional<Status> status) throws IncorrectUserRoleException {
    return taskService.getTasksByStatus(status)
      .stream()
      .map(taskDtoMapper::toDto)
      .toList();
  }
  @Operation(summary = "Progress a task")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Task is updated",
      content = {@Content}
    ),
    @ApiResponse(responseCode = "400", description = "Task is not updated because of a bad request",
      content = {@Content}
    ),
    @ApiResponse(responseCode = "404", description = "Id not found",
      content = {@Content})
  })
  @PostMapping("/my-tasks/{id}/progress")
  public ResponseEntity<String> progress(@PathVariable Long id, @RequestBody ProgressTaskDtoView progressTaskDtoView) throws TaskNotFoundException, IncorrectTaskIdException, StudentNotFoundException, IncorrectUserIdException, IncorrectTaskStatusException, IncorrectTaskUrlException {
    taskService.progress(
      new TaskId(id), progressTaskDtoView.url()
    );
    return ResponseEntity.status(HttpStatus.OK)
      .body(
        "Commitment url is added successfully; tutor can now review the task"
      );
  }
  @Operation(summary = "Return a task")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Task is returned",
      content = {@Content}
    ),
    @ApiResponse(responseCode = "400", description = "Task is not returned because of a bad request",
      content = {@Content}
    ),
    @ApiResponse(responseCode = "404", description = "Id not found",
      content = {@Content})
  })
  @PostMapping(value = {"/my-tasks/{id}/return"})
  public ResponseEntity<String> returnTask(@PathVariable Long id, @RequestBody ReturnTaskDtoView returnTaskDtoView) throws IncorrectTaskIdException, IncorrectTaskStatusException, TutorNotFoundException, IncorrectTaskReturnReasonException, TaskNotFoundException, CourseNotFoundException, InsufficientTutorAuthoritiesException, AssignmentNotFoundException {
    taskService.returnTask(
      new TaskId(id), returnTaskDtoView.reason()
    );
    return ResponseEntity.status(HttpStatus.OK)
      .body(
        "Task is returned successfully"
      );
  }
  @Operation(summary = "Finish a task")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Task is finished",
      content = {@Content}
    ),
    @ApiResponse(responseCode = "400", description = "Task is not finished because of a bad request",
      content = {@Content}
    ),
    @ApiResponse(responseCode = "404", description = "Id not found",
      content = {@Content})
  })
  @PostMapping("/my-tasks/{id}/finish")
  public ResponseEntity<String> finish(@PathVariable Long id, @RequestBody FinishTaskDtoView finishTaskDtoView) throws sigma.training.eum.tutor.exception.IncorrectUserIdException, IncorrectMarkValueException, TaskNotFoundException, TutorNotFoundException, IncorrectTaskStatusException, IncorrectTaskIdException {
    taskService.finish(
      new TaskId(id), finishTaskDtoView.mark()
    );
    return ResponseEntity.status(HttpStatus.OK)
      .body(
        "Task is finished successfully"
      );
  }
  @Operation(summary = "View all tasks which refer to an assignment")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Task list is found",
      content = {@Content(mediaType = "application/json",
        schema = @Schema(implementation = TaskDto.class))}
    ),
    @ApiResponse(responseCode = "400", description = "Tasks are not finished because of a bad request",
      content = {@Content})
  })
  @GetMapping("/my-assignments/{id}/tasks")
  public ResponseEntity<List<TaskDto>> view(@PathVariable Long id) throws sigma.training.eum.tutor.exception.IncorrectUserIdException, IncorrectMarkValueException, TutorNotFoundException, IncorrectTaskIdException {
    return ResponseEntity.status(HttpStatus.OK)
      .body(
          taskService.getTasksByAssignmentId(
            new AssignmentId(id)).stream().map(taskDtoMapper::toDto).toList()
      );
  }
}
