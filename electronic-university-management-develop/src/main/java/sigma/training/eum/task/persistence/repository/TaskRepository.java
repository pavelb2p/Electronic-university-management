package sigma.training.eum.task.persistence.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import sigma.training.eum.assignment.persistence.entity.AssignmentEntity;
import sigma.training.eum.task.persistence.entity.TaskEntity;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity,Long> {
  Optional<TaskEntity> findTaskEntityById(Long id);
  List<TaskEntity> findAll(Specification<TaskEntity> criteria);
}
