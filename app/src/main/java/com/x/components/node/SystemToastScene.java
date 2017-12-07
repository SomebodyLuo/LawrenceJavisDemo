package com.x.components.node;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;

import com.x.opengl.kernel.EngineConstanst;
import com.x.components.listener.OnAnimationListener;
import com.x.components.widget.TextView;

public  class SystemToastScene extends ToastScene{

	private ViewGroup mToastViewGroup = new ViewGroup();
	public SystemToastScene(){
		mToastViewGroup.setWidth(EngineConstanst.REFERENCE_SCREEN_WIDTH);
		mToastViewGroup.setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
		addLayer(mToastViewGroup);

		SetDebugName("SystemToastScene");
	}
	@Override
	public void initScene() {
		
	};
	@Override
	public void requestToastId(int stringId) {
		View view = initToastView(stringId);
		mToastViewGroup.removeAll();
		mToastViewGroup.addChild(view);
		doSendDelayAnimation(view);
	}
	@Override
	public void requestToastString(String toastString) {
		View view = initToastView(toastString);
		mToastViewGroup.removeAll();
		mToastViewGroup.addChild(view);
		doSendDelayAnimation(view);
	}
	protected TextView initToastView(String toastString) {
		TextView toasTextView = new TextView();
		toasTextView.setWidth(698 * 1.5f);
		toasTextView.setHeight(93 * 1.5f);
		toasTextView.setTextColor(Color.BLACK);
		toasTextView.setBackgroundColor(Color.WHITE);
		toasTextView.setText(toastString);
		toasTextView.setTextGravity(Gravity.CENTER);
		toasTextView.setTextSize(36);
		return toasTextView;
	}
	protected TextView initToastView(int stringId) {
		TextView toasTextView = new TextView();
		toasTextView.setWidth(698 * 1.5f);
		toasTextView.setHeight(93 * 1.5f);
		toasTextView.setTextColor(Color.BLACK);
		toasTextView.setBackgroundColor(Color.WHITE);
		toasTextView.setTextResource(stringId);
		toasTextView.setTextGravity(Gravity.CENTER);
		toasTextView.setTextSize(36);		
		return toasTextView;
	}
	
	public  void doSendDelayAnimation(View   view ) {
		// TODO Auto-generated method stub
		SystemToastScene.this.setRenderable(true);
		
		mHandler.removeMessages(0);
		view.setTranslate(0, 355, 0);
		view.setVisibility(true);
		view.setAlpha(1);
		view.setOnAnimationListenner(null);
		
		Message message = mHandler.obtainMessage();
		message.arg1 = 0;
		message.obj  = view;
		
		mHandler.sendMessageDelayed(message, 1000);
		
	}
	@SuppressLint("HandlerLeak") 
	private    Handler mHandler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(Message msg) {
			
			View  view = (View) msg.obj;
			view.setOnAnimationListenner(new OnAnimationListener() {
						@Override
						public void onAnimationStart(View view) {

						}

						@Override
						public void onAnimationRepeat(View view) {
						}

						@Override
						public void onAnimationEnd(View view) {
							  view.setVisibility(false);
							  SystemToastScene.this.setRenderable(false);
						}
					});
			view.alphaTo(0, 1500);
		};
	};
}
