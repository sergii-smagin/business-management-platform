package io.github.sergiismagin.businessmanagementplatform.domain.warehouse;

import java.util.UUID;

public class Warehouse {
    private final UUID id;
    private final UUID organizationId;
    private String name;

    public Warehouse(UUID id, UUID organizationId, String name) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        if (organizationId == null) {
            throw new IllegalArgumentException("Organization Id cannot be null");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        this.id = id;
        this.organizationId = organizationId;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public String getName() {
        return name;
    }
}
