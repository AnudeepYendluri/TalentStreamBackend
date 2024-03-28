package com.talentstream.entity;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class AppliedApplicantInfoDTO {
	private Long applyjobid;
	private Long id;
	  private String name;
	    private String email;
	    private String mobilenumber;
	    private String jobTitle;
	    private String applicantStatus;
	    private int minimumExperience;
	    private List<String> skillName;
	    private String minimumQualification;
	    private String location;
	    private String newStatus;
	    
	    

 
	    
		public Map<String, Integer> getSkills() {
			return skills;
		}
		public void setSkills(Map<String, Integer> skills) {
			this.skills = skills;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public AppliedApplicantInfoDTO() {}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getMobilenumber() {
			return mobilenumber;
		}
		public void setMobilenumber(String mobilenumber) {
			this.mobilenumber = mobilenumber;
		}
		public String getJobTitle() {
			return jobTitle;
		}
		public void setJobTitle(String jobTitle) {
			this.jobTitle = jobTitle;
		}
		public String getApplicantStatus() {
			return applicantStatus;
		}
		public void setApplicantStatus(String applicantStatus) {
			this.applicantStatus = applicantStatus;
		}
		public int getMinimumExperience() {
			return minimumExperience;
		}
		public void setMinimumExperience(int minimumExperience) {
			this.minimumExperience = minimumExperience;
		}
		public List<String> getSkillName() {
			return skillName;
		}
		public void setSkillName(List<String> skills2) {
			this.skillName = skills2;
		}
		public String getMinimumQualification() {
			return minimumQualification;
		}
		public void setMinimumQualification(String minimumQualification) {
			this.minimumQualification = minimumQualification;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public Long getApplyjobid() {
			return applyjobid;
		}
		public void setApplyjobid(Long applyjobid) {
			this.applyjobid = applyjobid;
		}
		private Map<String, Integer> skills = new HashMap<>();
	    public void addSkill(String skillName, int minimumExperience) {
	        skills.put(skillName, minimumExperience);
	    }
	    public String getNewStatus() {
			return newStatus;
		}
		public void setNewStatus(String newStatus) {
			this.newStatus = newStatus;
		}
}	
