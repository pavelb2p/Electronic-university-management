package sigma.training.eum.course.persistence.repository;

import org.springframework.data.jpa.domain.Specification;
import sigma.training.eum.assignment.persistence.entity.AssignmentEntity;
import sigma.training.eum.course.dictionary.Status;
import sigma.training.eum.course.persistence.entity.CourseEntity;

import java.util.List;

public interface CourseSpecification {
  static Specification<CourseEntity> byOrderStatus(Status status) {
    return((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"),
      criteriaBuilder.literal(status)));
  }
  static Specification<CourseEntity> byTutorId(Long tutorId){
    return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tutorId"),
      criteriaBuilder.literal(tutorId)));
  }
}
