package com.china317.gmmp.gmmp_report_analysis.service.imp;

import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.china317.gmmp.gmmp_report_analysis.bo.DgmEntryExit;
import com.china317.gmmp.gmmp_report_analysis.bo.DgmForbidden;
import com.china317.gmmp.gmmp_report_analysis.bo.DgmIllegalParking;
import com.china317.gmmp.gmmp_report_analysis.bo.FatigueAlarmEntity;
import com.china317.gmmp.gmmp_report_analysis.bo.PtmOffline;
import com.china317.gmmp.gmmp_report_analysis.bo.PtmOverSpeed;
import com.china317.gmmp.gmmp_report_analysis.bo.RuleResultWrap;
import com.china317.gmmp.gmmp_report_analysis.bo.VehicleLocate;
import com.china317.gmmp.gmmp_report_analysis.cache.AreaCache;
import com.china317.gmmp.gmmp_report_analysis.service.DgmAnalysis;
import com.china317.gmmp.gmmp_report_analysis.util.DateTime;
import com.china317.gmmp.gmmp_report_analysis.util.PolyUtilityE2;
import com.ibatis.sqlmap.client.SqlMapClient;

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
	
	public static Map<String,DgmEntryExit> illegalExitMap = new HashMap<String, DgmEntryExit>();
	
	public static Map<String,DgmIllegalParking> illegalParkingMap = new HashMap<String,DgmIllegalParking>();
	
	public static Map<String,FatigueAlarmEntity> fatigueAlarmMap = new HashMap<String, FatigueAlarmEntity>();
	
	public void putLastRecord(VehicleLocate l){
		lastRecordMap.put(l.getCode(),l);
	}
	
	public void addZeroBegin(VehicleLocate l){
		VehicleLocate old = lastRecordMap.get(l.getCode());
		if(l.getGpsSpeed() <=0){
			if(old!=null
					&& old.getZeroSpeedBegin()!=null
					&& old.getZeroSpeedBegin().length() >0){
				l.setZeroSpeedBegin(old.getZeroSpeedBegin());
			}else{
				l.setZeroSpeedBegin(DateTime.getDateTimeString(l.getGpsTime()));
			}
		}else{
			l.setZeroSpeedBegin(null);
		}
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
					log.info("[Dgm_OverSpeed_Analysis],[OverSpeedingMap.Size:]"+overSpeedingMap.size());
					System.out.println("[Dgm_OverSpeed_Analysis],[overSpeedCode:]"+entity.getCode()+"; time"+entity.getGpsTime()+"; speed:"+entity.getGpsSpeed());
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
					
					if(overspeed.getFlag()==1){
						if(overspeed.getMaxSpeed() >=80){
							overSpeedendMap.put(entity.getCode()+overspeed.getBeginTime(), overspeed);
						}
						if(overspeed.getMaxSpeed() >=60 
								&& overspeed.getMaxSpeed() <=80
								&& alarmTime >= (120*1000)){
							overSpeedendMap.put(entity.getCode()+overspeed.getBeginTime(), overspeed);
						}
					}else{
						if(alarmTime>=(60 * 2)){
							overSpeedendMap.put(entity.getCode()+overspeed.getBeginTime(), overspeed);
						}
					}
					/*if(overspeed.getMaxSpeed() >= 80){
						if(overspeed.getFlag()==2 && alarmTime>=(1000 * 60 * 2)){
							//外环内
							overSpeedendMap.put(entity.getCode()+overspeed.getBeginTime(), overspeed);
						}else{
							overSpeedendMap.put(entity.getCode()+overspeed.getBeginTime(), overspeed);
						}
					}else{
						if(alarmTime>=(1000 * 60 * 2)){
							overSpeedendMap.put(entity.getCode()+overspeed.getBeginTime(), overspeed);
						}
					}*/
				}
			}
		
		}//end licenseOfOverSpeed
	}

	@Override
	public void offlineAnalysis(VehicleLocate entity, String yyyyMMdd) throws Exception {
		
		VehicleLocate preEntity = lastRecordMap.get(entity.getCode());
		if(preEntity==null){
			//与0点进行比较，如果gpsTime超过10min，记录一条从0点开始的掉线记录
			long offLineTime = 0;
			Date begin = DateTime.parseDate(yyyyMMdd+"000000",DateTime.PATTERN_0);
			Date end = entity.getGpsTime();
			offLineTime = DateTime.accountTime3(begin, end);
			log.info("[Dgm_Offline_Analysis],[offLineTime:]"+offLineTime);
			if(offLineTime >= 10*60){
				PtmOffline offLine = new PtmOffline();
				//开始时间记录为00,00,00； 结束时间为gpstime
				//key = code + offlinebeginTIme
				instance.offlineMap.put(entity.getCode()+"", offLine);
				log.info("[Dgm_Offline_Analysis],[offlineMap.Size:]"+offlineMap.size());
				log.info("[Dgm_Offline_Analysis],[offlineCode:]"+entity.getCode());
				
			}
			
		}else{
			
			long offLineTime = 0L;
			//计算 gpstime时间差
			Date begin = preEntity.getGpsTime();
			Date end = entity.getGpsTime();
			offLineTime = DateTime.accountTime3(begin, end);
			log.info("[Dgm_Offline_Analysis],[offLineTime:]"+offLineTime);
			if(offLineTime >= 10*60){
				PtmOffline offLine = new PtmOffline();
				//开始时间记录为preEntity的gpsTIme； 结束时间为gpstime
				//key = code + offlinebeginTIme
				instance.offlineMap.put(entity.getCode()+"", offLine);
				log.info("[Dgm_Offline_Analysis],[offlineMap.Size:]"+offlineMap.size());
				log.info("[Dgm_Offline_Analysis],[offlineCode:]"+entity.getCode());
			}
			
		}
		
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
					forbiddeningMap.put(bo.getCode(),bo);
				}
			}else{
				if(forbiddeningMap.keySet().contains(e.getCode())){
					DgmForbidden bo = forbiddeningMap.get(e.getCode());
					
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
		
		VehicleLocate old = lastRecordMap.get(e.getCode());
		if(old!=null){
			if(e.isDomestic() && !old.isDomestic()){
				//入境
				if(!inEntryExit(e,old)){
					DgmEntryExit exit = new DgmEntryExit();
					exit.setCode(e.getCode());
					exit.setBegin_time(DateTime.getDateTimeString(old.getGpsTime()));
					exit.setEnd_time(DateTime.getDateTimeString(e.getGpsTime()));
					exit.setDetail("入境");
					illegalExitMap.put(exit.getCode()+exit.getBegin_time(), exit);
				}
			}else if(!e.isDomestic() && old.isDomestic()){
				//出境
				if(!inEntryExit(e,old)){
					DgmEntryExit exit = new DgmEntryExit();
					exit.setCode(e.getCode());
					exit.setBegin_time(DateTime.getDateTimeString(old.getGpsTime()));
					exit.setEnd_time(DateTime.getDateTimeString(e.getGpsTime()));
					exit.setDetail("出境");
					illegalExitMap.put(exit.getCode()+exit.getBegin_time(), exit);
				}
			}
		}
		
	}
	private boolean inEntryExit(VehicleLocate e, VehicleLocate old) {
		
		boolean re = false;
		
		double longtitude_before = old.getLon();
		double latitude_before = old.getLat();
		
		//LngLat in_before = new LngLat(longtitude_before, latitude_before);
		//LngLat des_before = CacheConvert.EncryptGPSUseCache(in_before);
		
		//double lon_before = des_before.getLng();
		//double lat_before = des_before.getLat();
		
		double ilongtitude_before = (longtitude_before * 1000000);
		double ilatitude_before = (latitude_before * 1000000);
		Point2D p_before = new Point2D.Double(ilongtitude_before, ilatitude_before);
		
		/*888888888888888888888888888888888888888888888888888888*/
		double longtitude_after = e.getLon();
		double latitude_after = e.getLat();
		
		//LngLat in_after = new LngLat(longtitude_after, latitude_after);
		//LngLat des_after = CacheConvert.EncryptGPSUseCache(in_after);
		
		//double lon_after = des_after.getLng();
		//double lat_after = des_after.getLat();
		
		double ilongtitude_after = (longtitude_after * 1000000);
		double ilatitude_after = (latitude_after * 1000000);
		Point2D p_after = new Point2D.Double(ilongtitude_after, ilatitude_after);
		
		/*888888888888888888888888888888888888888888888888888888*/
		//��װ�߶�
		Line2D line_normal = new Line2D.Double(p_before,p_after);
		
		for(int aa = 0; aa < linelist.size(); aa++){
			Line2D l = linelist.get(aa);
			if(line_normal.intersectsLine(l)){
				re = true;
				break;
			}
		}
		return re;
		
	}
	@Override
	public void illegalParkingAnalysis(VehicleLocate e) {
		VehicleLocate old = lastRecordMap.get(e.getCode());
		RuleResultWrap ret1 = AreaCache.matchIndex(e.getRuleRsWrapSet(),AreaCache.AreaIndex_Parking);
		if (ret1 != null) {
			e.offType = OffType_Parking;
			e.inarea = true;
		}
		RuleResultWrap ret2 = AreaCache.matchIndex(e.getRuleRsWrapSet(),AreaCache.AreaIndex_Inspection);
		if (ret2 != null) {
			e.offType = OffType_Inspection;
			e.inarea = true;
		}
		RuleResultWrap ret3 = AreaCache.matchIndex(e.getRuleRsWrapSet(),AreaCache.AreaIndex_Maintenance);
		if (ret3 != null) {
			e.offType = OffType_Maintenance;
			e.inarea = true;
		}
		
		if(old !=null){
			log.info("[illegalParking]:code:"+e.getCode()+" isoff:"+e.isOff(old));
			if(!e.isOff(old)){
				double distance = 0;
				if(0==e.getACCState() && e.getGpsSpeed() < 30){
					//熄火
					e.flameOut = true;
				}else{
					e.flameOut = false;
				}
				if(e.getZeroSpeedBegin()!=null){
					try{
						long tmp = DateTime.parseDate(e.getZeroSpeedBegin()).getTime();
						e.flameOutTIme = e.getGpsTime().getTime()-tmp;
						e.illegal_parkingTime = getIllegalParkingTime(e.getZeroSpeedBegin());
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
				
				if(e.flameOut){
					if(!e.inarea&& e.isOuter() && e.getIllegal_parkingTime() > _1Hour*5 && distance==0 ){
						e.illegal_parking = true;
					}
				}
			}else{
				long offTime = e.getGpsTime().getTime()-old.getGpsTime().getTime();
				e.setOffTime(offTime);
				
				if(e.inarea){
					
				}else{
					if(0==e.getACCState() && e.getGpsSpeed() < 30){
						e.illegal_parkingTime = getIllegalParkingTime(e.getGpsTime());
						e.offType = OffType_Engineoff;
						if(e.isOuter() && e.illegal_parkingTime > _1Hour*5){
							e.illegal_parking = true;
						}
					}
				}
			}
		}
		if(e.illegal_parking == true){
			DgmIllegalParking ill = null;
			if(illegalParkingMap.get(e.getCode())==null){
				ill = new DgmIllegalParking();
				ill.setCode(e.getCode());
				ill.setType(String.valueOf(e.getOffType()));
				ill.setFlag("0");
				ill.setLicense(e.getLicense());
				ill.setEndTime(DateTime.getDateTimeString(e.getGpsTime()));
			}else{
				ill =  illegalParkingMap.get(e.getCode());
				ill.setEndTime(DateTime.getDateTimeString(e.getGpsTime()));
			}
			illegalParkingMap.put(e.getCode(),ill);
		}
	}
	public long getIllegalParkingTime(String zeroSpeedBegin){
		Calendar tempDate = DateTime.getDateTime(zeroSpeedBegin);
		return getIllegalParkingTime(tempDate);
	}
	
	public long getIllegalParkingTime(Calendar zeroSpeedBegin){
		return getIllegalParkingTime(zeroSpeedBegin.getTime());
	}
	public long getIllegalParkingTime(Date zeroSpeedBegin){
		long time = 0;
		Calendar now = Calendar.getInstance();
		
		Calendar start_point = (Calendar)now.clone();
		if(now.get(Calendar.HOUR_OF_DAY)<22){ 
			start_point.add(Calendar.DAY_OF_MONTH, -1);
		}
		start_point.set(Calendar.HOUR_OF_DAY, 22);
		start_point.set(Calendar.MINUTE, 0);
		start_point.set(Calendar.SECOND, 0);
		
		
		Calendar end_point = (Calendar)start_point.clone();
		end_point.add(Calendar.HOUR_OF_DAY, 10);
		
		
		if(now.getTimeInMillis()>start_point.getTimeInMillis() && now.getTimeInMillis()<end_point.getTimeInMillis()){
			if(zeroSpeedBegin.getTime()>start_point.getTimeInMillis() && zeroSpeedBegin.getTime()<end_point.getTimeInMillis()){
				time = now.getTimeInMillis()-zeroSpeedBegin.getTime();
			} else if(zeroSpeedBegin.getTime()<=start_point.getTimeInMillis()){
				time = now.getTimeInMillis()-start_point.getTimeInMillis();
			} else {
				//zeroSpeedBegin.getTime()>=end_point.getTimeInMillis()
			}
		} else if(now.getTimeInMillis()<start_point.getTimeInMillis()){
		} else {
			//now.getTimeInMillis()>=end_point.getTimeInMillis()
			if(zeroSpeedBegin.getTime()>start_point.getTimeInMillis() && zeroSpeedBegin.getTime()<end_point.getTimeInMillis()){
				time = end_point.getTimeInMillis()-zeroSpeedBegin.getTime();
			} else if(zeroSpeedBegin.getTime()<=start_point.getTimeInMillis()){
				time = end_point.getTimeInMillis()-start_point.getTimeInMillis();
			} else {
				//zeroSpeedBegin.getTime()>=end_point.getTimeInMillis()
			}
		}
		
		Calendar end_point_yest = (Calendar)end_point.clone();
		end_point_yest.add(Calendar.DAY_OF_MONTH, -1);
		
		long tempTime = end_point_yest.getTimeInMillis()-zeroSpeedBegin.getTime();
		//time = Math.max(time, tempTime>_1Hour*8?_1Hour*8:tempTime);
		time += tempTime>_1Hour*8?_1Hour*8:(tempTime<0?0:tempTime);
		
		return time;
	}
	@Override
	public void offlineDisAnalysis(VehicleLocate e) {
		
		
	}
	

	public Map<String, DgmForbidden> getForbiddeningMap(){
		return forbiddenedMap;
	}
	
	public Map<String, PtmOverSpeed> getOverSpeedMap(){
		return overSpeedendMap;
	}
	
	public Map<String, DgmEntryExit> getExitMap(){
		return illegalExitMap;
	}
	
	public Map<String, DgmIllegalParking> getIllegalParking(){
		return illegalParkingMap;
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
	private static ApplicationContext context = new FileSystemXmlApplicationContext("//home/gmmp/repAnalysis/bin/applicationcontext.xml");

	public static HashMap rules=new HashMap();
	
	public static List<Point2D> plist = new ArrayList<Point2D>();
	public static List<Line2D> linelist = new ArrayList<Line2D>();
	
	private static String ruleid_1 = "2012070414552300002";
	
	static{
		SqlMapClient sqlMapClient = (SqlMapClient) context.getBean("sqlMapClientDgm");
		Connection conn = null;
		try{
			conn=sqlMapClient.getDataSource().getConnection();
			Statement stmt = conn.createStatement();
			String sql="select r.AREA_IN, r.ID rid,r.Name,p.NAME as polygonName,p.corp_code as corpCode,pp.* from tab_rule r,tab_rule_area_rel ra,tab_rule_area a,tab_rule_area_polygon p,tab_rule_area_polygon_point pp ";
			sql+=" where r.id=ra.rule_id and ra.area_id=a.id and a.id=p.area_id and p.id=pp.polygon_id  ";
			sql+=" and r.id= '2012070414552300002'";
			sql+=" order by r.id,pp.polygon_id,order_code ";
			
			ResultSet rs = stmt.executeQuery(sql);
			String oldrid="";
			String oldpid="";
			List polylist=new ArrayList();
			rules.clear();
			
			RuleResultWrap pw =  new RuleResultWrap();
			pw.setPg(new Polygon());
			
			int count=0;
			while(rs.next())
			{
				String rid=rs.getString("rid");
				String ruleName=rs.getString("name");
				String pid=rs.getString("polygon_id");
				String isAreaIn=rs.getString("AREA_IN");
				String polygonName=rs.getString("polygonName");
				String corpCode=rs.getString("corpCode");
				
				int  longitude = (int)(rs.getDouble("longitude")*1000000);
				int  latitude =  (int)(rs.getDouble("latitude")*1000000);
				if(oldrid.equalsIgnoreCase(rid)==false)
				{	polylist=new ArrayList();
					rules.put(rid,polylist);
					oldrid=rid;
					oldpid = "";
				}
				if(oldpid.equalsIgnoreCase(pid)==false)
				{
					pw =  new RuleResultWrap();
					pw.setPg(new Polygon());
					
					pw.setRuleId(rid);
					pw.setRuleName(ruleName);
					pw.setPolygonId(pid);
					pw.setPolygonName(polygonName);
					pw.setCorpCode(corpCode);
					
					pw.setIsAreaIn(isAreaIn);
					polylist.add(pw);
					oldpid=pid;
				}
				pw.getPg().addPoint(longitude, latitude);
				count++;
			}
			rs.close();
			stmt.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		List list = (List) rules.get(ruleid_1);
		List<Point2D> plist = new ArrayList<Point2D>();
		for (int j = 0; j < list.size(); j++) {
			RuleResultWrap pw = (RuleResultWrap) list.get(j);
			int[] x = pw.getPg().xpoints;
			int[] y = pw.getPg().ypoints;
			int length = x.length;
			for(int i = 0; i < length; i++){
				double x_d = (double)x[i];
				double y_d = (double)y[i];
				Point2D p = new Point2D.Double(x_d, y_d);
				plist.add(p);
			}
			//if("0".equals(pw.getIsAreaIn())){
				//if (pw.getPg().contains(ilongtitude, ilatitude) ) {
				//	System.out.println("in area");
				//	returnEn = pw;
				//	break;
				//}
			//}
		}
		
		for(int k =0;k < plist.size();k++){
			Point2D p1 = plist.get(k);
			for(int j = k; j < plist.size(); j++){
				Point2D p2 = plist.get(j);
				Line2D l = new Line2D.Double(p1,p2);
				linelist.add(l);
			}
		}
	}
	
	public int getOfflineRecordsSize(){
		return instance.offlineMap.size();
	}
	
	public int getOverSpeedRecordsSize(){
		return instance.overSpeedendMap.size();
	}
	
	public int getFobbidedSize(){
		return instance.forbiddenedMap.size();
	}
	
	public int getIllParkingSize(){
		return instance.illegalParkingMap.size();
	}
	
	public int getFatigueSize(){
		return instance.fatigueAlarmMap.size();
	}
	
	public Map<String,FatigueAlarmEntity> getFatigueMap(){
		return fatigueAlarmMap;
	}
	
	
	public static int OffType_Unknown = 0;//
	public static int OffType_Parking = 1;//ͣ
	public static int OffType_Inspection = 2;//
	public static int OffType_Maintenance = 3;//
	public static int OffType_Engineoff = 4;//
	public static int OffType_Blindspot = 5;//
	public static int OffType_NetBroken = 6;//
	
	
	@Override
	public void fatigueAnalysis(List<VehicleLocate> list, String yyyyMMdd)
			throws Exception {
		try{
		//&& num < 30){
		//VehiclePO vehicle = VehicleCache.getVeh("15000902549");
		//List<DgmDbPackEn> packages = INSTANCE.findVehiclePackages("15000902549", yyyyMMdd);
		VehicleLocate entity = null;
		VehicleLocate preEntity = null;	//上一个数据包
		VehicleLocate alarmStartEntity = null;	//一次报警记录的开始点
		VehicleLocate alarmEndEntity = null;		//一次报警记录的结束点
		VehicleLocate restStartEntity = null;	//一次休息记录的开始点
		VehicleLocate restEndEntity = null;		//一次休息记录的结束点
		
		//Map<String,Map<String,VehicleLocate>> workMap = new HashMap<String, Map<String,VehicleLocate>>();//开始时间为key的 工作记录Map
		//Map<String,FatigueWorkEntity> fatigueWorkMap = new HashMap<String, FatigueWorkEntity>();//开始时间为key的  工作记录Map
		//Map<String,Map<String,VehicleLocate>> alarmMap = new HashMap<String, Map<String,VehicleLocate>>();//开始时间为key的 报警记录Map
		
		
		long workTime = 0L;						//该车累计运营时长
		long restTime = 0L;						//该车累计休息时长
		//packages asc order;
		
		
		for(int i = 0; list != null && i < list.size(); i++){
			entity = list.get(i);
			
			boolean valid = true;
			//预处理 
			Date gpsTime =entity.getGpsTime();
			if(preEntity!=null && preEntity.getGpsTime().compareTo(entity.getGpsTime())==0){
				valid = false;
			}
			double longtitude = entity.getLon();
			double latitude = entity.getLat();
			double speed = entity.getGpsSpeed();
			
			if (latitude <= 10 || latitude >= 70 || longtitude <= 60 || longtitude >= 150 ){
				valid = false;
			}
			
			if(valid){
				
				//算法说明：无论当前点是运营还是休息，都要进行报警的判断
				/*
				 * 运营点	需要进行运营记录开始的判断
				 * 		isWork == true,该点是运营状态，
								pre点为空
									说明是第一个数据包，而且状态是运营点，那么当前点算作报警开始点
									pre 指向 当前点，continue
				 * 				pre点不为空
				 * 					pre点 isWork == false,pre点是休息的，所以当前点算作运营记录开始点
				 * 							休息结束点，计算累计休息时长
				 * 								
				 * 						
				 * 					pre点 isWork == true,pre点是运营的，所以当前点算作运营记录进行中
				 * 						
				 * 							
				 * 
				 * 休息点	需要进行运营记录结束点判断
				 * 				
				 * 				pre点为空
				 * 					说明是第一个数据包，而且状态是休息的，那么算作休息第一点
				 * 					pre 指向 当前点，continue
				 * 				pre点不为空
				 * 					pre点 isWork == false,pre点是休息的，所以当前点算作休息记录点
				 * 									更新累计休息时长
				 * 					pre点 isWork == true,pre点是运营的，所以当前点算作运营记录结束点，
				 * 									记录一条运营记录，同时将运营开始，结束清空，准备下一条运营记录。
				 * 									计算累计运营时长。
				 * 
				 * 							if 累计运营时长 > 4小时
				 * 									记录报警
				 * 									清空准备数据。
				 * 							else 
				 * 								
				 * 
				 * 
				 * 
				 * 
				 * 都需要进行是否是报警点的分析 	计算当前点前
				 */
				if(entity.isWork()){//当前点运营
					if(preEntity == null){
						//System.out.println("11");
						alarmStartEntity = entity;
						preEntity = entity;
						//log.info(vehicle.getLicensecard() + "step 1");
					}else{
						
						if(preEntity.isWork()){
							//System.out.println("12");
							//log.info(vehicle.getLicensecard() + "step 21");
							//前一点运营 更新累计运营时间
							//if(entity != null && entity.getGpsTime() != null && preEntity != null && preEntity.getGpsTime() != null){
								preEntity = entity;
							//}
						}else{
							//System.out.println("13");
							//log.info(vehicle.getLicensecard() + "step 22");
							//前一点休息	即 休息 转为 运营
							if(alarmStartEntity == null){
								alarmStartEntity = entity;
							}
							
							//记录一条休息记录
							if(entity != null && entity.getGpsTime() != null && restStartEntity != null && restStartEntity.getGpsTime() != null){
								restTime = DateTime.accountTime3(restStartEntity.getGpsTime(), entity.getGpsTime());
							}else{
								//out(错误:需要计算休息时间的时候，休息开始时间为空)
								//System.out.println("错误:需要计算休息时间的时候，休息开始时间为空1" + (restStartEntity == null) + (restStartEntity.getGpsTime() == null));
							}
														
							
							//log.info(workTime+"::"+restTime+"step22");
							if(restTime < _10Minute*2/1000){
								workTime += restTime;
								restTime = 0;
								restStartEntity = null;
								preEntity = entity;
								
							}else if(workTime > _4Hour/1000 && restTime >= _10Minute*2/1000){
									//记录一次报警
									log.info(workTime+"::"+restTime+"step23");
									alarmEndEntity = restStartEntity;
									FatigueAlarmEntity fatigueAlarmEntity = new FatigueAlarmEntity.Builder()
										.licence(entity.getLicense())
										.licenceColor("")
										.alarmStartTime(DateTime.getDateTimeString(alarmStartEntity.getGpsTime()))
										.alarmEndTime(DateTime.getDateTimeString(alarmEndEntity.getGpsTime()))
										.pointCount("")
										.create();

									fatigueAlarmMap.put(fatigueAlarmEntity.getLicence()+fatigueAlarmEntity.getAlarmStartTime(), fatigueAlarmEntity);
									
									alarmStartEntity = null;
									alarmEndEntity = null;
									
									preEntity = null;
									
									restStartEntity = null;
									workTime = 0;
									restTime = 0;							
							}else if(workTime <= _4Hour/1000 && restTime >= _10Minute*2/1000){
								
								preEntity = null;
								restTime = 0;
								workTime = 0;
								restStartEntity = null;
								alarmStartEntity = null;
								
							}else{
								preEntity = entity;
								restTime = 0;
								restStartEntity = null;
							}
						}
					}
				}else{
					if(preEntity == null){
						//System.out.println("21");
						//第一个点为休息点，现在还是休息，累计休息时间
						restStartEntity = entity;
						preEntity = entity;
					}else{
						if(preEntity.isWork()){
							//System.out.println("22");
							//运营 转为 休息 记录一个休息开始点
								if(restStartEntity == null){
									restStartEntity = entity;
								}
								//累计运营时间 记录一条运营记录
								if(alarmStartEntity != null){
									alarmEndEntity = entity;
									if(alarmEndEntity != null && alarmEndEntity.getGpsTime() != null && alarmStartEntity != null && alarmStartEntity.getGpsTime() != null){
										workTime = DateTime.accountTime3(alarmStartEntity.getGpsTime(), alarmEndEntity.getGpsTime());
									}
								}else{
									log.info("错误:需要计算运营时间的时候，运营开始时间为空1");	
								}
								preEntity = entity;
								
							//	log.info(workTime+"::"+restTime+"step32");
								
								
						}else{
							//休息 转为 休息 累计休息时间
							//System.out.println("23");
							if(entity != null && entity.getGpsTime() != null && restStartEntity != null && restStartEntity.getGpsTime() != null){
								restTime = DateTime.accountTime3(restStartEntity.getGpsTime(), entity.getGpsTime());
							}else{
								log.info("错误:需要计算休息时间的时候，休息开始时间为空2"+restStartEntity == null);	
							}
							
							if(workTime > _4Hour/1000 && restTime >= _10Minute*2/1000){
								//记录一次报警
								log.info(workTime+"::"+restTime+"step23");
								alarmEndEntity = restStartEntity;
								FatigueAlarmEntity fatigueAlarmEntity = new FatigueAlarmEntity.Builder()
									.licence(entity.getLicense())
									.licenceColor("")
									.alarmStartTime(DateTime.getDateTimeString(alarmStartEntity.getGpsTime()))
									.alarmEndTime(DateTime.getDateTimeString(alarmEndEntity.getGpsTime()))
									.pointCount("")
									.create();

								fatigueAlarmMap.put(fatigueAlarmEntity.getLicence()+fatigueAlarmEntity.getAlarmStartTime(), fatigueAlarmEntity);
								
								alarmStartEntity = null;
								alarmEndEntity = null;
								
								preEntity = null;
								
								restStartEntity = null;
								workTime = 0;
								restTime = 0;							
							}else if(workTime <= _4Hour/1000 && restTime >= _10Minute*2/1000){
								
								preEntity = null;
								restTime = 0;
								workTime = 0;
								restStartEntity = null;
								alarmStartEntity = null;
								
							}else{
								preEntity = entity;
							}
						}
					}
				}// end iswork if else 
			}//end valid
			//preEntity = entity;
			
		}// end for
		log.info("FatigueAlarmAnalysis :"  +  entity.getLicense() + " :: time :: " + yyyyMMdd+ ":::" +fatigueAlarmMap.size() );
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
		
		
}
