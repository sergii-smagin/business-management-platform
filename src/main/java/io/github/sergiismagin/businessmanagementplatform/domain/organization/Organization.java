package io.github.sergiismagin.businessmanagementplatform.domain.organization;

import java.util.UUID;

public class Organization {
    private final UUID id;
    private String name;

    public Organization(UUID id, String name) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
