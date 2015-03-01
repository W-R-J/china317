package com.china317.gmmp.gmmp_report_analysis.dao.imp;

import java.util.HashMap;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.china317.gmmp.gmmp_report_analysis.bo.Vehicle;
import com.china317.gmmp.gmmp_report_analysis.dao.VehicleDao;

public class VehicleDaoImp extends SqlMapClientDaoSupport  implements VehicleDao {

	public List<Vehicle> getBaseVehicleByDate(String yyyyMMdd,String businessType) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("yyyyMMdd",yyyyMMdd);
		map.put("businessType", businessType);
		List<Vehicle> list = getSqlMapClientTemplate().queryForList("getBaseVehicle",map);
		return list;
	}

}
