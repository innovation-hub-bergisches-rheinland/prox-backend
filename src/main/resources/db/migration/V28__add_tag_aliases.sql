create table prox_tag.tag_aliases (
  tag_id uuid not null references prox_tag.tag(id),
  aliases varchar(255) not null,
  primary key (tag_id, aliases)
                                  );