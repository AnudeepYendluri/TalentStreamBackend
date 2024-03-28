package com.talentstream.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
 
import com.talentstream.dto.JobDTO;
import com.talentstream.dto.RecuriterSkillsDTO;
import com.talentstream.entity.ApplicantProfile;
import com.talentstream.entity.Job;
import com.talentstream.entity.RecuriterSkills;
import com.talentstream.exception.CustomException;
import com.talentstream.repository.ApplicantProfileRepository;
import com.talentstream.service.CompanyLogoService;
import com.talentstream.service.FinRecommendedJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/recommendedjob")
public class FindRecommendedJobController {
	private final FinRecommendedJobService finJobService;
	@Autowired
	private CompanyLogoService companyLogoService;
	 private static final Logger logger = LoggerFactory.getLogger(ApplicantProfileController.class);
    
	 @Autowired
	    private ApplicantProfileRepository applicantRepository;
	 
	 @Autowired
    public FindRecommendedJobController(FinRecommendedJobService finJobService) {
        this.finJobService = finJobService;
    }
	
	 @GetMapping("/findrecommendedjob/{applicantId}")
	    public ResponseEntity<List<JobDTO>> recommendJobsForApplicant(@PathVariable String applicantId) {
	        try {
	            long applicantIdLong = Long.parseLong(applicantId);
	            ApplicantProfile applicantProfile = applicantRepository.findByApplicantId(applicantIdLong);

	            if (applicantProfile == null || !applicantProfile.getApplicant().getAppicantStatus().equalsIgnoreCase("active")) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
	            }

	            List<Job> recommendedJobs = finJobService.findJobsMatchingApplicantProfile(applicantProfile);

	            if (recommendedJobs.isEmpty()) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
	            } else {
	                List<JobDTO> jobDTOs = recommendedJobs.stream()
	                        .map(job -> convertEntityToDTO(job))
	                        .collect(Collectors.toList());

	                for (JobDTO job : jobDTOs) {
	                    long jobRecruiterId = job.getRecruiterId();
	                    byte[] imageBytes = null;
	                    try {
	                        imageBytes = companyLogoService.getCompanyLogo(jobRecruiterId);
	                    } catch (CustomException ce) {
	                        System.out.println(ce.getMessage());
	                    }
	                  //  System.out.println("Job Recruiter ID: " + jobRecruiterId);
	                  //  System.out.println("Image Bytes: " + Arrays.toString(imageBytes));
	                    job.setLogoFile(imageBytes);
	                }

	                return ResponseEntity.ok(jobDTOs);
	            }

	        } catch (NumberFormatException ex) {
	            throw new CustomException("Invalid applicant ID format", HttpStatus.BAD_REQUEST);
	        } catch (CustomException ce) {
	            System.out.println(ce.getMessage());
	            return ResponseEntity.status(ce.getStatus()).body(Collections.emptyList());
	        } catch (Exception e) {
	            System.out.println(e.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
	        }
	    }
    
    private JobDTO convertEntityToDTO(Job job) {
        JobDTO jobDTO = new JobDTO();
        jobDTO.setId(job.getId());
        jobDTO.setJobTitle(job.getJobTitle());
        jobDTO.setMinimumExperience(job.getMinimumExperience());
        jobDTO.setMaximumExperience(job.getMaximumExperience());
        jobDTO.setMinSalary(job.getMinSalary());
        jobDTO.setMaxSalary(job.getMaxSalary());
        jobDTO.setLocation(job.getLocation());
        jobDTO.setEmployeeType(job.getEmployeeType());
        jobDTO.setIndustryType(job.getIndustryType());
        jobDTO.setMinimumQualification(job.getMinimumQualification());        
        jobDTO.setRecruiterId(job.getJobRecruiter().getRecruiterId());
        jobDTO.setCompanyname(job.getJobRecruiter().getCompanyname());
        jobDTO.setEmail(job.getJobRecruiter().getEmail());
        jobDTO.setMobilenumber(job.getJobRecruiter().getMobilenumber());
        jobDTO.setSpecialization(job.getSpecialization());
        jobDTO.setJobHighlights(job.getJobHighlights());
        jobDTO.setDescription(job.getDescription());
        jobDTO.setCreationDate(job.getCreationDate());
        jobDTO.setIsSaved(job.getIsSaved());
        //jobDTO.setSaveJobStatus(job.getSaveJobStatus());
        
        Set<RecuriterSkillsDTO> skillsDTOList = job.getSkillsRequired().stream()
                .map(this::convertSkillsEntityToDTO)
                .collect(Collectors.toSet());
        jobDTO.setSkillsRequired(skillsDTOList);  
        return jobDTO;
    }
    private RecuriterSkillsDTO convertSkillsEntityToDTO(RecuriterSkills skill) {
        RecuriterSkillsDTO skillDTO = new RecuriterSkillsDTO();
        skillDTO.setSkillName(skill.getSkillName());
        skillDTO.setMinimumExperience(skill.getMinimumExperience());
        return skillDTO;
    }
    
    @GetMapping("/countRecommendedJobsForApplicant/{applicantId}")
    public long countRecommendedJobsForApplicant(@PathVariable long applicantId) {
        return finJobService.countRecommendedJobsForApplicant(applicantId);
    }
	
}
 
