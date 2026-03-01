package com.telecom.portal.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.telecom.portal.model.Plan;
import com.telecom.portal.model.Recharge;
import com.telecom.portal.model.User;
import com.telecom.portal.repository.PlanRepository;
import com.telecom.portal.repository.RechargeRepository;
import com.telecom.portal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RechargeServiceTest {

    @Mock
    private RechargeRepository rechargeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlanRepository planRepository;

    @InjectMocks
    private RechargeService rechargeService;

    private User testUser;
    private Plan testPlan;
    private Recharge testRecharge;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = User.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .build();

        testPlan = Plan.builder()
                .id(1L)
                .planName("Basic Plan")
                .build();

        testRecharge = Recharge.builder()
                .id(1L)
                .user(testUser)
                .plan(testPlan)
                .rechargeDate(LocalDateTime.now())
                .status("SUCCESS")
                .build();
    }

    @Test
    void testCreateRecharge_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(planRepository.findById(1L)).thenReturn(Optional.of(testPlan));
        when(rechargeRepository.save(any(Recharge.class))).thenAnswer(i -> i.getArgument(0));

        Recharge created = rechargeService.createRecharge(1L, 1L);

        assertThat(created.getUser()).isEqualTo(testUser);
        assertThat(created.getPlan()).isEqualTo(testPlan);
        assertThat(created.getStatus()).isEqualTo("SUCCESS");
        assertThat(created.getRechargeDate()).isNotNull();

        verify(rechargeRepository, times(1)).save(any(Recharge.class));
    }

    @Test
    void testCreateRecharge_UserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> rechargeService.createRecharge(2L, 1L));
        verify(rechargeRepository, never()).save(any());
    }

    @Test
    void testCreateRecharge_PlanNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(planRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> rechargeService.createRecharge(1L, 2L));
        verify(rechargeRepository, never()).save(any());
    }

    @Test
    void testGetRechargesByUser() {
        List<Recharge> recharges = Arrays.asList(testRecharge);
        when(rechargeRepository.findByUserId(1L)).thenReturn(recharges);

        List<Recharge> result = rechargeService.getRechargesByUser(1L);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUser()).isEqualTo(testUser);
    }

    @Test
    void testGetRechargesByPlan() {
        List<Recharge> recharges = Arrays.asList(testRecharge);
        when(rechargeRepository.findByPlanId(1L)).thenReturn(recharges);

        List<Recharge> result = rechargeService.getRechargesByPlan(1L);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPlan()).isEqualTo(testPlan);
    }

    @Test
    void testGetAllRecharges() {
        List<Recharge> recharges = Arrays.asList(testRecharge);
        when(rechargeRepository.findAll()).thenReturn(recharges);

        List<Recharge> result = rechargeService.getAllRecharges();
        assertThat(result).hasSize(1);
    }
}
