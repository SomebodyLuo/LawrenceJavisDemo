package com.x;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.PixelFormat;
import android.opengl.EGL14;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.hello.scene_for_vr.collisionbox.CollisionBoxView;
import com.x.opengl.gles.GLES;
import com.x.opengl.gles.T_GLES20;
import com.x.opengl.kernel.Texture;
import com.x.components.task.AsynDisposeTask;
import com.x.components.task.AsynInitializeTask;
import com.x.components.task.AsynTask;
import com.x.components.task.AsynTaskExecutor;

@SuppressLint("NewApi")
public abstract class EngineGLView extends GLSurfaceView {


	public final static int		OPENGLES_1_1					= 0x0001;
	public final static int		OPENGLES_2_0					= 0x0002;
	public final static int		OPENGLES_3_0					= 0x0003;
	
	
	public final static int		HANDLE_REQUEST_RENDER_DELAY		= 0x0001;
	public final static int		HANDLE_READY_TO_RENDER			= 0x0002;
	public final static int		HANDLE_REQUEST_QUIT				= 0x0003;
	
	
	public final static int 	HANDLE_REQUEST_SHOW_AABBBOX_LINE = 0x0010;
	

	public AsynTaskExecutor		mAsynTaskExecutor	= null;
	protected abstract Director shareDirector(Context context);

	public interface OnWolfStateChangedListener {
		void onWolfStateChanged(int state);

		void onWolfReadyToRender();
	}

	public EngineGLView(Context context) {
		super(context);
		initialize(context);
		initDirector(context);
	}

	public EngineGLView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
		initDirector(context);
	}

	private void initDirector(Context context) {
		setRenderer(shareDirector(context));
	}
	public void setRenderer(Renderer renderer) {
		super.setRenderer(renderer);
		mDirector = (Director) renderer;
		mDirector.setGLES(mGLESVersion);
		setRenderMode(RENDERMODE_WHEN_DIRTY);
//		setDebugFlags(DEBUG_CHECK_GL_ERROR);
//		setFocusable(true);
//		setFocusableInTouchMode(true);
//		requestFocus();
	}
	private void initialize(Context context) {
		
		
//		setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
		mContext = context;
		mGLEnvirnment = new GLEnvirnment();

		mEGLContextClientVersion = getAvaliableOpenGLESAPI(OPENGLES_2_0);// 初始化opengles版本
//		 mEGLContextClientVersion = getAvaliableOpenGLESAPI(OPENGLES_1_1);//
		// 初始化opengles版本

		// luoyouren
		mGLESVersion = new T_GLES20();

		setEGLContextClientVersion(mEGLContextClientVersion);

		// 选择一个最优的EGL配置，若支持8倍抗锯齿，则使用8倍抗锯齿，或者选择一个相对比较好的EGL配置,这里采用8倍抗锯齿很卡，所以暂不使用
		
		// 选择合适的egl的配置，同时检查异步纹理贴图的可行性
//		eglConfig(mGLEnvirnment, mEGLContextClientVersion);
//		eglContextFactory(mGLEnvirnment);// 初始化egl环境，用于后期的是否支持egl共享作判定


		// important: view置顶并且设置透明
		this.getHolder().setFormat(PixelFormat.TRANSPARENT);
		this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		this.setZOrderMediaOverlay(true);
		this.setZOrderOnTop(true);


		mHandler = new Handler() {

			@Override
			public void handleMessage(final Message msg) {
				switch (msg.what)
					{
				
					case HANDLE_REQUEST_RENDER_DELAY: {
						requestRender();
					}
					break;

					case HANDLE_READY_TO_RENDER: {
						if (mOnWolfStateChangedListener != null) {
							mOnWolfStateChangedListener.onWolfReadyToRender();
						}
					}
					break;

					case HANDLE_REQUEST_QUIT: {
						((Activity) mContext).finish();
					}

					break;
					case  HANDLE_REQUEST_SHOW_AABBBOX_LINE:
						mCollisionBoxView.update(msg.obj);
					break;
			
					default:
					break;
					}
			}

			

		};
//		getHolder().setFormat(PixelFormat.TRANSPARENT);
//		setZOrderOnTop(true);
	}

	public void setBackgroundTransparentEnable(boolean enabled) {
		mBackgroundTransparentEnable = enabled;
		queueEvent(new Runnable() {

			@Override
			public void run() {
				if (mBackgroundTransparentEnable) {
					getHolder().setFormat(PixelFormat.TRANSPARENT);
					setZOrderOnTop(true);
				} else {
					getHolder().setFormat(PixelFormat.OPAQUE);
					setZOrderOnTop(false);
				}
			}
		});
	}

	public void setOnWolfStateChangedListener(OnWolfStateChangedListener listener) {
		mOnWolfStateChangedListener = listener;
	}

	public GLEnvirnment getGLEnvirnment() {
		return mGLEnvirnment;
	}

	public boolean getBackgroundTransparentEnable() {
		return mBackgroundTransparentEnable;
	}


	public void requestRender(long delay) {
		if (mHandler != null) {
			mHandler.sendEmptyMessageDelayed(HANDLE_REQUEST_RENDER_DELAY, delay);
		}
	}

	public void requestRender() {

//		MLog.d("WolfView requestRender()");
		super.requestRender();
	}

	public void queueEvent(final Runnable runnable, final long delayMilliSecond) {
		postDelayed(new Runnable() {

			@Override
			public void run() {
				queueEvent(runnable);
			}

		}, delayMilliSecond);
	};

	public void quit() {
		if (mHandler != null) {
			mHandler.sendEmptyMessage(HANDLE_REQUEST_QUIT);
		}
	}

	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {

//		if ((event.getSource() & InputDevice.SOURCE_CLASS_POINTER) != 0) {
//			switch (event.getAction())
//				{
//				case MotionEvent.ACTION_SCROLL: {
//					final float vscroll = event.getAxisValue(MotionEvent.AXIS_VSCROLL);
//					if (vscroll != 0) {
//						if (mDirector != null) {
//							mDirector.dispatchWheelEvent(vscroll);
//						}
//					}
//				}
//				}
//		}
		return super.onGenericMotionEvent(event);
	}

