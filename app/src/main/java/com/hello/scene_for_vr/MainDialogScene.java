package com.hello.scene_for_vr;

import android.graphics.PointF;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.hello.scene_for_vr.mymediaplayerpackage.MediaPlayerManager;
import com.x.Director;
import com.x.components.node.DialogXScene;

public class MainDialogScene extends DialogXScene{

	//private EGreatDialogScene mEGreatDialogScene;
	private boolean	mFlagLockAnimation;
	private UI_Layer mUILayer;
	private SmallSphere_Layer mSmallSphere;
	private SkySphere mSkySphere;
	private SkyBox mSkyBox;
	private MyDirector mMyDirector;
	private Metro_Layer mMetro;
	
	public MainDialogScene(MyDirector myDirector) {
		this.mMyDirector = myDirector;
		SetDebugName("MainDialogScene");

		//mEGreatDialogScene = mainScene;
//		mUILayer = new UI_Layer(this);
//		addChild(mUILayer.getLayer());

//		mSmallSphere = new SmallSphere_Layer();
//		addChild(mSmallSphere.getLayer());

//		mSkySphere = new SkySphere();
//		addChild(mSkySphere.getLayer());

//		mSkyBox = new SkyBox();
//		addChild(mSkyBox.getLayer());

		mMetro = new Metro_Layer(this);
		addChild(mMetro.getLayer());


//		addChild(new MyWorldLayer().getLayer());

		Director.getInstance().enableLight(0);
		//setShowFocusView(true);
//		setStencilTestFlag(true);
		setFocusViewScale(1.0f, 1.0f);
	}
	public static void add(){
		int a = 6;
		int b = 6;
		int c = 6;
		int sum = a+b+c;
		System.out.println(" sum = "+sum);
		
	}
	
	@Override
	public void initScene() {
		mFlagLockAnimation = false;
//		setShowFocusView(true);
	}
	   
	@Override
	public boolean onKeyEvent(KeyEvent event) {

		boolean downFlag = false;
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			downFlag = true;
		}
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_ESCAPE: {
			// Director.getInstance().invalidate();
			}
		}
		return super.onKeyEvent(event);
	}

	PointF last = new PointF();
	PointF newM = new PointF();
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		if(event.getAction() == MotionEvent.ACTION_DOWN){
			
			last.x = event.getX();
			last.y = event.getY();
			
		}else if(event.getAction() == MotionEvent.ACTION_MOVE){

			float x = event.getX() - last.x;
			float y = event.getY() - last.y;
//			Director.getInstance().sCamera.addSingelEyeRotate(x/10f,y/10f,0);
			
			last.x = event.getX();
			last.y = event.getY();
			
		}else {
			
		}
		
		return super.dispatchTouchEvent(event);
	}

	public boolean hasLockAnimation() {
		return mFlagLockAnimation;
	}

	
	public int getOesTextureId(MediaPlayerManager mMediaPlayerManager) {

		return mMetro.getVideoTextureID() ;
	}
	public void changeShowPosition(int i) {
//		if(i == 0){
//			mSmallSphere.restoreTexture();
//			mSkySphere.restoreTexture();
//			mMyDirector.changeTextureId(mUILayer.getVideoTextureID());
//		}else if(i == 1){
//			mMyDirector.changeTextureId(mSmallSphere.getVideoTextureID());
//			mSkySphere.restoreTexture();
//			mUILayer.restoreTexture();
//		}else {
//			mSmallSphere.restoreTexture();
//			mMyDirector.changeTextureId(mSkySphere.getVideoTextureID());
//			mUILayer.restoreTexture();
//		}
	}
 
}
