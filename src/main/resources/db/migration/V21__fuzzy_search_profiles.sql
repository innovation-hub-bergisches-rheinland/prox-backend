create extension if not exists pg_trgm;

CREATE INDEX IF NOT EXISTS trgm_name_idx on prox_organization.organization using gin (name gin_trgm_ops);
CREATE INDEX IF NOT EXISTS trgm_name_idx on prox_user.user_profile using gin (display_name gin_trgm_ops);