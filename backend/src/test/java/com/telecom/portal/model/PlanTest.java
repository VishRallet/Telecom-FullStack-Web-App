package com.telecom.portal.model;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PlanTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void testGettersAndSetters() {
        Plan plan = new Plan();
        plan.setId(1L);
        plan.setPlanName("Super Saver");
        plan.setPlanType("Prepaid");
        plan.setPrice(299.0);
        plan.setValidity("28 Days");
        plan.setDataLimit("1.5GB/day");
        plan.setDescription("Affordable plan with unlimited calls");

        assertEquals(1L, plan.getId());
        assertEquals("Super Saver", plan.getPlanName());
        assertEquals("Prepaid", plan.getPlanType());
        assertEquals(299.0, plan.getPrice());
        assertEquals("28 Days", plan.getValidity());
        assertEquals("1.5GB/day", plan.getDataLimit());
        assertEquals("Affordable plan with unlimited calls", plan.getDescription());
    }

    @Test
    void testBuilder() {
        Plan plan = Plan.builder()
                .id(2L)
                .planName("Premium Plus")
                .planType("Postpaid")
                .price(999.0)
                .validity("30 Days")
                .dataLimit("2GB/day")
                .description("Premium postpaid plan with OTT benefits")
                .build();

        assertEquals(2L, plan.getId());
        assertEquals("Premium Plus", plan.getPlanName());
        assertEquals("Postpaid", plan.getPlanType());
        assertEquals(999.0, plan.getPrice());
        assertEquals("30 Days", plan.getValidity());
        assertEquals("2GB/day", plan.getDataLimit());
        assertEquals("Premium postpaid plan with OTT benefits", plan.getDescription());
    }

    @Test
    void testValidationFailsWhenFieldsBlank() {
        Plan plan = new Plan();
        plan.setPrice(-100.0); // invalid price

        Set<ConstraintViolation<Plan>> violations = validator.validate(plan);
        assertFalse(violations.isEmpty());

        boolean hasPlanNameViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("planName"));
        boolean hasPriceViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("price"));

        assertTrue(hasPlanNameViolation);
        assertTrue(hasPriceViolation);
    }

    @Test
    void testEqualsAndHashCode() {
        Plan p1 = Plan.builder()
                .id(10L)
                .planName("Basic Pack")
                .price(199.0)
                .build();

        Plan p2 = Plan.builder()
                .id(10L)
                .planName("Basic Pack")
                .price(199.0)
                .build();

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testToStringContainsFields() {
        Plan plan = Plan.builder()
                .id(5L)
                .planName("Family Pack")
                .description("Good for families")
                .build();

        String result = plan.toString();
        assertTrue(result.contains("Family Pack"));
        assertTrue(result.contains("Good for families"));
    }

    @Test
    void testPlanWithRecharges() {
        Plan plan = new Plan();
        plan.setId(3L);
        plan.setPlanName("Unlimited");
        plan.setPlanType("Prepaid");
        plan.setPrice(499.0);
        plan.setValidity("56 Days");
        plan.setDataLimit("2GB/day");
        plan.setDescription("Unlimited plan");

        Recharge recharge = new Recharge();
        recharge.setId(1L);
        recharge.setPlan(plan);

        plan.setRecharges(List.of(recharge));

        assertNotNull(plan.getRecharges());
        assertEquals(1, plan.getRecharges().size());
        assertEquals(plan, plan.getRecharges().get(0).getPlan());
    }
}
