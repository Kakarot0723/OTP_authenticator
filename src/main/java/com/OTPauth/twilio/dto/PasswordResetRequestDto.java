package com.OTPauth.twilio.dto;

import lombok.Data;

@Data
public class PasswordResetRequestDto {

    private String phoneNumber;//destination
    private String userName;
    private String oneTimePassword;
    private String config;
}
