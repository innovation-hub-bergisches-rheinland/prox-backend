create table prox_user.search_history
(
  id          uuid primary key,
  user_id     uuid not null,
  created_at  timestamp,
  modified_at timestamp
);

create table prox_user.project_search_entry
(
  id          uuid primary key,
  text        varchar(255) not null,

  created_at  timestamp,
  modified_at timestamp
);

create table prox_user.search_history_project_searches
(
  search_history_id   uuid not null,
  project_searches_id uuid not null,
  foreign key (search_history_id) references prox_user.search_history (id),
  foreign key (project_searches_id) references prox_user.project_search_entry (id)
);

create table prox_user.project_search_entry_disciplines
(
  project_search_entry_id uuid,
  disciplines             varchar(255) not null,
  foreign key (project_search_entry_id) references prox_user.project_search_entry (id)
);

create table prox_user.project_search_entry_module_types
(
  project_search_entry_id uuid,
  module_types            varchar(255) not null,
  foreign key (project_search_entry_id) references prox_user.project_search_entry (id)
);

create table prox_user.project_search_entry_states
(
  project_search_entry_id uuid,
  states                  varchar(255) not null,
  foreign key (project_search_entry_id) references prox_user.project_search_entry (id)
);

create table prox_user.project_search_entry_tags
(
  project_search_entry_id uuid,
  tags                    uuid not null,
  foreign key (project_search_entry_id) references prox_user.project_search_entry (id)
);