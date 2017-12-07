package com.x.components.widget;

import android.view.KeyEvent;
import android.view.MotionEvent;

import com.x.Director;
import com.x.opengl.kernel.AnimationHolder;
import com.x.components.node.View;

/**
 * 
 * 
 *
 */
public class LoopListView extends AdapterView<Adapter>{


	protected int mSpace = 315;
//	private int mSpace = 445;
	protected AnimationHolder mAnimationHolder;
	protected float mLeftRightSpeed = 0.30f;
	protected int mCardCount = 7;//必须是单数,才能保证以中间数为分界的两边数目相等
	protected int	mCardIndex = 0;
	protected int	mHalfCount = mCardCount/2;

	
	protected Adapter mAdapter;

	
	protected long	mTime;
	protected OnItemSelectChangeListenner	mOnItemSelectChangeListenner;
	
	public interface OnItemSelectChangeListenner{

		void onSelectPosition(int selectPosition);
		
	} 
	
	public void setOnItemSelectChangeListenner(OnItemSelectChangeListenner l){
		this.mOnItemSelectChangeListenner = l;
	}
	public LoopListView() {
		
		super();

		mAnimationHolder = new AnimationHolder(0, mSpace);
	}

	public int getSize() {
		return mAdapter.getCount();
	}
	
	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {

//		Log.d("onGenericMotionEvent", "onGenericMotionEvent,,"+event.getAction());
		
		return super.onGenericMotionEvent(event);
	}
	

	@Override
	public boolean onKeyEvent(KeyEvent event) {
		
//		Log.d("temp", "LoopListView onKeyEvent "+event.getAction());

		if(mRootScene != null){
			
			if(getChildList().indexOf(mRootScene.getFocusView().getBindView()) == -1){
				return super.onKeyEvent(event);
			}
		}

		boolean down_flag = false;
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			down_flag = true;
		}

