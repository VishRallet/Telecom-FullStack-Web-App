package com.telecom.portal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.telecom.portal.model.Plan;
import com.telecom.portal.repository.PlanRepository;

@Service
public class PlanService {
    private final PlanRepository repo;

    public PlanService(PlanRepository repo) {
        this.repo = repo;
    }

    public List<Plan> getAllPlans() {
        return repo.findAll();
    }

    public Plan getPlanById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Plan savePlan(Plan plan) {
        return repo.save(plan);
    }

    public void deletePlan(Long id) {
        repo.deleteById(id);
    }
}
