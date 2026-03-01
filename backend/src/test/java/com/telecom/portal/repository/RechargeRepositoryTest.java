package com.telecom.portal.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.telecom.portal.model.Recharge;
import com.telecom.portal.model.User;
import com.telecom.portal.model.Plan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class RechargeRepositoryTest {

    @Autowired
    private RechargeRepository rechargeRepository;

    private User testUser;
    private Plan testPlan;

    @BeforeEach
    public void setup() {
        // Create sample user and plan
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstname("John Doe");

        testPlan = new Plan();
        testPlan.setId(1L);
        testPlan.setPlanName("Basic Plan");

        // Save sample recharges
        Recharge recharge1 = new Recharge();
        recharge1.setUser(testUser);
        recharge1.setPlan(testPlan);

        Recharge recharge2 = new Recharge();
        recharge2.setUser(testUser);
        recharge2.setPlan(testPlan);

        rechargeRepository.save(recharge1);
        rechargeRepository.save(recharge2);
    }

    @Test
    public void testFindByUserId() {
        List<Recharge> recharges = rechargeRepository.findByUserId(testUser.getId());
        assertThat(recharges).isNotEmpty();
        assertThat(recharges.size()).isEqualTo(2);
        assertThat(recharges.get(0).getUser().getId()).isEqualTo(testUser.getId());
    }

    @Test
    public void testFindByPlanId() {
        List<Recharge> recharges = rechargeRepository.findByPlanId(testPlan.getId());
        assertThat(recharges).isNotEmpty();
        assertThat(recharges.size()).isEqualTo(2);
        assertThat(recharges.get(0).getPlan().getId()).isEqualTo(testPlan.getId());
    }

    @Test
    public void testFindByUserId_NoResult() {
        List<Recharge> recharges = rechargeRepository.findByUserId(999L);
        assertThat(recharges).isEmpty();
    }

    @Test
    public void testFindByPlanId_NoResult() {
        List<Recharge> recharges = rechargeRepository.findByPlanId(999L);
        assertThat(recharges).isEmpty();
    }
}
