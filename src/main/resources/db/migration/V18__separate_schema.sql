create schema if not exists "prox_project";
create schema if not exists "prox_organization";
create schema if not exists "prox_star";
create schema if not exists "prox_tag";
create schema if not exists "prox_user";

alter table curriculum_context
  set schema "prox_project";
alter table curriculum_context_disciplines
  set schema "prox_project";
alter table curriculum_context_module_types
  set schema "prox_project";
alter table discipline
  set schema "prox_project";
alter table lecturer_profile
  set schema "prox_user";
alter table lecturer_profile_publications
  set schema "prox_user";
alter table member
  set schema "prox_organization";
alter table membership
  set schema "prox_organization";
alter table module_type
  set schema "prox_project";
alter table module_type_disciplines
  set schema "prox_project";
alter table organization
  set schema "prox_organization";
alter table organization_members
  set schema "prox_organization";
alter table organization_social_media_handles
  set schema "prox_organization";
alter table organization_tags
  set schema "prox_organization";
alter table project
  set schema "prox_project";
alter table project_interested_users
  set schema "prox_project";
alter table project_supervisors
  set schema "prox_project";
alter table project_tags
  set schema "prox_project";
alter table star_collection
  set schema "prox_star";
alter table star_collection_starred_projects
  set schema "prox_star";
alter table tag
  set schema "prox_tag";
alter table tag_collection
  set schema "prox_tag";
alter table tag_collection_tags
  set schema "prox_tag";
alter table user_profile
  set schema "prox_user";
alter table user_profile_tags
  set schema "prox_user";