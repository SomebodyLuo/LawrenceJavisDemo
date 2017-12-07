package com.x;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

import android.content.pm.ActivityInfo;

import com.x.opengl.egl.ConfigSpec.MConfig;

/**
 * GL运行环境和一些状态值
 * @date   2014-03-17 14:36:11
 */
public class GLEnvirnment {

	public EGL10      mEgl        = null;
	public EGLDisplay mEglDisplay = null;
	public EGLConfig  mEglConfig  = null;
	public EGLContext mEglContext = null;
	public EGLSurface mEglSurface = null;
	
	public int	mFSAASamples = 0;	//全屏抗锯齿采样数
	public boolean mIsSupportAsyncTexture ;//是否支持异步纹理贴图
	public boolean mIsSupportAsyncTextureInRuntime = true;//默认认为运行时也支持异步纹理，因此初始化为true,需要等待运行时重新确认
	public boolean	mIsCheck;
	public MConfig	mMConfig;
	
	public static int SCREEN_LANDSCAPE = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE; // 屏幕朝向：横向
	public static int SCREEN_PORTRAIT  = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;  // 屏幕朝向：纵向
	
	private   boolean mIsLandscape = true; // 屏幕朝向
	private   float   mFPS         = 0;    //Frames Per Second,每秒帧数
	protected long    mDeltaTime   = 0;    // 上一帧与这一帧的时间差
	protected long    mLastTime    = 0;    // 上一帧的时间
	

	/**
	 * 获取FPS值
	 * @return
	 */
	public float getFPS() {
		return mFPS;
	}
	
	/**
	 * 获取帧间隔时间
	 * @return
	 */
	public long getDeltaTime() {
		return mDeltaTime;
	}
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public long getCurrentTime() {
		return mLastTime;
	}
	
	/**
	 * 设置当前帧时间
	 * @param currentMillTime
	 */
	protected void setCurrentTime(long currentMillTime) {
		mDeltaTime = currentMillTime - mLastTime;
		mLastTime = currentMillTime;
		mFPS = 1000f / (float)mDeltaTime;
	}

	/**
	 * 设置屏幕朝向
	 * @param true 为横屏 Landscape
	 *        false 为竖屏 Portrait          
	 */
	public void setScreenOrientation(boolean isLandscape) {
		mIsLandscape = isLandscape;
	}
	
	/**
	 * 设置屏幕朝向
	 * @param orientation SCREEN_LANDSCAPE 横向
	 *                    SCREEN_PORTRAIT 纵向
	 */
	public void setScreenOrientation(int orientation) {
		mIsLandscape = orientation == SCREEN_PORTRAIT ? false : true;
	}
	
	/**
	 * 获取屏幕朝向
	 * @return
	 */
	public int getScreenOrientation() {
		return mIsLandscape ? SCREEN_LANDSCAPE : SCREEN_PORTRAIT;
	}

}
