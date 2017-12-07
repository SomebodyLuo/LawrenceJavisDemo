package com.x.components.widget;

import java.util.HashMap;
import java.util.Map;

import android.view.KeyEvent;
import android.view.MotionEvent;

import com.x.opengl.kernel.AnimationHolder;
import com.x.opengl.kernel.EngineConstanst;
import com.x.components.node.View;

/**
 * 
 * 
 *
 */
public class NoLoopGridView  extends AdapterView<Adapter>{

	
	protected int mSpaceX = 305; // X方向的每个child之间的间距，即列间距
	protected int mSpaceY = 275; //Y 轴上每个child的间距，即行间距
	protected AnimationHolder mAnimationHolder = new AnimationHolder(0, 1);
	protected boolean mLimitMin = true; 
	protected boolean mLimitMax = true;
	protected int mCardRow = 5;//必须是单数,才能保证以中间数为分界的两边数目相等
	protected int	mHalfRow = mCardRow/2;//行数的一半
	protected   int	mShowHalfRow = 1;
	
	protected int mCardColumns = 5 ;//列数
	protected int	mCardRowIndex = 0;//指示第几行的索引
	

	protected boolean mLazy = false; //列表滚动模式，如果该标识为true,,则启用懒汉模式，即只有当焦点到达底部或者顶部才给以滚动列表，平时不动
	protected Adapter mAdapter;
	
	protected Map<Integer, View>  mFocusFindHashMap = new HashMap<Integer, View>();
	protected int	mHoldCardIndex;
	protected long	mTime;
	protected OnCenterLineChangeListenner	mCenterLineChangeListenner;
	protected OnFocusRowChangeListenner		mOnFocusRowChangeListenner;
	protected int	mTotalRowNumber;
	
	public interface OnCenterLineChangeListenner{
		void onSelectLine(int lineIndex);
	}
	public interface OnFocusRowChangeListenner{
		void onFocusLine(int focusLineIndex);
	}
	
	public void setOnCenterLineChangeListenner(OnCenterLineChangeListenner l){
		mCenterLineChangeListenner = l;
	}
	public void setOnFocusRowChangeListenner(OnFocusRowChangeListenner l){
		mOnFocusRowChangeListenner = l;
	}
	
