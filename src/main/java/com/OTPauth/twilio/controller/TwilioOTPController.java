package com.OTPauth.twilio.controller;

import com.OTPauth.twilio.dto.PasswordResetRequestDto;
import com.OTPauth.twilio.dto.PasswordResetResponseDto;
import com.OTPauth.twilio.service.TwilioOTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otp")
public class TwilioOTPController {

    @Autowired
    private TwilioOTPService service;

    @PostMapping("/sendOTP")
    public ResponseEntity<PasswordResetResponseDto> sendOTP(
            @RequestBody PasswordResetRequestDto dto,
            @RequestHeader("x-channel") String channel) {
        PasswordResetResponseDto response = service.sendOTPForPasswordReset(dto, channel);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/validateOTP")
    public ResponseEntity<String> validateOTP(
            @RequestBody PasswordResetRequestDto dto,
            @RequestHeader("x-channel") String channel) {
        try {
            String response = service.validateOTP(dto.getOneTimePassword(), dto.getPhoneNumber(), channel);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
