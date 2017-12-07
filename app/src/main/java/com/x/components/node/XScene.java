package com.x.components.node;

import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.x.Director;
import com.x.opengl.kernel.AbstractFocusFinder;
import com.x.opengl.kernel.AxialFocusFinder;
import com.x.opengl.kernel.CommonAxialFocusFinder;
import com.x.opengl.kernel.EngineConstanst;
import com.x.opengl.kernel.MiniDistanceFocusFinder;

@SuppressLint("NewApi")
public abstract class XScene  extends ViewGroup{

	protected ViewGroup mFocusViewViewGroup  ;
	protected MoveBox	mFocusView;
	protected AbstractFocusFinder mFocusFinder  ;

	private boolean	mIsRenderable		= true;
	private Object	mRoot;
	private boolean mShowFocusViewFlag = false;
	private boolean	mFocusAsBottomLayer = false; // 焦点框是在底下还是顶上
	protected boolean				mStencilTestFlag   = false;//是否启用模版测试，该标志位主要用于防止viewgroup中的子view超出viewgroup的边界,默认为不启用;

	public void setStencilTestFlag(boolean b){
		this.mStencilTestFlag = b;
	}
	public boolean getStencilTestFlag(){
		return this.mStencilTestFlag ;
	}
	public void setRoot(Object root){
		this.mRoot = root;
	}
	public Object getRoot(){
		return mRoot;
	};
	public XScene() {
		super();
		SetDebugName("XScene");
		mIsScene = true;
		mFocusFinder = new MiniDistanceFocusFinder();
		
		Bitmap bitmap = Bitmap.createBitmap(1,1,Config.ARGB_8888);
		bitmap.eraseColor(Color.argb(100, 255,0, 0));
		mFocusView = new IntactBoxFocus();

//		mFocusView = new DismantlingBoxFocus();
		mFocusView.setBackground(bitmap);
		
		mFocusViewViewGroup = new ViewGroup();
		mFocusViewViewGroup.SetDebugName("FocusViewViewGroup");
		mFocusViewViewGroup.setWidth(EngineConstanst.REFERENCE_SCREEN_WIDTH);
		mFocusViewViewGroup.setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
		mFocusViewViewGroup.addChild(mFocusView);
		mFocusViewViewGroup.setParent(this);
		
//		getBackgroundView().SetDebugName("XScene BackView");
//		setWidth(EngineConstanst.REFERENCE_SCREEN_WIDTH);
//		setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
	}

