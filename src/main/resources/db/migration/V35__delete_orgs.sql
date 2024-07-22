alter table prox_organization.organization_members drop constraint FKl40alv514imwvjr8fn96m0ysc;
alter table prox_organization.organization_members add constraint FKl40alv514imwvjr8fn96m0ysc foreign key (organization_id) references prox_organization.organization on delete cascade;

alter table prox_organization.organization_social_media_handles drop constraint FKu28j1w4fjs7xp9eoxb8fo3at;
alter table prox_organization.organization_social_media_handles add constraint FKu28j1w4fjs7xp9eoxb8fo3at foreign key (organization_id) references prox_organization.organization on delete cascade;

--alter table prox_tag.tag drop constraint FKplf791g9ahuj15c8duwwcw50d;
--alter table prox_tag.tag add constraint FKplf791g9ahuj15c8duwwcw50d foreign key (organization_id) references prox_organization.organization on delete cascade;

DELETE FROM prox_organization.organization WHERE name = 'Test';