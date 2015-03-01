package com.china317.gmmp.gmmp_report_analysis.dao.imp;

import java.awt.Polygon;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.china317.gmmp.gmmp_report_analysis.bo.Rule;
import com.china317.gmmp.gmmp_report_analysis.bo.RuleResultWrap;
import com.china317.gmmp.gmmp_report_analysis.dao.RuleDao;
import com.ibatis.sqlmap.client.SqlMapClient;

public class RuleDaoImp extends SqlMapClientDaoSupport implements RuleDao {

	@Override
	public List<Rule> findAll() {
		List<Rule> list = getSqlMapClientTemplate().queryForList("getAllRule");
		return list;
	}
	

}
