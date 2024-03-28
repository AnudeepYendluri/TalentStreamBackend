package com.talentstream.service;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.talentstream.exception.CustomException;
import com.talentstream.dto.ApplicantProfileDTO;
import com.talentstream.dto.ApplicantProfileViewDTO;
import com.talentstream.entity.Applicant;
import com.talentstream.entity.ApplicantProfile;
import com.talentstream.repository.ApplicantProfileRepository;
import com.talentstream.repository.RegisterRepository;
import javax.persistence.EntityNotFoundException;

@Service
public class ApplicantProfileService {
	 private final ApplicantProfileRepository applicantProfileRepository;
	 	 private final RegisterRepository applicantService;
	
	 	   
	    @Autowired
	    public ApplicantProfileService(ApplicantProfileRepository applicantProfileRepository,RegisterRepository applicantService) {
	        this.applicantProfileRepository = applicantProfileRepository;
	        this.applicantService=applicantService;
	      
	    }
	   
	  
	    public String createOrUpdateApplicantProfile(long applicantId, ApplicantProfileDTO applicantProfileDTO) throws IOException {
	    	Applicant applicant = applicantService.getApplicantById(applicantId);
	    	if(applicant==null)	    	
	    	  	throw new CustomException("Applicant not found for ID: " + applicantId, HttpStatus.NOT_FOUND);
	    	else
	    	{
	    		ApplicantProfile existingProfile = applicantProfileRepository.findByApplicantId(applicantId);
 	        if (existingProfile == null) {	        
 	        	 	ApplicantProfile applicantProfile = convertDTOToEntity(applicantProfileDTO);
	        	applicantProfile.setApplicant(applicant);	        
	            applicantProfileRepository.save(applicantProfile);
	            return "profile saved sucessfully";
	        } else {
	        	throw new CustomException("Profile for this applicant already exists", HttpStatus.BAD_REQUEST);
	        }
	    	}
	    }
	    
	    public ApplicantProfileViewDTO getApplicantProfileViewDTO(long applicantId) {
	        Applicant applicant = applicantService.findById(applicantId);
	        ApplicantProfile applicantProfile=null;
	                if(applicant==null)
	                	throw new EntityNotFoundException("Applicant not found with id: " + applicantId);
 
	                try
	                {
	        applicantProfile = applicantProfileRepository.findByApplicantId(applicantId);
	                }
	                catch(Exception e)
	                {
	                	 return convertToDTO(applicant, applicantProfile);
	                }
//	        		 if(applicantProfile==null)
//		                	throw  new EntityNotFoundException("ApplicantProfile not found for applicant with id: " + applicantId);
 
	        return convertToDTO(applicant, applicantProfile);
	    }
	    private ApplicantProfileViewDTO convertToDTO(Applicant applicant, ApplicantProfile applicantProfile) {
	        ApplicantProfileViewDTO dto = new ApplicantProfileViewDTO();
	        if(applicantProfile==null)
	        {
	        dto.setApplicant(applicant);
	        }
	        else
	        {
	        dto.setApplicant(applicant);
	        dto.setBasicDetails(applicantProfile.getBasicDetails());
	        dto.setxClassDetails(applicantProfile.getxClassDetails());
	        dto.setIntermediateDetails(applicantProfile.getIntermediateDetails());
	        dto.setGraduationDetails(applicantProfile.getGraduationDetails());
	        dto.setSkillsRequired(applicantProfile.getSkillsRequired());
	        dto.setExperienceDetails(applicantProfile.getExperienceDetails());
	        dto.setExperience(applicantProfile.getExperience());
	        dto.setQualification(applicantProfile.getQualification());
	        dto.setSpecialization(applicantProfile.getSpecialization());
	        dto.setPreferredJobLocations(applicantProfile.getPreferredJobLocations());
	        }
	        return dto;
	    }
	    


	    public ApplicantProfileDTO getApplicantProfileById(long applicantId) {
	    	try {
	            ApplicantProfile applicantProfile = applicantProfileRepository.findByApplicantId(applicantId);

	            if (applicantProfile != null) {
	                return convertEntityToDTO(applicantProfile);
	            } else {
	            	
	                throw new CustomException("Please Fill your  Profile" , HttpStatus.NOT_FOUND);
	            }
	        } catch (CustomException e) {	        	
	        	if (HttpStatus.NOT_FOUND.equals(e.getStatus())) {
	                throw e;
	            } else {
	               
	                throw new CustomException("Failed to get profile for applicant ID: " + applicantId, HttpStatus.INTERNAL_SERVER_ERROR);
	            }
	        }
	    }
	    
