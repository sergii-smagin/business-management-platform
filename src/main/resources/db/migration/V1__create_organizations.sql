CREATE TABLE organizations (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    CONSTRAINT chk_organizations_name_not_blank
        CHECK (btrim(name) <> '')
);
