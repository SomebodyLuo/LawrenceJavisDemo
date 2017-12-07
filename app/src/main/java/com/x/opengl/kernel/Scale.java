package com.x.opengl.kernel;

/**
 * 缩放向量
 * @date   2013-08-08 11:10:10
 */
public class Scale extends Vector3{

	public static final String TAG = "Scale";
	
	public Scale() {
		this.X = 1;
		this.Y = 1;
		this.Z = 1;
	}
	
	public Scale(float x, float y, float z) {
		this.X = x;
		this.Y = y;
		this.Z = z;
	}

	public void set(Vector3 scale) {
		this.X = scale.X;
		this.Y = scale.Y;
		this.Z = scale.Z;
	}
	
	public void set(float x, float y, float z) {
		this.X = x;
		this.Y = y;
		this.Z = z;
	}

	public void multil(Scale scale) {
		this.X *= scale.X;
		this.Y *= scale.Y;
		this.Z *= scale.Z;
	}

	public void devide(Scale scale) {

		this.X /= scale.X;
		this.Y /= scale.Y;
		this.Z /= scale.Z;
	}
}
