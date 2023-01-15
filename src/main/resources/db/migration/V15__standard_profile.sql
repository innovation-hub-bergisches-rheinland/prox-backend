create table lecturer_profile_tags
(
  lecturer_profile_id uuid not null,
  tags              uuid not null,
  foreign key (lecturer_profile_id) references lecturer_profile (id)
);

insert into lecturer_profile_tags (lecturer_profile_id, tags)
select up.id, user_profile_tags.tags
from user_profile_tags
       join user_profile up on up.id = user_profile_tags.user_profile_id
       join lecturer_profile lp on lp.id = up.id
where up.profile_type = 'Lecturer';
drop table user_profile_tags;

alter table lecturer_profile add column visible_in_public_search boolean not null default false;
update lecturer_profile set visible_in_public_search = true where id in (select id from user_profile where profile_type = 'Lecturer' and visible_in_public_search = true);
alter table user_profile drop column visible_in_public_search;

alter table user_profile add column lecturer_profile_id uuid references lecturer_profile (id);
update user_profile set lecturer_profile_id = lp.id from lecturer_profile lp where lp.id = user_profile.id;

alter table user_profile drop column profile_type;