package com.talentstream.controller;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
 
import com.talentstream.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import com.talentstream.dto.JobDTO;
import com.talentstream.entity.Job;
import com.talentstream.service.CompanyLogoService;
import com.talentstream.service.SavedJobService;
 
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/savedjob")
public class SavedJobController {
    final ModelMapper modelMapper = new ModelMapper();
	 @Autowired
	    private SavedJobService savedJobService;
	 @Autowired
		private CompanyLogoService companyLogoService;
	 private static final Logger logger = LoggerFactory.getLogger(ApplicantProfileController.class);
	    @PostMapping("/applicants/savejob/{applicantId}/{jobId}")
	    public ResponseEntity<String> saveJobForApplicant(
	            @PathVariable long applicantId,
	            @PathVariable long jobId
	    ) {
	    	try {
	            savedJobService.saveJobForApplicant(applicantId, jobId);
	            return ResponseEntity.ok("Job saved successfully for the applicant.");
	        } catch (CustomException e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving job for the applicant.");
	        }
	     }
 
	    
    @GetMapping("/getSavedJobs/{applicantId}")
	    public ResponseEntity<List<JobDTO>> getSavedJobsForApplicantAndJob(
	            @PathVariable long applicantId
	    ) {
    	try {
            List<Job> savedJobs = savedJobService.getSavedJobsForApplicant(applicantId);
 
            if (savedJobs.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
	 List<JobDTO> savedJobsDTO = savedJobs.stream()
            		.map(job -> {
        	            JobDTO jobDTO = modelMapper.map(job, JobDTO.class);
        	            jobDTO.setCompanyname(job.getJobRecruiter().getCompanyname());
        	            jobDTO.setMobilenumber(job.getJobRecruiter().getMobilenumber());
        	            jobDTO.setEmail(job.getJobRecruiter().getEmail());
        	            jobDTO.setRecruiterId(job.getJobRecruiter().getRecruiterId());
        	            return jobDTO;
        	        })
        	        .collect(Collectors.toList());
	 for (JobDTO job : savedJobsDTO) {
		    long jobRecruiterId = job.getRecruiterId();
		    byte[] imageBytes = null;
		    try {
		    	imageBytes = companyLogoService.getCompanyLogo(jobRecruiterId);
		    }catch (CustomException ce) {
	        	System.out.println(ce.getMessage());
	        } 

 
		   
		        job.setLogoFile(imageBytes);
		}
 
            return ResponseEntity.ok(savedJobsDTO);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    @GetMapping("/countSavedJobs/{applicantId}")
    public ResponseEntity<?> countSavedJobsForApplicant(@PathVariable long applicantId) {
        try {
            long count = savedJobService.countSavedJobsForApplicant(applicantId);
            return ResponseEntity.ok(count);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}