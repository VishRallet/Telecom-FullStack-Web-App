package com.telecom.portal.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void testEnumValues() {
        Role[] roles = Role.values();
        assertEquals(2, roles.length);
        assertTrue(contains(roles, Role.USER));
        assertTrue(contains(roles, Role.ADMIN));
    }

    @Test
    void testValueOf() {
        assertEquals(Role.USER, Role.valueOf("USER"));
        assertEquals(Role.ADMIN, Role.valueOf("ADMIN"));
    }

    @Test
    void testEnumNames() {
        assertEquals("USER", Role.USER.name());
        assertEquals("ADMIN", Role.ADMIN.name());
    }

    private boolean contains(Role[] roles, Role role) {
        for (Role r : roles) {
            if (r == role) return true;
        }
        return false;
    }
}
