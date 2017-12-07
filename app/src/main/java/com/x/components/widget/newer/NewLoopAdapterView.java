package com.x.components.widget.newer;
//package com.wolf.widget.newer;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import android.database.DataSetObserver;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.MotionEvent;
//
//import com.wolf.Director;
//import com.wolf.WolfView;
//import com.wolf.kernel.EngineConstanst;
//import com.wolf.node.View;
//import com.wolf.widget.Adapter;
//import com.wolf.widget.AdapterAnimationHelper;
//import com.wolf.widget.newer.NewAdapterView.RecycleBin;
//
//public abstract class NewLoopAdapterView extends NewAdapterView {
//
//	public NewLoopAdapterView() {
//		super();
//		mRowOffsetCount = 0;
//	}
//	@Override
//
//	protected boolean goNextPosition(boolean swapLineFlag) {
//
//		boolean dealEvent = false;
//		mHoldCardIndex++;
//
//		if (mHoldCardIndex % mCardColumns == 0) {
//			if (!swapLineFlag) {
//				mHoldCardIndex--;
//			} else {
//				removeFirstAndAddLast();
//				scrollChange(+1);
//				mCardRowIndex++;
//				dealEvent = true;
//			}
//		} else {
//			dealEvent = true;
//		}
//
//		View view = mFocusFindHashMap.get(mHoldCardIndex);
//		if (view != null) {
//			view.requestFocus();
//		}
//		return dealEvent;
//	}
//
//	protected boolean goPrePosition(boolean swapLineFlag) {
//
//		boolean dealEvent = false;
//		mHoldCardIndex--;
//		
////		Log.d("debug", "(mHoldCardIndex % mCardColumns = "+(mHoldCardIndex % mCardColumns));
//		
//		if ( mHoldCardIndex % mCardColumns == (mCardColumns - 1) ||  mHoldCardIndex % mCardColumns == -1) {
//			if (!swapLineFlag) {
//				mHoldCardIndex++;
//			} else {
//				removeLastAndAddFirst();
//				scrollChange(-1);
//				mCardRowIndex--;
//				dealEvent = true;
//			}
//		} else {
//			dealEvent = true;
//		}
//
//		View view = mFocusFindHashMap.get(mHoldCardIndex);
//		if (view != null) {
//			view.requestFocus();
//		}
//		return dealEvent;
//	}
//
//	protected boolean goPreLine(boolean flingStop) {
//		boolean changeLineSuccuss = false;
//		mHoldCardIndex -= mCardColumns;
//
//		removeLastAndAddFirst();
//		if (flingStop) {
//			flingChange(-1);
//		} else {
//			scrollChange(-1);
//		}
//		mCardRowIndex--;
//		changeLineSuccuss = true;
//
//		mFocusFindHashMap.get(mHoldCardIndex).requestFocus();
//
//		return changeLineSuccuss;
//	}
//
//	protected boolean goNextLine(boolean dragStop) {
//
//		boolean changeLineSuccuss = false;
//
//		mHoldCardIndex += mCardColumns;
//
//		removeFirstAndAddLast();
//		if (dragStop) {
//			flingChange(+1);
//		} else {
//			scrollChange(+1);
//		}
//		mCardRowIndex++;
//		changeLineSuccuss = true;
//
//		mFocusFindHashMap.get(mHoldCardIndex).requestFocus();
//		return changeLineSuccuss;
//	}
//
//	@Override
//	public boolean onWheelEvent(float vscroll) {
//
//		 if (timeLimit(100)) {
//			 return true;
//		 }
//		 return super.onWheelEvent(vscroll);
//	}
//
//
//	@Override
//	public boolean onKeyEvent(KeyEvent event) {
//
//		 if (timeLimit(50)) {
//			 return true;
//		 }
//		return super.onKeyEvent(event);
//	}
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		return super.onTouchEvent(event);
//	}
//
//	
//	@Override
//	protected void onItemSelectScrollTo(View v) {
//
//		TwoDTranslateInfo translateInfo = (com.wolf.widget.AdapterView.TwoDTranslateInfo) v.getInfo();
//		setSelectItem(translateInfo.mShowIndex);
//	}
//	protected void removeLastAndAddFirst() {
//		int removeIdY = mCardRowEndIndex + mHideOutRowCount;
//		for (int j = 0; j < mCardColumns; j++) {
//			int removeIdX = j;
//			int cardIndex = removeIdY * mCardColumns + removeIdX;
//
//				View view = mFocusFindHashMap.get(cardIndex);
//				if (view != null) {
//					removeChild(view);
//					mFocusFindHashMap.remove(cardIndex);
//					mRecycleBin.addScrapView(cardIndex, view);
//				}
//		}
//
//		mCardRowEndIndex -= 1;
//
//		float offestX = getOffsetX();
//		float offestY = getOffsetY();
//		// int ofset = -(int)(mwolfScroller.getCurrentPara() );
//		
//		for (int i = mCardRowStartIndex - 1; i >= mCardRowStartIndex - 1 - mHideOutRowCount; i--) {
//			int addIdY = i;
//			for (int j = mCardColumns - 1; j >= 0; j--) { //  
//				int addIdX = j;
//				int cardIndex = addIdY * mCardColumns + addIdX;
//
//				View view = mFocusFindHashMap.get(cardIndex);
//				if (view == null) {
//						View convertView = mRecycleBin.getScrapView();
//						view = mAdapter.getView((cardIndex%mCount+mCount)%mCount, convertView, null);
//						initListener(view);
//						// FocusInfo focusInfo = new
//						// FocusInfo(view,addIdX,addIdY,cardIndex);
//						// focusInfo.layoutView(l,t,addIdX,addIdY,cardIndex);
////						view.setInfo(new TwoDTranslateInfo(addIdX * mSpaceX + offestX, -addIdY * mSpaceY + offestY, addIdX, addIdY, cardIndex));
//						initTranslateInfo(view,addIdX,addIdY,offestX,offestY,cardIndex);
//						
//						mFocusFindHashMap.put(cardIndex, view);
//						addChild(0, view);
//					}
//
//			}
//		}
//
//		mCardRowStartIndex -= 1;
//	}
//
//	protected void removeFirstAndAddLast() {
//
//		int removeIdY = mCardRowStartIndex - mHideOutRowCount;
//		for (int j = 0; j < mCardColumns; j++) {
//			int removeIdX = j;
//			int cardIndex = removeIdY * mCardColumns + removeIdX;
//			View view = mFocusFindHashMap.get(cardIndex);
//			if (view != null) {
//				removeChild(view);
//				mFocusFindHashMap.remove(cardIndex);
//				mRecycleBin.addScrapView(cardIndex, view);
//			}
//		}
//		mCardRowStartIndex += 1;
//
//		float offestX = getOffsetX();
//		float offestY = getOffsetY();
//		// int ofset = -(int)(mwolfScroller.getCurrentPara() );
//		for (int i = mCardRowEndIndex + 1; i <= mCardRowEndIndex + 1 + mHideOutRowCount; i++) {
//			int addIdY = i;
//			for (int j = 0; j < mCardColumns; j++) {
//				int addIdX = j;
//				int cardIndex = addIdY * mCardColumns + addIdX;
//					// view.setVisibility(View.GONE);
//
//				View view = mFocusFindHashMap.get(cardIndex);
//				if (view == null) {
//
//					View convertView = mRecycleBin.getScrapView();
//					view = mAdapter.getView((cardIndex%mCount+mCount)%mCount, convertView, null);
//					initListener(view);
//
//					initTranslateInfo(view,addIdX,addIdY,offestX,offestY,cardIndex);
////					view.setInfo(new TwoDTranslateInfo(addIdX * mSpaceX + offestX, -addIdY * mSpaceY + offestY, addIdX, addIdY, cardIndex));
//
//					mFocusFindHashMap.put(cardIndex, view);
//					addChild(view);
//				}
//			}
//		}
//
//		mCardRowEndIndex += 1;
//	}
//
//	protected void onMouseTouchScroll() {
//		// setScale(0.3f, 0.3f, 1);
//
//		switch (mAdapterAnimationHelper.getState()) {
//			case AdapterAnimationHelper.DRAG:
//				float offset = -mAdapterAnimationHelper.getCurrentPara() + mSpaceY * mCardRowIndex;
//				if (offset > mSpaceY * 1 / 2f) {
//					goPreLine(true);
//
//				} else if (offset < -mSpaceY * 1 / 2f) {
//					goNextLine(true);
//				}
//				
//			// if (offset > mSpaceY * 1 / 2f /* && mCardIndex > mShowHalfRow */)
//			// {
//			// goPreLine(true);
//			// } else if (offset < -mSpaceY * 1 / 2f/*
//			// * && mCardIndex <
//			// * mAdapter.getCount() - 1 -
//			// * (mShowHalfRow )
//			// */) {
//			// goNextLine(true);
//			// }
//			break;
//			case AdapterAnimationHelper.DRAG_UP:
//
//
//					offset = -mAdapterAnimationHelper.getCurrentPara() + mSpaceY * mCardRowIndex;
//					if (offset > mSpaceY * 1 / 2f) {
//						goPreLine(true);
//
//					} else if (offset < -mSpaceY * 1 / 2f) {
//						goNextLine(true);
//
//					} else {
//						// mAdapterAnimationHelper.setState(AdapterAnimationHelper.DRAG_UP_WAIT);
//						// mAdapterAnimationHelper.setState(AdapterAnimationHelper.DRAG_STOP_ATTRACT);
//					}
//			break;
//			case AdapterAnimationHelper.DRAG_UP_TO_ATTRACT:
//
//				goDragStopAttractAnimation();
//				mAdapterAnimationHelper.setState(AdapterAnimationHelper.DEFAULT);
//				postInvalidate();
//			// goDragStopAttractAnimation();
//			// mwolfScroller.setState(wolfScroller.DEFAULT);
//			break;
//
//			default:
//			break;
//		}
//	}
//	protected void onUpdateChildProperty() {
//		float offset = mAdapterAnimationHelper.getCurrentPara();
//		for (int i = 0; i < getChildCount(); i++) {
//			View view = get(i);
//			TwoDTranslateInfo info = (TwoDTranslateInfo) view.getInfo();
////			if (info.mShowIndex < 0 || info.mShowIndex >= adapterItemCount) {
////				view.setVisibility(false);
////			} else {
////				view.setTranslate(info.mTranslateX, info.mTranslateY + offset, 0);
////			}
//			updateViewTranslate(view, info, offset);
////			view.setTranslate(info.mTranslateX, info.mTranslateY + offset, 0);
//		}
//	}
//
//	private MyDataSetObserver	mMyDataSetObserver;
//
//	class MyDataSetObserver extends DataSetObserver {
//		@Override
//		public void onChanged() {
//			updateUIbyDataSetChanged();
//			super.onChanged();
//		}
//
//		@Override
//		public void onInvalidated() {
//			updateUIbyDataSetChanged();
//			super.onInvalidated();
//		}
//	}
//
//	private void updateUIbyDataSetChanged() {
//		// Log.d("debug", "updateUIbyDataSetChanged ");
//		setAdapter(mAdapter);
//	}
//
//	/*
//	 * 循环列表初始设置适配器
//	 */
//	public void setAdapter(Adapter adapter) {
//
//		if (mAdapter != null && mMyDataSetObserver != null) {
//			mAdapter.unregisterDataSetObserver(mMyDataSetObserver);
//		}
//		mFocusFindHashMap.clear();
//		mRecycleBin.clear();
//		removeAll();
//
//		this.mAdapter = adapter;
//		mMyDataSetObserver = new MyDataSetObserver();
//		mAdapter.registerDataSetObserver(mMyDataSetObserver);
//
//		int adapterItemCount = mAdapter.getCount();
//		if (adapterItemCount == 0) {
//			return;
//		}
//		mCardRowIndex = 0;
//		mHoldCardIndex = 0;
//
//		initProperties();
//		mAdapterAnimationHelper.init(mScrollIndex, mSpaceY);
//
//		float offestX = getOffsetX();
//		float offestY = getOffsetY();
//		for (int i = mCardRowStartIndex - mHideOutRowCount; i <= mCardRowEndIndex + mHideOutRowCount; i++) {
//			int idY = i;
//			for (int j = 0; j < mCardColumns; j++) {
//				int idX = j;
//				int cardIndex = idY * mCardColumns + idX;
////				if (cardIndex < mAdapter.getCount() && cardIndex >= 0) {
//					// view.setVisibility(View.GONE);
//					View view = mAdapter.getView((cardIndex%mCount+mCount)%mCount, null, null);
//					if (cardIndex == 0) {
//						view.requestFocus();
//					}
//					initListener(view);
//					initTranslateInfo(view,idX,idY,offestX,offestY,cardIndex);
//					
//
//					mFocusFindHashMap.put(cardIndex, view);
//					super.addChild(view);
//
////				}
//			}
//		}
//
//		
//		// 检查listView可见的区域可以放置几个childView
//
//		// View centerChild = get(getChildCount()/2);
//		// centerChild.requestFocus();
//		// firstChildView.requestFocus();
//	}
//
//	private void initListener(View view) {
//
//		view.setOnWidgetItemListener(mOnWidgetItemListener);
//	}
//
//	private void initProperties() {
//
//		Log.d("debug", "normal mCount = " + mCount + ",,mTotalRowCount = " + mTotalRowCount);
//		Log.d("debug", "normal mCardRowStartIndex = " + mCardRowStartIndex + ",,mCardRowIndex = " + mCardRowIndex + ",,mCardRowEndIndex = " + mCardRowEndIndex);
//		Log.d("debug", "normal mHoldCardIndex = " + mHoldCardIndex + ",,mScrollIndex = " + mScrollIndex);
//
//		actToInitValue();
//		Log.d("debug", "----------------------------------");
//		Log.d("debug", "normal mCount = " + mCount + ",,mTotalRowCount = " + mTotalRowCount);
//		Log.d("debug", "normal mCardRowStartIndex = " + mCardRowStartIndex + ",,mCardRowIndex = " + mCardRowIndex + ",,mCardRowEndIndex = " + mCardRowEndIndex);
//		Log.d("debug", "normal mHoldCardIndex = " + mHoldCardIndex + ",,mScrollIndex = " + mScrollIndex);
//		arrowListenerCallBack();
//	}
//
//	private void arrowListenerCallBack() {
//		// if(mArrowListenner != null){
//		// int leftIndex = mScrollIndex;
//		// int rightIndex = mScrollIndex + mCardRow - 1;
//		// if(leftIndex > 0){
//		// mArrowListenner.leftArrowShow(true);
//		// }else{
//		// mArrowListenner.leftArrowShow(false);
//		// }
//		// if(rightIndex < getTotalRowNumber() - 1){
//		// mArrowListenner.rightArrowShow(true);
//		// }else{
//		// mArrowListenner.rightArrowShow(false);
//		// }
//		// }
//	}
//
//	protected void actToInitValue() {
//		int destHoldCardIndex = mCardRow / 2 * mCardColumns;
//		int adapterItemCount = mAdapter.getCount();
//		if (destHoldCardIndex > adapterItemCount - 1) {
//			if (destHoldCardIndex > 0) {
//				destHoldCardIndex -= 1;
//			}
//		}
//		;
//		// int destScrollIndex = mCardRow / 2;
//		int destScrollIndex = 0;
//		int cardRowStartIndex = 0;
////		int cardRowEndIndex = cardRowStartIndex + mCardRow - 1;
//		int cardRowEndIndex = cardRowStartIndex  ;
//		mHideOutRowCount = mCardRow/2;
//		int rowCount = (int) Math.ceil(adapterItemCount * 1f / mCardColumns);
//		;
//
//		int index = 0;
//		int rowIndex = 0;
//		// go to CenterIndex
//		for (int i = 0; i < destHoldCardIndex; i++) {
//			if (index < adapterItemCount - 1) {
//				index++;
//				if (index % mCardColumns == 0) {
//					if (rowIndex < rowCount - 1) {
//						if (rowIndex + mRowOffsetCount == cardRowEndIndex && rowIndex < rowCount - 1 - (mRowOffsetCount)) {
//							cardRowStartIndex += 1;
//							cardRowEndIndex += 1;
//							destScrollIndex += 1;
//						}
//						rowIndex++;
//					}
//				}
//			}
//		}
//		// go back to Zero
//		for (int i = 0; i < destHoldCardIndex; i++) {
//
//			if (index > 0) {
//				index--;
//				if (index % mCardColumns == (mCardColumns - 1)) {
//					if (rowIndex > 0) {
//						if (rowIndex - mRowOffsetCount == cardRowStartIndex && rowIndex > (mRowOffsetCount)) {
//							cardRowStartIndex -= 1;
//							cardRowEndIndex -= 1;
//							destScrollIndex -= 1;
//						}
//						rowIndex--;
//					}
//				}
//			}
//		}
//
//		mCount = adapterItemCount;
//		mTotalRowCount = rowCount;
//		mActiveCardCount = mCardColumns * (mCardRow + mHideOutRowCount * 2);
//
//		mCardRowStartIndex = cardRowStartIndex;
//		mCardRowIndex = rowIndex;
//		mCardRowEndIndex = cardRowEndIndex;
//
//		mHoldCardIndex = index;
//		mScrollIndex = destScrollIndex;
//	}
//
//
//	public void setSelectItem(int focusIndex) {
//
////		if (focusIndex > mAdapter.getCount() - 1) {
////			focusIndex = mAdapter.getCount() - 1;
////		}
//
//		if (focusIndex > mHoldCardIndex) {
//
//			int moveCount = focusIndex - mHoldCardIndex;
//			for (int i = 0; i < moveCount; i++) {
//				goNextPosition(true);
//			}
//		} else if (focusIndex < mHoldCardIndex) {
//
//			int moveCount = mHoldCardIndex - focusIndex;
//			for (int i = 0; i < moveCount; i++) {
//				goPrePosition(true);
//			}
//		} else {
//			View view = mFocusFindHashMap.get(mHoldCardIndex);
//			if (view != null) {
//				view.requestFocus();
//			}
//		}
//	};
//
//	
//}
