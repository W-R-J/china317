package com.china317.gmmp.gmmp_report_analysis.bo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.china317.gmmp.gmmp_report_analysis.util.DateTime;


public class Rule {
	public static final long RULE_STATUS_EDIT = 0;
	public static final long RULE_STATUS_USE = 1;
	public static final long RULE_TYPE_TIME = 0;
	private String corpname = "";
	private Map timeCycleMap = new HashMap();
	private long timeCycleStart = 0;
	private long timeCycleEnd = 0;
	private List areaList = new ArrayList();
	private String parent = "";
	
	private String id = "";
	private String name = "";
	private String corpId = "";
	private String modifyId = "";
	private String modifyTime = "";
	private long ruleStatus = 0;
	private long ruleType = 0;
	private String ruleStart = null;
	private String ruleEnd = null;
	private String timeCycle = "";
	private String timeCycleFirst = null;
	private String timeCycleDay = "";
	private String timeCycleDayStart = "";
	private String timeCycleDayEnd = "";
	private String remark = "";
	private long areaIn = 0;
	
	
	public String getCorpname() {
		return corpname;
	}

	public void setCorpname(String corpname) {
		this.corpname = corpname;
	}

	public Map getTimeCycleMap() {
		return timeCycleMap;
	}

	public void setTimeCycleMap(Map timeCycleMap) {
		this.timeCycleMap = timeCycleMap;
	}

	public long getTimeCycleStart() {
		return timeCycleStart;
	}

	public void setTimeCycleStart(long timeCycleStart) {
		this.timeCycleStart = timeCycleStart;
	}

	public long getTimeCycleEnd() {
		return timeCycleEnd;
	}

	public void setTimeCycleEnd(long timeCycleEnd) {
		this.timeCycleEnd = timeCycleEnd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getModifyId() {
		return modifyId;
	}

	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public long getRuleStatus() {
		return ruleStatus;
	}

	public void setRuleStatus(long ruleStatus) {
		this.ruleStatus = ruleStatus;
	}

	public long getRuleType() {
		return ruleType;
	}

	public void setRuleType(long ruleType) {
		this.ruleType = ruleType;
	}

	public String getRuleStart() {
		return ruleStart;
	}

	public void setRuleStart(String ruleStart) {
		this.ruleStart = ruleStart;
	}

	public String getRuleEnd() {
		return ruleEnd;
	}

	public void setRuleEnd(String ruleEnd) {
		this.ruleEnd = ruleEnd;
	}

	public String getTimeCycle() {
		return timeCycle;
	}

	public void setTimeCycle(String timeCycle) {
		this.timeCycle = timeCycle;
	}

	public String getTimeCycleFirst() {
		return timeCycleFirst;
	}

	public void setTimeCycleFirst(String timeCycleFirst) {
		this.timeCycleFirst = timeCycleFirst;
	}

	public String getTimeCycleDay() {
		return timeCycleDay;
	}

	public void setTimeCycleDay(String timeCycleDay) {
		this.timeCycleDay = timeCycleDay;
	}

	public String getTimeCycleDayStart() {
		return timeCycleDayStart;
	}

	public void setTimeCycleDayStart(String timeCycleDayStart) {
		this.timeCycleDayStart = timeCycleDayStart;
	}

	public String getTimeCycleDayEnd() {
		return timeCycleDayEnd;
	}

	public void setTimeCycleDayEnd(String timeCycleDayEnd) {
		this.timeCycleDayEnd = timeCycleDayEnd;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getAreaIn() {
		return areaIn;
	}

	public void setAreaIn(long areaIn) {
		this.areaIn = areaIn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	/**
	 * @return the areaList
	 */
	public List getAreaList() {
		return areaList;
	}

	/**
	 * @param areaList
	 *            the areaList to set
	 */
	public void setAreaList(List areaList) {
		this.areaList = areaList;
	}

	public String getCorpName() {
		return corpname;
	}

	public void setCorpName(String corpname) {
		this.corpname = corpname;
	}


	public boolean verifyTime(String time) {
		boolean isAlert = false;
		String tdate = time.substring(0, 8);
		long ttime = Long.parseLong(time.substring(8));
		if (timeCycleMap.containsKey(tdate)) {
			if (ttime >= timeCycleStart && ttime <= timeCycleEnd) {
				isAlert = true;
			}
		}
		return isAlert;
	}


	/**
	 * @param dou
	 * @return
	 */
	public int getInt(double dou) {
		return new Double(dou * 60 * 10000).intValue();
		// return new Double(dou).intValue();
	}


	public static String getDate(Calendar cal) {
		return DateTime.getDateTimeString(cal.getTime()).substring(0, 8);
	}

}
