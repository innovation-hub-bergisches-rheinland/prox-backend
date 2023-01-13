drop table prox_user_starred_projects;
drop table prox_user;

create table star_collection
(
  id          uuid,
  user_id     uuid,

  version     INT,
  created_at  timestamp,
  modified_at timestamp,
  primary key (id)
);

create table star_collection_starred_projects
(
  star_collection_id uuid,
  starred_projects   uuid,
  foreign key (star_collection_id) references star_collection (id)
);