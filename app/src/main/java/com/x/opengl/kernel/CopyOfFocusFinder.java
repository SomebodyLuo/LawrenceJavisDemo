package com.x.opengl.kernel;
//package com.kaiboer.Wolf.kernel;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.kaiboer.Wolf.WolfScene;
//import com.kaiboer.Wolf.gameobject.View;
//import com.kaiboer.Wolf.gameobject.ViewGroup;
//import com.kaiboer.Wolf.kernel.animation_redo.T_Transform;
//
//public class CopyOfFocusFinder {
//
//	public static final int	DOWN			= 0;
//	public static final int	UP				= 1;
//	public static final int	LEFT			= 2;
//	public static final int	RIGHT			= 3;
//	public static final int	INVALID_INDEX	= -1;
//	private FocusView	mFocusView; //用于移动的焦点view
//
//	public int findNextFocus(int focusIndex, int direction, WolfScene WolfScene) {
//
//		
//		
//		ViewGroup firstViewGroup = null;
//		
//		View view = FocusView.getBindView();
//		if(view == null){
//			firstViewGroup = WolfScene.mChildLinkedList.get(0);
//		}else{
//			View view = FocusView.getBindView();
//			for (int i = 0; i < WolfScene.mChildLinkedList.size(); i++) {
//				ViewGroup viewGroup = WolfScene.mChildLinkedList.get(i);
//				if(viewGroup.contain(view)){
//					firstViewGroup = viewGroup;
//					break;
//				}
//			}
//		}
//		
//		
//		if(focusIndex == INVALID_INDEX){
//			focusIndex = 0;
//			View currentView = get(focusIndex, WolfScene.mChildLinkedList.get(0));
//			currentView.onFocus();
//			mFocusView.bind(currentView);
//			return focusIndex;
//		}
//		
//		View currentView = get(focusIndex, WolfScene);
//
//		
//		switch (direction)
//		{
//		case UP:
//			focusIndex = find_Y_G(currentView,WolfScene);
//		break;
//		case DOWN:
//			focusIndex = find_Y_L(currentView,WolfScene);
//		break;
//		case LEFT:
//			focusIndex = find_X_L(currentView,WolfScene);
//		break;
//		case RIGHT:
//			focusIndex = find_X_G(currentView,WolfScene);
//		break;
//		default:
//		break;
//		}
//		
//
//		View nextView = get(focusIndex,firstViewGroup);;;
//		currentView.setFocused(false);
//		nextView.setFocused(true);
//		if(!nextView.equals(currentView)){
//			nextView.onFocus();
//			mFocusView.bind(nextView);
//		}
//		
//		return focusIndex ;
//	}
//
//
//	public void setFocusView(FocusView view) {
//		this.mFocusView = view;
//	}
//	
//	/**
//	 * 
//	 * @param currentView
//	 * @param viewGroup
//	 * @return  在viewGroup中遍历子view，查找Y值大于currentView的最小的那个
// 	 */
//	private int find_Y_G(View currentView, ViewGroup viewGroup) {
//
//		T_Transform t_Transform =  currentView.getRelativeTransform();
//		float y = t_Transform.Position.Y;
//		float x = t_Transform.Position.X;
//		
//		int nextIndex = viewGroup.indexOf(currentView);
//		
//		List<View> indexList = new ArrayList<View>();//拿到所有符合条件的view
//		for (int i = 0; i < viewGroup.size(); i++) {
//			View view = viewGroup.get(i);
//			T_Transform transform =  view.getRelativeTransform();
//			if(view.focusable() && transform .Position.Y > y){
//				indexList.add(view);
//			}
//		}
//		
//		float min = Integer.MAX_VALUE;
//		View obView = null; 
//		for (int j = 0; j < indexList.size(); j++) {
//			View view = indexList.get(j);
//			float temp = (float)Math.pow(view.getRelativeTransform().Position.X - x, 2) + (float)Math.pow(view.getRelativeTransform().Position.Y - y, 2);
//			if(temp < min){
//				min = temp;
//				obView = view;
//			}
//		}
//
//		if(obView != null){
//			nextIndex = viewGroup.indexOf(obView);
//		}
//		return nextIndex;
//	}
//
//	/**
//	 * 
//	 * @param currentView
//	 * @param viewGroup
//	 * @return  在viewGroup中遍历子view，查找Y值小于currentView的最小的那个
// 	 */
//	private int find_Y_L(View currentView, ViewGroup viewGroup) {
//
//		T_Transform t_Transform =  currentView.getRelativeTransform();
//		float y = t_Transform.Position.Y;
//		float x = t_Transform.Position.X;
//		
//		int nextIndex = viewGroup.indexOf(currentView);
//		
//		List<View> indexList = new ArrayList<View>();
//		
//		for (int i = 0; i < viewGroup.size(); i++) {
//			View view = viewGroup.get(i);
//			T_Transform transform =  view.getRelativeTransform();
//			if(view.focusable() && transform .Position.Y < y){
//				indexList.add(view);
//			}
//		}
//		
//		
//		float min = Integer.MAX_VALUE;
//		View obView = null; 
//		for (int j = 0; j < indexList.size(); j++) {
//			View view = indexList.get(j);
//			float temp = (float)Math.pow(view.getRelativeTransform().Position.X - x, 2) + (float)Math.pow(view.getRelativeTransform().Position.Y - y, 2);
//			if(temp < min){
//				min = temp;
//				obView = view;
//			}
//		}
//
//		if(obView != null){
//			nextIndex = viewGroup.indexOf(obView);
//		}
//		return nextIndex;
//	}
//
//	/**
//	 * 
//	 * @param currentView
//	 * @param viewGroup
//	 * @return  在viewGroup中遍历子view，查找X值小于currentView的最小的那个
// 	 */
//	private int find_X_L(View currentView, ViewGroup viewGroup) {
//
//		T_Transform t_Transform =  currentView.getRelativeTransform();
//		float x = t_Transform.Position.X;
//		float y = t_Transform.Position.Y;
//		
//		int nextIndex = viewGroup.indexOf(currentView);
//		List<View> indexList = new ArrayList<View>();
//		
//		for (int i = 0; i < viewGroup.size(); i++) {
//			View view = viewGroup.get(i);
//			T_Transform transform =  view.getRelativeTransform();
//			if(view.focusable() && transform .Position.X < x){
//				indexList.add(view);
//			}
//		}
//
//		
//		
//		float min = Integer.MAX_VALUE;
//		View obView = null; 
//		for (int j = 0; j < indexList.size(); j++) {
//			View view = indexList.get(j);
//			float temp = (float)Math.pow(view.getRelativeTransform().Position.X - x, 2) + (float)Math.pow(view.getRelativeTransform().Position.Y - y, 2);
//			if(temp < min){
//				min = temp;
//				obView= view;
//			}
//		}
//		if(obView != null){
//			nextIndex = viewGroup.indexOf(obView);
//		}
//		return nextIndex;
//	}
//
//	/**
//	 * 
//	 * @param currentView
//	 * @param viewGroup
//	 * @return  在viewGroup中遍历子view，查找X值大于currentView的最小的那个
// 	 */
//	private int find_X_G(View currentView, ViewGroup viewGroup) {
//
//		T_Transform t_Transform =  currentView.getRelativeTransform();
//		float y = t_Transform.Position.Y;
//		float x = t_Transform.Position.X;
//		
//		int nextIndex = viewGroup.indexOf(currentView);
//		
//		List<View> indexList = new ArrayList<View>();
//		
//		for (int i = 0; i < viewGroup.size(); i++) {
//			View view = viewGroup.get(i);
//			T_Transform transform =  view.getRelativeTransform();
//			if(view.focusable() && transform .Position.X > x){
//				indexList.add(view);
//			}
//		}
//
//		float min = Integer.MAX_VALUE;
//		View obView = null; 
//		for (int j = 0; j < indexList.size(); j++) {
//			View view = indexList.get(j);
//			float temp = (float)Math.pow(view.getRelativeTransform().Position.X - x, 2) + (float)Math.pow(view.getRelativeTransform().Position.Y - y, 2);
//			if(temp < min){
//				min = temp;
//				obView= view;
//			}
//		}
//		if(obView != null){
//			nextIndex = viewGroup.indexOf(obView);
//		}
//		return nextIndex;
//	}
//	private View get(int index, ViewGroup viewGroup) {
//		if(index < viewGroup.size()){
//			return viewGroup.get(index);
//		}
//		return null;
//	}
//
//	private int findCurrentFocusIndex(ViewGroup viewGroup) {
//
//		int currentFocusIndex = INVALID_INDEX;
//		for (int i = 0; i < viewGroup.size(); i++) {
//			View view = viewGroup.get(i);
//			if (view.focusable() && view.isFocused()) {
//				currentFocusIndex = i;
//			} else {
//				view.setFocused(false);
//			}
//		}
//		return currentFocusIndex;
//
//	}
//
//
//
//}
