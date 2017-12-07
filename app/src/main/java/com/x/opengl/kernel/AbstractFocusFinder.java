package com.x.opengl.kernel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.x.components.node.XScene;
import com.x.components.node.View;
import com.x.components.node.ViewGroup;

/**
 * 
 * 焦点查找方式抽象类
 *
 */
public abstract class AbstractFocusFinder {

	public static final int	DOWN			= 0;
	public static final int	UP				= 1;
	public static final int	LEFT			= 2;
	public static final int	RIGHT			= 3;
	public static final int	INVALID_INDEX	= -1;
	
	///////////////////////焦点查找方式
	public static final int	AXIAL_METHOD	= 0;
	public static final int	MINI_DISTANCE_METHOD	= 1;
	public static final int COMMON_AXIAL_METHOD = 2;
	

	protected abstract View find_Y_G(View bindView, List<View> allViews) ;
	protected abstract View find_Y_L(View bindView, List<View> allViews) ;
	protected abstract View find_X_G(View bindView, List<View> allViews) ;
	protected abstract View find_X_L(View bindView, List<View> allViews) ;
//	private FocusView	mFocusView; //用于移动的焦点view

	protected View findMinDistanceView(View currentView, List<View> indexList, float x, float y ) {
		float min = Integer.MAX_VALUE;
		View obView = currentView; 
		for (int j = 0; j < indexList.size(); j++) {
			View view = indexList.get(j);
			Position vPosition  =  view.getFinalModelViewTranslate();
			float temp = (float)Math.pow(vPosition.X - x, 2) + (float)Math.pow(vPosition.Y - y, 2);
			if(temp < min){
				min = temp;
				obView= view;
			}
		}
		return obView;
	}
	public void findNextFocus(int direction, XScene wolfScene) {

		
		View bindView = wolfScene.getFocusView().getBindView();
		if(bindView == null){

			if(wolfScene.getLayerSize() > 0){
				View currentView = get(0, wolfScene.getLayerAt(0));//这个地方需要修正为递归查找第一个visiable 不为false的view
				if(currentView != null ){
					 wolfScene.getFocusView().focusTo(currentView);
				}
			}
			return;
		}
		
		View nextView = null;
		

		List<View>  allViews = new ArrayList<View>();
		
		findAllViews(allViews,wolfScene.getLayerList());

		switch (direction)
		{
		case UP:
			nextView = find_Y_G(bindView,allViews);
		break;
		case DOWN:
			nextView = find_Y_L(bindView,allViews);
		break;
		case LEFT:
			nextView = find_X_L(bindView,allViews);
		break;
		case RIGHT:
			nextView = find_X_G(bindView,allViews);
		break;
		default:
		break;
		}

//		Log.d("nextView", "nextView =  "+nextView);
		if(!nextView.equals(bindView)){
			 wolfScene.getFocusView().focusTo(nextView);
		}
		allViews.clear();
		
	}



	protected void findAllViews(List<View> allViews, LinkedList<View> LayerList) {
		for (int i = 0; i < LayerList.size(); i++) {
			View viewGroup = LayerList.get(i);
			if(viewGroup.isVisiable() && viewGroup.isFocusable()){
				allViews.add(viewGroup);
			}
			if(viewGroup.isVisiable()){
				fillWith(allViews,viewGroup);
			}
		}
	}


	protected void fillWith(List<View> allViews, View view) {
		if(view instanceof ViewGroup){
			ViewGroup viewGroup = (ViewGroup) view;
			if(viewGroup.isVisiable() && viewGroup.isFocusable()){
				allViews.add(viewGroup);
			}
			for (int i = 0; i < viewGroup.getChildCount(); i++) {
				View child  = viewGroup.get(i);
				if(child.isVisiable()){
					fillWith(allViews, child);
				}
			}
		}else{
			allViews.add(view);
		}
	}


	

	



	protected View get(int index, ViewGroup viewGroup) {
		if(index < viewGroup.getChildCount()){
			return viewGroup.get(index);
		}
		return null;
	}

//	private int findCurrentFocusIndex(ViewGroup viewGroup) {
//
//		int currentFocusIndex = INVALID_INDEX;
//		for (int i = 0; i < viewGroup.size(); i++) {
//			View view = viewGroup.get(i);
//			if (view.isFocusable() && view.isFocused()) {
//				currentFocusIndex = i;
//			} else {
//				view.setFocused(false);
//			}
//		}
//		return currentFocusIndex;
//
//	}
//


}
