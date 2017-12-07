package com.x.components.widget;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;
import android.view.MotionEvent;

import com.x.opengl.kernel.AnimationHolder;
import com.x.opengl.kernel.EngineConstanst;
import com.x.components.node.View;

/**
 * 
 * 
 * 
 */
public abstract class NoLoopAbstractListView extends AdapterView<Adapter> {

	protected int						mSpace				= 175;
	protected AnimationHolder			mAnimationHolder;
	protected int						mCardCount			= 17;							// 必须是单数,才能保证以中间数为分界的两边数目相等
	protected int						mCardIndex			= 0;
	protected int						mHalfCount			= mCardCount / 2;
	protected int						mShowHalfRow		= 3;

	protected Adapter					mAdapter;
	protected HashMap<Integer, View>	mFocusHashMap		= new HashMap<Integer, View>();
	protected int						mFocusIndex			= 0;
	protected long						mTime;

	protected ArrayList<View>			mAnimationList		= new ArrayList<View>();

	protected boolean					mLazy				= false;
	protected int						mRemoveFocusIndex;

	protected abstract View getView(int index);

	protected abstract void setChildTranslate(View view, TranslateInfo info, float offset);
	protected abstract void onDrag(float mTouchOffsetX2, float mTouchOffsetY2) ;

	public NoLoopAbstractListView() {
		super();
	}

//	@Override
//	public boolean onKeyEvent(KeyEvent event) {
//
////		Log.d("temp", "LoopListView onKeyEvent " + event.getAction());
//		if (getChildCount() == 0) {
//			return super.onKeyEvent(event);
//		}
//		boolean down_flag = false;
//		if (event.getAction() == KeyEvent.ACTION_DOWN) {
//			down_flag = true;
//		}
//
//		switch (event.getKeyCode())
//			{
//
//			case KeyEvent.KEYCODE_DPAD_DOWN:
//
//				if (down_flag) {
//
//					if (noSelectAnyone()) {
////						setSelectItem(0);
//					} else {
//						goAdd(+1,false);
//						return true;
//					}
//
//				}
//			break;
//			case KeyEvent.KEYCODE_DPAD_UP:
//
//				if (down_flag) {
//
//					if (noSelectAnyone()) {
//
////						return super.onKeyEvent(event);
//					} else {
//						goSub(-1,false);
//						return true;
//					}
//
//				}
//
//			break;
//			default:
//
//			break;
//			}
//
//		return super.onKeyEvent(event);
//	}

	@Override
	protected void onItemSelectScrollTo(View v) {
		
		TranslateInfo translateInfo = (TranslateInfo) v.getInfo();
		setSelectItem(translateInfo.mShowIndex);;

	}
	private boolean timeLimit(float limitTime) {

		if (System.currentTimeMillis() - mTime < limitTime) {
			return true;
		}
		mTime = System.currentTimeMillis();
		return false;
	}

	@Override
	public boolean onWheelEvent(float vscroll) {
		if (timeLimit(100)) {
			return true;
		}

		if (noSelectAnyone()) {
//			setSelectItem(0);
		} else {

			if (vscroll > 0) {

				if (vscroll > 1) {
					vscroll = 1;
				}
				for (int i = 0; i < vscroll; i++) {
					goSub(-1,false);
				}

				return true;

			} else if (vscroll < 0) {

				if (vscroll < -1) {
					vscroll = -1;
				}
				for (int i = 0; i < -vscroll; i++) {
					goAdd(1,false);
				}
				return true;

			}

		}

		return super.onWheelEvent(vscroll);
	}

	protected static int	MOVE = 0;
	protected static int	UP = 2;
	protected static int  DOWN =  1;
	protected int	mLastTouchState = UP;
	protected float	mTouchOffsetX;
	protected float	mTouchOffsetY;
	protected float	mTouchX;
	protected float	mTouchY;
	protected int	mAllTouchOffsetX;
	protected int	mAllTouchOffsetY;
	private boolean	mIntercept;
	public static  float mLeftRightSpeed = 0.45f;
	private boolean	mTouchAvaliable;

