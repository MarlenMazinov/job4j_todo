alter table task add column priority_id int
    references priorities(id);