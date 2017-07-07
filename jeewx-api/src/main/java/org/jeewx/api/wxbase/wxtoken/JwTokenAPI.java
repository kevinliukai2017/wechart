package org.jeewx.api.wxbase.wxtoken;

import net.sf.json.JSONObject;

import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.core.req.WeiXinReqService;
import org.jeewx.api.core.req.model.AccessToken;

/**
 * 微信--token信息
 * 
 * @author lizr
 * 
 */
public class JwTokenAPI {

	private static AccessToken atoken = null;

	/**
	 * 获取权限令牌信息
	 * @param appid
	 * @param appscret
	 * @return kY9Y9rfdcr8AEtYZ9gPaRUjIAuJBvXO5ZOnbv2PYFxox__uSUQcqOnaGYN1xc4N1rI7NDCaPm_0ysFYjRVnPwCJHE7v7uF_l1hI6qi6QBsA
	 * @throws WexinReqException
	 */
	public static String getAccessToken(String appid, String appscret) throws WexinReqException{
		String newAccessToken = "";
		atoken = new AccessToken();
		atoken.setAppid(appid);
		atoken.setSecret(appscret);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(atoken);
		// 正常返回
		newAccessToken = result.getString("access_token");;
		return newAccessToken;
	}
	 
	
	public static void main(String[] args){
		 
		try {
			String s = JwTokenAPI.getAccessToken("wxa7e93a3f7bfc8065","4b91436f04c23ec0b5810e1a5be49d34");
			System.out.println(s);
		} catch (WexinReqException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
