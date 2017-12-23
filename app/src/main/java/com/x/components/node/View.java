package com.x.components.node;

import java.util.ArrayList;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;

import com.x.Director;
import com.x.opengl.gles.AttributeStatck;
import com.x.opengl.gles.StencilValueStatck;
import com.x.opengl.kernel.Drawable;
import com.x.opengl.kernel.Mesh;
import com.x.opengl.kernel.Position;
import com.x.opengl.kernel.Rotate;
import com.x.opengl.kernel.Scale;
import com.x.opengl.kernel.Texture;
import com.x.opengl.kernel.Transform;
import com.x.opengl.kernel.assetloader.CBResourcer;
import com.x.components.listener.OnAnimationListener;

@SuppressLint("NewApi")
public class View    {

	

	protected Drawable			mBGDrawable			= null;//view有很多个drawable，而这个最初始的基础的drawable可以当成view的代表，
	protected ArrayList<Drawable> mDrawableList		= null;
	
	protected float				mViewWidth				= 1;
	protected float				mViewHeight				= 1;
	protected float				mViewThickness			= 1;
	protected boolean 			mRequestInvalidate  = true;

	// private boolean mIsFocused = false;
	private OnClickListener		mOnClickListener;
	private OnStareAtListener		mOnStareAtListener;
	protected OnWidgetItemListener		mOnWidgetItemListener;
	private OnLongClickListener	mOnLongClickListener;
	private OnAnimationListener mOnAnimationListener;
	private onKeyListenner		mOnKeyListenner;
	private OnFocusListener 	mOnFocusListener;
	
	private boolean				mClickable			= true;
	protected boolean			mFocusable			= true;
	private boolean 			mTouchable 			= true;

	public boolean isEyeMatrixUpdate() {
		return mIsEyeMatrixUpdate;
	}

	public void setEyeMatrixUpdate(boolean isEyeMatrixUpdate) {
		this.mIsEyeMatrixUpdate = isEyeMatrixUpdate;
	}

	private boolean				mIsEyeMatrixUpdate	= false;

	protected boolean			mIsVisible			= true;

//	private T_Transform			mTransform			= new T_Transform();	// 相对位移缩放旋转等等
	private Transform			mDecodeTransform	= new Transform();
	protected Transform			mTransform			= new Transform();

	private float[] mGyroscopeMatrix = new float[16];
	
	private EventManager		mEventManager		= new EventManager();
	private Rotate				mFinalRoate = new Rotate();
	private Object				mParent;
	private Object				mInfo;
	private Object				mTag;
	protected boolean 			mIsViewGroup;
	protected boolean 			mIsScene;

	protected 	XScene	mRootScene;

	public static final int		STATE_FOCUSED		= 0x0001;
	public static final int		STATE_SELECTED		= 0x0002;
	public static final int		STATE_CHECKED		= 0X0004;

	protected int				mPrivateFlag    ;
	private String mDebugName = "View";
	public String getDebugName(){
		return mDebugName;
	}
	public void SetDebugName(String name) {
		mDebugName = name;
	}
	public View() {

		Matrix.setIdentityM(mGyroscopeMatrix, 0);

		initView();
		setFocused(false);
	}
	public View(Drawable drawable) {

		Matrix.setIdentityM(mGyroscopeMatrix, 0);

		mDrawableList = new ArrayList<Drawable>();
		mBGDrawable = drawable;
		addDrawable(mBGDrawable);
		mBGDrawable.scanMeshes();
	}

	public View(int width, int height) {

		Matrix.setIdentityM(mGyroscopeMatrix, 0);

		initView();
		mViewWidth = width;
		mViewHeight = height;
	}

	private void initView() {
		mDrawableList = new ArrayList<Drawable>();
		mBGDrawable = new Drawable(-1);
		addDrawable(mBGDrawable);
		mBGDrawable.scanMeshes();
	}

	public Drawable getBaseDrawable(){
		return mBGDrawable;
	}

