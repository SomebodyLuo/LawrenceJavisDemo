package com.x.opengl.kernel;

/**
 * 三维向量
 * @date   2013-08-08 11:10:10
 */
public class Vector3 extends Vector2 implements Cloneable{

	public static final String  TAG        = "Vector3";
	public static final Vector3 UP_AXIS    = new Vector3(0, 1, 0);
	public static final Vector3 DOWN_AXIS  = new Vector3(0, -1, 0);
	public static final Vector3 FRONT_AXIS = new Vector3(0, 0, 1);
	public static final Vector3 BACK_AXIS  = new Vector3(0, 0, -1);
	public static final Vector3 LEFT_AXIS  = new Vector3(-1, 0, 0);
	public static final Vector3 RIGHT_AXIS = new Vector3(1, 0, 0);
	public static final Vector3 ZERO       = new Vector3(0, 0, 0);
	public              float   Z          = 0;
	
	public Vector3() {
		
	}
	
	@Override
	public Vector3 clone() {
		Vector3 cloned = null;
		cloned = (Vector3) super.clone();
		cloned.X = X;
		cloned.Y = Y;
		cloned.Z = Z;
		return cloned;
	}

	public Vector3(Vector3 vector) {
		X = vector.X;
		Y = vector.Y;
		Z = vector.Z;
	}
	public Vector3(Vector3 source,Vector3 end) {
		X = end.X - source.X;	
		Y = end.Y - source.Y;	
		Z = end.Z - source.Z;
	}
	
	public Vector3(float x, float y, float z) {
		X = x;
		Y = y;
		Z = z;
	}
	
	public boolean equals(Vector3 vector) {
		return equals(this, vector);
	}
	
	/**
	 * 标准化
	 * 把向量转换为单位方向向量
	 * @return 向量的长度
	 */
	public float normalize() {
		float mag = magnitude(this);
		
		if (mag != 0) {
			this.X /= mag;
			this.Y /= mag;
			this.Z /= mag;
		}
		
		return mag;
	}
	
	/**
	 * 向量的长度
	 * @return
	 */
	public float magnitude(){
		return magnitude(this);
	}
	public Vector3 negative(){
		return new Vector3(-this.X,-this.Y,-this.Z);
	} 
	/**
	 * 比较两个向量是否相等
	 * @param vectorA
	 * @param vectorB
	 * @return
	 */
	public static boolean equals(Vector3 vectorA, Vector3 vectorB) {
		return (vectorA.X == vectorB.X) && (vectorA.Y == vectorB.Y) && (vectorA.Z == vectorB.Z);
	}
	
	/**
	 * 向量乘法
	 * @param vector
	 * @param factor
	 * @return
	 */
	public static Vector3 product(Vector3 vector, float factor) {
		return new Vector3(
				vector.X * factor,
				vector.Y * factor,
				vector.Z * factor);
	}
	
	/**
	 * 向量乘法
	 * @param vectorA
	 * @param vectorB
	 * @return
	 */
	public static Vector3 product(Vector3 vectorA, Vector3 vectorB) {
		return new Vector3(vectorA.X * vectorB.X, vectorA.Y * vectorB.Y, vectorA.Z * vectorB.Z);
	} 
		
	/**
	 * 向量叉乘
	 * 计算两个向量所围成的平面的法向量，这个向量为单位向量
	 * @param vectorA
	 * @param vectorB
	 * @return
	 */
	public static Vector3 crossProduct(Vector3 vectorA, Vector3 vectorB) {
        return new Vector3(
            vectorA.Y * vectorB.Z - vectorA.Z * vectorB.Y,
            vectorA.Z * vectorB.X - vectorA.X * vectorB.Z,
            vectorA.X * vectorB.Y - vectorA.Y * vectorB.X);
    }
	
	/**
	 * 计算向量的范数（长度）
	 * @param vector
	 * @return
	 */
	public static float magnitude(Vector3 vector) {
		return (float) Math.sqrt(Math.pow(vector.X, 2) + Math.pow(vector.Y, 2) + Math.pow(vector.Z, 2));
	}
	
	/**
	 * 向量平方长度
	 * @param vector
	 * @return
	 */
	public static float squaredLength(Vector3 vector) {
		return vector.X * vector.X + vector.Y * vector.Y + vector.Z * vector.Z;
	}
	
	public static float dotProduct(Vector3 vectorA, Vector3 vectorB) {
		return vectorA.X * vectorB.X + vectorA.Y * vectorB.Y + vectorA.Z * vectorB.Z;
	}
	
	public static Vector3 normalize(Vector3 vector) {
		float mag = magnitude(vector);
		
		if (mag != 0) {
			vector.X /= mag;
			vector.Y /= mag;
			vector.Z /= mag;
		}
		
		return vector;
	}
	
	/**
	 * 两个向量间的夹角
	 * @param vectorA
	 * @param vectorB
	 * @return
	 */
	public static float angleBetween(Vector3 vectorA, Vector3 vectorB) {
		float lengthProduce = Vector3.magnitude(vectorA) * Vector3.magnitude(vectorB);
		float factor        = 0;
		
		if (lengthProduce < 1E-8F) {
			lengthProduce = 1E-8F;
		}
		
		factor = Vector3.dotProduct(vectorA, vectorB) / lengthProduce;
//		factor = factor < -1.0f ? -1.0f : (factor > 1.0f ? 1.0f : factor);
		
		return (float) Math.acos(factor);
	}
	
	/**
	 * 向量加法：A + B
	 * AB = AO + OB;
	 * @param vectorA
	 * @param vectorB
	 * @return
	 */
	public static Vector3 addition(Vector3 vectorA, Vector3 vectorB) {
		return new Vector3(vectorA.X + vectorB.X, vectorA.Y + vectorB.Y, vectorA.Z + vectorB.Z);
	}
	
	/**
	 * 向量减法：A - B
	 * BA = OA - OB
	 * @param vectorA
	 * @param vectorB
	 * @return
	 */
	public static Vector3 subtraction(Vector3 vectorA, Vector3 vectorB) {
		return new Vector3(vectorA.X - vectorB.X, vectorA.Y - vectorB.Y, vectorA.Z - vectorB.Z);
	}
	
	/**
	 * 两点间的距离
	 * @param pointA
	 * @param PointB
	 * @return
	 */
	public static float distance(Vector3 pointA, Vector3 PointB) {
		return Vector3.magnitude(Vector3.subtraction(pointA, PointB));
	}
	
	/**
	 * 获取向量序列
	 * @return
	 */
	public float[] get() {
		return new float[]{X, Y, Z};
	}
	
	/**
	 * 设置向量序列
	 * @param src
	 */
	public void set(float[] src) {
		if (src == null) {
			X = 0;
			Y = 0; 
			Z = 0;
		}else{
			if (src.length == 1) {
				X = src[0];
				Y = 0;
				Z = 0;
			}else if (src.length == 2) {
				X = src[0];
				Y = src[1];
				Z = 0;
			}else if (src.length >= 3) {
				X = src[0];
				Y = src[1];
				Z = src[2];
			}else{
				X = 0;
				Y = 0; 
				Z = 0;
			}
		}
	}

	@Override
	public String toString() {
		return "(" + X + ", " + Y + ", " + Z + ")";
	}


}
