package com.china317.gmmp.gmmp_report_analysis.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.china317.gmmp.gmmp_report_analysis.bo.LineDenoter;
import com.china317.gmmp.gmmp_report_analysis.bo.LineDenoterCount;
import com.china317.gmmp.gmmp_report_analysis.bo.VehicleLocate;
import com.china317.gmmp.gmmp_report_analysis.dao.LineDenoterDao;
import com.china317.gmmp.gmmp_report_analysis.util.DateTime;
import com.china317.gmmp.gmmp_report_analysis.util.LineDenoterCodeUtil;



public class LineDenoterCache {
	
	private static LineDenoterCache instance = new LineDenoterCache();

	private static Map<String, LineDenoter> denoterMap = new HashMap<String, LineDenoter>();
	private static Map<String,LineDenoter> xplMap = new HashMap<String, LineDenoter>();
	private static Map<String,LineDenoterCount> numMap = new HashMap<String, LineDenoterCount>();
	private static boolean HASUPDATE;
	
	public static LineDenoterCache getInstance(){
		return instance;
	}
	
	private LineDenoterCache(){
		init();
	}
	
	private void init() {
		xplMap.clear();
		numMap.clear();
		
    	ApplicationContext context = new FileSystemXmlApplicationContext("//home/gmmp/repAnalysis/bin/applicationcontext.xml");
    	LineDenoterDao dao = (LineDenoterDao) context.getBean("lineDenoterDao");
    	String yyyyMMdd = DateTime.getSysTime(DateTime.PATTERN_7);
		List<LineDenoter> list = dao.getLineDenoterByDate(yyyyMMdd);
		for(LineDenoter denoter : list){
			xplMap.put(denoter.getLicenseCard(), denoter);
			LineDenoterCount c =  new LineDenoterCount();
			c.setLabelNumber(denoter.getLabelNumber());
			numMap.put(denoter.getLabelNumber(),c);
		}
		HASUPDATE = true;
	}

	/**
	 * 获取某日历史数据
	 * @param yyyyMMdd
	 */
	public void LineDenoterRm(String yyyyMMdd) {
		try{
			denoterMap.clear();
			
	    	ApplicationContext context = new FileSystemXmlApplicationContext("//home/gmmp/repAnalysis/bin/applicationcontext.xml");
	    	LineDenoterDao dao = (LineDenoterDao) context.getBean("lineDenoterDao");
			List<LineDenoter> list = dao.getLineDenoterByDate(yyyyMMdd);
			for(LineDenoter denoter : list){
				String mapKey = LineDenoterCodeUtil.getVehicleCode(denoter.getLicenseCard(), denoter.getColor());
				denoterMap.put(mapKey, denoter);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据vehicleCode获取线路牌信息
	 * @param vehicleCode
	 * @return
	 */
	public LineDenoter getLineDenoter(String vehicleCode){
		LineDenoter lineDenoter = null;
		if(denoterMap.containsKey(vehicleCode)){
			lineDenoter = denoterMap.get(vehicleCode);
		}
		return lineDenoter;
	}
	
	/**
	 * 根据license获取线路牌信息
	 * @param license
	 * @return
	 */
	public LineDenoter getLineDenoterByLicense(String license){
		LineDenoter lineDenoter = null;
		if(xplMap.containsKey(license)){
			lineDenoter = xplMap.get(license);
		}
		return lineDenoter;
	}
	
	/**
	 * 根据labelNumber获取线路牌num
	 * @param labelNumber
	 * @return
	 */
	public LineDenoterCount getLineDenoterNumByLabel(String labelNumber){
		if(HASUPDATE){
			
		} else {
			init();
		}
		LineDenoterCount count = null;
		if(numMap.containsKey(labelNumber)){
			count = numMap.get(labelNumber);
		}
		return count;
	}

	public void addNum(String labelNumber,VehicleLocate entity){
		if(HASUPDATE){
			
		} else {
			init();
		}
		synchronized (numMap) {
			if(numMap.containsKey(labelNumber)){
				LineDenoterCount num = numMap.get(labelNumber);
				if(num.getOutNum()==0){
					num.setFirstTime(DateTime.getDateTimeString(entity.getGpsTime(),DateTime.PATTERN_0));
				}
				num.setOutNum(num.getOutNum()+1);
			} else {
				LineDenoterCount num = new LineDenoterCount();
				num.setLabelNumber(labelNumber);
				num.setOutNum(1);
				num.setFirstTime(DateTime.getDateTimeString(entity.getGpsTime(),DateTime.PATTERN_0));
				numMap.put(labelNumber, num);
			}
		}
	}

}
