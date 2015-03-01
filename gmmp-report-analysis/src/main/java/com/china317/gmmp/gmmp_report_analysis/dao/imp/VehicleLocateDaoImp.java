package com.china317.gmmp.gmmp_report_analysis.dao.imp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.china317.gmmp.gmmp_report_analysis.bo.VehicleLocate;
import com.china317.gmmp.gmmp_report_analysis.dao.VehicleLocateDao;

public class VehicleLocateDaoImp  extends SqlMapClientDaoSupport implements VehicleLocateDao {

	private static final Log log = LogFactory.getLog(VehicleLocateDaoImp.class);
	public void testDB() {
		List<VehicleLocate> list = getSqlMapClientTemplate().queryForList("getAllVehicleLocatesTest");
		for(int i = 0; i < list.size(); i++){
			VehicleLocate l = list.get(i);
			log.info("[code]:"+l.getCode()
					+",[license]:"+l.getLicense()
					+"|||"
					);
		}
		log.info(list.size());
	}
	public List<VehicleLocate> findHistory(String yyyyMMdd) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("tableName","TRANS_VEHICLE_LOCATE_"+yyyyMMdd);
		List<VehicleLocate> list = getSqlMapClientTemplate().queryForList("getAllHistoryByDate", map);
		return list;
	}
	public List<VehicleLocate> findHistoryByParams(String yyyyMMdd,
			Set<String> codes) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("tableName","TRANS_VEHICLE_LOCATE_"+yyyyMMdd);
		String codeString = "";
		Iterator<String> it = codes.iterator();
		while(it.hasNext()){
			codeString += "'"+it.next()+"',";
		}
		if(codeString.indexOf(",")>0){
			codeString = codeString.substring(0, codeString.length()-1);
		}
		map.put("codes", codeString);
		List<VehicleLocate> list = getSqlMapClientTemplate().queryForList("getAllHistoryByParams", map);
		return list;
	}
}
