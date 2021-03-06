package com.wechart.wx;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeewx.api.mp.aes.AesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wechart.cofig.WXSettings;
import com.wechart.model.location.LocationModel;
import com.wechart.service.WXService;
import com.wechart.service.location.LocationService;

/**
 * 用于监听服务器和微信之间的通信
 * get方法用于首次验证
 * post方法用于微信和服务器之间的通信
 * @author k.c.liu
 *
 */
@RestController
@RequestMapping("/wx")
public class WXController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private WXService wxService;
	
	/**
	 * 
	 * @param request
	 * @param response
	 * 服务器之间的认证使用
	 * @throws AesException 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/sign") 
	public String signGet(HttpServletRequest request,HttpServletResponse response) throws AesException{
		return wxService.signGet();
	}

	
	@RequestMapping(method = RequestMethod.POST, value = "/sign") 
	public String signPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		return wxService.processRequest();
	}
	
	
}
