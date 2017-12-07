package com.x.opengl.utils;

import android.util.Log;

/**
 * 引擎内部Log，有DEBUG标识控制输出
 * @date   2013-11-05 11:23:28
 */
public class CBLog {
	
	static private final boolean DEBUG = true;
	
	static public final void w(String TAG, String msg) {
		if (DEBUG) Log.w(TAG, msg);
	}
	
	static public final void e(String TAG, String msg) {
		if (DEBUG) Log.e(TAG, msg);
	}
	
	static public final void i(String TAG, String msg) {
		if (DEBUG) Log.i(TAG, msg);
	}
	
	static public final void d(String TAG, String msg) {
		if (DEBUG) Log.d(TAG, msg);
	}
	
	static public final void v(String TAG, String msg) {
		if (DEBUG) Log.v(TAG, msg);
	}
	

	static public final void w(Object object, String msg) {
		if (DEBUG){
			String tag = object.getClass().getSimpleName();
			Log.w(tag, msg);
		}
	}
	
	static public final void e(Object object, String msg) {
		if (DEBUG){
			String tag = object.getClass().getSimpleName();
			Log.e(tag, msg);
		} 
	}
	
	static public final void i(Object object, String msg) {
		if (DEBUG){
			String tag = object.getClass().getSimpleName();
			Log.i(tag, msg);
		}
		
	}
	
	static public final void d(Object object, String msg) {
		if (DEBUG){
			String tag = object.getClass().getSimpleName();
			Log.d(tag, msg);
		} 
	}
	
	static public final void v(Object object, String msg) {
		if (DEBUG){
			String tag = object.getClass().getSimpleName();
			Log.v(tag, msg);
		} 
	}
}
