package sigma.training.eum.course.mapper.dto;

import org.springframework.stereotype.Component;
import sigma.training.eum.course.presentation.dto.CourseDto;
import sigma.training.eum.course.service.type.Course;
import sigma.training.eum.course.service.type.CourseId;
import sigma.training.eum.mapperInterface.DtoMapper;
import sigma.training.eum.tutor.service.type.TutorId;

@Component
public class CourseDtoMapper implements DtoMapper<CourseDto, Course> {
  public Course fromDto(CourseDto courseDto){
    return new Course(new CourseId(courseDto.id()),
      courseDto.status(),
      courseDto.name(),
      new TutorId(courseDto.tutorId()),
      courseDto.startDate(),
      courseDto.finishDate(),
      courseDto.createDate());
  }
  public CourseDto toDto(Course course){
    return new CourseDto(course.courseId().value(),
      course.status(),
      course.name(),
      course.tutorId().value(),
      course.startDate(),
      course.endDate(),
      course.createDate());
  }
}
