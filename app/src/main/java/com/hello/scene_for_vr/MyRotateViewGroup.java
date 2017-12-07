package com.hello.scene_for_vr;

import com.x.components.node.ViewGroup;

public class MyRotateViewGroup extends ViewGroup{

	private float rotate;
	private float rotateY;
	private float rotateZ;
	public MyRotateViewGroup() {
		super();
	}
	@Override
	protected void onAnimation() {
		// TODO Auto-generated method stub
		super.onAnimation();
//		rotate += 1;
//		if(rotate > 360){
//			rotate = 0;
//		}
//		rotateY += 0.5f;
//		if(rotateY > 360){
//			rotateY = 0;
//		}
//		rotateZ += 1;
//		if(rotateZ > 360){
//			rotateZ = 0;
//		}
//		setRotate(rotate , rotateY,rotateZ);
//		setScale(rotate/360f,rotate/360f,rotate/360f);
	}

}
