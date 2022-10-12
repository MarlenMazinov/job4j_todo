alter table task
add column user_id int not null references users(id)