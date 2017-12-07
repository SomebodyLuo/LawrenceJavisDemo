package com.x.components.widget.newer;

import com.x.components.node.ViewGroup;
import com.x.opengl.kernel.Position;
import com.x.components.node.View;

public class GridView extends NewAdapterView {


	public void setColumns(int i) {
		mCardColumns = i;
	}

	public void setRows(int i) {
		mCardRow = i;
		mHalfRow = mCardRow / 2;// 行数的一半
	}

	public void setBaseX(int x){
		mBaseLeftX = x;
	}
	public void setBaseY(int y){
		mBaseTopY = y;
	}

	public void setHorizontalSpace(int space) {
		this.mSpaceX = space;
	}

	public void setVeticalSpace(int space) {
		this.mSpaceY = space;
	}

	protected float getOffsetX() {
		return -(mCardColumns - 1) / 2f * mSpaceX;
	}

	protected float getOffsetY() {
		return mCardRow % 2 == 0 ? -mSpaceY / 2 : 0;
	}

	protected void setFocusLineDistance(int distance) {
		if (distance * 2 >= mCardRow) {
			throw new RuntimeException(
					"please makesure the FocusLineDistance less than or equals half of cardRows");
		}
		mRowOffsetCount = distance;
	}

	@Override
	protected void initTranslateInfo(View view, int idX, int idY,
			float offsetX, float offsetY, int cardIndex) {

		view.setInfo(new ViewGroup.TwoDTranslateInfo(idX * mSpaceX + offsetX, -idY
				* mSpaceY + offsetY, idX, idY, cardIndex));
	}

	@Override
	protected void updateViewTranslate(View view, ViewGroup.TwoDTranslateInfo info,
			float offset) {
		view.setTranslate(info.mTranslateX, info.mTranslateY + offset, 0);
	}

	Position mPosition = new Position();
	@Override
	protected Position getFocusPosition( ViewGroup.TwoDTranslateInfo info,
			float offset) {
		mPosition.X =info.mTranslateX ;
		mPosition.Y = info.mTranslateY + offset ;
		mPosition.Z = 0;
		return mPosition;
	}

	@Override
	protected void touchDrag(float mTouchOffsetX, float mTouchOffsetY) {
		mAdapterAnimationHelper.dragBy(-mTouchOffsetY, 0, (getTotalRowNumber() - 1) * mSpaceY);
		
	}

	@Override
	protected void touchFling(int velocityX, int velocityY) {
		mAdapterAnimationHelper.flingTo(-velocityY, 0, (getTotalRowNumber() - 1) * mSpaceY, 0, 0);
	}

	@Override
	protected void onKeyCodeLeft() {

		goPrePosition(true);		
	}

	@Override
	protected void onKeyCodeRight() {

		goNextPosition(true);
	}

	@Override
	protected void onKeyCodeUp() {

		goPreLine(false);
	}

	@Override
	protected void onKeyCodeDown() {

		goNextLine(false);
	}
	

	
}
