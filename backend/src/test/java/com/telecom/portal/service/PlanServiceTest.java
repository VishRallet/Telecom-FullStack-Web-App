package com.telecom.portal.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.telecom.portal.model.Plan;
import com.telecom.portal.repository.PlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PlanServiceTest {

    @Mock
    private PlanRepository planRepository;

    @InjectMocks
    private PlanService planService;

    private Plan plan1;
    private Plan plan2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        plan1 = Plan.builder()
                .id(1L)
                .planName("Basic Plan")
                .planType("Prepaid")
                .price(199.0)
                .validity("30 Days")
                .dataLimit("10GB")
                .description("Basic internet plan")
                .build();

        plan2 = Plan.builder()
                .id(2L)
                .planName("Premium Plan")
                .planType("Postpaid")
                .price(499.0)
                .validity("90 Days")
                .dataLimit("50GB")
                .description("Premium internet plan")
                .build();
    }

    @Test
    void testGetAllPlans() {
        when(planRepository.findAll()).thenReturn(Arrays.asList(plan1, plan2));

        List<Plan> plans = planService.getAllPlans();
        assertThat(plans).hasSize(2);
        verify(planRepository, times(1)).findAll();
    }

    @Test
    void testGetPlanById_Found() {
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan1));

        Plan plan = planService.getPlanById(1L);
        assertThat(plan).isNotNull();
        assertThat(plan.getPlanName()).isEqualTo("Basic Plan");
        verify(planRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPlanById_NotFound() {
        when(planRepository.findById(3L)).thenReturn(Optional.empty());

        Plan plan = planService.getPlanById(3L);
        assertNull(plan);
        verify(planRepository, times(1)).findById(3L);
    }

    @Test
    void testSavePlan() {
        when(planRepository.save(plan1)).thenReturn(plan1);

        Plan saved = planService.savePlan(plan1);
        assertThat(saved).isEqualTo(plan1);
        verify(planRepository, times(1)).save(plan1);
    }

    @Test
    void testDeletePlan() {
        doNothing().when(planRepository).deleteById(1L);

        planService.deletePlan(1L);
        verify(planRepository, times(1)).deleteById(1L);
    }
}
