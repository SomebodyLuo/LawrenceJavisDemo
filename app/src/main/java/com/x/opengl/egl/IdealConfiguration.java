package com.x.opengl.egl;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

import com.x.GLEnvirnment;

/**
 * 理想的配置,
 * 满足深度测试条件 EGL10.EGL_DEPTH_SIZE > 0，
 * 透明度测试条件 EGL10.EGL_ALPHA_SIZE > 0，
 * 模版测试条件EGL_STENCIL_SIZE > 0，
 * 满足异步贴图渲染 EGL10.EGL_SURFACE_TYPE 必须为 EGL10.EGL_PBUFFER_BIT|EGL10.EGL_PIXMAP_BIT中的一个，而不能是EGL_WINDOW_BIT
 * 支持多重采样 EGL10.EGL_SAMPLE_BUFFERS > 0
 * 多少倍的采样率 EGL10.EGL_SAMPLES每个像素的采样率,采样当然是更高更清晰，但伴随性能会有下降
 * @author Administrator
 *
 */
public class IdealConfiguration extends ConfigSpec{

	private int		mType			= EGL10.EGL_PBUFFER_BIT|EGL10.EGL_PIXMAP_BIT;
	private int		mRedSize		= 8;
	private int		mGreenSize		= 8;
	private int		mBlueSize		= 8;
	private int		mAlphaSize		= 8;
	private int		mDepthSize		= 24;
	private int		mStencilSize	= 8;
	private int		mSampleBuffers	= 1;
	private int		mSamples		= 4;

	int[]			mConfigSpec		= new int[] { 
			EGL10.EGL_SURFACE_TYPE, mType, 
			EGL10.EGL_ALPHA_SIZE, mAlphaSize, 
			EGL10.EGL_RED_SIZE,	  mRedSize, 
			EGL10.EGL_GREEN_SIZE, mGreenSize, 
			EGL10.EGL_BLUE_SIZE, mBlueSize, 
			EGL10.EGL_DEPTH_SIZE,mDepthSize, 
			EGL10.EGL_STENCIL_SIZE, mStencilSize, 
			EGL10.EGL_SAMPLE_BUFFERS, mSampleBuffers,
			EGL10.EGL_SAMPLES, mSamples, 
			EGL10.EGL_NONE // mConfigSpec结束标志
									};
	public IdealConfiguration(GLEnvirnment glEnvirnment){
		super(glEnvirnment);
	}
	@Override
	public EGLConfig findEGLConfig(int eglContextClientVersion,EGL10 egl, EGLDisplay display) {
		
		mConfigSpec = filterConfigSpec(eglContextClientVersion,mConfigSpec);
		return chooseBy(mConfigSpec,egl,display);
	}

	@Override
	public boolean compare(int surface_type, int depth, int stencil, int sample_buffer, int samples, int red, int green, int blue, int alpha) {

		boolean contidion = true;
		contidion &= (depth >= mDepthSize);
		contidion &= (stencil >= mStencilSize);
		contidion &= (samples >= mSamples);
		contidion &= (red >= mRedSize);
		contidion &= (green >= mGreenSize);
		contidion &= (blue >= mBlueSize);
		contidion &= (alpha >= mAlphaSize);
		return contidion;
	}


}
