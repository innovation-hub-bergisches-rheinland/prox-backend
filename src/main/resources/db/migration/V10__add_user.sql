create table prox_user (
  id uuid,

  version INT,
  created_at timestamp,
  modified_at timestamp,
  primary key (id)
);

create table prox_user_starred_projects(
  prox_user_id uuid,
  starred_projects uuid,
  foreign key (prox_user_id) references prox_user(id)
);