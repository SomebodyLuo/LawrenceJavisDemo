package com.x.components.node;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.x.opengl.kernel.AbstractFocusFinder;
import com.x.opengl.kernel.EngineConstanst;
import com.x.opengl.kernel.MiniDistanceFocusFinder;
import com.x.opengl.kernel.Texture;

@SuppressLint("NewApi")
public abstract class DialogXScene extends XScene {


	protected DialogXScene(){

		mFocusFinder = new MiniDistanceFocusFinder();
		Bitmap bitmap = Bitmap.createBitmap(40,40,Config.ARGB_8888);
		bitmap.eraseColor(Color.argb(155, 0,0, 255));
		mFocusView = new IntactBoxFocus();
		mFocusView.setBackground(bitmap);

		mFocusViewViewGroup = new ViewGroup();
		mFocusViewViewGroup.setWidth(EngineConstanst.REFERENCE_SCREEN_WIDTH);
		mFocusViewViewGroup.setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
		mFocusViewViewGroup.addChild(mFocusView);
		mFocusViewViewGroup.setParent(this);
		
	}

	public void setFocusViewImageResource(int rsid) {
		mFocusView.setBackgroundResource(rsid);
	}
	public void setFocusViewImage(Texture texture) {
		mFocusView.setBackground(texture);
	}
	public void setFocusViewScale(float x, float y) {
		// TODO Auto-generated method stub
		mFocusView.setSelfScale(x, y);
	}
	/**
	 * @param event
	 * @return
	 */
	public boolean onKeyEvent(KeyEvent event) {
		
		boolean state = false;
		if(getLayerSize() == 0){
			return state;
		}
		boolean downFlag = false;
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			downFlag = true;
		}
			
		switch (event.getKeyCode())
			{
			case KeyEvent.KEYCODE_DPAD_UP: {
				if(downFlag){
					mFocusFinder.findNextFocus(AbstractFocusFinder.UP, this);
				}
				state = true;
			}
			break;
			case KeyEvent.KEYCODE_DPAD_DOWN: {

				if(downFlag){
					mFocusFinder.findNextFocus(AbstractFocusFinder.DOWN, this);
				}
				state = true;
			}
			break;
			case KeyEvent.KEYCODE_DPAD_LEFT: {

				if(downFlag){
					mFocusFinder.findNextFocus(AbstractFocusFinder.LEFT, this);
				}
				state = true;
			}
			break;
			case KeyEvent.KEYCODE_DPAD_RIGHT: {
				
				if(downFlag){
					mFocusFinder.findNextFocus(AbstractFocusFinder.RIGHT, this);
				}
				state = true;
			}
			break;
			 case KeyEvent.KEYCODE_DPAD_CENTER:
			 case KeyEvent.KEYCODE_ENTER: {

				if (downFlag) {

					if ( getFocusView().getBindView() == null) {
						mFocusFinder.findNextFocus(AbstractFocusFinder.UP, this);
					}
				}
				state = true;
			 }
			 break;
			 case KeyEvent.KEYCODE_BACK:
			 case KeyEvent.KEYCODE_ESCAPE: {

				if (downFlag) {

//					if ( getFocusView().getBindView() != null) {
//						 getFocusView().removeBindView();
//						 Director.getInstance().postInvalidate();
//					}
				}
				state = true;
			 }
			 break;
			}
		return state;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}
	public boolean onWheelEvent(float vscroll) {
		return true;
	}


}
