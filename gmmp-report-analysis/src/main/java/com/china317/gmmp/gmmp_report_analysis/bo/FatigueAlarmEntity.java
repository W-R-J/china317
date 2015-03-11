package com.china317.gmmp.gmmp_report_analysis.bo;


public class FatigueAlarmEntity {

	private String licence;
	private String licenceColor;
	private String alarmStartTime;
	private String alarmEndTime;
	private String pointCount;
	
	private String corpName;
	private String corpId;
	
	
	
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getLicence() {
		return licence;
	}
	public void setLicence(String licence) {
		this.licence = licence;
	}
	public String getLicenceColor() {
		return licenceColor;
	}
	public void setLicenceColor(String licenceColor) {
		this.licenceColor = licenceColor;
	}
	public String getAlarmStartTime() {
		return alarmStartTime;
	}
	public void setAlarmStartTime(String alarmStartTime) {
		this.alarmStartTime = alarmStartTime;
	}
	public String getAlarmEndTime() {
		return alarmEndTime;
	}
	public void setAlarmEndTime(String alarmEndTime) {
		this.alarmEndTime = alarmEndTime;
	}
	public String getPointCount() {
		return pointCount;
	}
	public void setPointCount(String pointCount) {
		this.pointCount = pointCount;
	}
	
	public static class Builder{
		private String licence;
		private String licenceColor;
		private String alarmStartTime;
		private String alarmEndTime;
		private String pointCount;
		
		public Builder licence(String val){
			this.licence = val;
			return this;
		}
		public Builder licenceColor(String val){
			this.licenceColor = val;
			return this;
		}
		public Builder alarmStartTime(String val){
			this.alarmStartTime = val;
			return this;
		}
		public Builder alarmEndTime(String val){
			this.alarmEndTime = val;
			return this;
		}
		public Builder pointCount(String val){
			this.pointCount = val;
			return this;
		}
		
		public FatigueAlarmEntity create(){
			try{
				return new FatigueAlarmEntity(this);
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		
	}

	private FatigueAlarmEntity(Builder b) {
			this.licence = b.licence;
			this.licenceColor = b.licenceColor;
			this.alarmStartTime = b.alarmStartTime;
			this.alarmEndTime = b.alarmEndTime;
			this.pointCount = b.pointCount;
		
	}
	
	private void test(){
		String val = "";
		FatigueAlarmEntity entity = new FatigueAlarmEntity.Builder()
														  .licence(val)
														  .licenceColor(val)
														  .alarmStartTime(val)
														  .alarmEndTime(val)
														  .pointCount(val)
														  .create();
	}

	
	
	
	
}
