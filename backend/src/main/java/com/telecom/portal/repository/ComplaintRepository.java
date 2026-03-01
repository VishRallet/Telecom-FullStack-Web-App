package com.telecom.portal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telecom.portal.model.Complaint;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByUserId(Long userId);
}
