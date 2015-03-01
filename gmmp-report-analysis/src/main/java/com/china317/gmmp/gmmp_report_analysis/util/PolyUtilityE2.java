package com.china317.gmmp.gmmp_report_analysis.util;

import java.awt.Polygon;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.china317.gmmp.gmmp_report_analysis.bo.Rule;
import com.china317.gmmp.gmmp_report_analysis.bo.RuleResultWrap;
import com.china317.gmmp.gmmp_report_analysis.dao.RuleDao;
import com.ibatis.sqlmap.client.SqlMapClient;


public class PolyUtilityE2 extends Thread 
{
	
	protected static final Log log = LogFactory.getLog(PolyUtilityE2.class);
	
	public static String parkingId = "2008121508473804465";
	public static String inspectionId = "2008121508473804463";
	public static String maintenanceId = "2008121508473804464";
	public static String othersForbiddenAreaId = "20110617134300jrqy";
	public static String npdqAreaId = "2010072313430000001";
	public static String psAreaId = "alarm_ps";
	public static String cjsqAreaId = "2010020913430000001";
	public static String ypdqAreaId = "2010072313430000002";
	public static String shBorderId = "2008121216142800569";
	private static HashMap rules=new HashMap();
	private static Integer lock=new Integer(0);
	
	public static void resetRule(){
		lock=new Integer(0);
	}
	
	//private static ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
	private static ApplicationContext context = new FileSystemXmlApplicationContext("//home/gmmp/repAnalysis/bin/applicationcontext.xml");

//	public static boolean isBreakTheRule(String ruleid,double longtitude,double latitude){
//		PolygonWrap flag = isBreakTheRuleP( ruleid, longtitude, latitude);
//		if(flag!=null) return true;
//		else return false;
//	}
	
	
	public static List matchRuleResult(String ruleId,double longtitude,double latitude){
		List list = new ArrayList();
		synchronized (lock) {
			if (lock.intValue() == 0) {
				getRules();
				lock = new Integer(1);
			}
		}
		int ilongtitude = (int) (longtitude * 1000000);
		int ilatitude = (int) (latitude * 1000000);
		List ruleResultList = (List) rules.get(ruleId);
		for (int i = 0; ruleResultList!=null && i < ruleResultList.size(); i++) {
			RuleResultWrap pw = (RuleResultWrap) ruleResultList.get(i);
			if("0".equals(pw.getIsAreaIn())){
				if (pw.getPg().contains(ilongtitude, ilatitude) ) {
					list.add(pw);
					
				}
			}
			//"1"��ʾ�������ⱨ�� ��0�෴�߼�
			if("1".equals(pw.getIsAreaIn())){
				if (!pw.getPg().contains(ilongtitude, ilatitude)) {
					list.add(pw);
				}
			}
		}
		
		return list;
		
	}
	
	public static RuleResultWrap isBreakTheRule(String ruleid,double longtitude,double latitude)
	{
		synchronized (lock) {
			if (lock.intValue() == 0) {
				getRules();
				lock = new Integer(1);
			}
		}
		int ilongtitude = (int) (longtitude * 1000000);
		int ilatitude = (int) (latitude * 1000000);

		List list = (List) rules.get(ruleid);
		if (list == null)
			return null;

		RuleResultWrap returnEn = null;
		RuleResultWrap bakEn = null;
		for (int i = 0; i < list.size(); i++) {
			RuleResultWrap pw = (RuleResultWrap) list.get(i);
			
			if("0".equals(pw.getIsAreaIn())){
				if (pw.getPg().contains(ilongtitude, ilatitude) ) {
					bakEn=pw;
					//if(pw.getCorpCode()!=null && pw.getCorpCode().equals(corpCode)){
						
						returnEn = pw;
						break;
					//}
					
				}
			}
			//"1"��ʾ�������ⱨ�� ��0�෴�߼�
			if("1".equals(pw.getIsAreaIn())){
				if (!pw.getPg().contains(ilongtitude, ilatitude)) {
					returnEn = pw;
					break;
				}
			}
		}
		if(returnEn==null){
			returnEn = bakEn;
		}
//		if(returnEn!=null&&returnEn.getPolygonName().equals("�ڻ�")){
//			log.info("********************�ڻ�\t"+returnEn.getRuleId());
//		}
		return  returnEn;
	}
	
	public static RuleResultWrap confimeDk(double longtitude,double latitude){
		String ruleid = "2012070414552300002";
		synchronized (lock) {
			if (lock.intValue() == 0) {
				getRules();
				lock = new Integer(1);
			}
		}
		int ilongtitude = (int) (longtitude * 1000000);
		int ilatitude = (int) (latitude * 1000000);

		List list = (List) rules.get(ruleid);
		if (list == null) {
			return null;
		}
		//log.info(list.size());
		for (int i = 0; i < list.size(); i++) {
			RuleResultWrap pw = (RuleResultWrap) list.get(i);
			//log.info(pw.getPolygonName());
			if (pw.getPg().contains(ilongtitude, ilatitude)) {
				return pw;
			}
		}
		return null;
	}
	
