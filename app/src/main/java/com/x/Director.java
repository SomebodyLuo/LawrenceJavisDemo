package com.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.x.opengl.gles.GLES;
import com.x.opengl.kernel.EngineConstanst;
import com.x.opengl.kernel.GLHandler;
import com.x.opengl.kernel.Texture;
import com.x.opengl.kernel.assetloader.CBResourcer;
import com.x.components.listener.onAsyncTextureLoadListenner;
import com.x.opengl.kernel.sensor.AbsSensorStrategy;
import com.x.opengl.kernel.sensor.AbsSensorStrategy.EngineSensorListenr;
import com.x.opengl.kernel.sensor.SensorCardboardBlock;
import com.x.components.node.BlurScene;
import com.x.components.node.DialogXScene;
import com.x.components.node.SceneManager;
import com.x.components.node.ToastScene;
import com.x.components.node.View;
import com.x.components.node.XScene;
import com.x.components.task.AsynTask;
import com.x.components.task.FocusRunnable;
import com.x.components.task.PostRunnable;
import com.x.components.task.TextureRunnable;

@SuppressLint("NewApi")
public class Director implements Renderer {

	private static Director		sDirector;
	public static CBResourcer	sResourcer		= null;							// 资源解析者
	public static GLES			sGLESVersion;										// 不同的gles版本

	protected EngineGLView		mEngineGLView		= null;
	protected Context			mContext		= null;
	protected Handler			mHandler		= null;

	private SceneManager		mSceneManager	= new SceneManager();

	private List<PostRunnable>	mPostList		= new ArrayList<PostRunnable>();
	private List<FocusRunnable>	mFocusPostList	= new ArrayList<FocusRunnable>();

	// luoyouren: 传感器数据
	private AbsSensorStrategy mAbsSensorStrategy   = new SensorCardboardBlock( );
	private boolean							mInitFlag;
	private boolean 						mIsVR = false;

    private GLHandler mGlHandler  ;
	private boolean	mAABBBOXDisplayFlag = false;
	
	
	public void onDirectorSurfaceCreated(){
	};
	public void onDirectorSurfaceDestroyed(){
	};
	public void onSoundPlay(KeyEvent event){
	};
	public interface TextInputListenner{

		void onTextChange(String destString);
		
	}
	public Director(Context context, EngineGLView wolfView) {

		mContext = context;
		mEngineGLView = wolfView;
		sDirector = this;

		
	}

	public Context getContext(){
		return mContext;
	}
	
	public static Director getInstance() {
		return sDirector;
	}

	public void setListenHandler(Handler handler) {
		mHandler = handler;
	}


	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		mGlHandler = new GLHandler();
		sResourcer = new CBResourcer(mContext, mEngineGLView);
		sResourcer.setGLEnviroment(mEngineGLView.getGLEnvirnment());

		sGLESVersion.initGLSystem(gl);
		tryRegisterSensor();
		onDirectorSurfaceCreated();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {

		
		
		Log.d("ming","onSurfaceChanged() = width = "+width+",,height = "+height);
		if(width <  height){//如果出现宽比高小的情况，说明转屏幕异常
//			Log.d("Log"," ===== Appear width less than height ==== width = "+width+",,height = "+height);
			int temp = width;
			width = height;
			height = temp;
//			Log.d("Log"," ===== after fix = "+width+",,height = "+height);
		}
		
		EngineConstanst.SCREEN_WIDTH = width;
		EngineConstanst.SCREEN_HEIGHT = height;

		fitWindow(width, height);

		if (!mInitFlag) {
			mInitFlag = true;
			initRenderer();
		}
		mEngineGLView.getGLEnvirnment().setScreenOrientation(width >= height ? true : false);

	}

