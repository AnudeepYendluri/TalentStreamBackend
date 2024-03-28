package com.talentstream.controller;
import java.util.HashMap;
 
import java.util.*;
import com.talentstream.dto.LoginDTO;
import com.talentstream.dto.LoginDTO1;
 
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
 
import com.talentstream.dto.RegistrationDTO;
import com.talentstream.entity.Applicant;
import com.talentstream.entity.AuthenticationResponse;
import com.talentstream.entity.JobRecruiter;
import com.talentstream.entity.Login;
import com.talentstream.entity.NewPasswordRequest;
import com.talentstream.entity.OtpVerificationRequest;
import com.talentstream.entity.PasswordRequest;
import com.talentstream.exception.CustomException;
import com.talentstream.repository.JobRecruiterRepository;
import com.talentstream.repository.RegisterRepository;
import com.talentstream.response.ResponseHandler;
import com.talentstream.service.EmailService;
import com.talentstream.service.JwtUtil;
import com.talentstream.service.MyUserDetailsService;
import com.talentstream.service.OtpService;
import com.talentstream.service.RegisterService;
import com.talentstream.service.JobRecruiterService;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
@CrossOrigin("*")
@RestController
@RequestMapping("/applicant")
public class RegisterController {
	@Autowired
    MyUserDetailsService myUserDetailsService;
	
	 @Autowired
	    private OtpService otpService;
	 @Autowired
	 private RegisterRepository registerrepo;

	@Autowired
	 private JobRecruiterRepository recruiterRepository;

	@Autowired
	 private RegisterService registerService;
	
	
		 private Map<String, Boolean> otpVerificationMap = new HashMap<>();
		 private static final Logger logger = LoggerFactory.getLogger(ApplicantProfileController.class);
		 @Autowired
			private AuthenticationManager authenticationManager;
		     @Autowired
			private JwtUtil jwtTokenUtil;
		    
	    @Autowired
	    private EmailService emailService;
	    
		@Autowired
	     RegisterService regsiterService;
		@Autowired
		JobRecruiterService recruiterService;	
		@Autowired
		private PasswordEncoder passwordEncoder;
 
	    @Autowired
	    public RegisterController(RegisterService regsiterService)
	    {
	        this.regsiterService = regsiterService;	     
 
	    }
            @PutMapping("/editApplicant/{applicantId}")
	    public ResponseEntity<String> editApplicant(@PathVariable Long applicantId, @RequestBody RegistrationDTO updatedRegistrationDTO) {
	        try {
	            ResponseEntity<String> response = registerService.editApplicant(applicantId, updatedRegistrationDTO);
	            return response;
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating applicant");
	        }
	    }
            @PostMapping("/saveApplicant")
    	    public ResponseEntity<String> register(@Valid @RequestBody RegistrationDTO registrationDTO, BindingResult bindingResult) {
    	        if (bindingResult.hasErrors()) {
    	            // Handle validation errors
    	            Map<String, String> fieldErrors = new LinkedHashMap<>();
     
    	            bindingResult.getFieldErrors().forEach(fieldError -> {
    	                String fieldName = fieldError.getField();
    	                String errorMessage = fieldError.getDefaultMessage();
     
    	                // Append each field and its error message on a new line
    	                fieldErrors.merge(fieldName, errorMessage, (existingMessage, newMessage) -> existingMessage + "\n" + newMessage);
    	            });
     
    	            // Construct the response body with each field and its error message on separate lines
    	            StringBuilder responseBody = new StringBuilder();
    	            fieldErrors.forEach((fieldName, errorMessage) -> responseBody.append(fieldName).append(": ").append(errorMessage).append("\n"));
     
    	            return ResponseEntity.badRequest().body(responseBody.toString());
    	        }
     
    	        try {
    	            return regsiterService.saveapplicant(registrationDTO);
    	        } catch (CustomException e) {
    	            return ResponseEntity.badRequest().body(e.getMessage());
    	        } catch (Exception e) {
    	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering applicant");
    	        }
    	    }
 
//     	    @PostMapping("/applicantLogin")
//	    public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO) throws Exception {
//     	    	try {
//     	            Applicant applicant =regsiterService.login(loginDTO.getEmail(), loginDTO.getPassword());
//     	            if (applicant != null) {
//     	                return createAuthenticationToken(loginDTO, applicant);
//     	            } else {
//     	                return ResponseEntity.badRequest().body("Login failed");
//     	            }
//     	        } catch (BadCredentialsException e) {
//     	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
//     	        } catch (Exception e) {
//     	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during login");
//     	        }
//	    }
	    
