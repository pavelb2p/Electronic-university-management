package sigma.training.eum.task.service.type;

import sigma.training.eum.assignment.service.type.AssignmentId;
import sigma.training.eum.student.service.type.StudentId;
import sigma.training.eum.task.dictionary.Status;

public record Task(TaskId taskId,
                   Status status,
                   String completedTaskUrl,
                   AssignmentId assignmentId,
                   StudentId studentId,
                   Integer mark,
                   String returnReason,
                   String description) {

  public Task(TaskId taskId,
              Status status,
              String completedTaskUrl,
              AssignmentId assignmentId,
              StudentId studentId,
              Integer mark,
              String returnReason) {

    this(
      taskId,
      status,
      completedTaskUrl,
      assignmentId,
      studentId,
      mark,
      returnReason,
      null
    );
  }

  public Task supplementDescription(String description) {
    return new Task(
      this.taskId,
      this.status,
      this.completedTaskUrl,
      this.assignmentId,
      this.studentId,
      this.mark,
      this.returnReason,
      description
    );
  }
}
