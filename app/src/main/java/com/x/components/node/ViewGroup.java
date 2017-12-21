package com.x.components.node;

import java.util.Iterator;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;

@SuppressLint("NewApi") public class ViewGroup extends View {

	protected LinkedList<View>	mViewList			= new LinkedList<View>();
//	private View				mBackView			= new View();				;;//将ViewGroup的背景单独独立出来成一个View是因为，如果不这样做，当我调整viewgrou背景大小时就会影响子view的大小，而独立出来之后，就不存在这种相关性了
	
	
	public ViewGroup() {
		super();
		
		SetDebugName("ViewGroup");
		mIsViewGroup = true;
		Bitmap bitmap = Bitmap.createBitmap(1, 1, Config.ARGB_8888);
		bitmap.eraseColor(Color.argb(1, 255, 255, 255));//该行代码在gles2.0可直接注释，但gles1.1如果注释或者透明度调的太低就会看不到东西
		
//		mBackView.setBackground(bitmap);//给mBackGroundImageView预先设置背景图是为了避免用户在没有设置背景图的情况下因为启用了蒙板导致图像看不见
//		mBackView.setParent(this);
//		mBackView.SetDebugName("ViewGroup BackView");
//		mBackView.setFocusable(false);
		
	}


//	/**
//	 * viewGroup的背景和view的背景不是同一个Drawable对象，viewGroup操作的是mBackView,所以必须重新实现该方法
//	 */
//	@Override
//	public void setBackground(Bitmap bitmap) {
//		mBackView.setBackground(bitmap);
//		
//		Director.getInstance().postInvalidate();
//	}
//
//	public void setBackgroundColor(int color) 
//	{
//		Bitmap bitmap = Bitmap.createBitmap(1, 1, Config.ARGB_8888);
//		bitmap.eraseColor(color);//该行代码在gles2.0可直接注释，但gles1.1如果注释或者透明度调的太低就会看不到东西
//		mBackView.setBackground(bitmap);//给mBackGroundImageView预先设置背景图是为了避免用户在没有设置背景图的情况下因为启用了蒙板导致图像看不见
//		Director.getInstance().postInvalidate();
//	}
//
//	public void setBackgroundAlpha(float alpha) {
//		mBackView.setAlpha(alpha);
//	}
//	public void backgroundAlphaTo(float alpha) {
//		mBackView.alphaTo(alpha);
//	}
//	/**
//	 * viewGroup的背景和view的背景不是同一个Drawable对象，viewGroup操作的是mBackView,所以必须重新实现该方法
//	 */
//	@Override
//	public void setBackground(Texture text) {
//		mBackView.setBackground(text);
//		Director.getInstance().postInvalidate();
//	}
// 
//
//	/**
//	 * viewGroup的背景和view的背景不是同一个Drawable对象，viewGroup操作的是mBackView,所以必须重新实现该方法
//	 */
//	@Override
//	public void setBackground(Bitmap bitmap,boolean recycleLast) {
//		mBackView.setBackground(bitmap,recycleLast);
//		Director.getInstance().postInvalidate();
//	}
//	/**
//	 * viewGroup的背景和view的背景不是同一个Drawable对象，viewGroup操作的是mBackView,所以必须重新实现该方法
//	 */
//	@Override
//	public void setBackground(Texture text,boolean recycleLast) {
//		mBackView.setBackground(text,recycleLast);
//		Director.getInstance().postInvalidate();
//	}
//	/**
//	 * viewGroup的背景和view的背景不是同一个Drawable对象，viewGroup操作的是mBackView,所以必须重新实现该方法
//	 */
//	public Texture getBackground(){
//		return mBackView.getBackground();
//	};
//	public View  getBackgroundView(){
//		return mBackView;
//	}
//	@Override
//	public void setBackgroundResource(int resid) {
//		mBackView.setBackgroundResource(resid);
//	}
//	@Override
//	public void setWidth(float f) {
//		super.setWidth(f);
//		mBackView.setWidth(f);;
//		
//	}
//	@Override
//	public void setHeight(float f) {
//		super.setHeight(f);
//		mBackView.setHeight(f);;
//	}
	@Override
	protected   void  onViewGroupDraw() {
		super.onViewGroupDraw();

			
//		Object parent = getParent();
//		 if(parent == null && !(this instanceof XScene)){
//			 
//			throw new RuntimeException(
//					"English:In enabling template test cases, requires all viewGroup must have a parent layout, at the same time, the top level WolfScene must be set SceneManager  as the root layout, "
//					+ "so as to ensure the viewGroup template recursion is completed testing, or they will be show nothing "
//					+ "\n chinese:在启用模版测试的情形下，要求所有的viewGroup必须有一个父布局，同时最顶层的XScene (其实也是viewGroup)必须设置SceneManager为根布局，这样才能保证viewGroup模版测试的递归顺利完成，或者将不会有任何显示");
//		 }

		
		/*
		 * 初始模板值
		 * 00000000000000000000000000
		 * 00000000000000000000000000
		 * 00000000000000000000000000
		 * 00000000000000000000000000
		 * 
		 * 同一层级 :
		 * 第一个 绘制背景等于0才绘制，更新模板值为1 
		 * child 等于1才绘制 更新模板值为2
		 * 
		 * 第二个同上绘制
		 * 
		 * 
		 * 父子层级:
		 * 第一级 绘制背景等于0才绘制，更新模板值为1 
		 * child 等于1才绘制 更新模板值为2
		 * 
		 * 第二级 绘制背景等于2才绘制，更新模板值为3 
		 * child 等于3才绘制 更新模板值为4
		 * 
		 * 这里会有一个bug
		 * 比如说同级的情况下,用下图说明
		 * 000000000000000000000000000000
		 * 001111111000000111111100000000
		 * 001122211000000112221100000000
		 * 001111111000000111111100000000
		 * 000000000000000000000000000000
		 * 上图如果是正常情形是没有问题，第二个child中的child，即右边部分模板值为222的那个，这个child满足1的模板位是正常绘制的
		 * 
		 * 但如果这个222过长，则有可能会绘制在左边的child之上，因为左边部分也存在模板值为1的缓冲区。
		 * 如下图所示，第一个child里面的右边有两个22，是由右边区域的222过长所以伸展过来的
		 * 000000000000000000000000000000
		 * 0011111111111000000111111100000000
		 * 0011222111122000000222221100000000
		 * 0011111111111000000111111100000000
		 * 000000000000000000000000000000
		 * 
		 */

		onChildDraw();

	}

