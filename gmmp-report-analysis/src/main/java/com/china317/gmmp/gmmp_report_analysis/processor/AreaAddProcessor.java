package com.china317.gmmp.gmmp_report_analysis.processor;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.china317.gmmp.gmmp_report_analysis.bo.Rule;
import com.china317.gmmp.gmmp_report_analysis.bo.RuleResultWrap;
import com.china317.gmmp.gmmp_report_analysis.bo.VehicleLocate;
import com.china317.gmmp.gmmp_report_analysis.dao.RuleDao;
import com.china317.gmmp.gmmp_report_analysis.dao.VehicleDao;
import com.china317.gmmp.gmmp_report_analysis.dao.imp.RuleDaoImp;
import com.china317.gmmp.gmmp_report_analysis.util.PolyUtilityE2;


public class AreaAddProcessor {
	
	protected static final Log log = LogFactory.getLog(AreaAddProcessor.class);
	
	protected static List<Rule> ruleList =null;
	
	public static void addAreaRuleInfo(VehicleLocate en) {
		try {
			for (Iterator iter = ruleList.iterator(); iter.hasNext();) {
				Rule ruleEn = (Rule) iter.next();
				RuleResultWrap polygonWrap = PolyUtilityE2.isBreakTheRule(ruleEn.getId(), en.getLon(),en.getLat());
				if (polygonWrap != null)
					en.addRuleRsWrapSet(polygonWrap);
			}
		} catch (Exception e) {
			log.warn("VehicleLocate",e);
		}
	}
	
	public static List<Rule> getRuleList(){
		return ruleList;
	}

	public static void init(ApplicationContext context) {
		RuleDao dao = (RuleDao) context.getBean("ruleDao");
		ruleList = dao.findAll();	
	}
	
}
