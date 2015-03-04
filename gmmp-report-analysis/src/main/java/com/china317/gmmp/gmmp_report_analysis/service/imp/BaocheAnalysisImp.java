package com.china317.gmmp.gmmp_report_analysis.service.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.china317.gmmp.gmmp_report_analysis.bo.AlarmMore;
import com.china317.gmmp.gmmp_report_analysis.bo.AlarmNoMark;
import com.china317.gmmp.gmmp_report_analysis.bo.LineDenoter;
import com.china317.gmmp.gmmp_report_analysis.bo.LineDenoterCount;
import com.china317.gmmp.gmmp_report_analysis.bo.PtmOffline;
import com.china317.gmmp.gmmp_report_analysis.bo.PtmOverSpeed;
import com.china317.gmmp.gmmp_report_analysis.bo.VehicleLocate;
import com.china317.gmmp.gmmp_report_analysis.cache.AreaCache;
import com.china317.gmmp.gmmp_report_analysis.cache.LineDenoterCache;
import com.china317.gmmp.gmmp_report_analysis.service.BaocheAnalysis;
import com.china317.gmmp.gmmp_report_analysis.util.DateTime;

public class BaocheAnalysisImp implements BaocheAnalysis {

	private static final Log log = LogFactory.getLog(BaocheAnalysisImp.class);
	private static BaocheAnalysisImp instance = new BaocheAnalysisImp();
	private BaocheAnalysisImp(){
		
	}
	public static BaocheAnalysisImp getInstance(){
		return instance;
	}
	
	public static Map<String,PtmOverSpeed> overSpeedingMap = new HashMap<String, PtmOverSpeed>();
	public static Map<String,PtmOverSpeed> overSpeedendMap = new HashMap<String, PtmOverSpeed>();
	
	// 无牌出入境
	public static Map<String,AlarmNoMark> inOutNoneMap = new HashMap<String, AlarmNoMark>();
	
	// 多次出入境
	public static Map<String,AlarmMore> inOutMoreMap = new HashMap<String, AlarmMore>();
	
	
	public static Map<String,VehicleLocate> lastRecordMap = new HashMap<String, VehicleLocate>();
	public static Map<String,PtmOffline> offlineMap = new HashMap<String, PtmOffline>();
	
	public void putLastRecord(VehicleLocate l){
		lastRecordMap.put(l.getCode(),l);
	}
	
