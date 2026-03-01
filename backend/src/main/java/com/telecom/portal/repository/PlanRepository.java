package com.telecom.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telecom.portal.model.Plan;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}
