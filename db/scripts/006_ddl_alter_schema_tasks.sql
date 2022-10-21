alter table tasks add column priority int
    references priorities(id);