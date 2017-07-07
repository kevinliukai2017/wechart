package com.wechart.service.webaccesstoken;

import com.wechart.model.webaccesstoken.WebAccessTokenModel;

public interface WebAccessTokenService {

	/**
	 * 通过appid查询webaccesstoken
	 * @param key
	 * @return
	 */
	WebAccessTokenModel findWebAccessTokenByAppid(Object key);
}
