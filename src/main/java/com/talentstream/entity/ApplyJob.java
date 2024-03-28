
package com.talentstream.entity;

//import java.util.Date;
//import java.util.List;
// 
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
// 
//import javax.persistence.CascadeType;
//import javax.persistence.Column;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToMany;
//import javax.persistence.PrePersist;
// 
//@Entity
//@Table(name="Applyjob")
//public class ApplyJob {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long applyjobid;
// 
//	@ManyToOne
//    @JoinColumn(name = "applicantId")
//    private Applicant applicant;
// 
//    @ManyToOne
//    @JoinColumn(name = "job_id")
//    private Job job;
//    @Column(nullable = false)
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date applicationDate;
//    
//    @OneToMany(mappedBy = "applyJob", cascade = CascadeType.ALL)
//	@JsonManagedReference
//	@JsonIgnore
//	private List<ApplicantStatusHistory> statusHistory;
// 
//    @Column(nullable = false)
//    private String applicantStatus = "New";
//    @OneToMany(mappedBy = "applyJob", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private List<ScheduleInterview> scheduleInterviews;
//
//
//	public List<ScheduleInterview> getScheduleInterviews() {
//		return scheduleInterviews;
//	}
//	public void setScheduleInterviews(List<ScheduleInterview> scheduleInterviews) {
//		this.scheduleInterviews = scheduleInterviews;
//	}
// 
//	public Long getApplyjobid() {
//		return applyjobid;
//	}
// 
//	public void setApplyjobid(Long applyjobid) {
//		this.applyjobid = applyjobid;
//	}
// 
//public void setApplicationDate(Date applicationDate) {
//		this.applicationDate = applicationDate;
//	}
//	public Date getApplicationDate() {
//		return applicationDate;
//	}
// 
//@PrePersist
//	private void setApplicationDate() {
//	    applicationDate = new Date();
// 
//	}
// 
//	public Applicant getApplicant() {
//		return applicant;
//	}
// 
//	public void setApplicant(Applicant applicant) {
//		this.applicant = applicant;
//	}
// 
//	public Job getJob() {
//		return job;
//	}
// 
//	public void setJob(Job job) {
//		this.job = job;
//	}
//	public String getApplicantStatus() {
//        return applicantStatus;
//    }
//
//	public void setApplicantStatus(String applicantStatus) {
//        this.applicantStatus = applicantStatus;
//    }
//
// 
//}

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
@Entity
@Table(name="Applyjob")
public class ApplyJob {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long applyjobid;
	@ManyToOne
    @JoinColumn(name = "applicantId")
    private Applicant applicant;
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date applicationDate;
    @Column(nullable = false)
    private String applicantStatus = "New";
    @OneToMany(mappedBy = "applyJob", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ScheduleInterview> scheduleInterviews;
  
	public List<ScheduleInterview> getScheduleInterviews() {
		return scheduleInterviews;
	}
	public void setScheduleInterviews(List<ScheduleInterview> scheduleInterviews) {
		this.scheduleInterviews = scheduleInterviews;
	}
	public Long getApplyjobid() {
		return applyjobid;
	}
	public void setApplyjobid(Long applyjobid) {
		this.applyjobid = applyjobid;
	}
   public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}
	public Date getApplicationDate() {
		return applicationDate;
	}
@PrePersist
	private void setApplicationDate() {
	    applicationDate = new Date();
	}
	public Applicant getApplicant() {
		return applicant;
	}
	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	public String getApplicantStatus() {
        return applicantStatus;
    }
 
	public void setApplicantStatus(String applicantStatus) {
        this.applicantStatus = applicantStatus;
    }
}