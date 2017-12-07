package com.x.opengl.egl;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

import com.x.GLEnvirnment;
import com.x.EngineGLView;
import com.x.opengl.utils.MLog;

public abstract class ConfigSpec {

	public    final static int	OPENGLES_1_1	= EngineGLView.OPENGLES_1_1;
	public    final static int	OPENGLES_2_0	= EngineGLView.OPENGLES_2_0;
	public    final static int	OPENGLES_3_0	= EngineGLView.OPENGLES_3_0;
	protected GLEnvirnment		mGLEnviroment;

	public abstract EGLConfig findEGLConfig(int eglContextClientVersion, EGL10 egl, EGLDisplay display);
	public abstract boolean compare(int surface_types, int depth, int stencil, int sample_buffer, int samples, int red, int green, int blue, int alpha);

	public ConfigSpec(GLEnvirnment glEnviroment) {
		this.mGLEnviroment = glEnviroment;
	}

	protected EGLConfig chooseBy(int[] configSpec, EGL10 egl, EGLDisplay display) {
		int[] tempArray = new int[1];
		// / 第一步，以特定规则去选择可用的eglConfig,,,
		if (!egl.eglChooseConfig(display, configSpec, null, 0, tempArray)) {
			throw new IllegalArgumentException("eglChooseConfig configSpec failed");
		}
		int number = tempArray[0];
		if (number > 0) {
//			MLog.d("match configSpec success !");
			EGLConfig[] eglConfigs = new EGLConfig[number];

			if (!egl.eglChooseConfig(display, configSpec, eglConfigs, number, tempArray)) {
				throw new IllegalArgumentException("eglChooseConfig#2 failed");
			}
			EGLConfig config = chooseConfig(egl, display, eglConfigs);
			if (config == null) {
				throw new IllegalArgumentException("No config chosen");
			}
			return config;
		}
		return null;
	}

	protected int findConfigAttrib(EGL10 egl, EGLDisplay display, EGLConfig config, int attribute, int defaultValue) {

		int[] value = new int[1];
		if (egl.eglGetConfigAttrib(display, config, attribute, value)) {
			return value[0];
		}
		return defaultValue;
	}

	protected int[] filterConfigSpec(int eglContextClientVersion, int[] configSpec) {
		if (eglContextClientVersion != OPENGLES_2_0) {
			return configSpec;
		}
		/*
		 * We know none of the subclasses define EGL_RENDERABLE_TYPE. And we
		 * know the configSpec is well formed.
		 */
		int len = configSpec.length;
		int[] newConfigSpec = new int[len + 2];
		System.arraycopy(configSpec, 0, newConfigSpec, 0, len - 1);
		newConfigSpec[len - 1] = EGL10.EGL_RENDERABLE_TYPE;
		newConfigSpec[len] = 4; /* EGL_OPENGL_ES2_BIT */
		newConfigSpec[len + 1] = EGL10.EGL_NONE;
		return newConfigSpec;
	}

	private EGLConfig chooseConfig(EGL10 egl, EGLDisplay display, EGLConfig[] configs) {

		EGLConfig returnConfig = null;

		List<MConfig> avaliableEGLConfigList = new ArrayList<MConfig>();

//		MLog.d("configs.length = " + configs.length);
		for (int i = 0; i < configs.length; i++) {

			EGLConfig config = configs[i];

			String debug_message = "begin: ===========  " + "\n";

			int type = findConfigAttrib(egl, display, config, EGL10.EGL_SURFACE_TYPE, 0);
			int d = findConfigAttrib(egl, display, config, EGL10.EGL_DEPTH_SIZE, 0);
			int s = findConfigAttrib(egl, display, config, EGL10.EGL_STENCIL_SIZE, 0);
			int sb = findConfigAttrib(egl, display, config, EGL10.EGL_SAMPLE_BUFFERS, 0);
			int ss = findConfigAttrib(egl, display, config, EGL10.EGL_SAMPLES, 0);

			int r = findConfigAttrib(egl, display, config, EGL10.EGL_RED_SIZE, 0);
			int g = findConfigAttrib(egl, display, config, EGL10.EGL_GREEN_SIZE, 0);
			int b = findConfigAttrib(egl, display, config, EGL10.EGL_BLUE_SIZE, 0);
			int a = findConfigAttrib(egl, display, config, EGL10.EGL_ALPHA_SIZE, 0);

			debug_message += "EGL10.EGL_SURFACE_TYPE  = " + String.format("%x", type) + "\n";
			debug_message += "EGL10.EGL_DEPTH_SIZE  = " + d + "\n";
			debug_message += "EGL10.EGL_STENCIL_SIZE  = " + s + "\n";
			debug_message += "EGL10.EGL_SAMPLE_BUFFERS  = " + sb + "\n";
			debug_message += "EGL10.EGL_SAMPLES  = " + ss + "\n";

			debug_message += "EGL10.EGL_RED_SIZE  = " + r + "\n";
			debug_message += "EGL10.EGL_GREEN_SIZE  = " + g + "\n";
			debug_message += "EGL10.EGL_BLUE_SIZE  = " + b + "\n";
			debug_message += "EGL10.EGL_ALPHA_SIZE  = " + a + "\n";

			boolean avaliable = false;

			if (compare(type, d, s, sb, ss, r, g, b, a)) {
				MConfig mc = new MConfig(config, type, d, s, sb, ss, r, g, b, a);
				avaliableEGLConfigList.add(mc);
				avaliable = true;
			}
			debug_message += "avaliable  = " + avaliable + "\n";
			debug_message += "end: ===========  " + "\n";
			MLog.d(debug_message);
		}

//		MLog.d("count = " + avaliableEGLConfigList.size());

		for (int i = 0; i < avaliableEGLConfigList.size(); i++) {
			MConfig mconfig = avaliableEGLConfigList.get(i);
			EGLConfig eglConfig = mconfig.getEGLConfig();
			
			int result = mconfig.getSurfaceType() & (EGL10.EGL_PBUFFER_BIT | EGL10.EGL_PIXMAP_BIT);

			if (result > 0) { // 优选选取列表中支持异步纹理贴图的EGLConfig作为返回
				returnConfig = eglConfig;
				mGLEnviroment.mIsSupportAsyncTexture = true;
				mGLEnviroment.mMConfig = mconfig;
				return returnConfig;
			}
		}
		
		if (returnConfig == null && avaliableEGLConfigList.size() > 0) {

			MConfig mconfig = avaliableEGLConfigList.get(0);
			returnConfig = mconfig.getEGLConfig();
			mGLEnviroment.mMConfig = mconfig;
		}

		return returnConfig;
	}

	public static class MConfig {

		private int			mSurfaceType;
		private int			mDepth;
		private int			mStencil;
		private int			mSampleBuffers;
		private int			mSamples;
		private int			mRed;
		private int			mGreen;
		private int			mBlue;
		private int			mAlpha;
		private EGLConfig	mEGLConfig;

		public MConfig(EGLConfig config, int type, int d, int s, int sb, int ss, int r, int g, int b, int a) {
			this.mEGLConfig = config;
			this.mSurfaceType = type;
			this.mDepth = d;
			this.mStencil = s;
			this.mSampleBuffers = sb;
			this.mSamples = ss;
			this.mRed = r;
			this.mGreen = g;
			this.mBlue = b;
			this.mAlpha = a;
		}

		public EGLConfig getEGLConfig() {
			return mEGLConfig;
		}

		public int getSurfaceType() {
			return mSurfaceType;
		}

	}
}