	public NoLoopGridView() {
		super();

//		int count = Math.round(this.getWidth() / mList.get(0).getWidth() +0.5f); 
//		count = count > mList.size() ? mList.size() : count;
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
	private float	mTouchScrollRate = 0.93f; //触摸滚动的惯性转化率
	private float	mBaseOffsetY;
	protected	float mNormalSpeed = 0.5f;
	protected	float mQuicklySpeed = 0.7f;
	protected	float mDragSpeed = 0.3f;
	
	@Override
	public boolean onInterceptTouchevent() {
		
		
		return super.onInterceptTouchevent();
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		if(event.getAction() == MotionEvent.ACTION_DOWN){

			mLastTouchState = DOWN;
			this.mTouchOffsetX = 0;
			this.mTouchOffsetY = 0;
			this.mTouchX = event.getX();
			this.mTouchY = event.getY();
			this.mAllTouchOffsetX = 0;
			this.mAllTouchOffsetY = 0;
			this.mIntercept = false;

			if(mAnimationHolder.getState() == AnimationHolder.DRAG_STOP || mAnimationHolder.getState() == AnimationHolder.DRAG_STOP_ATTRACT){
				return true;
			}
			
		}else if( event.getAction() == MotionEvent.ACTION_MOVE){

			
			float offsetX = event.getX() - this.mTouchX;
			float offsetY = event.getY() - this.mTouchY;
			
//			if(mTouchOffsetX * offsetX < 0){
//				mAnimationHolder.dragReset();
//			}
			if(mTouchOffsetY * offsetY < 0){
				mAnimationHolder.dragReset();
			}
			
			this.mTouchOffsetX = offsetX;
			this.mTouchOffsetY = offsetY;
			
			this.mAllTouchOffsetX += this.mTouchOffsetX;
			this.mAllTouchOffsetY += this.mTouchOffsetY;
			
			float distan = Math.abs(mAllTouchOffsetY);
			if( distan   > EngineConstanst.REFERENCE_LIST_MOVE_TINGLE){//若在手指抖动误差值范围内则仍然生效
				mIntercept = true;
			}
			mAnimationHolder.drag(-mTouchOffsetY);
			postInvalidate();
//			if(Math.abs(mTouchOffsetX * 2) > EngineConstanst.REFERENCE_MOVE_CAN_ADD){
//				mAnimationHolder.drag(mTouchOffsetX * 2);
//				postInvalidate();
//			}else{
//				mAnimationHolder.drag(1);
//			}
			
			this.mTouchX = event.getX();
			this.mTouchY = event.getY();

//			if(mLastTouchState == DOWN){
				if(mTouchOffsetX > 0){
//					changeRight();
				}else if(mTouchOffsetX < 0){
//					changeLeft();
				}
//			}
			mLastTouchState = MOVE;
			if(mIntercept){
				return true;
			}
//			Log.d("drag", "mTouchOffsetX = " + mTouchOffsetX);
		}else {

			if(mLastTouchState == MOVE){
				mAnimationHolder.dragStop(mTouchScrollRate );
				postInvalidate();
			}
			mLastTouchState = UP;
			this.mTouchX = event.getX();
			this.mTouchY = event.getY();
			if(mIntercept){
				return true;
			}
			
		}
		
		return super.dispatchTouchEvent(event);
	}
	@Override
	public boolean onKeyEvent(KeyEvent event) {
		
//		Log.d("temp", "NoLoopGridView onKeyEvent "+event.getAction());
		boolean down_flag = false;
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			down_flag = true;
		}
		
		if(mRootScene != null){
			
//			if(getChildList().indexOf(mRootScene.getFocusView().getBindView()) == -1){
//				return super.onKeyEvent(event);
//			}
			if(getChildCount()  == 0){
				return super.onKeyEvent(event);
			}
		}

		switch (event.getKeyCode())
		{

		case KeyEvent.KEYCODE_DPAD_LEFT:
			
			if(down_flag){
				
				goPrePosition();
				
				return true;
			}
	    break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
				
			if(down_flag){

				goNextPosition();
				
				return true;
			}
		break;
		case KeyEvent.KEYCODE_DPAD_DOWN:

				if (down_flag) {

					goNextLine(false);
				
					return true;
				}
					
		break;
		case KeyEvent.KEYCODE_DPAD_UP:

				if (down_flag) {

					goPreLine(false);
					
					return true;
				}
		break;
		default:
			
		break;
		}
		
		return super.onKeyEvent(event);
	}

	protected void goNextPosition() {
		
		int adapterItemCount = mAdapter.getCount();
		
		if(mHoldCardIndex < adapterItemCount - 1){
				mHoldCardIndex ++;
				if(mHoldCardIndex % mCardColumns == 0){

					int rowCount = adapterItemCount % mCardColumns == 0 ? adapterItemCount/mCardColumns : adapterItemCount/mCardColumns + 1;//往上取整
//						
					if(mCardRowIndex < rowCount - 1){

						removeFirstAndAddLast(); 
						mCardRowIndex++;
						setDestination() ;
					}

				}
				mFocusFindHashMap.get(mHoldCardIndex).requestFocus();;
		}
	}

	protected void goPrePosition() {
		
		if(mHoldCardIndex > 0){
			mHoldCardIndex --;
			if(mHoldCardIndex % mCardColumns == mCardColumns - 1){
				
				if(mCardRowIndex > 0){

					removeLastAndAddFirst();
					mCardRowIndex--;
					setDestination() ;
				}
			}
			mFocusFindHashMap.get(mHoldCardIndex).requestFocus();;
		}
	}

	private boolean timeLimit(float limitTime) {
		
		if(System.currentTimeMillis() -  mTime< limitTime){
			return true;
		}
		mTime = System.currentTimeMillis();
		return false;
	}
	@Override
	public boolean onWheelEvent(float vscroll) {

		if(timeLimit(100)){
			return true;
		}
		if(vscroll > 0){
			
			if(vscroll > 1){
				vscroll = 1;
			}
			for (int i = 0; i < vscroll; i++) {
				goPreLine(false);
			}
			return true;
			
		}else if(vscroll < 0){
			
			if(vscroll < -1){
				vscroll = -1;
			}
			for (int i = 0; i < -vscroll; i++) {
				goNextLine(false);
			}
			return true;
			
		}
		
		return super.onWheelEvent(vscroll);
	}
	
