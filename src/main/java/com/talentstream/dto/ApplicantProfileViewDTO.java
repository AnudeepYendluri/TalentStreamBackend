package com.talentstream.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.talentstream.entity.Applicant;
import com.talentstream.entity.ApplicantSkills;
import com.talentstream.entity.BasicDetails;
import com.talentstream.entity.ExperienceDetails;
import com.talentstream.entity.GraduationDetails;
import com.talentstream.entity.IntermediateDetails;
import com.talentstream.entity.XClassDetails;

public class ApplicantProfileViewDTO {
	private Applicant applicant;
	private BasicDetails basicDetails;
    private XClassDetails xClassDetails;
    private IntermediateDetails intermediateDetails;
    private GraduationDetails graduationDetails;
    private Set<ApplicantSkills> skillsRequired;
    private List<ExperienceDetails> experienceDetails;
    private String experience;
    private String qualification;
    private String specialization;
    private Set<String> preferredJobLocations = new HashSet<>();
	
	public Applicant getApplicant() {
		return applicant;
	}
	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}
	public BasicDetails getBasicDetails() {
		return basicDetails;
	}
	public void setBasicDetails(BasicDetails basicDetails) {
		this.basicDetails = basicDetails;
	}
	public XClassDetails getxClassDetails() {
		return xClassDetails;
	}
	public void setxClassDetails(XClassDetails xClassDetails) {
		this.xClassDetails = xClassDetails;
	}
	public IntermediateDetails getIntermediateDetails() {
		return intermediateDetails;
	}
	public void setIntermediateDetails(IntermediateDetails intermediateDetails) {
		this.intermediateDetails = intermediateDetails;
	}
	public GraduationDetails getGraduationDetails() {
		return graduationDetails;
	}
	public void setGraduationDetails(GraduationDetails graduationDetails) {
		this.graduationDetails = graduationDetails;
	}
	public Set<ApplicantSkills> getSkillsRequired() {
		return skillsRequired;
	}
	public void setSkillsRequired(Set<ApplicantSkills> skillsRequired) {
		this.skillsRequired = skillsRequired;
	}
	public List<ExperienceDetails> getExperienceDetails() {
		return experienceDetails;
	}
	public void setExperienceDetails(List<ExperienceDetails> experienceDetails) {
		this.experienceDetails = experienceDetails;
	}
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public String getSpecialization() {
		return specialization;
	}
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	public Set<String> getPreferredJobLocations() {
		return preferredJobLocations;
	}
	public void setPreferredJobLocations(Set<String> preferredJobLocations) {
		this.preferredJobLocations = preferredJobLocations;
	}
    
}
