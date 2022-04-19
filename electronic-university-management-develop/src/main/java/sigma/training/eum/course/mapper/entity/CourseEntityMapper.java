package sigma.training.eum.course.mapper.entity;

import org.springframework.stereotype.Component;
import sigma.training.eum.course.persistence.entity.CourseEntity;
import sigma.training.eum.course.service.type.Course;
import sigma.training.eum.course.service.type.CourseId;
import sigma.training.eum.mapperInterface.EntityMapper;
import sigma.training.eum.tutor.service.type.TutorId;

@Component
public class CourseEntityMapper implements EntityMapper<CourseEntity, Course> {
  public Course fromEntity(CourseEntity courseEntity){
    return new Course(new CourseId(courseEntity.getId()),
      courseEntity.getStatus(),
      courseEntity.getName(),
      new TutorId(courseEntity.getTutorId()),
      courseEntity.getStartDate(),
      courseEntity.getEndDate(),
      courseEntity.getCreateDate());
  }
  public CourseEntity toEntity(Course course){
    CourseEntity courseEntity = new CourseEntity();
    courseEntity.setId(course.courseId() == null ? null : course.courseId().value());
    courseEntity.setStatus(course.status());
    courseEntity.setName(course.name());
    courseEntity.setTutorId(course.tutorId().value());
    courseEntity.setStartDate(course.startDate());
    courseEntity.setEndDate(course.endDate());
    courseEntity.setCreateDate(course.createDate());
    return courseEntity;
  }
}
