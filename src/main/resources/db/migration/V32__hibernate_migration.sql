-- Hibernate 6.2 changes mapping from tinyint to smallint
-- https://docs.jboss.org/hibernate/orm/6.2/migration-guide/migration-guide.html#ddl-implicit-datatype-enum

alter table prox_organization.membership alter column "role" type smallint using "role"::smallint;
alter table prox_organization.organization_social_media_handles alter column "social_media_handles_key" type smallint using "social_media_handles_key"::smallint;