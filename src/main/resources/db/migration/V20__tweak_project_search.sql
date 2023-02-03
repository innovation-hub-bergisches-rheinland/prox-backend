create or replace function f_concat_ws(text, variadic text[])
  returns text
  language sql
  immutable parallel safe as
'select array_to_string($2, $1)';

alter table prox_project.project
  add column document tsvector
    generated always as (to_tsvector('german', f_concat_ws(' ', title, summary, description)))
      STORED;
create index search_index_idx on prox_project.project using gin (document);

