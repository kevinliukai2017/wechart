package com.wechart.service.springsecurity;

import java.util.List;

import com.wechart.model.srpingsecurity.UserRoleModel;

public interface HeroUserService {

	/**
	 * 通过用户名查找用户和角色
	 * @param username
	 * @return
	 */
	List<UserRoleModel> findUserAndRole(String username);
}
