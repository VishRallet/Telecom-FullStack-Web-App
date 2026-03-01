package com.telecom.portal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "plans",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"planName"}),
        @UniqueConstraint(columnNames = {"price"})
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Plan name cannot be blank")
    @Column(nullable = false, unique = true)
    private String planName;

    @NotBlank(message = "Plan type cannot be blank")
    @Column(nullable = false)
    private String planType; // Prepaid or Postpaid

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    @Column(nullable = false, unique = true)
    private Double price;

    @NotBlank(message = "Validity cannot be blank")
    @Column(nullable = false)
    private String validity;

    @NotBlank(message = "Data limit cannot be blank")
    @Column(nullable = false)
    private String dataLimit;

    @NotBlank(message = "Description cannot be blank")
    @Column(nullable = false)
    private String description;

    // ✅ One plan can have many recharges
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Recharge> recharges;
}
