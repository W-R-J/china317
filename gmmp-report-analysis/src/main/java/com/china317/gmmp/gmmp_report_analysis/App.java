package com.china317.gmmp.gmmp_report_analysis;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.china317.gmmp.gmmp_report_analysis.bo.AlarmMore;
import com.china317.gmmp.gmmp_report_analysis.bo.AlarmNoMark;
import com.china317.gmmp.gmmp_report_analysis.bo.DgmEntryExit;
import com.china317.gmmp.gmmp_report_analysis.bo.DgmForbidden;
import com.china317.gmmp.gmmp_report_analysis.bo.DgmIllegalParking;
import com.china317.gmmp.gmmp_report_analysis.bo.FatigueAlarmEntity;
import com.china317.gmmp.gmmp_report_analysis.bo.PtmOverSpeed;
import com.china317.gmmp.gmmp_report_analysis.bo.Rule;
import com.china317.gmmp.gmmp_report_analysis.bo.Vehicle;
import com.china317.gmmp.gmmp_report_analysis.bo.VehicleLocate;
import com.china317.gmmp.gmmp_report_analysis.cache.LineDenoterCache;
import com.china317.gmmp.gmmp_report_analysis.dao.VehicleDao;
import com.china317.gmmp.gmmp_report_analysis.dao.VehicleLocateDao;
import com.china317.gmmp.gmmp_report_analysis.processor.AreaAddProcessor;
import com.china317.gmmp.gmmp_report_analysis.service.imp.BaocheAnalysisImp;
import com.china317.gmmp.gmmp_report_analysis.service.imp.DgmAnalysisImp;
import com.china317.gmmp.gmmp_report_analysis.service.imp.PtmAnalysisImp;
import com.china317.gmmp.gmmp_report_analysis.util.DateTime;
import com.china317.gmmp.gmmp_report_analysis.util.ListUtil;
import com.china317.gmmp.gmmp_report_analysis.util.PolyUtilityE2;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Hello world!
 *
 */
public class App {

	public static final Log log = LogFactory.getLog(App.class);

	/*
	 * public static String[] dates={"20150211","20150212","20150213"
	 * 
	 * ,"20150201","20150202","20150203","20150204","20150205"
	 * ,"20150206","20150207","20150208","20150209","20150210" ,"20150223"
	 * 
	 * ,"20150214","20150215","20150216","20150217","20150218"
	 * ,"20150219","20150220","20150221","20150222"
	 * ,"20150224","20150225","20150226","20150227","20150228"};
	 */

	//public static String[] dates = { "20150201", "20150202", "20150203",
	//		"20150204", "20150205", "20150206", "20150207", "20150208",
	//		"20150209", "20150210" };

	/*public static String[] dates = { "20150221", "20150222", "20150223",
		"20150224", "20150225", "20150226", "20150227", "20150228"
		};*/
	
	 public static String[] dates = { "20150228"};

	public static void main(String[] args) {
		log.info("[App main started]");
		String yyyyMMdd = "20150218";
		String beginTime = "20150211";

		// if (args != null && args.length > 0) {
		// yyyyMMdd = args[0];\
		// beginTime = args[0];
		// }

		log.info("[Spring init begin-------------]");
		// ApplicationContext context = new ClassPathXmlApplicationContext(new
		// String[]{"applicationContext.xml"});
		//ApplicationContext context = new FileSystemXmlApplicationContext(
		//		"//home/gmmp/lybc_reAnalySis/bin/applicationcontext.xml");
		
		 ApplicationContext context = new FileSystemXmlApplicationContext(
		 "//home/gmmp/repAnalysis4/bin/applicationcontext.xml");
		
		/*
		 * ApplicationContext context = new FileSystemXmlApplicationContext(
		 * "//home/gmmp/banche_reAnalysis/bin/applicationcontext.xml");
		 */
		log.info("[Spring init end-------------]");

		log.info("AreaAddProcessor areaList init begin");
		AreaAddProcessor.init(context);
		PolyUtilityE2.getRules();
		List<Rule> list_rules = AreaAddProcessor.getRuleList();
		log.info("AreaAddProcessor areaList init end:" + list_rules.size()
				+ "[PolyUtilityE2 rules:]" + PolyUtilityE2.getMapSize());

		for (int i = 0; i < dates.length; i++) {

			yyyyMMdd = dates[i];
			LineDenoterCache.getInstance().init(yyyyMMdd);
			log.info("LineDenoterCache size:"
					+ LineDenoterCache.getInstance().getSize());
			try {
				// analysisBanche(yyyyMMdd, context);

				// analysisBaoChe(yyyyMMdd, context);

				analysisDgm(yyyyMMdd, context);
			} catch (Exception e) {
				log.error(e);
			}

			log.info("ONE DAY ANALYSIS ENDED:" + yyyyMMdd);

			/*
			 * log.info("[Result:]"); log.info("ptm: offline:" +
			 * PtmAnalysisImp.getInstance().getOfflineRecordsSize() +
			 * "::: overspeed:" +
			 * PtmAnalysisImp.getInstance().getOverSpeedRecordsSize());
			 * 
			 * log.info("lybc: offline:" +
			 * BaocheAnalysisImp.getInstance().getOfflineRecordsSize() +
			 * "::: overspeed:" +
			 * BaocheAnalysisImp.getInstance().getOverSpeedRecordsSize());
			 * 
			 * log.info("dgm: fobbided:" +
			 * DgmAnalysisImp.getInstance().getFobbidedSize() +
			 * " ::: overspeed:" +
			 * DgmAnalysisImp.getInstance().getOverSpeedRecordsSize() +
			 * " ::: getIllegalParking:" +
			 * DgmAnalysisImp.getInstance().getIllegalParking().size() +
			 * " ::: getExitMap:" +
			 * DgmAnalysisImp.getInstance().getExitMap().size() +
			 * " ::: getFatigue:" +
			 * DgmAnalysisImp.getInstance().getFatigueSize() + "::: offline:" +
			 * DgmAnalysisImp.getInstance().getOfflineRecordsSize());
			 */
		}

		log.info("[App ended]");

	}

