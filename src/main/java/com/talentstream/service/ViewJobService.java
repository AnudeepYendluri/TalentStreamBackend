package com.talentstream.service;
 
import java.util.Arrays;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
 
import com.talentstream.dto.JobDTO;
import com.talentstream.entity.ApplyJob;
import com.talentstream.entity.Job;
import com.talentstream.repository.JobRepository;
import com.talentstream.exception.CustomException;
@Service
public class ViewJobService {
	@Autowired
    private JobRepository jobRepository;
	@Autowired
	private CompanyLogoService companyLogoService;
	@Autowired
    private ApplyJobService applyJobService;
public ResponseEntity<JobDTO> getJobDetailsForApplicant(Long jobId) {
    final ModelMapper modelMapper = new ModelMapper();
	Job job = jobRepository.findById(jobId).orElse(null);
	if (job != null) {
        JobDTO jobDTO = new JobDTO();
        jobDTO.setRecruiterId(job.getJobRecruiter().getRecruiterId());
        jobDTO.setCompanyname(job.getJobRecruiter().getCompanyname());
        jobDTO.setMobilenumber(job.getJobRecruiter().getMobilenumber());
        jobDTO.setEmail(job.getJobRecruiter().getEmail());
        jobDTO.setJobStatus(job.getJobStatus());
        jobDTO.setJobTitle(job.getJobTitle());
        jobDTO.setMinimumExperience(job.getMinimumExperience());
        jobDTO.setMaximumExperience(job.getMaximumExperience());
        jobDTO.setMaxSalary(job.getMaxSalary());
        jobDTO.setMinSalary(job.getMinSalary());
        jobDTO.setLocation(job.getLocation());
        jobDTO.setEmployeeType(job.getEmployeeType());
        jobDTO.setIndustryType(job.getIndustryType());
        jobDTO.setMinimumQualification(job.getMinimumQualification());
        jobDTO.setSpecialization(job.getSpecialization());
        jobDTO.setJobHighlights(job.getJobHighlights());
        jobDTO.setDescription(job.getDescription());
        jobDTO.setCreationDate(job.getCreationDate());
        long jobRecruiterId = job.getJobRecruiter().getRecruiterId();
	    byte[] imageBytes = null;
	    try {
	    	imageBytes = companyLogoService.getCompanyLogo(jobRecruiterId);
	    }catch (CustomException ce) {
        	System.out.println(ce.getMessage());
        } 
	    System.out.println("Job Recruiter ID: " + jobRecruiterId);
	    System.out.println("Image Bytes: " + Arrays.toString(imageBytes));
 
	   
	        jobDTO.setLogoFile(imageBytes);
        return ResponseEntity.ok(jobDTO);
    } else {
        throw new CustomException("Job with ID " + jobId + " not found.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
public ResponseEntity<?> getJobDetailsForApplicant(Long jobId, Long applicantId) {
 
    final ModelMapper modelMapper = new ModelMapper();
    Job job = jobRepository.findById(jobId).orElse(null);
 
    if (job != null) {
    	JobDTO jobDTO = modelMapper.map(job, JobDTO.class);
        jobDTO.setRecruiterId(job.getJobRecruiter().getRecruiterId());
        jobDTO.setCompanyname(job.getJobRecruiter().getCompanyname());
        jobDTO.setMobilenumber(job.getJobRecruiter().getMobilenumber());
        jobDTO.setEmail(job.getJobRecruiter().getEmail());

        long jobRecruiterId = job.getJobRecruiter().getRecruiterId();
	    byte[] imageBytes = null;
	    try {
	    	imageBytes = companyLogoService.getCompanyLogo(jobRecruiterId);
	    }catch (CustomException ce) {
        	System.out.println(ce.getMessage());
        } 

	    jobDTO.setLogoFile(imageBytes);

 
        ApplyJob applyJob = applyJobService.getByJobAndApplicant(jobId, applicantId);
        if (applyJob != null) {
            jobDTO.setJobStatus("Already Applied");
        } else {
            jobDTO.setJobStatus("Apply now");
        }
 
        return ResponseEntity.ok(jobDTO);
    } else {
        throw new CustomException("Job with ID " + jobId + " not found.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
 
}