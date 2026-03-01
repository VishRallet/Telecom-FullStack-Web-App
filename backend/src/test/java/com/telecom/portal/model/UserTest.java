package com.telecom.portal.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .password("password123")
                .role(Role.USER)
                .mobileNumber("9876543210")
                .city("Hyderabad")
                .activationDate(LocalDate.of(2024, 1, 1))
                .build();
    }

    @Test
    void testUserFields() {
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getFirstname()).isEqualTo("John");
        assertThat(user.getLastname()).isEqualTo("Doe");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.getMobileNumber()).isEqualTo("9876543210");
        assertThat(user.getCity()).isEqualTo("Hyderabad");
        assertThat(user.getActivationDate()).isEqualTo(LocalDate.of(2024, 1, 1));
    }

    @Test
    void testUserEquality() {
        User user2 = User.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .password("password123")
                .role(Role.USER)
                .mobileNumber("9876543210")
                .city("Hyderabad")
                .activationDate(LocalDate.of(2024, 1, 1))
                .build();

        assertThat(user).isEqualTo(user2);
        assertThat(user.hashCode()).isEqualTo(user2.hashCode());
    }

    @Test
    void testUserWithRecharges() {
        Recharge recharge1 = Recharge.builder()
                .id(101L)
                .status("SUCCESS")
                .user(user)
                .build();

        Recharge recharge2 = Recharge.builder()
                .id(102L)
                .status("PENDING")
                .user(user)
                .build();

        user.setRecharges(Arrays.asList(recharge1, recharge2));

        assertThat(user.getRecharges()).hasSize(2);
        assertThat(user.getRecharges().get(0).getStatus()).isEqualTo("SUCCESS");
        assertThat(user.getRecharges().get(1).getStatus()).isEqualTo("PENDING");
    }

    @Test
    void testBuilderPattern() {
        User builtUser = User.builder()
                .firstname("Alice")
                .lastname("Smith")
                .password("secret")
                .role(Role.ADMIN)
                .mobileNumber("1234567890")
                .city("Delhi")
                .activationDate(LocalDate.now())
                .build();

        assertThat(builtUser.getFirstname()).isEqualTo("Alice");
        assertThat(builtUser.getRole()).isEqualTo(Role.ADMIN);
    }
}
