package com.x.components.widget.newer;
//package com.wolf.widget.newer;
//
//import com.wolf.node.View;
//
//public class LoopGridView extends NewLoopAdapterView{
//
//	public void setColumns(int i) {
//		mCardColumns = i;
//	}
//
//	public void setRows(int i) {
//		mCardRow = i;
//		mHalfRow = mCardRow / 2;// 行数的一半
//	}
//
//	public void setHorizontalSpace(int space) {
//		this.mSpaceX = space;
//	}
//
//	public void setVeticalSpace(int space) {
//		this.mSpaceY = space;
//	}
//
//	protected float getOffsetX() {
//		return -(mCardColumns - 1) / 2f * mSpaceX;
//	}
//
//	protected float getOffsetY() {
//		return mCardRow % 2 == 0 ? -mSpaceY / 2 : 0;
//	}
//
//
//	protected void setFocusLineDistance(int distance) {
//		if (distance * 2 >= mCardRow) {
//			throw new RuntimeException(
//					"please makesure the FocusLineDistance less than or equals half of cardRows");
//		}
//		mRowOffsetCount = distance;
//	}
//
//	@Override
//	protected void initTranslateInfo(View view, int idX, int idY,
//			float offsetX, float offsetY, int cardIndex) {
//
//		view.setInfo(new TwoDTranslateInfo(idX * mSpaceX + offsetX, -idY
//				* mSpaceY + offsetY, idX, idY, cardIndex));
//	}
//
//	@Override
//	protected void updateViewTranslate(View view, TwoDTranslateInfo info,
//			float offset) {
//		view.setTranslate(info.mTranslateX, info.mTranslateY + offset, 0);
//	}
//
//	@Override
//	protected void touchDrag(float mTouchOffsetX, float mTouchOfmTouchOffsetYfsetY2) {
//
//		mAdapterAnimationHelper.dragBy(-mTouchOffsetY);
//	}
//
//	@Override
//	protected void touchFling(int velocityX, int velocityY) {
//		mAdapterAnimationHelper.flingTo(-velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
//	}
//
//
//	@Override
//	protected void onKeyCodeLeft() {
//
//		goPrePosition(true);		
//	}
//
//	@Override
//	protected void onKeyCodeRight() {
//
//		goNextPosition(true);
//	}
//
//	@Override
//	protected void onKeyCodeUp() {
//
//		goPreLine(false);
//	}
//
//	@Override
//	protected void onKeyCodeDown() {
//
//		goNextLine(false);
//	}
//	
//
//
//}
