alter table organization add column contact_email varchar(255);
alter table organization add column founding_date varchar(255);
alter table organization add column headquarter varchar(255);
alter table organization add column homepage varchar(255);
alter table organization add column number_of_employees varchar(255);
alter table organization add column vita varchar(255);

alter table organization_profile_quarters drop constraint FK5bq3c9c8gdmvsv7l07jakaa7j;
alter table organization_profile_social_media_handles drop constraint FKu28j1w4fjs7xp9eoxb8fo3at;

alter table organization_profile_quarters rename to organization_quarters;
alter table organization_quarters rename column organization_profile_id to organization_id;
alter table organization_profile_social_media_handles rename to organization_social_media_handles;
alter table organization_social_media_handles rename column organization_profile_id to organization_id;

alter table organization_quarters add constraint FK5bq3c9c8gdmvsv7l07jakaa7j foreign key (organization_id) references organization;
alter table organization_social_media_handles add constraint FKu28j1w4fjs7xp9eoxb8fo3at foreign key (organization_id) references organization;

alter table organization drop constraint FKbb80osvtpxo7xqu5tcumrjifp;
alter table organization drop column profile_id;

alter table lecturer add column affiliation varchar(255);
alter table lecturer add column college_page varchar(255);
alter table lecturer add column consultation_hour varchar(255);
alter table lecturer add column email varchar(255);
alter table lecturer add column homepage varchar(255);
alter table lecturer add column room varchar(255);
alter table lecturer add column subject varchar(255);
alter table lecturer add column telephone varchar(255);
alter table lecturer add column vita varchar(255);

alter table lecturer_profile_publications drop constraint FKdqx6rbygg8o9gbwer6kidkvev;
alter table lecturer_profile_publications rename to lecturer_publications;
alter table lecturer_publications rename column lecturer_profile_id to lecturer_id;
alter table lecturer_publications add constraint FKdqx6rbygg8o9gbwer6kidkvev foreign key (lecturer_id) references lecturer;

alter table lecturer drop constraint FKrpn3up2qyqcnicf75vkfurjey;
alter table lecturer drop column profile_id;

drop table lecturer_profile;
drop table organization_profile;

alter table membership drop column member_id;
alter table membership rename column id to member_id;
alter table organization_members rename column members_id to members_member_id;