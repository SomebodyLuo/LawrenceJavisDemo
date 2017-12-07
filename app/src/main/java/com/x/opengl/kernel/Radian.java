package com.x.opengl.kernel;

/**
 * 弧度
 * @date   2013-08-08 11:10:10
 */
public class Radian {
	
	public static float ToDegreeFactor = (float) (180.0 / Math.PI);
	public static float ToRadianFactor = (float) (Math.PI / 180.0);
	
	public static float toDegree(float radian) {
		return radian * ToDegreeFactor;
	}
	
	public static float toRadian(float degree) {
		return degree * ToRadianFactor;
	}
}