	/**
	 * �ж�ͨ���ĸ��Ϻ��߾�·��
	 * @param longtitude
	 * @param latitude
	 * @return
	 */
	public static RuleResultWrap isBreakSHBorder(double longtitude,double latitude){
		String ruleid = "2012070414552300001";
		synchronized (lock) {
			if (lock.intValue() == 0) {
				getRules();
				lock = new Integer(1);
			}
		}
		
		List list = (List) rules.get(ruleid);
		if (list == null)
			return null;

		RuleResultWrap returnEn = null;
		for (int i = 0; i < list.size(); i++) {
			RuleResultWrap pw = (RuleResultWrap) list.get(i);
			
			if("0".equals(pw.getIsAreaIn())){
				int x = pw.getPg().xpoints[0];
				int y = pw.getPg().ypoints[0];
				
				long  distance  = MapUtil.distance(x/1000000.0,y/1000000.0, longtitude, latitude);
				if(distance<500*100){
					returnEn=pw;
				}
				
			}
		}
		
		return  returnEn;
	}
	
	public static RuleResultWrap isNearestSHBorder(double longtitude1,double latitude1,double longtitude2,double latitude2){
		String ruleid = "2012070414552300001";
		synchronized (lock) {
			if (lock.intValue() == 0) {
				getRules();
				lock = new Integer(1);
			}
		}
		
		List list = (List) rules.get(ruleid);
		if (list == null)
			return null;

		RuleResultWrap returnEn = null;
		double minDis = 0;
		for (int i = 0; i < list.size(); i++) {
			RuleResultWrap pw = (RuleResultWrap) list.get(i);
			
			if("0".equals(pw.getIsAreaIn())){
				int x = pw.getPg().xpoints[0];
				int y = pw.getPg().ypoints[0];
				
				
				double middle_log = (longtitude1+longtitude2)/2;
				double middle_lat = (latitude1+latitude2)/2;
				//long  a  = MapUtil.distance(x/1000000.0,y/1000000.0, longtitude1, latitude1);
				//long  b  = MapUtil.distance(x/1000000.0,y/1000000.0, longtitude2, latitude2);
				//long  c  = MapUtil.distance(longtitude1,latitude1, latitude2, latitude2);
				
				//double pg_area = 0.25 * Math.sqrt((a+b+c)*(a+b-c)*(a+c-b)*(b+c-a));
				
				//double distinct = pg_area*2/c;
				double distinct = MapUtil.distance(x/1000000.0,y/1000000.0, middle_log, middle_lat);
				
				if(minDis>0){
					minDis = Math.min(minDis, distinct);
				} else {
					minDis = distinct;
				}
				if(minDis==distinct){
					returnEn = pw;
				}
				
			}
		}
		
		return  returnEn;
	}
	
	public static int getMapSize(){
		return rules.size();
	}
	
	public static void getRules()
	{

		SqlMapClient sqlMapClient = (SqlMapClient) context.getBean("sqlMapClientDgm");
		Connection conn = null;
		try {
			conn=sqlMapClient.getDataSource().getConnection();
			//Connection conn = ConnectionPool.getConnection();
			Statement stmt = conn.createStatement();
			String sql="select r.AREA_IN, r.ID rid,r.Name,p.NAME as polygonName,p.corp_code as corpCode,pp.* from tab_rule r,tab_rule_area_rel ra,tab_rule_area a,tab_rule_area_polygon p,tab_rule_area_polygon_point pp ";
			sql+=" where r.id=ra.rule_id and ra.area_id=a.id and a.id=p.area_id and p.id=pp.polygon_id  ";
			
			//debug
			//sql+=" and (r.id = '2008121217373800005' or r.id = '2008121217373800004')";
			
			sql+=" order by r.id,pp.polygon_id,order_code ";
			log.info("[sql:]"+sql);
			ResultSet rs=stmt.executeQuery(sql);
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
		} catch (Exception e) {
			log.error(e);
		}finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	
	public static RuleResultWrap isBreakSHCrossing(double longtitude,
			double latitude) {
		String ruleid = "2012070414552300001";
		synchronized (lock) {
			if (lock.intValue() == 0) {
				getRules();
				lock = new Integer(1);
			}
		}
		
		List list = (List) rules.get(ruleid);
		if (list == null)
			return null;

		RuleResultWrap returnEn = null;
		for (int i = 0; i < list.size(); i++) {
			RuleResultWrap pw = (RuleResultWrap) list.get(i);
			
			if("0".equals(pw.getIsAreaIn())){
				int x = pw.getPg().xpoints[0];
				int y = pw.getPg().ypoints[0];
				
				long  distance  = MapUtil.distance(x/1000000.0,y/1000000.0, longtitude, latitude);
				if(distance<500*100){
					returnEn=pw;
				}
				
			}
		}
		
		return  returnEn;
	}

}

