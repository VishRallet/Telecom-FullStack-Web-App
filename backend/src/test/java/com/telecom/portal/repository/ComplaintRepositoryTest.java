package com.telecom.portal.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.telecom.portal.model.Complaint;
import com.telecom.portal.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ComplaintRepositoryTest {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository; // assuming you have a UserRepository

    private User testUser;

    @BeforeEach
    void setUp() {
        // Create and save a test user
        testUser = User.builder()
                .firstname("John Doe")
                .lastname("G")
                .password("password")
                .build();
        userRepository.save(testUser);

        // Create some complaints
        Complaint c1 = Complaint.builder()
                .subject("Internet Issue")
                .description("No internet since morning")
                .status("OPEN")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(testUser)
                .build();

        Complaint c2 = Complaint.builder()
                .subject("Billing Issue")
                .description("Incorrect bill")
                .status("OPEN")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(testUser)
                .build();

        complaintRepository.save(c1);
        complaintRepository.save(c2);
    }

    @Test
    void testFindById() {
        List<Complaint> complaints = complaintRepository.findAll();
        Complaint firstComplaint = complaints.get(0);

        Optional<Complaint> found = complaintRepository.findById(firstComplaint.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getSubject()).isEqualTo(firstComplaint.getSubject());
    }

    @Test
    void testFindAll() {
        List<Complaint> complaints = complaintRepository.findAll();
        assertThat(complaints).hasSize(2);
    }

    @Test
    void testFindByUserId() {
        List<Complaint> complaints = complaintRepository.findByUserId(testUser.getId());
        assertThat(complaints).hasSize(2);
        assertThat(complaints.get(0).getUser().getId()).isEqualTo(testUser.getId());
    }

    @Test
    void testSaveComplaint() {
        Complaint newComplaint = Complaint.builder()
                .subject("TV not working")
                .description("Cannot turn on TV")
                .status("OPEN")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(testUser)
                .build();

        Complaint saved = complaintRepository.save(newComplaint);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getSubject()).isEqualTo("TV not working");
    }

    @Test
    void testDeleteComplaint() {
        List<Complaint> complaints = complaintRepository.findAll();
        Complaint c = complaints.get(0);

        complaintRepository.delete(c);

        Optional<Complaint> deleted = complaintRepository.findById(c.getId());
        assertThat(deleted).isEmpty();
    }
}
