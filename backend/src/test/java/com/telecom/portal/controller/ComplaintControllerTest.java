package com.telecom.portal.controller;

import com.telecom.portal.model.Complaint;
import com.telecom.portal.service.ComplaintService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ComplaintControllerTest {

    @Mock
    private ComplaintService complaintService;

    @InjectMocks
    private ComplaintController complaintController;

    private Complaint complaint;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        complaint = new Complaint();
        complaint.setId(1L);
        complaint.setDescription("Network issue");
        complaint.setStatus("Pending");
    }

    @Test
    void testCreateComplaint() {
        when(complaintService.createComplaint(eq(1L), any(Complaint.class)))
                .thenReturn(complaint);

        ResponseEntity<Complaint> response =
                complaintController.createComplaint(1L, complaint);

        assertNotNull(response.getBody());
        assertEquals("Network issue", response.getBody().getDescription());
        verify(complaintService, times(1)).createComplaint(1L, complaint);
    }

    @Test
    void testGetUserComplaints() {
        List<Complaint> complaints = List.of(complaint);
        when(complaintService.getUserComplaints(1L)).thenReturn(complaints);

        ResponseEntity<List<Complaint>> response =
                complaintController.getUserComplaints(1L);

        assertEquals(1, response.getBody().size());
        assertEquals("Network issue", response.getBody().get(0).getDescription());
        verify(complaintService, times(1)).getUserComplaints(1L);
    }

    @Test
    void testGetAllComplaints() {
        List<Complaint> complaints = Arrays.asList(complaint);
        when(complaintService.getAllComplaints()).thenReturn(complaints);

        ResponseEntity<List<Complaint>> response =
                complaintController.getAllComplaints();

        assertEquals(1, response.getBody().size());
        verify(complaintService, times(1)).getAllComplaints();
    }

    @Test
    void testUpdateStatus() {
        Complaint updatedComplaint = new Complaint();
        updatedComplaint.setId(1L);
        updatedComplaint.setStatus("Resolved");

        when(complaintService.updateStatus(1L, "Resolved"))
                .thenReturn(updatedComplaint);

        Map<String, String> request = new HashMap<>();
        request.put("status", "Resolved");

        ResponseEntity<Complaint> response =
                complaintController.updateStatus(1L, request);

        assertEquals("Resolved", response.getBody().getStatus());
        verify(complaintService, times(1)).updateStatus(1L, "Resolved");
    }
}
