package com.hello.scene_for_vr;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hello.R;
import com.hello.scene_for_vr.collisionbox.CollisionBoxView;
import com.x.EngineGLView;
import com.x.opengl.kernel.sensor.TestSensorCardboardBlock;

public class HomeActivity extends Activity {

	private VRGLView mVrView;
	private View coverView;
	TestSensorCardboardBlock mTestSensorCardboardBlock ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		initView();
		time = System.currentTimeMillis();
	}	
	public static long time = System.currentTimeMillis();
	
	private void initView() {
		ViewGroup viewGroup = (RelativeLayout) findViewById(R.id.MainRelative);
		mVrView = new VRGLView(this);
//		myWolfView.setBackgroundColor(Color.CYAN);
		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		viewGroup.addView(mVrView, layoutParams);

		CollisionBoxView cv = (CollisionBoxView) findViewById(R.id.CollisionView);
		((EngineGLView)mVrView).setCollisionBoxView(cv);
		// mDataTool = new DataTool(HomeActivity.this, myTarotView);
		// mStatusTool.setmDataTool(mDataTool);
		
//		new Handler().postDelayed(new Runnable() {
//			
//			@Override
//			public void run() {
//				mTestSensorCardboardBlock = new TestSensorCardboardBlock();
//				mTestSensorCardboardBlock.register(getApplicationContext(), new EngineSensorListenr() {
//					
//					@Override
//					public void onSensorEvent(float[] tmpMatrix) {
//						Director.getInstance().testSensorEvent(tmpMatrix);
//					}
//				});
//			}
//		}, 3000);
		
				
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub

		if (mVrView != null) {
			if(mVrView.dispatchGlKeyEvent(event)){
				return true;
			}
		}
		
		
		if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		
		return super.dispatchKeyEvent(event);
	}

	private void init() {
	}

	public void removeCover() {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				coverView.setVisibility(View.GONE);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		mTestSensorCardboardBlock.unRegister();
		((MyDirector)MyDirector.getInstance()).onDestory();
	}
	@Override
	protected void onPause() {
		super.onPause();
		((MyDirector)MyDirector.getInstance()).onPause();
	}

}
