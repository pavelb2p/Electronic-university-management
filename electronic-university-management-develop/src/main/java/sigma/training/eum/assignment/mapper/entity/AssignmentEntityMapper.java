package sigma.training.eum.assignment.mapper.entity;

import org.springframework.stereotype.Component;
import sigma.training.eum.assignment.persistence.entity.AssignmentEntity;
import sigma.training.eum.assignment.service.type.Assignment;
import sigma.training.eum.assignment.service.type.AssignmentId;
import sigma.training.eum.course.service.type.CourseId;
import sigma.training.eum.mapperInterface.EntityMapper;

@Component
public class AssignmentEntityMapper implements EntityMapper<AssignmentEntity, Assignment> {
  public Assignment fromEntity(AssignmentEntity assignmentEntity){
    return new Assignment(new AssignmentId(assignmentEntity.getId()),
      assignmentEntity.getStatus(),
      assignmentEntity.getDescription(),
      new CourseId(assignmentEntity.getCourseId()),
      assignmentEntity.getCreationDate(),
      assignmentEntity.getStartDate(),
      assignmentEntity.getEndDate());
  }
  public AssignmentEntity toEntity(Assignment assignment){
    AssignmentEntity assignmentEntity = new AssignmentEntity();
    assignmentEntity.setId(assignment.assignmentId() == null ? null : assignment.assignmentId().value());
    assignmentEntity.setStatus(assignment.status());
    assignmentEntity.setDescription(assignment.description());
    assignmentEntity.setCourseId(assignment.courseId().value());
    assignmentEntity.setCreationDate(assignment.creationDate());
    assignmentEntity.setStartDate(assignment.startDate());
    assignmentEntity.setEndDate(assignment.endDate());
    return assignmentEntity;
  }
}
