package com.x.components.node;

import android.annotation.SuppressLint;

import com.x.opengl.kernel.Drawable;

/**
 * 移动选框,即常说的飞框
 * 因技术限制,有两种实现,各有优缺点
 * 
 */
@SuppressLint("NewApi")
public abstract class MoveBox extends View
{

	// public static Position[] mPosition = new Position[10];
	// public static Scale[] mScale = new Scale[10];
	// public static Rotate[] mRotate = new Rotate[10];
	//	private static FocusView mFocusView;
	
	protected View mBindView;  
	protected float	mScaleX = 1;
	protected float	mScaleY = 1;
	protected abstract void focusUpdateState(View view,boolean byView);
	protected abstract void focusDraw();
	protected abstract void focusOnAnimation();
	protected abstract boolean focusHasMoreAnimation();
	protected  abstract void setSpeed(float speed);

	public MoveBox()
	{
		mBGDrawable = new Drawable(0);
	}
	@Override
	public void draw()
	{
		if (!mIsVisible || mBindView == null)
		{
			return;
		}
		
		focusDraw();
	}
	protected void onAnimation()
	{
		focusOnAnimation();
	}

	/*
	 * 检查view是不是FocusView绑定的mBindView
	 */
	public boolean hasBinded(View view)
	{
		if (view.equals(mBindView))
		{
			return true;
		}
		return false;
	}

	public void bind(View view)
	{

		this.setVisibility(true);

		mBindView = view;
		focusUpdateState(view,false);
		postInvalidate();
	}
	public  boolean hasMoreAnimation()
	{
		return focusHasMoreAnimation();
	}

	public View getBindView()
	{
		return mBindView;
	}

	public void focusTo(View view)
	{

		if(view == null || !view.isVisiable() || !view.isFocusable() || view.isFocused()){
			return;
		} 
		
		if (mBindView != null)
		{
			mBindView.onDispatchFocus(false);
		}
		bind(view);
		view.onDispatchFocus(true);
	}

	public void removeBindView()
	{

		if (mBindView != null)
		{
			mBindView.onDispatchFocus(false);
			mBindView = null;
		}
		this.setVisibility(false);
	}

	public void setSelfScale(float x, float y) {
		this.mScaleX = x;
		this.mScaleY = y;
	}

}
