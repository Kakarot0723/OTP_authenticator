package com.OTPauth.twilio.service;

import com.OTPauth.twilio.config.TwilioConfig;
import com.OTPauth.twilio.dto.OtpStatus;
import com.OTPauth.twilio.dto.PasswordResetRequestDto;
import com.OTPauth.twilio.dto.PasswordResetResponseDto;
import com.OTPauth.twilio.entity.OtpRecord;
import com.OTPauth.twilio.repository.OtpRecordRepository;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

@Service
public class TwilioOTPService {

    @Autowired
    private TwilioConfig twilioConfig;

    @Autowired
    private OtpRecordRepository otpRecordRepository;

    public PasswordResetResponseDto sendOTPForPasswordReset(PasswordResetRequestDto passwordResetRequestDto, String channel) {
        PasswordResetResponseDto passwordResetResponseDto = null;
        try {
            PhoneNumber to = new PhoneNumber(passwordResetRequestDto.getPhoneNumber());
            PhoneNumber from = new PhoneNumber(twilioConfig.getTrialNumber());
            String otp;
            if(Objects.equals(channel, "Login")){
                otp = generateOTP(4);
            }
            else {
                otp = generateOTP(6);
            }
            String otpMessage = "Dear Customer, Your OTP is ##" + otp + "##. Use this Passcode to complete your transaction. Thank you.";
            //Message message = Message.creator(to, from, otpMessage).create();

            // Save OTP to database
            OtpRecord otpRecord = new OtpRecord();
            otpRecord.setName(passwordResetRequestDto.getUserName());
            otpRecord.setPhoneNumber(passwordResetRequestDto.getPhoneNumber());
            otpRecord.setOtp(otp);
            otpRecord.setChannel(channel);
            otpRecord.setCreatedAt(LocalDateTime.now());
            otpRecordRepository.save(otpRecord);

            passwordResetResponseDto = new PasswordResetResponseDto(OtpStatus.DELIVERED, otpMessage);
        } catch (Exception ex) {
            passwordResetResponseDto = new PasswordResetResponseDto(OtpStatus.FAILED, ex.getMessage());
        }
        return passwordResetResponseDto;
    }

    public String validateOTP(String userInputOtp, String phoneNumber, String channel) {
        OtpRecord otpRecord = otpRecordRepository.findByPhoneNumberAndChannel(phoneNumber, channel);

        if (otpRecord == null) {
            throw new IllegalArgumentException("No OTP request found for this phone number and channel");
        }

        if (userInputOtp.equals(otpRecord.getOtp())) {
            otpRecordRepository.delete(otpRecord);
            return "Valid OTP, please proceed with your transaction!";
        } else {
            throw new IllegalArgumentException("Invalid OTP, please retry!");
        }
    }

    private String generateOTP(int length) {
        StringBuilder otp = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}



