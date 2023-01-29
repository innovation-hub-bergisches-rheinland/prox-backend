alter table "prox_user".user_profile
  add column visible_in_public_search boolean default false;

update "prox_user".user_profile
  set visible_in_public_search = lp.visible_in_public_search
from "prox_user".lecturer_profile lp
  where lp.id = user_profile.lecturer_profile_id;

alter table "prox_user".lecturer_profile drop column visible_in_public_search;