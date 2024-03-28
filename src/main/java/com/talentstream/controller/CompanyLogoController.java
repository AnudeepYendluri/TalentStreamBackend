package com.talentstream.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.talentstream.exception.CustomException;
import com.talentstream.exception.UnsupportedFileTypeException;
import com.talentstream.service.CompanyLogoService;

@RestController
@RequestMapping("/recruiters")
public class CompanyLogoController {
	   @Autowired
	    private CompanyLogoService companyLogoService;
		
	//	@Value("${project.photoimage}")
	//	private String path;
		
	    @PostMapping("/companylogo/upload/{jobRecruiterId}")
	    public String fileUpload(@PathVariable Long jobRecruiterId,@RequestParam  MultipartFile logoFile)
	    {
	    	try {
	            String filename = companyLogoService.saveCompanyLogo(jobRecruiterId,logoFile);
	            return filename+ " Image uploaded successfully";
	        } 
	    	catch (CustomException ce) {
	            return  ce.getMessage();
	    	}
	    	catch (UnsupportedFileTypeException e) {
    	        return "Only JPG and PNG file types are allowed.";
    	    } 
	    	catch (MaxUploadSizeExceededException e) {
    	        return "File size should be less than 1MB.";
    	    }
	    	catch (IOException e) {
	            e.printStackTrace();
	            return "Image not uploaded successfully";
	        }
	    }
	 
	    @GetMapping("/companylogo/download/{jobRecruiterId}")
	    public ResponseEntity<byte[]> getCompanyLogo(@PathVariable Long jobRecruiterId) {
	        try {
	            byte[] imageBytes = companyLogoService.getCompanyLogo(jobRecruiterId);

	            return ResponseEntity.ok()
	                    .contentType(MediaType.IMAGE_JPEG)
	                    .body(imageBytes);

	        } catch (CustomException ce) {
	            // Handle exception appropriately, e.g., return a 404 Not Found response
	            return ResponseEntity.status(ce.getStatus()).body(null);
	        }
	    }
	}