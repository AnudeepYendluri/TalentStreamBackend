package com.talentstream.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.talentstream.entity.Applicant;
import com.talentstream.entity.ApplicantImage;
import com.talentstream.exception.CustomException;
import com.talentstream.repository.ApplicantImageRepository;
import com.talentstream.repository.RegisterRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.core.io.InputStreamResource;
@Service
public class ApplicantImageService {
 
    private final Path root = Paths.get("applicantprofileimages");
 
    @Autowired
    private ApplicantImageRepository applicantImageRepository;
 
    @Autowired
    private RegisterRepository applicantService;
 
    public ApplicantImageService() throws IOException {
 
    }
 
    public String uploadImage(long applicantId, MultipartFile imageFile) {
//
//        Applicant applicant = applicantService.getApplicantById(applicantId);
//        if (applicant == null)
//            throw new CustomException("Applicant not found for ID: " + applicantId, HttpStatus.NOT_FOUND);
//        else {
//            if (applicantImageRepository.existsByApplicant(applicant)) {
//                throw new CustomException("An image already exists for the applicant.", HttpStatus.BAD_REQUEST);
//            }
//
//            if (imageFile.getSize() > 1 * 1024 * 1024) {
//                throw new CustomException("File size should be less than 1MB.", HttpStatus.BAD_REQUEST);
//            }
//            String contentType = imageFile.getContentType();
//            if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
//                throw new CustomException("Only JPG and PNG file types are allowed.", HttpStatus.BAD_REQUEST);
//            }
//
//            String name = StringUtils.cleanPath(imageFile.getOriginalFilename());
//            String fileName = applicantId + "_" + name;
//
//            String folderPath = "src/main/resources/applicant/photos";
//            String filePath = Paths.get(folderPath, fileName).toString();
//
//            try {
//                Files.createDirectories(Paths.get(folderPath)); // Create directories if they don't exist
//                Files.copy(imageFile.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            ApplicantImage applicantImage = new ApplicantImage();
//            applicantImage.setImagename(fileName);
//            applicantImage.setApplicant(applicant);
//            applicantImageRepository.save(applicantImage);
//
//            return name;
//        }
    	if (imageFile.getSize() > 1 * 1024 * 1024) {
            throw new CustomException("File size should be less than 1MB.", HttpStatus.BAD_REQUEST);
        }
        String contentType = imageFile.getContentType();
        if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
            throw new CustomException("Only JPG and PNG file types are allowed.", HttpStatus.BAD_REQUEST);
        }
        
        
    	Applicant applicant = applicantService.getApplicantById(applicantId);
        if (applicant == null)
            throw new CustomException("Applicant not found for ID: " + applicantId, HttpStatus.NOT_FOUND);
        else {
         
            ApplicantImage existingImage = applicantImageRepository.findByApplicant(applicant);
            if (existingImage != null) {             
                String folderPath = "src/main/resources/applicant/photos";
                String existingFileName = existingImage.getImagename();
                String existingFilePath = Paths.get(folderPath, existingFileName).toString();
 
                try {
                    Files.deleteIfExists(Paths.get(existingFilePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
               
                String name = StringUtils.cleanPath(imageFile.getOriginalFilename());
                String newFileName = applicantId + "_" + name;
                String filePath = Paths.get(folderPath, newFileName).toString();
                try {
                    Files.copy(imageFile.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
 
                existingImage.setImagename(newFileName);
                applicantImageRepository.save(existingImage);
 
                return name;
            } else {
            	String name = StringUtils.cleanPath(imageFile.getOriginalFilename());
                String fileName = applicantId + "_" + name;
 
                String folderPath = "src/main/resources/applicant/photos";
                String filePath = Paths.get(folderPath, fileName).toString();
 
                try {
                    Files.createDirectories(Paths.get(folderPath)); // Create directories if they don't exist
                    Files.copy(imageFile.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ApplicantImage applicantImage = new ApplicantImage();
                applicantImage.setImagename(fileName);
                applicantImage.setApplicant(applicant);
                applicantImageRepository.save(applicantImage);
                return name;
            }
        }
    }
 
	public ResponseEntity<Resource> getProfilePicByApplicantId(long applicantId) {
		try
		{
		ApplicantImage applicantImage = applicantImageRepository.findByApplicantId(applicantId);
        if (applicantImage != null) {
            String fileName = applicantImage.getImagename();
            Path filePath = Paths.get("src/main/resources/applicant/photos", fileName);
            Resource resource = new FileSystemResource(filePath.toFile());
 
			MediaType mediaType;
			if (fileName.toLowerCase().endsWith(".png")) {
			    mediaType = MediaType.IMAGE_PNG;
			} else if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
			    mediaType = MediaType.IMAGE_JPEG;
			} else {
			    throw new RuntimeException("Unsupported image file format for applicant ID: " + applicantId);
			}
 
			return ResponseEntity.ok()
			        .contentType(mediaType)
			        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
			        .body(resource);
        } else {
            String errorMessage = "Please upload your profile image  " ;
            InputStreamResource errorResource = new InputStreamResource(new ByteArrayInputStream(errorMessage.getBytes()));
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(errorResource);
        }
    } catch (Exception e) {
        String errorMessage = "Internal Server Error";
        InputStreamResource errorResource = new InputStreamResource(new ByteArrayInputStream(errorMessage.getBytes()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.TEXT_PLAIN)
                .body(errorResource);
    }
}
 
}
    