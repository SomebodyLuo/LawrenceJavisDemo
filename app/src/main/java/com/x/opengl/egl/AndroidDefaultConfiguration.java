package com.x.opengl.egl;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

import com.x.GLEnvirnment;

/*
 * 
 * 
 */
public class AndroidDefaultConfiguration extends ConfigSpec {

	private int		mRedSize		= 8;
	private int		mGreenSize		= 8;
	private int		mBlueSize		= 8;
	private int		mAlphaSize		= 0;
	private int		mDepthSize		= 16;
	private int		mStencilSize	= 0;
	int[] mConfigSpec  = new int[] { //android GLSurfaceView默认使用的ConfigSpec
            EGL10.EGL_RED_SIZE, mRedSize,
            EGL10.EGL_GREEN_SIZE, mGreenSize,
            EGL10.EGL_BLUE_SIZE, mBlueSize,
            EGL10.EGL_ALPHA_SIZE, mAlphaSize,
            EGL10.EGL_DEPTH_SIZE, mDepthSize,
            EGL10.EGL_STENCIL_SIZE, mStencilSize,
            EGL10.EGL_NONE};


	public AndroidDefaultConfiguration(GLEnvirnment glEnvirnment){
		super(glEnvirnment);
	}
	@Override
	public EGLConfig findEGLConfig(int eglContextClientVersion, EGL10 egl, EGLDisplay display) {
		
		mConfigSpec = filterConfigSpec(eglContextClientVersion,mConfigSpec);
		return chooseBy(mConfigSpec,egl,display);
	}

	@Override
	public boolean compare(int surface_type, int depth, int stencil, int sample_buffer, int samples, int red, int green, int blue, int alpha) {

		boolean contidion = true;
		contidion &= (depth >= mDepthSize);
		contidion &= (stencil >= mStencilSize);
		contidion &= (red >= mRedSize);
		contidion &= (green >= mGreenSize);
		contidion &= (blue >= mBlueSize);
		contidion &= (alpha >= mAlphaSize);
		return contidion;
	}
}
