package com.x.opengl.info;

import android.content.pm.ActivityInfo;

/**
 * 引擎运行信息
 * @date   2013-10-09 11:00:33
 */
public class EngineInfo {
	
	public static int SCREEN_LANDSCAPE = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE; // 屏幕朝向：横向
	public static int SCREEN_PORTRAIT  = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;  // 屏幕朝向：纵向
	
	private   boolean mIsLandscape = true; // 屏幕朝向
	private   float   mFPS         = 0;    // 帧速率
	protected long    mDeltaTime   = 0;    // 上一帧与这一帧的时间差
	protected long    mLastTime    = 0;    // 上一帧的时间
	
	public EngineInfo() {
		
	}

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
