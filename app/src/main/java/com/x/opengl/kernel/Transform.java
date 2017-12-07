package com.x.opengl.kernel;

import android.content.Context;
import android.view.animation.Interpolator;

import com.x.Director;

/**
 * 变换矩阵
 * 
 * @date 2013-08-08 11:10:10
 */
public class Transform implements Cloneable {

//	public AnimationHolder mHolderTranslateX = new AnimationHolder(0,EngineConstanst.ENGINE_SCREEN_REFERENCE/2);//x坐标初始值0  此处1并非实际的单位，实际单位为1*APPConstanst.PIX_REFERENCE 。间隔值取了与APPConstanst.ENGINE_SCREEN_REFERENCE相关的值
//	public AnimationHolder mHolderTranslateY = new AnimationHolder(0,EngineConstanst.ENGINE_SCREEN_REFERENCE/2);//y坐标初始值0
//	public AnimationHolder mHolderTranslateZ = new AnimationHolder(0,EngineConstanst.ENGINE_SCREEN_REFERENCE/2);//z坐标初始值0
//	
//	public AnimationHolder mHolderScaleX = new AnimationHolder(1, 500);//缩放，x 范围为0到1,然后划分为500个间隔值
//	public AnimationHolder mHolderScaleY = new AnimationHolder(1, 500);//缩放，y
//	public AnimationHolder mHolderScaleZ = new AnimationHolder(1, 500);//缩放，z
//	
//	public AnimationHolder mHolderRotateX = new AnimationHolder(0, 1);//旋转初始，x = 0
//	public AnimationHolder mHolderRotateY = new AnimationHolder(0, 1);//旋转初始，y = 0
//	public AnimationHolder mHolderRotateZ = new AnimationHolder(0, 1);//旋转初始，z = 0
//	
//	public 	AnimationHolder mHolderAlpha = new AnimationHolder(1, 255); //初始透明度 1，范围为0-1，这之间的间隔划分255等分
	
	
	public XScroller mHolderTranslateX = null;//x坐标初始值0  此处1并非实际的单位，实际单位为1*APPConstanst.PIX_REFERENCE 。间隔值取了与APPConstanst.ENGINE_SCREEN_REFERENCE相关的值
	public XScroller mHolderTranslateY = null;//y坐标初始值0
	public XScroller mHolderTranslateZ = null;//z坐标初始值0
	
	public XScroller mHolderScaleX = null;//缩放，x 范围为0到1,然后划分为500个间隔值
	public XScroller mHolderScaleY = null;//缩放，y
	public XScroller mHolderScaleZ = null;//缩放，z
	
	public XScroller mHolderRotateX = null;//旋转初始，x = 0
	public XScroller mHolderRotateY = null;//旋转初始，y = 0
	public XScroller mHolderRotateZ = null;//旋转初始，z = 0
	
	public 	XScroller mHolderAlpha = null; //初始透明度 1，范围为0-1，这之间的间隔划分255等分
	
	public static final String	TAG					= "Transform";

	/**
	 * 世界坐标系上的Transform属性,这里的位置都是相对位置，仅仅是单个view自身的变换属性，如果说view的父布局如viewgroup进行了变换，那么作为view本身的这几个属性不会发生改变的
	 */
	public Position				Position			= new Position();
	public Rotate				Rotate				= new Rotate();
	public Scale				Scale				= new Scale();
	
	public float 				Alpha 				= 1;

	/**
	 * 渲染排序 是物体在世界坐标系中距离视点的距离
	 */
	public float				RenderIndex			= -1;
	public float[] mFinalMatrix;

	public static int ANIMATION_DURATION = 300;
	
	public Transform() {
		Context context  = Director.getInstance().getContext();
		mHolderTranslateX = new XScroller(context,0,10);//x坐标初始值0  此处1并非实际的单位，实际单位为1*APPConstanst.PIX_REFERENCE 。间隔值取了与APPConstanst.ENGINE_SCREEN_REFERENCE相关的值
		mHolderTranslateY = new XScroller(context,0,10);//y坐标初始值0
		mHolderTranslateZ = new XScroller(context,0,10);//z坐标初始值0
		
		mHolderScaleX = new XScroller(context,1, 255);//缩放，x 范围为0到1,然后划分为255个间隔值
		mHolderScaleY = new XScroller(context,1, 255);//缩放，y
		mHolderScaleZ = new XScroller(context,1, 255);//缩放，z
		
		mHolderRotateX = new XScroller(context,0, 1);//旋转初始，x = 0
		mHolderRotateY = new XScroller(context,0, 1);//旋转初始，y = 0
		mHolderRotateZ = new XScroller(context,0, 1);//旋转初始，z = 0
		
		mHolderAlpha = new XScroller(context,1, 255); //初始透明度 1，范围为0-1，这之间的间隔划分255等分
		
	}
	

