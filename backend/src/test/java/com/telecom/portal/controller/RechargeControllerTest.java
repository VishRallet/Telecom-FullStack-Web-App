package com.telecom.portal.controller;

import com.telecom.portal.model.Plan;
import com.telecom.portal.model.Recharge;
import com.telecom.portal.model.User;
import com.telecom.portal.service.RechargeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RechargeControllerTest {

    @Mock
    private RechargeService rechargeService;

    @InjectMocks
    private RechargeController rechargeController;

    private Recharge recharge;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        User user = new User();
        user.setId(1L);
        user.setFirstname("Alice");

        Plan plan = new Plan();
        plan.setId(101L);
        plan.setPlanName("Monthly Pack");
        plan.setPrice(299.0);

        recharge = Recharge.builder()
                .id(1L)
                .rechargeDate(LocalDateTime.now())
                .status("SUCCESS")
                .user(user)
                .plan(plan)
                .build();
    }

    @Test
    void testCreateRecharge() {
        when(rechargeService.createRecharge(1L, 101L)).thenReturn(recharge);

        Recharge result = rechargeController.createRecharge(1L, 101L);

        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
        assertEquals(299.0, result.getPlan().getPrice());
        verify(rechargeService, times(1)).createRecharge(1L, 101L);
    }

    @Test
    void testGetRechargesByUser() {
        when(rechargeService.getRechargesByUser(1L)).thenReturn(List.of(recharge));

        List<Recharge> result = rechargeController.getRechargesByUser(1L);

        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getUser().getFirstname());
        verify(rechargeService, times(1)).getRechargesByUser(1L);
    }

    @Test
    void testGetRechargesByPlan() {
        when(rechargeService.getRechargesByPlan(101L)).thenReturn(List.of(recharge));

        List<Recharge> result = rechargeController.getRechargesByPlan(101L);

        assertEquals(1, result.size());
        assertEquals("Monthly Pack", result.get(0).getPlan().getPlanName());
        verify(rechargeService, times(1)).getRechargesByPlan(101L);
    }

    @Test
    void testGetAllRecharges() {
        when(rechargeService.getAllRecharges()).thenReturn(List.of(recharge));

        List<Recharge> result = rechargeController.getAllRecharges();

        assertEquals(1, result.size());
        assertEquals("SUCCESS", result.get(0).getStatus());
        verify(rechargeService, times(1)).getAllRecharges();
    }
}