	// 图片纹理在这里加入到View的纹理列表中
	protected void addDrawable(Drawable d) {
		d.setAttachView(this);
		mDrawableList.add(d);
	}

	/**
	 * 更新物体
	 */
	// important: luoyouren-20171209
	public void draw() {

		if (!mIsVisible) {
			return;
		}
//		Log.d("ming", "draw = "+this.getDebugName());
		{//
			Director.sGLESVersion.pushMatrix();
			
			onAnimation();//动画修改属性参数
			onViewTransform();//将修改属性传入opengl管线
			
			{//view自身以及子view的绘制
				

				StencilValueStatck.push();//模板测试值入栈  
				//只有参考值<>=!(蒙板缓存区的值&mask)时才通过  
				//重要的事情说三遍，不是蒙版值相对参考值，是参考值相对蒙版
				//重要的事情说三遍，不是蒙版值相对参考值，是参考值相对蒙版
				//重要的事情说三遍，不是蒙版值相对参考值，是参考值相对蒙版

					
				GLES20.glStencilFunc(GLES20.GL_GREATER, StencilValueStatck.getStackSize() , 0xff);
				GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_REPLACE);

				mBGDrawable.draw();

				//view实际绘制全部委派给drawable列表
				for (int i = 1; i < mDrawableList.size(); i++) {  
					Drawable drawable = mDrawableList.get(i);
					if(drawable.isRenderable()){
						GLES20.glStencilFunc(GLES20.GL_EQUAL, StencilValueStatck.getStackSize(), 0xff);
						GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_KEEP);

						// 遍历View的纹理列表，然后绘制
						drawable.draw( );
					}
				}
				
				
				//子view的绘制部分
				onViewGroupDraw();
				
				StencilValueStatck.pop();//模板测试值出栈
				
			}
			
			Director.sGLESVersion.popMatrix();
			
		}
		
		//处理焦点框
		if(!mIsScene){
			if(mRootScene == null){
				mRootScene = (XScene) findRootScene(this);//查找当前view所在的scene是哪一个scene
			}
			if ( mRootScene != null && mRootScene.needShowFocusView() && mRootScene.getFocusView().hasBinded(this)) {
				mFinalRoate = AttributeStatck.getFinalRotate();
				mRootScene.getFocusView().focusUpdateState(this,true);//操纵所在scene的飞框焦点的移动
			}
		} 
		
	}

	/**
	 * 因为添加顺序的原因，不一定当时就一定取得到rootScene是谁，因此可能出现这一桢没取到，而下一桢就取到了的情况
	 * @param object
	 * @return
	 */
	private Object findRootScene(View object) {
		Object object_d = object.getParent();
		if(object_d == null){
			return null;
		}
		if(object_d instanceof XScene){
			return object_d;
		}else{
			return findRootScene((View)object_d);
		}
	}

	protected void onViewGroupDraw() {

	}

	protected void onAnimation() {

		mTransform.run();
		
		if (mTransform.hasMoreAnimation() || mRequestInvalidate) {
			
			mTransform.normal(1, 1, 1, 1, 1, 1);
			mRequestInvalidate = false;
			dispatchAnimationRepeat();
			
		}else{
			dispatchAnimationEnd();
		}
	}

	// luoyouren: 我们所有的移动、旋转、缩放物体的操作，最终都通过这个函数传递到mModelMatrix，如下MatrixState.java：
