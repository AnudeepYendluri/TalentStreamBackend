package com.talentstream.entity;
import javax.persistence.Embeddable;
@Embeddable
public class GraduationDetails {
	private String gcollegeName;  
	 private String gboard;        
	 private String gprogram;      
	 private String gpercentage;   
	 private String gyearOfPassing;
	 private String gCity;        
	 private String gState;
	public String getGcollegeName() {
		return gcollegeName;
	}
	public void setGcollegeName(String gcollegeName) {
		this.gcollegeName = gcollegeName;
	}
	public String getGboard() {
		return gboard;
	}
	public void setGboard(String gboard) {
		this.gboard = gboard;
	}
	public String getGprogram() {
		return gprogram;
	}
	public void setGprogram(String gprogram) {
		this.gprogram = gprogram;
	}
	public String getGpercentage() {
		return gpercentage;
	}
	public void setGpercentage(String gpercentage) {
		this.gpercentage = gpercentage;
	}
	public String getGyearOfPassing() {
		return gyearOfPassing;
	}
	public void setGyearOfPassing(String gyearOfPassing) {
		this.gyearOfPassing = gyearOfPassing;
	}
	public String getgCity() {
		return gCity;
	}
	public void setgCity(String gCity) {
		this.gCity = gCity;
	}
	public String getgState() {
		return gState;
	}
	public void setgState(String gState) {
		this.gState = gState;
	}
	
	 

}