	abstract public void initScene();
	public void setFocusSpeed(float speed ){
		mFocusView.setSpeed(speed);
	}
	public void setShowFocusView(boolean b){
		this.mShowFocusViewFlag = b;
	}
	public void changeFocusFindMethod(int method ){
		if(method == AbstractFocusFinder.AXIAL_METHOD){
			mFocusFinder = new AxialFocusFinder();
		}else if(method == AbstractFocusFinder.MINI_DISTANCE_METHOD){
			mFocusFinder = new MiniDistanceFocusFinder();
		}else{
			mFocusFinder = new CommonAxialFocusFinder();
		}
	}
	public MoveBox getFocusView() {
		return mFocusView;
	}
	public boolean needShowFocusView() {
		return mShowFocusViewFlag;
	}

//	public void update() {
//		
//		
////		Iterator<ViewGroup> itr = mChildLinkedList.iterator();
////		while (itr.hasNext()) {
////			itr.next().draw();
////		}
//		long time = System.currentTimeMillis();
//
//		mHolderViewGroup.draw();
//		if(mShowFocusViewFlag){
//			if(mHolderViewGroup.getStencilTestFlag()){
//				mFocusViewViewGroup.setStencilTestFlag(true);
//			}else{
//				mFocusViewViewGroup.setStencilTestFlag(false);
//			}
//			mFocusViewViewGroup.draw();
//		}
//
////		MLog.d( "WolfScene time = "+(System.currentTimeMillis() - time));
//
//	}
//	public void update() {
//		
//		
////		Iterator<ViewGroup> itr = mChildLinkedList.iterator();
////		while (itr.hasNext()) {
////			itr.next().draw();
////		}
////		long time = System.currentTimeMillis();
//
//		draw();
//		if(mShowFocusViewFlag){
////			if(mFocusAsBottomLayer){
////				mFocusViewViewGroup.draw();
////				if(getStencilTestFlag()){
////					mFocusViewViewGroup.setStencilTestFlag(true);
////				}else{
////					mFocusViewViewGroup.setStencilTestFlag(false);
////				}
////			}else{
////
////			}
//			if(getStencilTestFlag()){
//				mFocusViewViewGroup.setStencilTestFlag(true);
//			}else{
//				mFocusViewViewGroup.setStencilTestFlag(false);
//			}
//			mFocusViewViewGroup.draw();
//		}
//
////		MLog.d( "WolfScene time = "+(System.currentTimeMillis() - time));
//
//	}
	public void update() {
		
		
//		Iterator<ViewGroup> itr = mChildLinkedList.iterator();
//		while (itr.hasNext()) {
//			itr.next().draw();
//		}
//		long time = System.currentTimeMillis();
	
		
		if(mFocusAsBottomLayer){
			if(mShowFocusViewFlag){
				mFocusViewViewGroup.draw();
			}	
			if(mStencilTestFlag){
				Director.getInstance().sGLESVersion.openStencilTest();
			}
			draw();
			if(mStencilTestFlag){
				Director.getInstance().sGLESVersion.closeStencilTest();
			}
		}else{
			if(mStencilTestFlag){
				Director.getInstance().sGLESVersion.openStencilTest();
			}
			draw();
			if(mStencilTestFlag){
				Director.getInstance().sGLESVersion.closeStencilTest();
			}
			if(mShowFocusViewFlag){
				mFocusViewViewGroup.draw();
			}
		}
//		MLog.d( "WolfScene time = "+(System.currentTimeMillis() - time));
	}
	public void setFocusAsBottomLayer(boolean botoomLayer){
		this.mFocusAsBottomLayer  = botoomLayer;
	}
	public void setRenderable(boolean renderable) {
		mIsRenderable = renderable;
	}

	public boolean isRenderable() {
		return mIsRenderable;
	}

	/**
	 * 添加子元素
	 * 
	 * @param viewGroup
	 * @return
	 */
	public boolean addLayer(ViewGroup viewGroup) {
		
		if(viewGroup.getParent() != null){
			throw new RuntimeException("You can not add viewGroup that has been added to  other Scene");
		}
		return super.addChild(viewGroup);
	}
	/**
	 * 移除子元素
	 * 
	 * @param viewGroup
	 */
	public boolean removeLayer(ViewGroup viewGroup) {
		return super.removeChild(viewGroup);
	}

//	@Override
//	public boolean addChild(int location, View view) {
//		throw new RuntimeException("This method is not recommanded,please use the method addLayer() instead");
//	}
//	@Override
//	public boolean addChild(View view) {
//		throw new RuntimeException("This method is not recommanded,please use the method addLayer() instead");
//	}
//	
//	@Override
//	public View removeChild(int index) {
//		throw new RuntimeException("This method is not recommanded,please use the method removeLayer() instead");
//	}
//	@Override
//	public boolean removeChild(View view) {
//		throw new RuntimeException("This method is not recommanded,please use the method removeLayer() instead");
//	}

	/**
	 * 获取元素的位置
	 * 
	 * @param viewGroup
	 * @return
	 */
	public int indexOf(ViewGroup viewGroup) {
		return indexOf(viewGroup);
	}


	/**
	 * 子元素数量
	 * 
	 * @return
	 */
	public int size() {
		return getChildCount();
	}