	private void goPreLine(boolean dragStop) {
		if(mHoldCardIndex - mCardColumns >= 0){
			mHoldCardIndex -= mCardColumns;

				if(mCardRowIndex > 0){

					removeLastAndAddFirst();
					mCardRowIndex--;
					if(!dragStop){
						setDestination() ;
					}
				}

			mFocusFindHashMap.get(mHoldCardIndex).requestFocus();;
		}
	}

	private void goNextLine(boolean dragStop) {
		int adapterItemCount = mAdapter.getCount();
		int rowCount = adapterItemCount % mCardColumns == 0 ? adapterItemCount/mCardColumns : adapterItemCount/mCardColumns + 1;//往上取整
			
		if(mHoldCardIndex + mCardColumns < adapterItemCount - 1){
				mHoldCardIndex += mCardColumns;

					if(mCardRowIndex < rowCount - 1){

						removeFirstAndAddLast();
						mCardRowIndex++;
						if(!dragStop){
							setDestination() ;
						}
					}

				mFocusFindHashMap.get(mHoldCardIndex).requestFocus();;
		}else {

			int rowIndex = (mHoldCardIndex+1) % mCardColumns == 0 ? (mHoldCardIndex+1)/mCardColumns : (mHoldCardIndex+1)/mCardColumns + 1;//往上取整
			rowIndex --;//当前所在行
			
			
			if(rowIndex == rowCount-1){//如果当前行已经是最后一行，则什么也不做
				
			}else{
				
				mHoldCardIndex = adapterItemCount - 1;
				
				if(mCardRowIndex < rowCount - 1){
					removeFirstAndAddLast();
					mCardRowIndex++;
					if(!dragStop){
						setDestination() ;
					}
				}
				mFocusFindHashMap.get(mHoldCardIndex).requestFocus();;
				
			}
			
		}
	}

	public void setLazyScrollMode(boolean flag){
		mLazy = flag;
	};
	
	protected void setDestination() {
		int lineIndex = 0;
		if(mLazy){
			int lastDestinationIndex = (int)(Math.round(mAnimationHolder.getDestinationIndex()));

			int newIndex = lastDestinationIndex;
			if(newIndex < mShowHalfRow){
				newIndex = mShowHalfRow;
			}else	if(mCardRowIndex - lastDestinationIndex < -mShowHalfRow){
				newIndex -= 1;
			}else if(mCardRowIndex - lastDestinationIndex > mShowHalfRow){
				newIndex += 1;
			}
			int rowCount = getTotalRowNumber();
			if(rowCount >= mShowHalfRow * 2 +1 ){
				if (newIndex < mShowHalfRow) {
					newIndex = mShowHalfRow;
				}else if (newIndex >= rowCount - mShowHalfRow - 1) {
					newIndex = rowCount - mShowHalfRow - 1;
				}
			}
			if(Math.abs(newIndex * mSpaceY - mAnimationHolder.getCurrentPara()) > 1.5f * mSpaceY){
				mAnimationHolder.setDestinationIndex(newIndex,AnimationHolder.RUNNING,mQuicklySpeed,mLimitMin,mLimitMax);
			}else{
				mAnimationHolder.setDestinationIndex(newIndex,AnimationHolder.RUNNING,mNormalSpeed,mLimitMin,mLimitMax);
			}
			lineIndex = newIndex;
			
		}else{
			
			int rowCount = mAdapter.getCount() % mCardColumns == 0 ? mAdapter.getCount()/mCardColumns : mAdapter.getCount()/mCardColumns + 1;//往上取整

			int newIndex = mCardRowIndex;
			if(newIndex <= 1){
				newIndex = 1;
			}else if(newIndex >= rowCount - 1){
				newIndex = rowCount -2 ;
			}
			if(Math.abs(mCardRowIndex * mSpaceY - mAnimationHolder.getCurrentPara()) > 1.5f * mSpaceY){
				mAnimationHolder.setDestinationIndex(newIndex,AnimationHolder.RUNNING,mQuicklySpeed,mLimitMin,mLimitMax);
			}else{
				mAnimationHolder.setDestinationIndex(newIndex,AnimationHolder.RUNNING,mNormalSpeed,mLimitMin,mLimitMax);
			}
			lineIndex = newIndex;
		}

		if(mOnFocusRowChangeListenner != null){
			mOnFocusRowChangeListenner.onFocusLine(mCardRowIndex);
		}
		if(mCenterLineChangeListenner != null){
			mCenterLineChangeListenner.onSelectLine(lineIndex);
		}
	}

