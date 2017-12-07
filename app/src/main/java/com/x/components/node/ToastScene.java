package com.x.components.node;


public abstract class ToastScene extends XScene{

	public abstract void requestToastId(int stringId);
	public abstract void requestToastString(String toastString);
	
	public ToastScene(){
	}
	@Override
	public void initScene() {
	};
}
