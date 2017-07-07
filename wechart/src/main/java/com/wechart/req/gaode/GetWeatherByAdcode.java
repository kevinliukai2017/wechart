package com.wechart.req.gaode;

import org.jeewx.api.core.annotation.ReqType;
import org.jeewx.api.core.req.model.WeixinReqParam;
@ReqType("getWeatherByAdcode")
public class GetWeatherByAdcode extends WeixinReqParam{

	private String key;
	private String city;
	private String output;
	//可选值：base/all  base:返回实况天气 all:返回预报天气
	private String extensions;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
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
