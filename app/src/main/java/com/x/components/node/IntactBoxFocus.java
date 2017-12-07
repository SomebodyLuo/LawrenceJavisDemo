package com.x.components.node;

import android.annotation.SuppressLint;

import com.x.Director;
import com.x.opengl.kernel.AnimationHolder;
import com.x.opengl.kernel.EngineConstanst;

/**
 * 该方案使用直接修改模型视图矩阵的方式进行飞框的位置计算，但过渡动画比较难看,后期如果有新的突破再做打算
 * 
 * 
 */
@SuppressLint("NewApi")
public class IntactBoxFocus extends MoveBox
{

	// public static Position[] mPosition = new Position[10];
	// public static Scale[] mScale = new Scale[10];
	// public static Rotate[] mRotate = new Rotate[10];


	// private static T_Transform mTransform = new T_Transform();
	// private static T_AnimationSet mAnimationSet = new T_AnimationSet(); //
	// 动画封装类
	// private static T_Transform mDestinationTransform = new T_Transform();

	private FocusViewAnimation mFocusViewAnimation = new FocusViewAnimation();

	private float[] mModelViewMatrix = new float[16];

	private float mSpeed = 0.4f;

	@Override
	protected void focusUpdateState(View view, boolean byView) {
		boolean flag = false;

		float translate_Z = +EngineConstanst.PIX_REFERENCE * (mBindView.getDrawableCount());// 确保飞框在最顶层
//		float translate_Z = -EngineConstanst.PIX_REFERENCE * (1);// 确保飞框在最顶层

		float[] tempModelViewMatrix = new float[16];
		for (int i = 0; i < mModelViewMatrix.length; i++)
		{

			if (i == 0 || i == 1 || i == 2)// 对x的缩放	
			{
				tempModelViewMatrix[i] = mBindView.getFinalModelMatrix()[i] * mScaleX;

			}else if(i == 4 || i == 5 || i == 6)// 对y的缩放 
			{
				tempModelViewMatrix[i] = mBindView.getFinalModelMatrix()[i] * mScaleY;
				
			} else if( i == 8 || i == 9 || i == 10)	// 对z的缩放,在这里不对z缩放
			{
				tempModelViewMatrix[i] = mBindView.getFinalModelMatrix()[i] ;
				
			}else if (i == 14)// z的位移
			{
				tempModelViewMatrix[i] = mBindView.getFinalModelMatrix()[i] /*+ translate_Z*/;
			} else	
			{
				// x的位移 // y的位移
				tempModelViewMatrix[i] = mBindView.getFinalModelMatrix()[i];
			}

			flag |= mModelViewMatrix[i] != tempModelViewMatrix[i];

			// if(flag){
			// break;
			// }
		}
		if (flag)
		{
			mFocusViewAnimation.setDetination(tempModelViewMatrix,byView);
		}
	}

	@Override
	protected void focusDraw() {
		Director.sGLESVersion.pushMatrix();

		onAnimation();
		mBGDrawable.setModelViewMatrix(mModelViewMatrix);
		mBGDrawable.draw( );

		Director.sGLESVersion.popMatrix();
	}

	@Override
	protected void focusOnAnimation() {
		mFocusViewAnimation.run();
		// mAnimationSet.normal(mTransform, 1.1f,
		// 1.1f,Director.sCamera.getScreenHeight(),Director.sCamera.getScreenHeight()
		// ,mThickness);
		mFocusViewAnimation.fill(mModelViewMatrix);
		if (mFocusViewAnimation.hasMoreAnimation())
		{
			postInvalidate();
		}
	}

	@Override
	protected boolean focusHasMoreAnimation() {
		return mFocusViewAnimation.hasMoreAnimation();
	}

	class FocusViewAnimation
	{

		private AnimationHolder[] mAnimationHolders = new AnimationHolder[16];

		public FocusViewAnimation()
		{
			for (int i = 0; i < mAnimationHolders.length; i++)
			{
				mAnimationHolders[i] = new AnimationHolder(0, 1);
			}
		}

		public void fill(float[] modelViewMatrix)
		{

			for (int i = 0; i < mAnimationHolders.length; i++)
			{
				modelViewMatrix[i] = mAnimationHolders[i].getCurrentIndex();
			}
		}

		public void setDetination(float[] finalModelViewMatrix, boolean byView)
		{
			float speed =mSpeed ;
			if(byView){
				speed = mSpeed * 1.2f;
			}
			for (int i = 0; i < finalModelViewMatrix.length; i++)
			{
				mAnimationHolders[i].setDestinationIndex(finalModelViewMatrix[i], AnimationHolder.RUNNING, speed, true, false);
			}
		}

		public void run()
		{
			for (int i = 0; i < mAnimationHolders.length; i++)
			{
				mAnimationHolders[i].run(this);
			}
		}

		public boolean hasMoreAnimation()
		{
			boolean has = false;
			for (int i = 0; i < mAnimationHolders.length; i++)
			{
				has |= mAnimationHolders[i].isRunning();
				if (has)
				{
					break;
				}
			}
			return has;
		}

	}

	@Override
	protected void setSpeed(float speed) {
		// TODO Auto-generated method stub
		mSpeed = speed;
	}


}
