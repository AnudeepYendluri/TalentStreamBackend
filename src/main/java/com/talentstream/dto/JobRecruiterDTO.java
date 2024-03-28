package com.talentstream.dto;

import java.util.List;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class JobRecruiterDTO {
//	private Long recruiterId;
 	@NotBlank(message = "Company Name required.")
    private String companyname;
 	@NotBlank(message = "Mobile Number required.")
 	@Pattern(regexp = "^$|^[6789]\\d{9}$", message = "Mobile number should begin with 6, 7, 8, or 9 and be 10 digits long.")
    private String mobilenumber;
 	@NotBlank(message = "Email required.")
 	@Pattern(
            regexp = "^$|^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$",
            message = "Invalid email format and white spaces are not allowed."
        )
    private String email;
 	@NotBlank(message = "Password required.")
 	@Pattern(
 		    regexp = "^$|^(?=.{6,}$)(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*(),.?\":{}|<>])(?!.*\\s).*$",
 		    message = "Password must be at least 6 characters long and contain at least one uppercase letter, one digit, one special character, and no white spaces."
 		)
    private String password;
   // private List<JobDTO> jobs;
    private String roles = "ROLE_JOBRECRUITER";
   // private List<TeamMemberDTO> teamMembers;

    public JobRecruiterDTO() {
    }

    public JobRecruiterDTO(String companyname, String mobilenumber, String email, String password/*,List<JobDTO> jobs, String roles, List<TeamMemberDTO> teamMembers*/) {
        
        this.companyname = companyname;
        this.mobilenumber = mobilenumber;
        this.email = email;
        this.password = password;
       // this.jobs = jobs;
        this.roles = roles;
       // this.teamMembers = teamMembers;
    }

//	public Long getRecruiterId() {
//		return recruiterId;
//	}
//
//	public void setRecruiterId(Long recruiterId) {
//		this.recruiterId = recruiterId;
//	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getMobilenumber() {
		return mobilenumber;
	}

	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

}