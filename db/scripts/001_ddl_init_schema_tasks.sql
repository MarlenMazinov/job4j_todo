CREATE TABLE if not exists task
(
    id          SERIAL PRIMARY KEY,
    name        TEXT UNIQUE,
    description TEXT,
    created     TEXT,
    done        BOOLEAN
);