		boolean speedAdd = false;
		if(Math.abs(mCardIndex * mSpace + mAnimationHolder.getCurrentPara()) >= 3 * mSpace){
			speedAdd  = true;
		}  ;
		switch (event.getKeyCode())
		{
		case KeyEvent.KEYCODE_DPAD_RIGHT:

			if (down_flag) {
				goAdd(+1,speedAdd);
			
				return true;
			}
			
		break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
		
			if (down_flag) {

				goSub(-1,speedAdd);
				
				return true;	
			}

		break;
		default:
		break;
		}		
		return super.onKeyEvent(event);
	}
	
	@Override
	protected void onItemSelectScrollTo(View v) {
		
	}
	public void goPrevious() {
		if(System.currentTimeMillis() -  mTime< 200){
			return ;
		}
		mTime = System.currentTimeMillis();
		goSub(-1,false);
		
	}

	public void goNext() {
		if(System.currentTimeMillis() -  mTime< 200){
			return ;
		}
		mTime = System.currentTimeMillis();
		goAdd(1,false);
	}

	private void goSub(int i, boolean speedAdd) {
		int count = mAdapter.getCount();
		
		int addPosition = mCardIndex - mHalfCount - 1;
		int addRealId = (addPosition % count + count) % count;

		View addView = mAdapter.getView(addRealId);

		addView.setInfo(new TranslateInfo(addPosition * mSpace,0,0,addPosition,addRealId));

		addView.setTranslate( addPosition * mSpace, 0 , 0 );

		removeChild(getChildCount() - 1);// 移除最后一个
		addChild(0, addView);// 添加新的到第一个位置
		
		mCardIndex--;			
		goAnimtaion();
		Director.getInstance().postInvalidate();

		View centerChild = get(getChildCount() / 2);
		centerChild.requestFocus();
	}

	protected void goAnimtaion() {
		
		if (Math.abs(-mCardIndex * mSpace - mAnimationHolder.getCurrentPara()) > 1.5f * mSpace) {
			mAnimationHolder.setDestinationIndex(-mCardIndex, AnimationHolder.RUNNING, 0.5f, true, false);
		} else {
			mAnimationHolder.setDestinationIndex(-mCardIndex, AnimationHolder.RUNNING, 0.7f, true, true);
		}
		if(mOnItemSelectChangeListenner != null){
			mOnItemSelectChangeListenner.onSelectPosition(getSelectPosition());
		}
	}
	private void goAdd(int i, boolean speedAdd) {
		int count = mAdapter.getCount();
		
		int addPosition = mCardIndex + mHalfCount + 1;
		int addRealId = (addPosition % count + count) % count;

		View addView = mAdapter.getView(addRealId);
		addView.setInfo(new TranslateInfo(addPosition * mSpace,0,0,addPosition,addRealId));

		addView.setTranslate( addPosition * mSpace, 0 , 0 );

		removeChild(0);// 移除第0个
		addChild(addView);// 添加新的到最后一个位置
		mCardIndex++;
		goAnimtaion();
		
		View centerChild = get(getChildCount() / 2);
		centerChild.requestFocus();		
	}

	public void setHorizontalSpace(int space){
		this.mSpace =  space;
	};

	public void setSelectPosition(int position) {
		
		if(Math.abs(mCardIndex - position) > 50){
			setAdapter(mAdapter);
		}
		
		int offset = position - mCardIndex;
		if(offset > 0){
			for (int i = 0; i < offset; i++) {
				goAdd(1, false);
			}
		}else if(offset < 0){

			for (int i = 0; i < Math.abs(offset); i++) {
				goSub(-1, false);
			}
		}
	}

	@Override
	protected void onAnimation() {
		super.onAnimation();
		if(mAnimationHolder != null){
			mAnimationHolder.run(this);
		}
	}
	@Override
	protected void onChildDraw() {

//		Log.d("onChildDraw", "childCount  = "+getChildCount());
		
		int count = getChildCount();
		if(mAnimationHolder.isRunning() ){
			for (int i = 0; i < count; i++) {
				View view = get(i);
				TranslateInfo info = (TranslateInfo) view.getInfo();
				view.setTranslate( info.mTranslateX + mAnimationHolder.getCurrentPara() , 0 , 0 );
			}
		}

		int center = getChildCount() / 2;
		for (int i = 0; i < getChildCount(); i++) {
			if(i != center){
				get(i).draw();
			}
		}
		View centerChild = get(getChildCount() / 2);
		if(centerChild != null){
			centerChild .draw();
		}
		
	}

	/*
	 * 循环列表初始设置适配器
	 */
	public void setAdapter(Adapter adapter) {
		
		
		removeAll();
		
		this.mAdapter = adapter;
		this.mAdapter.setAttachView(this);

		mCardIndex = 0;
		int count = mAdapter.getCount();
		if(count == 0 ){
			return;
		}
		
		for (int i = 0; i < mCardCount; i++) { //让第0项居中
			
			int addPosition = i-mHalfCount ;
			int addRealId = (addPosition % count + count) % count;
			View view = mAdapter.getView(addRealId);
			view.setInfo(new TranslateInfo(addPosition * mSpace,0,0,addPosition,addRealId));

			view.setTranslate( addPosition * mSpace, 0 , 0 );
			
			super.addChild(view);
		}
		
		//检查listView可见的区域可以放置几个childView
		
		mAnimationHolder = new AnimationHolder(0, mSpace);
		mAnimationHolder.setDestinationIndex(-mCardIndex);
		
		
		
		View centerChild = get(getChildCount()/2);
		centerChild.requestFocus();
		
	}
	
	public void requestCenterFocus() {

		View centerChild = get(getChildCount()/2);
		if(centerChild != null){
			centerChild.requestFocus();	
		}
	}

	public int getSelectPosition() {
		
		View view = get(getChildCount()/2);
		TranslateInfo info = (TranslateInfo) view.getInfo();
		return info.mShowIndex;
	}

	

	
}
