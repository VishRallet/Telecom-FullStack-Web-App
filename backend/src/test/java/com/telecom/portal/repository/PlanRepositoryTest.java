package com.telecom.portal.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import com.telecom.portal.model.Plan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
public class PlanRepositoryTest {

    @Autowired
    private PlanRepository planRepository;

    private Plan plan1;
    private Plan plan2;

    @BeforeEach
    void setUp() {
        plan1 = Plan.builder()
                .planName("Basic Plan")
                .planType("Prepaid")
                .price(199.0)
                .validity("30 Days")
                .dataLimit("10GB")
                .description("Basic internet plan")
                .build();

        plan2 = Plan.builder()
                .planName("Premium Plan")
                .planType("Postpaid")
                .price(499.0)
                .validity("90 Days")
                .dataLimit("50GB")
                .description("Premium internet plan")
                .build();

        planRepository.save(plan1);
        planRepository.save(plan2);
    }

    @Test
    void testFindAll() {
        List<Plan> plans = planRepository.findAll();
        assertThat(plans).hasSize(2);
    }

    @Test
    void testFindById() {
        Optional<Plan> found = planRepository.findById(plan1.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getPlanName()).isEqualTo("Basic Plan");
    }

    @Test
    void testSavePlan() {
        Plan newPlan = Plan.builder()
                .planName("Ultra Plan")
                .planType("Prepaid")
                .price(799.0)
                .validity("180 Days")
                .dataLimit("100GB")
                .description("Ultra speed plan")
                .build();

        Plan saved = planRepository.save(newPlan);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getPlanName()).isEqualTo("Ultra Plan");
    }

    @Test
    void testUpdatePlan() {
        plan1.setPrice(249.0);
        Plan updated = planRepository.save(plan1);
        assertThat(updated.getPrice()).isEqualTo(249.0);
    }

    @Test
    void testDeletePlan() {
        planRepository.delete(plan2);
        List<Plan> plans = planRepository.findAll();
        assertThat(plans).hasSize(1);
        assertThat(plans.get(0).getPlanName()).isEqualTo("Basic Plan");
    }

    // --------- Validation / Constraint Tests ---------

    @Test
    void testUniquePlanNameConstraint() {
        Plan duplicateNamePlan = Plan.builder()
                .planName("Basic Plan") // duplicate
                .planType("Prepaid")
                .price(299.0)
                .validity("30 Days")
                .dataLimit("15GB")
                .description("Another plan with same name")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            planRepository.saveAndFlush(duplicateNamePlan);
        });
    }

    @Test
    void testUniquePriceConstraint() {
        Plan duplicatePricePlan = Plan.builder()
                .planName("Special Plan")
                .planType("Prepaid")
                .price(199.0) // duplicate price
                .validity("30 Days")
                .dataLimit("15GB")
                .description("Plan with duplicate price")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            planRepository.saveAndFlush(duplicatePricePlan);
        });
    }

    @Test
    void testNotNullValidation() {
        Plan invalidPlan = Plan.builder()
                .planName(null) // null name
                .planType("Prepaid")
                .price(299.0)
                .validity("30 Days")
                .dataLimit("15GB")
                .description("Invalid plan")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            planRepository.saveAndFlush(invalidPlan);
        });
    }
}
