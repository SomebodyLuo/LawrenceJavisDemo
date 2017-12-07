package com.x.opengl.kernel;

import com.x.Director;

/**
 * 相机,主要包含了两个东西，一个叫视口，一个叫视图
 * 凝视点暂时也放在这里
 * @date 2013-08-08 11:10:10
 */
public abstract class Camera  {

	private float		mFovy			= 90f;
	private float		mNear			= 0.5f;
	private float		mFar			= 2000.0f;
	
	

	protected void setViewPort(int left,int top,float width, float height) {
		Director.sGLESVersion.cameraSetUp(left,top,width, height, mFovy, mNear, mFar);
	}

	public float getFovy() {
		return mFovy;
	}

	public float[] getVisualDistance() {
		return new float[] { mNear, mFar };
	}


	public abstract void resetEye() ;
	public abstract void initViewPort() ;
	public abstract void updateWorldViewMatrix();
	public abstract void updateUiViewMatrix() ;
	/**
	 * 不同于上面的ui方法，这个方法是用于给ui增加重力效果
	 */
	public abstract void updateUiGravityViewMatrix() ;
	public abstract void setEyeMatrix(float[] matrix);

}
