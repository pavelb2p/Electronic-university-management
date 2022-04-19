package sigma.training.eum.course.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateStudentCourseDtoView(@Schema (description = "Student id") Long studentId) {}
