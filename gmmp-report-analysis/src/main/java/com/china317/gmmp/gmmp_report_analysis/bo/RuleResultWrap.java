package com.china317.gmmp.gmmp_report_analysis.bo;

import java.awt.Polygon;

public class RuleResultWrap {

	private String ruleId;
	private String ruleName = "";
	
	private String polygonId;
	private String polygonName= "";
	
	private String isAreaIn;
	private Polygon pg ;
	private String corpCode;
	
	private String parent="";
	
	private String areaName;
	
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getPolygonId() {
		return polygonId;
	}
	
	public void setPolygonId(String polygonId) {
		this.polygonId = polygonId;
	}

	public Polygon getPg() {
		return pg;
	}
	public void setPg(Polygon pg) {
		this.pg = pg;
	}
	public String getIsAreaIn() {
		return isAreaIn;
	}
	public void setIsAreaIn(String isAreaIn) {
		this.isAreaIn = isAreaIn;
	}
	public String getRuleId() {
		return ruleId;
	}
	
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getPolygonName() {
		return polygonName;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setPolygonName(String polygonName) {
		this.polygonName = polygonName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	

}
