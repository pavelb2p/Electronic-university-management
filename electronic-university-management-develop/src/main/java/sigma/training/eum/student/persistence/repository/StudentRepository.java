package sigma.training.eum.student.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.training.eum.student.dictionary.Status;
import sigma.training.eum.student.persistence.entity.StudentEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
  Optional<StudentEntity> findStudentEntityById(Long id);
  Optional<StudentEntity> findStudentEntityByUserId(Long id);
  List<StudentEntity> findAllByStatus(Status status);
  boolean existsById(Long id);
}
