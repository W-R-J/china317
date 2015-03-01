package com.china317.gmmp.gmmp_report_analysis.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.china317.gmmp.gmmp_report_analysis.cache.AreaCache;

/**
 * 轨迹对象
 * @author Administrator
 *
 */
public class VehicleLocate implements Serializable{
	private static final long serialVersionUID = 4860620639228382592L;
	private String code;
	private String license;
	private String licenseColor;
	private double lon;
	private double lat;
	private double defLon;
	private double defLat;
	private Date gpsTime;
	private double gpsSpeed;
	private double recordSpeed;
	private double mileage;
	private int direction;
	private int altitude;
	private int ACCState;
	private int positionState;
	private int businessState;
	private int oilState;
	private int circuitState;
	private int doorsState;
	private int emergencyAlarm;
	private int overspeedAlarm;
	private int fatigueAlarm;
	private int warning;
	private int GNSSModuleFault;
	private int GNSSAntennaMissed;
	private int GNSSAntennaShortCircuit;
	private int mainpowerUndervoltage;
	private int mainpowerDown;
	private int LCDFault;
	private int TTSModuleFault;
	private int cameraFault;
	private int driveTimeoutDay;
	private int stopTimeout;
	private int inOutArea;
	private int inOutLine;
	private int sectionDriveProblem;
	private int routeDeviate;
	private int VSSFault;
	private int oilFault;
	private int vehicleStolen;
	private int illegalIgnition;
	private int illegalMove;
	private int rolloverAlarm;
	private Date getTime;
	private Date sendTime;
	private String platCode;
	private String picUrl;
	private String provinceArea;
	private int isOffline;
	private int offlineTimes;
	private int onlineTime;
	
	private int businessType;
	private boolean locate;
	private boolean flame=false;
	private boolean overspeed=false;
	private int area;
	private String areaFlag;
	private boolean emergency=false;
	private boolean drowsy=false;
	private boolean nonstop = false;
	private Date flameoutTime;
	private Date driveBeginTime;
	//add in 20140403
	public int offType;		
	public boolean inarea;	
	private String zeroSpeedBegin;
	private long flameoutTimeLong;
	private long illegalParkingTime = 0;
	private long offTime;	
	