	private static void analysisBanche(String yyyyMMdd,
			ApplicationContext context) {
		try {
			String businessType = "2";

			// System.out.println("[classpath]"+System.getProperty("java.class.path"));//系统的classpaht路径
			// System.out.println("[path]"+System.getProperty("user.dir"));//用户的当前路径

			log.info("[get baseVehicle begin---------]");
			VehicleDao vehicleDao = (VehicleDao) context.getBean("vehicleDao");
			List<Vehicle> vehs = vehicleDao.getBaseVehicleByDate(yyyyMMdd,
					businessType);

			List<List<Vehicle>> list_tm = ListUtil.splitList(vehs, 400);
			log.info("[get baseVehicle end1---------],vehicle total:"
					+ vehs.size());
			log.info("[get baseVehicle end2---------],list_tm total:"
					+ list_tm.size());
			for (List<Vehicle> vls : list_tm) {
				Map<String, Vehicle> vehMap = new HashMap<String, Vehicle>();

				log.info("[code set init------]");
				HashSet<String> codes = new HashSet<String>();
				for (Vehicle v : vls) {
					codes.add(v.getCode());
					vehMap.put(v.getCode(), v);
				}

				log.info("[code set end------]" + "setSize:" + vehMap.size());
				List<VehicleLocate> list = new ArrayList<VehicleLocate>();
				if (codes.size() > 0) {
					VehicleLocateDao vehicleLocateDao_gmmpraw = (VehicleLocateDao) context
							.getBean("vehicleLocateDaoGmmpRaw");
					list = vehicleLocateDao_gmmpraw.findHistoryByParams(
							yyyyMMdd, codes);
					log.info("[this time， total Points Size]:" + list.size());
				}

				Map<String, List<VehicleLocate>> map = new HashMap<String, List<VehicleLocate>>();
				for (VehicleLocate entity : list) {
					if (entity.getGpsSpeed() < 160) {
						// 先更新businessType
						Vehicle tmpV = vehMap.get(entity.getCode());
						entity.setBusinessType(tmpV.getBusinessType());
						List<VehicleLocate> records = map.get(entity.getCode());
						if (records == null) {
							records = new ArrayList<VehicleLocate>();
						}
						long lastlong = DateTime.accountTime3(
								entity.getGpsTime(), entity.getGetTime());
						if (lastlong <= 10 * 60) {
							records.add(entity);
						}
						map.put(entity.getCode(), records);
					}

				}

				log.info("analysis begin ,total:" + map.size());

				Iterator<String> it = map.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					List<VehicleLocate> tmps = map.get(key);
					log.info("analysis vehicle code:" + key
							+ "sort list begin, list size:" + tmps.size());
					Collections.sort(tmps, new Comparator<VehicleLocate>() {
						public int compare(VehicleLocate o1, VehicleLocate o2) {
							Date d1 = o1.getGpsTime();
							Date d2 = o2.getGpsTime();
							if (d1.after(d2)) {
								return 1;
							} else if (d1.before(d2)) {
								return -1;
							} else {
								return 0;
							}
						}
					});
					log.info("analysis vehicle code:" + key + "sort list end");

					log.info("analysis vehicle code:" + key
							+ "OVERSPEED OFFLINE ANALYSIS begin");
					for (int i = 0; i < tmps.size(); i++) {
						VehicleLocate e = tmps.get(i);
						AreaAddProcessor.addAreaRuleInfo(e);
						/*
						 * log.info("[vehcilelocate properties]" + e.getCode() +
						 * "; gpstime:" + e.getGpsTime() + "; gpsSpeed:" +
						 * e.getGpsSpeed() + "; businessType:" +
						 * e.getBusinessType() + "; lon:" + e.getLon() +
						 * "; lat:" + e.getLat() + "; acc:" + e.getACCState());
						 */
						PtmAnalysisImp.getInstance().overSpeedAnalysis(e);
						// PtmAnalysisImp.getInstance().offlineAnalysis(e,
						// yyyyMMdd);

						// 最后更新
						PtmAnalysisImp.getInstance().putLastRecord(e);
					}

					log.info("analysis vehicle code:" + key
							+ "OVERSPEED OFFLINE ANALYSIS end");

					log.info("result: overspeed:"
							+ PtmAnalysisImp.getInstance()
									.getOverSpeedRecordsSize()
							+ "; offline:"
							+ PtmAnalysisImp.getInstance()
									.getOfflineRecordsSize());
					// PtmAnalysisImp.getInstance().clear();
				}
				// OverSpeedRecordsStoreIntoDB(PtmAnalysisImp.getInstance()
				// .getOverSpeedRecords(), context);
				PtmOverSpeedRecordsStoreIntoDB(PtmAnalysisImp.getInstance()
						.getOverSpeedRecords(), context);
			}

			log.info("analysis end");

			log.info("[Ptm ended]");
		} catch (Exception e) {
			log.error(e);
		}
	}

	private static void analysisDgm(String yyyyMMdd, ApplicationContext context) {
		try {
			log.info("[Dgm App started]");
			String businessType = "1";

			DgmAnalysisImp.getInstance().clear();
			// System.out.println("[classpath]"+System.getProperty("java.class.path"));//系统的classpaht路径
			// System.out.println("[path]"+System.getProperty("user.dir"));//用户的当前路径

			log.info("[Dgm get baseVehicle begin---------]");
			VehicleDao vehicleDao = (VehicleDao) context.getBean("vehicleDao");
			List<Vehicle> vehs = vehicleDao.getBaseVehicleByDate(yyyyMMdd,
					businessType);

			List<List<Vehicle>> list_tm = ListUtil.splitList(vehs, 400);
			log.info("[Dgm get baseVehicle end1---------],vehicle total:"
					+ vehs.size());
			log.info("[Dgm get baseVehicle end2---------],list_tm total:"
					+ list_tm.size());
			for (List<Vehicle> vls : list_tm) {
				Map<String, Vehicle> vehMap = new HashMap<String, Vehicle>();

				log.info("[Dgm code set init------]");
				HashSet<String> codes = new HashSet<String>();
				for (Vehicle v : vls) {
					codes.add(v.getCode());
					vehMap.put(v.getCode(), v);
				}

				log.info("[Dgm code set end------]" + "setSize:"
						+ vehMap.size());
				List<VehicleLocate> list = new ArrayList<VehicleLocate>();
				if (codes.size() > 0) {
					VehicleLocateDao vehicleLocateDao_gmmpraw = (VehicleLocateDao) context
							.getBean("vehicleLocateDaoGmmpRaw");
					list = vehicleLocateDao_gmmpraw.findHistoryByParams(
							yyyyMMdd, codes);
					log.info("[Dgm this time， total Points Size]:"
							+ list.size());
				}

				Map<String, List<VehicleLocate>> map = new HashMap<String, List<VehicleLocate>>();
				for (VehicleLocate entity : list) {
					if (entity.getGpsSpeed() < 160) {
						// 先更新businessType
						Vehicle tmpV = vehMap.get(entity.getCode());
						entity.setBusinessType(tmpV.getBusinessType());
						List<VehicleLocate> records = map.get(entity.getCode());
						if (records == null) {
							records = new ArrayList<VehicleLocate>();
						}
						long lastlong = DateTime.accountTime3(
								entity.getGpsTime(), entity.getGetTime());
						if (lastlong <= 10 * 60) {
							records.add(entity);
						}
						map.put(entity.getCode(), records);
					}
				}

				log.info("analysis begin ,total:" + map.size());

				Iterator<String> it = map.keySet().iterator();
				int index = 0;
				while (it.hasNext()) {
					index++;
					String key = it.next();
					List<VehicleLocate> tmps = map.get(key);
					log.info("[Dgm]" + index + "analysis vehicle code:" + key
							+ "sort list begin, list size:" + tmps.size());
					Collections.sort(tmps, new Comparator<VehicleLocate>() {
						public int compare(VehicleLocate o1, VehicleLocate o2) {
							Date d1 = o1.getGpsTime();
							Date d2 = o2.getGpsTime();
							if (d1.after(d2)) {
								return 1;
							} else if (d1.before(d2)) {
								return -1;
							} else {
								return 0;
							}
						}
					});
					log.info("analysis vehicle code:" + key + "sort list end");

					DgmAnalysisImp.getInstance()
							.fatigueAnalysis(tmps, yyyyMMdd);
					log.info("-------Fatigue Analysis end");

					for (int i = 0; i < tmps.size(); i++) {
						VehicleLocate e = tmps.get(i);
						AreaAddProcessor.addAreaRuleInfo(e);
						/*
						 * log.info("[Dgm vehcilelocate properties]" +
						 * e.getCode() + "; gpstime:" + e.getGpsTime() +
						 * "; gpsSpeed:" + e.getGpsSpeed() + "; businessType:" +
						 * e.getBusinessType() + "; lon:" + e.getLon() +
						 * "; lat:" + e.getLat() + "; acc:" + e.getACCState());
						 */
						DgmAnalysisImp.getInstance().addZeroBegin(e);
						DgmAnalysisImp.getInstance().overSpeedAnalysis(e);
						DgmAnalysisImp.getInstance().offlineAnalysis(e,
								yyyyMMdd);
						DgmAnalysisImp.getInstance().fobiddenAnalysis(e, i,
								tmps.size(), yyyyMMdd);
						DgmAnalysisImp.getInstance().illegalParkingAnalysis(e);
						DgmAnalysisImp.getInstance().illegalInOutAnalysis(e);

						// 最后更新
						DgmAnalysisImp.getInstance().putLastRecord(e);
					}

					log.info("analysis vehicle code:" + key
							+ "OVERSPEED OFFLINE ANALYSIS end");

					log.info("result: overspeed:"
							+ DgmAnalysisImp.getInstance()
									.getOverSpeedRecordsSize()
							+ "; offline:"
							+ DgmAnalysisImp.getInstance()
									.getOfflineRecordsSize());

					// DgmAnalysisImp.getInstance().clear();
				}
				FatigueRecordsStoreIntoDB(DgmAnalysisImp.getInstance()
						.getFatigueMap(), context);
				log.info("--------Fatigue store into DB end");
				// OverSpeedRecordsStoreIntoDB(DgmAnalysisImp.getInstance()
				// .getOverSpeedMap(), context);

				DgmOverSpeedRecordsStoreIntoDB(DgmAnalysisImp.getInstance()
						.getOverSpeedMap(), context);

				DgmEntryExitStoreIntoDB(DgmAnalysisImp.getInstance()
						.getExitMap(), context);
				DgmFobbidenStoreIntoDB(DgmAnalysisImp.getInstance()
						.getForbiddeningMap(), context);
				DgmIllegalParkingStoreIntoDB(DgmAnalysisImp.getInstance()
						.getIllegalParking(), context);
			}

			log.info("analysis end");

			log.info("[Dgm ended]");
		} catch (Exception e) {
			log.error(e);
		}
	}

	private static void analysisBaoChe(String yyyyMMdd,
			ApplicationContext context) throws Exception {
		try {
			log.info("[Baoche App started]");
			String businessType = "3";
			// System.out.println("[classpath]"+System.getProperty("java.class.path"));//系统的classpaht路径
			// System.out.println("[path]"+System.getProperty("user.dir"));//用户的当前路径

			log.info("[Baoche get baseVehicle begin---------]");
			VehicleDao vehicleDao = (VehicleDao) context.getBean("vehicleDao");
			List<Vehicle> vehs = vehicleDao.getBaseVehicleByDate(yyyyMMdd,
					businessType);

			List<List<Vehicle>> list_tm = ListUtil.splitList(vehs, 400);
			log.info("[Baoche get baseVehicle end1---------],vehicle total:"
					+ vehs.size());
			log.info("[Baoche get baseVehicle end2---------],list_tm total:"
					+ list_tm.size());
			for (List<Vehicle> vls : list_tm) {
				Map<String, Vehicle> vehMap = new HashMap<String, Vehicle>();

				log.info("[Baoche code set init------]");
				HashSet<String> codes = new HashSet<String>();
				for (Vehicle v : vls) {
					codes.add(v.getCode());
					vehMap.put(v.getCode(), v);
				}

				log.info("[Baoche code set end------]" + "setSize:"
						+ vehMap.size());
				List<VehicleLocate> list = new ArrayList<VehicleLocate>();
				if (codes.size() > 0) {
					VehicleLocateDao vehicleLocateDao_gmmpraw = (VehicleLocateDao) context
							.getBean("vehicleLocateDaoGmmpRaw");
					list = vehicleLocateDao_gmmpraw.findHistoryByParams(
							yyyyMMdd, codes);
					log.info("[Baoche this time， total Points Size]:"
							+ list.size());
				}

				Map<String, List<VehicleLocate>> map = new HashMap<String, List<VehicleLocate>>();
				for (VehicleLocate entity : list) {

					if (entity.getGpsSpeed() < 160) {
						// 先更新businessType
						Vehicle tmpV = vehMap.get(entity.getCode());
						entity.setBusinessType(tmpV.getBusinessType());
						List<VehicleLocate> records = map.get(entity.getCode());
						if (records == null) {
							records = new ArrayList<VehicleLocate>();
						}
						long lastlong = DateTime.accountTime3(
								entity.getGpsTime(), entity.getGetTime());
						if (lastlong <= 10 * 60) {
							records.add(entity);
						}
						map.put(entity.getCode(), records);
					}

				}

				log.info("analysis begin ,total:" + map.size());

				Iterator<String> it = map.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					List<VehicleLocate> tmps = map.get(key);
					log.info("analysis vehicle code:" + key
							+ "sort list begin, list size:" + tmps.size());
					Collections.sort(tmps, new Comparator<VehicleLocate>() {
						public int compare(VehicleLocate o1, VehicleLocate o2) {
							Date d1 = o1.getGpsTime();
							Date d2 = o2.getGpsTime();
							if (d1.after(d2)) {
								return 1;
							} else if (d1.before(d2)) {
								return -1;
							} else {
								return 0;
							}
						}
					});
					log.info("analysis vehicle code:" + key + "sort list end");

					log.info("analysis vehicle code:" + key
							+ "OVERSPEED OFFLINE ANALYSIS begin");
					for (int i = 0; i < tmps.size(); i++) {
						VehicleLocate e = tmps.get(i);
						AreaAddProcessor.addAreaRuleInfo(e);
						/*
						 * log.info("[Baoche vehcilelocate properties]" +
						 * e.getCode() + "; gpstime:" + e.getGpsTime() +
						 * "; gpsSpeed:" + e.getGpsSpeed() + "; businessType:" +
						 * e.getBusinessType() + "; lon:" + e.getLon() +
						 * "; lat:" + e.getLat() + "; acc:" + e.getACCState());
						 */
						BaocheAnalysisImp.getInstance().overSpeedAnalysis(e);
						// BaocheAnalysisImp.getInstance().offlineAnalysis(e,
						// yyyyMMdd);

						// 线路牌报警分析（无牌出入境、多次出入境）
						BaocheAnalysisImp.getInstance().xlpAlarmAnalysis(e);

						// 最后更新
						BaocheAnalysisImp.getInstance().putLastRecord(e);
					}

					// BaocheAnalysisImp.getInstance().clear();
					log.info("analysis vehicle code:" + key
							+ "OVERSPEED OFFLINE ANALYSIS end");

					log.info("result: overspeed:"
							+ BaocheAnalysisImp.getInstance()
									.getOverSpeedRecordsSize()
							+ "; offline:"
							+ BaocheAnalysisImp.getInstance()
									.getOfflineRecordsSize());

				}
				LybcOverSpeedRecordsStoreIntoDB(BaocheAnalysisImp.getInstance()
						.getOverSpeedRecords(), context);
				// 无牌出入境报警
				IntOutNoneRecordsStoreIntoDB(BaocheAnalysisImp.getInstance()
						.getIniOutNoneRecords(), context);

				// 多次出入境报警
				InOutMoreRecordsStoreIntoDB(BaocheAnalysisImp.getInstance()
						.getIniOutMoreRecords(), context);

			}

			log.info("analysis end");

			log.info("[Baoche ended]");
		} catch (Exception e) {
			log.error(e);
		}
	}

	private static void PtmOverSpeedRecordsStoreIntoDB(
			Map<String, PtmOverSpeed> overSpeedRecords,
			ApplicationContext context) {
		// INSERT INTO
		// TAB_ALARM_OVERSPEED(LICENCE,BEGIN_TIME,END_TIME,SPEED,IS_END,AREA)
		// SELECT LICENSE,BEGINTIME,ENDTIME,AVGSPEED,'1',FLAG FROM
		// ALARMOVERSPEED_REA WHERE BUSINESSTYPE = '2'
		String sql = "";
		Connection conn = null;
		try {
			SqlMapClient sc = (SqlMapClient) context.getBean("sqlMapClientPtm");
			conn = sc.getDataSource().getConnection();
			conn.setAutoCommit(false);
			Statement st = conn.createStatement();
			Iterator<String> it = overSpeedRecords.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				PtmOverSpeed pos = overSpeedRecords.get(key);
				sql = "insert into TAB_ALARM_OVERSPEED "
						+ " (LICENCE,BEGIN_TIME,END_TIME,SPEED,IS_END,AREA) "
						+ " values ('" + pos.getLicense() + "','"
						+ pos.getBeginTime() + "','" + pos.getEndTIme() + "',"
						+ pos.getAvgSpeed() + "," + "1" + "," + pos.getFlag()
						+ ")";
				log.info(sql);
				st.addBatch(sql);
			}
			st.executeBatch();
			conn.commit();
			log.info("[insertIntoDB OverSpeed success!!!]");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(sql);
		} finally {
			overSpeedRecords.clear();
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private static void LybcOverSpeedRecordsStoreIntoDB(
			Map<String, PtmOverSpeed> overSpeedRecords,
			ApplicationContext context) {
		// INSERT INTO
		// TAB_ALARM_OVERSPEED(LICENCE,BEGIN_TIME,END_TIME,SPEED,IS_END,AREA,MAX_SPEED)
		// SELECT LICENSE,BEGINTIME,ENDTIME,AVGSPEED,'1',FLAG,MAXSPEED FROM
		// ALARMOVERSPEED_REA WHERE BUSINESSTYPE = '3'
		String sql = "";

		Connection conn = null;
		try {
			SqlMapClient sc = (SqlMapClient) context
					.getBean("sqlMapClientLybc");
			conn = sc.getDataSource().getConnection();
			conn.setAutoCommit(false);
			Statement st = conn.createStatement();
			Iterator<String> it = overSpeedRecords.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				PtmOverSpeed pos = overSpeedRecords.get(key);
				sql = "insert into TAB_ALARM_OVERSPEED "
						+ " (LICENCE,BEGIN_TIME,END_TIME,SPEED,IS_END,AREA,MAX_SPEED) "
						+ " values ('" + pos.getLicense() + "','"
						+ pos.getBeginTime() + "','" + pos.getEndTIme() + "',"
						+ pos.getAvgSpeed() + "," + "1" + "," + pos.getFlag()
						+ "," + pos.getMaxSpeed() + ")";
				log.info(sql);
				st.addBatch(sql);
			}
			st.executeBatch();
			conn.commit();
			log.info("[insertIntoDB OverSpeed success!!!]");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(sql);
		} finally {
			overSpeedRecords.clear();
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private static void DgmOverSpeedRecordsStoreIntoDB(
			Map<String, PtmOverSpeed> overSpeedRecords,
			ApplicationContext context) {
		// INSERT INTO
		// TAB_GPSEVENT_OVERSPEED(VID,TYPE,,BEGIN_TIME,END_TIME,DETAIL,AREA,MAX_SPEED,MIN_SPEED)
		// SELECT CODE,'overspeed',BEGINTIME,ENDTIME,AVGSPEED,FLAG,MAXSPEED,''
		// FROM ALARMOVERSPEED_REA WHERE BUSINESSTYPE = '1'

		String sql = "";
		Connection conn = null;
		try {
			SqlMapClient sc = (SqlMapClient) context.getBean("sqlMapClientDgm");
			conn = sc.getDataSource().getConnection();
			conn.setAutoCommit(false);
			Statement st = conn.createStatement();
			Iterator<String> it = overSpeedRecords.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				PtmOverSpeed pos = overSpeedRecords.get(key);
				sql = "INSERT INTO TAB_GPSEVENT_OVERSPEED (VID,TYPE,BEGIN_TIME,END_TIME,DETAIL,AREA,MAX_SPEED,MIN_SPEED) "
						+ " values ('"
						+ pos.getCode()
						+ "','"
						+ "overspeed"
						+ "','"
						+ pos.getBeginTime()
						+ "','"
						+ pos.getEndTIme()
						+ "',"
						+ pos.getAvgSpeed()
						+ ","
						+ pos.getFlag()
						+ ","
						+ pos.getMaxSpeed() + "," + 0 + ")";
				log.info(sql);
				st.addBatch(sql);
			}
			st.executeBatch();
			conn.commit();
			log.info("[insertIntoDB OverSpeed success!!!]");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(sql);
		} finally {
			overSpeedRecords.clear();
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private static void OverSpeedRecordsStoreIntoDB(
			Map<String, PtmOverSpeed> overSpeedRecords,
			ApplicationContext context) {
		Connection conn = null;
		String sql = "";
		try {

			SqlMapClient sc = (SqlMapClient) context.getBean("sqlMapClient1");
			conn = sc.getDataSource().getConnection();
			conn.setAutoCommit(false);
			Statement st = conn.createStatement();
			Iterator<String> it = overSpeedRecords.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				PtmOverSpeed pos = overSpeedRecords.get(key);
				sql = "insert into ALARMOVERSPEED_REA "
						+ " (CODE,LICENSE,LICENSECOLOR,BEGINTIME,ENDTIME,AVGSPEED,MAXSPEED,FLAG,BUSINESSTYPE) "
						+ " values (" + "'"
						+ pos.getCode()
						+ "',"
						+ "'"
						+ pos.getLicense()
						+ "',"
						+ "'"
						+ pos.getLicenseColor()
						+ "',"
						+ "'"
						+ pos.getBeginTime()
						+ "',"
						+ "'"
						+ pos.getEndTIme()
						+ "',"
						+ pos.getAvgSpeed()
						+ ","
						+ pos.getMaxSpeed()
						+ ","
						+ pos.getFlag()
						+ ","
						+ pos.getBusinessType()
						+ ")";
				log.info(sql);
				st.addBatch(sql);
			}
			st.executeBatch();
			conn.commit();
			log.info("[insertIntoDB OverSpeed success!!!]");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(sql);
		} finally {
			overSpeedRecords.clear();
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void DgmFobbidenStoreIntoDB(Map<String, DgmForbidden> map,
			ApplicationContext context) {
		/**
		 * INSERT INTO TAB_GPSEVENT_AREA SELECT
		 * CODE,ALARMTYPE,BEGINTIME,ENDTIME,'' FROM ALARMFOBBIDEN_REA
		 */
		Connection conn = null;
		String sql = "";
		try {

			SqlMapClient sc = (SqlMapClient) context.getBean("sqlMapClientDgm");
			conn = sc.getDataSource().getConnection();
			conn.setAutoCommit(false);
			Statement st = conn.createStatement();
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				DgmForbidden pos = map.get(key);
				/*
				 * String sql = "insert into ALARMFOBBIDEN_REA " +
				 * " (CODE,LICENSE,LICENSECOLOR,ALARMTYPE,BEGINTIME,ENDTIME) " +
				 * " values (" + "'" + pos.getCode() + "'," + "'" +
				 * pos.getLicense() + "'," + "'" + pos.getLicenseColor() + "',"
				 * + "'" + pos.getAlarmType() + "'," + "'" + pos.getBeginTime()
				 * + "','" + pos.getEndTime() + "')";
				 */
				sql = "insert into TAB_GPSEVENT_AREA "
						+ " (VID,TYPE,BEGIN_TIME,END_TIME,DETAIL) "
						+ " values (" + "'" + pos.getCode() + "'," + "'"
						+ pos.getAlarmType() + "'," + "'" + pos.getBeginTime()
						+ "'," + "'" + pos.getEndTime() + "'," + "'" + "')";
				log.info(sql);
				st.addBatch(sql);
			}
			st.executeBatch();
			conn.commit();
			log.info("[insertIntoDB DgmForbidden success!!!]");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(sql);
		} finally {
			DgmAnalysisImp.getInstance().getForbiddeningMap().clear();
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void DgmIllegalParkingStoreIntoDB(
			Map<String, DgmIllegalParking> map, ApplicationContext context) {
		/*
		 * INSERT INTO TAB_GPSEVENT_ILLEGALPARKING SELECT
		 * CODE,'illegalParking',BEGIN_TIME,END_TIME,'',FLAG FROM
		 * ALARMILLEGALPARKING_REA
		 */
		Connection conn = null;
		String sql = "";
		try {

			SqlMapClient sc = (SqlMapClient) context.getBean("sqlMapClientDgm");
			conn = sc.getDataSource().getConnection();
			conn.setAutoCommit(false);
			Statement st = conn.createStatement();
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				DgmIllegalParking pos = map.get(key);
				/*
				 * String sql = "insert into TAB_GPSEVENT_ILLEGALPARKING " +
				 * " (CODE,LICENSE,TYPE,BEGINTIME,ENDTIME,FLAG) " + " values ("
				 * + "'" + pos.getCode() + "'," + "'" + pos.getLicense() + "',"
				 * + "'" + pos.getType() + "'," + "'" + pos.getBeginTime() +
				 * "'," + "'" + pos.getEndTime() + "','" + pos.getFlag() + "')";
				 */
				sql = "insert into TAB_GPSEVENT_ILLEGALPARKING "
						+ " (VID,TYPE,BEGIN_TIME,END_TIME,DETAIL,IS_END) "
						+ " values (" + "'" + pos.getCode() + "'," + "'"
						+ pos.getType() + "'," + "'" + pos.getBeginTime()
						+ "'," + "'" + pos.getEndTime() + "'," + "'" + "',"
						+ pos.getFlag() + ")";
				log.info(sql);
				st.addBatch(sql);
			}
			st.executeBatch();
			conn.commit();
			log.info("[insertIntoDB DgmIllegalParking success!!!]");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(sql);
		} finally {
			DgmAnalysisImp.getInstance().getIllegalParking().clear();
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void DgmEntryExitStoreIntoDB(Map<String, DgmEntryExit> map,
			ApplicationContext context) {
		/**
		 * INSERT INTO TAB_GPSEVENT_ILLEGALENTRYEXIT SELECT
		 * CODE,'illegalEntryExit',BEGIN_TIME,END_TIME,DETAIL,ROAD FROM
		 * ALARMILLEGALEXIT_REA
		 */
		String sql = "";
		Connection conn = null;
		try {

			SqlMapClient sc = (SqlMapClient) context.getBean("sqlMapClientDgm");
			conn = sc.getDataSource().getConnection();
			conn.setAutoCommit(false);
			Statement st = conn.createStatement();
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				DgmEntryExit pos = map.get(key);
				/*
				 * String sql = "insert into ALARMILLEGALEXIT_REA " +
				 * " (CODE,TYPE,BEGIN_TIME,END_TIME,DETAIL,ROAD) " + " values ("
				 * + "'" + pos.getCode() + "'," + "'" + pos.getType() + "'," +
				 * "'" + pos.getBegin_time() + "'," + "'" + pos.getEnd_time() +
				 * "'," + "'" + pos.getDetail() + "','" + pos.getRoad() + "')";
				 */
				sql = "insert into TAB_GPSEVENT_ILLEGALENTRYEXIT "
						+ " (VID,TYPE,BEGIN_TIME,END_TIME,DETAIL,ROAD) "
						+ " values (" + "'" + pos.getCode() + "',"
						+ "'illegalEntryExit'," + "'" + pos.getBegin_time()
						+ "'," + "'" + pos.getEnd_time() + "'," + "'"
						+ pos.getDetail() + "','" + pos.getRoad() + "')";
				log.info(sql);
				st.addBatch(sql);
			}
			st.executeBatch();
			conn.commit();
			log.info("[insertIntoDB DgmEntryExit success!!!]");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(sql);
		} finally {
			DgmAnalysisImp.getInstance().getExitMap().clear();
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void FatigueRecordsStoreIntoDB(
			Map<String, FatigueAlarmEntity> map, ApplicationContext context) {
		/**
		 * INSERT INTO TAB_GPSEVENT_FATIGUE SELECT
		 * LICENCE,'',ALARMSTARTTIME,ALARMENDTIME,'' FROM ALARMFATIGUE_REA
		 */
		Connection conn = null;
		String sql = "";
		try {
			log.info("--------store Fatigue");
			SqlMapClient sc = (SqlMapClient) context.getBean("sqlMapClientDgm");
			conn = sc.getDataSource().getConnection();
			conn.setAutoCommit(false);
			Statement st = conn.createStatement();
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				FatigueAlarmEntity pos = map.get(key);
				sql = "insert into TAB_GPSEVENT_FATIGUE "
						+ " (license,license_color,start_time,end_time,pointcount) "
						+ " values (" + "'" + pos.getLicence() + "'," + "'"
						+ pos.getLicenceColor() + "'," + "'"
						+ pos.getAlarmStartTime() + "'," + "'"
						+ pos.getAlarmEndTime() + "'," + "'"
						+ pos.getPointCount() + "')";
				log.info(sql);
				st.addBatch(sql);
			}
			st.executeBatch();
			conn.commit();
			log.info("[insertIntoDB FatigueAlarmEntity success!!!]");
		} catch (Exception e) {
			log.info(e);
			log.error(sql);
		} finally {
			DgmAnalysisImp.getInstance().getFatigueMap().clear();
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void IntOutNoneRecordsStoreIntoDB(
			Map<String, AlarmNoMark> iniOutNoneRecords,
			ApplicationContext context) {
		Connection conn = null;
		String sql = "";
		try {
			SqlMapClient sc = (SqlMapClient) context
					.getBean("sqlMapClientLybc");
			conn = sc.getDataSource().getConnection();
			conn.setAutoCommit(false);
			Statement st = conn.createStatement();
			Iterator<String> it = iniOutNoneRecords.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				AlarmNoMark pos = iniOutNoneRecords.get(key);
				sql = "insert into TAB_ALARM_NOMARK "
						+ " (LICENCE,BEGIN_TIME,END_TIME,ROAD) " + " values ("
						+ "'" + pos.getLicense() + "'," + "'"
						+ pos.getBeginTime() + "'," + "'" + pos.getEndTime()
						+ "'," + "'" + pos.getRoad() + "')";
				log.info(sql);
				st.addBatch(sql);
			}
			st.executeBatch();
			conn.commit();
			log.info("[insertIntoDB TAB_ALARM_NOMARK success!!!]");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(sql);
		} finally {
			iniOutNoneRecords.clear();
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void InOutMoreRecordsStoreIntoDB(
			Map<String, AlarmMore> iniOutMoreRecords, ApplicationContext context) {
		Connection conn = null;
		String sql = "";
		try {
			SqlMapClient sc = (SqlMapClient) context
					.getBean("sqlMapClientLybc");
			conn = sc.getDataSource().getConnection();
			conn.setAutoCommit(false);
			Statement st = conn.createStatement();
			Iterator<String> it = iniOutMoreRecords.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				AlarmMore pos = iniOutMoreRecords.get(key);
				sql = "insert into TAB_ALARM_MORE "
						+ " (LICENCE,denoter,fisrt_Time,BEGIN_TIME,END_TIME,road) "
						+ " values (" + "'" + pos.getLicense() + "'," + "'"
						+ pos.getDenoter() + "'," + "'" + pos.getFirstTime()
						+ "'," + "'" + pos.getBeginTime() + "'," + "'"
						+ pos.getEndTime() + "'," + "'" + pos.getRoad() + "')";
				log.info(sql);
				st.addBatch(sql);
			}
			st.executeBatch();
			conn.commit();
			log.info("[insertIntoDB TAB_ALARM_MORE success!!!]");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(sql);
		} finally {
			iniOutMoreRecords.clear();
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
