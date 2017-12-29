package com.x.components.node;

import java.util.Stack;

import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.x.Director;
import com.x.opengl.kernel.Camera;
import com.x.opengl.kernel.DoubleMainCamera;
import com.x.opengl.kernel.EngineConstanst;
import com.x.opengl.kernel.EngineInfoCamera;
import com.x.opengl.kernel.SingleMainCamera;
import com.x.opengl.kernel.Texture;
import com.x.opengl.kernel.particles.ParticleSystemScene;

public class SceneManager {
	private Stack<XScene>				mSceneStack			= new Stack<XScene>();//场景堆栈秩序
//	private LinkedList<WolfScene>			mShowSceneList		= new LinkedList<WolfScene>();//用于显示的场景列表
	private Stack<DialogXScene>	     	mDialogSceneStack	= new Stack<DialogXScene>();

	private BlurScene						mBlurScene			= null;
	private ToastScene						mToastScene 		= null;
	private EngineInfoScene					mEngineInfoScene		= null;
	private VrStarePointScene				mVrStarePointScene		= null;

	private ParticleSystemScene				mParticleSystemScene 	= null;
	private LightingScene					mLightingScene  = null;

	private SingleMainCamera 				mSingleMainCamera = null;
	private DoubleMainCamera 				mDoubleMainCamera = null;
	private EngineInfoCamera 				mEngineInfoCamera = null;
	
