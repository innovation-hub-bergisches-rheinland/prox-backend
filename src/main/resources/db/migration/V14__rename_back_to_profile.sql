alter table prox_user_tags rename column prox_user_id to user_profile_id;

alter table prox_user_tags rename to user_profile_tags;
alter table prox_user rename to user_profile;

alter table lecturer rename to lecturer_profile;
alter table lecturer_publications rename to lecturer_profile_publications;
alter table lecturer_profile_publications rename column lecturer_id to lecturer_profile_id;