package com.telecom.portal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.telecom.portal.model.Plan;
import com.telecom.portal.service.PlanService;

class PlanControllerTest {

    @Mock
    private PlanService planService;

    @InjectMocks
    private PlanController planController;

    private Plan plan;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        plan = new Plan();
        plan.setId(1L);
        plan.setPlanName("Unlimited Pack");
        plan.setPrice(299.0);
        plan.setValidity("28 Days");
    }

    @Test
    void testGetAllPlans() {
        when(planService.getAllPlans()).thenReturn(List.of(plan));

        List<Plan> result = planController.getAllPlans();

        assertEquals(1, result.size());
        assertEquals("Unlimited Pack", result.get(0).getPlanName());
        verify(planService, times(1)).getAllPlans();
    }

    @Test
    void testGetPlanById() {
        when(planService.getPlanById(1L)).thenReturn(plan);

        Plan result = planController.getPlanById(1L);

        assertNotNull(result);
        assertEquals("Unlimited Pack", result.getPlanName());
        verify(planService, times(1)).getPlanById(1L);
    }

    @Test
    void testCreatePlan() {
        when(planService.savePlan(any(Plan.class))).thenReturn(plan);

        Plan result = planController.createPlan(plan);

        assertNotNull(result);
        assertEquals("Unlimited Pack", result.getPlanName());
        verify(planService, times(1)).savePlan(plan);
    }

    @Test
    void testUpdatePlan() {
        Plan updatedPlan = new Plan();
        updatedPlan.setId(1L);
        updatedPlan.setPlanName("Updated Pack");
        updatedPlan.setPrice(399.0);
        updatedPlan.setValidity("56 Days");

        when(planService.savePlan(any(Plan.class))).thenReturn(updatedPlan);

        Plan result = planController.updatePlan(1L, updatedPlan);

        assertEquals("Updated Pack", result.getPlanName());
        assertEquals(399.0, result.getPrice());
        assertEquals("56 Days", result.getValidity());
        verify(planService, times(1)).savePlan(updatedPlan);
    }

    @Test
    void testDeletePlan() {
        doNothing().when(planService).deletePlan(1L);

        planController.deletePlan(1L);

        verify(planService, times(1)).deletePlan(1L);
    }
}
