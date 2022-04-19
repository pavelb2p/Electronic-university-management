package sigma.training.eum.tutor.presentation.controller;

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
import sigma.training.eum.student.exception.StudentNotFoundException;
import sigma.training.eum.tutor.dictionary.Status;
import sigma.training.eum.tutor.exception.*;
import sigma.training.eum.tutor.presentation.dto.CreateTutorDtoView;
import sigma.training.eum.tutor.presentation.dto.TutorDto;
import sigma.training.eum.tutor.mapper.dto.CreateDtoViewMapper;
import sigma.training.eum.tutor.mapper.dto.TutorDtoMapper;
import sigma.training.eum.tutor.service.TutorService;
import sigma.training.eum.tutor.service.type.Tutor;
import sigma.training.eum.tutor.service.type.TutorId;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Tag(name = "Basic tutor operations")
@SecurityRequirement(name = "basicAuth")
public class TutorController {

  private final TutorService tutorService;
  private final TutorDtoMapper tutorDtoMapper;
  private final CreateDtoViewMapper createTutorDtoViewMapper;

  @Operation(summary = "Get a list of tutors")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Found tutor list",
      content = {@Content(mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = TutorDto.class)))}
    )
  })
  @GetMapping("/tutors")
  @ResponseBody
  public List<TutorDto> getStudents(@RequestParam (required = false, name = "status") Optional<Status> status) {
    return status.isPresent() ? tutorService.getAll(status.get()).stream().map(tutorDtoMapper::toDto).toList() :
      tutorService.getAll().stream().map(tutorDtoMapper::toDto).toList();
  }

  @Operation(summary = "Get a tutor by Id")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Tutor is found",
      content = {@Content(mediaType = "application/json",
        schema = @Schema(implementation = TutorDto.class))}
    ),
    @ApiResponse(responseCode = "404", description = "Tutor not found",
      content = {@Content})
  })
  @GetMapping("/tutors/{id}")
  @ResponseBody
  public TutorDto getById(@PathVariable("id") Long id) throws TutorNotFoundException, IncorrectUserIdException {
    Tutor tutor = tutorService.get(new TutorId(id));
    return tutorDtoMapper.toDto(tutor);
  }

  @Operation(summary = "Create a new tutor")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Tutor is created",
      content = {@Content}
    ),
    @ApiResponse(responseCode = "400", description = "Tutor is not created because of a bad request",
      content = {@Content})
  })
  @PostMapping("/tutors")
  public ResponseEntity<String> create(@RequestBody CreateTutorDtoView tutorDto) throws IncorrectUserIdException, IncorrectTutorNameException, IncorrectTutorStatusException {
    tutorService.create(
      createTutorDtoViewMapper.fromDto(tutorDto)
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(
      "Tutor is created successfully"
    );
  }

  @Operation(summary = "Suspend a tutor")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Tutor is suspended",
      content = {@Content}
    ),
    @ApiResponse(responseCode = "404", description = "Tutor can`t be suspended because of a bad request",
      content = {@Content})
  })
  @PostMapping("/tutors/{id}/suspend")
  public ResponseEntity<String> suspend(@PathVariable("id") Long id) throws IncorrectUserIdException, TutorNotFoundException, IncorrectStatusToSuspendException, ActiveCoursesInTutorException{
    tutorService.suspend(new TutorId(id));
    return ResponseEntity.status(HttpStatus.OK).body(
      "Tutor is suspended successfully"
    );
  }
}
