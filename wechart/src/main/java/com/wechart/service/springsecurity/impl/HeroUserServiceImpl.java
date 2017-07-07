package com.wechart.service.springsecurity.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wechart.dao.springsecurity.HeroUserMapper;
import com.wechart.model.srpingsecurity.HeroUser;

public class HeroUserServiceImpl implements UserDetailsService {

	@Autowired
	HeroUserMapper heroUserMapper;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		HeroUser heroUser = heroUserMapper.findUserAndRole(username);
		if(null == heroUser){
			throw new UsernameNotFoundException("用户名不存在");
		}
		return heroUser;
	}
}
