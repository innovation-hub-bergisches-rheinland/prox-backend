create table project_interested_users (
  project_id uuid,
  user_id uuid,
  foreign key (project_id) references project(id)
);