	public Transform clone() {
		Transform cloned = null;

		try {
			cloned = (Transform) super.clone();
			cloned.Position.set(Position.get());
			cloned.Rotate.set(Rotate.get());
			cloned.Scale.set(Scale.get());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			cloned = null;
		}

		return cloned;
	}

	public boolean transformEquals(Object o) {
		Transform transform = (Transform) o;
		
		boolean flag1 = Position.equals(transform.Position);
		boolean flag2 = Scale.equals(transform.Scale);
		boolean flag3 = Rotate.equals(transform.Rotate);
		boolean flag4 = Alpha == transform.Alpha;

		return 	   flag1 
				&& flag2 
				&& flag3 
				&& flag4;
	}

	/**
	 * 归一化处理
	 * @param width
	 * @param height
	 * @param mThickness
	 * @param transX
	 * @param transY
	 * @param transZ
	 */
	public void normal(float width, float height, float mThickness, int transX, int transY, int transZ) {

		Scale.X =  mHolderScaleX.getCurrentIndex()*width;
		Scale.Y =  mHolderScaleY.getCurrentIndex()*height;
		Scale.Z =  mHolderScaleZ.getCurrentIndex()*mThickness;
		
		Position.X =	mHolderTranslateX.getCurrentIndex()*EngineConstanst.PIX_REFERENCE*transX;
		Position.Y =	mHolderTranslateY.getCurrentIndex()*EngineConstanst.PIX_REFERENCE*transY;
		Position.Z =	mHolderTranslateZ.getCurrentIndex()*EngineConstanst.PIX_REFERENCE*transZ;
		
		Rotate.X = mHolderRotateX.getCurrentIndex();
		Rotate.Y = mHolderRotateY.getCurrentIndex();
		Rotate.Z = mHolderRotateZ.getCurrentIndex();
		
		Alpha = mHolderAlpha.getCurrentIndex();
//		Scale.X =  1;
//		Scale.Y =  1;
//		Scale.Z =  1;
//		
//		Position.X =	0;
//		Position.Y =	0;
//		Position.Z =	-2;
//		
//		Rotate.X = 0;
//		Rotate.Y = 0;
//		Rotate.Z = 0;
//		
//		Alpha = 1;
	}

	public void normalFocus(Transform transform, float width, float height,float mThickness, float transX,  float transY, float transZ ,float addZ ) {


		transform.Scale    = new Scale(
				mHolderScaleX.getCurrentIndex()*width, 
				mHolderScaleY.getCurrentIndex()*height, 
				mHolderScaleZ.getCurrentIndex()*mThickness
				);
		
		transform.Position = new Position(
				mHolderTranslateX.getCurrentIndex()*transX, 
				mHolderTranslateY.getCurrentIndex()*transY, 
				mHolderTranslateZ.getCurrentIndex()*transZ+1+EngineConstanst.PIX_REFERENCE*addZ /// 此处的1其实是等于camera中eye的z值,but why?
//				mHolderTranslateZ.getCurrent()+0
//				mHolderTranslateZ.getCurrent()+APPConstanst.PIX_REFERENCE*transZ
				);
		
		
		transform.Rotate   = new Rotate(
				mHolderRotateX.getCurrentIndex(), 
				mHolderRotateY.getCurrentIndex(), 
				mHolderRotateZ.getCurrentIndex());
		
		transform.Alpha = mHolderAlpha.getCurrentIndex();
		
	}
	

