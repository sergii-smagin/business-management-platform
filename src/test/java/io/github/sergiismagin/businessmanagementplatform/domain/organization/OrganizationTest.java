package io.github.sergiismagin.businessmanagementplatform.domain.organization;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrganizationTest {
    @Test
    void shouldCreateOrganizationWithValidArguments() {
        UUID id = UUID.randomUUID();
        String name = "Test Organization";

        Organization organization = new Organization(id, name);

        assertEquals(id, organization.getId());
        assertEquals(name, organization.getName());
    }

    @Test
    void shouldRejectNullId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Organization(null, "Test Organization")
        );
    }

    @Test
    void shouldRejectInvalidName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Organization(UUID.randomUUID(), null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Organization(UUID.randomUUID(), "")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Organization(UUID.randomUUID(), " ")
        );
    }
}
