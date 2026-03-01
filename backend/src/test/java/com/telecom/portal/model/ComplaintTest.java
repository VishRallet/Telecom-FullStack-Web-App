package com.telecom.portal.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ComplaintTest {

    @Test
    void testGettersAndSetters() {
        Complaint complaint = new Complaint();
        complaint.setId(1L);
        complaint.setSubject("Network Issue");
        complaint.setDescription("Internet not working properly");
        complaint.setStatus("OPEN");

        LocalDateTime now = LocalDateTime.now();
        complaint.setCreatedAt(now);
        complaint.setUpdatedAt(now);

        User user = new User();
        user.setId(10L);
        user.setFirstname("Vishal");
        complaint.setUser(user);

        assertEquals(1L, complaint.getId());
        assertEquals("Network Issue", complaint.getSubject());
        assertEquals("Internet not working properly", complaint.getDescription());
        assertEquals("OPEN", complaint.getStatus());
        assertEquals(now, complaint.getCreatedAt());
        assertEquals(now, complaint.getUpdatedAt());
        assertEquals(user, complaint.getUser());
    }

    @Test
    void testBuilder() {
        User user = new User();
        user.setId(20L);

        LocalDateTime now = LocalDateTime.now();

        Complaint complaint = Complaint.builder()
                .id(2L)
                .subject("Billing Error")
                .description("Charged extra for last month")
                .status("IN_PROGRESS")
                .createdAt(now)
                .updatedAt(now)
                .user(user)
                .build();

        assertEquals(2L, complaint.getId());
        assertEquals("Billing Error", complaint.getSubject());
        assertEquals("Charged extra for last month", complaint.getDescription());
        assertEquals("IN_PROGRESS", complaint.getStatus());
        assertEquals(now, complaint.getCreatedAt());
        assertEquals(now, complaint.getUpdatedAt());
        assertEquals(user, complaint.getUser());
    }

    @Test
    void testEqualsAndHashCode() {
        User user = new User();
        user.setId(5L);

        Complaint c1 = Complaint.builder()
                .id(100L)
                .subject("Test")
                .user(user)
                .build();

        Complaint c2 = Complaint.builder()
                .id(100L)
                .subject("Test")
                .user(user)
                .build();

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testToString() {
        Complaint complaint = new Complaint();
        complaint.setId(50L);
        complaint.setSubject("Slow Speed");
        complaint.setDescription("4G speed is too slow");

        String result = complaint.toString();
        assertTrue(result.contains("Slow Speed"));
        assertTrue(result.contains("4G speed is too slow"));
    }
}
