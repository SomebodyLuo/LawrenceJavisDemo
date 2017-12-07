package com.x.components.widget.newer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.DataSetObserver;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

import com.x.Director;
import com.x.opengl.kernel.AnimationHolder;
import com.x.opengl.kernel.EngineConstanst;
import com.x.opengl.kernel.Position;
import com.x.opengl.kernel.Texture;
import com.x.components.node.View;
import com.x.components.widget.Adapter;
import com.x.components.widget.AdapterAnimationHelper;
import com.x.components.widget.AdapterView;

/**
 * 
 * 
 * 
 */
public abstract class NewAdapterView extends AdapterView<Adapter> {

	protected int							mSpaceX					= 305;							// X方向的每个child之间的间距，即列间距
	protected int							mSpaceY					= 275;							// Y

	protected int mBaseLeftX = 0;
	protected int mBaseTopY = 0;
																									// 1);
	protected boolean						mLimitMin				= true;
	protected boolean						mLimitMax				= true;
	protected int							mCardRow				= 5;							// 必须是单数,才能保证以中间数为分界的两边数目相等
	protected int							mHalfRow				= mCardRow / 2;				// 行数的一半
	protected int							mRowOffsetCount			= 1;

	protected int							mHideOutRowCount		= 2;
	protected int							mCardColumns			= 5;							// 列数
	protected int							mCardRowIndex			= 0;							// 指示第几行的索引

	protected Adapter						mAdapter;

	protected Map<Integer, View>			mFocusFindHashMap		= new HashMap<Integer, View>();

	protected RecycleBin					mRecycleBin				= new RecycleBin();
	protected int							mHoldCardIndex;
	protected long							mTime;
	protected OnCenterLineChangeListenner	mCenterLineChangeListenner;
	protected OnFocusRowChangeListenner		mOnFocusRowChangeListenner;
	protected int							mTotalRowCount;
	protected int								mMinimumVelocity;
	protected int								mTouchSlop;

	protected static int					MOVE					= 0;
	protected static int					UP						= 2;
	protected static int					DOWN					= 1;
	protected int							mLastTouchState			= UP;
	protected float							mTouchOffsetX;
	protected float							mTouchOffsetY;
	protected float							mTouchX;
	protected float							mTouchY;
	protected int							mAllTouchOffsetX;
	protected int							mAllTouchOffsetY;
	protected boolean							mIntercept;
	protected float							mNormalSpeed			= 0.5f;
	protected float							mQuicklySpeed			= 0.7f;
	protected float							mDragSpeed				= 0.3f;

	protected VelocityTracker				mVelocityTracker;

	protected float							mLastionMotionX;
	protected float							mLastionMotionY;
	protected float							mMaximumVelocity;
	protected boolean					mAnimationing;
	protected int							mCount;
	protected int							mActiveCardCount;
	protected int							mCardRowStartIndex;
	protected int							mCardRowEndIndex;

	protected int							mScrollIndex;
	
	private 	  View		 mRestoreViewFOrFocusMove = new View();

	protected AdapterAnimationHelper		mAdapterAnimationHelper	= new AdapterAnimationHelper();
	private AnimationHolder mFocusMOveScrollerX ;
	private AnimationHolder mFocusMOveScrollerY ;

	protected abstract void setRows(int i);
	protected abstract void setColumns(int i);
	protected abstract void setHorizontalSpace(int space);
	protected abstract void setVeticalSpace(int space);
	protected abstract float getOffsetX();
	protected abstract float getOffsetY();
//	protected abstract void setUserOffsetX(int offset);
//	protected abstract void setUserOffsetY(int offset);

	protected abstract void initTranslateInfo(View view, int idX, int idY, float offsetX,
			float offsetY, int cardIndex);
	protected abstract void updateViewTranslate(View view, TwoDTranslateInfo info,float offset) ;
	protected abstract Position getFocusPosition( TwoDTranslateInfo info,float offset);

	protected abstract void touchDrag(float mTouchOffsetX2, float mTouchOffsetY2) ;
	protected abstract void touchFling(int velocityX, int velocityY) ;
	protected abstract void onKeyCodeLeft();
	protected abstract void onKeyCodeRight();
	protected abstract void onKeyCodeUp();
	protected abstract void onKeyCodeDown();
	
	
	
	public interface OnCenterLineChangeListenner {
		void onSelectLine(int lineIndex);
	}

	public interface OnFocusRowChangeListenner {
		void onFocusLine(int focusLineIndex);
		void onLastLineOut();
	}

