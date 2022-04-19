package sigma.training.eum.student.presentation.controller;


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
import sigma.training.eum.course.exception.CourseNotFoundException;
import sigma.training.eum.course.mapper.dto.CourseDtoMapper;
import sigma.training.eum.course.service.CourseService;
import sigma.training.eum.student.dictionary.Status;
import sigma.training.eum.student.exception.*;
import sigma.training.eum.student.mapper.dto.StudentDtoMapper;
import sigma.training.eum.student.mapper.dto.StudentDtoViewMapper;
import sigma.training.eum.student.presentation.dto.StudentDto;
import sigma.training.eum.student.presentation.dto.StudentDtoView;
import sigma.training.eum.student.service.StudentService;
import sigma.training.eum.student.service.type.Student;
import sigma.training.eum.student.service.type.StudentId;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Tag(name = "Basic student operations")
@SecurityRequirement(name = "basicAuth")
public class StudentController {
  private final StudentService studentService;
  private final StudentDtoMapper studentDtoMapper;
  private final StudentDtoViewMapper studentDtoViewMapper;

  @Operation(summary = "Get a list of students")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Found student list",
      content = {@Content(mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = StudentDto.class)))}
    )
  })
  @GetMapping("/students")
  @ResponseBody
  public List<StudentDto> getStudents(@RequestParam (required = false, name = "status") Optional<Status> status) {
    return status.isPresent() ? studentService.getAll(status.get()).stream().map(studentDtoMapper::toDto).toList() :
      studentService.getAll().stream().map(studentDtoMapper::toDto).toList();
  }

  @Operation(summary = "Get a student by Id")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Student is found",
      content = {@Content(mediaType = "application/json",
        schema = @Schema(implementation = StudentDto.class))}
    ),
    @ApiResponse(responseCode = "404", description = "Student not found",
      content = {@Content})
  }
  )
  @GetMapping("/students/{id}")
  @ResponseBody
  public StudentDto getById(@PathVariable("id") Long id) throws StudentNotFoundException {
    Student student = studentService.get(new StudentId(id));
    return studentDtoMapper.toDto(student);
  }

  @Operation(summary = "Create a new student")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Student is created",
      content = {@Content(mediaType = "application/json",
        schema = @Schema(implementation = StudentDto.class))}
    ),
    @ApiResponse(responseCode = "400", description = "Student is not created because of a bad request",
      content = {@Content})
  })
  @PostMapping("/students")
  public ResponseEntity<String> create(@RequestBody StudentDtoView studentDtoView) throws IncorrectUserIdException, IncorrectStudentNameException, IncorrectStudentStatusException {
    studentService.create(
      studentDtoViewMapper.fromDto(studentDtoView)
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(
      "Student is created successfully!"
    );
  }

  @Operation(summary = "Suspend a student")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Student is suspended",
      content = {@Content(mediaType = "application/json",
        schema = @Schema(implementation = StudentDto.class))}
    ),
    @ApiResponse(responseCode = "404", description = "Student can`t be suspended because of a bad request",
      content = {@Content})
  })
  @PostMapping("/students/{id}/suspend")
  public ResponseEntity<String> suspend(@PathVariable("id") Long id) throws IncorrectStatusToSuspendException, StudentNotFoundException, ActiveCoursesInStudentException, CourseNotFoundException {
    studentService.suspend(new StudentId(id));
    return ResponseEntity.status(HttpStatus.OK).body(
      "Student is suspended successfully!"
      );
  }

}
