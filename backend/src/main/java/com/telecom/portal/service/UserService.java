
package com.telecom.portal.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.telecom.portal.model.Role;
import com.telecom.portal.model.User;
import com.telecom.portal.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public User registerUser(User user) {
        // hash password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setActivationDate(LocalDate.now());
        return userRepository.save(user);
    }

    public User createAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ADMIN); // force admin role
        user.setActivationDate(LocalDate.now());
        return userRepository.save(user);
    }

    public Optional<User> findByMobileNumber(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber);
    }

    public User validateUser(String mobileNumber, String password) {
        return userRepository.findByMobileNumber(mobileNumber)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(null);
    }

 // ✅ Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Get user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

 // ✅ Update user by ID (without changing role)
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            // Update allowed fields
            existingUser.setFirstname(updatedUser.getFirstname());
            existingUser.setLastname(updatedUser.getLastname());
            existingUser.setCity(updatedUser.getCity());
            existingUser.setMobileNumber(updatedUser.getMobileNumber());

            // Update password only if provided
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            // ⚠️ Do NOT update role, activationDate, or recharges here

            return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

}