	public int getTotalRowNumber() {
		mTotalRowNumber = mAdapter.getCount() % mCardColumns == 0 ? mAdapter.getCount()/mCardColumns : mAdapter.getCount()/mCardColumns + 1;
		return 	mTotalRowNumber;//往上取整
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}
	private void removeLastAndAddFirst() {

		int adapterItemCount = mAdapter.getCount();
		
		for (int j = 0; j < mCardColumns; j++) {// 移除最后一行
			View view = removeChild(getChildCount() - 1);
			TwoDTranslateInfo info = (TwoDTranslateInfo) view.getInfo();
			mFocusFindHashMap.remove(info.mShowIndex); //同时移除HashMap中的key value;
		}

		float offestX = getBaseOffsetX();;
		float offestY = getBaseOffsetY();;
		int addIdY = mCardRowIndex - mHalfRow - 1;
		for (int j = mCardColumns - 1; j >= 0; j--) { //在最前面添加新行
			int addIdX = j ;
//			int addRealId = addIdY * mCardColumns + addIdX;
//			addRealId = (addRealId % adapterItemCount + adapterItemCount) % adapterItemCount;

			int cardIndex = addIdY * mCardColumns + addIdX;

			View view = getView(addIdX,addIdY,cardIndex,offestX,offestY);

			mFocusFindHashMap.put(cardIndex, view);//添加进hashMap
			super.addChild(0,view);
		}
	}

	protected float getBaseOffsetX() {
		return - (mCardColumns-1) / 2f * mSpaceX ;
	}
	protected float getBaseOffsetY() {
		return mBaseOffsetY ;
	}
	
	public void setBaseOffsetY(float baseY) {
		this.mBaseOffsetY = baseY;
	}

	@Override
	protected void onItemSelectScrollTo(View v) {
		
		TwoDTranslateInfo translateInfo = (TwoDTranslateInfo) v.getInfo();
		setSelectItem(translateInfo.mShowIndex);
	}
	private void removeFirstAndAddLast() {
		

		int adapterItemCount = mAdapter.getCount();
		
		for (int j = 0; j < mCardColumns; j++) {// 移除第0行
			View view = removeChild(0);
			TwoDTranslateInfo info = (TwoDTranslateInfo) view.getInfo();
			mFocusFindHashMap.remove(info.mShowIndex); //同时移除HashMap中的key value;
		}

		float offestX = getBaseOffsetX();;
		float offsetY = getBaseOffsetY();
		int addIdY = mCardRowIndex + mHalfRow + 1;
		for (int j = 0; j < mCardColumns; j++) { //在末尾添加新行
			int addIdX = j ;
//			int addRealId = addIdY * mCardColumns + addIdX;
//			addRealId = (addRealId % adapterItemCount + adapterItemCount) % adapterItemCount;

			int cardIndex = addIdY * mCardColumns + addIdX;
			View view = getView(addIdX,addIdY,cardIndex,offestX,offsetY);
			
			mFocusFindHashMap.put(cardIndex, view);//添加进hashMap
			
			super.addChild(view);
		}
	}

	private void  findFocusByUpDown(int flag) {

		if(mRootScene != null){
			
			View bindView = mFocusFindHashMap.get(mHoldCardIndex);
			TwoDTranslateInfo info = (TwoDTranslateInfo) bindView.getInfo();
			if(flag == 1){
	
				int adapterItemCount = mAdapter.getCount();
				int rowCount = adapterItemCount % mCardColumns == 0 ? adapterItemCount/mCardColumns : adapterItemCount/mCardColumns + 1;//往上取整

				int nextIndex = info.mShowIndex + mCardColumns;
				
				int nextRow = nextIndex  % mCardColumns == 0 ? nextIndex /mCardColumns : nextIndex /mCardColumns + 1;//往上取整
				
				if(nextRow < rowCount - 1){//其他行
					View view = mFocusFindHashMap.get(nextIndex);
					view.requestFocus();
					mHoldCardIndex = nextIndex;
				}else{ //如果是最后一行
					
					if(nextIndex < adapterItemCount){

						View view = mFocusFindHashMap.get(nextIndex);
						view.requestFocus();
						mHoldCardIndex = nextIndex;
					}else{

						View view = mFocusFindHashMap.get(adapterItemCount-1);
						view.requestFocus();
						mHoldCardIndex = adapterItemCount-1;
					}	
					
					
				}
				
			}else if(flag == -1){

				int nextIndex = info.mShowIndex - mCardColumns;
				if(nextIndex >= 0){
					View view = mFocusFindHashMap.get(nextIndex);
					view.requestFocus();
					mHoldCardIndex = nextIndex;
				}
			}
		}
		
	}

