package com.talentstream.service;

import java.io.File;

import java.io.IOException;

import java.nio.file.Files;

import java.nio.file.Path;

import java.nio.file.Paths;

import java.nio.file.StandardCopyOption;

import java.util.Arrays;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.io.Resource;

import org.springframework.core.io.UrlResource;

import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;

import org.springframework.web.multipart.MaxUploadSizeExceededException;

import org.springframework.web.multipart.MultipartFile;

import com.talentstream.entity.CompanyLogo;

import com.talentstream.entity.JobRecruiter;

import com.talentstream.exception.CustomException;
 
import com.talentstream.exception.UnsupportedFileTypeException;

import com.talentstream.repository.CompanyLogoRepository;

import com.talentstream.repository.JobRecruiterRepository;


@Service
public class CompanyLogoService {
	//private static final String LOGO_UPLOAD_DIR = "classpath:/companylogos/";
	//@Value("${project.photoimage}")
	//private String path;
	 private static final long MAX_FILE_SIZE_BYTES = 1024 * 1024; // 1 MB = 1024 * 1024 bytes
	    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png", "gif"};
 
        	@Autowired
    		private CompanyLogoRepository companyLogoRepository;
    		@Autowired
    		private  JobRecruiterRepository jobRecruiterRepository;
    	
    		  @Transactional
    		    public String saveCompanyLogo(long jobRecruiterId, MultipartFile logoFile) throws IOException {
    		        if (!isValidFormat(logoFile.getOriginalFilename())) {
    		            throw new CustomException("Image format not accepted. ", HttpStatus.BAD_REQUEST);
    		        }
                       // check, if the size of the logo file exceeds the maximum allowed size
    		        if (logoFile.getSize() > MAX_FILE_SIZE_BYTES) {
				// Throw a custom exception with the error message and HTTP status code (BAD_REQUEST)
    		            throw new CustomException("File size should be less than or equal to " + (MAX_FILE_SIZE_BYTES / 1024) + " KB. ", HttpStatus.BAD_REQUEST);
    		        }

    		        JobRecruiter recruiter = jobRecruiterRepository.findByRecruiterId(jobRecruiterId);
    		        if (recruiter == null) {
    		            throw new CustomException("Recruiter not found for ID: " + jobRecruiterId, HttpStatus.NOT_FOUND);
    		        } else {
    		            String name = StringUtils.cleanPath(logoFile.getOriginalFilename());
    		            String fileName = jobRecruiterId + ".jpg"; //fileUtility.getFileExtension(logoFile.getOriginalFilename());
    		            Files.createDirectories(Paths.get("src/main/resources/static/images/recruiter/companylogo"));
    		            String filePath = new File("src/main/resources/static/images/recruiter/companylogo").getAbsolutePath() + File.separator + fileName;
    		            Files.copy(logoFile.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

    		            CompanyLogo existingLogo = companyLogoRepository.findByJobRecruiterRecruiterId(jobRecruiterId);
		                if (existingLogo != null) {
		                    // Image record exists, update the existing record
		                    updateCompanyLogo(existingLogo, recruiter, fileName);
		                } else {
		                    // No existing image record, insert a new record
		                    insertCompanyLogo(recruiter, fileName);
		                }
		                return logoFile.getOriginalFilename();
    		    }
    		  }

    		/*    public String getFileExtension(String filename) {
    		        int dotIndex = filename.lastIndexOf('.');
    		        return (dotIndex == -1) ? "" : filename.substring(dotIndex);
    		    }  */

	       //validate the image format extension
    		    private boolean isValidFormat(String filename) {
    		        int dotIndex = filename.lastIndexOf('.');
    		        String ext = (dotIndex == -1) ? "" : filename.substring(dotIndex + 1).toLowerCase();
    		        return Arrays.asList(ALLOWED_EXTENSIONS).contains(ext);
    		    }

    		    private void updateCompanyLogo(CompanyLogo existingLogo, JobRecruiter recruiter, String fileName) {
    		        // Update the existing record
    		      //  System.out.println("Updating company logo");
    		        existingLogo.setLogoName(fileName);
    		        existingLogo.setJobRecruiter(recruiter);
    		        companyLogoRepository.save(existingLogo);
    		    }

    		    private void insertCompanyLogo(JobRecruiter recruiter, String fileName) {
    		        // Insert a new record
    		      //  System.out.println("Inserting new company logo");
    		        CompanyLogo companyLogo = new CompanyLogo();
    		        companyLogo.setLogoName(fileName);
    		        companyLogo.setJobRecruiter(recruiter);
    		        companyLogoRepository.save(companyLogo);
    		    }
    		    
    		    
    		    public byte[] getCompanyLogo(long jobRecruiterId) {
    		        CompanyLogo existingLogo = companyLogoRepository.findByJobRecruiterRecruiterId(jobRecruiterId);

    		        if (existingLogo == null) {
    		            throw new CustomException("Company logo not found for recruiter ID: " + jobRecruiterId, HttpStatus.NOT_FOUND);
    		        }

    		        String fileName = jobRecruiterId + ".jpg";
    		        String filePath = "src/main/resources/static/images/recruiter/companylogo/" + fileName; // Adjust the path accordingly

    		        try {
    		            Path path = Paths.get(filePath);
    		            return Files.readAllBytes(path);
    		        } catch (IOException e) {
    		            throw new CustomException("Error reading company logo file", HttpStatus.INTERNAL_SERVER_ERROR);
    		        }
    		    }
}
 