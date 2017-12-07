package com.x.opengl.kernel;

import java.io.Serializable;

import android.opengl.GLES20;

import com.x.Director;

/**
 * 贴图
 * @date   2013-08-08 11:10:10
 */
public class Texture implements Serializable{

	public static final String TAG = "Texture";
	
	public static final int INVALID_TEXTURE_ID = 0;
	
	private int     mTextureID = 0;
	private int     mWidth     = 0;
	private int     mHeight    = 0;
	private boolean mCanRecycle = true;//是否可回收，默认为可回收

	private int mBindTarget = GLES20.GL_TEXTURE_2D;
	
	public boolean isCanRecycle() {
		return mCanRecycle;
	}

	public void setCanRecycle(boolean canRecycle) {
		this.mCanRecycle = canRecycle;
	}

	public Texture() {
	}
	
	public int getTextureID() {
		return mTextureID;
	}
	
	public void setTextureID(int texture) {
		mTextureID = texture;
	}
	
	public void setWidth(int width) {
		mWidth = width;
	}
	
	public void setHeight(int height) {
		mHeight = height;
	}
	
	public int getWidth() {
		return mWidth;
	}
	
	public int getHeight() {
		return mHeight;
	}
	
	public float getRatio() {
		return (float)mWidth / (float)mHeight;
	}
	
	public void recycle() {
		if(!mCanRecycle){
			throw new RuntimeException("you cannot recycle this texture,please check your code");
		}
		Director.sGLESVersion.glDeleteTextures(new int[]{mTextureID});
	}

	public boolean equals(Texture texture) {
		if (texture != null) {
			return texture.getTextureID() == mTextureID;
		}else{
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "width:"+mWidth+",,height:"+mHeight+",,"+"mTextureID:"+mTextureID+",,";
	}

	public boolean isValid() {
		// 全志芯片的OpenGLES库中，仅仅0为无效ID，正数负数都是有效ID
		return mTextureID != 0 ;
	}

	public int getBindTarget() {
		return mBindTarget ;
	}

	public void setBindTarget(int glTextureTarget) {
		this.mBindTarget = glTextureTarget;
	}

	public float isBindTarget2d() {
		return  getBindTarget() == GLES20.GL_TEXTURE_2D ? 1.0f : 0.0f;
	}
//	public float isBindTarget2d() {
//		return  0.0f;
//	}
}
