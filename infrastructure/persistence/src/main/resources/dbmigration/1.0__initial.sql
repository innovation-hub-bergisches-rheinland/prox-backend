-- apply changes
create table discipline_entity (
  key                           varchar(255) not null,
  name                          varchar(255) not null,
  version                       bigint not null,
  when_created                  timestamptz not null,
  when_modified                 timestamptz not null,
  constraint pk_discipline_entity primary key (key)
);

create table lecturer_entity (
  id                            uuid not null,
  name                          varchar(255) not null,
  user_id                       uuid,
  affiliation                   varchar(255),
  subject                       varchar(255),
  vita                          varchar(255),
  room                          varchar(255),
  consultation_hour             varchar(255),
  email                         varchar(255),
  telephone                     varchar(255),
  homepage                      varchar(255),
  college_page                  varchar(255),
  version                       bigint not null,
  when_created                  timestamptz not null,
  when_modified                 timestamptz not null,
  constraint pk_lecturer_entity primary key (id)
);

create table lecturer_entity_tags (
  lecturer_entity_id            uuid not null,
  value                         uuid not null
);

create table lecturer_entity_publications (
  lecturer_entity_id            uuid not null,
  value                         varchar(255) not null
);

create table module_entity (
  key                           varchar(255) not null,
  name                          varchar(255) not null,
  active                        boolean default false not null,
  version                       bigint not null,
  when_created                  timestamptz not null,
  when_modified                 timestamptz not null,
  constraint pk_module_entity primary key (key)
);

create table module_entity_disciplines (
  module_entity_key             varchar(255) not null,
  value                         varchar(255) not null
);

create table organization_entity (
  id                            uuid not null,
  name                          varchar(255) not null,
  founding_date                 varchar(255),
  number_of_employees           varchar(255),
  homepage                      varchar(255),
  contact_email                 varchar(255),
  vita                          varchar(255),
  headquarter                   varchar(255),
  version                       bigint not null,
  when_created                  timestamptz not null,
  when_modified                 timestamptz not null,
  constraint pk_organization_entity primary key (id)
);

create table organization_entity_quarters (
  organization_entity_id        uuid not null,
  value                         varchar(255) not null
);

create table organization_entity_social_media (
  organization_entity_id        uuid not null,
  mkey                          varchar(255) not null,
  value                         varchar(255) not null
);

create table organization_entity_tags (
  organization_entity_id        uuid not null,
  value                         uuid not null
);

create table organization_membership_entity (
  organization_id               uuid not null,
  user_id                       uuid not null,
  organization_entity_id        uuid not null,
  role                          varchar(6) not null,
  version                       bigint not null,
  when_created                  timestamptz not null,
  when_modified                 timestamptz not null,
  constraint ck_organization_membership_entity_role check ( role in ('MEMBER','ADMIN')),
  constraint pk_organization_membership_entity primary key (organization_id,user_id)
);

create table project_entity (
  id                            uuid not null,
  creator                       uuid not null,
  organization                  uuid,
  title                         varchar(255) not null,
  summary                       varchar(255),
  description                   varchar(255),
  requirement                   varchar(255),
  state                         varchar(9),
  updated_at                    timestamptz,
  time_box_start                date,
  time_box_end                  date,
  version                       bigint not null,
  when_created                  timestamptz not null,
  when_modified                 timestamptz not null,
  constraint ck_project_entity_state check ( state in ('PROPOSED','ARCHIVED','STALE','OFFERED','RUNNING','COMPLETED')),
  constraint pk_project_entity primary key (id)
);

create table project_entity_disciplines (
  project_entity_id             uuid not null,
  value                         varchar(255) not null
);

create table project_entity_modules (
  project_entity_id             uuid not null,
  value                         varchar(255) not null
);

create table project_entity_supervisors (
  project_entity_id             uuid not null,
  value                         uuid not null
);

create table project_entity_tags (
  project_entity_id             uuid not null,
  value                         uuid not null
);

create table tag_entity (
  id                            uuid not null,
  tag                           varchar(255) not null,
  version                       bigint not null,
  when_created                  timestamptz not null,
  when_modified                 timestamptz not null,
  constraint uq_tag_entity_tag unique (tag),
  constraint pk_tag_entity primary key (id)
);

create table user_entity (
  id                            uuid not null,
  name                          varchar(255) not null,
  email                         varchar(255) not null,
  version                       bigint not null,
  when_created                  timestamptz not null,
  when_modified                 timestamptz not null,
  constraint pk_user_entity primary key (id)
);

-- foreign keys and indices
create index ix_lecturer_entity_tags_lecturer_entity_id on lecturer_entity_tags (lecturer_entity_id);
alter table lecturer_entity_tags add constraint fk_lecturer_entity_tags_lecturer_entity_id foreign key (lecturer_entity_id) references lecturer_entity (id) on delete restrict on update restrict;

create index ix_lecturer_entity_publications_lecturer_entity_id on lecturer_entity_publications (lecturer_entity_id);
alter table lecturer_entity_publications add constraint fk_lecturer_entity_publications_lecturer_entity_id foreign key (lecturer_entity_id) references lecturer_entity (id) on delete restrict on update restrict;

create index ix_module_entity_disciplines_module_entity_key on module_entity_disciplines (module_entity_key);
alter table module_entity_disciplines add constraint fk_module_entity_disciplines_module_entity_key foreign key (module_entity_key) references module_entity (key) on delete restrict on update restrict;

create index ix_organization_entity_quarters_organization_entity_id on organization_entity_quarters (organization_entity_id);
alter table organization_entity_quarters add constraint fk_organization_entity_quarters_organization_entity_id foreign key (organization_entity_id) references organization_entity (id) on delete restrict on update restrict;

create index ix_organization_entity_social_media_organization_entity_id on organization_entity_social_media (organization_entity_id);
alter table organization_entity_social_media add constraint fk_organization_entity_social_media_organization_entity_id foreign key (organization_entity_id) references organization_entity (id) on delete restrict on update restrict;

create index ix_organization_entity_tags_organization_entity_id on organization_entity_tags (organization_entity_id);
alter table organization_entity_tags add constraint fk_organization_entity_tags_organization_entity_id foreign key (organization_entity_id) references organization_entity (id) on delete restrict on update restrict;

create index ix_organization_membership_entity_organization_entity_id on organization_membership_entity (organization_entity_id);
alter table organization_membership_entity add constraint fk_organization_membership_entity_organization_entity_id foreign key (organization_entity_id) references organization_entity (id) on delete restrict on update restrict;

create index ix_project_entity_disciplines_project_entity_id on project_entity_disciplines (project_entity_id);
alter table project_entity_disciplines add constraint fk_project_entity_disciplines_project_entity_id foreign key (project_entity_id) references project_entity (id) on delete restrict on update restrict;

create index ix_project_entity_modules_project_entity_id on project_entity_modules (project_entity_id);
alter table project_entity_modules add constraint fk_project_entity_modules_project_entity_id foreign key (project_entity_id) references project_entity (id) on delete restrict on update restrict;

create index ix_project_entity_supervisors_project_entity_id on project_entity_supervisors (project_entity_id);
alter table project_entity_supervisors add constraint fk_project_entity_supervisors_project_entity_id foreign key (project_entity_id) references project_entity (id) on delete restrict on update restrict;

create index ix_project_entity_tags_project_entity_id on project_entity_tags (project_entity_id);
alter table project_entity_tags add constraint fk_project_entity_tags_project_entity_id foreign key (project_entity_id) references project_entity (id) on delete restrict on update restrict;

