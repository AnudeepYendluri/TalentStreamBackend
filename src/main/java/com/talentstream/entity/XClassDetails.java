package com.talentstream.entity;
import javax.persistence.*;


@Embeddable
public class XClassDetails {
	  private String xschoolName;
	    private String xboard;
	    private String xpercentage;
	    private String xyearOfPassing;
	    private String xCity;
	    private String xState;
	    private String xPincode;
		public String getXschoolName() {
			return xschoolName;
		}
		public void setXschoolName(String xschoolName) {
			this.xschoolName = xschoolName;
		}
		public String getXboard() {
			return xboard;
		}
		public void setXboard(String xboard) {
			this.xboard = xboard;
		}
		public String getXpercentage() {
			return xpercentage;
		}
		public void setXpercentage(String xpercentage) {
			this.xpercentage = xpercentage;
		}
		public String getXyearOfPassing() {
			return xyearOfPassing;
		}
		public void setXyearOfPassing(String xyearOfPassing) {
			this.xyearOfPassing = xyearOfPassing;
		}
		public String getxCity() {
			return xCity;
		}
		public void setxCity(String xCity) {
			this.xCity = xCity;
		}
		public String getxState() {
			return xState;
		}
		public void setxState(String xState) {
			this.xState = xState;
		}
		public String getxPincode() {
			return xPincode;
		}
		public void setxPincode(String xPincode) {
			this.xPincode = xPincode;
		}
		

}
