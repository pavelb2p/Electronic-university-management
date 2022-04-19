CREATE TABLE eum_schema.course (
  `id` int NOT NULL AUTO_INCREMENT,
  `status` enum('created','started','finished') DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `tutor_id` int NOT NULL,
  `start_date` timestamp NULL DEFAULT NULL,
  `end_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Course_tutor_id_fk` (`tutor_id`),
  CONSTRAINT `Course_tutor_id_fk` FOREIGN KEY (`tutor_id`) REFERENCES `tutor` (`id`)
)
