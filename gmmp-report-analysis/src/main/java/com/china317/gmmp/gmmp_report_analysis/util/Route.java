package com.china317.gmmp.gmmp_report_analysis.util;

import java.util.ArrayList;
import java.util.List;

public class Route {
	private String id;
	private String name;
	private List<Point> points;
	public Route(){
		points = new ArrayList();
	}
	
	public void addPoint(double longtitude,double latitude){
		this.points.add(new Point(longtitude,latitude));
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Point>  getPoints() {
		return points;
	}
	
	
}
