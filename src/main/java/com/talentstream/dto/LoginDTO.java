package com.talentstream.dto;

import lombok.Data;


public class LoginDTO {
    private String email;
    private String password;

	private String status = "active"; // New field with a default value of "active"
	    public String getStatus() {
	        return status;
	    }
 
	    public void setStatus(String status) {
	        this.status = status;
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
    
}
