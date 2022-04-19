package sigma.training.eum.course.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sigma.training.eum.course.dictionary.Status;
import sigma.training.eum.course.exception.*;
import sigma.training.eum.course.mapper.dto.CourseDtoMapper;
import sigma.training.eum.course.mapper.dto.CreateCourseDtoViewMapper;
import sigma.training.eum.course.presentation.dto.CourseDto;
import sigma.training.eum.course.presentation.dto.CreateCourseDtoView;
import sigma.training.eum.course.presentation.dto.UpdateStudentCourseDtoView;
import sigma.training.eum.course.service.CourseService;
import sigma.training.eum.course.service.type.CourseId;
import sigma.training.eum.student.exception.StudentNotFoundException;
import sigma.training.eum.student.service.type.StudentId;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Tag(name = "Basic course operations")
@SecurityRequirement(name = "basicAuth")
public class CourseController {
  private final CourseService courseService;
  private final CourseDtoMapper courseDtoMapper;
  private final CreateCourseDtoViewMapper createCourseDtoViewMapper;
  @Operation(summary = "Get a list of courses")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Found course list",
      content = {@Content(mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = CourseDto.class)))}
    )
  })
  @GetMapping("/courses")
  @ResponseBody
  public List<CourseDto> getCourses(@RequestParam(name = "status")Optional<Status> status,
                                    @RequestParam(name = "tutor-id") Optional<Long> tutorId,
                                    @RequestParam(name = "student-id") Optional<Long> studentId){
    return courseService.getAll(status, tutorId, studentId)
      .stream()
      .map(courseDtoMapper::toDto)
      .toList();
  }
  @Operation(summary = "Get a course by Id")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Course is found",
      content = {@Content(mediaType = "application/json",
        schema = @Schema(implementation = CourseDto.class))}
    ),
    @ApiResponse(responseCode = "404", description = "Course not found",
      content = {@Content})
  }
  )
  @GetMapping("/courses/{id}")
  @ResponseBody
  public CourseDto getById(@PathVariable("id") Long id) throws CourseNotFoundException {
    return courseDtoMapper.toDto(
      courseService.get(
        new CourseId(id)));
  }
  @Operation(summary = "Create a new course")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Course is created",
      content = {@Content(mediaType = "application/json",
        schema = @Schema(implementation = CourseDto.class))}
    ),
    @ApiResponse(responseCode = "400", description = "Course is not created because of a bad request",
      content = {@Content})
  })
  @PostMapping("/courses")
  public ResponseEntity<CourseDto> create(@RequestBody CreateCourseDtoView createCourseDtoView) throws IncorrectCourseStatusException, IncorrectCourseIdException, IncorrectCourseTutorException, IncorrectCourseNameException, IncorrectCourseTimestampException {
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(
        courseDtoMapper.toDto(
          courseService.create(
            createCourseDtoViewMapper.fromDto(createCourseDtoView)
          )
        )
      );
  }
  @Operation(summary = "Start a new course")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Course is created",
    content = {@Content(mediaType = "application/json",
    schema = @Schema(implementation = CourseDto.class))})})
  @PostMapping("/courses/{course-id}/start")
  public ResponseEntity<CourseDto> startCourse(@PathVariable ("course-id") Long courseId) throws IncorrectCourseStatusException, IncorrectCourseIdException, IncorrectCourseTimestampException, IncorrectCourseTutorException, IncorrectCourseNameException, CourseNotFoundException, NoStudentsInCourseException {
    return ResponseEntity.status(HttpStatus.CREATED).body(
      courseDtoMapper.toDto(
        courseService.startCourse(new CourseId(courseId))
      )
    );
  }

  @Operation(summary = "Add a student to a course")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Student is added",
      content = {@Content(mediaType = "application/json",
        schema = @Schema(implementation = CourseDto.class))}
    ),
    @ApiResponse(responseCode = "400", description = "Student is not added because of a bad request",
      content = {@Content})
  })
  @PostMapping("/courses/{course-id}/add-student")
  public ResponseEntity<CourseDto> addStudent(@RequestBody UpdateStudentCourseDtoView updateStudentCourseDtoView, @PathVariable ("course-id") Long courseId)
    throws IncorrectCourseIdException, CourseNotFoundException, IncorrectCourseStatusException, StudentNotFoundException, IncorrectCourseStudentException {
    return ResponseEntity.status(HttpStatus.CREATED).body(
        courseDtoMapper.toDto(
          courseService.addStudent(new StudentId(updateStudentCourseDtoView.studentId()),new CourseId(courseId))
        )
      );
  }
  @Operation(summary = "Remove a student from a course")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Student is removed",
      content = {@Content(mediaType = "application/json",
        schema = @Schema(implementation = CourseDto.class))}
    ),
    @ApiResponse(responseCode = "400", description = "Student is not removed because of a bad request",
      content = {@Content})
  })
  @PostMapping("/courses/{course-id}/remove-student")
  public ResponseEntity<CourseDto> removeStudent(@RequestBody UpdateStudentCourseDtoView updateStudentCourseDtoView, @PathVariable ("course-id") Long courseId)
    throws IncorrectCourseIdException, CourseNotFoundException, IncorrectCourseStudentException {
    return ResponseEntity.status(HttpStatus.CREATED).body(
      courseDtoMapper.toDto(
        courseService.removeStudent(new StudentId(updateStudentCourseDtoView.studentId()),new CourseId(courseId))
      )
    );
  }
}
