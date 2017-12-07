package com.x.opengl.kernel;

import java.util.ArrayList;
import java.util.List;

import com.x.components.node.View;

/**
 *  X轴方向时,优先选取同一y值的， Y轴方向时，优先查找同一x值相同的 ，取名轴向查找方式
 *
 */
public class CommonAxialFocusFinder extends AbstractFocusFinder{

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
		List<View> axiaList = new ArrayList<View>();

		float max = Integer.MIN_VALUE;
		
		float axiaMax = Integer.MIN_VALUE;
		
		for (int i = 0; i < viewList.size(); i++) {
				View view = viewList.get(i);
				Position vPosition =  view.getFinalModelViewTranslate();
				if(view.isFocusable() && view.isVisiable()&& vPosition.Y < y){
					indexList.add(view);
					if(vPosition.Y > max){
						max = vPosition.Y;
					}
					if(vPosition.X == x){
						axiaList.add(view);
						if(vPosition.Y > axiaMax){
							axiaMax = vPosition.Y;
						}
					}
				}
		}

		
		for (int i = 0; i < axiaList.size(); i++) {
			View view = axiaList.get(i);
			Position vPosition =  view.getFinalModelViewTranslate();
			if(vPosition.Y == axiaMax){
				return view;
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

		List<View> axiaList = new ArrayList<View>();

		float min = Integer.MAX_VALUE;
		float axiaMin = Integer.MAX_VALUE;
		for (int i = 0; i < viewList.size(); i++) {
			View view = viewList.get(i);
			Position vPosition=  view.getFinalModelViewTranslate();
			if(view.isFocusable() && view.isVisiable()&& vPosition.Y > y){
				indexList.add(view);
				if(vPosition.Y <= min){
					min = vPosition.Y;
				}
				if(vPosition.X == x){
					axiaList.add(view);
					if(vPosition.Y <= axiaMin){
						axiaMin = vPosition.Y;
					}
				}
			}
		}

		
		for (int i = 0; i < axiaList.size(); i++) {
			View view = axiaList.get(i);
			Position vPosition =  view.getFinalModelViewTranslate();
			if(vPosition.Y == axiaMin){
				return view;
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

		List<View> axiaList = new ArrayList<View>();
		float max = Integer.MIN_VALUE;
		float axiaMax = Integer.MIN_VALUE;
		for (int i = 0; i < viewList.size(); i++) {
			View view = viewList.get(i);
			Position vPosition  =  view.getFinalModelViewTranslate();
			if(view.isFocusable() && view.isVisiable() && vPosition.X < x){
				indexList.add(view);
				if(vPosition.X > max){
					max = vPosition.X;
				}
				if(vPosition.Y == y){
					axiaList.add(view);
					if(vPosition.X > axiaMax){
						axiaMax = vPosition.X;
					}
				}
			}
		}

		for (int i = 0; i < axiaList.size(); i++) {
			View view = axiaList.get(i);
			Position vPosition =  view.getFinalModelViewTranslate();
			if(vPosition.X == axiaMax){
				return view;
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

		List<View> axiaList = new ArrayList<View>();
		
		float min = Integer.MAX_VALUE;
		float axiaMin = Integer.MAX_VALUE;
		
		for (int i = 0; i < viewList.size(); i++) {
			View view = viewList.get(i);
			Position vPosition  =  view.getFinalModelViewTranslate();
			if(view.isFocusable() && view.isVisiable() && vPosition .X > x){
				indexList.add(view);
				if(vPosition.X < min){
					min = vPosition.X;
				}
				if(vPosition.Y == y){
					axiaList.add(view);
					if(vPosition.X < axiaMin){
						axiaMin = vPosition.X;
					}
				}
			}
		}
		for (int i = 0; i < axiaList.size(); i++) {
			View view = axiaList.get(i);
			Position vPosition =  view.getFinalModelViewTranslate();
			if(vPosition.X == axiaMin){
				return view;
			}
		}
		return findMinDistanceView(currentView,indexList,x,y);
	}
	

}
