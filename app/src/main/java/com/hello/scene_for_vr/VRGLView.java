package com.hello.scene_for_vr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.x.Director;
import com.x.EngineGLView;
import com.x.components.task.PostRunnable;

@SuppressLint("NewApi") 
public class VRGLView extends EngineGLView {

	private MyDirector		mDirector;
	private EditText		mEditBox;
	private Context			mContext;
	public final static int		HANDLE_REQUEST_EDITBOX			= 0x0004;
	public final static int		HANDLE_REQUEST_RESID_TOAST		= 0x0005;
	public final static int		HANDLE_REQUEST_RESSTRING_TOAST	= 0x0006;

	private Handler		mHandler	= new Handler() {
		
		public void handleMessage(Message msg) {
			
			switch (msg.what)
				{
				case HANDLE_REQUEST_EDITBOX:{
					mEditBox.setVisibility(View.VISIBLE);;
					mEditBox.requestFocus();
//					InputMethodManager imm = (InputMethodManager) mContext
//							  .getSystemService(Context.INPUT_METHOD_SERVICE);
//							imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
							

//							//打开软键盘
//							InputMethodManager imm = (InputMethodManager) ctx
//							  .getSystemService(Context.INPUT_METHOD_SERVICE);
//							imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		//
//							//关闭软键盘
//							imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
				}
				break;
				case HANDLE_REQUEST_RESID_TOAST:
					int rid = (Integer) msg.obj;
//					Toast.makeText(mContext, rid, Toast.LENGTH_SHORT).show();;
				//	Util.Toast_MSG(mContext, mContext.getResources().getString(rid), 0, 0, true);
				break;
				case HANDLE_REQUEST_RESSTRING_TOAST:
					String string = (String) msg.obj;
//					Toast.makeText(mContext, rid, Toast.LENGTH_SHORT).show();;
				//	Util.Toast_MSG(mContext,string, 0, 0, true);
				break;
				default:
				break;
				}
		};
	};
	public void requestToast(int resId) {
		Message message = mHandler.obtainMessage();
		message.what = HANDLE_REQUEST_RESID_TOAST;
		message.obj = resId;
		message.sendToTarget();
	}
	public void requestToast(String string) {
		Message message = mHandler.obtainMessage();
		message.what = HANDLE_REQUEST_RESSTRING_TOAST;
		message.obj = string;
		message.sendToTarget();
	}
	public void setEditBox(EditText editBox) {
		this.mEditBox = editBox;
		this.mEditBox.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				Log.d("ttt", "onTextChanged = "+s);
				
			}
			
			@Override
			public void beforeTextChanged(final CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

				Log.d("ttt", "beforeTextChanged = "+s);
				queueEvent(new  Runnable() {
					public void run() {
						mDirector.callBackEditText(s.toString());
					}
				});
			}
			
			@Override
			public void afterTextChanged(final Editable s) {
				// TODO Auto-generated method stub
				Log.d("ttt", "afterTextChanged = "+s);
				queueEvent(new  Runnable() {
					public void run() {
						mDirector.callBackEditText(s.toString());
					}
				});
			}
		});
	}
	public void requstEditBoxFocus() {
		mHandler.sendEmptyMessage(HANDLE_REQUEST_EDITBOX);
		
	}

	public boolean isInputTextFocus() {
		return mEditBox.isFocused();
	}
	public VRGLView(Context context) {
		super(context);
		initSurfaceView(context);
	}

	public VRGLView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initSurfaceView(context);
	}
 
	
//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//
//		if (event.getAction() == KeyEvent.ACTION_DOWN) {
//			SoundTool.soundKey(event.getKeyCode());
//		}
//		Log.d("ming", "dispatchKeyEvent = " + event.getKeyCode());
//		return super.dispatchKeyEvent(event);
//	}

	@Override
	public boolean dispatchDragEvent(DragEvent event) {

		Log.d("dispatch"," dispatchDragEvent "+event.getAction());
		return super.dispatchDragEvent(event);
	}
	@Override
	public boolean dispatchGenericMotionEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Log.d("dispatch"," dispatchGenericMotionEvent "+event.getAction());
		return super.dispatchGenericMotionEvent(event);
	}
	
	@Override
	protected boolean dispatchGenericPointerEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Log.d("dispatch"," dispatchGenericPointerEvent "+event.getAction());
		return super.dispatchGenericPointerEvent(event);
	}
	@Override
	public boolean dispatchKeyEventPreIme(KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d("dispatch"," dispatchKeyEventPreIme "+event.getAction());
		return super.dispatchKeyEventPreIme(event);
	}
	@Override
	protected boolean dispatchHoverEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Log.d("dispatch"," dispatchHoverEvent "+event.getAction());
		return super.dispatchHoverEvent(event);
	}
	long time;
	public boolean dispatchGlKeyEvent(KeyEvent event) {

		
		return super.dispatchGlKeyEvent(event);
	}
	/*
	 * 
	 */
	@Override
	protected boolean isUserInteruptKeyEvent(KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			if(Director.getInstance().getDialogSceneList().size() > 1){
				return true;
			}
		}
		return super.isUserInteruptKeyEvent(event);
	}
	public void updateScreenSaver() {
		mDirector.requstAsyncGeneralTask(new PostRunnable() {
			
			@Override
			public void run() {
				if (mDirector != null) {
					mDirector.updateScreensaver();;
				}
			}
		});
		
	}
	private void initSurfaceView(Context context) {
//		SoundTool.initSound(context);
		mContext = context;
		setRenderMode(RENDERMODE_WHEN_DIRTY);
//		setRenderMode(RENDERMODE_CONTINUOUSLY);
		setFocusable(true);
		setFocusableInTouchMode(true);
		requestFocus();
	}

	@Override
	protected Director shareDirector(Context context) {
		// TODO Auto-generated method stub
		mDirector = new MyDirector(context, this);
		return mDirector;
	}
}
