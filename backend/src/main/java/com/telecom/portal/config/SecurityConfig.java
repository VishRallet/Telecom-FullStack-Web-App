package com.telecom.portal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 🔑 Disable CSRF so POST/PUT works in Postman without tokens
            .csrf(csrf -> csrf.disable())

            // 🔑 Authorization rules
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/user/**").permitAll()        // register, login
                .requestMatchers("/api/admin/**").permitAll()       // admin setup
                .requestMatchers("/api/plans/**").permitAll()       // plans public
                .requestMatchers("/api/recharges/**").permitAll()   // recharge
                .requestMatchers("/api/complaints/**").permitAll() // complaints public
                .anyRequest().authenticated()                       // everything else secured
            );

        return http.build();
    }

    // ✅ Password encoder bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
