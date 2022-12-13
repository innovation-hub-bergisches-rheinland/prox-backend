alter table organization alter column vita type text using vita::text;
alter table lecturer alter column vita type text using vita::text;
alter table project alter column description type text using description::text;
alter table project alter column requirement type text using requirement::text;
alter table project alter column summary type varchar(10000);