package com.china317.gmmp.gmmp_report_analysis.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.china317.gmmp.gmmp_report_analysis.bo.RuleResultWrap;
import com.china317.gmmp.gmmp_report_analysis.bo.VehicleLocate;



public class AreaCache {


	protected static final Log log = LogFactory.getLog(AreaCache.class);
	
	
	public static final int AreaIndex_Inner = 0;
	public static final int AreaIndex_Medium = 1;
	public static final int AreaIndex_Outer = 2;
	public static final int AreaIndex_Domestic= 3;
	public static final int AreaIndex_Abroad = 4;
	
	public static final int AreaIndex_Parking = 7;
	public static final int AreaIndex_Inspection = 8;
	public static final int AreaIndex_Maintenance = 9;
	public static final int AreaIndex_cross = 12;
	public static final int AreaIndex_EntryExit = 13;
	
	public static final int AreaIndex_cjsq = 14;
	public static final int AreaIndex_npdq = 15;
	
	public static final int AreaIndex_forbidden = 16;
	public static final int AreaIndex_ypdq = 17;
//	public static final int AreaIndex_lpdq = 16;
//	public static final int AreaIndex_szlsd = 17;
//	public static final int AreaIndex_dplsd = 18;
//	public static final int AreaIndex_xznlsd = 19;
//	public static final int AreaIndex_fxdlsd = 20;
//	public static final int AreaIndex_rmlsd = 21;
//	public static final int AreaIndex_yadlsd = 22;
//	public static final int AreaIndex_xjlsd = 23;
//	public static final int AreaIndex_dllsd = 24;
//	public static final int AreaIndex_jglsd = 25;
//	public static final int AreaIndex_lylsd = 26;
//	public static final int AreaIndex_xylsd = 27;
	
	//itemNames
	protected static final String[] itemNames = {
		"内环以内","中环以内","外环以内","境内","境外"
		,"人民广场监控","世博监控","停车场","检测站","维修站"
		,"人民广场报警","世博报警","道口","出入口","长江隧桥","南浦大桥","禁入区域","杨浦大桥"
		//,"卢浦大桥" ,"上中路隧道","打浦路隧道","西藏南路隧道","复兴东路隧道","人民路隧道"
		//,"延安东路隧道","新建路隧道","大连路隧道","军工路隧道","龙耀路隧道","翔殷路隧道"
		};
	
	
	//CACHE LIST
	//CACHE LIST
	private  Map[] vehCache ;
	
	
	public AreaCache(){
		
		vehCache = new Map[itemNames.length];
		
		for (int i = 0; i < itemNames.length; i++) {
			vehCache[i]= new HashMap();
		}
	};

	public  String printCacheSize(){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < itemNames.length; i++) {
			sb.append("\n"+itemNames[i]+".size: "+vehCache[i].size());
		}
		return sb.toString();
	}
	

	public  List getCacheSet(int index) {
		return new ArrayList(vehCache[index].values());
	}


	
	private  void checkPutEn(VehicleLocate en ){
		clearAllCache(en.getCode());
		
		Set ruleRsSet = en.getRuleRsWrapSet();
		for (Iterator iter = ruleRsSet.iterator(); iter.hasNext();) {
			RuleResultWrap ruleResultWrap = (RuleResultWrap) iter.next();
			int itemIndex = matchName(ruleResultWrap.getRuleName());
			
			if(itemIndex>=0){
				vehCache[itemIndex].put(en.getCode(),en);
			}else{
				log.debug("计算出的区域结果 无法和区域缓存匹配名称."
						+"\nruleId:"+ruleResultWrap.getRuleId()
						+"\nruleName:"+ruleResultWrap.getRuleName());
			}
		}
	}

	private  void clearAllCache(String vehPhone) {
		for (int itemIndex = 0; itemIndex < itemNames.length; itemIndex++) {
			vehCache[itemIndex].remove(vehPhone);
		}
	}


	public static  int matchName(String ruleName) {
		int ret = -1;
		for (int itemIndex = 0; itemIndex < itemNames.length; itemIndex++) {
			boolean flag = matchNames(ruleName,itemNames[itemIndex]);
			if(flag){
				ret = itemIndex;
				break;
			}
		}
		return ret;
	}


	public  void putDgmVehInfo(VehicleLocate en) {
		checkPutEn(en);
	}
	
	public  void putDgmVehInfos(List list) {
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			VehicleLocate en = (VehicleLocate)iterator.next();
			checkPutEn(en);
		}
	}
	

	
	private static  boolean matchNames(String ruleName, String itemName) {
		//return ruleName.indexOf(itemName)>-1;
		return ruleName.equals(itemName);
	}
	
	public static RuleResultWrap matchIndex(Set set,int itemIndex){
		RuleResultWrap ret = null;
		try {
			String itemName =  itemNames[itemIndex];
			for (Iterator iter = set.iterator(); iter.hasNext();) {
				RuleResultWrap en = (RuleResultWrap) iter.next();
				boolean matchFlag = matchNames( en.getRuleName(),itemName);
				if (matchFlag) {
					ret = en;
					break;
				}
			}
		} catch (RuntimeException e) {
			log.warn(e);
		}
		return ret;
	}
	public static void main(String[] args) {
		
	}


	public static final int[] forbiddenAreaIds = {
		10,14,15,16,17,18,19,20,21,22,23,24,25,26,27
		}; 
	
	public static String getItemNames(int areaId){
		return itemNames[areaId];
	}
	
	
}
