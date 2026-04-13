package com.example.guangzhouorder.dto;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private String otpCode;
    private String newPassword;
    private String confirmPassword;
}