	  private ApplicantProfile convertDTOToEntity(ApplicantProfileDTO applicantProfileDTO) {
	        ApplicantProfile applicantProfile = new ApplicantProfile();
	        applicantProfile.setBasicDetails(applicantProfileDTO.getBasicDetails());	       
	        applicantProfile.setSkillsRequired(applicantProfileDTO.getSkillsRequired());
	        applicantProfile.setGraduationDetails(applicantProfileDTO.getGraduationDetails());
	        applicantProfile.setIntermediateDetails(applicantProfileDTO.getIntermediateDetails());
	        applicantProfile.setxClassDetails(applicantProfileDTO.getxClassDetails());
	        applicantProfile.setExperienceDetails(applicantProfileDTO.getExperienceDetails());
	        applicantProfile.setRoles(applicantProfileDTO.getRoles());
	        applicantProfile.setExperience(applicantProfileDTO.getExperience());
	        applicantProfile.setQualification(applicantProfileDTO.getQualification());
	        applicantProfile.setSpecialization(applicantProfileDTO.getSpecialization());
	        applicantProfile.setPreferredJobLocations(applicantProfileDTO.getPreferredJobLocations());
	       	        if (applicantProfileDTO.getRoles() == null) {
	            applicantProfile.setRoles("ROLE_JOBAPPLICANT");
	        } else {
	            applicantProfile.setRoles(applicantProfileDTO.getRoles());
	        }
	     
	        return applicantProfile;
		    
	          }
	     	        
	    public static ApplicantProfileDTO convertEntityToDTO(ApplicantProfile applicantProfile) {
	        if (applicantProfile == null) {
	            System.out.println("not exist");
	            return null;
	        }
	        ApplicantProfileDTO applicantProfileDTO = new ApplicantProfileDTO();
	        applicantProfileDTO.setBasicDetails(applicantProfile.getBasicDetails());
	        applicantProfileDTO.setGraduationDetails(applicantProfile.getGraduationDetails());
	        applicantProfileDTO.setIntermediateDetails(applicantProfile.getIntermediateDetails());
	        applicantProfileDTO.setxClassDetails(applicantProfile.getxClassDetails());
	        applicantProfileDTO.setSkillsRequired(applicantProfile.getSkillsRequired());
	        applicantProfileDTO.setExperienceDetails(applicantProfile.getExperienceDetails());
	        applicantProfileDTO.setRoles(applicantProfile.getRoles());
	        	        return applicantProfileDTO;
	    }
	       	        
	    public String updateApplicantProfile(long applicantId, ApplicantProfileViewDTO updatedProfileDTO) {
	    	 	Applicant applicant = applicantService.getApplicantById(applicantId);
	    	if(applicant==null)	    	
	    	  	throw new CustomException("Applicant not found " + applicantId, HttpStatus.NOT_FOUND);
	    	else
	    	{
	    		 applicant.setName(updatedProfileDTO.getApplicant().getName());
	             applicant.setMobilenumber(updatedProfileDTO.getApplicant().getMobilenumber());
	             applicantService.save(applicant);
	    	}
	    		ApplicantProfile existingProfile = applicantProfileRepository.findByApplicantId(applicantId);
	    		if (existingProfile == null) {
	                throw new CustomException("Your  profile not found and please fill profile " + applicantId, HttpStatus.NOT_FOUND);
	            }
	    		else
	    		{
	    			 existingProfile.setBasicDetails(updatedProfileDTO.getBasicDetails());
	    			 existingProfile.setExperienceDetails(updatedProfileDTO.getExperienceDetails());
	    			 existingProfile.setGraduationDetails(updatedProfileDTO.getGraduationDetails());
	    			 existingProfile.setIntermediateDetails(updatedProfileDTO.getIntermediateDetails());
	    			 existingProfile.setSkillsRequired(updatedProfileDTO.getSkillsRequired());
				 existingProfile.setQualification(updatedProfileDTO.getQualification());
	    			 existingProfile.setSpecialization(updatedProfileDTO.getSpecialization());
	    			 existingProfile.setxClassDetails(updatedProfileDTO.getxClassDetails());
	    			 	applicantProfileRepository.save(existingProfile);	
	    		}
	            return "profile saved sucessfully";
	    		}
	
	    public void deleteApplicantProfile(long applicantId) {
	    	try {
	            applicantProfileRepository.deleteById((int) applicantId);
	        } catch (Exception e) {
	            throw new CustomException("Failed to delete profile for applicant ID: " + applicantId, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	public int getApplicantProfileById1(int applicantId) {

			ApplicantProfile applicantProfile = applicantProfileRepository.findByApplicantId(applicantId);

	        return applicantProfile != null ? applicantProfile.getProfileid() : 0;

		}
	      
}

	 
