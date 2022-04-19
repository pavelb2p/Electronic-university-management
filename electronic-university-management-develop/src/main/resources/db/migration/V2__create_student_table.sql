CREATE TABLE eum_schema.student (
  id int NOT NULL AUTO_INCREMENT,
  status     enum ('active','suspended') NULL,
  name varchar(200) NOT NULL,
  user_id int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY Student_id_name_index (`id`,`name`),
  KEY fc_userId (`user_id`),
  CONSTRAINT fc_userId FOREIGN KEY (`user_id`) REFERENCES user (`id`)
);
CREATE UNIQUE INDEX fc_unique_userId
ON student(user_id);