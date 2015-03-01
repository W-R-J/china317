package com.china317.gmmp.gmmp_report_analysis.bo;

public class PtmOverSpeed {

	private String code;
	private String license;
	private String licenseColor;
	private String beginTime;
	private String endTIme;
	private double avgSpeed = 0L;
	private double maxSpeed = 0L;
	private int flag = 0;//1为外环外 ，2为外环内.
	private int businessType;
	
	
	public int getBusinessType() {
		return businessType;
	}
	public void setBusinessType(int businessType) {
		this.businessType = businessType;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public void addSpeed(double speed){
		if(speed > this.maxSpeed){
			this.maxSpeed = speed;
		}
		if(this.avgSpeed == 0L){
			this.avgSpeed = speed;
		}else{
			this.avgSpeed = (this.avgSpeed+speed)/2;
		}
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getLicenseColor() {
		return licenseColor;
	}
	public void setLicenseColor(String licenseColor) {
		this.licenseColor = licenseColor;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTIme() {
		return endTIme;
	}
	public void setEndTIme(String endTIme) {
		this.endTIme = endTIme;
	}
	public double getAvgSpeed() {
		return avgSpeed;
	}
	public void setAvgSpeed(double avgSpeed) {
		this.avgSpeed = avgSpeed;
	}
	public double getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	
}
