package com.talentstream.entity;

public class PasswordRequest {
	public PasswordRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	private String newPassword;
    private String oldPassword;
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
}