	private int flag;
	// add in 2014-10-20
	private Set<String> crossings;
	private String crossingName;
	//add in 2014-11-09
	public int type_parking;
	public boolean inarea_parking;
	public boolean illegal_parking;
	public double lonParkingBegin;
	public double latParkingBegin;
	public boolean flameOut;
	
	
	Set<RuleResultWrap> ruleRsWrapSet = new HashSet<RuleResultWrap>();
	
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
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getDefLon() {
		return defLon;
	}
	public void setDefLon(double defLon) {
		this.defLon = defLon;
	}
	public double getDefLat() {
		return defLat;
	}
	public void setDefLat(double defLat) {
		this.defLat = defLat;
	}
	public Date getGpsTime() {
		return gpsTime;
	}
	public void setGpsTime(Date gpsTime) {
		this.gpsTime = gpsTime;
	}
	public double getGpsSpeed() {
		return gpsSpeed;
	}
	public void setGpsSpeed(double gpsSpeed) {
		this.gpsSpeed = gpsSpeed;
	}
	public double getRecordSpeed() {
		return recordSpeed;
	}
	public void setRecordSpeed(double recordSpeed) {
		this.recordSpeed = recordSpeed;
	}
	public double getMileage() {
		return mileage;
	}
	public void setMileage(double mileage) {
		this.mileage = mileage;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public int getAltitude() {
		return altitude;
	}
	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}
	public int getACCState() {
		return ACCState;
	}
	public void setACCState(int aCCState) {
		ACCState = aCCState;
	}
	public int getPositionState() {
		return positionState;
	}
	public void setPositionState(int positionState) {
		this.positionState = positionState;
	}
	public int getBusinessState() {
		return businessState;
	}
	public void setBusinessState(int businessState) {
		this.businessState = businessState;
	}
	public int getOilState() {
		return oilState;
	}
	public void setOilState(int oilState) {
		this.oilState = oilState;
	}
	public int getCircuitState() {
		return circuitState;
	}
	public void setCircuitState(int circuitState) {
		this.circuitState = circuitState;
	}
	public int getDoorsState() {
		return doorsState;
	}
	public void setDoorsState(int doorsState) {
		this.doorsState = doorsState;
	}
	public int getEmergencyAlarm() {
		return emergencyAlarm;
	}
	public void setEmergencyAlarm(int emergencyAlarm) {
		this.emergencyAlarm = emergencyAlarm;
	}
	public int getOverspeedAlarm() {
		return overspeedAlarm;
	}
	public void setOverspeedAlarm(int overspeedAlarm) {
		this.overspeedAlarm = overspeedAlarm;
	}
	public int getFatigueAlarm() {
		return fatigueAlarm;
	}
	public void setFatigueAlarm(int fatigueAlarm) {
		this.fatigueAlarm = fatigueAlarm;
	}
	public int getWarning() {
		return warning;
	}
	public void setWarning(int warning) {
		this.warning = warning;
	}
	public int getGNSSModuleFault() {
		return GNSSModuleFault;
	}
	public void setGNSSModuleFault(int gNSSModuleFault) {
		GNSSModuleFault = gNSSModuleFault;
	}
	public int getGNSSAntennaMissed() {
		return GNSSAntennaMissed;
	}
	public void setGNSSAntennaMissed(int gNSSAntennaMissed) {
		GNSSAntennaMissed = gNSSAntennaMissed;
	}
	public int getGNSSAntennaShortCircuit() {
		return GNSSAntennaShortCircuit;
	}
	public void setGNSSAntennaShortCircuit(int gNSSAntennaShortCircuit) {
		GNSSAntennaShortCircuit = gNSSAntennaShortCircuit;
	}
	public int getMainpowerUndervoltage() {
		return mainpowerUndervoltage;
	}
	public void setMainpowerUndervoltage(int mainpowerUndervoltage) {
		this.mainpowerUndervoltage = mainpowerUndervoltage;
	}
	public int getMainpowerDown() {
		return mainpowerDown;
	}
	public void setMainpowerDown(int mainpowerDown) {
		this.mainpowerDown = mainpowerDown;
	}
	public int getLCDFault() {
		return LCDFault;
	}
	public void setLCDFault(int lCDFault) {
		LCDFault = lCDFault;
	}
	public int getTTSModuleFault() {
		return TTSModuleFault;
	}
	public void setTTSModuleFault(int tTSModuleFault) {
		TTSModuleFault = tTSModuleFault;
	}
	public int getCameraFault() {
		return cameraFault;
	}
	public void setCameraFault(int cameraFault) {
		this.cameraFault = cameraFault;
	}
	public int getDriveTimeoutDay() {
		return driveTimeoutDay;
	}
	public void setDriveTimeoutDay(int driveTimeoutDay) {
		this.driveTimeoutDay = driveTimeoutDay;
	}
	public int getStopTimeout() {
		return stopTimeout;
	}
	public void setStopTimeout(int stopTimeout) {
		this.stopTimeout = stopTimeout;
	}
	public int getInOutArea() {
		return inOutArea;
	}
	public void setInOutArea(int inOutArea) {
		this.inOutArea = inOutArea;
	}
	public int getInOutLine() {
		return inOutLine;
	}
	public void setInOutLine(int inOutLine) {
		this.inOutLine = inOutLine;
	}
	public int getSectionDriveProblem() {
		return sectionDriveProblem;
	}
	public void setSectionDriveProblem(int sectionDriveProblem) {
		this.sectionDriveProblem = sectionDriveProblem;
	}
	public int getRouteDeviate() {
		return routeDeviate;
	}
	public void setRouteDeviate(int routeDeviate) {
		this.routeDeviate = routeDeviate;
	}
	public int getVSSFault() {
		return VSSFault;
	}
	public void setVSSFault(int vSSFault) {
		VSSFault = vSSFault;
	}
	public int getOilFault() {
		return oilFault;
	}
	public void setOilFault(int oilFault) {
		this.oilFault = oilFault;
	}
	public int getVehicleStolen() {
		return vehicleStolen;
	}
	public void setVehicleStolen(int vehicleStolen) {
		this.vehicleStolen = vehicleStolen;
	}
	public int getIllegalIgnition() {
		return illegalIgnition;
	}
	public void setIllegalIgnition(int illegalIgnition) {
		this.illegalIgnition = illegalIgnition;
	}
	public int getIllegalMove() {
		return illegalMove;
	}
	public void setIllegalMove(int illegalMove) {
		this.illegalMove = illegalMove;
	}
	public int getRolloverAlarm() {
		return rolloverAlarm;
	}
	public void setRolloverAlarm(int rolloverAlarm) {
		this.rolloverAlarm = rolloverAlarm;
	}
	public Date getGetTime() {
		return getTime;
	}
	public void setGetTime(Date getTime) {
		this.getTime = getTime;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public String getPlatCode() {
		return platCode;
	}
	public void setPlatCode(String platCode) {
		this.platCode = platCode;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getProvinceArea() {
		return provinceArea;
	}
	public void setProvinceArea(String provinceArea) {
		this.provinceArea = provinceArea;
	}
	public int getIsOffline() {
		return isOffline;
	}
	public void setIsOffline(int isOffline) {
		this.isOffline = isOffline;
	}
	public int getOfflineTimes() {
		return offlineTimes;
	}
	public void setOfflineTimes(int offlineTimes) {
		this.offlineTimes = offlineTimes;
	}
	public int getOnlineTime() {
		return onlineTime;
	}
	public void setOnlineTime(int onlineTime) {
		this.onlineTime = onlineTime;
	}
	public int getBusinessType() {
		return businessType;
	}
	public void setBusinessType(int businessType) {
		this.businessType = businessType;
	}
	public boolean isLocate() {
		return locate;
	}
	public void setLocate(boolean locate) {
		this.locate = locate;
	}
	public boolean isFlame() {
		return flame;
	}
	public void setFlame(boolean flame) {
		this.flame = flame;
	}
	public boolean isOverspeed() {
		if(this.businessType==2){
			//班线车
			if(AreaCache.matchIndex(this.ruleRsWrapSet, AreaCache.AreaIndex_Outer)!=null){
				//在外环
				//1为外环外 ，2为外环内.
				this.flag = 2;
				if(this.gpsSpeed > 70){
					return true;
				}else{
					return false;
				}
			}else{
				this.flag = 1;
				if(this.gpsSpeed > 100){
					return true;
				}else{
					return false;
				}
			}
			
		}else if(this.businessType == 1){
			//危险品
		}else if(this.businessType == 3){
			//旅游包车
		}
		return false;
	}
	public void setOverspeed(boolean overspeed) {
		this.overspeed = overspeed;
	}
	public Set<RuleResultWrap> getRuleRsWrapSet() {
		return ruleRsWrapSet;
	}
	public void addRuleRsWrapSet(RuleResultWrap ruleResultWrap){
		this.ruleRsWrapSet.add(ruleResultWrap);
	}
	public void setRuleRsWrapSet(Set<RuleResultWrap> ruleRsWrapSet) {
		this.ruleRsWrapSet = ruleRsWrapSet;
	}
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
	}
	public String getAreaFlag() {
		return areaFlag;
	}
	public void setAreaFlag(String areaFlag) {
		this.areaFlag = areaFlag;
	}
	public boolean isEmergency() {
		return emergency;
	}
	public void setEmergency(boolean emergency) {
		this.emergency = emergency;
	}
	public boolean isDrowsy() {
		return drowsy;
	}
	public void setDrowsy(boolean drowsy) {
		this.drowsy = drowsy;
	}
	public boolean isNonstop() {
		return nonstop;
	}
	public void setNonstop(boolean nonstop) {
		this.nonstop = nonstop;
	}
	public Date getFlameoutTime() {
		return flameoutTime;
	}
	public void setFlameoutTime(Date flameoutTime) {
		this.flameoutTime = flameoutTime;
	}
	public Date getDriveBeginTime() {
		return driveBeginTime;
	}
	public void setDriveBeginTime(Date driveBeginTime) {
		this.driveBeginTime = driveBeginTime;
	}
	public int getOffType() {
		return offType;
	}
	public void setOffType(int offType) {
		this.offType = offType;
	}
	public boolean isInarea() {
		return inarea;
	}
	public void setInarea(boolean inarea) {
		this.inarea = inarea;
	}
	public String getZeroSpeedBegin() {
		return zeroSpeedBegin;
	}
	public void setZeroSpeedBegin(String zeroSpeedBegin) {
		this.zeroSpeedBegin = zeroSpeedBegin;
	}
	public long getFlameoutTimeLong() {
		return flameoutTimeLong;
	}
	public void setFlameoutTimeLong(long flameoutTimeLong) {
		this.flameoutTimeLong = flameoutTimeLong;
	}
	public long getIllegalParkingTime() {
		return illegalParkingTime;
	}
	public void setIllegalParkingTime(long illegalParkingTime) {
		this.illegalParkingTime = illegalParkingTime;
	}
	public long getOffTime() {
		return offTime;
	}
	public void setOffTime(long offTime) {
		this.offTime = offTime;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public Set<String> getCrossings() {
		return crossings;
	}
	public void setCrossings(Set<String> crossings) {
		this.crossings = crossings;
	}
	public String getCrossingName() {
		return crossingName;
	}
	public void setCrossingName(String crossingName) {
		this.crossingName = crossingName;
	}
	public int getType_parking() {
		return type_parking;
	}
	public void setType_parking(int type_parking) {
		this.type_parking = type_parking;
	}
	public boolean isInarea_parking() {
		return inarea_parking;
	}
	public void setInarea_parking(boolean inarea_parking) {
		this.inarea_parking = inarea_parking;
	}
	public boolean isIllegal_parking() {
		return illegal_parking;
	}
	public void setIllegal_parking(boolean illegal_parking) {
		this.illegal_parking = illegal_parking;
	}
	public double getLonParkingBegin() {
		return lonParkingBegin;
	}
	public void setLonParkingBegin(double lonParkingBegin) {
		this.lonParkingBegin = lonParkingBegin;
	}
	public double getLatParkingBegin() {
		return latParkingBegin;
	}
	public void setLatParkingBegin(double latParkingBegin) {
		this.latParkingBegin = latParkingBegin;
	}
	public boolean isFlameOut() {
		return flameOut;
	}
	public void setFlameOut(boolean flameOut) {
		this.flameOut = flameOut;
	}
	public String toString(){
		return 
		this.code+""+
		this.license+""+
		this.licenseColor+""+
		this.lon+""+
		this.lat+""+
		this.defLon+""+
		this.defLat+""+
		this.gpsTime+""+
		this.gpsSpeed+""+
		this.recordSpeed+""+
		this.mileage+""+
		this.direction+""+
		this.altitude+""+
		this.ACCState+""+
		this.positionState+""+
		this.businessState+""+
		this.oilState+""+
		this.circuitState+""+
		this.doorsState+""+
		this.emergencyAlarm+""+
		this.overspeedAlarm+""+
		this.fatigueAlarm+""+
		this.warning+""+
		this.GNSSModuleFault+""+
		this.GNSSAntennaMissed+""+
		this.GNSSAntennaShortCircuit+""+
		this.mainpowerUndervoltage+""+
		this.mainpowerDown+""+
		this.LCDFault+""+
		this.TTSModuleFault+""+
		this.cameraFault+""+
		this.driveTimeoutDay+""+
		this.stopTimeout+""+
		this.inOutArea+""+
		this.inOutLine+""+
		this.sectionDriveProblem+""+
		this.routeDeviate+""+
		this.VSSFault+""+
		this.oilFault+""+
		this.vehicleStolen+""+
		this.illegalIgnition+""+
		this.illegalMove+""+
		this.rolloverAlarm+""+
		this.getTime+""+
		this.sendTime+""+
		this.platCode+""+
		this.picUrl+""+
		this.provinceArea+""+
		this.isOffline+""+
		this.offlineTimes+""+
		this.onlineTime+""+
		this.businessType+""+
		this.locate+""+
		this.flame+""+
		this.overspeed+""+
		this.area+""+
		this.areaFlag+""+
		this.emergency+""+
		this.drowsy+""+
		this.nonstop+""+
		this.flameoutTime+""+
		this.driveBeginTime+""+
		//add in 20140403
		this.offType+""+		
		this.inarea+""+	
		this.zeroSpeedBegin+""+
		this.flameoutTimeLong+""+
		this.illegalParkingTime+""+
		this.offTime+""+	
		this.flag+""+
		this.crossingName+""+
		//add in 2014-11-09
		this.type_parking+""+
		this.inarea_parking+""+
		this.illegal_parking+""+
		this.lonParkingBegin+""+
		this.latParkingBegin+""+
		this.flameOut+"";
	}
	
}
