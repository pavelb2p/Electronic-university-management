SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE user;
TRUNCATE student;
TRUNCATE tutor;
TRUNCATE students_courses;
TRUNCATE course;
TRUNCATE task;
TRUNCATE assignment;

SET FOREIGN_KEY_CHECKS = 1;
insert into eum_schema.user (id,login,password,role)
values (1, 'sofia', '$2a$12$oHm9JPOLRf2K0.ftm89X9ug76KRDqyIV/V4ZD8573G4XfPLzxSRo.', 'admin');
# 'sofia'

insert into eum_schema.user (id, login,password,role)
values (2,'pavlo', '$2a$12$Z7tkDAfgx7XPRxyulb8.OO6CjKDmnxv0iLUZZNHh7QdRN2ZqLXqQi', 'tutor');
# pavlo

insert into eum_schema.user (id,login,password,role)
values (3,'arsen', '$2a$12$5i6pP3t55tO1.1yX3AKdOuxI8SHb.wo0/MzpdhKDHhFGRzZzNrOYq', 'student');
# arsen

insert into eum_schema.user (id,login,password,role)
values (4, 'serhii', '$2a$12$oHm9JPOLRf2K0.ftm89X9ug76KRDqyIV/V4ZD8573G4XfPLzxSRo.', 'admin');
# 'serhii'

insert into eum_schema.user (id, login,password,role)
values (5,'elza', '$2a$12$nYtqzUVuJ0/fhjyhHhr0IONWrjEx2hQ7YYbYGT1vZ.jNLEVM9e5O6', 'tutor');
# elza

insert into eum_schema.user (id,login,password,role)
values (6,'david', '$2a$12$uwrV7vpdhdG9lZDdWBua8O3KCOZvmbI5uWJW3ymZrr5.ThjsXUkzC', 'tutor');
# david

insert into eum_schema.user (id,login,password,role)
values (7, 'mariya', '$2a$12$UeNjs9WncJsFQQ0lSDslyOLXeHYZeo3bN/7riEFU05wW.jg5TqeN2', 'tutor');
# 'mariya'

insert into eum_schema.user (id, login,password,role)
values (8,'yan', '$2a$12$RHOuuwqJAkpSyRfTrKyhDO66rMCjt0mYzdrJVs433gaePDq.mbrV6', 'student');
# yan

insert into eum_schema.user (id,login,password,role)
values (9,'lev', '$2a$12$YwvFpyfaJBylj5AVOa4wB.qj8T44z.x38SjIWQeVtmZsNIrLDmtOq', 'student');
# lev

insert into eum_schema.user (id,login,password,role)
values (10, 'alisa', '$2a$12$LLxbH6ZARL6S0OZZSb8.qu3x8t4/XXlNHAdNfpZ4T3sJ4QaHketQC', 'student');
# 'alisa'

insert into eum_schema.user (id, login,password,role)
values (11,'eva', '$2a$12$AHfp9r6KjZwS34UEWKUTvOEm2SfksG294nIwTvrKI9T0fKdxhA.jK', 'student');
# eva

# *******************************************
insert into eum_schema.student(id,status, name, user_id)
values(1, 'active', 'Student Arsen', 3);

insert into eum_schema.student(id,status, name, user_id)
values(2, 'active', 'Student Yan', 8);

insert into eum_schema.student(id,status, name, user_id)
values(3, 'active', 'Student Lev', 9);

insert into eum_schema.student(id,status, name, user_id)
values(4, 'suspended', 'Student Alisa', 10);

insert into eum_schema.student(id,status, name, user_id)
values(5, 'suspended', 'Student Eva', 11);

# ****************************************************
insert into eum_schema.tutor(id,status, name, user_id)
values(1,'active', 'Pavlo Lukich', 2);

insert into eum_schema.tutor(id,status, name, user_id)
values(2,'active', 'Sergii Arnoldovich', 4);

insert into eum_schema.tutor(id, status, name, user_id)
values(3,'active', 'Elza Karlovna', 5);

insert into eum_schema.tutor(id,status, name, user_id)
values(4,'active', 'David Markovich', 6);

insert into eum_schema.tutor(id,status, name, user_id)
values(5,'suspended', 'Mariya Serhiivna', 7);

# ****************************************************
insert into eum_schema.course(id,status,name,tutor_id, start_date, create_date)
values(1,'created','Computer science',1,null, '2020-07-04*10:00:00');

insert into eum_schema.course(id,status,name,tutor_id, start_date, create_date)
values(2,'started','Software engineering',1,'2021-12-20*14:33:56', '2020-05-01*10:44:44');

insert into eum_schema.course(id,status,name,tutor_id,start_date, create_date)
values(3,'created','Applied math',1,null, '2021-01-01*17:55:21');

insert into eum_schema.course(id,status,name,tutor_id, start_date, create_date)
values(4,'created','Cybersecurity Analyst',1,null, '2021-02-22*10:55:36');

