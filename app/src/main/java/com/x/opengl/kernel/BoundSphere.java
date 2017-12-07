package com.x.opengl.kernel;

/**
 * 包围球
 * @date   2013-10-26 10:49:52
 */
public class BoundSphere implements Cloneable{

	public Vector3 CenterPoint = new Vector3();
	public float   Radius      = 0;
	
	public BoundSphere() {
		
	}
	
	@Override
	protected BoundSphere clone() {
		BoundSphere cloned = null;
		try {
			cloned = (BoundSphere) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			cloned = new BoundSphere();
		}
		cloned.CenterPoint = CenterPoint.clone();
		return cloned;
	}
	
	public BoundSphere(Vector3 center, float radius) {
		CenterPoint = center;
		Radius = radius;
	}
	
	public void generateBoundSphere(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		float distanceX = maxX - minX;
		float distanceY = maxY - minY;
		float distanceZ = maxZ - minZ;
		
		CenterPoint.X = minX + distanceX / 2f;
		CenterPoint.Y = minY + distanceY / 2f;
		CenterPoint.Z = minZ + distanceZ / 2f;
		Radius        = (float) Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2) + Math.pow(distanceZ, 2)) / 2f;
	}
	
	public Vector3 getCenter() {
		return CenterPoint;
	}
	
	public float[] getCenterArray() {
		return new float[]{CenterPoint.X, CenterPoint.Y, CenterPoint.Z};
	}
	
	public float getRadius() {
		return Radius;
	}
}
