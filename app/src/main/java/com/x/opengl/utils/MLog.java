package com.x.opengl.utils;

import javax.microedition.khronos.egl.EGL10;

import android.util.Log;

public class MLog {

	private static  boolean mDebug = true;
	private static String mTag = "ming";
	public static void d(String msg) {
		if(mDebug){
			Log.d(mTag, msg);
		}
	}
	public static void d(String tag, String msg) {
		if(mDebug){
			Log.d(tag, msg);
		}
	}
	public static void E(  String msg) {
		Log.e(mTag, msg);
	}

    public static void throwEglException(String message,EGL10 egl) {
        Log.e("EglHelper","error = " + egl.eglGetError()+ "   >>  throwEglException tid=" + Thread.currentThread().getId() + " "
        		+ "   >>  ThreadName = " + Thread.currentThread().getName()
                + "   " + message );
        throw new RuntimeException(message);
    }
	
}
