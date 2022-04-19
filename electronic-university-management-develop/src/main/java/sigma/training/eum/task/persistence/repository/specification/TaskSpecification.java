package sigma.training.eum.task.persistence.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import sigma.training.eum.assignment.persistence.entity.AssignmentEntity;
import sigma.training.eum.task.dictionary.Status;
import sigma.training.eum.task.persistence.entity.TaskEntity;

import java.util.List;

public interface TaskSpecification {
  static Specification<TaskEntity> byOrderStatus(Status status){
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), criteriaBuilder.literal(status));
  }
  static Specification<TaskEntity> byOrderStudentId(Long studentId){
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("studentId"), criteriaBuilder.literal(studentId));
  }
  static Specification<TaskEntity> byOrderAssignmentId(Long assignmentId){
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("assignmentId"), criteriaBuilder.literal(assignmentId));
  }
  static Specification<TaskEntity> byAssignmentIds(List<Long> assignmentIdsList){
    return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("assignmentId")).value(assignmentIdsList);
  }
}
