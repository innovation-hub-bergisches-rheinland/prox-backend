create table user_profile
(
    id                       uuid,
    profile_type             varchar(255),

    visible_in_public_search boolean,
    user_id                  uuid,
    display_name             varchar(255),
    avatar_key               varchar(255),

    version                  INT,
    created_at               timestamp,
    modified_at              timestamp,
    unique (user_id),
    primary key (id)
);

create table user_profile_tags
(
    user_profile_id uuid,
    tags            uuid,
    foreign key (user_profile_id) references user_profile (id)
);

insert into user_profile (id, profile_type, visible_in_public_search, user_id, display_name, avatar_key, version,
                          created_at, modified_at)

select id,
       'Lecturer',
       visible_in_public_search,
       user_id,
       name,
       avatar_key,
       version,
       created_at,
       modified_at
from lecturer;

insert into user_profile_tags (user_profile_id, tags)
select lecturer_id, tags from lecturer_tags;

alter table lecturer drop column visible_in_public_search;
alter table lecturer drop column user_id;
alter table lecturer drop column name;
alter table lecturer drop column avatar_key;
alter table lecturer drop column version;
alter table lecturer drop column created_at;
alter table lecturer drop column modified_at;

drop table lecturer_tags;