	/**
	 * 注意！！！！
	 * 该方法在renderer接口中已被google取消，也就是说该方法不在属于renderer回调方法，
	 * 为了让该方法继续有效，我们手动使用GLView的 onDetachedFromWindow调用到这边来，暂时用起来并无问题。但不保证会有意想不到的问题。
	 */
	public void onSurfaceDestroyed() {
		
		tryUnRegisterSensor();
		onDirectorSurfaceDestroyed();
	}
	/**
	 * 为适配窗口而调整全局引用变量的值
	 * 
	 * @param width
	 * @param height
	 */
	private void fitWindow(int width, int height) {
		float baseBite = EngineConstanst.REFERENCE_SCREEN_WIDTH / EngineConstanst.REFERENCE_SCREEN_HEIGHT;

		if (width / height < baseBite) {

//			Log.d("Log"," ========== rebuild PIX_REFERENCE and ENGINE_SCREEN_REFERENCE ========== ");

			float sScreenHeight = width / EngineConstanst.REFERENCE_SCREEN_WIDTH * EngineConstanst.REFERENCE_SCREEN_HEIGHT;
			float bite = sScreenHeight / height;

			EngineConstanst.PIX_REFERENCE  = EngineConstanst.INITIAL_PIX_REFERENCE * bite;
			EngineConstanst.ENGINE_SCREEN_REFERENCE = EngineConstanst.INITIAL_ENGINE_SCREEN_REFERENCE /bite;
		}
		EngineConstanst.REFERENCE_CLICK_TINGLE_ERROR = EngineConstanst.ENGINE_SCREEN_REFERENCE/210;
		EngineConstanst.REFERENCE_LIST_MOVE_TINGLE = EngineConstanst.ENGINE_SCREEN_REFERENCE/200;
		EngineConstanst.REFERENCE_MOVE_ATTRACT = EngineConstanst.ENGINE_SCREEN_REFERENCE/200;
		
//		Log.d("Log"," APPConstanst.PIX_REFERENCE  = "+EngineConstanst.PIX_REFERENCE);
//		Log.d("Log"," APPConstanst.ENGINE_SCREEN_REFERENCE  = "+EngineConstanst.ENGINE_SCREEN_REFERENCE);
//		Log.d("Log"," EngineConstanst.REFERENCE_CLICK_TINGLE_ERROR  = "+EngineConstanst.REFERENCE_CLICK_TINGLE_ERROR);
//		Log.d("Log"," EngineConstanst.REFERENCE_MOVE_CAN_ADD  = "+EngineConstanst.REFERENCE_MOVE_ATTRACT);

	}

	// luoyouren
	@Override
	public void onDrawFrame(GL10 gl) {
		// FPS统计

//		sCamera.setup(60 , 60, 180, 180);
//		sCamera.updateEngineInfoEye();
//		mTracerScene.update();

		sGLESVersion.resetFrame(gl);


		drawFrame(gl);
		drawScissorFrame(gl);
		
		
		// 处理同步加载列表,每一帧只调取列表中的一项进行纹理加载，防止一次性加载太多引起的卡顿
		getGLHandler().dealMessage();
		dealSyncRequest();

		// luoyouren: 凝视点事件
		dealVrStarePointEvent();

		
	}

	private long mStarePoint_DelayTime = 300 ;
	private long mStarePoint_LastTime = 0;
	private void dealVrStarePointEvent() {
		if(mIsVR){
			if(System.currentTimeMillis() - mStarePoint_LastTime > mStarePoint_DelayTime ){
				mStarePoint_LastTime = System.currentTimeMillis();
				dispatchStarePointEvent();
				postDelayInvalidate(5);
			} 
		}
	}
	public void setNeedShowFrame(boolean flag){
	}

	public void setVREnable(boolean flag){
		this.mIsVR = flag;
	}
	public void setDoubleEyeEnable(boolean flag){
		this.mSceneManager.setDoubleEyeEnable(flag);
	}
	public void openAABBBoxDisplay() {
		this.mAABBBOXDisplayFlag = true;
	}
	public void closeAABBBoxDisplay() {
		this.mAABBBOXDisplayFlag = false;
	}
	/**
	 * 
	 * @param 用于显示引擎信息的裁剪区域
	 */
	private void drawScissorFrame(GL10 gl) {

		if(false){//该代码暂时已废弃，
//			mTracerScene.update();
			sGLESVersion.openScissorFrame(60 , 60, 180, 180);
//			sCamera.setup(60 , 60, 180, 180);
//			sCamera.updateEngineInfoEye();
//			mTracerScene.update();
			sGLESVersion.closeScissorTest();
			
		}

	}

