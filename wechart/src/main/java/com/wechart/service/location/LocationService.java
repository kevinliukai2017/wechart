package com.wechart.service.location;

import com.wechart.model.location.LocationModel;


public interface LocationService {
	
	/**
	 * 微信返回的经纬度存入到数据库
	 * @param record
	 */
	void addWXLocation(LocationModel record);
	
	/**
	 * 通过微信号查找位置信息
	 * @param key
	 * @return
	 */
	LocationModel findLocationByWxId(Object key);
	
	/**
	 * 更新微信地址信息
	 * @param record
	 */
	int modifyWXLocation(LocationModel record);
}