	@Override
	public void draw() {
		super.draw();
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

		
		
		onMouseTouchScroll();
		onUpdateChildProperty();
		///////////
		View focusView = mFocusFindHashMap.get(mHoldCardIndex);
		for (int i = 0; i < getChildCount(); i++) {
			View view = get(i);
			if(view != focusView){
				view.draw();
			}
		}
		if(focusView != null){
			focusView.draw();;
		}
		
	}
	protected void onMouseTouchScroll() {
		if(mAnimationHolder.isRunning()){
//			setScale(0.3f, 0.3f, 1);
			if(mAnimationHolder.getState() == AnimationHolder.DRAG){
				
				float offset = -mAnimationHolder.getCurrentPara() +  mSpaceY * mCardRowIndex;
				if(offset > mSpaceY*1/2f /*&& mCardIndex > mShowHalfRow */){
					goPreLine(true);
				}else if(offset < -mSpaceY*1/2f/* && mCardIndex < mAdapter.getCount() - 1 - (mShowHalfRow )*/){
					goNextLine(true);
				} 
				
			}else if(mAnimationHolder.getState() == AnimationHolder.DRAG_STOP){
				
				int adapterItemCount = mAdapter.getCount();
				int rowCount = adapterItemCount % mCardColumns == 0 ? adapterItemCount/mCardColumns : adapterItemCount/mCardColumns + 1;//往上取整
			
				if(mCardRowIndex <= 0  ){
				
					mAnimationHolder.mState = AnimationHolder.DRAG_STOP_ATTRACT;
					
				}else if(mCardRowIndex >= rowCount - 1 - (0 )){
					
					mAnimationHolder.mState = AnimationHolder.DRAG_STOP_ATTRACT;
					
				}else{
					float offset = -mAnimationHolder.getCurrentPara() +  mSpaceY * mCardRowIndex;
					if(offset > mSpaceY*1/2f/* && mCardIndex > mShowHalfRow */ ){
						goPreLine(true);

					}else if(offset < -mSpaceY*1/2f/* && mCardIndex < mAdapter.getCount() - 1 - (mShowHalfRow )*/){
						goNextLine(true);
						
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
		
		int adapterItemCount = mAdapter.getCount();
		int rowCount = adapterItemCount % mCardColumns == 0 ? adapterItemCount/mCardColumns : adapterItemCount/mCardColumns + 1;//往上取整
	
		int lineIndex = 0;
		int newIndex = mCardRowIndex;
		if(rowCount < 2 * mShowHalfRow + 1){ //如果总个数小于
			newIndex = mShowHalfRow;
		}else{

			if (newIndex <= mShowHalfRow) {
				newIndex = mShowHalfRow;
			} else if (newIndex >= rowCount - mShowHalfRow - 1) {
				newIndex = rowCount - mShowHalfRow - 1;
			}
		}
		if (Math.abs(newIndex * mSpaceY - mAnimationHolder.getCurrentPara()) > 1.5f * mSpaceY) {
			mAnimationHolder.setDestinationIndex(newIndex, AnimationHolder.RUNNING, mDragSpeed, true, false);
		} else {
			mAnimationHolder.setDestinationIndex(newIndex, AnimationHolder.RUNNING, mDragSpeed, true, true);
		}

		lineIndex = newIndex;


		if(mOnFocusRowChangeListenner != null){
			mOnFocusRowChangeListenner.onFocusLine(mCardRowIndex);
		}
		if(mCenterLineChangeListenner != null){
			mCenterLineChangeListenner.onSelectLine(lineIndex);
		}
	}

	protected void onUpdateChildProperty() {
		if(mAnimationHolder.isRunning() ){
			float offset = mAnimationHolder.getCurrentPara();
			int adapterItemCount = mAdapter.getCount();
			for (int i = 0; i < getChildCount(); i++) {
				View view = get(i);
				TwoDTranslateInfo info = (TwoDTranslateInfo) view.getInfo();
				if(info.mShowIndex < 0 || info.mShowIndex >= adapterItemCount ){
					view.setVisibility(false);
				}else{
					view.setTranslate( info.mTranslateX,info.mTranslateY + offset  , 0);
				}
//						view.setTranslate( info.mTranslateX,info.mTranslateY + mAnimationHolder.getCurrentPara()  , 0);
//						view.setTranslate( info.mTranslateX,info.mTranslateY + offset  , 0);
			}
			
		}
	}
	/*
	 * 循环列表初始设置适配器
	 */
	public void setAdapter(Adapter adapter) {


		mFocusFindHashMap.clear();
		removeAll();
		
		this.mAdapter = adapter;
		this.mAdapter.setAttachView(this);
		
		mCardRowIndex = 0;
	
		mAnimationHolder = new AnimationHolder(1, mSpaceY);
		mAnimationHolder.setDestinationIndex(1,AnimationHolder.RUNNING,1,true,false);
	
		
		int adapterItemCount = mAdapter.getCount();
		if(adapterItemCount == 0){
			return;
		}
		
		float offestX = getBaseOffsetX();
		float offestY = getBaseOffsetY();
		for (int i = 0; i < mCardRow; i++) { //让第0行居中
			int idY = i-mHalfRow ;
			for (int j = 0; j < mCardColumns; j++) { //计算列
				int idX = j ;
				int cardIndex = idY * mCardColumns + idX;

//				int addRealId = idY * mCardColumns + idX;
//				addRealId = (addRealId % adapterItemCount + adapterItemCount) % adapterItemCount;
//				Log.d("getView", "cardIndex "+cardIndex);
				View view = getView(idX,idY,cardIndex,offestX,offestY);
				
				if(cardIndex == 0){
					view.requestFocus();
				}
				
				mFocusFindHashMap.put(cardIndex, view);
				
				super.addChild(view);
			}
			
		}
		
		mTotalRowNumber = mAdapter.getCount() % mCardColumns == 0 ? mAdapter.getCount()/mCardColumns : mAdapter.getCount()/mCardColumns + 1;
		
		//检查listView可见的区域可以放置几个childView
		
		
//		View centerChild = get(getChildCount()/2);
//		centerChild.requestFocus();
//		firstChildView.requestFocus();
		mHoldCardIndex = 0 ;
		
	}

	protected View getView(int idX, int idY, int cardIndex, float baseOffestX, float baseOffsetY) {
		View view = mAdapter.getView(cardIndex);
		view.setOnWidgetItemListener(mOnWidgetItemListener);
		view.setInfo(new TwoDTranslateInfo(idX * mSpaceX + baseOffestX,-idY * mSpaceY + baseOffsetY,idX,idY,cardIndex));
		view.setTranslate( idX * mSpaceX + baseOffestX, -idY * mSpaceY, 0);
		return view;
	}
	public void setHorizontalSpace(int space) {
		this.mSpaceX = space;
	}
	public void setVeticalSpace(int space){
		this.mSpaceY = space;
	}

	public void setColumns(int i) {
		mCardColumns = i;
	}
	public void setRows(int i) {
		mCardRow = i;
		mHalfRow = mCardRow/2;//行数的一半
	}
	public void setTouchScrollRate(float t){
		this.mTouchScrollRate = t;
	}

	public View getSelectItem() {
		return mFocusFindHashMap.get(mHoldCardIndex);
	}
	public int getSelectIndex(){
		return mHoldCardIndex;
	};
	public void setSelectItem(int focusIndex){
		
		if(focusIndex >  mAdapter.getCount() - 1){
			focusIndex = mAdapter.getCount() - 1;
		}
		
		if(focusIndex > mHoldCardIndex){

			int moveCount = focusIndex - mHoldCardIndex;
			for (int i = 0; i < moveCount; i++) {
				goNextPosition();
			}
		}else if(focusIndex < mHoldCardIndex){

			int moveCount = mHoldCardIndex - focusIndex;
			for (int i = 0; i < moveCount; i++) {
				goPrePosition();
			}
		}else{
			View view = mFocusFindHashMap.get(mHoldCardIndex);
			if (view != null) {
				view.requestFocus();
			}
		}
	};
	
}