	private float[]	mHitIn = new float[]{0,0};
	public boolean isInTouchBox(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN ){
			if(mBGDrawable.isHit(event.getX(), event.getY(), mHitIn)){
				mTouchAvaliable = false;
			}else{
				mTouchAvaliable = true;
			}
			String ss = this.toString();
			ss = ss.substring(ss.length()-6,ss.length() - 1);
			Log.d("LOG", ss+"  dispatchTouchEvent = "+event.getAction() + ",,mTouchAvaliable = "+mTouchAvaliable);
		
		}else if(event.getAction() == MotionEvent.ACTION_MOVE){
			
		}else {
//			String ss = this.toString();
//			ss = ss.substring(ss.length()-6,ss.length() - 1);
//			Log.d("LOG", ss+"  dispatchTouchEvent = "+event.getAction() + ",,mTouchAvaliable = "+mTouchAvaliable);
	//	
			mTouchAvaliable = true;
		}
		return true;
	}

//	@Override
//	public boolean dispatchTouchEvent(MotionEvent event) {
//
//	
////		String ss = this.toString();
////		ss = ss.substring(ss.length()-6,ss.length() - 1);
////		Log.d("LOG", ss+"  dispatchTouchEvent = "+event.getAction() + ",,mTouchAvaliable = "+mTouchAvaliable);
////	
//		
//		if(event.getAction() == MotionEvent.ACTION_DOWN ){
//
////			boolean isHit = mAabbBox.isHit(event.getX(), event.getY(), mHitIn);
////			
//			mLastTouchState = DOWN;
//			this.mTouchOffsetX = 0;
//			this.mTouchOffsetY = 0;
//			this.mTouchX = event.getX();
//			this.mTouchY = event.getY();
//			this.mAllTouchOffsetX = 0;
//			this.mAllTouchOffsetY = 0;
//			this.mIntercept = false;
//
//			if(mAnimationHolder.getState() == AnimationHolder.DRAG_STOP || mAnimationHolder.getState() == AnimationHolder.DRAG_STOP_ATTRACT){
//				mIntercept = true;
//			}
//			
//		}else if(event.getAction() == MotionEvent.ACTION_MOVE){
//
//			
//
//			float offsetX = event.getX() - this.mTouchX;
//			float offsetY = event.getY() - this.mTouchY;
//			
//			if(mTouchOffsetX * offsetX < 0){
//				mAnimationHolder.dragReset();
//			}
//			if(mTouchOffsetY * offsetY < 0){
//				mAnimationHolder.dragReset();
//			}
//
//			this.mTouchOffsetX = offsetX;
//			this.mTouchOffsetY = offsetY;
//			
//			this.mAllTouchOffsetX += this.mTouchOffsetX;
//			this.mAllTouchOffsetY += this.mTouchOffsetY;
//			
//			float distan = Math.abs(mAllTouchOffsetX);
//			if( distan   > EngineConstanst.REFERENCE_LIST_MOVE_TINGLE){//若在手指抖动误差值范围内则仍然生效 
//				mIntercept = true;
//			}
//
//			onDrag(mTouchOffsetX,mTouchOffsetY);
//			postInvalidate();
////			if(Math.abs(mTouchOffsetX * 2) > EngineConstanst.REFERENCE_MOVE_CAN_ADD){
////				mAnimationHolder.drag(mTouchOffsetX * 2);
////				postInvalidate();
////			}else{
////				mAnimationHolder.drag(1);
////			}
//			
//			this.mTouchX = event.getX();
//			this.mTouchY = event.getY();
//
////			if(mLastTouchState == DOWN){
//				if(mTouchOffsetX > 0){
////					changeRight();
//				}else if(mTouchOffsetX < 0){
////					changeLeft();
//				}
////			}
//			mLastTouchState = MOVE;
//		
////			Log.d("drag", "mTouchOffsetX = " + mTouchOffsetX);
//		}else {
//			this.mTouchX = event.getX();
//			this.mTouchY = event.getY();	
//			
//			if(mLastTouchState == MOVE){
//
//				mAnimationHolder.dragStop(0.93f);
//				postInvalidate();
//			}
//			mLastTouchState = UP;
//			
//		}
//		
//		if(mIntercept ){
//			return true;
//		}
//		
//		return super.dispatchTouchEvent(event);
//	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		

		boolean intercept = false;
		if(event.getAction() == MotionEvent.ACTION_DOWN  ){

//			boolean isHit = mAabbBox.isHit(event.getX(), event.getY(), mHitIn);
//			
			mLastTouchState = DOWN;
			this.mTouchOffsetX = 0;
			this.mTouchOffsetY = 0;
			this.mAllTouchOffsetX = 0;
			this.mAllTouchOffsetY = 0;

			if(mAnimationHolder.getState() == AnimationHolder.DRAG_STOP || mAnimationHolder.getState() == AnimationHolder.DRAG_STOP_ATTRACT){
				intercept = true;
			}
			if(mBGDrawable.isHit(event.getX(), event.getY(), mHitIn)){
				mTouchAvaliable = true;
			}else{
				mTouchAvaliable = false;
			}
			
		}else if(event.getAction() == MotionEvent.ACTION_MOVE){

			this.mTouchOffsetX = event.getX() - this.mTouchX;
			this.mTouchOffsetY = event.getY() - this.mTouchY;
			
			this.mAllTouchOffsetX += this.mTouchOffsetX;
			this.mAllTouchOffsetY += this.mTouchOffsetY;
			
			float distan = Math.abs(mAllTouchOffsetX);
			if( distan   < EngineConstanst.REFERENCE_LIST_MOVE_TINGLE){//若在手指抖动误差值范围内则仍然生效 
				intercept = true;
			}	
		}else{

		}

		this.mTouchX = event.getX();
		this.mTouchY = event.getY();
		if(!mTouchAvaliable){
			mAnimationHolder.dragReset();
			intercept = true;
		}
		
		if(intercept){//拦截不符合条件的拖动事件 
			
			return false;
			
		}else{
			super.dispatchTouchEvent(event);
//			onTouchEvent(event);
			return true;
		}
		