	protected void onChildDraw() {

		View focusView = null;
		for (int i = 0; i < mViewList.size(); i++) {
			View view = mViewList.get(i);
			if(!view.isFocused()){
				view.draw();
			}else{
				focusView = view;
			}
		}
		if(focusView != null){
			focusView.draw();
		}
		
//		Iterator<View> itr = mViewList.iterator();
//		while (itr.hasNext()) {
//			View item = itr.next();
//			item.draw();
//		}
	}

	/**
	 * 添加子元素
	 * 
	 * @param view
	 * @return
	 */
	public boolean addChild(View view) {
		
		
		if(view.getParent() != null){
			throw new RuntimeException("You can not add view that has been added to  other parent");
		}
		
		boolean flag = (view.getParent() == null) && mViewList.add(view);
		if (flag) {
			view.setParent(this);
			postInvalidate();
		}
		return flag;
	}
	/**
	 * 添加子元素
	 * 
	 * @param view
	 * @return
	 */
	public boolean addChild(int location,View view) {
		
		
		if(view.getParent() != null){
			throw new RuntimeException("You can not add view that has been added to  other parent");
		}
		boolean flag = (view.getParent() == null) ;
		if (flag) {
			view.setParent(this);
			mViewList.add(location,view);
			postInvalidate();
		}
		
		return flag;
	}
	/**
	 * 移除子元素
	 * 
	 * @param view
	 */
	public boolean removeChild(View view) {
		boolean flag = mViewList.remove(view);
		if (flag) {
			view.setParent(null);
			postInvalidate();
		}
		return flag;
	}

