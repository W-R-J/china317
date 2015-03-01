package com.china317.gmmp.gmmp_report_analysis.service;

import java.util.List;

import com.china317.gmmp.gmmp_report_analysis.bo.VehicleLocate;

public interface BaocheAnalysis {

	/**
	 * 超速分析
	 * @return
	 */
	public void overSpeedAnalysis(List<VehicleLocate> list);
	/**
	 * 2-5点运营分析
	 * @return
	 */
	public void nonstopAnalysis(List<VehicleLocate> list);
	
	/**
	 * 多次出入境
	 */
	public void inOutMoreAnalysis(List<VehicleLocate> list);
	/**
	 * 无牌出入境
	 */
	public void inOutNoneAnalysis(List<VehicleLocate> list);
	/**
	 * 掉线
	 */
	public void offlineAnalysis(List<VehicleLocate> list);
	/**
	 * 掉线偏移
	 */
	public void offlineDisAnalysis(List<VehicleLocate> list);
}