	public void resetSensor() {

		if(!mIsVR){
			return;
		}
		mAbsSensorStrategy.reset();
	}
	private void tryRegisterSensor() {
		if(!mIsVR){
			return;
		}

		// luoyouren: 传感器数据
		mAbsSensorStrategy.register(mContext,new EngineSensorListenr() {
			@Override
			public void onSensorEvent(float[] tmpMatrix) {
				// luoyouren: 这里把Sensor数据传给场景
				mSceneManager.setEyeMatrix(tmpMatrix);

				mSceneManager.updateEyeMatrixToScene(tmpMatrix);
			}
		});		
	}
	public void testSensorEvent(float[] tmpMatrix) {
		mSceneManager.setEyeMatrix(tmpMatrix);
	}
	public GLHandler getGLHandler() {
		return mGlHandler;		
	}
	private void tryUnRegisterSensor() {
		if(!mIsVR){
			return;
		}
		mAbsSensorStrategy.unRegister();
	}
	public void setBlurScene(BlurScene blurScene){
		mSceneManager.setBlueScene(blurScene);;
	}
	public XScene getBlurScene(){
		return mSceneManager.getBlueScene();
	};
	// float mLastTime = 0;
	public void drawFrame(GL10 gl) {

		mSceneManager.update();
	}

	protected void initRenderer() {

		mSceneManager.init();

	};

	public void postInvalidate() {

//		 MLog.d("Director requestRender()");
		mEngineGLView.requestRender();
	}
	public void postDelayInvalidate(long delayTime) {
		
//		 MLog.d("Director requestRender()");
		mEngineGLView.requestRender(delayTime);
	}

	public boolean dispatchGenericMotionEvent(MotionEvent event) {
		boolean state = false;

		// 遍历dispatch
		state |= mSceneManager.dispatchGenericMotionEvent(event);
		

		// 没有可以处理事件的子元素
		// 由Renderer处理
		if (!state) {
			state |= this.onGenericMotionEvent(event);
		}
		return state;
	}

	private boolean onGenericMotionEvent(MotionEvent event) {
		return false;
	}

	/**
	 * @param event
	 * @return
	 */
	// luoyouren: 事件分发
	// 在EngineGLView.java中被调用
	public boolean dispatchTouchEvent(MotionEvent event) {
		boolean state = false;

		// 遍历dispatch
		state |= mSceneManager.dispatchTouchEvent(event);
		
		
		// 没有可以处理事件的子元素
		// 由Renderer处理
		if (!state) {
			state |= this.onTouchEvent(event);
		}

		return state;
	}

	
	/**
	 * @param event
	 * @return
	 */
	public boolean dispatchStarePointEvent( ) {
		

		return mSceneManager.dispatchStarePointEvent( );
	}

	/** 
	 * @param event
	 * @return
	 */
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

	/**
	 * @param event
	 * @return
	 */
	
