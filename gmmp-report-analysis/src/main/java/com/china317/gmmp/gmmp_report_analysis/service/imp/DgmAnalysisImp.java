package com.china317.gmmp.gmmp_report_analysis.service.imp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.china317.gmmp.gmmp_report_analysis.bo.DgmForbidden;
import com.china317.gmmp.gmmp_report_analysis.bo.PtmOffline;
import com.china317.gmmp.gmmp_report_analysis.bo.PtmOverSpeed;
import com.china317.gmmp.gmmp_report_analysis.bo.RuleResultWrap;
import com.china317.gmmp.gmmp_report_analysis.bo.VehicleLocate;
import com.china317.gmmp.gmmp_report_analysis.service.DgmAnalysis;
import com.china317.gmmp.gmmp_report_analysis.util.DateTime;
import com.china317.gmmp.gmmp_report_analysis.util.PolyUtilityE2;

public class DgmAnalysisImp implements DgmAnalysis{

	private static final Log log = LogFactory.getLog(DgmAnalysisImp.class);
	
	private static DgmAnalysisImp instance = new DgmAnalysisImp();
	private DgmAnalysisImp(){
		
	}
	public static DgmAnalysisImp getInstance(){
		return instance;
	}
	
	public static Map<String,PtmOverSpeed> overSpeedingMap = new HashMap<String, PtmOverSpeed>();
	public static Map<String,PtmOverSpeed> overSpeedendMap = new HashMap<String, PtmOverSpeed>();
	
	
	public static Map<String,VehicleLocate> lastRecordMap = new HashMap<String, VehicleLocate>();
	public static Map<String,PtmOffline> offlineMap = new HashMap<String, PtmOffline>();
	
	public static Map<String,DgmForbidden> forbiddeningMap = new HashMap<String,DgmForbidden>();
	public static Map<String,DgmForbidden> forbiddenedMap = new HashMap<String,DgmForbidden>();
	
	public void putLastRecord(VehicleLocate l){
		lastRecordMap.put(l.getCode(),l);
	}
	
	@Override
	public void overSpeedAnalysis(VehicleLocate entity) {
		if(!Arrays.asList(hasLicenseOfOverspeed).contains(entity.getLicense())){
			
			PtmOverSpeed overspeed = instance.overSpeedingMap.get(entity.getCode());
			if(overspeed==null){
				if(entity.isOverspeed()){
					overspeed = new PtmOverSpeed();
					//set Properties
					overspeed.addSpeed(entity.getGpsSpeed());
					overspeed.setCode(entity.getCode());
					overspeed.setLicense(entity.getLicense());
					overspeed.setLicenseColor(entity.getLicenseColor());
					overspeed.setBeginTime(DateTime.getDateTimeString(entity.getGpsTime()));
					overspeed.setFlag(entity.getFlag());
					overspeed.setBusinessType(entity.getBusinessType());
					overSpeedingMap.put(entity.getCode(),overspeed);
				}else{
					//do nothing
				}
			}else{
				if(entity.isOverspeed()){
					//更新最高速度，更新平均速度，更新结束时间
					overspeed.addSpeed(entity.getGpsSpeed());
				}else{
					//报警结束，记录一条报警记录,write a record
					log.info("[Lybc_OverSpeed_Analysis],[OverSpeedingMap.Size:]"+overSpeedingMap.size());
					System.out.println("[Lybc_OverSpeed_Analysis],[overSpeedCode:]"+entity.getCode()+"; time"+entity.getGpsTime()+"; speed:"+entity.getGpsSpeed());
					//overspeed.addSpeed(entity.getGpsSpeed());
					VehicleLocate pre = lastRecordMap.get(entity.getCode());
					if(pre != null){
						overspeed.setEndTIme(DateTime.getDateTimeString(pre.getGpsTime()));
					}else{
						overspeed.setEndTIme(DateTime.getDateTimeString(entity.getGpsTime()));
					}
					
					synchronized (overSpeedingMap) {
						instance.overSpeedingMap.remove(entity.getCode());	
					}
					//key = code+beginTime
					
					long alarmTime = DateTime.accountTime(overspeed.getBeginTime(), overspeed.getEndTIme());
					
					if(overspeed.getMaxSpeed() >= 80){
						if(overspeed.getFlag()==2 && alarmTime>=(1000 * 60 * 2)){
							//外环内
							overSpeedendMap.put(entity.getCode()+overspeed.getBeginTime(), overspeed);
						}
					}else{
						if(alarmTime>=(1000 * 60 * 2)){
							overSpeedendMap.put(entity.getCode()+overspeed.getBeginTime(), overspeed);
						}
					}
				}
			}
		
		}//end licenseOfOverSpeed
	}

