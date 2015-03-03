package com.china317.gmmp.gmmp_report_analysis.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.china317.gmmp.gmmp_report_analysis.bo.LineDenoter;
import com.china317.gmmp.gmmp_report_analysis.dao.LineDenoterDao;
import com.china317.gmmp.gmmp_report_analysis.util.LineDenoterCodeUtil;



public class LineDenoterCache {

	private static Map<String, LineDenoter> denoterMap = new HashMap<String, LineDenoter>();
	
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
	 * 根据code获取线路牌信息
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

}
