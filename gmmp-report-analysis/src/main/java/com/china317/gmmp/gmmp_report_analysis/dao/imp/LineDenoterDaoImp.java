package com.china317.gmmp.gmmp_report_analysis.dao.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.china317.gmmp.gmmp_report_analysis.bo.LineDenoter;
import com.china317.gmmp.gmmp_report_analysis.dao.LineDenoterDao;

public class LineDenoterDaoImp extends SqlMapClientDaoSupport  implements LineDenoterDao {

	public List<LineDenoter> getLineDenoterByDate(String yyyyMMdd) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yyyyMMdd",yyyyMMdd);
		List<LineDenoter> list = getSqlMapClientTemplate().queryForList("getLineDenoter", map);
		return list;
	}

}
