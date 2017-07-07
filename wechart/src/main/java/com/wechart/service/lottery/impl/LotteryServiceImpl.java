package com.wechart.service.lottery.impl;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wechart.dao.lottery.LotteryMapper;
import com.wechart.model.lottery.LotteryModel;
import com.wechart.service.lottery.LotteryService;

import br.com.objectos.core.lang.Preconditions;

@Service
public class LotteryServiceImpl implements LotteryService{

	@Autowired
	private LotteryMapper lotteryMapper;
	
	@Override
	public int addLotteryRecord(LotteryModel record) {
		// TODO Auto-generated method stub
		Preconditions.checkNotNull(record, "插入抽奖对象为空");
		Preconditions.checkArgument(StringUtils.isNotEmpty(record.getOpenid()), "微信号不能为空");
		Preconditions.checkArgument(StringUtils.isNotEmpty(record.getAwards()), "奖项不能为空");
		return lotteryMapper.insert(record);
	}

	
	public static void main(String[] args) throws Exception {
		String a = null;
		Preconditions.checkArgument(StringUtils.isNotEmpty(a), "微信号不能为空");
		System.out.println(a);
	}


}
