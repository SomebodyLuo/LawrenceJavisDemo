package com.x.components.widget.newer;
//package com.wolf.widget.newer;
//
//import com.wolf.node.View;
//
//public class LoopHorizontalGridView extends NewLoopAdapterView{
//
//
//	public void setColumns(int i) {
//		mCardRow = i;
//		mHalfRow = mCardRow / 2;// 行数的一半
//	}
//
//	public void setRows(int i) {
//		mCardColumns = i;
//	}
//
//	@Override
//	public void setHorizontalSpace(int space) {
//		mSpaceY = space;
//	}
//
//	@Override
//	public void setVeticalSpace(int space) {
//		mSpaceX = space;
//	}
//
//	protected float getOffsetX() {
//		return mCardRow % 2 == 0 ? -mSpaceY / 2 : 0;
//	}
//
//	protected float getOffsetY() {
//		return -(mCardColumns - 1) / 2f * mSpaceX;
//	}
//
//	public void setFocusLineDistance(int distance) {
//		if (distance * 2 >= mCardRow) {
//			throw new RuntimeException(
//					"please makesure the FocusLineDistance less than or equals half of carColumns");//It is the tips for user ,and in fact here is carRows
//		}
//		mRowOffsetCount = distance;
//	}
//	
//	@Override
//	protected void initTranslateInfo(View view, int idX, int idY,
//			float offsetX, float offsetY, int cardIndex) {
//
//		
//		view.setInfo(new TwoDTranslateInfo(idY * mSpaceY + offsetX,-idX * mSpaceX - offsetY, idX, idY, cardIndex));
//		
//	}
//
//	@Override
//	protected void updateViewTranslate(View view, TwoDTranslateInfo info,
//			float offset) {
//		view.setTranslate(info.mTranslateX - offset, info.mTranslateY, 0);
//		
//	}
//
//
//	@Override
//	protected void touchDrag(float mTouchOffsetX, float mTouchOfmTouchOffsetYfsetY2) {
//
//		mAdapterAnimationHelper.dragBy(-mTouchOffsetX);
//	}
//
//	@Override
//	protected void touchFling(int velocityX, int velocityY) {
//		mAdapterAnimationHelper.flingTo(-velocityX, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
//	}
//
//	@Override
//	protected void onKeyCodeLeft() {
//		// TODO Auto-generated method stub
//
//		goPreLine(false);
//	}
//
//	@Override
//	protected void onKeyCodeRight() {
//		// TODO Auto-generated method stub
//
//		goNextLine(false);
//
//	}
//
//	@Override
//	protected void onKeyCodeUp() {
//		// TODO Auto-generated method stub
//
//		goPrePosition(true);
//	}
//
//	@Override
//	protected void onKeyCodeDown() {
//		// TODO Auto-generated method stub
//
//		goNextPosition(true);
//	}
//
//}