	  @PostMapping("/applicantLogin")
	    public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO) throws Exception {
	        try {
	            Applicant applicant = null;
 
	            if (regsiterService.isGoogleSignIn(loginDTO)) {
	                // Handle Google Sign-In
	                System.out.println("Before " + loginDTO.getEmail());
	                applicant = regsiterService.googleSignIn(loginDTO.getEmail());
	                System.out.println(applicant.getEmail());
	                System.out.println("could return obj successfully");
	            } else {
	                // Handle regular login
	                applicant = regsiterService.login(loginDTO.getEmail(), loginDTO.getPassword());
 
	                if (applicant == null) {
	                    // Check if the email exists in the database
	                    boolean emailExists = regsiterService.emailExists(loginDTO.getEmail());
	                    if (emailExists) {
	                        // Incorrect password
	                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect password");
	                    } else {
	                        // No account found with this email address
	                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No account found with this email address");
	                    }
	                }
	            }
 
	            if (applicant != null) {
	                return createAuthenticationToken(loginDTO, applicant);
	            } else {
	                return ResponseEntity.badRequest().body("Login failed");
	            }
	        } catch (BadCredentialsException e) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
	        } catch (Exception e) {
	        	 e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during login");
	        }
	    }
     	
//     	   @PostMapping("/applicantLogin1")
//   	    public ResponseEntity<Object> login1(@RequestBody LoginDTO1 loginDTO1) throws Exception {
//        	    	try {
//        	            Applicant applicant =regsiterService.login1(loginDTO1.getEmail());
//        	            if (applicant != null) {
//        	                return createAuthenticationToken(loginDTO1, applicant);
//        	            } else {
//        	                return ResponseEntity.badRequest().body("Login failed");
//        	            }
//        	        } catch (BadCredentialsException e) {
//        	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username");
//        	        } catch (Exception e) {
//        	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during login");
//        	        }
//   	    }

	@PostMapping("/changeStatus/{id}")
	    public ResponseEntity<String> changeApplicantStatus(@PathVariable long id){
	    	//create service layer method
	    	//based on id fetch applicant
	    	//if the applicant status is active change it to inactive viceversa
	    	//return successfully status changed
	    	try {
	    		// Fetch the applicant by id
		    	Applicant applicant=regsiterService.findById(id);
		    	
		    	// Toggle the status
	            if (applicant.getAppicantStatus().equalsIgnoreCase("active")) {
	                applicant.setAppicantStatus("inactive");
	            } else {
	                applicant.setAppicantStatus("active");
	            }
	            
	            // Save the updated applicant
	            registerrepo.save(applicant);
		    	return ResponseEntity.ok("Applicant status changed successfully.");
	    	}catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while changing applicant status.");
	        }
	    }
 
	    private ResponseEntity<Object> createAuthenticationToken(LoginDTO loginDTO,  Applicant applicant ) throws Exception {
	    	try {
	    		if (regsiterService.isGoogleSignIn(loginDTO)) {
	                // Handle Google sign-in separately
	    			System.out.println("Now I am at token gen");
	                UserDetails userDetails = myUserDetailsService.loadUserByUsername(applicant.getEmail());
	                final String jwt = jwtTokenUtil.generateToken(userDetails);
	                return ResponseHandler.generateResponse("Login successfully" + userDetails.getAuthorities(), HttpStatus.OK, new AuthenticationResponse(jwt), applicant.getEmail(), applicant.getName(), applicant.getId());
	            } else {
	                // Regular login functionality
	                authenticationManager.authenticate(
	                        new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
	                );
	                UserDetails userDetails = myUserDetailsService.loadUserByUsername(applicant.getEmail());
	                final String jwt = jwtTokenUtil.generateToken(userDetails);
	                return ResponseHandler.generateResponse("Login successfully" + userDetails.getAuthorities(), HttpStatus.OK, new AuthenticationResponse(jwt), applicant.getEmail(), applicant.getName(), applicant.getId());
	            }
//	            authenticationManager.authenticate(
//	                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
//	            );
//	            UserDetails userDetails = myUserDetailsService.loadUserByUsername(applicant.getEmail());
//	            final String jwt = jwtTokenUtil.generateToken(userDetails);
//	            return ResponseHandler.generateResponse("Login successfully" + userDetails.getAuthorities(), HttpStatus.OK, new AuthenticationResponse(jwt), applicant.getEmail(), applicant.getName(), applicant.getId());
	        } catch (BadCredentialsException e) {
	            throw new CustomException("Incorrect username or password", HttpStatus.UNAUTHORIZED);
	        }
	           
		}
 
 
	   @PostMapping("/applicantsendotp")
	    public ResponseEntity<String> sendOtp(@RequestBody Applicant request) {
	        String userEmail = request.getEmail();
	        String userMobile = request.getMobilenumber();
	        try {
	            Applicant applicantByEmail = regsiterService.findByEmail(userEmail);
	            Applicant applicantByMobile = regsiterService.findByMobilenumber(userMobile);
	            JobRecruiter recruiterByEmail = findByEmail(userEmail);
	            JobRecruiter recruiterByMobile = findByMobilenumber(userMobile);
 
	            if (applicantByEmail == null && applicantByMobile == null && recruiterByEmail == null && recruiterByMobile == null) {
	                String otp = otpService.generateOtp(userEmail);
	                emailService.sendOtpEmail(userEmail, otp);
	                otpVerificationMap.put(userEmail, true);
	                return ResponseEntity.ok("OTP sent to your email.");
	            } else {
	                if (applicantByEmail != null) {
	                    throw new CustomException("Email is already registered as an Applicant.", null);
	                } else if (recruiterByEmail != null) {
	                    throw new CustomException("Email is already registered as a Recruiter.", null);
	                } else if (applicantByMobile != null) {
	                    throw new CustomException("Mobile number is already registered as an Applicant.", null);
	                } else if (recruiterByMobile != null) {
	                    throw new CustomException("Mobile number is already registered as a Recruiter.", null);
	                } else {
	                    throw new CustomException("Email or mobile number is already registered.", null);
	                }
	            }
	        } catch (CustomException e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending OTP");
	        }
	    }
	    @PostMapping("/forgotpasswordsendotp")
	    public ResponseEntity<String> ForgotsendOtp(@RequestBody Applicant  request) {
	    	String userEmail = request.getEmail();
	        Applicant applicant = regsiterService.findByEmail(userEmail);
	        System.out.println(applicant);
	        if (applicant != null) {     
	            String otp = otpService.generateOtp(userEmail);
	         	            emailService.sendOtpEmail(userEmail, otp);
	 	            otpVerificationMap.put(userEmail, true);
	 	            System.out.println(otp);
	 	            return ResponseEntity.ok("OTP sent successfully");
	        }
	        else {
	        	 return ResponseEntity.badRequest().body("Email not found.");
	        }
	    }
 
	    @PostMapping("/applicantverify-otp")
	    public ResponseEntity<String> verifyOtp( @RequestBody  OtpVerificationRequest verificationRequest
 
	    )
	    {
	    	try {
	            String otp = verificationRequest.getOtp();
	            String email = verificationRequest.getEmail();
	            System.out.println(otp + email);
 
	            if (otpService.validateOtp(email, otp)) {
	                return ResponseEntity.ok("OTP verified successfully");
	            } else {
	                throw new CustomException("Incorrect OTP or Timeout.", HttpStatus.BAD_REQUEST);
	            }
	        } catch (CustomException e) {
	            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
	        } catch (Exception e) {
	        	e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error verifying OTP");
	        }
	    }
 
	    @PostMapping("/applicantreset-password/{email}")
	    public ResponseEntity<String> setNewPassword(@RequestBody NewPasswordRequest request,@PathVariable String email) {
	    	try {
	            String newpassword = request.getPassword();
	            String confirmedPassword = request.getConfirmedPassword();
	            	
	            if (email == null) {
	                  throw new CustomException("Email not found.", HttpStatus.BAD_REQUEST);
	            }
	           
	            Applicant applicant = regsiterService.findByEmail(email);
	                if (applicant == null) {
	                 throw new CustomException("User not found.", HttpStatus.BAD_REQUEST);
	            }
	            	
	            applicant.setPassword(passwordEncoder.encode(newpassword));
	           regsiterService.addApplicant(applicant);
	               return ResponseEntity.ok("Password reset was done successfully");
	        } catch (CustomException e) {
	        	
	            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
	        } catch (Exception e) {
	        	System.out.println(e.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error resetting password");
	        }
	    }
 
		@GetMapping("/viewApplicants")
 
	    public ResponseEntity<List<Applicant>> getAllApplicants() {
 
	        try {
	            List<Applicant> applicants = regsiterService.getAllApplicants();
	            return ResponseEntity.ok(applicants);
	        } catch (Exception e) {
	             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
 
	    }
		@PostMapping("/applicantsignOut")
	    public ResponseEntity<Void> signOut(@AuthenticationPrincipal Applicant user) {
			 try {
		            SecurityContextHolder.clearContext();
		            return ResponseEntity.noContent().build();
		        } catch (Exception e) {
		            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		        }
		    }
 
		public void setOtpService(OtpService otpService2) {
			otpService=otpService2;
			
		}
		@PostMapping("/authenticateUsers/{id}")
	    public String authenticateUser( @RequestBody PasswordRequest passwordRequest, @PathVariable long id) {
	        String newpassword = passwordRequest.getNewPassword();
	        String oldpassword = passwordRequest.getOldPassword();
	        return regsiterService.authenticateUser(id, oldpassword, newpassword);
	    }
	public JobRecruiter findByEmail(String userEmail) {
			try {
				System.out.println(userEmail);
	            return recruiterRepository.findByEmail(userEmail);
	            
	        } catch (Exception e) {
	        	
	            throw new CustomException("Error finding applicant by email", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	
		}
	    
	    public JobRecruiter findByMobilenumber(String userMobile) {
			try {
				
	            return recruiterRepository.findByMobilenumber(userMobile);
	            
	        } catch (Exception e) {
	        	
	            throw new CustomException("Error finding applicant by Mobile Number", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
		}
		
}