	private boolean 						mIsDoubleEye =false;//默认是单眼
	private boolean mEnableEngineInfo = true;
	private boolean mEnableStarePoint = true;
	private boolean mEnableParticleEffect = true;
	private MotionEvent mStarePointMotionEvent;
	public void init() {
		if(mStarePointMotionEvent == null){

			if(mIsDoubleEye){
				//当是双眼是默认是用了右眼的事件触发
				mStarePointMotionEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, EngineConstanst.SCREEN_WIDTH / 2 + EngineConstanst. SCREEN_WIDTH / 2 /2, EngineConstanst.SCREEN_HEIGHT / 2, 0);
			}else{
				mStarePointMotionEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, EngineConstanst.SCREEN_WIDTH / 2, EngineConstanst. SCREEN_HEIGHT / 2, 0);
				//
			}
		}
		
		mSingleMainCamera = new SingleMainCamera();
		mDoubleMainCamera = new DoubleMainCamera() ;
		mEngineInfoCamera = new EngineInfoCamera();
		
		// 初始化用于显示焦点位置的场景
		mVrStarePointScene = new VrStarePointScene();
		mVrStarePointScene.initScene();
		mVrStarePointScene.setRenderable(true);

		// 初始化用于高斯模糊的场景区域
		mBlurScene = new BlurScene();
		mBlurScene.initScene();
		mBlurScene.setRenderable(false);
		
		//Toast层级，用于弹出信息框
		mToastScene = new SystemToastScene();
		mToastScene.initScene();
		mToastScene.setRenderable(false);
		
		// 初始化用于显示引擎信息的场景
		// luoyouren: 引擎信息
		mEngineInfoScene = new EngineInfoScene();
		mEngineInfoScene.initScene();
		mEngineInfoScene.setRenderable(true);

		// 粒子系统管理场景
		mParticleSystemScene = new ParticleSystemScene();
		mParticleSystemScene.initScene();
		mParticleSystemScene.setRenderable(false);

		// 灯光系统管理场景
		// luoyouren: 光照物体
		mLightingScene = new LightingScene();
		mLightingScene.initScene();
		mLightingScene.setRenderable(false);

	}
	
	
	
	public void update() {

		if(mIsDoubleEye){
			
			mDoubleMainCamera.resetEye();
			sceneUpdate(mDoubleMainCamera);//刷新左眼
			sceneUpdate(mDoubleMainCamera);//刷新右眼
			
		}else{
			sceneUpdate(mSingleMainCamera);
			
		}
		// MLog.d("Director update");
		engineInfoUpdate(mEngineInfoCamera);

		
	}
	private void sceneUpdate(Camera camera){
		
		camera.initViewPort();
		camera.updateWorldViewMatrix();
//		camera.updateUiViewMatrix();
		// 层级：
		{//世界场景
			//1.普通场景列表（每个场景持有自身焦点focusView）
			for (int i = 0; i < mSceneStack.size(); i++) {
				XScene scene = mSceneStack.get(i);
				if (scene.isRenderable()) {
					scene.update();
				}
			}
	//
//			//2.毛玻璃效果
//			if (mBlurScene.isRenderable()) {
//				mBlurScene.update();
//			}

			//3.对话框场景列表（每个场景持有自身焦点focusView）
			for (int i = 0; i < mDialogSceneStack.size(); i++) {

				XScene scene = mDialogSceneStack.get(i);
				if (scene.isRenderable()) {
					scene.update();
				}
			}

			if (mLightingScene.isRenderable()) {
				mLightingScene.update();
			}
			
		}
	
		
//		camera.updateUiGravityViewMatrix();
		// .粒子系统场景
		if (mEnableParticleEffect && mParticleSystemScene.isRenderable()) {
			mParticleSystemScene.update();
		}

		camera.updateUiViewMatrix();
		// .VR凝视点层
		if(mEnableStarePoint){
			mVrStarePointScene.setRenderable(true);
			mVrStarePointScene.update();
		}
		// .toast场景
		if(mToastScene.isRenderable()){
			mToastScene.update();
		}
	}


	public void engineInfoUpdate(Camera camera) {
		if(mEnableEngineInfo){
			camera.initViewPort();
			camera.updateUiViewMatrix();
			if(mEngineInfoScene.isRenderable()){
				mEngineInfoScene.update();
			}
		}
	}

	public boolean dispatchGenericMotionEvent(MotionEvent event) {
		
		boolean state = false;
		
		if(mDialogSceneStack.size() > 0){
			XScene scene = mDialogSceneStack.peek();
			if (scene.isRenderable()) {
				state |= scene.dispatchGenericMotionEvent(event);
			}
		}
		
		if(!state){
			
			//只派遣栈顶的场景

			if(mSceneStack.size() > 0){

				XScene scene = mSceneStack.peek();
				if (scene.isRenderable()) {
					state |= scene.dispatchGenericMotionEvent(event);
				}
			}
//			Iterator<WolfScene> itr = mSceneStack.iterator();
//			while (itr.hasNext()) {
//				WolfScene child = itr.next();
//				if (child.isRenderable()) {
//					state |= child.dispatchTouchEvent(event);
//					if (state) {
//						// 如果处理，有child自行将事件交给onTouchEvent
//						break;
//					}
//				}
//			}
		}
		
		return false;
	}
	
	public boolean dispatchTouchEvent(MotionEvent event) {

		boolean state = false;
		
		if(mDialogSceneStack.size() > 0){
			XScene scene = mDialogSceneStack.peek();
			if (scene.isRenderable()) {
				state |= scene.dispatchTouchEvent(event);
			}
		}
		
		if(!state){
			

			//只派遣栈顶的场景
			if(mSceneStack.size() > 0){

				XScene scene = mSceneStack.peek();
				if (scene.isRenderable()) {
					state |= scene.dispatchTouchEvent(event);
				}
			}
//			Iterator<WolfScene> itr = mSceneStack.iterator();
//			while (itr.hasNext()) {
//				WolfScene child = itr.next();
//				if (child.isRenderable()) {
//					state |= child.dispatchTouchEvent(event);
//					if (state) {
//						// 如果处理，有child自行将事件交给onTouchEvent
//						break;
//					}
//				}
//			}
		}
		
		
		return state;
	}


	// luoyouren: 凝视点事件分发
	public boolean dispatchStarePointEvent( ) {

		if(mStarePointMotionEvent == null){
			return false;
		}
		
		boolean state = false;

		MotionEvent event = mStarePointMotionEvent;
		
		
		if(mDialogSceneStack.size() > 0){
			XScene scene = mDialogSceneStack.peek();
			if (scene.isRenderable()) {
				state |= scene.dispatchStarePointEvent(event);
			}
		}
		
		if(!state){
			//只派遣栈顶的场景
			if(mSceneStack.size() > 0){

				XScene scene = mSceneStack.peek();
				if (scene.isRenderable()) {
					state |= scene.dispatchStarePointEvent(event);
				}
			}
		}

		// 没有可以处理事件的子元素
		// 由Renderer处理
		if (!state) {
			state |= this.onStarePoint(event);
		}

		return state;
	}

	public boolean onStarePoint(MotionEvent event){
		return true;
	}

	public boolean dispatchKeyEvent(KeyEvent event) {
		
		
		boolean state = false;
	   // 遍历对话框场景列表dispatch
		{
			
			//只派遣栈顶的场景
			if(mDialogSceneStack.size() > 0){
				XScene scene = mDialogSceneStack.peek();
				if (scene.isRenderable()) {
					state |= scene.dispatchKeyEvent(event);
				}
			}
			
//			Iterator<DialogWolfScene> itr = mDialogSceneStack.iterator();
//			while (itr.hasNext()) {
//				DialogWolfScene scene = itr.next();
//				if (scene.isRenderable()) {
//					state |= scene.dispatchKeyEvent(event);
//					if (state) {
//						// 如果处理，有child自行将事件交给onKeyEvent
//						break;
//					}
//				}
//			}
		}
	
		// 遍历普通场景dispatch
		if (!state) {
			
			//只派遣栈顶的场景
			if(mSceneStack.size() > 0){
				XScene scene = mSceneStack.peek();
				if (scene.isRenderable()) {
					state |= scene.dispatchKeyEvent(event);
				}
			}
		//		Iterator<WolfScene> itr = mSceneStack.iterator();
		//		while (itr.hasNext()) {
		//			WolfScene scene = itr.next();
		//			if (scene.isRenderable()) {
		//				state |= scene.dispatchKeyEvent(event);
		//				if (state) {
		//					// 如果处理，有child自行将事件交给onKeyEvent
		//					break;
		//				}
		//			}
		//		}
			}
		return state;
	}



	public boolean dispatchWhellEvent(float vscroll) {

		boolean state = false;


		if(mDialogSceneStack.size() > 0){
			XScene scene = mDialogSceneStack.peek();
			if (scene.isRenderable()) {
				state |= scene.dispatchWheelEvent(vscroll);
			}
		}
		//只派遣栈顶的场景
		if(!state){

			if(mSceneStack.size() > 0){
				XScene scene = mSceneStack.peek();
				if (scene.isRenderable()) {
					state |= scene.dispatchWheelEvent(vscroll);
				}
			}
		}
		
		// 遍历dispatch
//		Iterator<WolfScene> itr = mSceneStack.iterator();
//		while (itr.hasNext()) {
//			WolfScene child = itr.next();
//			state |= child.dispatchWheelEvent(vscroll);
//			if (state) {
//				// 如果处理，有child自行将事件交给onKeyEvent
//				break;
//			}
//		}
		return state;
	}



	public boolean addScene(XScene scene) {
		if (scene != null) {

			if (scene.getRoot() != null) {
				throw new RuntimeException("You can not add scene that has been added to  other root");
			}
			scene.setRoot(this);
			scene.setVisibility(true);
			return mSceneStack.add(scene);
		} else {
			return false;
		}
	}

	public boolean addDialogScene(DialogXScene wolfDialogScene) {
		if (wolfDialogScene != null) {

			if (wolfDialogScene.getRoot() != null) {
				throw new RuntimeException("You can not add dialogScene that has been added to  other root");
			}
			wolfDialogScene.setRoot(this);
			wolfDialogScene.setVisibility(true);
			Director.getInstance().postInvalidate();
			return mDialogSceneStack.add(wolfDialogScene);
		} else {
			if(wolfDialogScene == null ){
				throw new RuntimeException("the scene you add is NULL");
			}
			return false;
		}
	}



	public boolean removeScene(XScene scene) {
		if (scene != null) {
			scene.setRoot(null);
			scene.setVisibility(false);
			return mSceneStack.remove(scene);
		} else {
			return false;
		}
	}

	public boolean removeCurrentScene() {
		XScene scene = this.mSceneStack.peek();
		if (scene != null) {
			scene.setRoot(null);
			scene.setVisibility(false);
			return mSceneStack.remove(scene);
		} else {
			return false;
		}
		
	}

	public boolean removeDialogScene(DialogXScene dialogScene) {
		
		if (dialogScene != null) {
			dialogScene.setRoot(null);
			dialogScene.setVisibility(false);
			boolean flag = mDialogSceneStack.remove(dialogScene);
			Director.getInstance().postInvalidate();
			return flag;
		} else {
			return false;
		}
	}

	public boolean removeScene(int index) {
		if (index >= 0 || index < mSceneStack.size()) {
			mSceneStack.get(index).setRoot(null);
			mSceneStack.get(index).setVisibility(false);
			return mSceneStack.remove(index) != null;
		} else {
			return false;
		}
	}



	public XScene getScene(int index) {
		if (index >= 0 || index < mSceneStack.size()) {
			return mSceneStack.get(index);
		} else {
			return null;
		}
	}
	
	public Stack<XScene>getSceneList() {
		return mSceneStack;
	}
	public Stack<DialogXScene> getDialogSceneList() {
		return mDialogSceneStack;
	}


	public int indexOfScene(XScene scene) {
		if (scene != null) {
			return mSceneStack.indexOf(scene);
		} else {
			return -1;
		}
	}



	public XScene getCurrentScene() {
		return mSceneStack.peek();
	}



	public void removeAllScene() {
		for (int i = 0; i < mSceneStack.size(); i++) {
			mSceneStack.get(i).setRoot(null);
			mSceneStack.get(i).setVisibility(false);
		}
		mSceneStack.clear();		
	}



	public void removeAllDialogScene() {

		for (int i = 0; i < mDialogSceneStack.size(); i++) {
			mDialogSceneStack.get(i).setRoot(null);
			mDialogSceneStack.get(i).setVisibility(false);
		}
		mDialogSceneStack.clear();
	}



	public int getSceneCount() {
		return mSceneStack.size();
	}


	public void requestBlur(float tagetAlpha) {
		
		mBlurScene.setRenderable(true);
		long time = System.currentTimeMillis();
		Bitmap bitmap = Director.getInstance().sGLESVersion.getSnapshot((int) EngineConstanst.SCREEN_WIDTH, (int) EngineConstanst.SCREEN_HEIGHT);

		time = System.currentTimeMillis();
		mBlurScene.requestBlur(bitmap,tagetAlpha,30,true);
		
//		WolfScene scene = mSceneStack.peek();
//		scene.alphaTo(0.6f);
	}

	public void requestBlur(Bitmap bitmap, float tagetAlpha, long timeDelayed) {
		mBlurScene.setRenderable(true);
		mBlurScene.requestBlur(bitmap,tagetAlpha,timeDelayed,false);
	}
	public void cancelBlur(long timeDelayed) {

//		WolfScene scene = mSceneStack.peek();
//		scene.alphaTo(1);
		
		mBlurScene.cancelBlur(timeDelayed);
	}



	public boolean isMainSceneTop() {
		return mDialogSceneStack.size() == 0;
	}
	public boolean isSceneTop(XScene scene) {
		return mDialogSceneStack.size() == 0 && mSceneStack.indexOf(scene) == mSceneStack.size() - 1;
	}

	public boolean isDialogSceneTop(DialogXScene scene) {
		return mDialogSceneStack.indexOf(scene) == mDialogSceneStack.size() - 1;
	}


	public XScene getBlueScene() {
		return mBlurScene;
	}
	public void setBlueScene(BlurScene blurScene) {
		this.mBlurScene = blurScene;
	}

	public void replaceToastScene(ToastScene scene){
		mToastScene = scene;
	}
	public ToastScene getToastScene() {
		return mToastScene;
	}



	public void requestTextToEngineInfoScene(float[] startFromSensorTransformation) {
		mEngineInfoScene.requestText( startFromSensorTransformation);
	}


	public void setEyeMatrix(float[] tmpMatrix) {

		if(mIsDoubleEye){
			mDoubleMainCamera.setEyeMatrix(tmpMatrix);				
		}else{
			mSingleMainCamera.setEyeMatrix(tmpMatrix);	
		}		
	}

	// luoyouren: 让某些场景跟随视线移动
	public void updateEyeMatrixToScene(float[] tmpMatrix)
	{
		for (int i = 0; i < mDialogSceneStack.size(); i++)
		{
			XScene scene = mDialogSceneStack.get(i);
			scene.updateEyeMatrixToScene(tmpMatrix);
		}
	}

	public void playParticleEffect(int effect,Texture texture) {
		mEnableParticleEffect = true;
		mParticleSystemScene.playParticleEffect(effect,texture);
	}

	public void enableLight(int i) {
		mLightingScene.enableLight(i);
	}

	public boolean isLightEnable() {
		// TODO Auto-generated method stub
		return mLightingScene.isLightEnable();
	}

	public float[] getLightPosition() {
		return mLightingScene.getLightPosition();
	}

	public float[] getAmbient() {
		return mLightingScene.getAmbient();
	}

	public float[] getDiffuse() {
		return mLightingScene.getDiffuse(); 
	}

	public float[] getSpecular() {
		return  mLightingScene.getSpecular(); 
	}

	public float[] getEyePosition() {

		if(mIsDoubleEye){
			
			return mDoubleMainCamera.getEyePosition();
			
		}else{
			return mSingleMainCamera.getEyePosition();
			
		}
	}

	public float[] getLightMMatrix() {
		return mLightingScene.getLightMMatrix();
	}

	public float getLightStyle() {
		return mLightingScene.getLightStyle();
	}



	public void setDoubleEyeEnable(boolean flag) {
		this.mIsDoubleEye = flag;
	}


	
}
