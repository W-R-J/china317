package com.china317.gmmp.gmmp_report_analysis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.china317.gmmp.gmmp_report_analysis.bo.AlarmMore;
import com.china317.gmmp.gmmp_report_analysis.bo.AlarmNoMark;
import com.china317.gmmp.gmmp_report_analysis.bo.PtmOverSpeed;
import com.china317.gmmp.gmmp_report_analysis.bo.Rule;
import com.china317.gmmp.gmmp_report_analysis.bo.Vehicle;
import com.china317.gmmp.gmmp_report_analysis.bo.VehicleLocate;
import com.china317.gmmp.gmmp_report_analysis.dao.OverSpeedDao;
import com.china317.gmmp.gmmp_report_analysis.dao.VehicleDao;
import com.china317.gmmp.gmmp_report_analysis.dao.VehicleLocateDao;
import com.china317.gmmp.gmmp_report_analysis.dao.imp.VehicleLocateDaoImp;
import com.china317.gmmp.gmmp_report_analysis.processor.AreaAddProcessor;
import com.china317.gmmp.gmmp_report_analysis.service.PtmAnalysis;
import com.china317.gmmp.gmmp_report_analysis.service.imp.BaocheAnalysisImp;
import com.china317.gmmp.gmmp_report_analysis.service.imp.DgmAnalysisImp;
import com.china317.gmmp.gmmp_report_analysis.service.imp.PtmAnalysisImp;
import com.china317.gmmp.gmmp_report_analysis.util.ListUtil;
import com.china317.gmmp.gmmp_report_analysis.util.PolyUtilityE2;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Hello world!
 *
 */
public class App 
{
	
	public static final Log log = LogFactory.getLog(App.class);
    public static void main( String[] args ) throws Exception
    {
    	log.info("[App main started]");
    	String yyyyMMdd = "20150218";
    	String businessType = "2";
    	if(args!=null&&args.length>0){
    		yyyyMMdd=args[0];
    	}
    	if(args!=null&&args.length>1){
    		businessType=args[1];
    	}
    	//System.out.println("[classpath]"+System.getProperty("java.class.path"));//系统的classpaht路径
    	//System.out.println("[path]"+System.getProperty("user.dir"));//用户的当前路径
    	log.info("[Spring init begin-------------]");
    	//ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
    	ApplicationContext context = new FileSystemXmlApplicationContext("//home/gmmp/repAnalysis/bin/applicationcontext.xml");
    	log.info("[Spring init end-------------]");
    	
    	log.info("AreaAddProcessor areaList init begin");
    	AreaAddProcessor.init(context);
    	PolyUtilityE2.getRules();
    	List<Rule> list_rules = AreaAddProcessor.getRuleList();
    	log.info("AreaAddProcessor areaList init end:"+list_rules.size()+"[PolyUtilityE2 rules:]"+PolyUtilityE2.getMapSize());
    	
    	log.info("[get baseVehicle begin---------]");
    	VehicleDao vehicleDao = (VehicleDao) context.getBean("vehicleDao");
    	List<Vehicle> vehs = vehicleDao.getBaseVehicleByDate(yyyyMMdd, businessType);
    	
    	List<List<Vehicle>> list_tm = ListUtil.splitList(vehs, 17);
    	log.info("[get baseVehicle end1---------],vehicle total:"+vehs.size());
    	log.info("[get baseVehicle end2---------],list_tm total:"+list_tm.size());
    	for(List<Vehicle> vls:list_tm){
    		Map<String,Vehicle> vehMap = new HashMap<String, Vehicle>();
        	
        	log.info("[code set init------]");
        	HashSet<String> codes = new HashSet<String>();
        	for(Vehicle v:vls){
        		codes.add(v.getCode());
        		vehMap.put(v.getCode(), v);
        	}

        	log.info("[code set end------]"+"setSize:"+vehMap.size());
        	List<VehicleLocate> list = new ArrayList<VehicleLocate>();
        	if(codes.size() > 0){
        		VehicleLocateDao vehicleLocateDao_gmmpraw = (VehicleLocateDao) context.getBean("vehicleLocateDaoGmmpRaw");
            	list = vehicleLocateDao_gmmpraw.findHistoryByParams(yyyyMMdd, codes);
            	log.info("[this time， total Points Size]:"+list.size());
        	}
        	
        	Map<String,List<VehicleLocate>> map = new HashMap<String,List<VehicleLocate>>();
        	for(VehicleLocate entity:list){
        		//先更新businessType
        		Vehicle tmpV = vehMap.get(entity.getCode());
        		entity.setBusinessType(tmpV.getBusinessType());
        		List<VehicleLocate> records = map.get(entity.getCode());
        		if(records==null){
        			records = new ArrayList<VehicleLocate>();
        		}
        		records.add(entity);
        		map.put(entity.getCode(),records);
        	}
        	
        	log.info("analysis begin ,total:"+map.size());
        	
        	Iterator<String> it = map.keySet().iterator();
        	while(it.hasNext()){
        		String key = it.next();
        		List<VehicleLocate> tmps = map.get(key);
        		log.info("analysis vehicle code:"+key+"sort list begin, list size:"+tmps.size());
        		Collections.sort(tmps, new Comparator<VehicleLocate>() {
        			public int compare(VehicleLocate o1,
        					VehicleLocate o2) {
        				Date d1 = o1.getGpsTime();
        				Date d2 = o2.getGpsTime();
        				if(d1.after(d2)){
        					return 1;
        				} else if(d1.before(d2)){
        					return -1;
        				} else {
        					return 0;
        				}
        			}
        		});
        		log.info("analysis vehicle code:"+key+"sort list end");
        		
        		log.info("analysis vehicle code:"+key+"OVERSPEED OFFLINE ANALYSIS begin");
        		for(int i = 0; i < tmps.size(); i++){
        			VehicleLocate e = tmps.get(i);
        			AreaAddProcessor.addAreaRuleInfo(e);
        			log.info("[vehcilelocate properties]"+e.getCode()
        					+"; gpstime:"+e.getGpsTime()
        					+"; gpsSpeed:"+e.getGpsSpeed()
        					+"; businessType:"+e.getBusinessType()
        					+"; lon:"+e.getLon()
        					+"; lat:"+e.getLat()
        					+"; acc:"+e.getACCState()
        					);
        			PtmAnalysisImp.getInstance().overSpeedAnalysis(e);
        			PtmAnalysisImp.getInstance().offlineAnalysis(e, yyyyMMdd);
        			
        			
        			//最后更新
        			PtmAnalysisImp.getInstance().putLastRecord(e);
        		}
        		log.info("analysis vehicle code:"+key+"OVERSPEED OFFLINE ANALYSIS end");
        		
        		log.info("result: overspeed:"+PtmAnalysisImp.getInstance().getOverSpeedRecordsSize()
        					+"; offline:"+PtmAnalysisImp.getInstance().getOfflineRecordsSize());
        	}
    	}
    	
    	
    	
    	log.info("analysis end");
    	OverSpeedRecordsStoreIntoDB(PtmAnalysisImp.getInstance().getOverSpeedRecords(),context);
    	log.info("[Ptm ended]");
    	
    	analysisBaoChe(yyyyMMdd);
    	
    	analysisDgm(yyyyMMdd);
    	
    	log.info("[App ended]");
    }
    
