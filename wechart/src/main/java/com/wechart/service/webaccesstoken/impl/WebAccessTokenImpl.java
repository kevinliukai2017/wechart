package com.wechart.service.webaccesstoken.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wechart.dao.webaccesstoken.WebAccessTokenMapper;
import com.wechart.model.webaccesstoken.WebAccessTokenModel;
import com.wechart.service.webaccesstoken.WebAccessTokenService;

@Service
public class WebAccessTokenImpl implements WebAccessTokenService {

	@Autowired
	WebAccessTokenMapper webAccessTokenMapper;
	
	@Override
	public WebAccessTokenModel findWebAccessTokenByAppid(Object key) {
		// TODO Auto-generated method stub
		return webAccessTokenMapper.selectByPrimaryKey(key);
	}

}
