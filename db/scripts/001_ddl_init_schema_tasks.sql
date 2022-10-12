create table if not exists task
(
    id serial primary key,
    name text unique,
    description text,
    created timestamp,
    done boolean
)