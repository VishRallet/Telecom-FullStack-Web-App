package com.telecom.portal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void testGettersAndSetters() {
        LoginRequest request = new LoginRequest();
        request.setMobileNumber("9876543210");
        request.setPassword("securePass");

        assertEquals("9876543210", request.getMobileNumber());
        assertEquals("securePass", request.getPassword());
    }

    @Test
    void testEqualsAndHashCode() {
        LoginRequest req1 = new LoginRequest();
        req1.setMobileNumber("9876543210");
        req1.setPassword("securePass");

        LoginRequest req2 = new LoginRequest();
        req2.setMobileNumber("9876543210");
        req2.setPassword("securePass");

        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());
    }

    @Test
    void testToString() {
        LoginRequest request = new LoginRequest();
        request.setMobileNumber("9876543210");
        request.setPassword("securePass");

        String toString = request.toString();
        assertTrue(toString.contains("9876543210"));
        assertTrue(toString.contains("securePass"));
    }
}
