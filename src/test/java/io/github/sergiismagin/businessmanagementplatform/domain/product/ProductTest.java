package io.github.sergiismagin.businessmanagementplatform.domain.product;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {
    @Test
    void shouldCreateProductWithValidArguments() {
        UUID id = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();
        String sku = "Test SKU";
        String name = "Test Product";

        Product product = new Product(id, organizationId, sku, name);

        assertEquals(id, product.getId());
        assertEquals(organizationId, product.getOrganizationId());
        assertEquals(sku, product.getSku());
        assertEquals(name, product.getName());
    }

    @Test
    void shouldRejectNullId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Product(null, UUID.randomUUID(), "Test SKU", "Test Product")
        );
    }

    @Test
    void shouldRejectNullOrganizationId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Product(UUID.randomUUID(), null, "Test SKU", "Test Product")
        );
    }

    @Test
    void shouldRejectInvalidSku() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Product(UUID.randomUUID(), UUID.randomUUID(), null, "Test Product")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Product(UUID.randomUUID(), UUID.randomUUID(), "", "Test Product")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Product(UUID.randomUUID(), UUID.randomUUID(), " ", "Test Product")
        );
    }

    @Test
    void shouldRejectInvalidName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Product(UUID.randomUUID(), UUID.randomUUID(), "Test SKU", null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Product(UUID.randomUUID(), UUID.randomUUID(), "Test SKU", "")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Product(UUID.randomUUID(), UUID.randomUUID(), "Test SKU", " ")
        );
    }
}
