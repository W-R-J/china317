package com.china317.gmmp.gmmp_report_analysis.dao;

import java.util.List;

import com.china317.gmmp.gmmp_report_analysis.bo.Vehicle;

public interface VehicleDao {

	public List<Vehicle> getBaseVehicleByDate(String yyyyMMdd,String businessType);
}
