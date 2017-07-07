package com.wechart.util;

import java.io.IOException;
import java.util.Map;

import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.core.req.WeiXinReqService;

import com.wechart.model.LatLng;
import com.wechart.req.baidu.GetAddressByLoc;
import com.wechart.req.gaode.GaoDeAPI;
import com.wechart.req.gaode.GetInfoByGDLatLng;

import net.sf.json.JSONObject;

public class HttpUtil {

	public static void main(String[] args) throws IOException, WexinReqException {
//		String ak = "1QBI59DwZntFw0DeSWYrcDuFZSGHvdo2";
//		String url = "http://api.map.baidu.com/geocoder/v2/?address=上海五角场万达广场&output=json&ak=" + ak;
//		// 1.地理编码服务，即根据地址来查经度纬度
//		String return_value = doGet(url);
//		//System.out.println(return_value);
//		// {"status":0,"result":{"location":{"lng":121.51996737545,"lat":31.308233584217},"precise":1,"confidence":80,"level":"\u5546\u52a1\u5927\u53a6"}}
////32.2055770000,120.0376230000
//		// 2.逆地理编码,即根据经度纬度来查地址
//		String url2 = "http://api.map.baidu.com/geocoder/v2/?ak=" + ak
//				+ "&location=32.2055770000,120.0376230000&output=json";
//		JSONObject jsonObj = JSONObject.parseObject(doGet(url2));
//		System.out.println(jsonObj.getJSONObject("result").getJSONObject("addressComponent").get("city").toString() + jsonObj.getJSONObject("result").getJSONObject("addressComponent").get("district").toString());
//	
//		GetAddressByLoc test = new GetAddressByLoc();
//		test.setAk("1QBI59DwZntFw0DeSWYrcDuFZSGHvdo2");
//		test.setOutput("json");
//		test.setLocation("31.406696,121.419998");
//		test.setCoordtype("wgs84ll");
//		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(test);
//		System.out.println(result.toString());
//		System.out.println(result.getJSONObject("result").get("formatted_address"));
//		System.out.println(result.getJSONObject("result").get("sematic_description"));

		//
		LatLng latLng = new LatLng();
		latLng.setLatitude(Double.valueOf("31.406696"));
		latLng.setLongitude(Double.valueOf("121.419998"));
		latLng = CoordinateUtil.transformFromWGSToGCJ(latLng);
		System.out.println("lat:" + latLng.getLatitude());
		System.out.println("lng:" + latLng.getLongitude());
		
//		GetInfoByGDLatLng t = new GetInfoByGDLatLng();
//		t.setKey("5d27ba8bf79fc4ec68ee079feef68e9a");
//		t.setLocation(latLng.getLongitude()+","+latLng.getLatitude());
//		t.setExtensions("base");
//		t.setOutput("json");
//		JSONObject gaodeResult = WeiXinReqService.getInstance().doWeinxinReqJson(t);
//		System.out.println(gaodeResult.toString());
//		System.out.println(gaodeResult.getJSONObject("regeocode").get("formatted_address").toString());
//		System.out.println(gaodeResult.getJSONObject("regeocode").getJSONObject("addressComponent").get("adcode").toString());
		
		Map<String,String> res = GaoDeAPI.getWeatherByAdcode("5d27ba8bf79fc4ec68ee079feef68e9a", "json",
				"440106", "base");
		
		String respContent = "天气:" +  res.get("weather") + "\n\n" +
					  "气温:" +  res.get("temperature") + "℃" + "\n\n" +
					  "风向:" +  res.get("winddirection") + "风" + "\n\n" +
					  "级别:" +  res.get("windpower") + "级" + "\n\n" +
					  "更新时间:" +  res.get("reporttime");
		System.out.println(respContent);

		
	}
}