	@Override
	public void overSpeedAnalysis(VehicleLocate entity)  throws Exception{
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
				overSpeedendMap.put(entity.getCode()+overspeed.getBeginTime(), overspeed);
			}
		}
	}

	@Override
	public void nonstopAnalysis(List<VehicleLocate> list) {
		
	}

	/**
	 * 线路牌报警
	 * （无牌出入境、多次出入境）
	 */
	@Override
	public void xlpAlarmAnalysis(VehicleLocate entity) {
		VehicleLocate preEntity = lastRecordMap.get(entity.getCode());
		if(preEntity==null){
			 // 上次GPS没有信号，不做处理
		} else {
			if(AreaCache.matchIndex(preEntity.getRuleRsWrapSet(), AreaCache.AreaIndex_Domestic)==null){
				//境外,无需判断
				return;
			} else {
				//境内
				if(AreaCache.matchIndex(preEntity.getRuleRsWrapSet(), AreaCache.AreaIndex_Domestic)!=null){
					//境内
					return;
				} else {
					//境外，表示出境
					LineDenoter lineDenoter = LineDenoterCache.getInstance().getLineDenoterByLicense(entity.getLicense());
					if(lineDenoter != null){
						//有线路牌，需判断多次出入境报警
						LineDenoterCount numEntity = LineDenoterCache.getInstance().getLineDenoterNumByLabel(lineDenoter.getLabelNumber());
						int num = numEntity!=null?numEntity.getOutNum():0;
						if(num<1){
							//第一次允许出境，只需累计次数和记录时间即可
							log.info(entity.getLicense()+"当前线路牌："+lineDenoter.getLabelNumber());
							LineDenoterCache.getInstance().addNum(lineDenoter.getLabelNumber(),entity);
							log.info("有线路牌第一次出境");
						} else {
							//多次出境报警，生成记录，保存数据库
							log.info(entity.getLicense()+"当前线路牌："+lineDenoter.getLabelNumber());
							log.info("有线路牌多次出境");
							LineDenoterCache.getInstance().addNum(lineDenoter.getLabelNumber(),entity);
							
							AlarmMore alarmMore = new AlarmMore();
							alarmMore.setLicense(entity.getLicense());
							alarmMore.setBeginTime(DateTime.getDateTimeString(preEntity.getGpsTime(),DateTime.PATTERN_0));
							alarmMore.setEndTime(DateTime.getDateTimeString(entity.getGpsTime(),DateTime.PATTERN_0));
							alarmMore.setDenoter(lineDenoter.getLabelNumber());
							alarmMore.setFirstTime(numEntity.getFirstTime());
							inOutMoreMap.put(alarmMore.getLicense()+"@"+alarmMore.getDenoter(), alarmMore);
						}
						
					} else {
						log.info("lybc_inOutNone_Analysis");
						//没有线路牌，直接报未领标志牌报警,生成记录，保存数据库
						AlarmNoMark bo = new AlarmNoMark();
						bo.setLicense(entity.getLicense());
						bo.setBeginTime(DateTime.getDateTimeString(entity.getGpsTime(),DateTime.PATTERN_0));
						bo.setEndTime(DateTime.getDateTimeString(entity.getGpsTime(),DateTime.PATTERN_0));
						bo.setRoad("");
						inOutNoneMap.put(bo.getLicense(), bo);
					}
					
				}
			}
		}
	}

	@Override
	public void offlineAnalysis(VehicleLocate entity,String yyyyMMdd) throws Exception {
		
		VehicleLocate preEntity = lastRecordMap.get(entity.getCode());
		if(preEntity==null){
			//与0点进行比较，如果gpsTime超过10min，记录一条从0点开始的掉线记录
			long offLineTime = 0;
			
			Date begin = DateTime.parseDate(yyyyMMdd+"000000",DateTime.PATTERN_0);
			Date end = entity.getGpsTime();
			offLineTime = DateTime.accountTime3(begin, end);
			log.info("[lybc_Offline_Analysis],[offLineTime:]"+offLineTime);
			if(offLineTime >= 10*60*1000){
				PtmOffline offLine = new PtmOffline();
				//开始时间记录为00,00,00； 结束时间为gpstime
				//key = code + offlinebeginTIme
				instance.offlineMap.put(entity.getCode()+"", offLine);
				log.info("[Lybc_Offline_Analysis],[offlineMap.Size:]"+offlineMap.size());
				log.info("[Lybc_Offline_Analysis],[offlineCode:]"+entity.getCode());
				
			}
			
		}else{
			
			long offLineTime = 0L;
			//计算 gpstime时间差
			Date begin = DateTime.parseDate(yyyyMMdd+"000000",DateTime.PATTERN_0);
			Date end = entity.getGpsTime();
			offLineTime = DateTime.accountTime3(begin, end);
			log.info("[Lybc_Offline_Analysis],[offLineTime:]"+offLineTime);
			if(offLineTime >= 10*60*1000){
				PtmOffline offLine = new PtmOffline();
				//开始时间记录为preEntity的gpsTIme； 结束时间为gpstime
				//key = code + offlinebeginTIme
				instance.offlineMap.put(entity.getCode()+"", offLine);
				log.info("[Lybc_Offline_Analysis],[offlineMap.Size:]"+offlineMap.size());
				log.info("[Lybc_Offline_Analysis],[offlineCode:]"+entity.getCode());
			}
			
		}
		
	}

	@Override
	public void offlineDisAnalysis(List<VehicleLocate> list) {
		
	}

	public Map<String,AlarmNoMark> getIniOutNoneRecords() {
		return instance.inOutNoneMap;
	}
	
	public Map<String,AlarmMore>  getIniOutMoreRecords() {
		return instance.inOutMoreMap;
	}

}
