package com.talentstream.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.talentstream.dto.ApplicantProfileDTO;
import com.talentstream.dto.ApplicantProfileViewDTO;
import com.talentstream.exception.CustomException;
import com.talentstream.service.ApplicantProfileService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin("*")
@RestController
@RequestMapping("/applicantprofile")
public class ApplicantProfileController {
	private final ApplicantProfileService applicantProfileService;
	
	  private static final Logger logger = LoggerFactory.getLogger(ApplicantProfileController.class);
    
	  @Autowired
    public ApplicantProfileController(ApplicantProfileService applicantProfileService) {
        this.applicantProfileService = applicantProfileService;		
    }

    @PostMapping("/createprofile/{applicantid}")
    public ResponseEntity<String> createOrUpdateApplicantProfile(@PathVariable long applicantid, @RequestBody ApplicantProfileDTO applicantProfileDTO) throws IOException {
        try {
            String result = applicantProfileService.createOrUpdateApplicantProfile(applicantid, applicantProfileDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (CustomException e) {
        	logger.error("INTERNAL_SERVER_ERROR");
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @GetMapping("/getdetails/{applicantid}")
    public ResponseEntity<?> getApplicantProfileById(@PathVariable long applicantid) {
        try {
            ApplicantProfileDTO applicantProfileDTO = applicantProfileService.getApplicantProfileById(applicantid);
            return ResponseEntity.ok(applicantProfileDTO);
        } catch (CustomException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(errorResponse);
        }
    }
       @DeleteMapping("/deletedetails/{applicantId}")
       public ResponseEntity<Void> deleteApplicantProfile(@PathVariable int applicantId) {
    	   try {
               applicantProfileService.deleteApplicantProfile(applicantId);
               return ResponseEntity.noContent().build();
           } catch (CustomException e) {
               return ResponseEntity.status(e.getStatus()).build();
           }
       }
       
       @PutMapping("/updateprofile/{applicantid}")
       public ResponseEntity<String> updateApplicantProfile(@PathVariable long applicantid, @RequestBody ApplicantProfileViewDTO updatedProfileDTO) throws IOException {
           try {
        	           	   
               String result = applicantProfileService.updateApplicantProfile(applicantid, updatedProfileDTO);
               return ResponseEntity.status(HttpStatus.OK).body(result);
           } catch (CustomException e) {
               logger.error("INTERNAL_SERVER_ERROR");
               return ResponseEntity.status(e.getStatus()).body(e.getMessage());
           }
       }
       
       @GetMapping("/{applicantId}/profile-view")
       public ResponseEntity<ApplicantProfileViewDTO> getApplicantProfileViewDTO(@PathVariable long applicantId) {
           try {
               ApplicantProfileViewDTO dto = applicantProfileService.getApplicantProfileViewDTO(applicantId);
               return ResponseEntity.ok(dto);
           } catch (EntityNotFoundException e) {
               return ResponseEntity.notFound().build();
           }
       }
       @GetMapping("/{applicantId}/profile-view1")
       public ResponseEntity<ApplicantProfileViewDTO> getApplicantProfileViewDTO1(@PathVariable long applicantId) {
           try {
               ApplicantProfileViewDTO dto = applicantProfileService.getApplicantProfileViewDTO(applicantId);
               return ResponseEntity.ok(dto);
           } catch (EntityNotFoundException e) {
               return ResponseEntity.notFound().build();
           }
       }
	@GetMapping("/{applicantId}/profileid")
       public int getApplicantProfileId(@PathVariable int applicantId) {
           return applicantProfileService.getApplicantProfileById1(applicantId);
       }
      
}
