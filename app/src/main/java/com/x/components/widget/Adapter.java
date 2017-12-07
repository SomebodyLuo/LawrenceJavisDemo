package com.x.components.widget;

import android.database.DataSetObservable;
import android.database.DataSetObserver;

import com.x.components.node.View;
import com.x.components.node.ViewGroup;

public abstract class  Adapter {

    private final DataSetObservable mDataSetObservable = new DataSetObservable();
	ViewGroup mAttachViewGroup ;
	public  View getView(  int realId, View convertView,  ViewGroup parent){
		return null;
	};
	public  View getView(  int realId ){
		return null;
	};
	public int getCount(){
		return 0;
	};

	public void notifyDataSetChanged(){
        mDataSetObservable.notifyChanged();
	};
	public void setAttachView(ViewGroup viewGroup){
		mAttachViewGroup = viewGroup;
	};
	public ViewGroup getAttachView(){
		return null;
	};
	public View getFocusView(){
		return null;
	}
	public void unregisterDataSetObserver(DataSetObserver observer) {

        mDataSetObservable.unregisterObserver(observer);
	}
	public void registerDataSetObserver(DataSetObserver observer) {

        mDataSetObservable.registerObserver(observer);
	};

}
