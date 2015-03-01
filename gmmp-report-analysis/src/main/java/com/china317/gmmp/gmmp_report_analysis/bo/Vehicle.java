package com.china317.gmmp.gmmp_report_analysis.bo;

import java.io.Serializable;
import java.util.Date;


public class Vehicle implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8664629211249111879L;
	private String code;
	private int businessType;
	private String license;
	private String licenseColor;
	private String nationality;
	private String type;
	private String transType;
	private String RTPN;
	private String RTPNCode;
	private String vehicleStyle;
	private String vehicleColor;
	private String driver;
	private String driverLicense;
	private String driverTel;
	private String status;
	private int tonnage;
	private int seats;
	private String businessScope;
	private String businessArea;
	private String banlineType;
	private String guards;
	private String guardsLicense;
	private String guardsTel;
	private String gargoName;
	private int gargoTonnage;
	private String transOrigin;
	private String transDes;
	private String transStart;
	private String transEnd;
	private String origin;
	private String destination;
	private String originStation;
	private String desStation;
	private String corpCode;
	private String lineCode;
	private Date updateTime;
	private int deleteFlag;
	
	private Date transadminDate;
	
	
	// add in 2014-03-05
	private String operAttrName = null;
	
	//add in 2014-12-30
	public String risk; 			//100
	public String grade;			//200
	public String changpai_type ;			//	�����ͺ� 		200
	public String transport_licence;		//	��·����֤		200
	public String licence_date ;			//	��֤����   		200
	public String licence_end_date ;		//	֤����ֹ����   	200
	public String total_seat ;				//	����λ��  		number ����10�� С��0
	public String driver_licence_date ;		//	��ʻ֤������	200
	public String check_year ;				//	�������                             200  
	public String check_state ;				//	����״̬            200
	
	public String getOperAttrName() {
		return operAttrName;
	}

	public void setOperAttrName(String operAttrName) {
		this.operAttrName = operAttrName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public int getBusinessType() {
		return businessType;
	}

	public void setBusinessType(int businessType) {
		this.businessType = businessType;
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

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getRTPN() {
		return RTPN;
	}

	public void setRTPN(String rTPN) {
		RTPN = rTPN;
	}

	public String getRTPNCode() {
		return RTPNCode;
	}

	public void setRTPNCode(String rTPNCode) {
		RTPNCode = rTPNCode;
	}

	public String getVehicleStyle() {
		return vehicleStyle;
	}

	public void setVehicleStyle(String vehicleStyle) {
		this.vehicleStyle = vehicleStyle;
	}

	public String getVehicleColor() {
		return vehicleColor;
	}

	public void setVehicleColor(String vehicleColor) {
		this.vehicleColor = vehicleColor;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getDriverLicense() {
		return driverLicense;
	}

	public void setDriverLicense(String driverLicense) {
		this.driverLicense = driverLicense;
	}

	public String getDriverTel() {
		return driverTel;
	}

	public void setDriverTel(String driverTel) {
		this.driverTel = driverTel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getTonnage() {
		return tonnage;
	}

	public void setTonnage(int tonnage) {
		this.tonnage = tonnage;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public String getBusinessScope() {
		return businessScope;
	}

	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}

	public String getBusinessArea() {
		return businessArea;
	}

	public void setBusinessArea(String businessArea) {
		this.businessArea = businessArea;
	}

	public String getBanlineType() {
		return banlineType;
	}

	public void setBanlineType(String banlineType) {
		this.banlineType = banlineType;
	}

	public String getGuards() {
		return guards;
	}

	public void setGuards(String guards) {
		this.guards = guards;
	}

	public String getGuardsLicense() {
		return guardsLicense;
	}

	public void setGuardsLicense(String guardsLicense) {
		this.guardsLicense = guardsLicense;
	}

	public String getGuardsTel() {
		return guardsTel;
	}

	public void setGuardsTel(String guardsTel) {
		this.guardsTel = guardsTel;
	}

	public String getGargoName() {
		return gargoName;
	}

	public void setGargoName(String gargoName) {
		this.gargoName = gargoName;
	}

	public int getGargoTonnage() {
		return gargoTonnage;
	}

	public void setGargoTonnage(int gargoTonnage) {
		this.gargoTonnage = gargoTonnage;
	}

	public String getTransOrigin() {
		return transOrigin;
	}

	public void setTransOrigin(String transOrigin) {
		this.transOrigin = transOrigin;
	}

	public String getTransDes() {
		return transDes;
	}

	public void setTransDes(String transDes) {
		this.transDes = transDes;
	}

	public String getTransStart() {
		return transStart;
	}

	public void setTransStart(String transStart) {
		this.transStart = transStart;
	}

	public String getTransEnd() {
		return transEnd;
	}

	public void setTransEnd(String transEnd) {
		this.transEnd = transEnd;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getOriginStation() {
		return originStation;
	}

	public void setOriginStation(String originStation) {
		this.originStation = originStation;
	}

	public String getDesStation() {
		return desStation;
	}

	public void setDesStation(String desStation) {
		this.desStation = desStation;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getLineCode() {
		return lineCode;
	}

	public void setLineCode(String lineCode) {
		this.lineCode = lineCode;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Date getTransadminDate() {
		return transadminDate;
	}

	public void setTransadminDate(Date transadminDate) {
		this.transadminDate = transadminDate;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getChangpai_type() {
		return changpai_type;
	}

	public void setChangpai_type(String changpai_type) {
		this.changpai_type = changpai_type;
	}

	public String getTransport_licence() {
		return transport_licence;
	}

	public void setTransport_licence(String transport_licence) {
		this.transport_licence = transport_licence;
	}

	public String getLicence_date() {
		return licence_date;
	}

	public void setLicence_date(String licence_date) {
		this.licence_date = licence_date;
	}

	public String getLicence_end_date() {
		return licence_end_date;
	}

	public void setLicence_end_date(String licence_end_date) {
		this.licence_end_date = licence_end_date;
	}

	public String getTotal_seat() {
		return total_seat;
	}

	public void setTotal_seat(String total_seat) {
		this.total_seat = total_seat;
	}

	public String getDriver_licence_date() {
		return driver_licence_date;
	}

	public void setDriver_licence_date(String driver_licence_date) {
		this.driver_licence_date = driver_licence_date;
	}

	public String getCheck_year() {
		return check_year;
	}

	public void setCheck_year(String check_year) {
		this.check_year = check_year;
	}

	public String getCheck_state() {
		return check_state;
	}

	public void setCheck_state(String check_state) {
		this.check_state = check_state;
	}
	
	
}
