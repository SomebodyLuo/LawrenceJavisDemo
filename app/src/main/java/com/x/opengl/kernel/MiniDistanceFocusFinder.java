package com.x.opengl.kernel;

import java.util.ArrayList;
import java.util.List;

import com.x.components.node.View;

/**
 * 以最近位置为查找原则，即x，y均参与考虑
 *
 */
public class MiniDistanceFocusFinder extends AbstractFocusFinder{

	/**
	 * 
	 * @param currentView
	 * @param viewGroup
	 * @return  在viewGroup中遍历子view，查找Y值小于currentView的最大的那个
 	 */
	protected View find_Y_L(View currentView,  List<View> viewList) {

		Position position =  currentView.getFinalModelViewTranslate();
		float y = position.Y;
		float x = position.X;
		
		
		List<View> indexList = new ArrayList<View>();
		
		float max = Integer.MIN_VALUE;
		for (int i = 0; i < viewList.size(); i++) {
				View view = viewList.get(i);
				Position vPosition =  view.getFinalModelViewTranslate();
				if(view.isFocusable() && view.isVisiable()&& vPosition.Y < y){
					indexList.add(view);
					if(vPosition.Y > max){
						max = vPosition.Y;
					}
				}
		}
		
		return findMinDistanceView(currentView,indexList,x,y);
	}
	/**
	 * 
	 * @param currentView
	 * @param viewGroup
	 * @return  在viewGroup中遍历子view，查找Y值大于currentView的最小的那个
 	 */
	protected View find_Y_G(View currentView, List<View> viewList) {

		Position position =  currentView.getFinalModelViewTranslate();
		float y = position.Y;
		float x = position.X;
		
		List<View> indexList = new ArrayList<View>();//拿到所有符合条件的view

		float min = Integer.MAX_VALUE;
		for (int i = 0; i < viewList.size(); i++) {
			View view = viewList.get(i);
			Position vPosition=  view.getFinalModelViewTranslate();
			if(view.isFocusable() && view.isVisiable()&& vPosition.Y > y){
				indexList.add(view);
				if(vPosition.Y <= min){
					min = vPosition.Y;
				}
			}
		}
		
		return findMinDistanceView(currentView,indexList,x,y);
	}
	/**
	 * 
	 * @param currentView
	 * @param viewGroup
	 * @return  在viewGroup中遍历子view，查找X值小于currentView的最大的那个
 	 */
	protected View find_X_L(View currentView, List<View> viewList) {

		Position position =  currentView.getFinalModelViewTranslate();
		float y = position.Y;
		float x = position.X;
		
		List<View> indexList = new ArrayList<View>();
		
		float max = Integer.MIN_VALUE;
		for (int i = 0; i < viewList.size(); i++) {
			View view = viewList.get(i);
			Position vPosition  =  view.getFinalModelViewTranslate();
			if(view.isFocusable() && view.isVisiable() && vPosition.X < x){
				indexList.add(view);
				if(vPosition.X > max){
					max = vPosition.X;
				}
			}
		}
		return findMinDistanceView(currentView,indexList,x,y);
	}

	/**
	 * 
	 * @param currentView
	 * @param viewGroup
	 * @return  在viewGroup中遍历子view，查找X值大于currentView的最小的那个
 	 */
	protected View find_X_G(View currentView,  List<View> viewList) {

		Position position =  currentView.getFinalModelViewTranslate();
		float y = position.Y;
		float x = position.X;
		
		List<View> indexList = new ArrayList<View>();
		
		float min = Integer.MAX_VALUE;
		for (int i = 0; i < viewList.size(); i++) {
			View view = viewList.get(i);
			Position vPosition  =  view.getFinalModelViewTranslate();
			if(view.isFocusable() && view.isVisiable() && vPosition .X > x){
				indexList.add(view);
				if(vPosition.X < min){
					min = vPosition.X;
				}
			}
		}
		return findMinDistanceView(currentView,indexList,x,y);
	}
	

}
