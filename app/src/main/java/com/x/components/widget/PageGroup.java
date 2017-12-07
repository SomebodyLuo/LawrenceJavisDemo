package com.x.components.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.x.opengl.kernel.AnimationHolder;
import com.x.components.node.View;
import com.x.components.node.ViewGroup;

/**
 * 
 * 
 *
 */
public class PageGroup extends ViewGroup{


	private int mXSpace = 230;
	private int mYSpace = 205;
	private AnimationHolder mAnimationHolder;
	private int mCardCount = 13;//必须是单数,才能保证以中间数为分界的两边数目相等
	private int	mCardIndex = 0;
	private int	mHalfCount = mCardCount/2;
	
	private int mRows = 2;
	private int mColumns = 5;
	
	
	private Adapter mAdapter;
	private float	mStrollMin;
	private float	mStrollMax;
	private int mFocusIndex = 0 ;
	private HashMap<Integer, View>	mFocusHashMap = new HashMap<Integer, View>();
	

	private int	mPage;
	private int	mPageCount;
	
	protected List<AnimationObject> mAnimationObject = new ArrayList<AnimationObject>();
	
	protected class AnimationObject{

		private int		mIndex;
		private float	mX;
		private float	mY;

		public AnimationObject(float x, float y, int index) {
			
			this.mX = x;
			this.mY = y;
			
			this.mIndex = index;
			
		}
		
	}
	public PageGroup() {
		super();
	}
//	private void goSub(int offset) {
//		
//		for (int i = 0; i < Math.abs(offset); i++) {
//			if(mFocusIndex > 0){
//				
//				int count = mAdapter.getCount();
//				
//				int addCardIndex = mFocusIndex - mHalfCount - 1;
//				int addRealId = (addCardIndex % count + count) % count;
//
//				View addView = mAdapter.getView(addRealId);
//
//				Log.d("position", "addPosition  = "+(addCardIndex));
//				addView.setInfo(new TranslateInfo(addCardIndex * mXSpace,addCardIndex));
//				addView.setTranslate(addCardIndex * mXSpace, 0,0,true,false,false);
//
//				View view = removeChild(getChildCount() - 1);// 移除最后一个
//				TranslateInfo info = (TranslateInfo) view.getInfo();
//				mFocusHashMap.remove(info.mCardIndex);
//				
//				addChild(0, addView);// 添加新的到第一个位置
//				mFocusHashMap.put(addCardIndex, addView);
//				
//
//				mFocusIndex --;
//				Log.d("goSub", "mFocusIndex  = "+(mFocusIndex));
//				if(mCardIndex > 0){
//					mCardIndex--;
//					mAnimationHolder.setDestinationIndex(-mCardIndex,AnimationHolder.RUNNING,0.3f,true,false);
//
//				}
//
//				mFocusHashMap.get(mFocusIndex).requestFocus();
//			}
//		}
//		
//	}
//	private void goAdd(int offset) {
//
//		int count = mAdapter.getCount();
//		for (int i = 0; i < offset; i++) {
//			
//			if(mFocusIndex < count - 1){
//
//				int addCardIndex = mFocusIndex + mHalfCount + 1;
//				int addRealId = (addCardIndex % count + count) % count;
//
//				View addView = mAdapter.getView(addRealId);
//				addView.setInfo(new TranslateInfo(addCardIndex * mXSpace,addCardIndex));
//				Log.d("position", "addPosition  = "+(addCardIndex));
//				addView.setTranslate(addCardIndex * mXSpace, 0,0,true,false,false);
//
//				View view = removeChild(0);// 移除第0个
//				TranslateInfo info = (TranslateInfo) view.getInfo();
//				mFocusHashMap.remove(info.mCardIndex);
//				
//				addChild(addView);// 添加新的到最后一个位置
//				mFocusHashMap.put(addCardIndex,addView);
//				
//
//				mFocusIndex ++;
//				Log.d("goAdd", " mFocusIndex  = "+(mFocusIndex));
//				if(mCardIndex < count - 1){
//					
//					mCardIndex++;
//					mAnimationHolder.setDestinationIndex(-mCardIndex,AnimationHolder.RUNNING,0.2f,true,true);
//				}
//				
//				mFocusHashMap.get(mFocusIndex).requestFocus();;
//			}		
//		}
		
//	}
	@Override
	protected void onChildDraw() {

		

//		float offset22 = mAnimationHolder.getCurrentPara();
//		
//		if(mStrollMin > mStrollMax){
//
//			offset22 = -mStrollMin;
//		}else{
//			
//			if(offset22 > -mStrollMin){
//				offset22 = -mStrollMin;
//			}else if(offset22 < -mStrollMax){
//				offset22 = -mStrollMax;
//			}
//		}
//		
//		
//		int adapterItemCount = mAdapter.getCount();
//		if(mAnimationHolder.isRunning() ){
//			for (int i = 0; i < getChildCount(); i++) {
//				View view = get(i);
//				TranslateInfo info = (TranslateInfo) view.getInfo();
//				if(info.mCardIndex < 0 || info.mCardIndex >= adapterItemCount ){
//					view.setVisibility(false);
//				}else{
//					view.setTranslate(info.mTranslateX + offset22 , 0,0,true,false,false);
//				}
//			}
//		}

		View focusView = null;
		for (int i = 0; i < getChildCount(); i++) {

			View view = get(i);
			if(view.isFocused()){
				focusView = view;
			}else{
				view.draw();
			}
		}
		if(focusView != null){
			focusView.draw();
		}
//		super.onChildDraw();
	}

