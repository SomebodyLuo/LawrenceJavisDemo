package com.x.components.widget;

import com.x.Director;
import com.x.opengl.kernel.XScroller;

public class AdapterAnimationHelper {

	private static final int	ANIMATION_DURATION		= 300;
	private static final int	ANIMATION_FAST_DURATION	= 100;
	public static final int		DEFAULT					= -1;							// 初始
	public static final int		DRAG					= 0;							// 按住左键拖动
	public static final int		DRAG_UP					= 1;							// 拖动后放开左键，即抬起
	public static final int		DRAG_UP_WAIT			= 2;							// 等待惯性结束
	public static final int		DRAG_UP_TO_ATTRACT		= 3;							// 惯性结束后需要归位到临近的view位置

	private int					mState					= DEFAULT;
	private XScroller		mwolfScroller;
	private long				mTime					= System.currentTimeMillis();

	public AdapterAnimationHelper() {

		mwolfScroller = new XScroller(Director.getInstance().getContext());
		
	}

	public void init(int index, int mSpaceY) {

		mwolfScroller = new XScroller(Director.getInstance().getContext(), index, mSpaceY);
		mwolfScroller.scrollToTargetIndex(index, ANIMATION_DURATION);
	}

	public float getCurrentPara() {
		return mwolfScroller.getCurrentPara();
	}
	public float getTargetPara() {
		return mwolfScroller.getTargetPara();
	}

	public void scrollToTargetIndex(int newIndex) {
//		Log.d("debug","System.currentTimeMillis() - mTime = "+(System.currentTimeMillis() - mTime));
		if (System.currentTimeMillis() - mTime < 150) {
			mwolfScroller.scrollToTargetIndex(newIndex, ANIMATION_FAST_DURATION);

		} else {

			mwolfScroller.scrollToTargetIndex(newIndex, ANIMATION_DURATION);
		}
		mTime = System.currentTimeMillis();
	}

	public boolean computeOffset() {
		boolean b = mwolfScroller.computeOffset();
		// if(!b){
		// if(mState == DRAG_STOP_ATTRACT || mState == DRAG_STOP){
		// mState = DEFAULT;
		// }
		// }
		return b;
	}

	public float getTargetIndex() {
		return mwolfScroller.getTargetIndex();
	}

	public void dragBy(float offset, int minX, int maxX) {
		float tarp = mwolfScroller.getCurrentPara() + offset;
		if (tarp < minX) {
			mwolfScroller.setCurrentPara(minX + 1);
			mState = DRAG;
			return;
		} else if (tarp > maxX) {
			mwolfScroller.setCurrentPara(maxX - 1);
			mState = DRAG;
			return;
		}
		mwolfScroller.dragBy((int) offset);
		mState = DRAG;
	}

	public void dragBy(float offset) {
		mwolfScroller.dragBy((int) offset);
		mState = DRAG;
	}

	public void flingTo(int velocity, int minX, int maxX, int minY, int maxY) {
		mwolfScroller.flingTo(velocity, minX, maxX, minY, maxY);
		mState = DRAG_UP;
	}

	public int getState() {
		return mState;
	}

	public void setState(int s) {
		mState = s;
	}

	public void forceFinish() {
		mwolfScroller.forceFinish();
	}

	public double getCurrVelocity() {
		return mwolfScroller.getCurrVelocity();
	}

}
