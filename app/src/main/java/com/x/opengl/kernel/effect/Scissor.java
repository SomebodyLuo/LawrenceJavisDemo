package com.x.opengl.kernel.effect;

import android.graphics.Rect;

import com.x.opengl.kernel.GLObject;

/**
 * 剪裁框
 * @date   2013-11-11 11:12:21
 */
public class Scissor implements GLObject{

//	private GL10      mGl          = null;
	private boolean   mEnabled     = true;
	private Rect      mScissorRect = new Rect();
	
	public Scissor() {
//		mGl = WolfRenderer.Resourcer.getGL();
	}
	
	public void setEnabled(boolean enabled) {
		mEnabled = enabled;
	}
	
	public boolean isEnabled() {
		return mEnabled;
	}
	
	public void setScissorRect(int left, int top, int right, int bottom) {
		mScissorRect.set(left, top, right, bottom); 
	}
	
	public void beginScissor() {
		if (mEnabled) {
//			mGl.glEnable(GL10.GL_SCISSOR_TEST);
//			mGl.glScissor(mScissorRect.left, mScissorRect.top, mScissorRect.width(), mScissorRect.height());
		}
	}
	
	public void endScissor() {
//		mGl.glDisable(GL10.GL_SCISSOR_TEST);
	}
	
	@Override
	public void dispose() {
		
	}

}
