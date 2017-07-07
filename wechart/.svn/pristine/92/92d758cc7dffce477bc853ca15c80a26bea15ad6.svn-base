package com.wechart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jeewx.api.core.exception.WexinReqException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wechart.model.lottery.LotteryModel;
import com.wechart.model.srpingsecurity.UserRoleModel;
import com.wechart.model.webaccesstoken.WebAccessTokenModel;
import com.wechart.service.lottery.LotteryService;
import com.wechart.service.springsecurity.HeroUserService;
import com.wechart.service.webauth.WebAuthService;
import com.wechart.util.LotteryUtil;

@Controller
public class LoginController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/*@Autowired
	private HeroUserService userRoleService;
	
	
	@RequestMapping("/find")
	@ResponseBody
	public List<UserRoleModel> showLotteryPage(Model model,HttpServletRequest request) throws WexinReqException{
		return userRoleService.findUserAndRole("aaa");
	}
	*/
	
	@RequestMapping("/loginsuccess")
	@ResponseBody
	public String loginSuccess(Model model,HttpServletRequest request) throws WexinReqException{
		return "success";
	}
}