	/**
	 * 移除子元素
	 * 
	 * @param index
	 * @return
	 */
	public View removeChild(int index) {
		View view = mViewList.remove(index);
		view.setParent(null);
		postInvalidate();
		return view;
	}

	/**
	 * 移除所有元素
	 */
	public void removeAll() {
		for (int i = 0; i < mViewList.size(); i++) {
			mViewList.get(i).setParent(null);
		}
		mViewList.clear();
		postInvalidate();
	}

	/**
	 * 获取元素的位置
	 * 
	 * @param view
	 * @return
	 */
	public int indexOf(View view) {
		return mViewList.indexOf(view);
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	public View get(int index) {
		if(index >= mViewList.size()){
			return null;
		}
		return mViewList.get(index);
	}

	/**
	 * 子元素数量
	 * 
	 * @return
	 */
	public int getChildCount() {
		return mViewList.size();
	}
	
	/**
	 * 子元素列表
	 * 
	 * @return
	 */
	public LinkedList<View> getChildList() {
		return mViewList ;
	}


	public boolean contain(View View) {
		return mViewList.contains(View);
	}

	// luoyouren: 让某些场景跟随视线移动
	public void updateEyeMatrixToScene(float[] tmpMatrix)
	{
		for (int i = 0; i < mViewList.size(); i++)
		{
			View view = mViewList.get(i);
			if (view.isEyeMatrixUpdate())
			{

			}
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		boolean state = false;
	
		
//		Log.d("temp", "ViewGroup dispatchTouchEvent");
		if(onInterceptTouchevent()){
			return true;
		}
			// 遍历dispatch
		LinkedList<View> linkedList = mViewList ;
		for (int i = linkedList.size() - 1; i >= 0; i--) {//因为后添加进的对象反而绘制在上层，所以在同级别中时间进行反向遍历
			View view =  linkedList.get(i);
			
			if (view.isVisiable()) {	 
				

				state |= view.dispatchTouchEvent(event);
				// Log.d("BUG"," return state = "+state +"     ]" );
					
				if (state) {
				//	view.onTouchEvent(event);//子类在dispatch中会自行交由OnTouchEvent，这里不用管。
					// 如果处理，有child自行将事件交给onTouchEvent
					break;
				}
			}
		}
			
		// 没有可以处理事件的子元素
		// 由Layout处理
		if (!state) {
			state |= this.onTouchEvent(event);
		}

		return state;
		
	}
	@Override
	public boolean dispatchStarePointEvent(MotionEvent event) {

		boolean state = false;
	
		
//		Log.d("temp", "ViewGroup dispatchTouchEvent");
		if(onInterceptStarePointevent()){
			return true;
		}
			// 遍历dispatch
		LinkedList<View> linkedList = mViewList ;
		for (int i = linkedList.size() - 1; i >= 0; i--) {//因为后添加进的对象反而绘制在上层，所以在同级别中时间进行反向遍历
			View view =  linkedList.get(i);
			
			if (view.isVisiable()) {	 
				

				state |= view.dispatchStarePointEvent(event);
				// Log.d("BUG"," return state = "+state +"     ]" );
					
				if (state) {
				//	view.onTouchEvent(event);//子类在dispatch中会自行交由OnTouchEvent，这里不用管。
					// 如果处理，有child自行将事件交给onTouchEvent
					break;
				}
			}
		}
			
		// 没有可以处理事件的子元素
		// 由Layout处理
		if (!state) {
			state |= this.onStarePointEvent(event);
		}

		return state;
		
	}

	public boolean onInterceptTouchevent(){
		
		return false;
	}
	public boolean onInterceptStarePointevent(){
		
		return false;
	}
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent event) {
//		boolean state = false;
//
//		
////		Log.d("temp", "ViewGroup dispatchTouchEvent");
//		// 遍历dispatch
//		LinkedList<View> linkedList = mViewList ;
//		for (int i = linkedList.size() - 1; i >= 0; i--) {//因为后添加进的对象反而绘制在上层，所以在同级别中时间进行反向遍历
//			View view =  linkedList.get(i);
//			if (view.isVisiable()) {
//				state |= view.dispatchTouchEvent(event);
//				if (state) {
//					// 如果处理，有child自行将事件交给onTouchEvent
//					break;
//				}
//			}
//		}
//		
//		// 没有可以处理事件的子元素
//		// 由Layout处理
//		if (!state) {
//			state |= this.onTouchEvent(event);
//		}
//
//		return state;
//	}

	public boolean dispatchGenericMotionEvent(MotionEvent event) {	boolean state = false;

		// 遍历dispatch
		Iterator<View> itr = mViewList.iterator();
		while (itr.hasNext()) {
			View view = itr.next();
			if (view.isVisiable()) {
				state |= view.dispatchGenericMotionEvent(event);
				if (state) {
					// 如果处理，有child自行将事件交给onTouchEvent
					break;
				}
			}
		}
	
		// 没有可以处理事件的子元素
		// 由Layout处理
		if (!state) {
			state |= this.onGenericMotionEvent(event);
		}
	
		return state;
	}
	public boolean onGenericMotionEvent(MotionEvent event) {
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		Log.d("temp", "ViewGroup onTouchEvent");
		return super.onTouchEvent(event);
	}


	public boolean onStarePointEvent(MotionEvent event){

		return super.onStarePointEvent(event);
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		boolean state = false;

//		Log.d("temp", "ViewGroup dispatchKeyEvent");
		// 遍历dispatch
		Iterator<View> itr = mViewList.iterator();
		while (itr.hasNext()) {
			View view = itr.next();
			if (view.isVisiable()) {
				state |= view.dispatchKeyEvent(event);
				if (state) {
					// 如果处理，有child自行将事件交给onKeyEvent
					break;
				}
			}
		}

		// 没有可以处理事件的子元素
		// 由Layout处理
		if (!state) {
			state |= this.onKeyEvent(event);
		}

		return state;
	}

	

	@Override
	public boolean dispatchWheelEvent(float vscroll) {
		boolean state = false;

		// 遍历dispatch
		Iterator<View> itr = mViewList.iterator();
		while (itr.hasNext()) {
			View view = itr.next();
			if (view.isVisiable()) {
				state |= view.dispatchWheelEvent(vscroll);
				if (state) {
					// 如果处理，有child自行将事件交给onKeyEvent
					break;
				}
			}
		}

		// 没有可以处理事件的子元素
		// 由Renderer处理
		if (!state) {
			state |= this.onWheelEvent(vscroll);
		}

		return state;
	}

	@Override
	public boolean onWheelEvent(float vscroll) {
		return super.onWheelEvent(vscroll);
	}

	public class TranslateInfo {

		public int	mTranslateX;
		public int	mTranslateY;
		public int	mTranslateZ;
		public int	mPositionIndex;
		public int	mShowIndex;

		public TranslateInfo(int x, int y,int z, int addIndex,int realIndex) {
			this.mTranslateX = x;
			this.mTranslateY = y;
			this.mTranslateZ = z;
			this.mPositionIndex = addIndex;
			this.mShowIndex = realIndex;
		}

	}
	
	public class TwoDTranslateInfo{

		public float	mTranslateX;
		public float	mTranslateY;
		public int		mShowIndex;
		public int		mIndexX;
		public int		mIndexY;
		
		public TwoDTranslateInfo(float x,float y, int indexX,int indexY ,int cardIndex) {
			
			this.mTranslateX = x;
			this.mTranslateY = y;
			this.mShowIndex = cardIndex;
			this.mIndexX = indexX;
			this.mIndexY = indexY;
		}
		
	}

}
