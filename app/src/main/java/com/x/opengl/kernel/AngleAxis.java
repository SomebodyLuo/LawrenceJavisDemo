package com.x.opengl.kernel;

/**
 * 轴角矩阵
 * @date   2013-08-08 11:10:10
 */
public class AngleAxis {
	public float Angle;
	public float xAxis;
	public float yAxis;
	public float zAxis;

	public AngleAxis() {
		Angle = 0;
		xAxis = 0;
		yAxis = 0;
		zAxis = 0;
	}
	public AngleAxis(float angle ,float x,float y,float z) {
		Angle = angle;
		xAxis = x;
		yAxis = y;
		zAxis = z;
	}
	@Override
	public String toString() {
		return "Angle :"+Angle+",,"+"xAxis :"+xAxis+",,"+"yAxis :"+yAxis+",,"+"zAxis:"+zAxis+",,";
	}
	
}