package com.china317.gmmp.gmmp_report_analysis.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.china317.gmmp.gmmp_report_analysis.bo.Vehicle;
import com.china317.gmmp.gmmp_report_analysis.bo.VehicleLocate;

public interface VehicleLocateDao {

	public void testDB();
	/**
	 * 根据日期，从轨迹库里面取得所有的轨迹点 gmmpraw
	 * @param yyyyMMdd
	 * @return
	 */
	public List<VehicleLocate> findHistory(String yyyyMMdd);
	/**
	 * 根据日期，车辆唯一标示符集合，取得符合要求的轨迹点 gmmpraw
	 * @param yyyyMMdd
	 * @param codes
	 * @return
	 */
	public List<VehicleLocate> findHistoryByParams(String yyyyMMdd,Set<String> codes);

}
