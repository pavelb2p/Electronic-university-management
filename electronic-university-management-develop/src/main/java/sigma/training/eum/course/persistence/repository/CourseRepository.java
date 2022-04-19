package sigma.training.eum.course.persistence.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import sigma.training.eum.course.persistence.entity.CourseEntity;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<CourseEntity,Long> {
  Optional<CourseEntity> findCourseEntityById(Long id);
  List<CourseEntity> getCourseEntitiesByStudentIdsContaining(Long studentId);
  List<CourseEntity> findAll(Specification<CourseEntity>courseFilterToCriteria);

}
