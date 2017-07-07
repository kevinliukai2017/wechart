package com.wechart.cofig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "third")
@PropertySource("classpath:config/thirdAPI.properties")
public class ThirdAPISettings {
	
	private String gaodeKey;
	private String gaodeExtensions;
	private String gaodeoutPutType;
	public String getGaodeKey() {
		return gaodeKey;
	}
	public void setGaodeKey(String gaodeKey) {
		this.gaodeKey = gaodeKey;
	}
	public String getGaodeExtensions() {
		return gaodeExtensions;
	}
	public void setGaodeExtensions(String gaodeExtensions) {
		this.gaodeExtensions = gaodeExtensions;
	}
	public String getGaodeoutPutType() {
		return gaodeoutPutType;
	}
	public void setGaodeoutPutType(String gaodeoutPutType) {
		this.gaodeoutPutType = gaodeoutPutType;
	}
	
	

}
