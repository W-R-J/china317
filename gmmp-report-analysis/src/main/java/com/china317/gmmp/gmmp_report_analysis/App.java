package com.china317.gmmp.gmmp_report_analysis;

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

import com.china317.gmmp.gmmp_report_analysis.bo.Rule;
import com.china317.gmmp.gmmp_report_analysis.bo.Vehicle;
import com.china317.gmmp.gmmp_report_analysis.bo.VehicleLocate;
import com.china317.gmmp.gmmp_report_analysis.dao.VehicleDao;
import com.china317.gmmp.gmmp_report_analysis.dao.VehicleLocateDao;
import com.china317.gmmp.gmmp_report_analysis.dao.imp.VehicleLocateDaoImp;
import com.china317.gmmp.gmmp_report_analysis.processor.AreaAddProcessor;
import com.china317.gmmp.gmmp_report_analysis.service.PtmAnalysis;
import com.china317.gmmp.gmmp_report_analysis.service.imp.PtmAnalysisImp;
import com.china317.gmmp.gmmp_report_analysis.util.PolyUtilityE2;

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
    	String yyyyMMdd = "20150215";
    	String businessType = "2";
    	if(args!=null&&args.length>0){
    		yyyyMMdd=args[0];
    	}
    	if(args!=null&&args.length>1){
    		businessType=args[1];
    	}
    	System.out.println("[classpath]"+System.getProperty("java.class.path"));//系统的classpaht路径
    	System.out.println("[path]"+System.getProperty("user.dir"));//用户的当前路径
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
    	
    	Map<String,Vehicle> vehMap = new HashMap<String, Vehicle>();
    	log.info("[get baseVehicle end---------],vehicle total:"+vehs.size());
    	
    	log.info("[code set init------]");
    	HashSet<String> codes = new HashSet<String>();
    	for(Vehicle v:vehs){
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
    		}
    		log.info("analysis vehicle code:"+key+"OVERSPEED OFFLINE ANALYSIS end");
    		
    		log.info("result: overspeed:"+PtmAnalysisImp.getInstance().getOverSpeedRecordsSize()
    					+"; offline:"+PtmAnalysisImp.getInstance().getOfflineRecordsSize());
    	}
    	
    	log.info("analysis end");
    	
    	log.info("[App main ended]");
    }
}
