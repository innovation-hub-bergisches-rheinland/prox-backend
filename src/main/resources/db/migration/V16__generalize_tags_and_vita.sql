alter table user_profile add column vita text;
update user_profile set vita = lp.vita from lecturer_profile lp where lp.id = user_profile.id;
alter table lecturer_profile drop column vita;

create table user_profile_tags
(
  user_profile_id uuid not null,
  tags              uuid not null,
  foreign key (user_profile_id) references user_profile (id)
);
insert into user_profile_tags (user_profile_id, tags)
select lecturer_profile_id, tags
from lecturer_profile_tags;
drop table lecturer_profile_tags;