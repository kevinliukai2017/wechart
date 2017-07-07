package com.wechart.service.webauth;

import java.util.Map;

import org.jeewx.api.core.exception.WexinReqException;

import com.wechart.model.webaccesstoken.AccessTokenModel;
import com.wechart.model.webaccesstoken.JSAPITokenModel;
import com.wechart.model.webaccesstoken.WebAccessTokenModel;

public interface WebAuthService {

	/**
	 * 生成引导用户打开的url
	 * @return
	 */
	public String generateURL(String scope,String state);
	
	/**
	 * 通过code换取网页授权access_token
	 * @param code
	 * @return
	 * @throws WexinReqException 
	 */
	public WebAccessTokenModel getWebAccessToken(String code) throws WexinReqException;
	
	/**
	 * 获取access_token
	 * @param appid
	 * @param secret
	 * @return
	 * @throws WexinReqException
	 */
	public AccessTokenModel getAccessToken() throws WexinReqException;
	
	/**
	 * 获取js api token
	 * @param accessToken
	 * @return
	 * @throws WexinReqException
	 */
	public JSAPITokenModel getJSAPIToken(String accessToken) throws WexinReqException;
	
	/**
	 * 生成前端 wx.config需要的参数
	 * @return
	 */
	public Map<String, String> generateJSWXConfig() throws WexinReqException;
}
