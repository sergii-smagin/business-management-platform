package io.github.sergiismagin.businessmanagementplatform.domain.product;

import java.util.UUID;

public class Product {
    private final UUID id;
    private final UUID organizationId;
    private String sku;
    private String name;

    public Product(UUID id, UUID organizationId, String sku, String name) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        if (organizationId == null) {
            throw new IllegalArgumentException("Organization Id cannot be null");
        }

        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU cannot be null or empty");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        this.id = id;
        this.organizationId = organizationId;
        this.sku = sku;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }
}
