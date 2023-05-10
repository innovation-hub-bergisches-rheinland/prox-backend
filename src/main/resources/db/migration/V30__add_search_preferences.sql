create table prox_user.project_search
(
  id      uuid primary key,
  enabled boolean not null
);

create table prox_user.project_search_disciplines
(
  project_search_id uuid not null,
  disciplines varchar(255) not null,
  unique (project_search_id, disciplines)
);

create table prox_user.project_search_module_types
(
  project_search_id uuid not null,
  module_types varchar(255) not null,
  unique (project_search_id, module_types)
);

create table prox_user.organization_search
(
  id      uuid primary key,
  enabled boolean not null
);

create table prox_user.lecturer_search
(
  id      uuid primary key,
  enabled boolean not null
);

create table prox_user.search_preferences
(
  id uuid primary key,
  user_id uuid not null unique,
  project_search_id uuid,
  organization_search_id uuid,
  lecturer_search_id uuid,
  tag_collection_id uuid,
  created_at timestamp,
  modified_at timestamp,
  foreign key (project_search_id) references prox_user.project_search(id),
  foreign key (organization_search_id) references prox_user.organization_search(id),
  foreign key (lecturer_search_id) references prox_user.lecturer_search(id)
);