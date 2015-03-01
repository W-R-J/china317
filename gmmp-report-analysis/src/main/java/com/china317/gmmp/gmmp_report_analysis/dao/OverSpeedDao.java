package com.china317.gmmp.gmmp_report_analysis.dao;

import java.util.Map;

import com.china317.gmmp.gmmp_report_analysis.bo.PtmOverSpeed;

public interface OverSpeedDao {

	public void insertBatch(Map<String, PtmOverSpeed> overSpeedRecords);
}
