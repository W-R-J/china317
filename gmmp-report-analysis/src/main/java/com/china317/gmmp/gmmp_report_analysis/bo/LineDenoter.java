package com.china317.gmmp.gmmp_report_analysis.bo;

import java.io.Serializable;

/**
 * 线路牌信息
 * @author Administrator
 */
public class LineDenoter implements Serializable {

	/**
	 * 线路牌
	 */
	private static final long serialVersionUID = -8007534937508484943L;

	private String licenseCard;
	private String barCode;
	private String denoter;
	private String color;
	private String auditNum;
	private String startDate;
	private String endDate;
	private String renter;
	private String haulier;
	private String capacity;
	private String labelNumber;
	private String destination;
	private String waytTo;
	private String labelStatus;
	private String auditDate; 

	public String getLicenseCard() {
		return licenseCard;
	}

	public void setLicenseCard(String licenseCard) {
		this.licenseCard = licenseCard;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getDenoter() {
		return denoter;
	}

	public void setDenoter(String denoter) {
		this.denoter = denoter;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getAuditNum() {
		return auditNum;
	}

	public void setAuditNum(String auditNum) {
		this.auditNum = auditNum;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getRenter() {
		return renter;
	}

	public void setRenter(String renter) {
		this.renter = renter;
	}

	public String getHaulier() {
		return haulier;
	}

	public void setHaulier(String haulier) {
		this.haulier = haulier;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getLabelNumber() {
		return labelNumber;
	}

	public void setLabelNumber(String labelNumber) {
		this.labelNumber = labelNumber;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getWaytTo() {
		return waytTo;
	}

	public void setWaytTo(String waytTo) {
		this.waytTo = waytTo;
	}

	public String getLabelStatus() {
		return labelStatus;
	}

	public void setLabelStatus(String labelStatus) {
		this.labelStatus = labelStatus;
	}

	public String getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}

}