	@Override
	public void offlineAnalysis(VehicleLocate e, String yyyyMMdd) {
		
	}
	@Override
	/**
	 * 禁入区域报警分析
	 */
	public void fobiddenAnalysis(VehicleLocate e,int index,int size,String yyyyMMdd) {
		/********************************************/
		//长江隧桥
		if(!Arrays.asList(hasLicenseOfCJSQ).contains(e.getLicense())){
			try{
				if(PolyUtilityE2.isBreakTheRuleBoolean(PolyUtilityE2.cjsqAreaId, e.getLon(),e.getLat())){
					if(forbiddeningMap.keySet().contains(e.getCode()+Type_AlarmCjsq)){
						
					}else{
						DgmForbidden bo = new DgmForbidden();
						bo.setCode(e.getCode());
						bo.setAlarmType(Type_AlarmCjsq);
						bo.setLicense(e.getLicense());
						bo.setLicenseColor(e.getLicenseColor());
						bo.setBeginTime(DateTime.getDateTimeString(e.getGpsTime()));
						forbiddeningMap.put(bo.getCode()+bo.getAlarmType(),bo);
					}
				}else{
					if(forbiddeningMap.keySet().contains(e.getCode()+Type_AlarmCjsq)){
						DgmForbidden bo = forbiddeningMap.get(e.getCode()+Type_AlarmCjsq);
						
						VehicleLocate preEntity = lastRecordMap.get(e.getCode());
						if(preEntity !=null){
							Date preGpsTime = preEntity.getGpsTime();
							if(e.getGpsTime().getTime() - preGpsTime.getTime() > _OffTime){
								Calendar endTimeCalendar = Calendar.getInstance();
								endTimeCalendar.setTimeInMillis(preGpsTime.getTime()+60*1000);
								bo.setEndTime(DateTime.getDateTimeString(endTimeCalendar.getTime()));
							}else{
								bo.setEndTime(DateTime.getDateTimeString(e.getGpsTime()));
							}
							forbiddenedMap.put(bo.getCode()+bo.getAlarmType()+bo.getBeginTime(), bo);
							synchronized (forbiddeningMap) {
								forbiddeningMap.remove(bo.getCode()+bo.getAlarmType());
							}
						}
					}
				}
				
				if(index == size -1){
					//只有一个轨迹点
					if(PolyUtilityE2.isBreakTheRuleBoolean(PolyUtilityE2.cjsqAreaId, e.getLon(),e.getLat())){
						DgmForbidden bo = new DgmForbidden();
						bo.setCode(e.getCode());
						bo.setAlarmType(Type_AlarmCjsq);
						bo.setLicense(e.getLicense());
						bo.setLicenseColor(e.getLicenseColor());
						bo.setBeginTime(DateTime.getDateTimeString(e.getGpsTime()));
						Calendar endTimeCalendar = Calendar.getInstance();
						endTimeCalendar.setTimeInMillis(e.getGpsTime().getTime()+60*1000);
						if(endTimeCalendar.getTimeInMillis()<=dateFormat.parse(yyyyMMdd+"235959").getTime()){
							bo.setEndTime(DateTime.getDateTimeString(endTimeCalendar.getTime()));
						}else{
							bo.setEndTime(yyyyMMdd+"235959");
						}
						forbiddenedMap.put(bo.getCode()+bo.getAlarmType()+bo.getBeginTime(), bo);
						synchronized (forbiddeningMap) {
							forbiddeningMap.remove(bo.getCode()+bo.getAlarmType());
						}
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		/********************************************/
		//南浦大桥
		try{
			if(PolyUtilityE2.isBreakTheRuleBoolean(PolyUtilityE2.npdqAreaId, e.getLon(),e.getLat())){
				if(forbiddeningMap.keySet().contains(e.getCode()+Type_AlarmNpdq)){
					
				}else{
					DgmForbidden bo = new DgmForbidden();
					bo.setCode(e.getCode());
					bo.setAlarmType(Type_AlarmNpdq);
					bo.setLicense(e.getLicense());
					bo.setLicenseColor(e.getLicenseColor());
					bo.setBeginTime(DateTime.getDateTimeString(e.getGpsTime()));
					forbiddeningMap.put(bo.getCode()+bo.getAlarmType(),bo);
				}
			}else{
				if(forbiddeningMap.keySet().contains(e.getCode()+Type_AlarmNpdq)){
					DgmForbidden bo = forbiddeningMap.get(e.getCode()+Type_AlarmNpdq);
					
					VehicleLocate preEntity = lastRecordMap.get(e.getCode());
					if(preEntity !=null){
						Date preGpsTime = preEntity.getGpsTime();
						if(e.getGpsTime().getTime() - preGpsTime.getTime() > _OffTime){
							Calendar endTimeCalendar = Calendar.getInstance();
							endTimeCalendar.setTimeInMillis(preGpsTime.getTime()+60*1000);
							bo.setEndTime(DateTime.getDateTimeString(endTimeCalendar.getTime()));
						}else{
							bo.setEndTime(DateTime.getDateTimeString(e.getGpsTime()));
						}
						forbiddenedMap.put(bo.getCode()+bo.getAlarmType()+bo.getBeginTime(), bo);
						synchronized (forbiddeningMap) {
							forbiddeningMap.remove(bo.getCode()+bo.getAlarmType());
						}
					}
				}
			}
			
			if(index == size -1){
				//只有一个轨迹点
				if(PolyUtilityE2.isBreakTheRuleBoolean(PolyUtilityE2.npdqAreaId, e.getLon(),e.getLat())){
					DgmForbidden bo = new DgmForbidden();
					bo.setCode(e.getCode());
					bo.setAlarmType(Type_AlarmNpdq);
					bo.setLicense(e.getLicense());
					bo.setLicenseColor(e.getLicenseColor());
					bo.setBeginTime(DateTime.getDateTimeString(e.getGpsTime()));
					Calendar endTimeCalendar = Calendar.getInstance();
					endTimeCalendar.setTimeInMillis(e.getGpsTime().getTime()+60*1000);
					if(endTimeCalendar.getTimeInMillis()<=dateFormat.parse(yyyyMMdd+"235959").getTime()){
						bo.setEndTime(DateTime.getDateTimeString(endTimeCalendar.getTime()));
					}else{
						bo.setEndTime(yyyyMMdd+"235959");
					}
					forbiddenedMap.put(bo.getCode()+bo.getAlarmType()+bo.getBeginTime(), bo);
					synchronized (forbiddeningMap) {
						forbiddeningMap.remove(bo.getCode()+bo.getAlarmType());
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		/********************************************/
		//杨浦大桥
		try{
			if(PolyUtilityE2.isBreakTheRuleBoolean(PolyUtilityE2.ypdqAreaId, e.getLon(),e.getLat())){
				if(forbiddeningMap.keySet().contains(e.getCode()+Type_AlarmYpdq)){
					
				}else{
					DgmForbidden bo = new DgmForbidden();
					bo.setCode(e.getCode());
					bo.setAlarmType(Type_AlarmYpdq);
					bo.setLicense(e.getLicense());
					bo.setLicenseColor(e.getLicenseColor());
					bo.setBeginTime(DateTime.getDateTimeString(e.getGpsTime()));
					forbiddeningMap.put(bo.getCode()+bo.getAlarmType(),bo);
				}
			}else{
				if(forbiddeningMap.keySet().contains(e.getCode()+Type_AlarmYpdq)){
					DgmForbidden bo = forbiddeningMap.get(e.getCode()+Type_AlarmYpdq);
					
					VehicleLocate preEntity = lastRecordMap.get(e.getCode());
					if(preEntity !=null){
						Date preGpsTime = preEntity.getGpsTime();
						if(e.getGpsTime().getTime() - preGpsTime.getTime() > _OffTime){
							Calendar endTimeCalendar = Calendar.getInstance();
							endTimeCalendar.setTimeInMillis(preGpsTime.getTime()+60*1000);
							bo.setEndTime(DateTime.getDateTimeString(endTimeCalendar.getTime()));
						}else{
							bo.setEndTime(DateTime.getDateTimeString(e.getGpsTime()));
						}
						forbiddenedMap.put(bo.getCode()+bo.getAlarmType()+bo.getBeginTime(), bo);
						synchronized (forbiddeningMap) {
							forbiddeningMap.remove(bo.getCode()+bo.getAlarmType());
						}
					}
				}
			}
			
			if(index == size -1){
				//只有一个轨迹点
				if(PolyUtilityE2.isBreakTheRuleBoolean(PolyUtilityE2.ypdqAreaId, e.getLon(),e.getLat())){
					DgmForbidden bo = new DgmForbidden();
					bo.setCode(e.getCode());
					bo.setAlarmType(Type_AlarmYpdq);
					bo.setLicense(e.getLicense());
					bo.setLicenseColor(e.getLicenseColor());
					bo.setBeginTime(DateTime.getDateTimeString(e.getGpsTime()));
					Calendar endTimeCalendar = Calendar.getInstance();
					endTimeCalendar.setTimeInMillis(e.getGpsTime().getTime()+60*1000);
					if(endTimeCalendar.getTimeInMillis()<=dateFormat.parse(yyyyMMdd+"235959").getTime()){
						bo.setEndTime(DateTime.getDateTimeString(endTimeCalendar.getTime()));
					}else{
						bo.setEndTime(yyyyMMdd+"235959");
					}
					forbiddenedMap.put(bo.getCode()+bo.getAlarmType()+bo.getBeginTime(), bo);
					synchronized (forbiddeningMap) {
						forbiddeningMap.remove(bo.getCode()+bo.getAlarmType());
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		/********************************************/
		//其他禁入区域
		try{
			RuleResultWrap warp = PolyUtilityE2.isBreakTheRule(PolyUtilityE2.othersForbiddenAreaId,  e.getLon(),e.getLat());
			if(warp!=null){
				if(forbiddeningMap.keySet().contains(e.getCode()+warp.getPolygonName())){
					
				}else{
					DgmForbidden bo = new DgmForbidden();
					bo.setCode(e.getCode());
					bo.setAlarmType(getForbiddenAreaType(warp.getPolygonName()));
					bo.setLicense(e.getLicense());
					bo.setLicenseColor(e.getLicenseColor());
					bo.setBeginTime(DateTime.getDateTimeString(e.getGpsTime()));
					forbiddeningMap.put(bo.getCode()+bo.getAlarmType(),bo);
				}
			}else{
				if(forbiddeningMap.keySet().contains(e.getCode()+warp.getPolygonName())){
					DgmForbidden bo = forbiddeningMap.get(e.getCode()+warp.getPolygonName());
					
					VehicleLocate preEntity = lastRecordMap.get(e.getCode());
					if(preEntity !=null){
						Date preGpsTime = preEntity.getGpsTime();
						if(e.getGpsTime().getTime() - preGpsTime.getTime() > _OffTime){
							Calendar endTimeCalendar = Calendar.getInstance();
							endTimeCalendar.setTimeInMillis(preGpsTime.getTime()+60*1000);
							bo.setEndTime(DateTime.getDateTimeString(endTimeCalendar.getTime()));
						}else{
							bo.setEndTime(DateTime.getDateTimeString(e.getGpsTime()));
						}
						forbiddenedMap.put(bo.getCode()+bo.getAlarmType()+bo.getBeginTime(), bo);
						synchronized (forbiddeningMap) {
							forbiddeningMap.remove(bo.getCode()+bo.getAlarmType());
						}
					}
				}
			}
			
			if(index == size -1){
				//只有一个轨迹点
				if(warp!=null){
					DgmForbidden bo = new DgmForbidden();
					bo.setCode(e.getCode());
					bo.setAlarmType(warp.getPolygonName());
					bo.setLicense(e.getLicense());
					bo.setLicenseColor(e.getLicenseColor());
					bo.setBeginTime(DateTime.getDateTimeString(e.getGpsTime()));
					Calendar endTimeCalendar = Calendar.getInstance();
					endTimeCalendar.setTimeInMillis(e.getGpsTime().getTime()+60*1000);
					if(endTimeCalendar.getTimeInMillis()<=dateFormat.parse(yyyyMMdd+"235959").getTime()){
						bo.setEndTime(DateTime.getDateTimeString(endTimeCalendar.getTime()));
					}else{
						bo.setEndTime(yyyyMMdd+"235959");
					}
					forbiddenedMap.put(bo.getCode()+bo.getAlarmType()+bo.getBeginTime(), bo);
					synchronized (forbiddeningMap) {
						forbiddeningMap.remove(bo.getCode()+bo.getAlarmType());
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	@Override
	public void illegalInOutAnalysis(VehicleLocate e) {
		
	}
	@Override
	public void illegalParkingAnalysis(VehicleLocate e) {
		
	}
	@Override
	public void offlineDisAnalysis(VehicleLocate e) {
		
	}
	
	
	/**************************下面是静态变量******************************/
	//有长江隧桥通行证的车
	public static String[] hasLicenseOfCJSQ = {"沪BK5615","沪BD1628","沪B76701","沪BK5568","沪BK5607"};
	//人民广场通行证 2011-9-6 
	public static String[] hasLicenseOfPS = {"沪BH5397","沪BH5470","沪BH5476","沪BH5477","沪BH5478","沪BH5480","沪BH5482"
								,"沪BH5485","沪BH5493","沪BH5497","沪BH5521","沪BH5526","沪BH5537","沪BH5559","沪BH5561",
								"沪BH5562","沪BH5563","沪BH5566","沪BH5568","沪BH5576","沪AP1940","沪AP1949","沪AL6942",
								"沪AR2669","沪AR2591","沪B17380","沪B63892","沪B63919","沪K06092","沪JC1076","沪H56858",
								"沪B04391","沪F05722","沪B66365","沪B66290","沪B66296","沪B66442","沪BQ2576","沪BQ5060",
								"沪BQ2657","沪B04691","沪B55210","沪AS3235","沪B04755","沪B26412","沪B04818","沪B04758",
								"沪B04812","沪BH1878","沪A96605","沪A96384","沪AP0776","沪AQ9641","沪AT1222","沪FB0756",
								"沪JQ5611","沪AQ9751","沪AR8130","沪AR8166","沪A97884","沪AL0087","沪LG2661","沪AG4078",
								"沪AP5730","沪B21216","沪B09737","沪B11020","沪B08577","沪B12987","沪B12978","沪B39193",
								"沪B39266","沪FF5866","沪GP8131","沪B30967","沪AS8585","沪AT6996","沪AH2380","沪B24252"};
	
	public static String[] hasLicenseOfOverspeed = {"沪BH3868","沪BK6038","沪BE7301","沪BK6045","沪BE7321","沪BK6063"
													,"沪BD8035","沪BG5701","沪AK3136","沪BG6048","沪AR3195","沪BG6063"
													,"沪B60066","沪BG6066","沪B60200","沪BG6070","沪B60053","沪B86760"
													,"沪BD8099","沪B86821","沪BD8070","沪B87138","沪BE7307","沪B96495"
													,"沪BK6036","沪B96692","沪D32971","沪D32965","沪D32903","沪D32935"
													,"沪D32988","沪D32990","沪D32989","沪D32983"};
	
	//南浦大桥禁行时间早上6：00至晚上20：00
	public static String[] forbiddenTimeOfNPDQ = {"06:00","20:00"};
	public static String[][] forbiddenTimeOfYPDQ = {{"06:00","10:00"},{"16:00","20:00"}};
	
	public static final String Type_Fatigue = "fatigue";
	public static final String Type_Emergency = "emergency";
	public static final String Type_Overspeed = "overspeed";
	public static final String Type_illegalEntryExit = "illegalEntryExit";
	public static final String Type_illegalParking = "illegalParking";
	public static final String Type_Off = "off";
	public static final String Type_Run = "run";
	public static final String Type_Alarmps = "alarmps";
	public static final String Type_Alarmexpo = "alarmexpo";
	public static final String Type_AlarmCjsq = "alarmcjsq";
	public static final String Type_AlarmNpdq = "alarmnpdq";
	
	public static final String Type_Alarmlpdq = "alarmlpdq";
	public static final String Type_Alarmszlsd = "alarmszlsd";
	public static final String Type_Alarmdplsd = "alarmdplsd";
	public static final String Type_Alarmxznlsd = "alarmxznlsd";
	public static final String Type_Alarmfxdlsd = "alarmfxdlsd";
	public static final String Type_Alarmrmlsd = "alarmrmlsd";
	public static final String Type_Alarmyadlsd = "alarmyadlsd";
	public static final String Type_Alarmxjlsd = "alarmxjlsd";
	public static final String Type_Alarmdllsd = "alarmpdllsd";
	public static final String Type_Alarmjglsd = "alarmjglsd";
	public static final String Type_Alarmlylsd = "alarmlylsd";
	public static final String Type_Alarmxylsd = "alarmnpxylsd";
	
	private static final String Type_AlarmYpdq = "alarmypdq";
	
	public static long _3Minute = 1000 * 60 * 3;
	public static long _5Minute = 1000 * 60 * 5;
	public static long _10Minute = 1000 * 60 * 10;
	public static long _15Minute = 1000 * 60 * 15;
	public static long _30Minute = 1000 * 60 * 30;
	public static long _1Hour = 1000 * 60 * 60 * 1;
	public static long _4Hour = 1000 * 60 * 60 * 4;
	public static long _12Hour = 1000 * 60 * 60 * 12;
	public static long _24Hour = 1000 * 60 * 60 * 24;
	public static long _72Hour = 1000 * 60 * 60 * 72;
	public static long _5Day = 1000 * 60 * 60 * 24 * 5;
	public static long _15Day = 1000 * 60 * 60 * 24 * 15;
	
	public static long _OffTime = _30Minute;

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	public static final String[] forbiddenAreaTypes = {Type_Alarmlpdq,Type_Alarmszlsd,Type_Alarmdplsd,Type_Alarmxznlsd,Type_Alarmfxdlsd,Type_Alarmrmlsd,
		Type_Alarmyadlsd,Type_Alarmxjlsd,Type_Alarmdllsd,Type_Alarmjglsd,Type_Alarmlylsd,Type_Alarmxylsd};
	public static final String[] forbiddenAreaNames = {"卢浦大桥" ,"上中路隧道","打浦路隧道","西藏南路隧道","复兴东路隧道","人民路隧道"
		,"延安东路隧道","新建路隧道","大连路隧道","军工路隧道","龙耀路隧道","翔殷路隧道"};
	public static String getForbiddenAreaType(String areaName){
		for(int i=0;i<forbiddenAreaNames.length;i++){
			if(forbiddenAreaNames[i].equals(areaName)){
				return forbiddenAreaTypes[i];
			}
		}
		return "";
	}
}
