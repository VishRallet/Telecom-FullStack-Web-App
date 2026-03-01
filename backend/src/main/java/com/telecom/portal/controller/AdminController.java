package com.telecom.portal.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.telecom.portal.model.Role;
import com.telecom.portal.model.User;
import com.telecom.portal.service.UserService;

@RestController
@RequestMapping("/api/admin")
//@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createAdmin(
            @RequestBody User user,
            @RequestHeader("X-ADMIN-KEY") String adminKey) {

        if (!"supersecret".equals(adminKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid admin key");
        }

        User savedAdmin = userService.createAdmin(user);
        return ResponseEntity.ok(savedAdmin);
    }
        @PostMapping("/login")
        public ResponseEntity<?> login1(@RequestBody Map<String, String> request) {
            String mobileNumber = request.get("mobileNumber");
            String password = request.get("password");

            User admin = userService.validateUser(mobileNumber, password);

            if (admin != null && admin.getRole() == Role.ADMIN) {
                Map<String, Object> response = new HashMap<>();
                response.put("id", admin.getId());
                response.put("firstname", admin.getFirstname());
                response.put("lastname", admin.getLastname());
                response.put("mobileNumber", admin.getMobileNumber());
                response.put("city", admin.getCity());
                response.put("activationDate", admin.getActivationDate());
                response.put("role", admin.getRole());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid admin credentials");
            }
        }
}
