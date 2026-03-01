package com.telecom.portal.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "complaints")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;      // Short title
    private String description;  // Full details
    private String status;       // OPEN, IN_PROGRESS, RESOLVED, CLOSED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ✅ Many-to-one relation: Many complaints belong to one user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
