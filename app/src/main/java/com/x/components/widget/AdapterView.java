package com.x.components.widget;

import com.x.components.node.View;
import com.x.components.node.ViewGroup;

public abstract class AdapterView<T extends Adapter> extends ViewGroup{

	
	protected abstract void onItemSelectScrollTo(View v);

	protected  OnItemClickListener mOnItemClickListener;
	protected  OnItemLongClickListener mOnItemLongClickListener;
	
	public interface   OnItemClickListener{

		void onItemClick(View v);
		
	}
	public interface   OnItemLongClickListener{

		void onItemLongClick(View v);
		
	}
	public void setOnItemClickListener(OnItemClickListener l){
		this.mOnItemClickListener = l;
	}
	public void setOnItemLongClickListener(OnItemLongClickListener l){
		this.mOnItemLongClickListener = l;
	}
	
	/*
	 * 本监听器主要用于用户主动Focus,点击,或者长按AdapterView中的某一项Item的时候将AdapterView滚动到这一项Item
	 */
	protected OnWidgetItemListener mOnWidgetItemListener = new OnWidgetItemListener() {
		
		private View mLastFocusView = null;
		@Override
		public void onClick(View v) {
			onItemSelectScrollTo(v);
			if(mOnItemClickListener != null){
				mOnItemClickListener.onItemClick(v);
			}
		}

		@Override
		public void onFocus(View v) {

//			Log.d("LOG", "mLastFocusView 1= "+mLastFocusView);
			if(v != mLastFocusView ){
				mLastFocusView = v;
				onItemSelectScrollTo(v);
			}
//			Log.d("LOG", "mLastFocusView 2= "+mLastFocusView);
			
		}

		@Override
		public void onLongClick(View v) {

			onItemSelectScrollTo(v);
			if(mOnItemLongClickListener != null){
				mOnItemLongClickListener.onItemLongClick(v);
			}
		}

		@Override
		public void onStareAt(View v) {
			
		}
	};
	
	
}
