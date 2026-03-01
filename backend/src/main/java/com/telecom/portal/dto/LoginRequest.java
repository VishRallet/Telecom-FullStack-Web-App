package com.telecom.portal.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String mobileNumber;
    private String password;
}
