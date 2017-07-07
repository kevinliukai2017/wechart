package com.wechart.req.gaode;

import org.jeewx.api.core.annotation.ReqType;
import org.jeewx.api.core.req.model.WeixinReqParam;
@ReqType("getInfoByGDLatLng")
public class GetInfoByGDLatLng extends WeixinReqParam{

	private String key;
	private String location;
	private String output;
	//此项默认返回基本地址信息；取值为all返回地址信息、附近POI、道路以及道路交叉口信息。
	private String extensions;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public String getExtensions() {
		return extensions;
	}
	public void setExtensions(String extensions) {
		this.extensions = extensions;
	}
	

}
