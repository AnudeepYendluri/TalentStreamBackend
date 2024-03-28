package com.talentstream.service;
 
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.talentstream.dto.JobDTO;
import com.talentstream.dto.RecuriterSkillsDTO;
import com.talentstream.entity.Alerts;
import com.talentstream.entity.Applicant;
import com.talentstream.entity.ApplicantJobInterviewDTO;
import com.talentstream.entity.ApplicantStatusHistory;
import com.talentstream.entity.AppliedApplicantInfo;
import com.talentstream.entity.AppliedApplicantInfoDTO;
import com.talentstream.entity.ApplyJob;
import com.talentstream.entity.Job;
import com.talentstream.entity.MatchTypes;
import com.talentstream.entity.AppliedApplicantInfo;
import java.util.stream.Collectors;
import com.talentstream.entity.JobRecruiter;
import com.talentstream.entity.RecuriterSkills;
import com.talentstream.repository.AlertsRepository;
import com.talentstream.repository.ApplicantStatusHistoryRepository;
import com.talentstream.repository.ApplyJobRepository;
import com.talentstream.repository.JobRepository;
import com.talentstream.repository.JobRecruiterRepository;
import com.talentstream.repository.RegisterRepository;
import com.talentstream.repository.ScheduleInterviewRepository; 
import jakarta.persistence.EntityNotFoundException;
import com.talentstream.exception.CustomException;
@Service
public class ApplyJobService {
	 @Autowired
	   private ApplyJobRepository applyJobRepository;	
	 @Autowired
	   private ScheduleInterviewRepository scheduleInterviewRepository;	
	 @Autowired
		private CompanyLogoService companyLogoService;
	    @Autowired
	    private JobRepository jobRepository;
	    @Autowired
	    private RegisterRepository applicantRepository;
	    @Autowired
	    private ApplicantStatusHistoryRepository statusHistoryRepository;
	    @Autowired
	    private JavaMailSender javaMailSender;
	    @Autowired
	    private AlertsRepository alertsRepository;
	@Autowired
	    private JobRecruiterRepository jobRecruiterRepository;
public String ApplicantApplyJob(long  applicantId, long jobId) {
	    	
	    	try {
	            Applicant applicant = applicantRepository.findById(applicantId);
	            Job job = jobRepository.findById(jobId).orElse(null);
 
	            if (applicant == null || job == null) {
	                throw new CustomException("Applicant ID or Job ID not found", HttpStatus.NOT_FOUND);
	            }
 
	            else{
	            	if (applyJobRepository.existsByApplicantAndJob(applicant, job)) {
	                       	return "Job has already been applied by the applicant";
	            	}else {
	            		ApplyJob applyJob = new ApplyJob();
	    	            applyJob.setApplicant(applicant);
	    	            applyJob.setJob(job);
	    	            applyJobRepository.save(applyJob);
	    	            
	    	            job.setJobStatus("Already Applied");
	    	            job.setAlertCount(job.getAlertCount()+1);
	    	            job.setRecentApplicationDateTime(LocalDateTime.now());
				job.setNewStatus("newapplicants");
	    				jobRepository.save(job);
	    	            
	    	            // Increment alert count
	    		        incrementAlertCount(applyJob.getApplicant());
	    		        
	    		        //SaveStatusHistory
	    	            saveStatusHistory(applyJob, applyJob.getApplicantStatus());
	    	            Job jobs=applyJob.getJob();
	    	            if(jobs!=null) {
	    	            	JobRecruiter recruiter=jobs.getJobRecruiter();
	    	            	
	    	            	
	    	            	if(recruiter!=null) {
	    	            		String companyName=recruiter.getCompanyname();
	    	            		if(companyName!=null) {
	    	            			String cN=recruiter.getCompanyname();
	    	            			   
	    	            			String jobTitle = jobs.getJobTitle();
	    	            			recruiter.setAlertCount(recruiter.getAlertCount()+1);
	    	            			jobRecruiterRepository.save(recruiter);
	    	            			sendAlerts(applyJob,applyJob.getApplicantStatus(),cN,jobTitle);
	    	            			return "Job applied successfully";
	    	            		}
	    	            	}
	    	            }return "Company information not found for the given ApplyJob";
	            		}
	            	}
	            }catch (CustomException ex) {
	                    throw ex;
	             } catch (Exception e) {
	                    throw new CustomException("An error occurred while applying for the job: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	             }
	      }
public long countAppliedJobsForApplicant(long applicantId) {
    try {
        // Check if the applicant exists
        if (!applicantRepository.existsById(applicantId)) {
            // Throw CustomException if the applicant is not found
            throw new CustomException("Applicant not found", HttpStatus.NOT_FOUND);
        }
        // Use the custom query to count applied jobs
        return applyJobRepository.countByApplicantId(applicantId);
    } catch (CustomException e) {
        throw e; // Re-throw CustomException as is
    } catch (Exception e) {
        // Handle other exceptions as needed
        throw new CustomException("Error while counting applied jobs for the applicant", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
	    //This method is to increment count of alerts whenever recruiter updating the status
	    private void incrementAlertCount(Applicant applicant) {
			// TODO Auto-generated method stub
	    	if (applicant != null) {
	            int currentAlertCount = applicant.getAlertCount();
	            applicant.setAlertCount(currentAlertCount + 1);
	            applicantRepository.save(applicant);
	        }
		}
	    //This method is to display alerts whenever we click on Alerts
	    private
	    void sendAlerts(ApplyJob applyJob, String applicantStatus, String cN, String jobTitle) {
			// TODO Auto-generated method stub
	    	Alerts alerts=new Alerts();
			alerts.setApplyJob(applyJob);
			alerts.setApplicant(applyJob.getApplicant());
			alerts.setCompanyName(cN);
			alerts.setStatus(applicantStatus);			
			alerts.setJobTitle(jobTitle);
			alerts.setChangeDate(LocalDate.now());
			alertsRepository.save(alerts);
			// Send email to the applicant
	        sendEmailToApplicant(applyJob.getApplicant().getEmail(), cN, applicantStatus);
		}
		//This method is to send interview status to the applicant mail id
		private void sendEmailToApplicant(String toEmail, String cN, String applicantStatus) {
			// TODO Auto-generated method stub
			try {
				SimpleMailMessage message=new SimpleMailMessage();
				// Set email properties
				message.setTo(toEmail);
				message.setSubject("Job Alert Notification");
				// Customize your email content
	            String content = "Dear Applicant,\n\n"
	                    + "Your job application status has been updated to: " + applicantStatus + "\n"
	                    + "Company: " + cN + "\n\n"
	                    + "Thank you.\n\n"
	                    + "Best regards,\n"
	                    + "Your Company Name";
	            message.setText(content);
	            // Send the email
	            javaMailSender.send(message);
	        } catch (Exception e) {
	            // Handle exceptions, log, and consider appropriate error handling
	        	e.printStackTrace();
	        }
		}
		//This method is to save the track of statuses that updated by recruiter
		private void saveStatusHistory(ApplyJob applyJob, String applicationStatus) {
			// TODO Auto-generated method stub
			ApplicantStatusHistory statusHistory=new ApplicantStatusHistory();
			statusHistory.setApplyJob(applyJob);
			statusHistory.setStatus(applicationStatus);
			statusHistory.setChangeDate(LocalDate.now());
			statusHistoryRepository.save(statusHistory);
		}
	    public List<ApplyJob> getAppliedApplicantsForJob(Long jobId) {
	    	 try {
	             return applyJobRepository.findByJobId(jobId);
	         } catch (Exception e) {
	             throw new CustomException("Failed to retrieve applied applicants for the job", HttpStatus.INTERNAL_SERVER_ERROR);
	         }
	    }
	    public List<JobDTO> getAppliedJobsForApplicant(long applicantId) {
			List<JobDTO> result = new ArrayList<>();
    try {
        List<ApplyJob> appliedJobs = applyJobRepository.findByApplicantId(applicantId);
        for (ApplyJob appliedJob : appliedJobs) {
            Job job = appliedJob.getJob();
            JobDTO jobDTO = new JobDTO();
            jobDTO.setId(job.getId());
            jobDTO.setRecruiterId(job.getJobRecruiter().getRecruiterId());
            jobDTO.setCompanyname(job.getJobRecruiter().getCompanyname());
            jobDTO.setMobilenumber(job.getJobRecruiter().getMobilenumber());
            jobDTO.setEmail(job.getJobRecruiter().getEmail());
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
            Set<RecuriterSkillsDTO> skillsDTOSet = new HashSet<>();
            for (RecuriterSkills skill : job.getSkillsRequired()) {
                RecuriterSkillsDTO skillDTO = new RecuriterSkillsDTO();
                skillDTO.setSkillName(skill.getSkillName());
                skillDTO.setMinimumExperience(skill.getMinimumExperience());
                skillsDTOSet.add(skillDTO);
            }
            jobDTO.setSkillsRequired(skillsDTOSet);
            jobDTO.setJobHighlights(job.getJobHighlights());
            jobDTO.setDescription(job.getDescription());
            jobDTO.setCreationDate(job.getCreationDate());
            jobDTO.setCompanyname(job.getJobRecruiter().getCompanyname());
            jobDTO.setMobilenumber(job.getJobRecruiter().getMobilenumber());
            jobDTO.setEmail(job.getJobRecruiter().getEmail());	           
            jobDTO.setApplyJobId(appliedJob.getApplyjobid());
 
       		    long jobRecruiterId = appliedJob.getJob().getJobRecruiter().getRecruiterId();
       		    byte[] imageBytes = null;
       		    try {
       		    	imageBytes = companyLogoService.getCompanyLogo(jobRecruiterId);
       		    }catch (CustomException ce) {
       	        	System.out.println(ce.getMessage());
       	        } 
       		    System.out.println("Job Recruiter ID: " + jobRecruiterId);
       		    System.out.println("Image Bytes: " + Arrays.toString(imageBytes));

       		 jobDTO.setLogoFile(imageBytes);
 
 

            result.add(jobDTO);
        }
    } catch (Exception e) {
        throw new CustomException("Failed to get applied jobs for the applicant", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return result;
  }
	public List<AppliedApplicantInfo> getAppliedApplicants2(long jobRecruiterId, MatchTypes matchTypes, String name, String email, String mobileNumber, String jobTitle, String applicantStatus, Integer minimumExperience, String skillName, String minimumQualification, String location) {
	        List<AppliedApplicantInfo> all = applyJobRepository.findAppliedApplicantsInfo(jobRecruiterId);
	        
	        System.out.println(matchTypes.getName());
	        System.out.println(matchTypes.getMobilenumber());

	        List<AppliedApplicantInfo> filteredList = all.stream()
	                .filter(applicant ->
	                        (name == null || applyMatchType(applicant.getName(), name, matchTypes.getName(), "is")) &&
	                        (email == null || applyMatchType(applicant.getEmail(), email, matchTypes.getEmail(), "contains")) &&
	                        (mobileNumber == null || applyMobileType(applicant.getMobilenumber(), mobileNumber, matchTypes.getMobilenumber(), "is")) &&
	                        (jobTitle == null || applyMatchType(applicant.getJobTitle(), jobTitle, matchTypes.getJobTitle(), "contains")) &&
	                        (applicantStatus == null || applyMatchType(applicant.getApplicantStatus(), applicantStatus, matchTypes.getApplicantStatus(), "contains")) &&
	                        (skillName == null || applyMatchType(applicant.getSkillName(), skillName, matchTypes.getSkillName(), "contains")) &&
	                        (minimumQualification == null || applyMatchType(applicant.getMinimumQualification(), minimumQualification, matchTypes.getMinimumQualification(), "contains")) &&
	                        (location == null || applyMatchType(applicant.getLocation(), location, matchTypes.getLocation(), "contains")) &&
	                        (minimumExperience == null || applyExperienceMatchType(applicant.getMinimumExperience(), minimumExperience, matchTypes.getMinimumExperience(), "lessThan")))
	                .collect(Collectors.toList());

	     // Eliminate duplicates based on applyjobid while preserving order
	        Set<Long> uniqueApplyJobIds = new HashSet<>();
	        List<AppliedApplicantInfo> uniqueList = new ArrayList<>();

	        for (AppliedApplicantInfo applicant : filteredList) {
	            long applyJobId = applicant.getApplyjobid();
	            if (!uniqueApplyJobIds.contains(applyJobId) && applyJobId >=1) {
	                uniqueApplyJobIds.add(applyJobId);
	                uniqueList.add(applicant);
	            }
	        }
	        return uniqueList;
	    }

	    private boolean applyMatchType(String value, String filterValue, String matchValue, String matchType) {
	        if (matchValue == null) {
	            return true; // If matchValue is null, it means it's not provided, so return true
	        }
	        if (matchValue.equalsIgnoreCase("contains")) {
	            return value.toLowerCase().contains(filterValue.toLowerCase());
	        } else if (matchValue.equalsIgnoreCase("is")) {
	            return value.equalsIgnoreCase(filterValue);
	        }
	        return false;
	    }
	    private boolean applyMobileType(String value, String filterValue, String matchValue, String matchType) {
	        if (matchValue == null) {
	            return true; // If matchValue is null, it means it's not provided, so return true
	        }
	        if (matchValue.equalsIgnoreCase("contains")) {
	            return value.toLowerCase().contains(filterValue.toLowerCase());
	        } else if (matchValue.equalsIgnoreCase("is")) {
	            if (filterValue.length() == value.length()) { // Perform exact match if filter length matches value length
	                return value.equalsIgnoreCase(filterValue);
	            } else {
	                return false; // If lengths don't match, it's not an exact match
	            }
	        }
	        return false;
	    }

	    private boolean applyExperienceMatchType(int value, int filterValue, String matchValue, String matchType) {
	        if (matchValue == null) {
	            return true; // If matchValue is 0, it means it's not provided, so return true
	        }
	        if (matchValue.equalsIgnoreCase("greaterThan")) {
	            return value > filterValue;
	        } else if (matchValue.equalsIgnoreCase("lessThan")) {
	            return value < filterValue;
	        }
	        else if (matchValue.equalsIgnoreCase("is")) {
	            return value == filterValue;
	        }
	        return false;
	    }
	public Map<String, List<AppliedApplicantInfoDTO>> getAppliedApplicants(long jobRecruiterId) {
	    List<AppliedApplicantInfo> appliedApplicants = applyJobRepository.findAppliedApplicantsInfo(jobRecruiterId);
	    Map<String, List<AppliedApplicantInfoDTO>> applicantMap = new HashMap<>();
	    for (AppliedApplicantInfo appliedApplicantInfo : appliedApplicants) {
	        String applicantKey = appliedApplicantInfo.getEmail() + "_" + appliedApplicantInfo.getApplyjobid();
	        if (!applicantMap.containsKey(applicantKey)) {
	            List<AppliedApplicantInfoDTO> dtoList = new ArrayList<>();
	            dtoList.add(mapToDTO(appliedApplicantInfo));
	            applicantMap.put(applicantKey, dtoList);
	        } else {
	            List<AppliedApplicantInfoDTO> existingDTOList = applicantMap.get(applicantKey);
	            boolean found = false;
	            for (AppliedApplicantInfoDTO existingDTO : existingDTOList) {
	                if (existingDTO.getName().equals(appliedApplicantInfo.getName())) {
	                    existingDTO.addSkill(appliedApplicantInfo.getSkillName(), appliedApplicantInfo.getMinimumExperience());
	                    found = true;
	                    break;
	                }
	            }
	            if (!found) {
	                AppliedApplicantInfoDTO dto = mapToDTO(appliedApplicantInfo);
	                existingDTOList.add(dto);
	            }
	        }
	    }
	    return applicantMap;
	}

	public Map<String, List<AppliedApplicantInfoDTO>> getAppliedApplicants1(long jobRecruiterId,long id) {
	    List<AppliedApplicantInfo> appliedApplicants = applyJobRepository.findAppliedApplicantsInfoWithJobId(jobRecruiterId, id);
	    Map<String, List<AppliedApplicantInfoDTO>> applicantMap = new HashMap<>();
	    for (AppliedApplicantInfo appliedApplicantInfo : appliedApplicants) {
	        String applicantKey = appliedApplicantInfo.getEmail() + "_" + appliedApplicantInfo.getApplyjobid();
	        if (!applicantMap.containsKey(applicantKey)) {
	            List<AppliedApplicantInfoDTO> dtoList = new ArrayList<>();
	            dtoList.add(mapToDTO(appliedApplicantInfo));
	            applicantMap.put(applicantKey, dtoList);
	        } else {
	            List<AppliedApplicantInfoDTO> existingDTOList = applicantMap.get(applicantKey);
	            boolean found = false;
	            for (AppliedApplicantInfoDTO existingDTO : existingDTOList) {
	                if (existingDTO.getName().equals(appliedApplicantInfo.getName())) {
	                    existingDTO.addSkill(appliedApplicantInfo.getSkillName(), appliedApplicantInfo.getMinimumExperience());
	                    found = true;
	                    break;
	                }
	            }
	            if (!found) {
	                AppliedApplicantInfoDTO dto = mapToDTO(appliedApplicantInfo);
	                existingDTOList.add(dto);
	            }
	        }
	    }
		Optional<Job> optionalJob = jobRepository.findById(id);

	    if (optionalJob.isPresent()) {
	        Job job = optionalJob.get();
	        job.setNewStatus("oldApplicants"); // Set the new status to the job object
	        jobRepository.save(job); // Save the updated job object
	    }
	    return applicantMap;
	}

 
private AppliedApplicantInfoDTO mapToDTO(AppliedApplicantInfo appliedApplicantInfo) {
	 AppliedApplicantInfoDTO dto = new AppliedApplicantInfoDTO();
	    dto.setApplyjobid(appliedApplicantInfo.getApplyjobid());
	    dto.setName(appliedApplicantInfo.getName());
	    dto.setId(appliedApplicantInfo.getId());
	    dto.setEmail(appliedApplicantInfo.getEmail());
	    dto.setMobilenumber(appliedApplicantInfo.getMobilenumber());
	    dto.setJobTitle(appliedApplicantInfo.getJobTitle());
	    dto.setApplicantStatus(appliedApplicantInfo.getApplicantStatus());
	    dto.setMinimumExperience(appliedApplicantInfo.getMinimumExperience());
	    dto.setMinimumQualification(appliedApplicantInfo.getMinimumQualification());
	    List<String> skills = new ArrayList<>();
	    skills.add(appliedApplicantInfo.getSkillName());
	    dto.setSkillName(skills);
	    dto.setLocation(appliedApplicantInfo.getLocation());
	    return dto;
}
 
public String updateApplicantStatus(Long applyJobId, String newStatus) {
    ApplyJob applyJob = applyJobRepository.findById(applyJobId)
            .orElseThrow(() -> new EntityNotFoundException("ApplyJob not found"));
    Job job=applyJob.getJob();
    if(job!=null) {
    	JobRecruiter recruiter=job.getJobRecruiter();
    	if(recruiter!=null) {
    		String companyName=recruiter.getCompanyname();
    		String jobTitle = job.getJobTitle();
    		if(companyName!=null) {
    			applyJob.setApplicantStatus(newStatus);
    		    applyJobRepository.save(applyJob);
    		    //Increment alert count
    			incrementAlertCount(applyJob.getApplicant());
    			// Save status history
    		    saveStatusHistory(applyJob, applyJob.getApplicantStatus());
    		    //Send alerts
    		    sendAlerts(applyJob,applyJob.getApplicantStatus(),companyName,jobTitle);
    		    return "Applicant status updated to: " + newStatus;
    		}
    	}
    }
    return "Company information not found for the given ApplyJob";    
}
public List<ApplicantJobInterviewDTO> getApplicantJobInterviewInfoForRecruiterAndStatus(
        long recruiterId, String applicantStatus) {
	try {
        return scheduleInterviewRepository.getApplicantJobInterviewInfoByRecruiterAndStatus(recruiterId, applicantStatus);
    } catch (Exception e) {
        throw new CustomException("Failed to retrieve applicant job interview info", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
public long countJobApplicantsByRecruiterId(Long recruiterId) {
	try {
        return applyJobRepository.countJobApplicantsByRecruiterId(recruiterId);
    } catch (Exception e) {
        throw new CustomException("Failed to count job applicants for the recruiter", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
public long countSelectedApplicants() {
	 try {
         return applyJobRepository.countByApplicantStatus("Selected");
     } catch (Exception e) {
         throw new CustomException("Failed to count selected applicants", HttpStatus.INTERNAL_SERVER_ERROR);
     }
}
public long countShortlistedAndInterviewedApplicants() {
	try {
        List<String> desiredStatusList = Arrays.asList("Shortlisted", "Interviewing");
        return applyJobRepository.countByApplicantStatusIn(desiredStatusList);
    } catch (Exception e) {
        throw new CustomException("Failed to count shortlisted and interviewed applicants", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
//This method is to get list of statuses related to particular job
public List<ApplicantStatusHistory> getApplicantStatusHistory(long applyJobId) {
	// TODO Auto-generated method stub
	return statusHistoryRepository.findByApplyJob_ApplyjobidOrderByChangeDateDesc(applyJobId);
}
//This method is to get alerts sent by recruiter
//public List<Alerts> getAlerts(long applyjobid) {
//	// TODO Auto-generated method stub
//	return alertsRepository.findByApplyJob_applyJobIdOrderByChangeDateDesc(applyjobid);
//}
public List<Alerts> getAlerts(long applicantId) {
	return alertsRepository.findByApplicantIdOrderByChangeDateDesc(applicantId);
}
//This method is to reset count of alerts to zero once after reading all the alert messages.
public void resetAlertCount(long applicantId) {
	// TODO Auto-generated method stub
	try {
		
		Applicant applicant=applicantRepository.findById(applicantId);
		
		applicant.setAlertCount(0);
		applicantRepository.save(applicant);
		
		
		
  } catch (Exception e) {
      // Handle exceptions, log, and consider appropriate error handling
  	e.printStackTrace();
  }
}
public long countShortlistedAndInterviewedApplicants(long recruiterId) {
    try {
        List<String> desiredStatusList = Arrays.asList("shortlisted", "interviewing");
        return applyJobRepository.countShortlistedAndInterviewedApplicants(recruiterId, desiredStatusList);
    } catch (Exception e) {
        throw new CustomException("Failed to count shortlisted and interviewed applicants", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
public ApplyJob getByJobAndApplicant(Long jobId, Long applicantId) {
    try {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new EntityNotFoundException("Job not found"));
        Applicant applicant = applicantRepository.findById(applicantId);
        return applyJobRepository.findByJobAndApplicant(job, applicant);
    } catch (EntityNotFoundException e) {
        throw new CustomException("Job or Applicant not found", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
        throw new CustomException("Error while retrieving ApplyJob", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
}
 
 