	/*
	 * 循环列表初始设置适配器
	 */
	public void setAdapter(Adapter adapter) {

		mAnimationObject.clear();
		mFocusHashMap.clear();
		removeAll();
		
		this.mAdapter = adapter;
		this.mAdapter.setAttachView(this);

		this.mFocusIndex = 0;
		
		
	}

	public void startChildAnimation() {
		
//		Log.d("start", "mAnimationObject "+mAnimationObject.size());
		for (int i = 0; i < mAnimationObject.size(); i++) {
			
			AnimationObject animationObject = mAnimationObject.get(i);;
			View view = get(i);
//			view.setRotate(60, 0,  0);
//			view.rotateTo(0, 0, 0);
			
			view.setScale(0.2f, 0, 1);
			view.scaleTo(1, 1, 1);
//			view.scaleTo(1, 1, 1,AnimationHolder.RUNNING,0.20f+(float)Math.random()*0.4f,false,false);
//			view.setTranslate(animationObject.mX, animationObject.mY-400, 0, true,true,false);
//			view.translateTo(animationObject.mX,animationObject.mY, 0,AnimationHolder.RUNNING,0.2f+0.005f*(mAnimationObject.size() - i),true,false);
			
		}
	}
	
//	class TranslateInfo{
//
//		private float	mTranslateX;
//		private float	mTranslateY;
//		private int	mCardIndex;
//
//		public TranslateInfo(float x,float y , int addIndex) {
//			this.mTranslateX = x;
//			this.mTranslateY = y;
//			this.mCardIndex = addIndex;
//		}
//	}
	
	
	public void requestCenterFocus() {

		View centerChild = get(getChildCount()/2);
		centerChild.requestFocus();
	}

	public void setHorizontalSpace(int space ) {
		mXSpace = space;
	}

	public void setVeticalSpace(int space) {
		mYSpace = space;
	}
	public void setRows(int rows){
		this.mRows = rows;
	}
	public void setColumns(int columns){
		this.mColumns = columns;
	}

//	public void setSelectItem(int focusIndex) {
//		
//		if(mFocusIndex > focusIndex){
//			goSub(focusIndex - mFocusIndex);
//		}else if(mFocusIndex < focusIndex){
//			goAdd(focusIndex - mFocusIndex);
//		}else{
//			mFocusHashMap.get(mFocusIndex).requestFocus();
//		}
//	}
	public void setSelectPage(int page) {

		
		this.mAdapter.setAttachView(this);
		
		removeAll();
		mFocusHashMap.clear();
		mAnimationObject.clear();
		
		int count = mAdapter.getCount();
		updatePageCount();
		if(page + 1 > mPageCount){
			page = mPageCount - 1;
		}
		this.mPage = page;

//		Log.d("page", ",,count = "+count + ",, countPage = "+mPageCount + ".,page = "+mPage);
		
		float left = (mColumns - 1) * mXSpace / 2f;
		float top = (mRows - 1) * mYSpace / 2f;;
		
		int startIndex = mPage * (mRows * mColumns); 
		
		for (int i = 0; i < mRows; i++) { //让第0项居中

			float yPosition =   -( i   * mYSpace - top);
			
			for (int j = 0; j < mColumns; j++) {
				float  xPosition =    j  * mXSpace - left;
				
				View view = mAdapter.getView(startIndex);
				view.setInfo(new TwoDTranslateInfo(xPosition ,yPosition,i,j,startIndex));
				view.setTranslate(xPosition, yPosition,0,true,true,false);
				
//				mFocusHashMap.put(addCardIndex,view);
				mAnimationObject.add(new AnimationObject(xPosition,yPosition,startIndex));
				super.addChild(view);
				
				startIndex++;

				if(startIndex > count - 1){
					break;
				}
			}
			if(startIndex > count - 1){
				break;
			}
		}
	}

	public int getCurrentPage() {
		return mPage;
	}

	public int getPageCount() {
		return mPageCount;
	}

	public void goNextPage() {
		
		if(mPage + 1 < mPageCount){
			

			View lastFocusView = mAdapter.getFocusView();
			
			int lastIndex = mColumns - 1;
			if(lastFocusView != null){
				 lastIndex = indexOf(lastFocusView);
			}
			
			setSelectPage(mPage + 1);
			startChildAnimation();
			
			if(lastIndex == mColumns * 2 - 1 && getChildCount() > mColumns){
				get(mColumns).requestFocus();
			}else{
				get(0).requestFocus();
			}
		}
	}

	public void goPreviousPage() {

		if(mPage - 1 >= 0){
			

			View lastFocusView = mAdapter.getFocusView();
			
			int lastIndex = 0;
			if(lastFocusView != null){
				 lastIndex = indexOf(lastFocusView);
			}
			
			setSelectPage(mPage - 1);
			startChildAnimation();
			
			if(lastIndex == mColumns){
				get(getChildCount()-1).requestFocus();
			}else {
				get(mColumns-1).requestFocus();
			}
		}
	}

	public void requestFirstChildFocus() {
		if(getChildCount() > 0){
			get(0).requestFocus();
		}
	}
	public void requestLastChildFocus() {
		if(getChildCount() > 0){
			get(getChildCount()-1).requestFocus();
		}
	}

	public void updatePageCount() {

		int count = mAdapter.getCount();
		
		float temp = count*1f/(mRows * mColumns);
		if(temp - (int)temp == 0){
			this.mPageCount = (int) temp;
		}else{
			this.mPageCount = (int) temp + 1;
		}
		
	}

	public boolean canFinish() {

		View lastFocusView = mAdapter.getFocusView();
		
		int count = mRows * mColumns/2;
		if(count > getChildCount()){
			count = getChildCount();
		}
		
		for (int i = 0; i < count ; i++) {
			View view = get(i);
			if(lastFocusView.equals(view)){
				return true;
			}
		}
		return false;
	}

}
