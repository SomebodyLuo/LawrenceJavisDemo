package com.x.opengl.kernel;

/**
 * 位置向量
 * @date   2013-08-08 11:10:10
 */
public class Position extends Vector3{
	
	public static final String TAG = "Position";
	
	public Position() {
		
	}
	
	public Position(float x, float y, float z) {
		this.X = x;
		this.Y = y;
		this.Z = z;
	}

	public void set(Vector3 position) {
		this.X = position.X;
		this.Y = position.Y;
		this.Z = position.Z;
	}

	public String toString(){
		return "{x = "+X+", y = "+Y +", z = "+Z +"}";
	}

	public void add(Position position) {
		this.X += position.X;
		this.Y += position.Y;
		this.Z += position.Z;
	}
	public void sub(Position position) {
		this.X -= position.X;
		this.Y -= position.Y;
		this.Z -= position.Z;
	}
}
