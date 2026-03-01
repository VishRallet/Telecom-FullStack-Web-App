package com.telecom.portal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.telecom.portal.model.Role;
import com.telecom.portal.model.User;
import com.telecom.portal.service.UserService;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setFirstname("Alice");
        user.setLastname("Smith");
        user.setMobileNumber("9876543210");
        user.setCity("Delhi");
        user.setActivationDate(LocalDate.now());
        user.setRole(Role.USER);
        user.setPassword("password123");
    }

    // ✅ Register user success
    @Test
    void testRegisterUser_Success() {
        when(userService.registerUser(user)).thenReturn(user);

        ResponseEntity<?> response = userController.register(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof User);
        verify(userService, times(1)).registerUser(user);
    }

    // ✅ Register user failure
    @Test
    void testRegisterUser_Failure() {
        when(userService.registerUser(user)).thenThrow(new RuntimeException("Mobile already exists"));

        ResponseEntity<?> response = userController.register(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Registration failed"));
    }

    // ✅ Login success
    @Test
    void testLogin_Success() {
        when(userService.validateUser("9876543210", "password123")).thenReturn(user);

        ResponseEntity<?> response = userController.login(
                Map.of("mobileNumber", "9876543210", "password", "password123")
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseMap = (Map<?, ?>) response.getBody();
        assertEquals("Alice", responseMap.get("firstname"));
    }

    // ✅ Login failure
    @Test
    void testLogin_Failure() {
        when(userService.validateUser("9876543210", "wrongpass")).thenReturn(null);

        ResponseEntity<?> response = userController.login(
                Map.of("mobileNumber", "9876543210", "password", "wrongpass")
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid mobile number or password", response.getBody());
    }

    // ✅ Get all users
    @Test
    void testGetAllUsers() {
        when(userService.getAllUsers()).thenReturn(List.of(user));

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).getAllUsers();
    }

    // ✅ Get user by ID (found)
    @Test
    void testGetUserById_Found() {
        when(userService.getUserById(1L)).thenReturn(user);

        ResponseEntity<?> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof User);
    }

    // ✅ Get user by ID (not found)
    @Test
    void testGetUserById_NotFound() {
        when(userService.getUserById(2L)).thenReturn(null);

        ResponseEntity<?> response = userController.getUserById(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found with ID: 2", response.getBody());
    }

    // ✅ Update user success
    @Test
    void testUpdateUser_Success() {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setFirstname("Updated");

        when(userService.updateUser(1L, updatedUser)).thenReturn(updatedUser);

        ResponseEntity<?> response = userController.updateUser(1L, updatedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated", ((User) response.getBody()).getFirstname());
    }

    // ✅ Update user failure
    @Test
    void testUpdateUser_Failure() {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setFirstname("Updated");

        when(userService.updateUser(1L, updatedUser)).thenThrow(new RuntimeException("Update failed"));

        ResponseEntity<?> response = userController.updateUser(1L, updatedUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Update failed"));
    }
}
