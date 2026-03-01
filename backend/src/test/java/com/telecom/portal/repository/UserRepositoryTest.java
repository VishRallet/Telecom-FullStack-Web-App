package com.telecom.portal.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Optional;

import com.telecom.portal.model.Role;
import com.telecom.portal.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user1;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .firstname("John")
                .lastname("Doe")
                .password("password123")
                .role(Role.USER)
                .mobileNumber("9876543210")
                .city("Mumbai")
                .activationDate(LocalDate.now())
                .build();

        userRepository.save(user1);
    }

    @Test
    void testFindByMobileNumber_Existing() {
        Optional<User> found = userRepository.findByMobileNumber("9876543210");
        assertThat(found).isPresent();
        assertThat(found.get().getFirstname()).isEqualTo("John");
    }

    @Test
    void testFindByMobileNumber_NotExisting() {
        Optional<User> found = userRepository.findByMobileNumber("9999999999");
        assertThat(found).isNotPresent();
    }

    @Test
    void testSaveAndFindByMobileNumber() {
        User newUser = User.builder()
                .firstname("Alice")
                .lastname("Smith")
                .password("pass123")
                .role(Role.USER)
                .mobileNumber("9123456789")
                .city("Delhi")
                .activationDate(LocalDate.now())
                .build();

        userRepository.save(newUser);

        Optional<User> found = userRepository.findByMobileNumber("9123456789");
        assertThat(found).isPresent();
        assertThat(found.get().getFirstname()).isEqualTo("Alice");
    }

    @Test
    void testDuplicateMobileNumberConstraint() {
        User duplicateUser = User.builder()
                .firstname("Bob")
                .lastname("Marley")
                .password("pass")
                .role(Role.USER)
                .mobileNumber("9876543210") // duplicate
                .city("Bangalore")
                .activationDate(LocalDate.now())
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(duplicateUser);
        });
    }
}
