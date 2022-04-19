create table eum_schema.user
(
    id       int auto_increment
        primary key,
    login    varchar(200)                       not null,
    password varchar(200)                       not null,
    role     enum ('student', 'tutor', 'admin') null,
    constraint `user-info_userLogin_uindex`
        unique (login)
);