	/**
	 * @hide 暂不使用，后延至Wolf 2.0
	 * @param event
	 * @return
	 */
//	public boolean dispatchTouchEvent(MotionEvent event) {
//		boolean state = false;
//
//		// 遍历dispatch
////		Iterator<ViewGroup> itr = mChildLinkedList.iterator();
//
//		LinkedList<View> linkedList = mHolderViewGroup.getChildList();
//		for (int i = linkedList.size() - 1; i >= 0; i--) {//因为后添加进的对象反而绘制在上层，所以在同级别中时间进行反向遍历
//			ViewGroup viewGroup = (ViewGroup) linkedList.get(i);
//			if (viewGroup.isVisiable()) {
//				state |= viewGroup.dispatchTouchEvent(event);
//				if (state) {
//					// 如果处理，有child自行将事件交给onTouchEvent
//					break;
//				}
//			}
//		}
//
//		// 没有可以处理事件的子元素
//		// 由Renderer处理
//		if (!state) {
//			state |= this.onTouchEvent(event);
//		}
//
//		return state;
//	}

//	public boolean dispatchGenericMotionEvent(MotionEvent event) {
//		boolean state = false;
//
//		// 遍历dispatch
////		Iterator<ViewGroup> itr = mChildLinkedList.iterator();
//		Iterator<View> itr = mHolderViewGroup.getChildList().iterator();
//		while (itr.hasNext()) {
//			ViewGroup viewGroup = (ViewGroup) itr.next();
//			if (viewGroup.isVisiable()) {
//				state |= viewGroup.dispatchGenericMotionEvent(event);
//				if (state) {
//					// 如果处理，有child自行将事件交给onTouchEvent
//					break;
//				}
//			}
//		}
//
//		// 没有可以处理事件的子元素
//		// 由Renderer处理
//		if (!state) {
//			state |= this.onGenericMotionEvent(event);
//		}
//
//		return state;
//	}
	public boolean onGenericMotionEvent(MotionEvent event) {
		return false;
	}

	/**
	 * @hide 暂不使用，后延至Wolf 2.0
	 * @param event
	 * @return
	 */
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	/**
	 * @hide 暂不使用，后延至Wolf 2.0
	 * @param event
	 * @return
	 */
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		boolean state = false;
//
////		Log.d("temp", "WolfScene dispatchKeyEvent");
//		// 遍历dispatch
////		for (int i = 0; i < mChildLinkedList.size(); i++) {
////			ViewGroup viewGroup = mChildLinkedList.get(i);
//		for (int i = 0; i < mHolderViewGroup.getChildCount(); i++) {
//			ViewGroup viewGroup = (ViewGroup) mHolderViewGroup.get(i);
//			if (viewGroup.isVisiable()) {
//
//				state |= viewGroup.dispatchKeyEvent(event);
//				if (state) {
//					// 如果处理，有child自行将事件交给onKeyEvent
//					break;
//				}
//			}
//		}
//
//
//		// 没有可以处理事件的子元素
//		// 由Renderer处理
//		if (!state) {
//			state |= this.onKeyEvent(event);
//		}
//
//		return state;
//	}

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

				if(downFlag){

					 if(mFocusView.getBindView() == null){
						 mFocusFinder.findNextFocus(AbstractFocusFinder.UP, this);
					 }
				}
				state = true;
			 }
			 break;
			 case KeyEvent.KEYCODE_BACK:
			 case KeyEvent.KEYCODE_ESCAPE: {
				if(downFlag){
						
//					if(mFocusView.getBindView() != null){
//						mFocusView.removeBindView();
//					}
				}
//				state = true;
			 }
			 
			 break;
			}
		return state;
	}

	/**
	 * @hide 暂不使用，后延至Wolf 2.0
	 * @param vscroll
	 * @return
	 */
//	public boolean dispatchWheelEvent(float vscroll) {
//		boolean state = false;
//
//		// 遍历dispatch
////		Iterator<ViewGroup> itr = mChildLinkedList.iterator();
//		LinkedList<View> linkedList = mHolderViewGroup.getChildList();
//		for (int i = linkedList.size() - 1; i >= 0; i--) {//因为后添加进的对象反而绘制在上层，所以在同级别中时间进行反向遍历
//			ViewGroup viewGroup = (ViewGroup) linkedList.get(i);
//			if (viewGroup.isVisiable()) {
//				state |= viewGroup.dispatchWheelEvent(vscroll);
//				if (state) {
//					// 如果处理，有child自行将事件交给onTouchEvent
//					break;
//				}
//			}
//		}
//		
//		// 没有可以处理事件的子元素
//		// 由Renderer处理
//		if (!state) {
//			state |= this.onWheelEvent(vscroll);
//		}
//
//		return state;
//	}

	/**
	 * @hide 暂不使用，后延至Wolf 2.0
	 * @param vscroll
	 * @return
	 */
	public boolean onWheelEvent(float vscroll) {
		return false;
	}

	public int getLayerSize() {
		return getChildCount();
	}
	public ViewGroup getLayerAt(int index) {
		return (ViewGroup) get(index);
	}
	public LinkedList<View> getLayerList() {
		return getChildList() ;
	}



}
