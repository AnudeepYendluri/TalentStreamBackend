package com.talentstream.entity;
import jakarta.persistence.*;

@Embeddable
public class IntermediateDetails {
	 private String icollegeName;
	    private String iboard;
	    private String iprogram;
	    private String ipercentage;
	    private String iyearOfPassing;
	    private String iCity;
	    private String iState;
		public String getIcollegeName() {
			return icollegeName;
		}
		public void setIcollegeName(String icollegeName) {
			this.icollegeName = icollegeName;
		}
		public String getIboard() {
			return iboard;
		}
		public void setIboard(String iboard) {
			this.iboard = iboard;
		}
		public String getIprogram() {
			return iprogram;
		}
		public void setIprogram(String iprogram) {
			this.iprogram = iprogram;
		}
		public String getIpercentage() {
			return ipercentage;
		}
		public void setIpercentage(String ipercentage) {
			this.ipercentage = ipercentage;
		}
		public String getIyearOfPassing() {
			return iyearOfPassing;
		}
		public void setIyearOfPassing(String iyearOfPassing) {
			this.iyearOfPassing = iyearOfPassing;
		}
		public String getiCity() {
			return iCity;
		}
		public void setiCity(String iCity) {
			this.iCity = iCity;
		}
		public String getiState() {
			return iState;
		}
		public void setiState(String iState) {
			this.iState = iState;
		}
		
	    
}
