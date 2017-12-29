package com.hello.scene_for_vr;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

import com.hello.R;
import com.hello.scene_for_vr.mymediaplayerpackage.MediaPlayerManager;
import com.x.Director;
import com.x.EngineGLView;
import com.x.opengl.kernel.EngineConstanst;

public class MyDirector extends Director
{
 
	private VRGLView	mMyTarotView	= null;
	private TextInputListenner	mTextInputListenner;
	public static boolean isOPENGLOK;
	public static boolean isTextureOk;
	//private  EGreatDialogScene mEGreatDialogScene = null;

	//private BlurControl mBlurControl ;

	MediaPlayerManager mMediaPlayerManager;


	public MyDirector(Context context, EngineGLView tarotView)
	{
		super(context, tarotView);
		this.mMyTarotView = (VRGLView) tarotView;
		mMediaPlayerManager = new MediaPlayerManager(context, R.raw.video_pacific_ar_building);
		setVREnable(true);
		setDoubleEyeEnable(false);
	}

	public void requstTextInput(TextInputListenner textInputListenner) {

		Log.d("box", "===requstTextInput===");
		this.mTextInputListenner = textInputListenner;
		mMyTarotView.requstEditBoxFocus();
	}

	public boolean isEditBoxFocused() {
		return mMyTarotView.isInputTextFocus();
	}

	public void callBackEditText(String destString) {
		if(mTextInputListenner != null){
			mTextInputListenner.onTextChange(destString);
		}
	}

	@Override
	public void onDirectorSurfaceCreated() {
		super.onDirectorSurfaceCreated();
		isOPENGLOK = true;
	}
	private long mStartTime = 0 ;
	@Override
	public void onSoundPlay(KeyEvent event) {
		super.onSoundPlay(event);
		long endTime = System.currentTimeMillis();
		if (endTime - mStartTime > 150) {
			mStartTime = endTime;
			//SoundTool.soundKey(event.getKeyCode());
		}
	}
	public void requestToast(int resId) {

		Log.d("requst", " requestToast = resId");
		mMyTarotView.requestToast(resId);
	}
	public void requestToast(String string) {
		Log.d("requst", " requestToast = string");
		mMyTarotView.requestToast(string);
	}

	@Override
	protected void initRenderer()
	{
		super.initRenderer();
		Com();

	}
	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		super.onDrawFrame(gl);
		mMediaPlayerManager.updateTexImage(null);
		postInvalidate();
	}


	MainDialogScene mAllAppDialogScene;


	public void Com(){
//		Log.d("ming","Com()  "+( System.currentTimeMillis()- HomeActivity. time)+"   time");
//
//		AppManager	mAppManager = ((MyDirector)(Director.getInstance())).getWolfView().getAppManager();
		if (mAllAppDialogScene == null) { 
			mAllAppDialogScene = new MainDialogScene(this);
	//		setBackgroundResource(R.drawable.allapp_dialog_background);
			mAllAppDialogScene.setRenderable(true);
			mAllAppDialogScene.setWidth(EngineConstanst.REFERENCE_SCREEN_WIDTH);
			mAllAppDialogScene.setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT/2);
//			mAllAppDialogScene.setEyeMatrixUpdate(true);
			isTextureOk = true;
		}
		
		mAllAppDialogScene.initScene(); 
		Director.getInstance().addDialogScene(mAllAppDialogScene);

//		mMediaPlayerManager.initVideo(mAllAppDialogScene.getOesTextureId(mMediaPlayerManager));
//		mMediaPlayerManager.onStart();
	}


	public void changeTextureId(int veodioTextureID) {
		// TODO Auto-generated method stub
		mMediaPlayerManager.initVideo(veodioTextureID);
		
	}

	public void onDestory() {
		mMediaPlayerManager.onDestroy();
	}
	public void onPause() {
		mMediaPlayerManager.onPause();
	}

	public void updateScreensaver() {
		
	}
	public VRGLView getWolfView(){
		return mMyTarotView;
	}
}
