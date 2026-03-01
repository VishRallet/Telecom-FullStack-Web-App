package com.telecom.portal.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import com.telecom.portal.model.Role;
import com.telecom.portal.model.User;
import com.telecom.portal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = User.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .password("password123")
                .role(Role.USER)
                .mobileNumber("9876543210")
                .city("Mumbai")
                .activationDate(LocalDate.now())
                .build();
    }

    @Test
    void testRegisterUser() {
        when(passwordEncoder.encode("password123")).thenReturn("hashedPass");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User registered = userService.registerUser(testUser);

        assertThat(registered.getPassword()).isEqualTo("hashedPass");
        assertThat(registered.getRole()).isEqualTo(Role.USER);
        assertThat(registered.getActivationDate()).isNotNull();
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testCreateAdmin() {
        when(passwordEncoder.encode("password123")).thenReturn("hashedPass");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User admin = userService.createAdmin(testUser);

        assertThat(admin.getRole()).isEqualTo(Role.ADMIN);
        assertThat(admin.getPassword()).isEqualTo("hashedPass");
        assertThat(admin.getActivationDate()).isNotNull();
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testFindByMobileNumber_Found() {
        when(userRepository.findByMobileNumber("9876543210")).thenReturn(Optional.of(testUser));

        Optional<User> found = userService.findByMobileNumber("9876543210");
        assertThat(found).isPresent();
        assertThat(found.get().getFirstname()).isEqualTo("John");
    }

    @Test
    void testValidateUser_Success() {
        when(userRepository.findByMobileNumber("9876543210")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", testUser.getPassword())).thenReturn(true);

        User validated = userService.validateUser("9876543210", "password123");
        assertThat(validated).isEqualTo(testUser);
    }

    @Test
    void testValidateUser_Failure() {
        when(userRepository.findByMobileNumber("9876543210")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongpass", testUser.getPassword())).thenReturn(false);

        User validated = userService.validateUser("9876543210", "wrongpass");
        assertNull(validated);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser));

        assertThat(userService.getAllUsers()).hasSize(1);
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User user = userService.getUserById(1L);
        assertThat(user).isEqualTo(testUser);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        User user = userService.getUserById(2L);
        assertNull(user);
    }

    @Test
    void testUpdateUser_WithPassword() {
        User updatedUser = User.builder()
                .firstname("Jane")
                .lastname("Smith")
                .city("Pune")
                .mobileNumber("9876543211")
                .password("newpass")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newpass")).thenReturn("hashedNewPass");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = userService.updateUser(1L, updatedUser);

        assertThat(result.getFirstname()).isEqualTo("Jane");
        assertThat(result.getPassword()).isEqualTo("hashedNewPass");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_WithoutPassword() {
        User updatedUser = User.builder()
                .firstname("Jane")
                .lastname("Smith")
                .city("Pune")
                .mobileNumber("9876543211")
                .password(null)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = userService.updateUser(1L, updatedUser);

        assertThat(result.getFirstname()).isEqualTo("Jane");
        assertThat(result.getPassword()).isEqualTo("password123"); // unchanged
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUser(2L, testUser));
    }
}