//	public static void translate(float x,float y,float z)
//	{
//		Matrix.translateM(mModelMatrix, 0, x, y, z);
//	}
//
//	public static void rotate(float angle,float x,float y,float z)
//	{
//		Matrix.rotateM(mModelMatrix,0,angle,x,y,z);
//	}
//
//	public static void scale(float x,float y,float z)
//	{
//		Matrix.scaleM(mModelMatrix,0, x, y, z);
//	}
	protected void onViewTransform() {
		// 转换自身位移
		if (mIsEyeMatrixUpdate)
		{
			Director.sGLESVersion.onViewTransform(mTransform, mGyroscopeMatrix);
		} else {
			Director.sGLESVersion.onViewTransform(mTransform);
		}
		// 如果当前被当前焦点绑定，则转换焦点位移
	}

	// luoyouren: 让某些场景跟随视线移动
	public void updateEyeMatrixToScene(float[] tmpMatrix)
	{
		if (mIsEyeMatrixUpdate) {
			Log.i("luoyouren", "View: getDebugName = " + getDebugName());
//			Director.sGLESVersion.updateEyeMatrixToScene(tmpMatrix);
			for (int i = 0; i < 16; i++)
			{
				mGyroscopeMatrix[i] = tmpMatrix[i];
			}
		}
	}

	public CBResourcer getCBResourcer() {
		return Director.sResourcer;
	}

	public float getWidth() {
		return mViewWidth;
	}

	public void setCullFrontFace(boolean b) {
		mBGDrawable.setCullFrontFace(b);
	}

	public void setWidth(float f) {
		this.mViewWidth = f;
		mBGDrawable.setWidth(f);
		mRequestInvalidate = true;
		Director.getInstance().postInvalidate();
	}
	public void setHeight(float f) {
		this.mViewHeight = f;
		mBGDrawable.setHeight(f);
		mRequestInvalidate = true;
		Director.getInstance().postInvalidate();
	}
	public void
	setThickness(float f) {
		this.mViewThickness = f;
		mBGDrawable.setThickness(f);
		mRequestInvalidate = true;
		Director.getInstance().postInvalidate();
	}

	public float getHeight() {
		return mViewHeight;
	}


	public float getThickness() {
		return mViewThickness;
	}

	public void setBackgroundRenderable(boolean b) {
		this.mBGDrawable.setRenderable(b);
	}

	public void setBackgroundColor(int color) {
		Bitmap bitmap = Bitmap.createBitmap(1, 1, Config.ARGB_8888);
		bitmap.eraseColor(color);
		mBGDrawable.setTexture(bitmap);
	}
	public void setBackground(Bitmap bitmap) {
		mBGDrawable.setTexture(bitmap);
	}
	public void setBackground(Bitmap bitmap,boolean recycleLast) {
		mBGDrawable.setTexture(bitmap,recycleLast);
	}
	public void setBackground(Texture text) {
		mBGDrawable.setTexture(text);
	}
	public void setBackground(Texture text,boolean recycleLast) {
		mBGDrawable.setTexture(text,recycleLast);
	}
	public void setBackgroundResource(int resid) {
		mBGDrawable.setTexture(resid);
	}
	public void setBackgroundResource(int resid,boolean recycleLast) {
		mBGDrawable.setTexture(resid,recycleLast);
	}
	public Texture getBackground() {
		return mBGDrawable.getTexture();
	}



	public void setVisibility(boolean isVisible) {
		if (mIsVisible != isVisible) {
			mIsVisible = isVisible;
			postInvalidate();
		}
	}


	public void requestFocus() {
		
		if(mRootScene == null){
			mRootScene = (XScene) findRootScene(this);
		}
		if(mIsVisible){
			if(mRootScene != null){
				mRootScene.getFocusView().focusTo(this);//移动焦点框
			}else{
				Director.getInstance().requestAsyncFocusTask(this);
			}
		}
	}

	public void translateTo(float x, float y, float z) {
		mTransform.translateTo(x, y, z);
		dispatchAnimationStart();
	};
	public void translateTo(float x, float y, float z,int time ) {
		mTransform.translateTo(x, y, z,time);
		dispatchAnimationStart();
	};
	public void translateTo(float x, float y, float z,int time ,Interpolator i) {
		mTransform.translateTo(x, y, z,time,i);
		dispatchAnimationStart();
	};
	public void translateTo(float x, float y, float z,boolean xFlag,boolean  yFlag,boolean zFlag) {
		mTransform.translateTo(x, y, z,xFlag,yFlag,zFlag);
		dispatchAnimationStart();
	};
	public void translateZTo(float z) {
		mTransform.translateZTo(z);
		dispatchAnimationStart();
	};
	public void translateTo(float x, float y, float z,boolean xFlag,boolean yFlag,boolean zFlag,int time ) {
		mTransform.translateTo(x, y, z,xFlag,yFlag,zFlag,time);
		dispatchAnimationStart();
	};

	public void scaleTo(float x, float y, float z) {
		mTransform.scaleTo(x, y, z);
		dispatchAnimationStart();
	};

	public void scaleTo(float x, float y, float z,int time ) {
		mTransform.scaleTo(x, y, z,time);
		dispatchAnimationStart();
	};
	public void rotateTo(float x, float y, float z) {
		mTransform.rotateTo(x, y, z);
		dispatchAnimationStart();
	};

	public void rotateTo(float x, float y, float z,int time ) {
		mTransform.rotateTo(x, y, z,time);
		dispatchAnimationStart();
	};
	public void alphaTo(float alpha) {
		mTransform.alphaTo(alpha);
		dispatchAnimationStart();
	}
	public void alphaTo(float alpha,int time) {
		mTransform.alphaTo(alpha,time);
		dispatchAnimationStart();
	}
	
	public void setScale(float x, float y, float z) {
		mTransform.setScale(x, y, z);
	}
	public void setTranslate(float x, float y, float z) {
		mTransform.setTranslate(x, y, z);
	}
	public void setTranslate(float x, float y, float z,boolean xFlag,boolean yFlag ,boolean zFlag) {
		mTransform.setTranslate(x, y, z,xFlag,yFlag,zFlag);
	}
	public void translateTo(float x, float y, float z, boolean xFlag, boolean yFlag, Boolean zFlag) {
		mTransform.translateTo(x, y, z,xFlag,yFlag,zFlag);
	}
	public void setRotate(float x, float y, float z) {
		mTransform.setRotate(x, y, z);
	}

	public void setAlpha(float alpha) {
		mTransform.setAlpha(alpha);
	}
	
	public void backgroundAlphaTo(float alpha){
		mBGDrawable.alphaTo(alpha);
	}
	public void backgroundAlphaTo(float alpha,int time){
		mBGDrawable.alphaTo(alpha,time);
	}
	public void setBackgroundTranslate(float x, float y, int z) {
		mBGDrawable.setTranslate(x, y, z);
	}
	public void setBackgroundScale(float x, float y, int z) {
		mBGDrawable.setScale(x, y, z);
	}
	public void setBackgroundAlpha(float alpha) {
		mBGDrawable.setAlpha(alpha);
	}
	public void backgroundScaleTo(float x,float y,float z){
		mBGDrawable.scaleTo(x,y,z);
	}
	public void backgroundTranslateTo(float x,float y,float z){
		mBGDrawable.translateTo(x, y, z);
	}
	/**
	 * 刷新绘图帧
	 */
	public void postInvalidate() {

//		 MLog.d("View invalidate()");
		Director.getInstance().postInvalidate();
	}
	// ////////////////////////////////
	// GameObject Input Event Logic //
	// ////////////////////////////////
	/**
	 * @param event
	 * @return
	 */
	public boolean dispatchTouchEvent(MotionEvent event) {

		return onTouchEvent(event);
	}
	public boolean dispatchStarePointEvent(MotionEvent event) {

		return onStarePointEvent(event);
	}
	
	public boolean dispatchGenericMotionEvent(MotionEvent event) {
		return false;
	}

	/**
	 * @param event
	 * @return
	 */
	public boolean dispatchKeyEvent(KeyEvent event) {
		 boolean state = false;

		return onKeyEvent(event);
	}

	/**
	 * @hide 暂不使用
	 * @param event
	 * @return
	 */
	float[] innerHitPoint = new float[2];;
	public boolean onTouchEvent(MotionEvent event) {
		boolean state = false;;
		boolean isHit = false;

		if(!isFocusable() || !isTouchable()){
			return false;
		}

//		Log.d("ming","click getDebugName = "+getDebugName());
		isHit = mBGDrawable.isHit(event.getX(), event.getY(), innerHitPoint);

		// ///////构建Click,LongClick////////
			
		state = mEventManager.dealTouchEvent(isHit, event);

		if (state) {

			// ///////构建onTouchEvent/////////
//			MotionEvent normalizedEvent = MotionEvent.obtain(event);
//			normalizedEvent.setLocation(innerHitPoint[0], innerHitPoint[1]);
////			Log.d("tag", this + "  " + normalizedEvent.getAction() + "  normalizedEvent  = " + normalizedEvent.getX() + "," + normalizedEvent.getY());
//			normalizedEvent.recycle();
//			normalizedEvent = null;
		}	
		
		return state;
	}
	public boolean onStarePointEvent(MotionEvent event) {
		boolean state = false;;
		boolean isHit = false;

		if(!isFocusable() || !isTouchable()){
			return false;
		}

//		Log.d("ming","click getDebugName = "+getDebugName());
		isHit = mBGDrawable.isHit(event.getX(), event.getY(), innerHitPoint);

		// ///////构建Click,LongClick////////
			
		state = mEventManager.dealStarePointEvent(isHit, event);

		if (state) {

			// ///////构建onTouchEvent/////////
//			MotionEvent normalizedEvent = MotionEvent.obtain(event);
//			normalizedEvent.setLocation(innerHitPoint[0], innerHitPoint[1]);
////			Log.d("tag", this + "  " + normalizedEvent.getAction() + "  normalizedEvent  = " + normalizedEvent.getX() + "," + normalizedEvent.getY());
//			normalizedEvent.recycle();
//			normalizedEvent = null;
		}		
		return state;
	}
	/**
	 * @param event
	 * @return
	 */
	public boolean onKeyEvent(KeyEvent event) {
		boolean state = false;
		
		if(mOnKeyListenner != null){
			if(this.isFocused()){
				state = mOnKeyListenner.onKeyEvent(this,event);
				if(state){
					return state;
				}
			}
		}
		
		if (event.getAction() == KeyEvent.ACTION_DOWN) {

			switch (event.getKeyCode())
				{
				case KeyEvent.KEYCODE_DPAD_UP: {
				}
				break;
				case KeyEvent.KEYCODE_DPAD_DOWN: {
				}
				break;
				case KeyEvent.KEYCODE_DPAD_LEFT: {
				}
				break;
				case KeyEvent.KEYCODE_DPAD_RIGHT: {
				}
				break;
				case KeyEvent.KEYCODE_DPAD_CENTER:
				case KeyEvent.KEYCODE_ENTER: {

					if ( (mRootScene != null) && this.equals(mRootScene.getFocusView().getBindView()) && this.isFocused()) {
	
						this.requestFocus();
						this.requestClick();
					
						state = true;
					}
				}
				break;
				case KeyEvent.KEYCODE_BACK:
				case KeyEvent.KEYCODE_ESCAPE: {
				}
				break;
				}
		}
		return state;
	}
	
	private void requestStareAt() {
		if(mIsVisible && isClickable()){
			if(this.mOnStareAtListener!= null){
				this.mOnStareAtListener.onStareAt(this);
			}else{
				this.onStareAt();
			}		
			
			if(this.mOnWidgetItemListener != null){
				mOnWidgetItemListener.onStareAt(this);
			}
		}
	}
	private void requestClick() {
		if(mIsVisible && isClickable()){
			if(this.mOnClickListener!= null){
				this.mOnClickListener.onClick(this);
			}else{
				this.onClick();
			}		
			
			if(this.mOnWidgetItemListener != null){
				mOnWidgetItemListener.onClick(this);
			}
		}
	}
	public void requestLongClick() {

		if(mIsVisible && isClickable()){
			if (mOnLongClickListener != null) {
				mOnLongClickListener.onLongClick(View.this);
			} else {
				View.this.onLongClick();
			}

			if(this.mOnWidgetItemListener != null){
				mOnWidgetItemListener.onLongClick(this);
			}
		}
	}
	public boolean dispatchWheelEvent(float vscroll) {
		return false;
	}

	public boolean onWheelEvent(float vscroll) {
		return false;
	}

	/**
	 * 发送可达性事件
	 * 
	 * @param eventType
	 */
	public void sendAccessibilityEvent(int eventType) {
		dispatchAccessibilityEvent(AccessibilityEvent.obtain(eventType));
	}

	/**
	 * 发送可达性事件
	 * 
	 * @param event
	 */
	public void sendAccessibilityEvent(AccessibilityEvent event) {
		dispatchAccessibilityEvent(event);
	}

	/**
	 * 分发可达性事件
	 * 
	 * @param event
	 */
	public void dispatchAccessibilityEvent(AccessibilityEvent event) {
		// Do Nothing
	}

	public Mesh getMesh() {
		return mBGDrawable.mMesh;
	}

	public void setOnAnimationListenner(OnAnimationListener l) {
//		dispatchAnimationEnd();
		mOnAnimationListener = l;
		Director.getInstance().postDelayInvalidate(50);
//		postInvalidate();
	}

	public void setOnStareAtListener(OnStareAtListener l) {
		mOnStareAtListener = l;
	}
	public void setOnClickListener(OnClickListener l) {
		mOnClickListener = l;
	}
	public void setOnWidgetItemListener(OnWidgetItemListener l) {
		mOnWidgetItemListener = l;
	}

	public void setOnKeyListenner(onKeyListenner l) {
		mOnKeyListenner = l;
	}
	public void setOnFocusListener(OnFocusListener l) {
		mOnFocusListener = l;
	}

	public void setOnLongClickListener(OnLongClickListener l) {
		mOnLongClickListener = l;
	}

	/**
	 * Interface definition for a callback to be invoked when a view is clicked.
	 */
	public interface OnClickListener {
		/**
		 * Called when a view has been clicked.
		 * 
		 * @param v
		 *            The view that was clicked.
		 */
		void onClick(View v);
	}
	public interface  OnStareAtListener{
		void onStareAt(View v);
	}
	public interface OnWidgetItemListener {
		/**
		 * Called when a view has been clicked.
		 * 
		 * @param v
		 *            The view that was clicked.
		 */
		void onClick(View v);
		/**
		 * Called when a view has been focused.
		 * 
		 * @param v
		 *            The view that was focused.
		 */
		void onFocus(View v);
		/**
		 * Called when a view has been clicked.
		 * 
		 * @param v
		 *            The view that was clicked.
		 */
		void onLongClick(View v);
		/**
		 * Called when a view has been StareAt.
		 * 
		 * @param v
		 *            The view that was StareAt.
		 */
		void onStareAt(View v);
		
	}
	/**
	 * Interface definition for a callback to be invoked when a view has been
	 * clicked and held.
	 */
	public interface OnLongClickListener {
		/**
		 * Called when a view has been clicked and held.
		 * 
		 * @param v
		 *            The view that was clicked and held.
		 * 
		 * @return true if the callback consumed the long click, false
		 *         otherwise.
		 */
		boolean onLongClick(View v);
	}

	public interface onKeyListenner {

		boolean onKeyEvent(View v, KeyEvent event);

	}
	public interface OnFocusListener {
		/**
		 * Called when a view has been clicked and held.
		 * 
		 * @param v
		 *            The view that was clicked and held.
		 * 
		 * @return true if the callback consumed the long click, false
		 *         otherwise.
		 */
		boolean onFocus(View v);
		boolean onRemoveFocus(View v);
	}


	public class EventManager {

		private final long	TIME_INTERVAL	= 600;
		private float mDownEventX = 0 ;
		private float mDownEventY = 0 ;
		
		private float mMoveEventX = 0 ;
		private float mMoveEventY = 0 ;
		public final static int		DELAYED_LONG_CLICK				= 0x0007;
		public final static int		LONG_CLICK						= 0x0008;
		public final static int		CANCEL_LONG_CLICK				= 0x0009;
		private long downTime = 0 ;
		private long CLICK_TIME_RANGE = 1000 ;
		private boolean		isTouchValid	= false;	;
		private boolean havenLongClicked = false;//是否已经触发了长按事件
		
		
		public boolean dealTouchEvent(boolean isHit, MotionEvent event) {
			
			boolean state = false;
			if (event.getAction() == MotionEvent.ACTION_DOWN) {

				isTouchValid = true;
				state = true;
				if(!isHit){ //若移动过程中并未点击中，则取消
					isTouchValid = false;
					state = false;
				}
			    havenLongClicked = false;
				mDownEventX = event.getX();
				mDownEventY = event.getY();
				downTime = System.currentTimeMillis();
			}
			//Log.d("BUG", " this "+View.this + "action = "+event.getAction() +",,mInitTime = "+mInitTime);

			if (isTouchValid && event.getAction() == MotionEvent.ACTION_MOVE) {
				
				if(!isHit){ //若移动过程中并未点击中，则取消

					isTouchValid = false;
					state = false;
				}
				if(isTouchValid && !havenLongClicked && System.currentTimeMillis() -  downTime > CLICK_TIME_RANGE){
					performLongClick();
					havenLongClicked = true;
				}
			}

			if (isTouchValid
					&& 
					(
					event.getAction() == MotionEvent.ACTION_UP || 
					event.getAction() == MotionEvent.ACTION_HOVER_ENTER || 
					event.getAction() == MotionEvent.ACTION_HOVER_MOVE
					)
				) {
				
				if(!havenLongClicked && System.currentTimeMillis() -  downTime < CLICK_TIME_RANGE){
					performClick();
				}
				isTouchValid = false;
				state = true;
				havenLongClicked = false;
			}

			return state;
		}

		public boolean dealStarePointEvent(boolean isHit, MotionEvent event) {
			if(isHit){
				// 凝视点问题在这儿！
				performStareAt();
			}
			return isHit;
		}
		public void performStareAt() {

			View.this.requestFocus();
			View.this.requestStareAt();
//			isTouchValid = false;
		}
		public void performLongClick() {

			View.this.requestFocus();
			View.this.requestLongClick();
//			isTouchValid = false;
		}

		public void performClick() {

			View.this.requestFocus();
			View.this.requestClick();
//			isTouchValid = false;
		}

	}

	public boolean isTouchable() {
		return mTouchable;
	}
	public boolean isVisiable(){
		return mIsVisible;
	};

	public boolean isFocusable() {
		return mFocusable;
	}

	public boolean isFocused() {
		return (mPrivateFlag & STATE_FOCUSED) != 0;
	}

	public boolean isSelected() {
		return (mPrivateFlag & STATE_SELECTED) != 0;
	}

	public boolean isChecked() {
		return (mPrivateFlag & STATE_SELECTED) != 0;
	}

	public void setSelected(boolean isSelected) {
		if (isSelected) {
			mPrivateFlag |= STATE_SELECTED;
		} else {
			mPrivateFlag &= ~STATE_SELECTED;
		}
	}

	public void setChecked(boolean isChecked) {
		if (isChecked) {
			mPrivateFlag |= STATE_CHECKED;
		} else {
			mPrivateFlag &= ~STATE_CHECKED;
		}
	}

	public void setFocused(boolean isFocused) {
		if (isFocused && mFocusable) {
			mPrivateFlag |= STATE_FOCUSED;
		} else {
			mPrivateFlag &= ~STATE_FOCUSED;
		}
	}

	public void setFocusable(boolean isFocusable) {
		mFocusable = isFocusable;
	}


	public void setTouchAble(boolean b) {

		this.mTouchable = b;
	}

	public boolean isClickable() {
		return mClickable;
	}

	public void setClickable(boolean isClickable) {
		mClickable = isClickable;
	}

	/**
	 * 获取的是当前view的相对旋转，缩放，位移，透明度转换 
	 * @return
	 */
	public Transform getRelativeTransform() {
		return mTransform;
	}



	/*
	 * 获取绝对位移三元数
	 */
	public Position getFinalModelViewTranslate() {
		return mBGDrawable.getFinalModelViewTranslate();
	}

	/**
	 * 绝对缩放三元数
	 * @return
	 */
	public Scale getFinalModelViewScale() {
		return mBGDrawable.getFinalModelViewScale();
	}

	/*
	 * 获取当前view 的绝对模型视图转换矩阵，即参考世界坐标系而得来
	 */
	public float[] getFinalModelMatrix() {
		return mBGDrawable.getFinalModelMatrix();
	}

	/**
	 * 绝对旋转三元数
	 * 暂时不支持多层迭代旋转，即如果父布局也进行了旋转，则该方法无法判断绝对旋转一定正确了
	 * @return
	 */
	public Rotate getFinalModelViewRotate() {
		return mFinalRoate;
	}
	/**
	 * 
	 * @return 返回相对透明度，即view自身的透明度
	 */
	public float getFinalAlpha() {
		return mTransform.Alpha;
	}

	public void onFocus() {
	}

	public void onRemoveFocus() {
	}

	public void onLongClick() {
		
	}
	public void onClick() {
//		this.scaleTo(1.1f, 1.1f, 1f);
	}
	public void onStareAt() {
//		this.scaleTo(1.1f, 1.1f, 1f);
	}
	
	protected void onAnimationStart(){
//		postInvalidate();
	}
	protected void onAnimationRepeat(){
//		postInvalidate();
	}
	protected void onAnimationEnd(){
	}
	
	private boolean mAnimationStart = false;
	private boolean mAnimationRepeat = false;
	private boolean mAnimationEnd = true;
	private void dispatchAnimationStart(){

		
		if(mAnimationEnd){
			mAnimationEnd = false;
			mAnimationStart = true;
			
			boolean flag = false;
			if(mOnAnimationListener != null){
				mOnAnimationListener.onAnimationStart(this); //返回值后续补充
			}
			
			if(!flag){
				onAnimationStart();
			}
		} 
	}
	
	private void dispatchAnimationRepeat(){

		if(!mAnimationEnd && (mAnimationStart || mAnimationRepeat)){
			mAnimationRepeat = true;
			if(mOnAnimationListener != null){
				mOnAnimationListener.onAnimationRepeat(this);
			}
			onAnimationRepeat();
		}
		
	}
	private void dispatchAnimationEnd(){

		if(!mAnimationEnd && (mAnimationStart || mAnimationRepeat)){
			mAnimationEnd = true;
			mAnimationStart = false;
			mAnimationRepeat = false;
			if(mOnAnimationListener != null){
				mOnAnimationListener.onAnimationEnd(this);
			}
			onAnimationEnd();
		}
	}
	
	public float getDrawableCount() {
		return mDrawableList.size();
	}

	public Object getParent() {
		return mParent;
	}

	public void setParent(Object object) {
		this.mParent = object;
	}

	/**
	 * 客户程序使用
	 * @param viewData
	 */
	public void setTag(Object viewData) {
		this.mTag = viewData;
	}
	public Object getTag(){
		return mTag;
	}
	/**引擎内部使用
	 * @hide*/
	public void setInfo(Object object) {
		this.mInfo = object;
	}
	/**@hide*/
	public Object getInfo() {
		return this.mInfo;
	}

	public void onDispatchFocus(boolean flag) {

		setFocused(flag);
		if(flag){
			
			if(mOnFocusListener != null){
				mOnFocusListener.onFocus(this);
			}
			onFocus();

			if(this.mOnWidgetItemListener != null){
				mOnWidgetItemListener.onFocus(this);
			}
		}else{
			if(mOnFocusListener != null){
				mOnFocusListener.onRemoveFocus(this);
			}
			onRemoveFocus();
		}
	}

}