//	@Override
//	public boolean onTouchEvent(final MotionEvent event) {
//		if (mDirector == null) {
//			return false;
//		} else {
//			queueEvent(new Runnable() {
//
//				@Override
//				public void run() {
//					mDirector.dispatchTouchEvet(event);
//				}
//
//			});
//
//			return true;
//		}
//	}

	@Override
	public boolean dispatchTouchEvent(final MotionEvent event) {
		// luoyouren 从这里开始分发
		final MotionEvent newEvent = MotionEvent.obtain(event);
		newEvent.setLocation(event.getX(), event.getY());

		if (mDirector == null) {
			
		} else {


			queueEvent(new Runnable() {

				@Override
				public void run() {
					mDirector.dispatchTouchEvent(newEvent);
				}

			});

			return true;
		}
		return super.dispatchTouchEvent(event);
	}

	@Override
	public boolean dispatchGenericMotionEvent(final MotionEvent event) {
		// TODO Auto-generated method stub
//		Log.d("wheel", " onGenericMotionEvent "+event.getAction());
		
		if (mDirector == null) {
		} else {
			queueEvent(new Runnable() {

				@Override
				public void run() {
					
					if ((event.getSource() & InputDevice.SOURCE_CLASS_POINTER) != 0) {
						switch (event.getAction())
							{
							case MotionEvent.ACTION_SCROLL: {
								final float vscroll = event.getAxisValue(MotionEvent.AXIS_VSCROLL);
								if (vscroll != 0) {
									if (mDirector != null) {
										mDirector.dispatchWheelEvent(vscroll);
									}
								}
								
							}
						}
					}
					
//					mDirector.dispatchGenericMotionEvent(event);
				}

			});

		}
		return super.dispatchGenericMotionEvent(event);
//		return false;
	}
	/*
	 * 由主界面分发事件
	 */
	public boolean dispatchGlKeyEvent(final KeyEvent event) {
		if (mDirector == null) {
			return false;
		}
		int keyCode = event.getKeyCode();
		switch (keyCode)
			{
				case KeyEvent.KEYCODE_DPAD_CENTER:
				case KeyEvent.KEYCODE_DPAD_UP:
				case KeyEvent.KEYCODE_DPAD_DOWN:
				case KeyEvent.KEYCODE_DPAD_LEFT:
				case KeyEvent.KEYCODE_DPAD_RIGHT:
				case KeyEvent.KEYCODE_BACK:
				case KeyEvent.KEYCODE_ESCAPE:
				case KeyEvent.KEYCODE_ENTER:
				case KeyEvent.KEYCODE_MENU: 
				case KeyEvent.KEYCODE_0:
				case KeyEvent.KEYCODE_1:
				case KeyEvent.KEYCODE_2:
				case KeyEvent.KEYCODE_3:
				case KeyEvent.KEYCODE_4:
				case KeyEvent.KEYCODE_5:
				case KeyEvent.KEYCODE_6:
				case KeyEvent.KEYCODE_7:
				case KeyEvent.KEYCODE_8:
				case KeyEvent.KEYCODE_9:
					{
	
					queueEvent(new Runnable() {
	
						@Override
						public void run() {
							mDirector.dispatchKeyEvent(event);
						}
	
					});
					if(isUserInteruptKeyEvent(event)){
						return true;
					}
				}
			}
		return false;
	}
	protected boolean  isUserInteruptKeyEvent (KeyEvent event) {
		return false;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		this.mWidth = right - left;
		this.mHeight = bottom - top;
	}

	private void eglContextFactory(final GLEnvirnment glEnvirnment) {
		setEGLContextFactory(new EGLContextFactory() {

			private int	EGL_CONTEXT_CLIENT_VERSION	= 0x3098;

			public EGLContext createContext(EGL10 egl, EGLDisplay eglDisplay, EGLConfig eglConfig) {
				int[] attrib_list = { EGL_CONTEXT_CLIENT_VERSION, mEGLContextClientVersion, EGL10.EGL_NONE };

//				MLog.d("aaa","createContext");

				EGLContext wolfEglContext = egl.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT,
						mEGLContextClientVersion != 0 ? attrib_list : null);
			
				if(glEnvirnment.mIsSupportAsyncTexture){
					/////plan A///////////	
					glEnvirnment.mEgl = egl;
					glEnvirnment.mEglDisplay = eglDisplay;
					glEnvirnment.mEglConfig = eglConfig;
					glEnvirnment.mEglContext = wolfEglContext; //直接取wolfEglContext声明为wolf的egl环境
					
					/////plan B///////////
//					glEnvirnment.mEgl = egl;
//					glEnvirnment.mEglDisplay = eglDisplay;
//					glEnvirnment.mEglConfig = eglConfig;
//					glEnvirnment.mEglContext = egl.eglCreateContext(eglDisplay, eglConfig, wolfEglContext,
//							mEGLContextClientVersion != 0 ? attrib_list : null);//对wolfEglContext进行一个代理共享操作，然后将该代理声明为wolf的egl环境
					
					mAsynTaskExecutor	= new AsynTaskExecutor();
					mAsynTaskExecutor.setTaskExecutorInitializeTask(new AsynInitializeTask() {

						
						private int              mBufferAttribs[] = { 
								EGL10.EGL_WIDTH,  1,
								EGL10.EGL_HEIGHT, 1,
								EGL14.EGL_TEXTURE_TARGET, EGL14.EGL_NO_TEXTURE, 
				                EGL14.EGL_TEXTURE_FORMAT, EGL14.EGL_NO_TEXTURE,
				                EGL10.EGL_NONE };
						
						@Override
						public Object initializedRun() {
							
//							MLog.d("aaa",Thread.currentThread().getId()  + "======initializedRun===");

							GLEnvirnment threadGLEnvirnment = new GLEnvirnment();
							
							GLEnvirnment wolfGLEnvirnment = glEnvirnment;
							// 通过wolf引擎环境去初始化线程环境，使线程具有和wolf引擎共享eglContext,这样线程就可以用来进行纹理的加载工作了。
							if (wolfGLEnvirnment != null) {
								
								threadGLEnvirnment.mEgl = wolfGLEnvirnment.mEgl;
								threadGLEnvirnment.mEglConfig = wolfGLEnvirnment.mEglConfig;
								threadGLEnvirnment.mEglDisplay = wolfGLEnvirnment.mEglDisplay;
								threadGLEnvirnment.mEglContext = threadGLEnvirnment.mEgl.eglCreateContext(wolfGLEnvirnment.mEglDisplay, wolfGLEnvirnment.mEglConfig, wolfGLEnvirnment.mEglContext, null);
								threadGLEnvirnment.mEglSurface = threadGLEnvirnment.mEgl.eglCreatePbufferSurface(wolfGLEnvirnment.mEglDisplay, wolfGLEnvirnment.mEglConfig, mBufferAttribs); 

								threadGLEnvirnment.mEgl.eglMakeCurrent(threadGLEnvirnment.mEglDisplay, threadGLEnvirnment.mEglSurface, threadGLEnvirnment.mEglSurface, threadGLEnvirnment.mEglContext);
							}
							
						    return threadGLEnvirnment;
						}
					});
					
					mAsynTaskExecutor.setTaskExecutorDisposeTask(new AsynDisposeTask() {
						
						@Override
						public void disposeRun(Object environment) {
//							MLog.d("aaa",Thread.currentThread().getId()  + "======disposeRun===");
							GLEnvirnment threadGLEnvirnment = (GLEnvirnment) environment;
							threadGLEnvirnment.mEgl.eglDestroyContext(threadGLEnvirnment.mEglDisplay, threadGLEnvirnment.mEglContext);
							threadGLEnvirnment.mEgl.eglDestroySurface(threadGLEnvirnment.mEglDisplay, threadGLEnvirnment.mEglSurface);
						}
					});
					
//					开启异步线程任务，，这里暂时注释掉
//					mAsynTaskExecutor.startAllWorkThread();
				}
						

				return wolfEglContext;
			}

			@Override
			public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context) {
				egl.eglDestroyContext(display, context);
				if(glEnvirnment.mIsSupportAsyncTexture){
					mAsynTaskExecutor.stopAllWorkThread();
				}
			}

		});

	}

	private void eglConfig(final GLEnvirnment glEnvirnment,final int eglContextClientVersion) {


//		setEGLConfigChooser(8, 8, 8, 8, 16, 0); 
//		setEGLConfigChooser(new EGLConfigChooser() {
//			public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
//
//				//理想的配置
//				ConfigSpec configuration = new IdealConfiguration(glEnvirnment);
//				EGLConfig eglConfig = configuration.findEGLConfig(eglContextClientVersion,egl,display);
//				if(eglConfig != null){
////					MLog.d("成功获取理想EGL配置！！");
//					return eglConfig;
//				}
//
//				//尚可的，差强人意的配置
//				configuration = new PassableConfiguration(glEnvirnment);
//				eglConfig = configuration.findEGLConfig(eglContextClientVersion, egl, display);
//				if(eglConfig != null){
////					MLog.d("成功获取自定义的EGL配置！！");
//					return eglConfig;
//				}
//				
//				//默认配置
//				configuration = new AndroidDefaultConfiguration(glEnvirnment);
//				eglConfig = configuration.findEGLConfig(eglContextClientVersion, egl, display);
//				if(eglConfig != null){
////					MLog.d("成功获取android默认的EGL配置！！");
//					return eglConfig;
//				}
//				
//				return null;
//			}
//		});
	}

	private int getAvaliableOpenGLESAPI(int apiVersion) {

		ActivityManager activityManager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

		if (apiVersion == OPENGLES_3_0) {
			if (configurationInfo.reqGlEsVersion >= 0x00030000) {
				return OPENGLES_3_0;
			} else {
				return getAvaliableOpenGLESAPI(OPENGLES_2_0);
			}
		} else if (apiVersion == OPENGLES_2_0) {
			if (configurationInfo.reqGlEsVersion >= 0x00020000) {
				return OPENGLES_2_0;
			} else {
				return getAvaliableOpenGLESAPI(OPENGLES_1_1);
			}
		} else {
			return OPENGLES_1_1;
		}

	}

	public void buildShowAABBBoxMessage(Object obj) {
		
		mHandler.removeMessages(HANDLE_REQUEST_SHOW_AABBBOX_LINE);
		
		Message message = mHandler.obtainMessage();
		message.obj = obj;
		message.what = HANDLE_REQUEST_SHOW_AABBBOX_LINE;
		message.sendToTarget();
		
	}
	public boolean request(AsynTask asynTask) {
		boolean async = false;
		if(mGLEnvirnment.mIsSupportAsyncTexture && mGLEnvirnment.mIsSupportAsyncTextureInRuntime){//是否支持异步纹理贴图
			mAsynTaskExecutor.startAllWorkThread();//只有当有使用异步纹理的需求才开放异步线程
			if(mGLEnvirnment.mIsCheck){ //对运行时是否支持异步纹理进行确认
				async = mAsynTaskExecutor.request(asynTask);
				return true;//结果没返回之前，异步纹理请求不受理，直接返回交由主循环进行同步纹理解析。
			}
			mAsynTaskExecutor.request(new AsynTask() {
				
				@Override
				public void run(Object envirnment) {

					if(!mGLEnvirnment.mIsCheck){

						Bitmap bitmap = Bitmap.createBitmap(1, 1, Config.ARGB_8888);
						Texture texture = Director.getInstance().sResourcer.generateTexture(bitmap);
						if(texture.isValid()){
							mGLEnvirnment.mIsSupportAsyncTextureInRuntime = true;

//							MLog.E(  "Not Support Runtime AsyncTexture ");
						}else{
							mGLEnvirnment.mIsSupportAsyncTextureInRuntime = false;

//							MLog.E(  "Not Support Runtime AsyncTexture ");
						}
						mGLEnvirnment.mIsCheck = true;
					}
				}
				
				@Override
				public void onCanceled(Object envirnment) {
					
				}
			});
		}else{
//			MLog.E(  "Not Support AsyncTexture ");
		}
		return async;
	}

	private int							mEGLContextClientVersion		= OPENGLES_1_1;
	private int							mFSAASamples					= 0;
	private boolean						mBackgroundTransparentEnable	= false;
	private Context						mContext						= null;
	private GLEnvirnment				mGLEnvirnment					= null;
	private Handler						mHandler						= null;
	private Director					mDirector						= null;
	private OnWolfStateChangedListener	mOnWolfStateChangedListener	= null;
	private int							mWidth;
	private int							mHeight;

	private GLES						mGLESVersion;
	private CollisionBoxView 			mCollisionBoxView;
	public void setCollisionBoxView(CollisionBoxView cv) {
		this.mCollisionBoxView = cv;
	}
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		Log.d("ming", "onAttachedToWindow ");

        AssetManager assetManager = mContext.getAssets();
        String pathToInternalDir = mContext.getFilesDir().getAbsolutePath();
        // call the native constructors to create an object
      //  JniNativeLoad. CreateObjectNative(assetManager, pathToInternalDir);
	}
	@Override
	protected void onDetachedFromWindow() {
		Log.d("ming", "onDetachedFromWindow ");
		
		super.onDetachedFromWindow();
		mDirector.onSurfaceDestroyed();
		//JniNativeLoad .DeleteObjectNative();		
	}
	
	


}
