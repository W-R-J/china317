package com.china317.gmmp.gmmp_report_analysis.service;

import java.util.List;

import com.china317.gmmp.gmmp_report_analysis.bo.VehicleLocate;

public interface DgmAnalysis {

	/**
	 * 超速分析
	 * @return
	 */
	public void overSpeedAnalysis(VehicleLocate entity) throws Exception;
	
	/**
	 * 禁入区域
	 */
	public void fobiddenAnalysis(VehicleLocate entity,int index,int size,String yyyyMMdd)  throws Exception;
	/**
	 * 违规出入道口
	 */
	public void illegalInOutAnalysis(VehicleLocate entity) throws Exception;
	/**
	 * 场外停车
	 */
	public void illegalParkingAnalysis(VehicleLocate entity) throws Exception;
	/**
	 * 掉线报警
	 */
	public void offlineAnalysis (VehicleLocate e, String yyyyMMdd) throws Exception ;
	/**
	 * 掉线偏移
	 */
	public void offlineDisAnalysis(VehicleLocate entity) throws Exception;
}
