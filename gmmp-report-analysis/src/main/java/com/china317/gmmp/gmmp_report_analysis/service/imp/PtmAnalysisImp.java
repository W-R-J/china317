package com.china317.gmmp.gmmp_report_analysis.service.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.china317.gmmp.gmmp_report_analysis.bo.PtmOffline;
import com.china317.gmmp.gmmp_report_analysis.bo.PtmOverSpeed;
import com.china317.gmmp.gmmp_report_analysis.bo.VehicleLocate;
import com.china317.gmmp.gmmp_report_analysis.service.PtmAnalysis;
import com.china317.gmmp.gmmp_report_analysis.util.DateTime;

public class PtmAnalysisImp implements PtmAnalysis {

	private static final Log log = LogFactory.getLog(PtmAnalysisImp.class);
	
	private static PtmAnalysisImp instance = new PtmAnalysisImp();
	private PtmAnalysisImp(){
		
	}
	public static PtmAnalysisImp getInstance(){
		return instance;
	}
	
	public static Map<String,PtmOverSpeed> overSpeedingMap = new HashMap<String, PtmOverSpeed>();
	public static Map<String,PtmOverSpeed> overSpeedendMap = new HashMap<String, PtmOverSpeed>();
	
	public static Map<String,VehicleLocate> lastRecordMap = new HashMap<String, VehicleLocate>();
	public static Map<String,PtmOffline> offlineMap = new HashMap<String, PtmOffline>();
	
	public void clear(){
		overSpeedendMap.clear();
		offlineMap.clear();
	}
	
	public void putLastRecord(VehicleLocate l){
		lastRecordMap.put(l.getCode(),l);
	}
	
	public void overSpeedAnalysis(VehicleLocate entity) {
		try{
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
				log.info("[Ptm_OverSpeed_Analysis],[OverSpeedingMap.Size:]"+overSpeedingMap.size());
				System.out.println("[Ptm_OverSpeed_Analysis],[overSpeedCode:]"+entity.getCode()+"; time"+entity.getGpsTime()+"; speed:"+entity.getGpsSpeed());
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
				overSpeedendMap.put(entity.getCode()+overspeed.getBeginTime(), overspeed);
				//overSpeedQueue.add(overspeed);
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void nonstopAnalysis(List<VehicleLocate> list)  throws Exception{
		//2-5点分析本来就有。所以不需要了。
	}

	public void offlineAnalysis(VehicleLocate entity,String yyyyMMdd) throws Exception {
		
		VehicleLocate preEntity = lastRecordMap.get(entity.getCode());
		if(preEntity==null){
			//与0点进行比较，如果gpsTime超过10min，记录一条从0点开始的掉线记录
			long offLineTime = 0;
			
			Date begin = DateTime.parseDate(yyyyMMdd+"000000",DateTime.PATTERN_0);
			Date end = entity.getGpsTime();
			offLineTime = DateTime.accountTime3(begin, end);
			log.info("[Ptm_Offline_Analysis],[offLineTime:]"+offLineTime);
			if(offLineTime >= 10*60){
				PtmOffline offLine = new PtmOffline();
				//开始时间记录为00,00,00； 结束时间为gpstime
				//key = code + offlinebeginTIme
				instance.offlineMap.put(entity.getCode()+"", offLine);
				log.info("[Ptm_Offline_Analysis],[offlineMap.Size:]"+offlineMap.size());
				log.info("[Ptm_Offline_Analysis],[offlineCode:]"+entity.getCode());
				
			}
			
		}else{
			
			long offLineTime = 0L;
			//计算 gpstime时间差
			Date begin = preEntity.getGpsTime();
			Date end = entity.getGpsTime();
			offLineTime = DateTime.accountTime3(begin, end);
			log.info("[Ptm_Offline_Analysis],[offLineTime:]"+offLineTime);
			if(offLineTime >= 10*60){
				PtmOffline offLine = new PtmOffline();
				//开始时间记录为preEntity的gpsTIme； 结束时间为gpstime
				//key = code + offlinebeginTIme
				instance.offlineMap.put(entity.getCode()+"", offLine);
				log.info("[Ptm_Offline_Analysis],[offlineMap.Size:]"+offlineMap.size());
				log.info("[Ptm_Offline_Analysis],[offlineCode:]"+entity.getCode());
			}
			
		}
		
	}

	public void offlineDisAnalysis(List<VehicleLocate> list)  throws Exception{

		
		
	}
	
	
	public int getOfflineRecordsSize(){
		return instance.offlineMap.size();
	}
	
	public int getOverSpeedRecordsSize(){
		return instance.overSpeedendMap.size();
	}
	
	public Map<String,PtmOverSpeed> getOverSpeedRecords(){
		//return instance.overSpeedendMap;
		//Map<String,PtmOverSpeed> map = new HashMap<String, PtmOverSpeed>();
		//map = overSpeedendMap;
		//overSpeedendMap.clear();
		return overSpeedendMap;
	}
	
	public Map<String,PtmOffline> getOffLineRecords(){
		return instance.offlineMap;
	}

}
