package sigma.training.eum.task.mapper.dto;

import org.springframework.stereotype.Component;
import sigma.training.eum.assignment.service.type.AssignmentId;
import sigma.training.eum.mapperInterface.DtoMapper;
import sigma.training.eum.student.service.type.StudentId;
import sigma.training.eum.task.presentation.dto.TaskDto;
import sigma.training.eum.task.service.type.Task;
import sigma.training.eum.task.service.type.TaskId;
@Component
public class TaskDtoMapper implements DtoMapper<TaskDto, Task> {
  @Override
  public Task fromDto(TaskDto taskDto) {
    return new Task(new TaskId(taskDto.id()),
      taskDto.status(),
      taskDto.completedTaskUrl(),
      new AssignmentId(taskDto.assignmentId()),
      new StudentId(taskDto.studentId()),
      taskDto.mark(),
      taskDto.returnReason());
  }

  @Override
  public TaskDto toDto(Task task){
    return new TaskDto(task.taskId().value(),
      task.status(),
      task.completedTaskUrl(),
      task.assignmentId().value(),
      task.studentId().value(),
      task.mark(),
      task.returnReason(),
      task.description());
  }
}
