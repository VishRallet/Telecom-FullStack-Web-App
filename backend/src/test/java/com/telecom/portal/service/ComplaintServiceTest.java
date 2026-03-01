package com.telecom.portal.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.telecom.portal.model.Complaint;
import com.telecom.portal.model.User;
import com.telecom.portal.repository.ComplaintRepository;
import com.telecom.portal.repository.UserRepository;

class ComplaintServiceTest {

    @Mock
    private ComplaintRepository complaintRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ComplaintService complaintService;

    private User testUser;
    private Complaint testComplaint;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = User.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .build();

        testComplaint = Complaint.builder()
                .id(1L)
                .subject("Internet Issue")
                .description("No internet since morning")
                .build();
    }

    @Test
    void testCreateComplaint_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(complaintRepository.save(any(Complaint.class))).thenAnswer(i -> i.getArgument(0));

        Complaint created = complaintService.createComplaint(1L, testComplaint);

        assertThat(created.getUser()).isEqualTo(testUser);
        assertThat(created.getStatus()).isEqualTo("OPEN");
        assertThat(created.getCreatedAt()).isNotNull();
        assertThat(created.getUpdatedAt()).isNotNull();

        verify(complaintRepository, times(1)).save(testComplaint);
    }

    @Test
    void testCreateComplaint_UserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> complaintService.createComplaint(2L, testComplaint));
        verify(complaintRepository, never()).save(any());
    }

    @Test
    void testGetUserComplaints() {
        List<Complaint> complaints = Arrays.asList(testComplaint);
        when(complaintRepository.findByUserId(1L)).thenReturn(complaints);

        List<Complaint> result = complaintService.getUserComplaints(1L);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSubject()).isEqualTo("Internet Issue");
    }

    @Test
    void testGetAllComplaints() {
        List<Complaint> complaints = Arrays.asList(testComplaint);
        when(complaintRepository.findAll()).thenReturn(complaints);

        List<Complaint> result = complaintService.getAllComplaints();
        assertThat(result).hasSize(1);
    }

    @Test
    void testUpdateStatus_Success() {
        when(complaintRepository.findById(1L)).thenReturn(Optional.of(testComplaint));
        when(complaintRepository.save(any(Complaint.class))).thenAnswer(i -> i.getArgument(0));

        Complaint updated = complaintService.updateStatus(1L, "RESOLVED");

        assertThat(updated.getStatus()).isEqualTo("RESOLVED");
        assertThat(updated.getUpdatedAt()).isNotNull();
        verify(complaintRepository, times(1)).save(testComplaint);
    }

    @Test
    void testUpdateStatus_ComplaintNotFound() {
        when(complaintRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> complaintService.updateStatus(2L, "RESOLVED"));
        verify(complaintRepository, never()).save(any());
    }
}
