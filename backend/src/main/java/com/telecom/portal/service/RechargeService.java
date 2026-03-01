package com.telecom.portal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.telecom.portal.model.Plan;
import com.telecom.portal.model.Recharge;
import com.telecom.portal.model.User;
import com.telecom.portal.repository.PlanRepository;
import com.telecom.portal.repository.RechargeRepository;
import com.telecom.portal.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RechargeService {

    private final RechargeRepository rechargeRepository;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;

    // ✅ Create a recharge for a user and a plan
    public Recharge createRecharge(Long userId, Long planId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        Recharge recharge = Recharge.builder()
                .user(user)
                .plan(plan)
                .rechargeDate(LocalDateTime.now())
                .status("SUCCESS")
                .build();

        return rechargeRepository.save(recharge);
    }

    // ✅ View recharges by user
    public List<Recharge> getRechargesByUser(Long userId) {
        return rechargeRepository.findByUserId(userId);
    }

    // ✅ View recharges by plan
    public List<Recharge> getRechargesByPlan(Long planId) {
        return rechargeRepository.findByPlanId(planId);
    }

    // ✅ View all recharges
    public List<Recharge> getAllRecharges() {
        return rechargeRepository.findAll();
    }
}