	public void setOnCenterLineChangeListenner(OnCenterLineChangeListenner l) {
		mCenterLineChangeListenner = l;
	}

	public void setOnFocusRowChangeListenner(OnFocusRowChangeListenner l) {
		mOnFocusRowChangeListenner = l;
	}

	public NewAdapterView() {
		super();
		final ViewConfiguration configuration = ViewConfiguration.get(Director.getInstance().getContext());

		mTouchSlop = configuration.getScaledTouchSlop();
		mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
		// int count = Math.round(this.getWidth() / mList.get(0).getWidth()
		// +0.5f);
		// count = count > mList.size() ? mList.size() : count;
	}

	protected void initVelocityTrackerIfNotExists() {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
	}

	@Override
	public boolean onKeyEvent(KeyEvent event) {

		// Log.d("temp", "NoLoopGridView onKeyEvent "+event.getAction());
		boolean down_flag = false;
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			down_flag = true;
		}

		if (mRootScene != null) {

			boolean hasFocus = false;
			for (int i = 0; i < getChildCount(); i++) {
				hasFocus |= get(i).isFocused();
				if(hasFocus){

					break;
				}
			}

			if (!hasFocus) {
				return super.onKeyEvent(event);
			}
		}

		switch (event.getKeyCode()) {

			case KeyEvent.KEYCODE_DPAD_LEFT:

				if (down_flag) {

					onKeyCodeLeft();

					return true;
				}
			break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:

				if (down_flag) {

					onKeyCodeRight();

					return true;
				}
			break;
			case KeyEvent.KEYCODE_DPAD_DOWN:

				if (down_flag) {

					onKeyCodeDown();

					return true;
				}

			break;
			case KeyEvent.KEYCODE_DPAD_UP:

				if (down_flag) {

					onKeyCodeUp();

					return true;
				}
			break;
			default:

			break;
		}

		return super.onKeyEvent(event);
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		initVelocityTrackerIfNotExists();
		mVelocityTracker.addMovement(event);

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				mLastTouchState = DOWN;
				this.mTouchOffsetX = 0;
				this.mTouchOffsetY = 0;
				this.mTouchX = event.getX();
				this.mTouchY = event.getY();
				this.mAllTouchOffsetX = 0;
				this.mAllTouchOffsetY = 0;
				this.mIntercept = false;

				if (mAdapterAnimationHelper.getState() == AdapterAnimationHelper.DRAG_UP || mAdapterAnimationHelper.getState() == AdapterAnimationHelper.DRAG_UP_WAIT) {

					mAdapterAnimationHelper.forceFinish();

					mAdapterAnimationHelper.setState(AdapterAnimationHelper.DEFAULT);
					postInvalidate();
				}
			break;

			case MotionEvent.ACTION_MOVE:

				float offsetX = event.getX() - this.mTouchX;
				float offsetY = event.getY() - this.mTouchY;

				this.mTouchOffsetX = offsetX;
				this.mTouchOffsetY = offsetY;

				this.mAllTouchOffsetX += this.mTouchOffsetX;
				this.mAllTouchOffsetY += this.mTouchOffsetY;

				float distan = Math.abs(mAllTouchOffsetY);
				if (distan > EngineConstanst.REFERENCE_LIST_MOVE_TINGLE) {// 若在手指抖动误差值范围内则仍然生效
					mIntercept = true;
				}

				touchDrag(mTouchOffsetX,mTouchOffsetY);
				postInvalidate();

				this.mTouchX = event.getX();
				this.mTouchY = event.getY();

				// if(mLastTouchState == DOWN){
				if (mTouchOffsetX > 0) {
					// changeRight();
				} else if (mTouchOffsetX < 0) {
					// changeLeft();
				}
				// }
				mLastTouchState = MOVE;
				if (mIntercept) {

					return true;
				}

			break;
			case MotionEvent.ACTION_UP:

				if (mLastTouchState == MOVE) {

					mVelocityTracker.computeCurrentVelocity(500, mMaximumVelocity);
					int velocityX = (int) mVelocityTracker.getXVelocity();
					int velocityY = (int) mVelocityTracker.getYVelocity();
					// mwolfScroller.forceFinish();
					if (velocityY < 0 && velocityY > -mMinimumVelocity) {
						velocityY = -mMinimumVelocity;
					}
					if (velocityY > 0 && velocityY < mMinimumVelocity) {
						velocityY = mMinimumVelocity;
					}
					if (velocityY == 0) {
						if (mAllTouchOffsetY > 0) {
							velocityY = -mMinimumVelocity;
						} else {
							velocityY = mMinimumVelocity;
						}
					}
					if (velocityX == 0) {
						if (mAllTouchOffsetX > 0) {
							velocityX = -mMinimumVelocity;
						} else {
							velocityX = mMinimumVelocity;
						}
					}
					touchFling(velocityX,velocityY);
					postInvalidate();
				}
				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
				mLastTouchState = UP;
				this.mTouchX = event.getX();
				this.mTouchY = event.getY();
				if (mIntercept) {
					return true;
				}

