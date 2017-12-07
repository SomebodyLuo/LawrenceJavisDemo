package com.x.opengl.kernel;

/**
 * 旋转向量
 * @date   2013-08-08 11:10:10
 */
public class Rotate extends Vector3{

	public  static final String TAG        = "Rotate";
	
	public               Quat   Quaternion = new Quat();
	
	public Rotate() {

	}
	
	public Rotate(Quat quat) {
		setQuatRotate(quat);
	}
	
	public Rotate(float x, float y, float z) {
		setRotate(x, y, z);
	}

	public Quat getQuatRotate(){
		return Quaternion;
	}
	
	public void setQuatRotate(Quat quaternion) {
		if (quaternion != null) {
			Quaternion.W = quaternion.W;
			Quaternion.X = quaternion.X;
			Quaternion.Y = quaternion.Y;
			Quaternion.Z = quaternion.Z;
		}
	}
	
	public void setRotate(float x, float y, float z) {
		X = x; 
		Y = y;
		Z = z;
		Quaternion = Quat.toQuat(X, Y, Z);
	}

	public void add(Rotate rotate) {
		this.X += rotate.X;
		this.Y += rotate.Y;
		this.Z += rotate.Z;
	}

	public void set(Rotate rotate) {
		this.X = rotate.X;
		this.Y = rotate.Y;
		this.Z = rotate.Z;
	}
}
