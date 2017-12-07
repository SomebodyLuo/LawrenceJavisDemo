package com.x.opengl.kernel.effect;

import com.x.opengl.kernel.GLObject;

/**
 * 颜色过滤器
 * @date   2013-10-17 09:38:14
 */
public class ColorFilter implements GLObject {

	public static final boolean[] FILTER_COLOR_NONE        = new boolean[]{true, true, true, true};
	public static final boolean[] FILTER_COLOR_TRANSPARENT = new boolean[]{true, true, true, false};
	public static final boolean[] FILTER_COLOR_RED         = new boolean[]{true, false, false, true};
	public static final boolean[] FILTER_COLOR_GREEN       = new boolean[]{false, true, false, true};
	public static final boolean[] FILTER_COLOR_BLUE        = new boolean[]{false, false, true, true};
	public static final boolean[] FILTER_COLOR_YELLOW      = new boolean[]{true, true, false, true};
	public static final boolean[] FILTER_COLOR_CYAN        = new boolean[]{false, true, true, true};
	public static final boolean[] FILTER_COLOR_MAGENTA     = new boolean[]{true, false, true, true};
	public static final boolean[] FILTER_COLOR_WHITE       = new boolean[]{true, true, true, true};
	
	private boolean   mEnabled     = true;
	private boolean[] mFilterColor = FILTER_COLOR_NONE;
	
	public ColorFilter() {
//		mGl = WolfRenderer.Resourcer.getGL();
	}
	
	public void setFilterColor(boolean[] filterColor) {
		if (filterColor != null) {
			mFilterColor = filterColor;
		}
	}
	
	public void setEnabled(boolean enabled) {
		mEnabled = enabled;
	}
	
	public boolean getEnabled() {
		return mEnabled;
	}
	
	public void update() {
//		mGl.glColorMask(mFilterColor[0], mFilterColor[1], mFilterColor[2], mFilterColor[3]);
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}

	
