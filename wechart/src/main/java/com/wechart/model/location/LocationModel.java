package com.wechart.model.location;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 微信返回地址位置存库model
 * @author k.c.liu
 * use @Tranisent to ignore
 *
 */
@Table(name="location")
public class LocationModel {

	//微信id
	@Id
	@Column(name="wxid")
	private String wxId;
	//经度
	private String lng;
	//纬度
	private String lat;
	//精度
	@Column(name="wxprecision")
	private String precision;
	
	private String address;
	
	private String adcode;
	public String getWxId() {
		return wxId;
	}
	public void setWxId(String wxId) {
		this.wxId = wxId;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getPrecision() {
		return precision;
	}
	public void setPrecision(String precision) {
		this.precision = precision;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAdcode() {
		return adcode;
	}
	public void setAdcode(String adcode) {
		this.adcode = adcode;
	}
	
	
}
