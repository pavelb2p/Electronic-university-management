package sigma.training.eum.assignment.persistence.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import sigma.training.eum.assignment.persistence.entity.AssignmentEntity;
import sigma.training.eum.assignment.service.type.Assignment;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<AssignmentEntity,Long> {
  Optional<AssignmentEntity> findAssignmentEntityById(Long id);
  List<AssignmentEntity> findAll(Specification<AssignmentEntity> criteria);
}
