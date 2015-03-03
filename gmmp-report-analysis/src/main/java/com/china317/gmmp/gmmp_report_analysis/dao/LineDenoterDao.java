package com.china317.gmmp.gmmp_report_analysis.dao;

import java.util.List;

import com.china317.gmmp.gmmp_report_analysis.bo.LineDenoter;

public interface LineDenoterDao {

	public List<LineDenoter> getLineDenoterByDate(String yyyyMMdd);

}