    private static void analysisDgm(String yyyyMMdd) throws Exception{


    	log.info("[Dgm App started]");
    	String businessType = "1";
    	//System.out.println("[classpath]"+System.getProperty("java.class.path"));//系统的classpaht路径
    	//System.out.println("[path]"+System.getProperty("user.dir"));//用户的当前路径
    	log.info("[Dgm Spring init begin-------------]");
    	//ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
    	ApplicationContext context = new FileSystemXmlApplicationContext("//home/gmmp/repAnalysis/bin/applicationcontext.xml");
    	log.info("[Dgm Spring init end-------------]");
    	
    	log.info("AreaAddProcessor areaList init begin");
    	AreaAddProcessor.init(context);
    	PolyUtilityE2.getRules();
    	List<Rule> list_rules = AreaAddProcessor.getRuleList();
    	log.info("AreaAddProcessor areaList init end:"+list_rules.size()+"[Dgm PolyUtilityE2 rules:]"+PolyUtilityE2.getMapSize());
    	
    	log.info("[Dgm get baseVehicle begin---------]");
    	VehicleDao vehicleDao = (VehicleDao) context.getBean("vehicleDao");
    	List<Vehicle> vehs = vehicleDao.getBaseVehicleByDate(yyyyMMdd, businessType);
    	
    	List<List<Vehicle>> list_tm = ListUtil.splitList(vehs, 17);
    	log.info("[Dgm get baseVehicle end1---------],vehicle total:"+vehs.size());
    	log.info("[Dgm get baseVehicle end2---------],list_tm total:"+list_tm.size());
    	for(List<Vehicle> vls:list_tm){
    		Map<String,Vehicle> vehMap = new HashMap<String, Vehicle>();
        	
        	log.info("[Dgm code set init------]");
        	HashSet<String> codes = new HashSet<String>();
        	for(Vehicle v:vls){
        		codes.add(v.getCode());
        		vehMap.put(v.getCode(), v);
        	}

        	log.info("[Dgm code set end------]"+"setSize:"+vehMap.size());
        	List<VehicleLocate> list = new ArrayList<VehicleLocate>();
        	if(codes.size() > 0){
        		VehicleLocateDao vehicleLocateDao_gmmpraw = (VehicleLocateDao) context.getBean("vehicleLocateDaoGmmpRaw");
            	list = vehicleLocateDao_gmmpraw.findHistoryByParams(yyyyMMdd, codes);
            	log.info("[Dgm this time， total Points Size]:"+list.size());
        	}
        	
        	Map<String,List<VehicleLocate>> map = new HashMap<String,List<VehicleLocate>>();
        	for(VehicleLocate entity:list){
        		//先更新businessType
        		Vehicle tmpV = vehMap.get(entity.getCode());
        		entity.setBusinessType(tmpV.getBusinessType());
        		List<VehicleLocate> records = map.get(entity.getCode());
        		if(records==null){
        			records = new ArrayList<VehicleLocate>();
        		}
        		records.add(entity);
        		map.put(entity.getCode(),records);
        	}
        	
        	log.info("analysis begin ,total:"+map.size());
        	
        	Iterator<String> it = map.keySet().iterator();
        	while(it.hasNext()){
        		String key = it.next();
        		List<VehicleLocate> tmps = map.get(key);
        		log.info("analysis vehicle code:"+key+"sort list begin, list size:"+tmps.size());
        		Collections.sort(tmps, new Comparator<VehicleLocate>() {
        			public int compare(VehicleLocate o1,
        					VehicleLocate o2) {
        				Date d1 = o1.getGpsTime();
        				Date d2 = o2.getGpsTime();
        				if(d1.after(d2)){
        					return 1;
        				} else if(d1.before(d2)){
        					return -1;
        				} else {
        					return 0;
        				}
        			}
        		});
        		log.info("analysis vehicle code:"+key+"sort list end");
        		
        		log.info("analysis vehicle code:"+key+"OVERSPEED OFFLINE ANALYSIS begin");
        		for(int i = 0; i < tmps.size(); i++){
        			VehicleLocate e = tmps.get(i);
        			AreaAddProcessor.addAreaRuleInfo(e);
        			log.info("[Dgm vehcilelocate properties]"+e.getCode()
        					+"; gpstime:"+e.getGpsTime()
        					+"; gpsSpeed:"+e.getGpsSpeed()
        					+"; businessType:"+e.getBusinessType()
        					+"; lon:"+e.getLon()
        					+"; lat:"+e.getLat()
        					+"; acc:"+e.getACCState()
        					);
        			DgmAnalysisImp.getInstance().overSpeedAnalysis(e);
        			DgmAnalysisImp.getInstance().offlineAnalysis(e, yyyyMMdd);
        			DgmAnalysisImp.getInstance().fobiddenAnalysis(e, i,tmps.size(),yyyyMMdd);
        			
        			
        			//最后更新
        			DgmAnalysisImp.getInstance().putLastRecord(e);
        		}
        		log.info("analysis vehicle code:"+key+"OVERSPEED OFFLINE ANALYSIS end");
        		
        		log.info("result: overspeed:"+PtmAnalysisImp.getInstance().getOverSpeedRecordsSize()
        					+"; offline:"+PtmAnalysisImp.getInstance().getOfflineRecordsSize());
        	}
    	}
    	
    	
    	
    	log.info("analysis end");
    	OverSpeedRecordsStoreIntoDB(PtmAnalysisImp.getInstance().getOverSpeedRecords(),context);
    	log.info("[Dgm ended]");
    
		
	
    }
    
