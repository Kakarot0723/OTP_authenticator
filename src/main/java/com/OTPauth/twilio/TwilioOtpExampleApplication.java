package com.OTPauth.twilio;

import com.OTPauth.twilio.config.TwilioConfig;
import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class TwilioOtpExampleApplication {

	private final TwilioConfig twilioConfig;
	@Autowired
	public TwilioOtpExampleApplication(TwilioConfig twilioConfig) {
		this.twilioConfig = twilioConfig;
		initTwilio(); // Call the init method in the constructor
	}

	private void initTwilio(){
		Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
	}

	public static void main(String[] args) {
		SpringApplication.run(TwilioOtpExampleApplication.class, args);
	}

}
