package sigma.training.eum.course.service.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import sigma.training.eum.course.dictionary.Status;
import sigma.training.eum.course.exception.IncorrectCourseIdException;
import sigma.training.eum.course.exception.IncorrectCourseNameException;
import sigma.training.eum.course.exception.IncorrectCourseStatusException;
import sigma.training.eum.course.exception.IncorrectCourseTimestampException;
import sigma.training.eum.course.service.type.Course;

@Component
@AllArgsConstructor
public class CourseValidator {
  public void validateNewCourse(Course course) throws IncorrectCourseNameException, IncorrectCourseStatusException, IncorrectCourseIdException, IncorrectCourseTimestampException {
    if (course.name() == null){
      throw new IncorrectCourseNameException("Course name cannot be null!");
    }
    if (course.name().isEmpty()){
      throw new IncorrectCourseNameException("Course name cannot be empty!");
    }
    if (course.courseId() != null){
      throw new IncorrectCourseIdException("Course id cannot be set!");
    }
    if (course.status() != null){
      throw new IncorrectCourseStatusException("Course status cannot be set!");
    }
    if (course.startDate() != null){
      throw new IncorrectCourseTimestampException("Start date cannot be set!");
    }
    if (course.endDate() != null){
      throw new IncorrectCourseTimestampException("End date cannot be set!");
    }
   }

   public void validateStartCourse(Course course) throws IncorrectCourseNameException, IncorrectCourseIdException, IncorrectCourseTimestampException, IncorrectCourseStatusException {
     if (course.name() == null){
       throw new IncorrectCourseNameException("Course name cannot be null!");
     }
     if (course.name().isEmpty()){
       throw new IncorrectCourseNameException("Course name cannot be empty!");
     }
     if (course.startDate() != null){
       throw new IncorrectCourseTimestampException("Start date must be null!");
     }
     if (course.endDate() != null){
       throw new IncorrectCourseTimestampException("End date cannot be set!");
     }
     if(course.status() != Status.CREATED){
       throw new IncorrectCourseStatusException("Course Status should be \"Created\"!");
     }

   }
}