	public void setScale(float x, float y, float z) {
		mHolderScaleX.setCurrentIndex(x);
		mHolderScaleY.setCurrentIndex(y);
		mHolderScaleZ.setCurrentIndex(z);
		Director.getInstance().postInvalidate();
	}
	public void scaleTo(float x, float y, float z) {

		mHolderScaleX.scrollToTargetIndex(x, ANIMATION_DURATION); 
		mHolderScaleY.scrollToTargetIndex(y, ANIMATION_DURATION);
		mHolderScaleZ.scrollToTargetIndex(z, ANIMATION_DURATION);
		Director.getInstance().postInvalidate();
	}
	public void scaleTo(float x, float y, float z,int time ) {

		mHolderScaleX.scrollToTargetIndex(x, time); 
		mHolderScaleY.scrollToTargetIndex(y, time);
		mHolderScaleZ.scrollToTargetIndex(z, time);
		Director.getInstance().postInvalidate();
	}
	public void scaleTo(float x, float y, float z, int method,float f,boolean limitMin,boolean limitMax) {

		scaleTo(x,y,z);
		Director.getInstance().postInvalidate();
	}

//	public void scaleTo(float x, float y, float z, Parameter parameter) {
//		mHolderScaleX.setDestinationIndex(x,parameter);
//		mHolderScaleY.setDestinationIndex(y,parameter);
//		mHolderScaleZ.setDestinationIndex(z,parameter);
//		Director.getInstance().postInvalidate();
//		
//	}

	public void setTranslate(float x, float y, float z) {
		mHolderTranslateX.setCurrentIndex(x);
		mHolderTranslateY.setCurrentIndex(y);
		mHolderTranslateZ.setCurrentIndex(z);
		Director.getInstance().postInvalidate();
	}
	public void setTranslate(float x, float y, float z,boolean xFlag,boolean yFlag ,boolean zFlag) {
		if(xFlag){
			mHolderTranslateX.setCurrentIndex(x);
		}
		if(yFlag){
			mHolderTranslateY.setCurrentIndex(y);
		}
		if(zFlag){
			mHolderTranslateZ.setCurrentIndex(z);
		}
		Director.getInstance().postInvalidate();
	}
	public void setTranslate(float x, float y) {
		mHolderTranslateX.setCurrentIndex(x);
		mHolderTranslateY.setCurrentIndex(y);
		Director.getInstance().postInvalidate();
	}
	
	public void translateTo(float x, float y, float z ) {
		mHolderTranslateX.scrollToTargetIndex(x, ANIMATION_DURATION );
		mHolderTranslateY.scrollToTargetIndex(y, ANIMATION_DURATION );
		mHolderTranslateZ.scrollToTargetIndex(z, ANIMATION_DURATION );
		Director.getInstance().postInvalidate();
	}

	public void translateTo(float x, float y, float z,int time) {
		mHolderTranslateX.scrollToTargetIndex(x, time);
		mHolderTranslateY.scrollToTargetIndex(y, time);
		mHolderTranslateZ.scrollToTargetIndex(z, time);
		Director.getInstance().postInvalidate();
	};
	public void translateTo(float x, float y, float z,int time, Interpolator i) {
		mHolderTranslateX.scrollToTargetIndex(x, time,i);
		mHolderTranslateY.scrollToTargetIndex(y, time,i);
		mHolderTranslateZ.scrollToTargetIndex(z, time,i);
		Director.getInstance().postInvalidate();
	};
	public void translateZTo(float z) {
		mHolderTranslateZ.scrollToTargetIndex(z, ANIMATION_DURATION);
		Director.getInstance().postInvalidate();
	}

	public void translateTo(float x, float y, float z, boolean xFlag, boolean yFlag, Boolean zFlag, int time ) {
		if(xFlag){
			mHolderTranslateX.scrollToTargetIndex(x, time);
		}
		if(yFlag){
			mHolderTranslateY.scrollToTargetIndex(y, time);
		}
		if(zFlag){
			mHolderTranslateZ.scrollToTargetIndex(z, time);
		}
		Director.getInstance().postInvalidate();
	}
	public void translateTo(float x, float y, float z, boolean xFlag, boolean yFlag, Boolean zFlag) {
		if(xFlag){
			mHolderTranslateX.scrollToTargetIndex(x, ANIMATION_DURATION);
		}
		if(yFlag){
			mHolderTranslateY.scrollToTargetIndex(y, ANIMATION_DURATION);
		}
		if(zFlag){
			mHolderTranslateZ.scrollToTargetIndex(z, ANIMATION_DURATION);
		}
		Director.getInstance().postInvalidate();
	}

	public void setRotate(float x, float y, float z) {
		mHolderRotateX.setCurrentIndex(x);
		mHolderRotateY.setCurrentIndex(y);
		mHolderRotateZ.setCurrentIndex(z);
		Director.getInstance().postInvalidate();
	}

