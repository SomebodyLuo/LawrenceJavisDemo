package com.x.components.node;
//package com.wolf.node;
//
//import android.annotation.SuppressLint;
//
//import com.wolf.Director;
//import com.wolf.kernel.Drawable;
//import com.wolf.kernel.EngineConstanst;
//import com.wolf.utils.MLog;
//
///**
// * 该方案使用直接修改模型视图矩阵的方式进行飞框的位置计算，但过渡动画比较难看,后期如果有新的突破再做打算
// * 
// * 
// */
//@SuppressLint("NewApi")
//public class CopyOfFocusView extends View
//{
//
//	// public static Position[] mPosition = new Position[10];
//	// public static Scale[] mScale = new Scale[10];
//	// public static Rotate[] mRotate = new Rotate[10];
//
//
//	private View mBindView;
//	// private static T_Transform mTransform = new T_Transform();
//	// private static T_AnimationSet mAnimationSet = new T_AnimationSet(); //
//	// 动画封装类
//	// private static T_Transform mDestinationTransform = new T_Transform();
//
//	private FocusViewAnimation mFocusViewAnimation = new FocusViewAnimation();
//	private FocusViewAnimation mFocusViewDestinationAnimation = new FocusViewAnimation();
//
//	private float[] mModelViewMatrix = new float[16];
//
//
//	public CopyOfFocusView()
//	{
//		MLog.d("FocusView()");
//		mBGDrawable = new Drawable(0);
//	}
//
//	@Override
//	public void draw()
//	{
//		if (!mIsVisible || mBindView == null)
//		{
//			return;
//		}
//		Director.sGLESVersion.pushMatrix();
//
//		onAnimation();
//		mBGDrawable.setModelViewMatrix(mModelViewMatrix);
//		mBGDrawable.draw(mBindView.getFinalAlpha(), 1, mPrivateFlag);
//
//		Director.sGLESVersion.popMatrix();
//	}
//
//	protected void onAnimation()
//	{
//		mFocusViewAnimation.run();
//		// mAnimationSet.normal(mTransform, 1.1f,
//		// 1.1f,Director.sCamera.getScreenHeight(),Director.sCamera.getScreenHeight()
//		// ,mThickness);
//		mFocusViewAnimation.fill(mModelViewMatrix);
//		if (mFocusViewAnimation.hasMoreAnimation())
//		{
//			postInvalidate();
//		}
//	}
//
//	/*
//	 * 检查view是不是FocusView绑定的mBindView
//	 */
//	public boolean hasBinded(View view)
//	{
//		if (view.equals(mBindView))
//		{
//			return true;
//		}
//		return false;
//	}
//
//	public void bind(View view)
//	{
//
//		this.setVisibility(true);
//		mBindView = view;
//
//		check();
//		postInvalidate();
//	}
//
//	private void check()
//	{
//
//		boolean flag = false;
//
//		float scaleX_Y = 1.1f;
//		float translate_Z = +EngineConstanst.PIX_REFERENCE * (mBindView.getDrawableCount());// 确保飞框在最顶层
//
//		float[] tempModelViewMatrix = new float[16];
//		for (int i = 0; i < mModelViewMatrix.length; i++)
//		{
//
//			if (
//
//			// 对x的缩放
//			i == 0 || i == 1 || i == 2
//
//			// 对y的缩放
//					|| i == 4 || i == 5 || i == 6
//
//			// 对z的缩放,在这里不对z缩放
//			// || i == 8
//			// || i == 9
//			// || i == 10
//
//			)
//			{
//				tempModelViewMatrix[i] = mBindView.getFinalModelViewMatrix()[i] * scaleX_Y;
//
//			} else if (// x的位移
//						// y的位移
//						// z的位移
//			i == 14)
//			{
//
//				tempModelViewMatrix[i] = mBindView.getFinalModelViewMatrix()[i] + translate_Z;
//
//			} else
//			{
//
//				tempModelViewMatrix[i] = mBindView.getFinalModelViewMatrix()[i];
//
//			}
//
//			flag |= mModelViewMatrix[i] != tempModelViewMatrix[i];
//
//			// if(flag){
//			// break;
//			// }
//		}
//		if (flag)
//		{
//			mFocusViewAnimation.setDetination(tempModelViewMatrix);
//		}
//	}
//
//	public void updateDestination(View view)
//	{
//
//		check();
//	}
//
//	public boolean hasMoreAnimation()
//	{
//		return mFocusViewAnimation.hasMoreAnimation();
//	}
//
//	public View getBindView()
//	{
//		return mBindView;
//	}
//
//	public void focusTo(View view)
//	{
//
//		if (mBindView != null)
//		{
//			mBindView.setFocused(false);
//			mBindView.onRemoveFocus();
//		}
//		bind(view);
//		view.setFocused(true);
//		view.onFocus();
//	}
//
//	public void removeBindView()
//	{
//
//		if (mBindView != null)
//		{
//			mBindView.setFocused(false);
//			mBindView.onRemoveFocus();
//			mBindView = null;
//		}
//		this.setVisibility(false);
//	}
//
//	class FocusViewAnimation
//	{
//
//		private AnimationHolder[] mAnimationHolders = new AnimationHolder[16];
//
//		public FocusViewAnimation()
//		{
//			for (int i = 0; i < mAnimationHolders.length; i++)
//			{
//				mAnimationHolders[i] = new AnimationHolder(0, 1);
//			}
//		}
//
//		public void fill(float[] modelViewMatrix)
//		{
//
//			for (int i = 0; i < mAnimationHolders.length; i++)
//			{
//				modelViewMatrix[i] = mAnimationHolders[i].getCurrentIndex();
//			}
//		}
//
//		public void setDetination(float[] finalModelViewMatrix)
//		{
//			for (int i = 0; i < finalModelViewMatrix.length; i++)
//			{
//				mAnimationHolders[i].setDestinationIndex(finalModelViewMatrix[i]);
//			}
//		}
//
//		public void run()
//		{
//			for (int i = 0; i < mAnimationHolders.length; i++)
//			{
//				mAnimationHolders[i].run(this);
//			}
//		}
//
//		public boolean hasMoreAnimation()
//		{
//			boolean has = false;
//			for (int i = 0; i < mAnimationHolders.length; i++)
//			{
//				has |= mAnimationHolders[i].isRunning();
//				if (has)
//				{
//					break;
//				}
//			}
//			return has;
//		}
//
//	}
//}
