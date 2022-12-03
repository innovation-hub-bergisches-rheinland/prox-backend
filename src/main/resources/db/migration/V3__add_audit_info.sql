alter table organization add column version INT;
alter table organization add column created_at timestamp;
alter table organization add column modified_at timestamp;

alter table lecturer add column version INT;
alter table lecturer add column created_at timestamp;
alter table lecturer add column modified_at timestamp;

alter table discipline add column version INT;
alter table discipline add column created_at timestamp;
alter table discipline add column modified_at timestamp;

alter table module_type add column version INT;
alter table module_type add column created_at timestamp;
alter table module_type add column modified_at timestamp;

alter table project add column version INT;
alter table project add column created_at timestamp;
alter table project add column modified_at timestamp;

alter table tag add column version INT;
alter table tag add column created_at timestamp;
alter table tag add column modified_at timestamp;

alter table tag_collection add column version INT;
alter table tag_collection add column created_at timestamp;
alter table tag_collection add column modified_at timestamp;