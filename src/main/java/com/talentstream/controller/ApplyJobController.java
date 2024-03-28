package com.talentstream.controller;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.talentstream.entity.AppliedApplicantInfo;
import com.talentstream.dto.JobDTO;
import com.talentstream.dto.ScheduleInterviewDTO;
import com.talentstream.entity.Alerts;
import com.talentstream.entity.Applicant;
import com.talentstream.entity.ApplicantJobInterviewDTO;
import com.talentstream.entity.ApplicantStatusHistory;
import com.talentstream.entity.AppliedApplicantInfoDTO;
import com.talentstream.entity.ApplyJob;
import com.talentstream.entity.Job;
import com.talentstream.entity.MatchTypes;
import com.talentstream.entity.ScheduleInterview;
import com.talentstream.service.ApplyJobService;
import com.talentstream.service.ScheduleInterviewService;

import jakarta.persistence.EntityNotFoundException;

import com.talentstream.exception.CustomException;
import com.talentstream.repository.RegisterRepository;
@RestController       
@RequestMapping("/applyjob")
public class ApplyJobController {
	
	  final ModelMapper modelMapper = new ModelMapper();
	 @Autowired
	    private ApplyJobService applyJobService;
	 @Autowired
	    private ScheduleInterviewService scheduleInterviewService;
	 
	 @Autowired
	 private RegisterRepository applicantRepository;
	 
	 private static final Logger logger = LoggerFactory.getLogger(ApplicantProfileController.class);
	 
