create table eum_schema.assignment
(
    `id` int auto_increment primary key,
    `status` enum('created','started','finished') DEFAULT NULL,
    `description` varchar(200) DEFAULT NULL,
    `course_id` int NOT NULL,
    KEY `Assignment_course_id_fk` (`course_id`),
    CONSTRAINT `Assignment_course_id_fk` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
    `creation_date` timestamp NULL DEFAULT NULL,
    `start_date` timestamp NULL DEFAULT NULL,
    `end_date` timestamp NULL DEFAULT NULL
);