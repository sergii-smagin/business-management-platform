package io.github.sergiismagin.businessmanagementplatform.domain.warehouse;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WarehouseTest {
    @Test
    void shouldCreateWarehouseWithValidArguments() {
        UUID id = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();
        String name = "Test Warehouse";

        Warehouse warehouse = new Warehouse(id, organizationId, name);

        assertEquals(id, warehouse.getId());
        assertEquals(organizationId, warehouse.getOrganizationId());
        assertEquals(name, warehouse.getName());
    }

    @Test
    void shouldRejectNullId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Warehouse(null, UUID.randomUUID(), "Test Warehouse")
        );
    }

    @Test
    void shouldRejectNullOrganizationId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Warehouse(UUID.randomUUID(), null, "Test Warehouse")
        );
    }

    @Test
    void shouldRejectInvalidName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Warehouse(UUID.randomUUID(), UUID.randomUUID(), null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Warehouse(UUID.randomUUID(), UUID.randomUUID(), "")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Warehouse(UUID.randomUUID(), UUID.randomUUID(), " ")
        );
    }

    @Test
    void shouldAllowDuplicateWarehouseNames() {
        UUID organizationId = UUID.randomUUID();

        Warehouse firstWarehouse = new Warehouse(
                UUID.randomUUID(),
                organizationId,
                "Main Warehouse"
        );

        Warehouse secondWarehouse = new Warehouse(
                UUID.randomUUID(),
                organizationId,
                "Main Warehouse"
        );

        assertNotEquals(firstWarehouse.getId(), secondWarehouse.getId());
        assertEquals(firstWarehouse.getOrganizationId(), secondWarehouse.getOrganizationId());
        assertEquals(firstWarehouse.getName(), secondWarehouse.getName());
    }
}
