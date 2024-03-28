package com.talentstream.service;

import com.talentstream.exception.CustomException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.talentstream.entity.Applicant;
import com.talentstream.entity.Job;
import com.talentstream.entity.SavedJob;
import com.talentstream.repository.JobRepository;
import com.talentstream.repository.RegisterRepository;
import com.talentstream.repository.SavedJobRepository;

@Service
public class SavedJobService {
	@Autowired
    private SavedJobRepository savedJobRepository;

    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private RegisterRepository applicantRepository;

    public void saveJobForApplicant(long applicantId, long jobId) throws Exception {
    	 try {
             Applicant applicant = applicantRepository.findById(applicantId);
             Job job = jobRepository.findById(jobId).orElse(null);

             if (applicant == null || job == null) {
                 throw new CustomException("Applicant or Job not found",HttpStatus.INTERNAL_SERVER_ERROR);
             }

             if (!savedJobRepository.existsByApplicantAndJob(applicant, job)) {
            	 SavedJob savedJob = new SavedJob();
                 savedJob.setApplicant(applicant);                 
                 savedJob.setSaveJobStatus("saved");
                 jobRepository.save(job);
                 savedJob.setJob(job);
                 savedJobRepository.save(savedJob);
             } else {
                 throw new CustomException("Job has already been saved by the applicant",HttpStatus.INTERNAL_SERVER_ERROR);
             }
         }  catch (Exception e) {
             throw new CustomException("Error saving job for the applicant", HttpStatus.INTERNAL_SERVER_ERROR);
         }
    }

public List<Job> getSavedJobsForApplicant(long applicantId) {
	List<Job> result = new ArrayList<>();      
     
      try {
          List<SavedJob> savedJobs = savedJobRepository.findByApplicantId(applicantId);

          for (SavedJob savedJob : savedJobs) {
              result.add(savedJob.getJob());
          }

      } catch (Exception e) {
    	  e.printStackTrace();
      }

      return result;
  }

public long countSavedJobsForApplicant(long applicantId) {
    try {
        // Check if the applicant exists
        if (!applicantRepository.existsById(applicantId)) {
            // Throw CustomException with a specific error message and 404 status
            throw new CustomException("Applicant not found", HttpStatus.NOT_FOUND);
        }

        // Use the custom query to count saved jobs
        return savedJobRepository.countByApplicantId(applicantId);
    } catch (CustomException e) {
        throw e; // Re-throw CustomException as is
    } catch (Exception e) {
        // Handle other exceptions as needed
        throw new CustomException("Error while counting saved jobs for the applicant", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
}

