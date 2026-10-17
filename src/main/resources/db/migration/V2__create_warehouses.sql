CREATE TABLE warehouses (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL,
    name TEXT NOT NULL,
    CONSTRAINT chk_warehouses_name_not_blank
        CHECK (btrim(name) <> ''),
    CONSTRAINT fk_warehouses_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id)
);
