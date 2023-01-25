alter table user_profile add column email varchar(255);
alter table user_profile add column homepage varchar(255);
alter table user_profile add column telephone varchar(255);

update user_profile
  set
    email = lp.email,
    homepage = lp.homepage,
    telephone = lp.telephone
from lecturer_profile lp where lp.id = user_profile.lecturer_profile_id;

alter table lecturer_profile drop column email;
alter table lecturer_profile drop column homepage;
alter table lecturer_profile drop column telephone;