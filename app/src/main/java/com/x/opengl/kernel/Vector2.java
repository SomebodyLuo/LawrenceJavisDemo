package com.x.opengl.kernel;

import android.graphics.PointF;

/**
 * 二维向量
 * @date   2013-08-08 11:10:10
 */
public class Vector2 implements Cloneable{

	public static final String  TAG        = "Vector2";
	public static final Vector2 UP_AXIS    = new Vector2(0, 1);
	public static final Vector2 DOWN_AXIS  = new Vector2(0, -1);
	public static final Vector2 LEFT_AXIS  = new Vector2(-1, 0);
	public static final Vector2 RIGHT_AXIS = new Vector2(1, 0);
	public static final Vector2 ZERO       = new Vector2(0, 0);
	public              float   X          = 0;
	public              float   Y          = 0;
	
	public Vector2() {
		
	}
	
	@Override
	public Vector2 clone() {
		Vector2 cloned = null;
		try {
			cloned = (Vector2) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			cloned.X = X;
			cloned.Y = Y;
		}
		
		return cloned;
	}
	
	public Vector2(Vector2 vector) {
		X = vector.X;
		Y = vector.Y;
	}
	
	public Vector2(float x, float y) {
		X = x;
		Y = y;
	}
	
	public Vector2(Vector3 vector) {
		X = vector.X;
		Y = vector.Y;
	}
	
	public Vector2(PointF src, PointF dst) {
		this.X = dst.x - src.x; 
		this.Y = dst.y - src.y; 
	}

	public boolean equals(Vector2 vector) {
		return equals(this, vector);
	}
	
	public void set(float x, float y) {
		X = x;
		Y = y;
	}
	
	/**
	 * 比较两个向量是否相等
	 * @param vectorA
	 * @param vectorB
	 * @return
	 */
	public static boolean equals(Vector2 vectorA, Vector2 vectorB) {
		return (vectorA.X == vectorB.X) && (vectorA.Y == vectorB.Y);
	}
	
	@Override
	public String toString() {
		return "(" + X + ", " + Y + ")";
	}

	/**
	 * 点的排序方式是 0  --------------  1
	 *           |         .        |
	 * 			 3  --------------	2 
	 * 中心到0点和中心到1点的向量叉乘指向屏幕里面(右手定则)，即叉乘小于0为true
	 * @param vector1
	 * @param vector2
	 * @return 如果向量积<0,表示三角形为顺时针，反之为逆时针
	 */
	public static boolean cross(Vector2 vector1, Vector2 vector2) {
		return (vector1.X * vector2.Y -  vector2.X * vector1.Y) < 0;
	}
	/**
	 * @param vector1
	 * @param vector2
	 * @return 如果向量积<0,表示三角形为顺时针，反之为逆时针
	 */
	public static boolean isCWcross(Vector2 vector1, Vector2 vector2) {
		return (vector1.X * vector2.Y -  vector2.X * vector1.Y) < 0;
	}

}
