alter table tasks
add column user_id int not null references users(id)