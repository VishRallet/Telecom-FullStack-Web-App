package com.telecom.portal.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.telecom.portal.model.Recharge;
import com.telecom.portal.service.RechargeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/recharges")
//@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class RechargeController {

    private final RechargeService rechargeService;

    // ✅ Create recharge for a user with a plan
    @PostMapping("/create/{userId}/{planId}")
    public Recharge createRecharge(@PathVariable Long userId, @PathVariable Long planId) {
        return rechargeService.createRecharge(userId, planId);
    }

    // ✅ Get recharges by user
    @GetMapping("/user/{userId}")
    public List<Recharge> getRechargesByUser(@PathVariable Long userId) {
        return rechargeService.getRechargesByUser(userId);
    }

    // ✅ Get recharges by plan
    @GetMapping("/plan/{planId}")
    public List<Recharge> getRechargesByPlan(@PathVariable Long planId) {
        return rechargeService.getRechargesByPlan(planId);
    }

    // ✅ Get all recharges
    @GetMapping("/all")
    public List<Recharge> getAllRecharges() {
        return rechargeService.getAllRecharges();
    }
}