insert into eum_schema.course(id,status,name,tutor_id,start_date, create_date)
values(5,'created','Project Planning',1,null, '2019-07-04*13:23:55');

insert into eum_schema.course(id,status,name,tutor_id, start_date, create_date)
values(6,'created','Technical Support',2, null, '2020-03-03*19:15:00');

insert into eum_schema.course(id,status,name,tutor_id, start_date, end_date, create_date)
values(7,'finished','Blockchain Scalability',2,'2021-05-19*11:22:00', '2022-01-19*13:56:11', '2021-01-05*17:00:00');

insert into eum_schema.course(id,status,name,tutor_id, start_date, end_date, create_date)
values(8,'finished','Data – What It Is',2,'2021-01-07*10:53:35', '2022-01-19*10:22:10', '2021-01-01*10:05:05');

# ****************************************************
insert into eum_schema.students_courses(student_id,course_id)
values(1,1);

insert into eum_schema.students_courses(student_id,course_id)
values(2,1);

insert into eum_schema.students_courses(student_id,course_id)
values(3,2);

# ****************************************************
insert into eum_schema.assignment (id,status,description,course_id,creation_date)
values (1, 'created', null, 1, '2020-04-09*13:23:55');

insert into eum_schema.assignment (id,status,description,course_id,creation_date)
values (2, 'created', null, 2, '2021-08-11*17:21:40');

insert into eum_schema.assignment (id,status,description,course_id,creation_date, start_date)
values (3, 'started',null, 3, '2021-10-05*18:28:54', '2021-10-07*14:14:00');

insert into eum_schema.assignment (id,status,description,course_id,creation_date, start_date)
values (4, 'started', 'R - Wine Preference Prediction', 4, '2022-01-19*11:18:20', '2022-01-19*12:11:50');

insert into eum_schema.assignment (id,status,description,course_id,creation_date, start_date)
values (5, 'started', 'JavaScript- TTT Game', 1, '2022-01-12*12:42:32', '2022-01-14*10:00:10');

insert into eum_schema.assignment (id,status,description,course_id,creation_date, start_date)
values (6, 'started', 'Kotlin - Erste bank application', 2, '2022-01-03*13:32:12', '2022-01-05*13:16:00');

insert into eum_schema.assignment (id,status,description,course_id,creation_date, start_date)
values (7, 'started', 'Swift - Injection III', 3, '2021-12-10*10:11:33', '2022-01-29*11:22:47');

insert into eum_schema.assignment (id,status,description,course_id,creation_date, start_date)
values (8, 'started', 'Go - CriptoFinancial App', 4, '2021-11-11*15:55:00','2021-11-29*12:00:47');

insert into eum_schema.assignment (id,status,description,course_id,creation_date, start_date)
values (9, 'started', 'Scala - Documentation Compiler', 2, '2021-09-06*10:00:55', '2021-09-08*11:55:13');

insert into eum_schema.assignment (id,status,description,course_id,creation_date, start_date)
values (10, 'started', 'Dart - HELSI.you', 2, '2021-03-14*10:10:10', '2021-03-14*17:00:00');

insert into eum_schema.assignment (id,status,description,course_id,creation_date, start_date, end_date)
values (11, 'finished', 'PHP - CarBook', 3, '2021-05-19*11:22:00', '2021-05-20*11:22:00', '2021-08-20*11:22:00');

insert into eum_schema.assignment (id,status,description,course_id,creation_date, start_date, end_date)
values (12, 'finished', 'Java - Schedule moon', 4, '2021-01-07*12:53:35', '2021-01-08*19:05:07', '2021-05-08*19:05:07');

# ****************************************************
insert into eum_schema.task (id,status,assignment_id,student_id)
values (1, 'started', 1, 1);

insert into eum_schema.task (id,status,assignment_id,student_id)
values (2, 'started', 2, 1);

insert into eum_schema.task (id,status,assignment_id,student_id)
values (3, 'completed', 3, 1);

insert into eum_schema.task (id,status,completed_task_url, assignment_id,student_id)
values (4, 'completed', 'https://uem.ua/completed/task/1', 4, 1);

insert into eum_schema.task (id,status, completed_task_url, assignment_id,student_id)
values (5, 'completed', 'https://uem.ua/completed/task/2',1, 1);

insert into eum_schema.task (id,status, completed_task_url, assignment_id,student_id)
values (6, 'inreview', 'https://uem.ua/completed/task/3' ,2, 1);

insert into eum_schema.task (id,status,assignment_id,student_id)
values (7, 'started', 5, 1);

insert into eum_schema.task (id,status,assignment_id,student_id)
values (8, 'started', 6, 1);

insert into eum_schema.task (id,status, completed_task_url ,assignment_id,student_id)
values (9, 'inreview', 'https://uem.ua/completed/task/5' ,7, 1);
# ****************************************************
