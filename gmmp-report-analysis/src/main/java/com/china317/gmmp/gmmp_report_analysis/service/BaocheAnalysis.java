package com.china317.gmmp.gmmp_report_analysis.service;

import java.util.List;

import com.china317.gmmp.gmmp_report_analysis.bo.VehicleLocate;

public interface BaocheAnalysis {

	/**
	 * 超速分析
	 * @return
	 */
	public void overSpeedAnalysis(VehicleLocate entity) throws Exception;
	/**
	 * 2-5点运营分析
	 * @return
	 */
	public void nonstopAnalysis(List<VehicleLocate> list) throws Exception;

	/**
	 * 线路牌分析报警（无牌出入境、多次出入境）
	 */
	public void xlpAlarmAnalysis(VehicleLocate entity) throws Exception;
	/**
	 * 掉线
	 */
	public void offlineAnalysis(VehicleLocate entity,String yyyyMMdd) throws Exception;
	/**
	 * 掉线偏移
	 */
	public void offlineDisAnalysis(List<VehicleLocate> list) throws Exception;
}
