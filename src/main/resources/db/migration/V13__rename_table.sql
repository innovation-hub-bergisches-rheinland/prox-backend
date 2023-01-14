alter table user_profile_tags
  rename to prox_user_tags;
alter table user_profile
  rename to prox_user;

alter table prox_user_tags rename column user_profile_id to prox_user_id;