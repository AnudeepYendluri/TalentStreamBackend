package com.talentstream.entity;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity 
public class Applicant {
 
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String email;
    @Column(name = "mobile")
    private String mobilenumber;
    private String password; 
    @OneToMany(mappedBy="applicant")
    @JsonIgnore
    private Set<ApplyJob> appliedJobs = new HashSet<>();
    @OneToMany(mappedBy="applicant")
    @JsonIgnore
    private Set<SavedJob> savedJobs = new HashSet<>();
    
    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Alerts> alerts=new HashSet<>();
    
    public Applicant() {
        this.appicantStatus = "Active";
    }
    public Set<Alerts> getAlerts() {
		return alerts;
	}
    @Column(nullable = false)
    private String roles="ROLE_JOBAPPLICANT";
    
    @Column(columnDefinition = "int default 0")
    private int alertCount;

private String appicantStatus="Active";
    
    public String getAppicantStatus() {
		return appicantStatus;
	}
 
	public void setAppicantStatus(String appicantStatus) {
		this.appicantStatus = appicantStatus;
 
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<ApplyJob> getAppliedJobs() {
		return appliedJobs;
	}

	public void setAppliedJobs(Set<ApplyJob> appliedJobs) {
		this.appliedJobs = appliedJobs;
	}

	public Set<SavedJob> getSavedJobs() {
		return savedJobs;
	}

	public void setSavedJobs(Set<SavedJob> savedJobs) {
		this.savedJobs = savedJobs;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public int getAlertCount() {
		return alertCount;
	}

	public void setAlertCount(int alertCount) {
		this.alertCount = alertCount;
	}

	public void setAlerts(Set<Alerts> alerts) {
		this.alerts = alerts;
	} 	
}