	 @PostMapping("/applicants/applyjob/{applicantId}/{jobId}")
	    public ResponseEntity<String> saveJobForApplicant(
	            @PathVariable long applicantId,
	            @PathVariable long jobId
	    ){
		 try {
	
            String result = applyJobService.ApplicantApplyJob(applicantId, jobId);
            return ResponseEntity.ok(result);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        } catch (Exception e) {
          
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
	 }
	 
	 @GetMapping("/appliedapplicants/{jobId}")
	 public ResponseEntity<List<ApplyJob>> getAppliedApplicantsForJob(@PathVariable Long jobId) {
	        try {
	            List<ApplyJob> appliedApplicants = applyJobService.getAppliedApplicantsForJob(jobId);
	            return ResponseEntity.ok(appliedApplicants);
	        } catch (Exception e) {
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
	        }
	    }
	    
	 @GetMapping("/getAppliedJobs/{applicantId}")
	 public ResponseEntity<List<JobDTO>> getAppliedJobsForApplicant(@PathVariable long applicantId) {
	  
	 	 try {
	 	        List<JobDTO> appliedJobsDTO = applyJobService.getAppliedJobsForApplicant(applicantId);
	  
	 	        if (appliedJobsDTO.isEmpty()) {
	 	            return ResponseEntity.noContent().build();
	 	        }
	  
	 	        return ResponseEntity.ok(appliedJobsDTO);
	 	    } catch (CustomException e) {
	 	        return ResponseEntity.status(e.getStatus()).body(new ArrayList<>());
	 	    } catch (Exception e) {
	 	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
	 	    }
	  
	 }
 
 
 @GetMapping("/countAppliedJobs/{applicantId}")
 public ResponseEntity<?> countAppliedJobsForApplicant(@PathVariable long applicantId) {
     try {
         long count = applyJobService.countAppliedJobsForApplicant(applicantId);
         return ResponseEntity.ok(count);
     } catch (CustomException e) {
         return ResponseEntity.status(e.getStatus()).body(e.getMessage());
     } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
     }
 }
 
 
 @GetMapping("/recruiter/{jobRecruiterId}/appliedapplicants")
 public ResponseEntity<Map<String, List<AppliedApplicantInfoDTO>>> getAppliedApplicantsForRecruiter(@PathVariable long jobRecruiterId) {
      try {
          Map<String, List<AppliedApplicantInfoDTO>> appliedApplicantsMap = applyJobService.getAppliedApplicants(jobRecruiterId);
          return ResponseEntity.ok(appliedApplicantsMap);
      } catch (CustomException e) {
          return ResponseEntity.status(e.getStatus()).body(new HashMap<>());
      } catch (Exception e) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HashMap<>());
      }
 }
	 @PostMapping("/recruiter/{jobRecruiterId}/appliedapplicants1")
 public ResponseEntity<List<AppliedApplicantInfo>> getAppliedApplicantsForRecruiter(
         @PathVariable long jobRecruiterId,
         @RequestParam(required = false) String name,
         @RequestParam(required = false) String email,
         @RequestParam(required = false) String mobileNumber,
         @RequestParam(required = false) String jobTitle,
         @RequestParam(required = false) String applicantStatus,
         @RequestParam(required = false) Integer minimumExperience,
         @RequestParam(required = false) String skillName,
         @RequestParam(required = false) String minimumQualification,
         @RequestParam(required = false) String location,
         @RequestBody MatchTypes matchTypes) {
     try {
    	 
         List<AppliedApplicantInfo> appliedApplicants = applyJobService.getAppliedApplicants2(jobRecruiterId,matchTypes, name, email, mobileNumber, jobTitle, applicantStatus, minimumExperience, skillName, minimumQualification, location);
         return ResponseEntity.ok(appliedApplicants);
     } catch (CustomException e) {
         return ResponseEntity.status(e.getStatus()).body(null);
     } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
     }
 }
 @GetMapping("/recruiter/{jobRecruiterId}/appliedapplicants/{id}")
 public ResponseEntity<Map<String, List<AppliedApplicantInfoDTO>>> getAppliedApplicantsForRecruiter1(@PathVariable long jobRecruiterId,@PathVariable long id) {
      try {
          Map<String, List<AppliedApplicantInfoDTO>> appliedApplicantsMap = applyJobService.getAppliedApplicants1(jobRecruiterId,id);
          return ResponseEntity.ok(appliedApplicantsMap);
      } catch (CustomException e) {
          return ResponseEntity.status(e.getStatus()).body(new HashMap<>());
      } catch (Exception e) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HashMap<>());
      }
 }

 @PostMapping("/scheduleInterview/{applyJobId}")
 public ResponseEntity<Void> createScheduleInterview(
         @PathVariable Long applyJobId,
         @RequestBody ScheduleInterviewDTO scheduleInterviewDTO) {
	 try {
         ScheduleInterview scheduleInterview = modelMapper.map(scheduleInterviewDTO, ScheduleInterview.class);
         ScheduleInterviewDTO createdInterview = scheduleInterviewService.createScheduleInterview(applyJobId, scheduleInterview);

         return createdInterview != null ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
     } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
     }
 }
   
   
 @PutMapping("/recruiters/applyjob-update-status/{applyJobId}/{newStatus}")
   public ResponseEntity<String> updateApplicantStatus(
           @PathVariable Long applyJobId,
           @PathVariable String newStatus) {
	   try {
	        String updateMessage = applyJobService.updateApplicantStatus(applyJobId, newStatus);
	        return ResponseEntity.ok(updateMessage);
	    } catch (CustomException e) {
	        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
	    } catch (Exception e) {
	      
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
	    }
   }
   @GetMapping("/recruiter/{recruiterId}/interviews/{status}")
   public ResponseEntity<List<ApplicantJobInterviewDTO>> getApplicantJobInterviewInfo(
           @PathVariable("recruiterId") long recruiterId,
           @PathVariable("status") String status) {
	   try {
	        List<ApplicantJobInterviewDTO> interviewInfo = applyJobService.getApplicantJobInterviewInfoForRecruiterAndStatus(recruiterId, status);
	        return ResponseEntity.ok(interviewInfo);
	    } catch (CustomException e) {
	        return ResponseEntity.status(e.getStatus()).body(new ArrayList<>());
	    } catch (Exception e) {
	       
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
	    }
   }
   
   @GetMapping("/recruiters/applyjobapplicantscount/{recruiterId}")
   public ResponseEntity<Long> countJobApplicantsByRecruiterId( @PathVariable Long recruiterId) {
	   try {
	        long count = applyJobService.countJobApplicantsByRecruiterId(recruiterId);
	        return ResponseEntity.ok(count);
	    } catch (CustomException e) {
	        return ResponseEntity.status(e.getStatus()).body(0L); 
	    } catch (Exception e) {
	     
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L); 
	    }
   }
   
   @GetMapping("/recruiters/selected/count")
   public ResponseEntity<Long> countSelectedApplicants() {
       
	   try {
	        long count = applyJobService.countSelectedApplicants();
	        return ResponseEntity.ok(count);
	    } catch (CustomException e) {
	        return ResponseEntity.status(e.getStatus()).body(0L); 
	    } catch (Exception e) {
	      
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L); 
	    }
   }
   
   @GetMapping("/recruiters/countShortlistedAndInterviewed")
   public ResponseEntity<Long> countShortlistedAndInterviewedApplicants() {
	   try {
	        long count = applyJobService.countShortlistedAndInterviewedApplicants();
	        return ResponseEntity.ok(count);
	    } catch (CustomException e) {
	        return ResponseEntity.status(e.getStatus()).body(0L); 
	    } catch (Exception e) {
	       
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L); 
	    }
   }
   
   @GetMapping("/current-date")
   public ResponseEntity<List<ScheduleInterviewDTO>> getScheduleInterviewsForCurrentDate() {
       try {
           List<ScheduleInterview> scheduleInterviews = scheduleInterviewService.getScheduleInterviewsForCurrentDate();

           if (scheduleInterviews.isEmpty()) {
               return ResponseEntity.noContent().build();
           }
           List<ScheduleInterviewDTO> scheduleInterviewDTOs = scheduleInterviews.stream()
                   .map(interview -> modelMapper.map(interview, ScheduleInterviewDTO.class))
                   .collect(Collectors.toList());

           return ResponseEntity.ok(scheduleInterviewDTOs);
       } catch (CustomException e) {
           return ResponseEntity.status(e.getStatus()).body(new ArrayList<>());
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
       }
   }
   @GetMapping("/getScheduleInterviews/applicant/{applyJobId}/{applicantId}")
   public ResponseEntity<List<ScheduleInterviewDTO>> getScheduleInterviews(
           @PathVariable Long applicantId, @PathVariable Long applyJobId) {
       try {
           List<ScheduleInterview> scheduleInterviews = scheduleInterviewService.getScheduleInterviewsByApplicantAndApplyJob(applicantId, applyJobId);

           if (scheduleInterviews.isEmpty()) {
               return ResponseEntity.noContent().build();
           }

           List<ScheduleInterviewDTO> scheduleInterviewDTOs = scheduleInterviews.stream()
                   .map(interview -> modelMapper.map(interview, ScheduleInterviewDTO.class))
                   .collect(Collectors.toList());
           return ResponseEntity.ok(scheduleInterviewDTOs);
       } catch (CustomException e) {
           return ResponseEntity.status(e.getStatus()).body(new ArrayList<>());
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
       }
   }
   
   @GetMapping("/recruiters/applyjob-status-history/{applyJobId}")
	public ResponseEntity<List<ApplicantStatusHistory>> getApplicantStatusHistory(@PathVariable long applyJobId){
		try {
			List<ApplicantStatusHistory> statusHistory=applyJobService.getApplicantStatusHistory(applyJobId);
			return ResponseEntity.ok(statusHistory);
		} catch (EntityNotFoundException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	@GetMapping("/applicant/job-alerts/{applicantId}")
	public ResponseEntity<List<Alerts>> getAlerts(@PathVariable long applicantId){
		try {
			List<Alerts> notifications=applyJobService.getAlerts(applicantId);
			// Reset alertCount to zero when fetching alerts
	        applyJobService.resetAlertCount(applicantId);
			return ResponseEntity.ok(notifications);
		} catch (EntityNotFoundException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	@GetMapping("/applicants/{applicantId}/unread-alert-count")
	public ResponseEntity<Integer> getUnreadAlertCount(@PathVariable long applicantId) {
	    try {
	        Applicant applicant = applicantRepository.findById(applicantId);
	        if (applicant != null) {
	            int unreadAlertCount = applicant.getAlertCount();
	            return ResponseEntity.ok(unreadAlertCount);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}
	@GetMapping("/recruiters/countShortlistedAndInterviewed/{recruiterId}")
	   public ResponseEntity<Long> countShortlistedAndInterviewedApplicants(@PathVariable Long recruiterId) {
		   try {
		        long count = applyJobService.countShortlistedAndInterviewedApplicants(recruiterId);
		        return ResponseEntity.ok(count);
		    } catch (CustomException e) {
		        return ResponseEntity.status(e.getStatus()).body(0L);
		    } catch (Exception e) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L);
		    }
	   }
	
	@DeleteMapping("/scheduleInterview/{scheduleInterviewId}")
	public ResponseEntity<String> deleteScheduledInterview(@PathVariable Long scheduleInterviewId) {
	    try {
	        scheduleInterviewService.deleteScheduledInterview(scheduleInterviewId);
	        return ResponseEntity.ok("Interview cancelled successfully");
	    } catch (EntityNotFoundException e) {
	        return ResponseEntity.notFound().build();
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

	    }

	}
}
 


