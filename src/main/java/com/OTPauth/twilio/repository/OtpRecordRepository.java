package com.OTPauth.twilio.repository;

import com.OTPauth.twilio.entity.OtpRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRecordRepository extends JpaRepository<OtpRecord, Long> {
    OtpRecord findByPhoneNumber(String phoneNumber);

    OtpRecord findByPhoneNumberAndChannel(String phoneNumber, String channel);
}