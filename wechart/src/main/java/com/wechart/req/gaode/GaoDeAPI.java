package com.wechart.req.gaode;

import java.util.HashMap;
import java.util.Map;

import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.core.req.WeiXinReqService;

import net.sf.json.JSONObject;

public class GaoDeAPI {

	/**
	 * 通过经纬度转换为地址
	 * 
	 * @param ak
	 * @param outPutType
	 * @param lonAndLat
	 * @return
	 * @throws WexinReqException
	 */
	public static Map<String,String> getInfoByGDLatLng(String gaodeKey,String outPutType, String latAndLon,String extensions)
			throws WexinReqException {
		GetInfoByGDLatLng parm = new GetInfoByGDLatLng();
		parm.setKey(gaodeKey);
		parm.setOutput(outPutType);
		parm.setLocation(latAndLon);
		parm.setExtensions(extensions);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(parm);
		Map<String, String> resultMap = new HashMap<String,String>();
		try {
			resultMap.put("formatted_address", result.getJSONObject("regeocode").get("formatted_address").toString());
			resultMap.put("adcode", result.getJSONObject("regeocode").getJSONObject("addressComponent").get("adcode").toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 获取天气信息
	 * @param gaodeKey
	 * @param outPutType
	 * @param city
	 * @param extensions
	 * @return
	 * @throws WexinReqException
	 */
	public static Map<String, String> getWeatherByAdcode(String gaodeKey,String outPutType, String city,String extensions) throws WexinReqException{
		GetWeatherByAdcode parm = new GetWeatherByAdcode();
		parm.setKey(gaodeKey);
		parm.setOutput(outPutType);
		parm.setCity(city);
		parm.setExtensions(extensions);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(parm);
		Map<String, String> resultMap = new HashMap<String,String>();
		//result.getJSONObject("lives").getJSONObject("0").get("province").toString()
		try {
			resultMap.put("province", result.getJSONArray("lives").getJSONObject(0).get("province").toString());
			resultMap.put("city", result.getJSONArray("lives").getJSONObject(0).get("province").toString());
			resultMap.put("adcode", result.getJSONArray("lives").getJSONObject(0).get("adcode").toString());
			resultMap.put("weather", result.getJSONArray("lives").getJSONObject(0).get("weather").toString());
			resultMap.put("temperature", result.getJSONArray("lives").getJSONObject(0).get("temperature").toString());
			resultMap.put("winddirection", result.getJSONArray("lives").getJSONObject(0).get("winddirection").toString());
			resultMap.put("windpower", result.getJSONArray("lives").getJSONObject(0).get("windpower").toString());
			resultMap.put("humidity", result.getJSONArray("lives").getJSONObject(0).get("humidity").toString());
			resultMap.put("reporttime", result.getJSONArray("lives").getJSONObject(0).get("reporttime").toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return resultMap;
	}
}