	public void rotateTo(float x, float y, float z) {

		mHolderRotateX.scrollToTargetIndex(x, ANIMATION_DURATION);
		mHolderRotateY.scrollToTargetIndex(y, ANIMATION_DURATION);
		mHolderRotateZ.scrollToTargetIndex(z, ANIMATION_DURATION);
		Director.getInstance().postInvalidate();
		
	}
	public void rotateTo(float x, float y, float z, int time ) {

		mHolderRotateX.scrollToTargetIndex(x, time);
		mHolderRotateY.scrollToTargetIndex(y, time);
		mHolderRotateZ.scrollToTargetIndex(z, time);
		Director.getInstance().postInvalidate();
		
	}


	public void setAlpha(float alpha) {
		mHolderAlpha.setCurrentIndex(alpha);
		Director.getInstance().postInvalidate();
	}

	public void alphaTo(float alpha) {
		mHolderAlpha.scrollToTargetIndex(alpha,ANIMATION_DURATION);
		Director.getInstance().postInvalidate();
	}
	public void alphaTo(float alpha,int time ) {
		mHolderAlpha.scrollToTargetIndex(alpha,time);
		Director.getInstance().postInvalidate();
	}
	
	public void alphaMutil(float alpha) {
		mHolderAlpha.scrollToTargetIndex(mHolderAlpha.getCurrentIndex() * alpha,0);
		Director.getInstance().postInvalidate();
	}
	public void setFinalModeMatrix(float[] finalMatrix){
		this.mFinalMatrix = finalMatrix;
	}
	
	public void translateAdd(float x, float y, float z) {
		
		translateTo(mHolderTranslateX.getCurrentIndex() + x,mHolderTranslateY.getCurrentIndex()  + y,mHolderTranslateZ.getCurrentIndex()  +z);

		Director.getInstance().postInvalidate();
	}

	public void scaleAdd(float x, float y, float z) {
		scaleTo(mHolderScaleX.getCurrentIndex() + x,mHolderScaleY.getCurrentIndex() + y,mHolderScaleZ.getCurrentIndex() + z);
		Director.getInstance().postInvalidate();
	}

	public void rotateAdd(float x, float y, float z) {

		rotateTo(mHolderRotateX.getCurrentIndex() + x,mHolderRotateY.getCurrentIndex() + y,mHolderRotateZ.getCurrentIndex() + z);
		Director.getInstance().postInvalidate();
		
	}
	
	public void alphaAdd(float alphaOffset) {
		alphaTo(mHolderAlpha.getCurrentIndex() + alphaOffset);
		Director.getInstance().postInvalidate();
	}

	
	public boolean hasMoreAnimation() {
		boolean 
		boo = mHolderTranslateX.computeOffset() ;
		boo |= mHolderTranslateY.computeOffset() ;
		boo |= mHolderTranslateZ.computeOffset() ;

		boo |= mHolderScaleX.computeOffset() ;
		boo |= mHolderScaleY.computeOffset() ;
		boo |= mHolderScaleZ.computeOffset() ;
		
		boo |= mHolderRotateX.computeOffset() ;
		boo |= mHolderRotateY.computeOffset() ;
		boo |= mHolderRotateZ.computeOffset() ;
		
		boo |= mHolderAlpha.computeOffset() ;
		return boo;
	}

	public void run() {
//		mHolderTranslateX.run(this);
//		mHolderTranslateY.run(this);
//		mHolderTranslateZ.run(this);
//
//		mHolderScaleX.run(this);
//		mHolderScaleY.run(this);
//		mHolderScaleZ.run(this);
//		
//		mHolderRotateX.run(this);
//		mHolderRotateY.run(this);
//		mHolderRotateZ.run(this);
//		
//		mHolderAlpha.run(this);
	}

	public void transformTo(Transform transform,boolean byView) {


		
//		float speed = 0.4f;
//		if(byView){
//			speed = 0.8f;
//		}
//		
		mHolderTranslateX.scrollToTargetIndex( transform.Position.X,0);
		mHolderTranslateY.scrollToTargetIndex(transform.Position.Y,0);
		mHolderTranslateZ.scrollToTargetIndex(transform.Position.Z,0);
		
		mHolderRotateX.scrollToTargetIndex(transform.Rotate.X,0);
		mHolderRotateY.scrollToTargetIndex(transform.Rotate.Y,0);
		mHolderRotateZ.scrollToTargetIndex(transform.Rotate.Z,0);
		
		mHolderScaleX.scrollToTargetIndex(transform.Scale.X,0);
		mHolderScaleY.scrollToTargetIndex(transform.Scale.Y,0);
		mHolderScaleZ.scrollToTargetIndex(transform.Scale.Z,0);
		
		mHolderAlpha.scrollToTargetIndex(transform.Alpha,0);
		
	}



}
