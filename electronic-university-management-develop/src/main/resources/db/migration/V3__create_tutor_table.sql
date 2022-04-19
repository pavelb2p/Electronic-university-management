CREATE TABLE eum_schema.tutor

(
    `id`      int NOT NULL AUTO_INCREMENT,
    `status`  varchar(200) DEFAULT NULL,
    `name`    varchar(200) DEFAULT NULL,
    `user_id` int          DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fc_tutor_userId` (`user_id`),
    CONSTRAINT `fc_tutor_userId` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

create unique index unique_userId
    on tutor (user_id);