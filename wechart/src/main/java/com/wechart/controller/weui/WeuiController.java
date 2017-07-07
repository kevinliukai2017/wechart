package com.wechart.controller.weui;

import java.util.Map;

import org.jeewx.api.core.exception.WexinReqException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wechart.model.webaccesstoken.AccessTokenModel;
import com.wechart.model.webaccesstoken.JSAPITokenModel;
import com.wechart.service.webauth.WebAuthService;
import com.wechart.util.WXJSUtil;

@Controller
public class WeuiController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private WebAuthService webAuthService;
	
	@RequestMapping("/weuidemo")
	public String demo() {

		return "weui/weuidemo";
	}

	@RequestMapping("/scanQrcode")
	public String scanQrcode(Model model) throws WexinReqException {
		AccessTokenModel accessToken = webAuthService.getAccessToken();
		JSAPITokenModel jsAPIToken = webAuthService.getJSAPIToken(accessToken.getAccess_token());
		String url = "http://herocenter.cn/wx/scanQrcode";
		Map<String, String> ret = WXJSUtil.sign(jsAPIToken.getTicket(), url);
		for (Map.Entry entry : ret.entrySet()) {
			logger.info(entry.getKey() + "=" + entry.getValue());
			model.addAttribute(entry.getKey().toString(), entry.getValue());
		}
		return "weui/scanQrcode";
	}
	
	@RequestMapping("/jssdkdemo")
	public String jssdkdemo(Model model) throws WexinReqException {
		Map<String, String> ret = webAuthService.generateJSWXConfig();
		for (Map.Entry entry : ret.entrySet()) {
			logger.info(entry.getKey() + "=" + entry.getValue());
			model.addAttribute(entry.getKey().toString(), entry.getValue());
		}
		return "weui/jssdkdemo";
	}

}
