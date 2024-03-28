package com.talentstream.controller;

import java.util.HashMap;
import java.util.Map;
import com.talentstream.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.talentstream.entity.JobRecruiter;
import com.talentstream.entity.NewPasswordRequest;
import com.talentstream.entity.OtpVerificationRequest;
import com.talentstream.entity.ResetPasswordRequest;
import com.talentstream.service.EmailService;
import com.talentstream.service.JobRecruiterService;
import com.talentstream.service.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@CrossOrigin("*")
@RequestMapping("/forgotpassword")
public class ForgetPasswordController {
	@Autowired
    private OtpService otpService;

    @Autowired
    private JobRecruiterService jobRecruiterService; 

    @Autowired
    private EmailService emailService;
    
        @Autowired
    private PasswordEncoder passwordEncoder;
        
        private Map<String, Boolean> otpVerificationMap = new HashMap<>();
        private static final Logger logger = LoggerFactory.getLogger(ApplicantProfileController.class);
    @PostMapping("/recuritersend-otp")
    public ResponseEntity<String> sendOtp(@RequestBody ResetPasswordRequest request) {
    	 String userEmail = request.getEmail();
         JobRecruiter jobRecruiter = jobRecruiterService.findByEmail(userEmail);
         if (jobRecruiter == null) {
             throw new CustomException("Email is not registered or not found.", HttpStatus.BAD_REQUEST);
         }
         String otp = otpService.generateOtp(userEmail);
         emailService.sendOtpEmail(userEmail, otp);
         otpVerificationMap.put(userEmail, true);
         return ResponseEntity.ok("OTP sent successfully");
    }
    
    
    @PostMapping("/recuriterverify-otp")
    public ResponseEntity<String> verifyOtp(
       @RequestBody  OtpVerificationRequest verificationRequest
    ) {
    	 String otp = verificationRequest.getOtp();
         String email = verificationRequest.getEmail();

         try {
             if (otpService.validateOtp(email, otp)) {
                 return ResponseEntity.ok("OTP verified successfully");
             } else {
                 throw new CustomException("Incorrect OTP or Time Out", HttpStatus.BAD_REQUEST);
             }
         } catch (CustomException ce) {
             return ResponseEntity.status(ce.getStatus()).body(ce.getMessage());
         } catch (Exception e) {
             throw new CustomException("Error verifying OTP", HttpStatus.INTERNAL_SERVER_ERROR);
         }
    }


    @PostMapping("/recuriterreset-password/set-new-password/{email}")
    public ResponseEntity<String> setNewPassword(@RequestBody NewPasswordRequest request,  @PathVariable String email) {
    	 try {
             String newPassword = request.getPassword();
             String confirmedPassword = request.getConfirmedPassword();
                        if (email == null) {
                 throw new CustomException("Email not found.", HttpStatus.BAD_REQUEST);
             }
             JobRecruiter jobRecruiter = jobRecruiterService.findByEmail(email);
             System.out.println(jobRecruiter.getEmail());
             if (jobRecruiter == null) {
                 throw new CustomException("User not found.", HttpStatus.BAD_REQUEST);
             }
             else
             {
               if (!newPassword.equals(confirmedPassword)) {
                 throw new CustomException("Passwords do not match.", HttpStatus.BAD_REQUEST);
             }
                       jobRecruiter.setPassword(passwordEncoder.encode(newPassword));
                        jobRecruiterService.addRecruiter(jobRecruiter);
             return ResponseEntity.ok("Password reset was done successfully");
         }
    	 }catch (CustomException ce) {
             return ResponseEntity.status(ce.getStatus()).body(ce.getMessage());
         } catch (Exception e) {
             throw new CustomException("Error resetting password", HttpStatus.INTERNAL_SERVER_ERROR);
         }
        
     }
}
