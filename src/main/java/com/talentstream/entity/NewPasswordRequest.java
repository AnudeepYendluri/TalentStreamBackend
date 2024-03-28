package com.talentstream.entity;

public class NewPasswordRequest {

    public NewPasswordRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	private String password;
    private String confirmedPassword;

	
	public String getConfirmedPassword() {
		return confirmedPassword;
	}
	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public NewPasswordRequest(String password, String confirmedPassword) {
		super();
		this.password = password;
		this.confirmedPassword = confirmedPassword;
	}
	
     
    
}
