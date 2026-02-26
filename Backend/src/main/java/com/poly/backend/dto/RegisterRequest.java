package com.poly.backend.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phoneNumber;
    private Boolean gender;
    private String address;
}