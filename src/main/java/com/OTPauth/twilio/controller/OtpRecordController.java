package com.OTPauth.twilio.controller;

import com.OTPauth.twilio.entity.OtpRecord;
import com.OTPauth.twilio.repository.OtpRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/otp-records")
public class OtpRecordController {

    @Autowired
    private OtpRecordRepository otpRecordRepository;

    @GetMapping
    public List<OtpRecord> getAllOtpRecords() {
        return otpRecordRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OtpRecord> getOtpRecordById(@PathVariable Long id) {
        return otpRecordRepository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public OtpRecord createOtpRecord(@RequestBody OtpRecord otpRecord) {
        return otpRecordRepository.save(otpRecord);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OtpRecord> updateOtpRecord(@PathVariable Long id, @RequestBody OtpRecord otpRecord) {
        return otpRecordRepository.findById(id)
                .map(existingRecord -> {
                    existingRecord.setName(otpRecord.getName());
                    existingRecord.setPhoneNumber(otpRecord.getPhoneNumber());
                    existingRecord.setOtp(otpRecord.getOtp());
                    existingRecord.setChannel(otpRecord.getChannel());
                    return ResponseEntity.ok().body(otpRecordRepository.save(existingRecord));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOtpRecord(@PathVariable Long id) {
        return otpRecordRepository.findById(id)
                .map(record -> {
                    otpRecordRepository.delete(record);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
