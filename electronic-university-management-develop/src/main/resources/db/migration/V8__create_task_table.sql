create table eum_schema.task
(
    `id` int auto_increment primary key,
    `status` enum('started','inreview','completed') DEFAULT NULL,
    `completed_task_url` varchar(200) DEFAULT NULL,
    `assignment_id` int NOT NULL,
    `student_id` int NOT NULL,
    KEY `Task_assignment_id_fk` (`assignment_id`),
    CONSTRAINT `Task_assignment_id_fk` FOREIGN KEY (`assignment_id`) REFERENCES `assignment` (`id`),
    KEY `Task_student_id_fk` (`student_id`),
    CONSTRAINT `Task_student_id_fk` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
    `mark` int DEFAULT NULL
    check ('mark'>=0 and 'mark'<=100)
);