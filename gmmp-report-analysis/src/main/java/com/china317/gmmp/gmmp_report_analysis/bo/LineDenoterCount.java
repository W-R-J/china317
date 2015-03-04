package com.china317.gmmp.gmmp_report_analysis.bo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class LineDenoterCount {
	protected final static Log log = LogFactory.getLog(LineDenoterCount.class);
	private String labelNumber;
	private int outNum;
	private String firstTime;

	
	public String getFirstTime() {
		return firstTime;
	}

	public void setFirstTime(String firstTime) {
		this.firstTime = firstTime;
	}

	public String getLabelNumber() {
		return labelNumber;
	}

	public void setLabelNumber(String denoter) {
		this.labelNumber = denoter;
	}

	public int getOutNum() {
		return outNum;
	}

	public void setOutNum(int outNum) {
		this.outNum = outNum;
	}

	
}
