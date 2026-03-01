package com.telecom.portal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telecom.portal.model.Recharge;

public interface RechargeRepository extends JpaRepository<Recharge, Long> {
    List<Recharge> findByUserId(Long userId);
    List<Recharge> findByPlanId(Long planId);
}
