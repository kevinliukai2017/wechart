package com.wechart.req.baidu;

import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.core.req.WeiXinReqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

public class BaiduAPI {
	
	private final static Logger logger = LoggerFactory.getLogger(BaiduAPI.class);
	
	/**
	 * 通过经纬度转换为地址
	 * @param ak
	 * @param outPutType
	 * @param lonAndLat
	 * @return
	 * @throws WexinReqException
	 */
	public static String getAddressByLoc(String ak,String outPutType,String latAndLon,String coordType) throws WexinReqException{
		GetAddressByLoc parm = new GetAddressByLoc();
		parm.setAk(ak);
		parm.setOutput(outPutType);
		parm.setLocation(latAndLon);
		parm.setCoordtype(coordType);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(parm);
		String str = "地球的某个角落";
		try {
			str = result.getJSONObject("result").get("formatted_address").toString() + "\n\n" +
					result.getJSONObject("result").get("sematic_description").toString();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(latAndLon + " 经纬度转换地址失败，使用默认地址-地球的某个角落");
		}
		return str;
	}
}
