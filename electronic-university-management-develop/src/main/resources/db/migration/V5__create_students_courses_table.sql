CREATE TABLE `students_courses` (
  `student_id` int NOT NULL,
  `course_id` int NOT NULL,
  KEY `students_courses_student_id_fk` (`student_id`),
  KEY `students_courses_course_id_fk` (`course_id`),
  CONSTRAINT `students_courses_course_id_fk` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
  CONSTRAINT `students_courses_student_id_fk` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)
)