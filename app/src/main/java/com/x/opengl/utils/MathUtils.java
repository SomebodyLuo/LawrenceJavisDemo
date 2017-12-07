package com.x.opengl.utils;

public class MathUtils {

	/**
	 * 将src值从[srcLowerLimit, srcUpperLimit]映射到[destLowerLimit, destUpperLimit]
	 * @param src 要用于映射的[srcLowerLimit, srcUpperLimit]空间中的值
	 * @param srcLowerLimit src值域的下限
	 * @param srcUpperLimit src值域的上限
	 * @param destLowerLimit src要被映射到的值域空间下限
	 * @param destUpperLimit src要被映射到的值域空间上限
	 * @return
	 */
	public static float mappingRange(float src, float srcLowerLimit, float srcUpperLimit, float destLowerLimit, float destUpperLimit) {
		float srcRange     = srcUpperLimit - srcLowerLimit;
		float destRange    = destUpperLimit - destLowerLimit;
		float srcCenter    = srcLowerLimit + srcRange * 0.5f;
		float destCenter   = destLowerLimit + destRange * 0.5f;
		return ((src - srcCenter) / srcRange) * destRange + destCenter;
	}
	
	/**
	 * 把角度限制在minAngle和maxAngle之间
	 * @param angle
	 * @param minAngle
	 * @param maxAngle
	 * @return
	 */
	public static float clampAngle(float angle, float minAngle, float maxAngle) {
		angle %= 360;
		if (angle < minAngle) angle = minAngle;
		else if (angle > maxAngle) angle = maxAngle;
		return angle;
	}
	
	/**
	 * 把毫秒时间转换为时间格式
	 * @param millSecond
	 * @return
	 */
	public static String millSecondToTime(long millSecond) {
		String result = "00:00";
		int hour      = 0;
		int minute    = 0;
		int second    = 0;
		
		second = (int) (millSecond / 1000 % 60);
		minute = (int) ((millSecond / 1000 / 60) % 60);
		hour   = (int) ((millSecond / 1000 / 60) / 60);
		
		if(hour == 0){
			result = String.format("%02d:%02d", minute, second);
		}else{
			result = String.format("%d:%02d:%02d", hour, minute, second);
		}
		
		return result;
	}
}
