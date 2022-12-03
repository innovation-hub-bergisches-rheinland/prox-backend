alter table organization add column quarters varchar(255);
delete from organization_quarters where 1 = 1;
drop table organization_quarters;