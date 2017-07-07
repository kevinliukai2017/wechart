package com.wechart.util;

import com.wechart.model.LatLng;

/**
 * 手机GPS坐标转火星坐标
 * 
 * @author k.c.liu
 *
 */
public class CoordinateUtil {

	private static double a = 6378245.0;
	private static double ee = 0.00669342162296594323;

	public static LatLng transformFromWGSToGCJ(LatLng wgLoc) {

		// 如果在国外，则默认不进行转换
		if (outOfChina(wgLoc.getLatitude(), wgLoc.getLongitude())) {
			return new LatLng(wgLoc.getLatitude(), wgLoc.getLongitude());
		}
		double dLat = transformLat(wgLoc.getLongitude() - 105.0, wgLoc.getLatitude() - 35.0);
		double dLon = transformLon(wgLoc.getLongitude() - 105.0, wgLoc.getLatitude() - 35.0);
		double radLat = wgLoc.getLatitude() / 180.0 * Math.PI;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * Math.PI);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * Math.PI);

		return new LatLng(wgLoc.getLatitude() + dLat, wgLoc.getLongitude() + dLon);
	}

	public static double transformLat(double x, double y) {
		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(x > 0 ? x : -x);
		ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0 * Math.PI)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(y / 12.0 * Math.PI) + 320 * Math.sin(y * Math.PI / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	public static double transformLon(double x, double y) {
		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(x > 0 ? x : -x);
		ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0 * Math.PI)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x / 30.0 * Math.PI)) * 2.0 / 3.0;
		return ret;
	}

	public static boolean outOfChina(double lat, double lon) {
		if (lon < 72.004 || lon > 137.8347)
			return true;
		if (lat < 0.8293 || lat > 55.8271)
			return true;
		return false;
	}
}
