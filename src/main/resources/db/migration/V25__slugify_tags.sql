create or replace function pg_temp.slugify(text) returns text as
$$
select lower(regexp_replace(regexp_replace(trim($1), '\s+', '-', 'g'), '-{2,}', '-', 'g'))
$$ language sql IMMUTABLE;

CREATE TEMPORARY TABLE tag_duplicates AS (SELECT id, pg_temp.slugify(tag_name) slugged, c
                                          FROM (SELECT *, COUNT(*) OVER (PARTITION BY pg_temp.slugify(tag_name)) AS c
                                                FROM prox_tag.tag) AS sub
                                          WHERE c > 1);

CREATE TEMPORARY TABLE primary_tags AS (SELECT DISTINCT ON (td.slugged) *
                                        FROM tag_duplicates td);

CREATE TEMPORARY TABLE obsolete_tags AS (
  SELECT td.*, pt.id new_primary
  FROM tag_duplicates td
         JOIN primary_tags pt on td.slugged = pt.slugged
  WHERE td.id NOT IN (SELECT id FROM primary_tags)
);

UPDATE "prox_organization".organization_tags t
SET tags = ot.new_primary
FROM obsolete_tags ot
WHERE ot.id = t.tags;

UPDATE "prox_user".user_profile_tags t
SET tags = ot.new_primary
FROM obsolete_tags ot
WHERE ot.id = t.tags;

UPDATE "prox_project".project_tags t
SET tags = ot.new_primary
FROM obsolete_tags ot
WHERE ot.id = t.tags;

UPDATE "prox_tag".tag_collection_tags t
SET tags = ot.new_primary
FROM obsolete_tags ot
WHERE ot.id = t.tags;

DELETE
FROM "prox_tag".tag
WHERE id IN (SELECT id FROM obsolete_tags);

UPDATE "prox_tag".tag
SET tag_name = lower(regexp_replace(tag_name, '\s+', '-', 'g'))
WHERE tag_name IS NOT NULL;