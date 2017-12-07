package com.x.components.widget;

import android.view.KeyEvent;

import com.x.components.node.View;

/**
 * 
 * 
 *
 */
public class NoLoopVeticalListView extends NoLoopAbstractListView{

	@Override
	public boolean onKeyEvent(KeyEvent event) {
		
		if(getChildCount() == 0){
			return super.onKeyEvent(event);
		}
		boolean down_flag = false;
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			down_flag = true;
		}
		
		switch (event.getKeyCode())
		{
			case KeyEvent.KEYCODE_DPAD_DOWN:
				if (down_flag) {
					if(noSelectAnyone()){
						setSelectItem(0);
					}else{
						goAdd(+1,false);
					}
					return true;
				}
			break;
			case KeyEvent.KEYCODE_DPAD_UP:
				if (down_flag) {
					if(mFocusIndex == 0){
						return super.onKeyEvent(event);
					}else{
						goSub(-1,false);
						return true;	
					}
				}
			break;
			default:
			
			break;
		}
		
		return super.onKeyEvent(event);
	}
	@Override
	protected View getView(int index) {
		View view = mAdapter.getView(index);
		view.setInfo(new TranslateInfo(0,index * mSpace,0,index,index));
		view.setTranslate( 0, -index * mSpace,0,false,true,false);
		return view;
	}
	@Override
	protected void setChildTranslate(View view, TranslateInfo info,float offset) {
		view.setTranslate( 0,-(info.mTranslateY + offset) ,0,false,true,false);
	}

	public void setVeticalSpace(int space) {
		mSpace = space;
	}

	@Override
	protected void onDrag(float touchOffsetX, float touchOffsetY) {

		mAnimationHolder.drag(-touchOffsetY);
	}
}
