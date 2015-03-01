package com.china317.gmmp.gmmp_report_analysis.service;

import java.util.List;

import com.china317.gmmp.gmmp_report_analysis.bo.VehicleLocate;

public interface DgmAnalysis {

	/**
	 * 超速分析
	 * @return
	 */
	public void overSpeedAnalysis(List<VehicleLocate> list);
	
	/**
	 * 禁入区域
	 */
	public void fobiddenAnalysis(List<VehicleLocate> list);
	/**
	 * 违规出入道口
	 */
	public void illegalInOutAnalysis(List<VehicleLocate> list);
	/**
	 * 场外停车
	 */
	public void illegalParkingAnalysis(List<VehicleLocate> list);
	/**
	 * 掉线报警
	 */
	public void offlineAnalysis(List<VehicleLocate> list);
	/**
	 * 掉线偏移
	 */
	public void offlineDisAnalysis(List<VehicleLocate> list);
}
