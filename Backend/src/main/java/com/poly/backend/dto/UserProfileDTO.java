package com.poly.backend.dto;

import lombok.Data;

@Data
public class UserProfileDTO {
    private String fullName;
    private String phoneNumber;
    private String email;
    private String address;
}