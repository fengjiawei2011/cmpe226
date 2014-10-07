package cmpe226.project1.fire.schema1.data;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fire")
public class Fire {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "fire_id", updatable = false, nullable = false)
	private Long id;
	private double latitude;
	private double longitude;
	private Date report_date;
	private double area;
	private String fire_name;
	private String fire_number;
	private String Condition;
	private int wfu;
	private int report_age;
	private String gacc;
	
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public Date getReport_date() {
		return report_date;
	}
	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}
	public double getArea() {
		return area;
	}
	public void setArea(double area) {
		this.area = area;
	}
	public String getFire_name() {
		return fire_name;
	}
	public void setFire_name(String fire_name) {
		this.fire_name = fire_name;
	}
	public String getFire_number() {
		return fire_number;
	}
	public void setFire_number(String fire_number) {
		this.fire_number = fire_number;
	}
	public String getCondition() {
		return Condition;
	}
	public void setCondition(String condition) {
		Condition = condition;
	}
	public int getWfu() {
		return wfu;
	}
	public void setWfu(int wfu) {
		this.wfu = wfu;
	}
	public int getReport_age() {
		return report_age;
	}
	public void setReport_age(int report_age) {
		this.report_age = report_age;
	}
	public String getGacc() {
		return gacc;
	}
	public void setGacc(String gacc) {
		this.gacc = gacc;
	}
}
