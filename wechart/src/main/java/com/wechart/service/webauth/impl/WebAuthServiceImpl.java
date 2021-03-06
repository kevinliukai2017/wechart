package com.wechart.service.webauth.impl;

import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wechart.cofig.WXSettings;
import com.wechart.model.webaccesstoken.AccessTokenModel;
import com.wechart.model.webaccesstoken.JSAPITokenModel;
import com.wechart.model.webaccesstoken.WebAccessTokenModel;
import com.wechart.req.weixin.WebAuthAPI;
import com.wechart.service.webauth.WebAuthService;
import com.wechart.util.WXJSUtil;

@Service
public class WebAuthServiceImpl implements WebAuthService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private WXSettings wxSettings;
	
	@Autowired
	private HttpServletRequest request;
	
	@Override
	public String generateURL(String scope,String state) {
		// TODO Auto-generated method stub
		String appId = wxSettings.getAppId();
		String redirectURL = URLEncoder.encode(wxSettings.getRedirectURL());
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
		String finalURL = url.replace("APPID", appId).replace("REDIRECT_URI", redirectURL)
		.replace("SCOPE", scope).replace("STATE", state);
		return finalURL;
	}
	
	@Override
	public WebAccessTokenModel getWebAccessToken(String code) throws WexinReqException {
		// TODO Auto-generated method stub
		return WebAuthAPI.getWebAccessToken(wxSettings.getAppId(), wxSettings.getAppsecret(), code, "authorization_code");
	}
	
	@Override
	public AccessTokenModel getAccessToken() throws WexinReqException {
		// TODO Auto-generated method stub
		return WebAuthAPI.getAccessToken(wxSettings.getAppId(), wxSettings.getAppsecret());
	}

	@Override
	public JSAPITokenModel getJSAPIToken(String accessToken) throws WexinReqException {
		// TODO Auto-generated method stub
		return WebAuthAPI.getJSAPIToken(accessToken);
	}

	@Override
	public Map<String, String> generateJSWXConfig() throws WexinReqException {
		// TODO Auto-generated method stub
		AccessTokenModel accessToken = this.getAccessToken();
		JSAPITokenModel jsAPIToken = this.getJSAPIToken(accessToken.getAccess_token());
		String url = request.getRequestURL().toString();
		String parm = request.getQueryString();
		if(StringUtils.isNotBlank(parm)){
		    url = url + "?" + parm;
		}
		logger.info("请求url：" + url.toString() + "，用该url生成签名");
		Map<String, String> ret = WXJSUtil.sign(jsAPIToken.getTicket(),url);
		ret.put("appid", wxSettings.getAppId());
		return ret;
	}

	


	

}
