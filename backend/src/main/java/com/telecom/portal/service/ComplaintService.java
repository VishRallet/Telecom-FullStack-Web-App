package com.telecom.portal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.telecom.portal.model.Complaint;
import com.telecom.portal.model.User;
import com.telecom.portal.repository.ComplaintRepository;
import com.telecom.portal.repository.UserRepository;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    // Create complaint
    public Complaint createComplaint(Long userId, Complaint complaint) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        complaint.setUser(user);
        complaint.setStatus("OPEN");
        complaint.setCreatedAt(LocalDateTime.now());
        complaint.setUpdatedAt(LocalDateTime.now());

        return complaintRepository.save(complaint);
    }

    // Get all complaints for a user
    public List<Complaint> getUserComplaints(Long userId) {
        return complaintRepository.findByUserId(userId);
    }

    // Get all complaints (Admin)
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    // Update complaint status
    public Complaint updateStatus(Long complaintId, String status) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        complaint.setStatus(status);
        complaint.setUpdatedAt(LocalDateTime.now());

        return complaintRepository.save(complaint);
    }
}
