-- We delete possible duplicates
delete from prox_tag.tag_collection_tags
where ctid not in (
  select min(ctid)
  from   prox_tag.tag_collection_tags
  group  by tag_collection_id, tags_id);
alter table prox_tag.tag_collection_tags add constraint uk_tag_collection_id_tag_id unique (tag_collection_id, tags_id);

alter table prox_user.user_profile add column tag_collection_id uuid;
update prox_user.user_profile set tag_collection_id = user_id where tag_collection_id is null;

insert into prox_tag.tag_collection (id, version,created_at, modified_at)
  (
    select user_id as id, null::int as version, current_timestamp, current_timestamp from prox_user.user_profile
                   UNION
    select id, null::int as version, current_timestamp, current_timestamp from prox_organization.organization
                    UNION
    select id, null::int as version, current_timestamp, current_timestamp from prox_project.project

  ) on conflict do nothing;

insert into prox_tag.tag_collection_tags (tag_collection_id, tags_id)
  (
    select up.user_id, ut.tags from prox_user.user_profile_tags ut
                join prox_user.user_profile up on ut.user_profile_id = up.id
) on conflict do nothing;

alter table prox_organization.organization add column tag_collection_id uuid;
update prox_organization.organization set tag_collection_id = id where tag_collection_id is null;

insert into prox_tag.tag_collection_tags (tag_collection_id, tags_id)
  (
    select organization_id, tags from prox_organization.organization_tags
  ) on conflict do nothing;

alter table prox_project.project add column tag_collection_id uuid;
update prox_project.project set tag_collection_id = id where tag_collection_id is null;

insert into prox_tag.tag_collection_tags (tag_collection_id, tags_id)
  (
    select project_id, tags from prox_project.project_tags
  ) on conflict do nothing;


drop table prox_user.user_profile_tags;
drop table prox_organization.organization_tags;
drop table prox_project.project_tags;