			break;
		}

		return super.dispatchTouchEvent(event);
	}

	protected void onMouseTouchScroll() {
		// setScale(0.3f, 0.3f, 1);
//		Log.d("tag", mMinimumVelocity + "," + mMaximumVelocity + "  mAdapterAnimationHelper.volicity = " + mAdapterAnimationHelper.getCurrVelocity() + ",,state = "
//				+ mAdapterAnimationHelper.getState());

		switch (mAdapterAnimationHelper.getState()) {
			case AdapterAnimationHelper.DRAG:
				float offset = -mAdapterAnimationHelper.getCurrentPara() + mSpaceY * mCardRowIndex;
				if (offset > mSpaceY * 1 / 2f) {
					goPreLine(true);

				} else if (offset < -mSpaceY * 1 / 2f) {
					goNextLine(true);
				}
			// if (offset > mSpaceY * 1 / 2f /* && mCardIndex > mShowHalfRow */)
			// {
			// goPreLine(true);
			// } else if (offset < -mSpaceY * 1 / 2f/*
			// * && mCardIndex <
			// * mAdapter.getCount() - 1 -
			// * (mShowHalfRow )
			// */) {
			// goNextLine(true);
			// }
			break;
			case AdapterAnimationHelper.DRAG_UP:

				int rowCount = getTotalRowNumber();

				if (mCardRowIndex <= 0) {

					mAdapterAnimationHelper.setState(AdapterAnimationHelper.DRAG_UP_WAIT);

				} else if (mCardRowIndex >= rowCount - 1 - (0)) {

					mAdapterAnimationHelper.setState(AdapterAnimationHelper.DRAG_UP_WAIT);

				} else {

					offset = -mAdapterAnimationHelper.getCurrentPara() + mSpaceY * mCardRowIndex;
					if (offset > mSpaceY * 1 / 2f) {
						goPreLine(true);

					} else if (offset < -mSpaceY * 1 / 2f) {
						goNextLine(true);

					} else {
						// mAdapterAnimationHelper.setState(AdapterAnimationHelper.DRAG_UP_WAIT);
						// mAdapterAnimationHelper.setState(AdapterAnimationHelper.DRAG_STOP_ATTRACT);
					}
				}
			break;
			case AdapterAnimationHelper.DRAG_UP_TO_ATTRACT:

				goDragStopAttractAnimation();
				mAdapterAnimationHelper.setState(AdapterAnimationHelper.DEFAULT);
				postInvalidate();
			// goDragStopAttractAnimation();
			// mwolfScroller.setState(wolfScroller.DEFAULT);
			break;

			default:
			break;
		}
	}


	protected boolean goNextPosition(boolean swapLineFlag) {

		boolean dealEvent = false;
		int adapterItemCount = mAdapter.getCount();
		int rowCount = getTotalRowNumber();

		if (mHoldCardIndex < adapterItemCount - 1) {
			mHoldCardIndex++;
			if (mHoldCardIndex % mCardColumns == 0) {
				if (!swapLineFlag) {
					mHoldCardIndex--;
				} else {
					if (mCardRowIndex < getTotalRowNumber() - 1) {
						if (mCardRowIndex + mRowOffsetCount == mCardRowEndIndex && mCardRowIndex < rowCount - 1 - (mRowOffsetCount)) {
							removeFirstAndAddLast();
							scrollChange(+1);
						}
						mCardRowIndex++;
						dealEvent = true;
					}
				}
			} else {
				dealEvent = true;
			}
			adapterFocusMove();
			
		} else { 
		}

		if (mOnFocusRowChangeListenner != null) {
			mOnFocusRowChangeListenner.onFocusLine(mCardRowIndex);
		}

		return dealEvent;
	}

	protected boolean goPrePosition(boolean swapLineFlag) {

		boolean dealEvent = false;
		if (mHoldCardIndex > 0) {
			mHoldCardIndex--;
			if (mHoldCardIndex % mCardColumns == (mCardColumns - 1)) {
				if (!swapLineFlag) {
					mHoldCardIndex++;
				} else {
					if (mCardRowIndex > 0) {

						if (mCardRowIndex - mRowOffsetCount == mCardRowStartIndex && mCardRowIndex > (mRowOffsetCount)) {
							removeLastAndAddFirst();
							scrollChange(-1);
						}
						mCardRowIndex--;
						dealEvent = true;
					}
				}
			} else {
				dealEvent = true;
			}

			adapterFocusMove();
		} else {
		}

		if (mOnFocusRowChangeListenner != null) {
			mOnFocusRowChangeListenner.onFocusLine(mCardRowIndex);
		}

		return dealEvent;
	}

	protected boolean timeLimit(float limitTime) {

		if (System.currentTimeMillis() - mTime < limitTime) {
			return true;
		}
		mTime = System.currentTimeMillis();
		return false;
	}

	@Override
	public boolean onWheelEvent(float vscroll) {

		// if (timeLimit(100)) {
		// return true;
		// }
		if (vscroll > 0) {

			if (vscroll > 1) {
				vscroll = 1;
			}
			for (int i = 0; i < vscroll; i++) {
				goPreLine(false);
			}
			return true;

		} else if (vscroll < 0) {

			if (vscroll < -1) {
				vscroll = -1;
			}
			for (int i = 0; i < -vscroll; i++) {
				goNextLine(false);
			}
			return true;

		}

		return super.onWheelEvent(vscroll);
	}

	protected boolean goPreLine(boolean flingStop) {
		boolean changeLineSuccuss = false;
		if (mHoldCardIndex - mCardColumns >= 0) {
			mHoldCardIndex -= mCardColumns;

			if (mCardRowIndex > 0) {

				if (mCardRowIndex - mRowOffsetCount == mCardRowStartIndex && mCardRowIndex > (mRowOffsetCount)) {
					removeLastAndAddFirst();
					if (flingStop) {
						flingChange(-1);
					} else {
						scrollChange(-1);
					}
				}
				mCardRowIndex--;
				changeLineSuccuss = true;
			}
			adapterFocusMove();
		}

		if(mOnFocusRowChangeListenner != null  ){
			mOnFocusRowChangeListenner.onFocusLine(mCardRowIndex);
		}
		return changeLineSuccuss;
	}

	protected boolean goNextLine(boolean dragStop) {

		boolean changeLineSuccuss = false;
		int adapterItemCount = mAdapter.getCount();
		int rowCount = getTotalRowNumber();

		if (mHoldCardIndex + mCardColumns < adapterItemCount - 1) {
			mHoldCardIndex += mCardColumns;

			if (mCardRowIndex < rowCount - 1) {
				if (mCardRowIndex + mRowOffsetCount == mCardRowEndIndex && mCardRowIndex < rowCount - 1 - (mRowOffsetCount)) {
					removeFirstAndAddLast();
					if (dragStop) {
						flingChange(+1);
					} else {
						scrollChange(+1);
					}
				}
				mCardRowIndex++;
				changeLineSuccuss = true;
			}

			adapterFocusMove();
		} else {

			int rowIndex = getCurrentRowNumber();
			rowIndex--;

			if (rowIndex == rowCount - 1) {

			} else {

				mHoldCardIndex = adapterItemCount - 1;

				if (mCardRowIndex < rowCount - 1) {
					if (mCardRowIndex + mRowOffsetCount == mCardRowEndIndex && mCardRowIndex < rowCount - 1 - (mRowOffsetCount)) {
						removeFirstAndAddLast();
						if (dragStop) {
							flingChange(+1);
						} else {
							scrollChange(+1);
						}
					}
					mCardRowIndex++;
					changeLineSuccuss = true;
				}
				adapterFocusMove();
				;
			}
		}


		if(mOnFocusRowChangeListenner != null && !changeLineSuccuss ){
			mOnFocusRowChangeListenner.onLastLineOut();
		}
		if(mOnFocusRowChangeListenner != null   ){
			mOnFocusRowChangeListenner.onFocusLine(mCardRowIndex);
		}
		return changeLineSuccuss;
	}

	protected void flingChange(int i) {
		mScrollIndex += i;
		// mwolfScroller.scrollToTargetIndex(mScrollIndex,500);
		arrowListenerCallBack();
	}

	protected void scrollChange(int i) {
		mScrollIndex += i;
		mAdapterAnimationHelper.scrollToTargetIndex(mScrollIndex);
		arrowListenerCallBack();
	}


	public int getTotalRowNumber() {
		mTotalRowCount = (int) Math.ceil(mAdapter.getCount() * 1f / mCardColumns);
		;// 往上取整
			// mTotalRowCount = mAdapter.getCount() % mCardColumns == 0 ?
			// mAdapter.getCount() / mCardColumns : mAdapter.getCount() /
			// mCardColumns + 1;
		return mTotalRowCount;
	}

	public int getCurrentRowNumber() {
		return (int) Math.ceil((mHoldCardIndex + 1f) / mCardColumns);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	protected void removeLastAndAddFirst() {
		int removeIdY = mCardRowEndIndex + mHideOutRowCount;
		for (int j = 0; j < mCardColumns; j++) {
			int removeIdX = j;
			int cardIndex = removeIdY * mCardColumns + removeIdX;
			if (cardIndex < mAdapter.getCount() && cardIndex >= 0) {


				View focusInfo = mFocusFindHashMap.get(cardIndex);
				if (focusInfo != null) {
					removeChild(focusInfo);
					mFocusFindHashMap.remove(cardIndex);
					mRecycleBin.addScrapView(cardIndex, focusInfo);
				}
			}
		}

		mCardRowEndIndex -= 1;

		float offestX = getOffsetX() + mBaseLeftX;
		float offestY = getOffsetY() + mBaseTopY;
		// int ofset = -(int)(mwolfScroller.getCurrentPara() );
		for (int i = mCardRowStartIndex - 1; i >= mCardRowStartIndex - 1 - mHideOutRowCount; i--) {
			int addIdY = i;
			for (int j = mCardColumns - 1; j >= 0; j--) { // 锟斤拷锟斤拷前锟斤拷锟斤拷锟斤拷锟斤拷锟�
				int addIdX = j;
				int cardIndex = addIdY * mCardColumns + addIdX;

				if (cardIndex < mAdapter.getCount() && cardIndex >= 0) {
					// view.setVisibility(View.GONE);
					if (mFocusFindHashMap.get(cardIndex) != null) {
						// mFocusFindHashMap.get(cardIndex).layoutView(addIdX,
						// addIdY, ofset);
					} else {
						View convertView = mRecycleBin.getScrapView();
						View view = mAdapter.getView(cardIndex, convertView, null);
						initListener(view);
						// FocusInfo focusInfo = new
						// FocusInfo(view,addIdX,addIdY,cardIndex);
						// focusInfo.layoutView(l,t,addIdX,addIdY,cardIndex);
						initTranslateInfo(view,addIdX,addIdY,offestX,offestY,cardIndex);

						mFocusFindHashMap.put(cardIndex,  view);
						addChild(0, view);
					}

				}
			}
		}

		mCardRowStartIndex -= 1;
	}


	@Override
	protected void onItemSelectScrollTo(View v) {

		TwoDTranslateInfo translateInfo = (TwoDTranslateInfo) v.getInfo();
	
		setSelectItem(translateInfo.mShowIndex);
	}

	protected void removeFirstAndAddLast() {

		int removeIdY = mCardRowStartIndex - mHideOutRowCount;
		for (int j = 0; j < mCardColumns; j++) {
			int removeIdX = j;
			int cardIndex = removeIdY * mCardColumns + removeIdX;
			if (cardIndex < mAdapter.getCount() && cardIndex >= 0) {
				

				View focusInfo = mFocusFindHashMap.get(cardIndex);
				if (focusInfo != null) {
					removeChild(focusInfo);
					mFocusFindHashMap.remove(cardIndex);
					mRecycleBin.addScrapView(cardIndex, focusInfo);
				}
			}
		}
		mCardRowStartIndex += 1;

		float offestX = getOffsetX()  + mBaseLeftX;;
		float offestY = getOffsetY()  + mBaseTopY;;
		// int ofset = -(int)(mwolfScroller.getCurrentPara() );
		for (int i = mCardRowEndIndex + 1; i <= mCardRowEndIndex + 1 + mHideOutRowCount; i++) {
			int addIdY = i;
			for (int j = 0; j < mCardColumns; j++) {
				int addIdX = j;
				int cardIndex = addIdY * mCardColumns + addIdX;
				if (cardIndex < mAdapter.getCount() && cardIndex >= 0) {
					// view.setVisibility(View.GONE);
					if (mFocusFindHashMap.get(cardIndex) != null) {
						// mFocusFindHashMap.get(cardIndex).layoutView(addIdX,
						// addIdY, ofset);
					} else {

						View convertView = mRecycleBin.getScrapView();
						View view = mAdapter.getView(cardIndex, convertView, null);
						initListener(view);
						initTranslateInfo(view,addIdX,addIdY,offestX,offestY,cardIndex);

						mFocusFindHashMap.put(cardIndex, (view));
						addChild(view);
					}
				}
			}
		}

		mCardRowEndIndex += 1;
	}


	@Override
	public void draw() {
		super.draw();
	}

	@Override
	protected void onAnimation() {
		super.onAnimation();
		mAnimationing = mAdapterAnimationHelper.computeOffset();
		if (!mAnimationing) {
			if (mAdapterAnimationHelper.getState() == AdapterAnimationHelper.DRAG_UP_WAIT) {

				goDragStopAttractAnimation();
				mAdapterAnimationHelper.setState(AdapterAnimationHelper.DEFAULT);
			} else if (mAdapterAnimationHelper.getState() == AdapterAnimationHelper.DRAG_UP) {

				mAdapterAnimationHelper.setState(AdapterAnimationHelper.DRAG_UP_WAIT);
			}
			
			postInvalidate();
		}

		if(mFocusMOveScrollerX != null){
			mFocusMOveScrollerX.run(this);
			mFocusMOveScrollerY.run(this);
			if(mFocusMOveScrollerX.isRunning() || mFocusMOveScrollerY.isRunning()){

				mRestoreViewFOrFocusMove.setTranslate(mFocusMOveScrollerX.getCurrentPara() , mFocusMOveScrollerY.getCurrentPara(), 0);
				postInvalidate();
			}
		}
		
	}

	private View mFocusView;
	@Override
	protected void onChildDraw() {

		if (mAnimationing) {
			onMouseTouchScroll();
			onUpdateChildProperty();
		}
		// /////////

		View focusInfo = mFocusFindHashMap.get(mHoldCardIndex);
		for (int i = 0; i < getChildCount(); i++) {
			View view = get(i);
			if (view != focusInfo) {
				view.draw();
			}
		}
		if ( focusInfo != null) {
			 focusInfo.draw();
			;
		}
		mRestoreViewFOrFocusMove.draw();

	}

	protected void goDragStopAttractAnimation() {


		mAdapterAnimationHelper.scrollToTargetIndex(mScrollIndex);
		if (mOnFocusRowChangeListenner != null) {
			mOnFocusRowChangeListenner.onFocusLine(mCardRowIndex);
		}
		// if (mCenterLineChangeListenner != null) {
		// mCenterLineChangeListenner.onSelectLine(lineIndex);
		// }
	}

	protected void onUpdateChildProperty() {
		float offset = mAdapterAnimationHelper.getCurrentPara();
		int adapterItemCount = mAdapter.getCount();
		for (int i = 0; i < getChildCount(); i++) {
			View view = get(i);
			TwoDTranslateInfo info = (TwoDTranslateInfo) view.getInfo();
			updateViewTranslate(view,info,offset);
			if (info.mShowIndex < 0 || info.mShowIndex >= adapterItemCount) {
				view.setVisibility(false);
			}
			
		}
	}



	private MyDataSetObserver	mMyDataSetObserver;

	class MyDataSetObserver extends DataSetObserver {
		@Override
		public void onChanged() {
			updateUIbyDataSetChanged();
			super.onChanged();
		}

		@Override
		public void onInvalidated() {
			updateUIbyDataSetChanged();
			super.onInvalidated();
		}
	}

	private void updateUIbyDataSetChanged() {
		// Log.d("debug", "updateUIbyDataSetChanged ");
		setAdapter(mAdapter);
	}

	/*
	 * 循环列表初始设置适配器
	 */
	public void setAdapter(Adapter adapter) {

		if (mAdapter != null && mMyDataSetObserver != null) {
			mAdapter.unregisterDataSetObserver(mMyDataSetObserver);
		}
		mFocusFindHashMap.clear();
		mRecycleBin.clear();
		removeAll();

		this.mAdapter = adapter;
		mMyDataSetObserver = new MyDataSetObserver();
		mAdapter.registerDataSetObserver(mMyDataSetObserver);

		int adapterItemCount = mAdapter.getCount();
		if (adapterItemCount == 0) {
			return;
		}
		mCardRowIndex = 0;
		mHoldCardIndex = 0;

		initProperties();
		mAdapterAnimationHelper.init(mScrollIndex, mSpaceY);

		 mFocusMOveScrollerX = null;
		 mFocusMOveScrollerY = null;
		 
		float offestX = getOffsetX()  + mBaseLeftX;;
		float offestY = getOffsetY()  + mBaseTopY;;
		for (int i = mCardRowStartIndex - mHideOutRowCount; i <= mCardRowEndIndex + mHideOutRowCount; i++) {
			int idY = i;
			for (int j = 0; j < mCardColumns; j++) {
				int idX = j;
				int cardIndex = idY * mCardColumns + idX;
				if (cardIndex < mAdapter.getCount() && cardIndex >= 0) {
					// view.setVisibility(View.GONE);
					View view = mAdapter.getView(cardIndex, null, null);
					if (cardIndex == 0) {
//						view.requestFocus();
							
						mRestoreViewFOrFocusMove.setFocusable(false);
						mRestoreViewFOrFocusMove.setClickable(false);
						mRestoreViewFOrFocusMove.setVisibility(false);
						mRestoreViewFOrFocusMove.setWidth(view.getWidth());
						mRestoreViewFOrFocusMove.setHeight(view.getHeight());
//						mRestoreViewFOrFocusMove.setBackgroundColor(Color.parseColor("#77ff0000"));
					}
					initListener(view);
					initTranslateInfo(view,idX,idY,offestX,offestY,cardIndex);
					
					mFocusFindHashMap.put(cardIndex,(view));
					super.addChild(view);

				}
			}
		}

		if (mOnFocusRowChangeListenner != null) {
			mOnFocusRowChangeListenner.onFocusLine(mCardRowIndex);
		}

		
//		adapterFocusMove();
		// 检查listView可见的区域可以放置几个childView

		// View centerChild = get(getChildCount()/2);
		// centerChild.requestFocus();
		// firstChildView.requestFocus();
	}

	private void initListener(View view) {
//		view.setOnWidgetItemListener(mOnWidgetItemListener);
	}

	private void initProperties() {

//		Log.d("debug", "normal mCount = " + mCount + ",,mTotalRowCount = " + mTotalRowCount);
//		Log.d("debug", "normal mCardRowStartIndex = " + mCardRowStartIndex + ",,mCardRowIndex = " + mCardRowIndex + ",,mCardRowEndIndex = " + mCardRowEndIndex);
//		Log.d("debug", "normal mHoldCardIndex = " + mHoldCardIndex + ",,mScrollIndex = " + mScrollIndex);

		actToInitValue();
//		Log.d("debug", "----------------------------------");
//		Log.d("debug", "normal mCount = " + mCount + ",,mTotalRowCount = " + mTotalRowCount);
//		Log.d("debug", "normal mCardRowStartIndex = " + mCardRowStartIndex + ",,mCardRowIndex = " + mCardRowIndex + ",,mCardRowEndIndex = " + mCardRowEndIndex);
//		Log.d("debug", "normal mHoldCardIndex = " + mHoldCardIndex + ",,mScrollIndex = " + mScrollIndex);
		arrowListenerCallBack();
	}

	private void arrowListenerCallBack() {
		// if(mArrowListenner != null){
		// int leftIndex = mScrollIndex;
		// int rightIndex = mScrollIndex + mCardRow - 1;
		// if(leftIndex > 0){
		// mArrowListenner.leftArrowShow(true);
		// }else{
		// mArrowListenner.leftArrowShow(false);
		// }
		// if(rightIndex < getTotalRowNumber() - 1){
		// mArrowListenner.rightArrowShow(true);
		// }else{
		// mArrowListenner.rightArrowShow(false);
		// }
		// }
	}

	protected void actToInitValue() {
		int destHoldCardIndex = mCardRow / 2 * mCardColumns;
		int adapterItemCount = mAdapter.getCount();
		if (destHoldCardIndex > adapterItemCount - 1) {
			if (destHoldCardIndex > 0) {
				destHoldCardIndex -= 1;
			}
		}
		;
		int destScrollIndex = mCardRow / 2;
		int cardRowStartIndex = 0;
		int cardRowEndIndex = cardRowStartIndex + mCardRow - 1;
		int rowCount = (int) Math.ceil(adapterItemCount * 1f / mCardColumns);
		;

		int index = 0;
		int rowIndex = 0;
		// go to CenterIndex
		for (int i = 0; i < destHoldCardIndex; i++) {
			if (index < adapterItemCount - 1) {
				index++;
				if (index % mCardColumns == 0) {
					if (rowIndex < rowCount - 1) {
						if (rowIndex + mRowOffsetCount == cardRowEndIndex && rowIndex < rowCount - 1 - (mRowOffsetCount)) {
							cardRowStartIndex += 1;
							cardRowEndIndex += 1;
							destScrollIndex += 1;
						}
						rowIndex++;
					}
				}
			}
		}
		// go back to Zero
		for (int i = 0; i < destHoldCardIndex; i++) {

			if (index > 0) {
				index--;
				if (index % mCardColumns == (mCardColumns - 1)) {
					if (rowIndex > 0) {
						if (rowIndex - mRowOffsetCount == cardRowStartIndex && rowIndex > (mRowOffsetCount)) {
							cardRowStartIndex -= 1;
							cardRowEndIndex -= 1;
							destScrollIndex -= 1;
						}
						rowIndex--;
					}
				}
			}
		}

		mCount = adapterItemCount;
		mTotalRowCount = rowCount;
		mActiveCardCount = mCardColumns * (mCardRow + mHideOutRowCount * 2);

		mCardRowStartIndex = cardRowStartIndex;
		mCardRowIndex = rowIndex;
		mCardRowEndIndex = cardRowEndIndex;

		mHoldCardIndex = index;
		mScrollIndex = destScrollIndex;
	}

	protected View getView(int idX, int idY, int cardIndex, float baseOffestX, float baseOffsetY) {
		View view = mAdapter.getView(cardIndex);
		view.setOnWidgetItemListener(mOnWidgetItemListener);
		view.setInfo(new TwoDTranslateInfo(idX * mSpaceX + baseOffestX, -idY * mSpaceY + baseOffsetY, idX, idY, cardIndex));
		view.setTranslate(idX * mSpaceX + baseOffestX, -idY * mSpaceY, 0);
		return view;
	}



	public View getSelectItem() {
		return mFocusFindHashMap.get(mHoldCardIndex);
	}

	public int getSelectIndex() {
		return mHoldCardIndex;
	};

	public void setSelectItem(int focusIndex) {

//		Log.d("debug","jar  focusIndex = "+focusIndex+ "   jar  mHoldCardIndex = "+mHoldCardIndex );
		if(mAdapter == null){
			return;
		}
		if (focusIndex > mAdapter.getCount() - 1) {
			focusIndex = mAdapter.getCount() - 1;
		}

//		Log.d("debug","jar  focusIndex = "+focusIndex+ "   jar  mHoldCardIndex = "+mHoldCardIndex );
		if (focusIndex > mHoldCardIndex) {

			int moveCount = focusIndex - mHoldCardIndex;
			for (int i = 0; i < moveCount; i++) {
				goNextPosition(true);
			}
		} else if (focusIndex < mHoldCardIndex) {

			int moveCount = mHoldCardIndex - focusIndex;
			for (int i = 0; i < moveCount; i++) {
				goPrePosition(true);
			}
		} else {
			
			adapterFocusMove();
			
		}
	};




	class RecycleBin {

//		private HashMap<Integer, View>	mScrapViews	= new HashMap<Integer, View>();
		private List<View> mScrapViews = new ArrayList<View>();

		public void clear() {
			mScrapViews.clear();
		}

		public View getScrapView() {
			if(mScrapViews.size() == 0){
				return null;
			}
			View view = mScrapViews.remove(mScrapViews.size()-1);
			return view;
		}

		public void addScrapView(int realIndex, View view) {
//			mScrapViews.put(index, view);
//			Log.d("debug", "scrapviews = "+mScrapViews.size());
			mScrapViews.add(0,view);

		}
		
	}

	private void adapterFocusMove() {
		View view = mFocusFindHashMap.get(mHoldCardIndex);
		mFocusView = view;

		if (view != null) {	
			mFocusView.requestFocus();
			TwoDTranslateInfo translateInfo = (TwoDTranslateInfo) view.getInfo();
			 Position position = getFocusPosition(translateInfo,mScrollIndex * mSpaceY);

			 if(mFocusMOveScrollerX == null){
				 
				 mFocusMOveScrollerX = new AnimationHolder((int) position.X-1, 1);
				 mFocusMOveScrollerY = new AnimationHolder( (int) position.Y-1, 1);
				 mFocusMOveScrollerX.setDestinationIndex( position.X );
				 mFocusMOveScrollerY.setDestinationIndex(position.Y );
				 postInvalidate();
			 }else{
				 
				 mFocusMOveScrollerX.setDestinationIndex( position.X );
				 mFocusMOveScrollerY.setDestinationIndex(position.Y );
			 }
//			mRestoreViewFOrFocusMove.requestFocus();
		}
	}

	public void setFocusViewTexture(Texture texture){
		mRestoreViewFOrFocusMove.setBackground(texture);
		postInvalidate();
	}

	public void setFocusViewResource(int resourceId ){
		mRestoreViewFOrFocusMove.setBackgroundResource(resourceId);
		postInvalidate();
	}

	public void setFocusViewVisible(boolean visible ){
		mRestoreViewFOrFocusMove.setVisibility(visible);
		postInvalidate();
	}
	public View getFocusView(){
		return mRestoreViewFOrFocusMove;
	}

}
