package sigma.training.eum.task.mapper.entity;

import org.springframework.stereotype.Component;
import sigma.training.eum.assignment.service.type.AssignmentId;
import sigma.training.eum.mapperInterface.EntityMapper;
import sigma.training.eum.student.service.type.StudentId;
import sigma.training.eum.task.persistence.entity.TaskEntity;
import sigma.training.eum.task.service.type.Task;
import sigma.training.eum.task.service.type.TaskId;
@Component
public class TaskEntityMapper implements EntityMapper<TaskEntity, Task> {
  @Override
  public Task fromEntity(TaskEntity taskEntity) {
    return new Task(new TaskId(taskEntity.getId()),
      taskEntity.getStatus(),
      taskEntity.getCompletedTaskUrl(),
      new AssignmentId(taskEntity.getAssignmentId()),
      new StudentId(taskEntity.getStudentId()),
      taskEntity.getMark(),
      taskEntity.getReturnReason());
  }
  @Override
  public TaskEntity toEntity(Task task) {
    TaskEntity taskEntity = new TaskEntity();
    taskEntity.setId(task.taskId() == null ? null : task.taskId().value());
    taskEntity.setStatus(task.status());
    taskEntity.setCompletedTaskUrl(task.completedTaskUrl());
    taskEntity.setAssignmentId(task.assignmentId().value());
    taskEntity.setStudentId(task.studentId().value());
    taskEntity.setMark(task.mark());
    taskEntity.setReturnReason(task.returnReason());
    return taskEntity;
  }
}