	public boolean dispatchKeyEvent(final KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			onSoundPlay(event);
		}
		boolean state = false;
		state |= mSceneManager.dispatchKeyEvent(event);
		// 没有可以处理事件的子元素
		// 由Renderer处理
		if (!state) {
			state |= this.onKeyEvent(event/*,state*/);
		}
		return state;
	}

	/**
	 * @param event
	 * @return
	 */
	public boolean onKeyEvent(KeyEvent event) {

		boolean state = false;
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
			case KeyEvent.KEYCODE_MENU: {

//				if (KeyEvent.KEYCODE_BACK == keyCode) {
//					mWolfView.quit();
//					state = true;
//				}
			}
			break;
			}
		return state;
	}

	/**
	 * @hide 暂不使用，后延至Wolf 2.0
	 * @param vscroll
	 * @return
	 */
	public boolean dispatchWheelEvent(float vscroll) {

//		Log.d("wheel", " Wheel "+vscroll);
		
		boolean state = false;

		state |= mSceneManager.dispatchWhellEvent(vscroll);
		
		// 没有可以处理事件的子元素
		// 由Renderer处理
		if (!state) {
			state |= this.onWheelEvent(vscroll);
		}

		return state;
	}

	/**
	 * @hide 暂不使用，后延至Wolf 2.0
	 * @param vscroll
	 * @return
	 */
	public boolean onWheelEvent(float vscroll) {
		return false;
	}

	public void onResume() {
	}

	public void onPause() {
	}

	/**
	 * 添加Scene
	 * 
	 * @return
	 */
	public boolean addScene(XScene scene) {
		
		return mSceneManager.addScene(scene);
	}

	public boolean addDialogScene(DialogXScene dialogScene) {
		
		return mSceneManager.addDialogScene(dialogScene);
	}

	/**
	 * 移除Scene
	 * 
	 * @param scene
	 * @return
	 */
	public boolean removeScene(XScene scene) {
		return mSceneManager.removeScene(scene);
	}

	/**
	 * 移除当前正在活动的最前端的Scene
	 * 
	 * @param scene
	 * @return
	 */
	public boolean removeCurrentScene() {
		return mSceneManager.removeCurrentScene();
	}
	/**
	 * 移除Scene
	 * 
	 * @param scene
	 * @return
	 */
	public boolean removeDialogScene(DialogXScene dialogScene) {

		return mSceneManager.removeDialogScene(dialogScene);
	}

	/**
	 * 移除Scene
	 * 
	 * @param index
	 * @return
	 */
	public boolean removeScene(int index) {
		return mSceneManager.removeScene(index);
	}

	/**
	 * 获取Scene
	 * 
	 * @param index
	 * @return
	 */
	public XScene getScene(int index) {
		return mSceneManager.getScene(index);
	}
	/**
	 * 
	 * @return
	 */
	public Stack<XScene> getSceneList() {
		return mSceneManager.getSceneList();
	}
	/**
	 * 
	 * @return
	 */
	public Stack<DialogXScene> getDialogSceneList() {
		return mSceneManager.getDialogSceneList();
	}

	/**
	 * 获取Scene序号
	 * 
	 * @param scene
	 * @return
	 */
	public int indexOfScene(XScene scene) {
		return mSceneManager.indexOfScene(scene);
	}

	public XScene getCurrentScene() {
		return mSceneManager.getCurrentScene();
	}

	/**
	 * 移除所有Scene
	 */
	public void removeAllScene() {
		
		mSceneManager.removeAllScene();
	}

	/**
	 * 移除所有DialogScene
	 */
	public void removeAllDialogScene() {
		mSceneManager.removeAllDialogScene();
	}

	/**
	 * Scene数量
	 */
	public int sceneSize() {
		return mSceneManager.getSceneCount();
	}

	public void setGLES(GLES glesVersion) {
		this.sGLESVersion = glesVersion;
	}

	public void requestBlur(float tagetAlpha) {
		mSceneManager.requestBlur(tagetAlpha);
	}
	public void requestBlur(Bitmap bitmap,float tagetAlpha,long timeDelayed) {
		mSceneManager.requestBlur(bitmap,tagetAlpha,timeDelayed);
	}

	public void replaceToastScene(ToastScene scene){
		mSceneManager.replaceToastScene(scene);
	}

	public ToastScene getToastScene() {
		return mSceneManager.getToastScene();
	}


	/*
	 * timeDelayed  毫秒
	 */
	public void requestCancelBlur(long timeDelayed) {

		mSceneManager.cancelBlur(timeDelayed);
	}
	public void requestCancelBlur() {
		
		mSceneManager.cancelBlur(20);
	}
	public void requstAsyncTexture(final View view, final Bitmap bitmap) {
		boolean sucuss = mEngineGLView.request(new AsynTask() { // 尝试提交异步贴图任务，若失败则继续往下
					@Override
					public void run(Object envirnment) {
						Texture texture = sGLESVersion.generateTexture(bitmap);
						view.setBackground(texture);
						view.postInvalidate();
					}

					@Override
					public void onCanceled(Object envirnment) {

					}
				});
		if(sucuss){
			return;
		} 
		// 否则作为同步任务平分到每一帧
		mPostList.add(new TextureRunnable() {

			@Override
			public void run() {

				Texture texture = sGLESVersion.generateTexture(bitmap);
				view.setBackground(texture);
				view.postInvalidate();
			}
		});
	}
	/*
	 * holdTexture仅用作持有纹理作用
	 */
	public void requstAsyncTexture(final onAsyncTextureLoadListenner l, final Bitmap bitmap) {
		boolean sucuss = mEngineGLView.request(new AsynTask() { // 尝试提交异步贴图任务，若失败则继续往下
					@Override
					public void run(Object envirnment) {
						Texture texture = sGLESVersion.generateTexture(bitmap);
						l.onTextureLoaded(texture);;
					}

					@Override
					public void onCanceled(Object envirnment) {

					}
				});
		if(sucuss){
			return;
		} 
		// 否则作为同步任务平分到每一帧
		mPostList.add(new TextureRunnable() {

			@Override
			public void run() {

				Texture texture = sGLESVersion.generateTexture(bitmap);
				l.onTextureLoaded(texture);;
			}
		});
	}
	/**
	 * 普通的任务提交
	 * @param runnable
	 */
	public void requstAsyncGeneralTask(PostRunnable runnable) {

//		Log.d("requst", " requstAsyncGeneralTask = ");
		mPostList.add(runnable);
		postInvalidate();
	}

	public void requestAsyncFocusTask(final View view) {

		//清除之(这是因为requestFocus只应该对最后调用的那个view产生效果);
		mFocusPostList.clear();
		mFocusPostList.add(new FocusRunnable() {
			
			@Override
			public void run() {
				view.requestFocus();
			}
		});
	}

	private void dealSyncRequest() {
		
		if(mPostList.size() > 0 ){
			final Runnable post = mPostList.remove(0);
			post.run();
		}

		if(mFocusPostList.size() > 0){
			final Runnable post2 = mFocusPostList.remove(0);
			post2.run();
//			mWolfView.queueEvent(post2);
		}

		if(mFocusPostList.size() == 0 && mPostList.size() == 0){
			return;
		}
		 postDelayInvalidate(50);
		
	}

	class Post {

		private View	mView;
		private Bitmap	mBitmap;

		public Post(View view, Bitmap bitmap) {
			this.mView = view;
			this.mBitmap = bitmap;
		}
	}
	public boolean isMainSceneTop() {
		return mSceneManager.isMainSceneTop();
	}
	public boolean isSceneTop(XScene scene) {
		return mSceneManager.isSceneTop(scene);
	}
	public boolean isDialogSceneTop(DialogXScene scene) {
		return mSceneManager.isDialogSceneTop(scene);
	}
 
	public void requstAABBBoxDisplay(Object obj) {

		if(mAABBBOXDisplayFlag){
			mEngineGLView.buildShowAABBBoxMessage(obj);
		};
	} 

	public boolean containDialog(DialogXScene scene) {
		return mSceneManager.getDialogSceneList().contains(scene);
	}
	public void requestTextToTracerScene(float[] startFromSensorTransformation) {
		mSceneManager.requestTextToEngineInfoScene(startFromSensorTransformation);
	}
	public void playParticleEffect(int effect ,Texture tex) {
		mSceneManager.playParticleEffect(effect,tex);
	}
	public void enableLight(int i) {
		mSceneManager.enableLight(i);
	}
	public boolean getLightEnable() {
		return mSceneManager.isLightEnable();
	}
	public float[] getLightPosition() {
		return mSceneManager.getLightPosition();
	}
	public float[] getAmbient() {
		return mSceneManager.getAmbient();
	}
	public float[] getDiffuse() {
		return mSceneManager.getDiffuse();
	}
	public float[] getSpecular() {
		return mSceneManager.getSpecular();
	}
	public float[] getEyePosition() {
		return mSceneManager.getEyePosition();
	}
	public float[] getLightMMatrix() {
		return mSceneManager.getLightMMatrix();
	}
	public float getLightStyle() {
		return mSceneManager.getLightStyle();
	}



}
