package com.telecom.portal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.telecom.portal.model.Complaint;
import com.telecom.portal.service.ComplaintService;

@RestController
@RequestMapping("/api/complaints")
//@CrossOrigin(origins = "http://localhost:5173")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    // Raise a complaint
    @PostMapping("/create/{userId}")
    public ResponseEntity<Complaint> createComplaint(
            @PathVariable Long userId,
            @RequestBody Complaint complaint) {
        return ResponseEntity.ok(complaintService.createComplaint(userId, complaint));
    }

    // View complaints of a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Complaint>> getUserComplaints(@PathVariable Long userId) {
        return ResponseEntity.ok(complaintService.getUserComplaints(userId));
    }

    // View all complaints (Admin)
    @GetMapping("/all")
    public ResponseEntity<List<Complaint>> getAllComplaints() {
        return ResponseEntity.ok(complaintService.getAllComplaints());
    }

    // Update complaint status (Admin)
    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<Complaint> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String status = request.get("status");
        return ResponseEntity.ok(complaintService.updateStatus(id, status));
    }
}
