package com.china317.gmmp.gmmp_report_analysis.dao.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.china317.gmmp.gmmp_report_analysis.bo.PtmOverSpeed;
import com.china317.gmmp.gmmp_report_analysis.dao.OverSpeedDao;
import com.ibatis.sqlmap.client.SqlMapClient;

public class OverSpeedDaoImp extends SqlMapClientDaoSupport implements OverSpeedDao {

	@Override
	public void insertBatch(Map<String, PtmOverSpeed> overSpeedRecords) {
		/*List<PtmOverSpeed> list = new ArrayList<PtmOverSpeed>();
		list.addAll(overSpeedRecords.values());
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("ragrouppreinsert", list);
		getSqlMapClientTemplate().insert("insertBatchOverSpeed",map);*/
		
		
	}

}
