package sigma.training.eum.assignment.persistence.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import sigma.training.eum.assignment.persistence.entity.AssignmentEntity;
import sigma.training.eum.assignment.dictionary.Status;

import java.util.List;

public interface AssignmentSpecification {
  static Specification<AssignmentEntity> byOrderStatus(Status status){
     return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), criteriaBuilder.literal(status));
  }
  static Specification<AssignmentEntity> byCourseId(Long courseId){
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("courseId"), criteriaBuilder.literal(courseId));
  }
  static Specification<AssignmentEntity> byCourseIdList(List<Long> courseIdList){
    return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("courseId")).value(courseIdList);
  }
}