	private static void analysisBaoChe(String yyyyMMdd) throws Exception {

    	log.info("[Baoche App started]");
    	String businessType = "3";
    	//System.out.println("[classpath]"+System.getProperty("java.class.path"));//系统的classpaht路径
    	//System.out.println("[path]"+System.getProperty("user.dir"));//用户的当前路径
    	log.info("[Baoche Spring init begin-------------]");
    	//ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
    	ApplicationContext context = new FileSystemXmlApplicationContext("//home/gmmp/repAnalysis/bin/applicationcontext.xml");
    	log.info("[Baoche Spring init end-------------]");
    	
    	log.info("AreaAddProcessor areaList init begin");
    	AreaAddProcessor.init(context);
    	PolyUtilityE2.getRules();
    	List<Rule> list_rules = AreaAddProcessor.getRuleList();
    	log.info("AreaAddProcessor areaList init end:"+list_rules.size()+"[Baoche PolyUtilityE2 rules:]"+PolyUtilityE2.getMapSize());
    	
    	log.info("[Baoche get baseVehicle begin---------]");
    	VehicleDao vehicleDao = (VehicleDao) context.getBean("vehicleDao");
    	List<Vehicle> vehs = vehicleDao.getBaseVehicleByDate(yyyyMMdd, businessType);
    	
    	List<List<Vehicle>> list_tm = ListUtil.splitList(vehs, 17);
    	log.info("[Baoche get baseVehicle end1---------],vehicle total:"+vehs.size());
    	log.info("[Baoche get baseVehicle end2---------],list_tm total:"+list_tm.size());
    	for(List<Vehicle> vls:list_tm){
    		Map<String,Vehicle> vehMap = new HashMap<String, Vehicle>();
        	
        	log.info("[Baoche code set init------]");
        	HashSet<String> codes = new HashSet<String>();
        	for(Vehicle v:vls){
        		codes.add(v.getCode());
        		vehMap.put(v.getCode(), v);
        	}

        	log.info("[Baoche code set end------]"+"setSize:"+vehMap.size());
        	List<VehicleLocate> list = new ArrayList<VehicleLocate>();
        	if(codes.size() > 0){
        		VehicleLocateDao vehicleLocateDao_gmmpraw = (VehicleLocateDao) context.getBean("vehicleLocateDaoGmmpRaw");
            	list = vehicleLocateDao_gmmpraw.findHistoryByParams(yyyyMMdd, codes);
            	log.info("[Baoche this time， total Points Size]:"+list.size());
        	}
        	
        	Map<String,List<VehicleLocate>> map = new HashMap<String,List<VehicleLocate>>();
        	for(VehicleLocate entity:list){
        		//先更新businessType
        		Vehicle tmpV = vehMap.get(entity.getCode());
        		entity.setBusinessType(tmpV.getBusinessType());
        		List<VehicleLocate> records = map.get(entity.getCode());
        		if(records==null){
        			records = new ArrayList<VehicleLocate>();
        		}
        		records.add(entity);
        		map.put(entity.getCode(),records);
        	}
        	
        	log.info("analysis begin ,total:"+map.size());
        	
        	Iterator<String> it = map.keySet().iterator();
        	while(it.hasNext()){
        		String key = it.next();
        		List<VehicleLocate> tmps = map.get(key);
        		log.info("analysis vehicle code:"+key+"sort list begin, list size:"+tmps.size());
        		Collections.sort(tmps, new Comparator<VehicleLocate>() {
        			public int compare(VehicleLocate o1,
        					VehicleLocate o2) {
        				Date d1 = o1.getGpsTime();
        				Date d2 = o2.getGpsTime();
        				if(d1.after(d2)){
        					return 1;
        				} else if(d1.before(d2)){
        					return -1;
        				} else {
        					return 0;
        				}
        			}
        		});
        		log.info("analysis vehicle code:"+key+"sort list end");
        		
        		log.info("analysis vehicle code:"+key+"OVERSPEED OFFLINE ANALYSIS begin");
        		for(int i = 0; i < tmps.size(); i++){
        			VehicleLocate e = tmps.get(i);
        			AreaAddProcessor.addAreaRuleInfo(e);
        			log.info("[Baoche vehcilelocate properties]"+e.getCode()
        					+"; gpstime:"+e.getGpsTime()
        					+"; gpsSpeed:"+e.getGpsSpeed()
        					+"; businessType:"+e.getBusinessType()
        					+"; lon:"+e.getLon()
        					+"; lat:"+e.getLat()
        					+"; acc:"+e.getACCState()
        					);
        			BaocheAnalysisImp.getInstance().overSpeedAnalysis(e);
        			BaocheAnalysisImp.getInstance().offlineAnalysis(e, yyyyMMdd);
        			
        			// 线路牌报警分析（无牌出入境、多次出入境）
        			BaocheAnalysisImp.getInstance().xlpAlarmAnalysis(e);
        			
        			
        			//最后更新
        			BaocheAnalysisImp.getInstance().putLastRecord(e);
        		}
        		log.info("analysis vehicle code:"+key+"OVERSPEED OFFLINE ANALYSIS end");
        		
        		log.info("result: overspeed:"+PtmAnalysisImp.getInstance().getOverSpeedRecordsSize()
        					+"; offline:"+PtmAnalysisImp.getInstance().getOfflineRecordsSize());
        	}
    	}
    	
    	
    	
    	log.info("analysis end");
    	// 超速报警
    	OverSpeedRecordsStoreIntoDB(PtmAnalysisImp.getInstance().getOverSpeedRecords(),context);
    	
    	// 无牌出入境报警
    	IntOutNoneRecordsStoreIntoDB(BaocheAnalysisImp.getInstance().getIniOutNoneRecords(),context);
    	 
    	// 多次出入境报警
    	InOutMoreRecordsStoreIntoDB(BaocheAnalysisImp.getInstance().getIniOutMoreRecords(),context);
    	
    	
    	log.info("[Baoche ended]");
    
		
	}

