package com.wechart.util;

import java.util.Random;

public class LotteryUtil {

	public static Object[][] prizeArr = new Object[][] {
			// id,min,max，prize【奖项】,v【中奖率】
			// 外面的转盘转动
			// {1,1,14,"一等奖",1},
			// {2,346,364,"一等奖",1},
			// {3,16,44,"不要灰心",10},
			// {4,46,74,"神马也没有",10},
			// {5,76,104,"祝您好运",10},
			// {6,106,134,"二等奖",2},
			// {7,136,164,"再接再厉",10},
			// {8,166,194,"神马也没有",10},
			// {9,196,224,"运气先攒着",10},
			// {10,226,254,"三等奖",5},
			// {11,256,284,"要加油哦",10},
			// {12,286,314,"神马也没有",10},
			// {13,316,344,"谢谢参与",10}

			// 里面的指针转动
			{ 1, 1, 14, "一等奖", 1 }, { 2, 346, 364, "一等奖", 1 }, { 3, 16, 44, "不要灰心", 900 }, { 4, 46, 74, "神马也没有", 900 },
			{ 5, 76, 104, "祝您好运", 900 }, { 6, 106, 134, "二等奖", 20 }, { 7, 136, 164, "再接再厉", 900 },
			{ 8, 166, 194, "神马也没有", 900 }, { 9, 196, 224, "运气先攒着", 900 }, { 10, 226, 254, "三等奖", 30 },
			{ 11, 256, 284, "要加油哦", 900 }, { 12, 286, 314, "神马也没有", 900 }, { 13, 316, 344, "谢谢参与", 900 } };

	// 抽奖并返回角度和奖项
	public static Object[] award(Object[][] prizeArr) {
		// 概率数组
		Integer obj[] = new Integer[prizeArr.length];
		for (int i = 0; i < prizeArr.length; i++) {
			obj[i] = (Integer) prizeArr[i][4];
		}
		Integer prizeId = getRand(obj); // 根据概率获取奖项id
		// 旋转角度
		int angle = new Random().nextInt((Integer) prizeArr[prizeId][2] - (Integer) prizeArr[prizeId][1])
				+ (Integer) prizeArr[prizeId][1];
		String msg = (String) prizeArr[prizeId][3];// 提示信息
		return new Object[] { angle, prizeId, msg };
	}

	// 根据概率获取奖项
	public static Integer getRand(Integer obj[]) {
		Integer result = null;
		try {
			int sum = 0;// 概率数组的总概率精度
			for (int i = 0; i < obj.length; i++) {
				sum += obj[i];
			}
			for (int i = 0; i < obj.length; i++) {// 概率数组循环
				int randomNum = new Random().nextInt(sum);// 随机生成1到sum的整数
				if (randomNum < obj[i]) {// 中奖
					result = i;
					break;
				} else {
					sum -= obj[i];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
