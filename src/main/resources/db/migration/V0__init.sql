create table curriculum_context (id uuid not null, primary key (id));
create table curriculum_context_disciplines (curriculum_context_id uuid not null, disciplines_key varchar(255) not null);
create table curriculum_context_module_types (curriculum_context_id uuid not null, module_types_key varchar(255) not null);
create table discipline (key varchar(255) not null, name varchar(255), primary key (key));
create table lecturer (id uuid not null, avatar_key varchar(255), name varchar(255), user_id uuid, profile_id uuid, primary key (id));
create table lecturer_tags (lecturer_id uuid not null, tags uuid);
create table lecturer_profile (id uuid not null, affiliation varchar(255), college_page varchar(255), consultation_hour varchar(255), email varchar(255), homepage varchar(255), room varchar(255), subject varchar(255), telephone varchar(255), vita varchar(255), primary key (id));
create table lecturer_profile_publications (lecturer_profile_id uuid not null, publications varchar(255));
create table member (id uuid not null, user_id uuid, primary key (id));
create table membership (id uuid not null, role int4, member_id uuid, primary key (id));
create table module_type (key varchar(255) not null, active boolean not null, name varchar(255), primary key (key));
create table module_type_disciplines (module_type_key varchar(255) not null, disciplines_key varchar(255) not null);
create table organization (id uuid not null, logo_key varchar(255), name varchar(255), profile_id uuid, primary key (id));
create table organization_members (organization_id uuid not null, members_id uuid not null);
create table organization_tags (organization_id uuid not null, tags uuid);
create table organization_profile (id uuid not null, contact_email varchar(255), founding_date varchar(255), headquarter varchar(255), homepage varchar(255), number_of_employees varchar(255), vita varchar(255), primary key (id));
create table organization_profile_quarters (organization_profile_id uuid not null, quarters varchar(255));
create table organization_profile_social_media_handles (organization_profile_id uuid not null, social_media_handles varchar(255), social_media_handles_key int4 not null, primary key (organization_profile_id, social_media_handles_key));
create table project (id uuid not null, user_id uuid, description varchar(255), organization_id uuid, requirement varchar(255), state varchar(255), updated_at timestamp, summary varchar(255), end_date date, start_date date, title varchar(255), curriculum_context_id uuid, primary key (id));
create table project_supervisors (project_id uuid not null, lecturer_id uuid);
create table project_tags (project_id uuid not null, tags uuid);
create table tag (id uuid not null, tag_name varchar(255), primary key (id));
create table tag_collection (id uuid not null, primary key (id));
create table tag_collection_tags (tag_collection_id uuid not null, tags_id uuid not null);
alter table organization_members add constraint UK_3qoxo6mk3ake4mept5t1krtm3 unique (members_id);
alter table tag add constraint UK_qp93jyuw586kcgdajsb3tfbjy unique (tag_name);
alter table curriculum_context_disciplines add constraint FKkrn5vnmdhq4va1fqjpulv8nbv foreign key (disciplines_key) references discipline;
alter table curriculum_context_disciplines add constraint FK6rtjpimxh0p3d9cq1fha86jr0 foreign key (curriculum_context_id) references curriculum_context;
alter table curriculum_context_module_types add constraint FKmw35g83u02spti010jukpvl0r foreign key (module_types_key) references module_type;
alter table curriculum_context_module_types add constraint FKosv8umkli787mi24cgshstj9l foreign key (curriculum_context_id) references curriculum_context;
alter table lecturer add constraint FKrpn3up2qyqcnicf75vkfurjey foreign key (profile_id) references lecturer_profile;
alter table lecturer_tags add constraint FKbemrkfcd6u19dymfmo8s3vr1r foreign key (lecturer_id) references lecturer;
alter table lecturer_profile_publications add constraint FKdqx6rbygg8o9gbwer6kidkvev foreign key (lecturer_profile_id) references lecturer_profile;
alter table membership add constraint FKej551mo6x4epei474ys7ojb7c foreign key (member_id) references member;
alter table module_type_disciplines add constraint FKhujl09eqhkyauk00bma4ji69f foreign key (disciplines_key) references discipline;
alter table module_type_disciplines add constraint FKj92f4m2o9ys5uh5yj520d8twu foreign key (module_type_key) references module_type;
alter table organization add constraint FKbb80osvtpxo7xqu5tcumrjifp foreign key (profile_id) references organization_profile;
alter table organization_members add constraint FKn6ktojmpx50rwvnrjtabfm2m2 foreign key (members_id) references membership;
alter table organization_members add constraint FKl40alv514imwvjr8fn96m0ysc foreign key (organization_id) references organization;
alter table organization_tags add constraint FKplf791g9ahuj15c8duwwcw50d foreign key (organization_id) references organization;
alter table organization_profile_quarters add constraint FK5bq3c9c8gdmvsv7l07jakaa7j foreign key (organization_profile_id) references organization_profile;
alter table organization_profile_social_media_handles add constraint FKu28j1w4fjs7xp9eoxb8fo3at foreign key (organization_profile_id) references organization_profile;
alter table project add constraint FKt0mtr8339hdk9tjw23dc93q0g foreign key (curriculum_context_id) references curriculum_context;
alter table project_supervisors add constraint FK4nyjnuxg7lqyxqmw5sbtnbx3o foreign key (project_id) references project;
alter table project_tags add constraint FKfvy64usu7e9x7ev6obh91q0qe foreign key (project_id) references project;
alter table tag_collection_tags add constraint FKlgor9y9re8ymd8ujgqi5f5lf0 foreign key (tags_id) references tag;
alter table tag_collection_tags add constraint FKrggcam8wo243mq1adg18pwtgm foreign key (tag_collection_id) references tag_collection;
