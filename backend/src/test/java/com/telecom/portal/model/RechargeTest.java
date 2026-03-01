package com.telecom.portal.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RechargeTest {

    @Test
    void testGettersAndSetters() {
        User user = new User();
        user.setId(1L);
        user.setFirstname("Vishal");

        Plan plan = Plan.builder()
                .id(101L)
                .planName("Super Saver")
                .price(299.0)
                .build();

        LocalDateTime now = LocalDateTime.now();

        Recharge recharge = new Recharge();
        recharge.setId(100L);
        recharge.setRechargeDate(now);
        recharge.setStatus("SUCCESS");
        recharge.setUser(user);
        recharge.setPlan(plan);

        assertEquals(100L, recharge.getId());
        assertEquals(now, recharge.getRechargeDate());
        assertEquals("SUCCESS", recharge.getStatus());
        assertEquals(user, recharge.getUser());
        assertEquals(plan, recharge.getPlan());
    }

    @Test
    void testBuilder() {
        User user = User.builder().id(2L).firstname("Raj").build();
        Plan plan = Plan.builder().id(202L).planName("Premium").price(499.0).build();
        LocalDateTime date = LocalDateTime.of(2025, 9, 1, 12, 30);

        Recharge recharge = Recharge.builder()
                .id(200L)
                .rechargeDate(date)
                .status("PENDING")
                .user(user)
                .plan(plan)
                .build();

        assertEquals(200L, recharge.getId());
        assertEquals(date, recharge.getRechargeDate());
        assertEquals("PENDING", recharge.getStatus());
        assertEquals(user, recharge.getUser());
        assertEquals(plan, recharge.getPlan());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime date = LocalDateTime.now();

        Recharge r1 = Recharge.builder()
                .id(1L)
                .rechargeDate(date)
                .status("SUCCESS")
                .build();

        Recharge r2 = Recharge.builder()
                .id(1L)
                .rechargeDate(date)
                .status("SUCCESS")
                .build();

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testToStringContainsFields() {
        Recharge recharge = Recharge.builder()
                .id(5L)
                .status("FAILED")
                .build();

        String result = recharge.toString();
        assertTrue(result.contains("FAILED"));
        assertTrue(result.contains("5"));
    }

    @Test
    void testUserAndPlanRelationship() {
        User user = User.builder().id(3L).firstname("Anita").build();
        Plan plan = Plan.builder().id(303L).planName("Basic Pack").price(199.0).build();

        Recharge recharge = new Recharge();
        recharge.setId(300L);
        recharge.setUser(user);
        recharge.setPlan(plan);

        assertNotNull(recharge.getUser());
        assertNotNull(recharge.getPlan());
        assertEquals("Anita", recharge.getUser().getFirstname());
        assertEquals("Basic Pack", recharge.getPlan().getPlanName());
    }
}
