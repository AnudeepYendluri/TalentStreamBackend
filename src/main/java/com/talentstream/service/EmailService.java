package com.talentstream.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
 
@Service
public class EmailService {
 
    @Autowired
    private JavaMailSender javaMailSender;
 
    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("OTP for TalentStreamApplication Registration");
        message.setTo(to);
        message.setText("Your OTP is: " + otp+ "\n and this otp will be valid for 1 min");
        javaMailSender.send(message);
    }
}