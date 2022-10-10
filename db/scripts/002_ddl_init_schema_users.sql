create table if not exists users
(
    id    serial    primary    key,
    name varchar(255),
    login    varchar(255) unique,
    password text
    )