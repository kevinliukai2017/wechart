package com.wechart.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wechart.model.webaccesstoken.WebAccessTokenModel;
import com.wechart.req.weixin.WebAuthAPI;
import com.wechart.service.webauth.WebAuthService;


@Controller
@RequestMapping("/wx")
public class AuthController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	WebAuthService webAuthService;
	
	@RequestMapping("/generateURL")
	public String generateURL(Model model) {
		model.addAttribute("authUrl", webAuthService.generateURL("snsapi_base", "123"));
		return "index";
	}
	
	@RequestMapping("/getWebAccessToken")
	public String getCodeState(Model model,HttpServletRequest request) throws WexinReqException {
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		logger.info("getWebAccessToken--code: " + code  + ",state: " + state);
		WebAccessTokenModel webAccessToken = webAuthService.getWebAccessToken(code);
		model.addAttribute("webAccessToekn", webAccessToken);
		return "webAccessToken";
	}
}
