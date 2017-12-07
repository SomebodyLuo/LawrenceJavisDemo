package com.x.opengl.kernel.effect;

import com.x.opengl.kernel.GLObject;

/**
 * 夜视效果
 * @date   2013-10-17 09:27:55
 */
public class NightSight implements GLObject{

	private boolean mEnabled = true;
//	private GL10    mGl      = null;
	
	public NightSight() {
//		mGl = WolfRenderer.Resourcer.getGL();
	}
	
	public void setEnabled(boolean enabled) {
		mEnabled = enabled;
	}
	
	public boolean getEnabled() {
		return mEnabled;
	}
	
	public void update() {
		if (mEnabled) {
//			mGl.glColorMask(false, true, false, true);
		}
	}

	@Override
	public void dispose() {
		
	}
	
}
