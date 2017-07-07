package com.wechart.service.location.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wechart.dao.location.LocationMapper;
import com.wechart.model.location.LocationModel;
import com.wechart.service.location.LocationService;

@Service
public class LocationServiceImpl implements LocationService{

	@Autowired
	LocationMapper locationMapper;

	@Override
	public void addWXLocation(LocationModel record) {
		// TODO Auto-generated method stub
		locationMapper.insert(record);
	}

	@Override
	public LocationModel findLocationByWxId(Object key) {
		// TODO Auto-generated method stub
		return locationMapper.selectByPrimaryKey(key);
	}

	@Override
	public int modifyWXLocation(LocationModel record) {
		// TODO Auto-generated method stub
		return locationMapper.updateByPrimaryKeySelective(record);
	}

}
