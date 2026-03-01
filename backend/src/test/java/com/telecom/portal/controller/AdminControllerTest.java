package com.telecom.portal.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telecom.portal.model.Role;
import com.telecom.portal.model.User;
import com.telecom.portal.service.UserService;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void createAdmin_withValidKey_shouldReturnAdmin() throws Exception {
        User mockAdmin = new User();
        mockAdmin.setId(1L);
        mockAdmin.setFirstname("John");
        mockAdmin.setLastname("Doe");
        mockAdmin.setMobileNumber("9999999999");
        mockAdmin.setCity("Delhi");
        mockAdmin.setActivationDate(LocalDate.now());
        mockAdmin.setRole(Role.ADMIN);

        Mockito.when(userService.createAdmin(Mockito.any(User.class)))
                .thenReturn(mockAdmin);

        mockMvc.perform(post("/api/admin/create")
                        .header("X-ADMIN-KEY", "supersecret")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockAdmin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void createAdmin_withInvalidKey_shouldReturnUnauthorized() throws Exception {
        User mockAdmin = new User();
        mockAdmin.setFirstname("John");

        mockMvc.perform(post("/api/admin/create")
                        .header("X-ADMIN-KEY", "wrongkey")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockAdmin)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid admin key"));
    }

    @Test
    void login_withValidCredentials_shouldReturnAdminDetails() throws Exception {
        User mockAdmin = new User();
        mockAdmin.setId(1L);
        mockAdmin.setFirstname("Alice");
        mockAdmin.setLastname("Smith");
        mockAdmin.setMobileNumber("8888888888");
        mockAdmin.setCity("Mumbai");
        mockAdmin.setActivationDate(LocalDate.now());
        mockAdmin.setRole(Role.ADMIN);

        Mockito.when(userService.validateUser("8888888888", "password123"))
                .thenReturn(mockAdmin);

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("mobileNumber", "8888888888");
        loginRequest.put("password", "password123");

        mockMvc.perform(post("/api/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("Alice"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void login_withInvalidCredentials_shouldReturnUnauthorized() throws Exception {
        Mockito.when(userService.validateUser("1234567890", "wrongpass"))
                .thenReturn(null);

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("mobileNumber", "1234567890");
        loginRequest.put("password", "wrongpass");

        mockMvc.perform(post("/api/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid admin credentials"));
    }
}