	private static void IntOutNoneRecordsStoreIntoDB(Map<String,AlarmNoMark> iniOutNoneRecords,
			ApplicationContext context) {
		try{
			SqlMapClient sc = (SqlMapClient) context.getBean("sqlMapClientLybc");
			Connection conn = sc.getDataSource().getConnection();
			conn.setAutoCommit(false);
			Statement st = conn.createStatement();
			Iterator<String> it = iniOutNoneRecords.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				AlarmNoMark pos = iniOutNoneRecords.get(key);
				String sql = "insert into TAB_ALARM_NOMARK "
						+ " (LICENCE,BEGIN_TIME,END_TIME,ROAD) "
						+ " values ("
						+ "'" + pos.getLicense() + "',"
						+ "'" + pos.getBeginTime() + "',"
						+ "'" + pos.getEndTime() + "',"
						+ "'" + pos.getRoad() + "')"
						;
				log.info(sql);
				st.addBatch(sql);
			}
			st.executeBatch();
			conn.commit();
			log.info("[insertIntoDB TAB_ALARM_NOMARK success!!!]");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void InOutMoreRecordsStoreIntoDB(Map<String,AlarmMore> iniOutMoreRecords,
			ApplicationContext context) {
		try{
			SqlMapClient sc = (SqlMapClient) context.getBean("sqlMapClientLybc");
			Connection conn = sc.getDataSource().getConnection();
			conn.setAutoCommit(false);
			Statement st = conn.createStatement();
			Iterator<String> it = iniOutMoreRecords.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				AlarmMore pos = iniOutMoreRecords.get(key);
				String sql = "insert into TAB_ALARM_MORE "
						+ " (LICENCE,denoter,fisrt_Time,BEGIN_TIME,END_TIME,road) "
						+ " values ("
						+ "'" + pos.getLicense() + "',"
						+ "'" + pos.getDenoter() + "',"
						+ "'" + pos.getFirstTime() + "',"
						+ "'" + pos.getBeginTime() + "',"
						+ "'" + pos.getEndTime() + "',"
						+ "'" + pos.getRoad() + "')"
						;
				log.info(sql);
				st.addBatch(sql);
			}
			st.executeBatch();
			conn.commit();
			log.info("[insertIntoDB TAB_ALARM_MORE success!!!]");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void OverSpeedRecordsStoreIntoDB(
			Map<String, PtmOverSpeed> overSpeedRecords, ApplicationContext context) {
		try{
			
			SqlMapClient sc = (SqlMapClient) context.getBean("sqlMapClient1");
			Connection conn = sc.getDataSource().getConnection();
			conn.setAutoCommit(false);
			Statement st = conn.createStatement();
			Iterator<String> it = overSpeedRecords.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				PtmOverSpeed pos = overSpeedRecords.get(key);
				String sql = "insert into ALARMOVERSPEED_REA "
						+ " (CODE,LICENSE,LICENSECOLOR,BEGINTIME,ENDTIME,AVGSPEED,MAXSPEED,FLAG,BUSINESSTYPE) "
						+ " values ("
						+ "'" + pos.getCode() + "',"
						+ "'" + pos.getLicense() + "',"
						+ "'" + pos.getLicenseColor() + "',"
						+ "'" + pos.getBeginTime() + "',"
						+ "'" + pos.getEndTIme() + "',"
					    + pos.getAvgSpeed() + ","
						+ pos.getMaxSpeed() + ","
						+ pos.getFlag() + ","
						+ pos.getBusinessType() + ")"
						;
				log.info(sql);
				st.addBatch(sql);
			}
			st.executeBatch();
			conn.commit();
			log.info("[insertIntoDB OverSpeed success!!!]");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