//		return super.dispatchTouchEvent(event);
		
	}
	
	public boolean onTouchEvent(MotionEvent event)
	{

//		String ss = this.toString();
//		ss = ss.substring(ss.length()-9,ss.length() - 1);
//		Log.d("LOG", ss+"  onTouchEvent = "+event.getAction() + ",,mTouchAvaliable = "+mTouchAvaliable);

		if(event.getAction() == MotionEvent.ACTION_DOWN ){

//			boolean isHit = mAabbBox.isHit(event.getX(), event.getY(), mHitIn);
//			
			mLastTouchState = DOWN;
			
		}else if(event.getAction() == MotionEvent.ACTION_MOVE){

			
			if(mTouchOffsetX * mTouchOffsetX < 0){
				mAnimationHolder.dragReset();
			}
			if(mTouchOffsetY * mTouchOffsetX < 0){
				mAnimationHolder.dragReset();
			}

			onDrag(mTouchOffsetX,mTouchOffsetY);
			postInvalidate();
			mLastTouchState = MOVE;
		
//			Log.d("drag", "mTouchOffsetX = " + mTouchOffsetX);
		}else {
			
			if(mLastTouchState == MOVE){

				mAnimationHolder.dragStop(0.93f);
				postInvalidate();
			}
			mLastTouchState = UP;

			mTouchAvaliable = false;
		}
		return super.onTouchEvent(event);
	}
	
	
	
	protected boolean noSelectAnyone() {

		if (mRootScene != null) {
			if (getChildList().indexOf(mRootScene.getFocusView().getBindView()) == -1) {
				return true;
			}
		}
		return false;
	}

	protected void goSub(int offset,boolean dragStop) {

		for (int i = 0; i < Math.abs(offset); i++) {
			if (mFocusIndex > 0) {

				int addCardIndex = mFocusIndex - mHalfCount - 1;

				View addView = getView(addCardIndex);
				addView.setOnWidgetItemListener(mOnWidgetItemListener);

				View view = removeChild(getChildCount() - 1);// 移除最后一个
				TranslateInfo info = (TranslateInfo) view.getInfo();
				mFocusHashMap.remove(info.mPositionIndex);

				addChild(0, addView);// 添加新的到第一个位置
				mFocusHashMap.put(addCardIndex, addView);

				mFocusIndex--;

				if (mCardIndex > 0) {
					mCardIndex--;
					if(!dragStop){
						goAnimation();
					}
				}

				View focusview = mFocusHashMap.get(mFocusIndex);
				if (focusview != null) {
					focusview.requestFocus();
				}
			}
		}

	}

	protected OnCenterChangeListenner	mCenterChangeListenner;
	public interface OnCenterChangeListenner{

		void onSelectPosition(int center);
		
	}
	public void setOnCenterChangeListenner(OnCenterChangeListenner l){
		this.mCenterChangeListenner = l;
	}
	private void goAnimation() {
		
		int center = 0;
		if (mLazy) {
			int lastDestinationIndex = (int) (Math.round(mAnimationHolder.getDestinationIndex()));
			int newIndex = lastDestinationIndex;
			if (newIndex < mShowHalfRow) {
				newIndex = mShowHalfRow;
			} else if (mCardIndex - lastDestinationIndex < -mShowHalfRow) {
				newIndex -= 1;
				if (newIndex < mShowHalfRow) {
					newIndex = mShowHalfRow;
				}
			} else if (mCardIndex - lastDestinationIndex > mShowHalfRow) {
				newIndex += 1;
				int count = mAdapter.getCount();
				if (newIndex >= count - mShowHalfRow - 1) {
					newIndex = count - mShowHalfRow - 1;
				}
			}
			
			if (Math.abs(newIndex * mSpace - mAnimationHolder.getCurrentPara()) > 1.5f * mSpace) {
				mAnimationHolder.setDestinationIndex(newIndex, AnimationHolder.RUNNING, 0.5f, true, false);
			} else {
				mAnimationHolder.setDestinationIndex(newIndex, AnimationHolder.RUNNING, 0.7f, true, true);
			}
			center = newIndex;
		} else {

			int count = mAdapter.getCount();
			int newIndex = mCardIndex;
			if(count < 2 * mShowHalfRow + 1){ //如果总个数小于
				newIndex = mShowHalfRow;
			}else{

				if (newIndex <= mShowHalfRow) {
					newIndex = mShowHalfRow;
				} else if (newIndex >= count - mShowHalfRow - 1) {
					newIndex = count - mShowHalfRow - 1;
				}
			}
			if (Math.abs(newIndex * mSpace - mAnimationHolder.getCurrentPara()) > 1.5f * mSpace) {
				mAnimationHolder.setDestinationIndex(newIndex, AnimationHolder.RUNNING, 0.5f, true, false);
			} else {
				mAnimationHolder.setDestinationIndex(newIndex, AnimationHolder.RUNNING, 0.7f, true, true);
			}

			center = newIndex;
		}
		
		if(mCenterChangeListenner != null){
			mCenterChangeListenner.onSelectPosition(center);
		}

	}

	protected void goAdd(int offset,boolean dragStop) {

		int count = mAdapter.getCount();
		for (int i = 0; i < offset; i++) {

			if (mFocusIndex < count - 1) {

				int addCardIndex = mFocusIndex + mHalfCount + 1;
				// int addRealId = (addCardIndex % count + count) % count;

				View addView = getView(addCardIndex);
				addView.setOnWidgetItemListener(mOnWidgetItemListener);

				View view = removeChild(0);// 移除第0个
				TranslateInfo info = (TranslateInfo) view.getInfo();
				mFocusHashMap.remove(info.mPositionIndex);

				addChild(addView);// 添加新的到最后一个位置
				mFocusHashMap.put(addCardIndex, addView);

				mFocusIndex++;

				if (mCardIndex < count - 1) {

					mCardIndex++;
					if(!dragStop){
						goAnimation();
					}
				}

				View focusview = mFocusHashMap.get(mFocusIndex);
				if (focusview != null) {
					focusview.requestFocus();
				}
			}
		}

	}

	@Override
	protected void onAnimation() {
		
		super.onAnimation();
		mAnimationHolder.run(this + "center");
		
	}

	@Override
	protected void onChildDraw() {
		
		onMouseTouchScroll();
		onUpdateChildProperty();
//		super.onChildDraw();

		View focuView = null;
		for (int i = 0; i < getChildCount(); i++) {
			View view = get(i);
			if(view.isFocused()){
				focuView = view;
			}else {
				view.draw();
			}
		}
		if(focuView != null){
			focuView.draw();
		}
	}


	private void onUpdateChildProperty() {
		if (mAnimationHolder.isRunning()) {
			float offset = -mAnimationHolder.getCurrentPara();
			int adapterItemCount = mAdapter.getCount();
			for (int i = 0; i < getChildCount(); i++) {
				View view = get(i);
				TranslateInfo info = (TranslateInfo) view.getInfo();
				if (info.mPositionIndex < 0 || info.mPositionIndex >= adapterItemCount) {
					view.setVisibility(false);
				} else {
					setChildTranslate(view, info, offset);
				}
			}
			
		}
	}

	private void onMouseTouchScroll() {

		if(mAnimationHolder.isRunning()){
//			setScale(0.3f, 0.3f, 1);
			if(mAnimationHolder.getState() == AnimationHolder.DRAG){
				
				float offset = -mAnimationHolder.getCurrentPara() +  mSpace * mCardIndex;
				if(offset > mSpace*1/2f /*&& mCardIndex > mShowHalfRow */){
					goSub(-1,true);
				}else if(offset < -mSpace*1/2f/* && mCardIndex < mAdapter.getCount() - 1 - (mShowHalfRow )*/){
					goAdd(1,true);
				} 
				
			}else if(mAnimationHolder.getState() == AnimationHolder.DRAG_STOP){
				
				if(mCardIndex <= 0  ){
				
					mAnimationHolder.mState = AnimationHolder.DRAG_STOP_ATTRACT;
					
				}else if(mCardIndex >= mAdapter.getCount() - 1 - (0 )){
					
					mAnimationHolder.mState = AnimationHolder.DRAG_STOP_ATTRACT;
					
				}else{
					float offset = -mAnimationHolder.getCurrentPara() +  mSpace * mCardIndex;
					if(offset > mSpace*1/2f/* && mCardIndex > mShowHalfRow */ ){
						goSub(-1,true);

					}else if(offset < -mSpace*1/2f/* && mCardIndex < mAdapter.getCount() - 1 - (mShowHalfRow )*/){
						goAdd(1,true);

					}else{

//						mAnimationHolder.mState = AnimationHolder.DRAG_STOP_ATTRACT;
//						View centerChild = get(getChildCount() / 2);
//						centerChild.requestFocus();	
					}
				}
			}else if(mAnimationHolder.getState() == AnimationHolder.DRAG_STOP_ATTRACT){
				
				goDragStopAttractAnimation();
			}
			
		}
	}

	private void goDragStopAttractAnimation() {

		int center = 0;
		int count = mAdapter.getCount();
		int newIndex = mCardIndex;
		if(count < 2 * mShowHalfRow + 1){ //如果总个数小于
			newIndex = mShowHalfRow;
		}else{

			if (newIndex <= mShowHalfRow) {
				newIndex = mShowHalfRow;
			} else if (newIndex >= count - mShowHalfRow - 1) {
				newIndex = count - mShowHalfRow - 1;
			}
		}
		if (Math.abs(newIndex * mSpace - mAnimationHolder.getCurrentPara()) > 1.5f * mSpace) {
			mAnimationHolder.setDestinationIndex(newIndex, AnimationHolder.RUNNING, 0.2f, true, false);
		} else {
			mAnimationHolder.setDestinationIndex(newIndex, AnimationHolder.RUNNING, 0.3f, true, true);
		}

		center = newIndex;

		if(mCenterChangeListenner != null){
			mCenterChangeListenner.onSelectPosition(center);
		}
	}

	/*
	 * 循环列表初始设置适配器
	 */
	public void setAdapter(Adapter adapter) {

		mFocusHashMap.clear();
		mFocusIndex = 0;
		removeAll();

		// 检查listView可见的区域可以放置几个childView
		mAnimationHolder = new AnimationHolder(mShowHalfRow, mSpace);
		mAnimationHolder.setDestinationIndex(mShowHalfRow, AnimationHolder.RUNNING, 1, true, false);

		this.mAdapter = adapter;
		this.mAdapter.setAttachView(this);

		mCardIndex = 0;
		int count = mAdapter.getCount();
		if (count == 0) {
			return;
		}

		for (int i = 0; i < mCardCount; i++) { // 让第0项居中
			int addCardIndex = i - mHalfCount;

			View view = getView(addCardIndex);
			view.setOnWidgetItemListener(mOnWidgetItemListener);
			
			mFocusHashMap.put(addCardIndex, view);

			super.addChild(view);
		}

	}


	public void setLazyMode(boolean flag) {
		mLazy = flag;
	};

	public void setShowHalf(int half) {
		mShowHalfRow = half;
	};

	public void requestCenterFocus() {

		View centerChild = get(getChildCount() / 2);
		centerChild.requestFocus();
	}

	public void setSelectItem(int focusIndex) {

		if(focusIndex >= mAdapter.getCount() - 1){
			focusIndex = mAdapter.getCount() - 1;
		}
		
		if (mFocusIndex > focusIndex) {
			goSub(focusIndex - mFocusIndex,false);
		} else if (mFocusIndex < focusIndex) {
			goAdd(focusIndex - mFocusIndex,false);
		} else {
			View view = mFocusHashMap.get(mFocusIndex);
			if (view != null) {
				view.requestFocus();
			}
		}

	}

	public void requestFirstFocus() {

		View view = mFocusHashMap.get(0);
		if (view != null) {
			view.requestFocus();
		}
	}
	

}
