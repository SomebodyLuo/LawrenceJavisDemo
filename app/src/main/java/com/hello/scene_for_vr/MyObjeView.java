package com.hello.scene_for_vr;

import com.x.opengl.kernel.Drawable;
import com.x.components.node.View;

public class MyObjeView extends View{

	private float rotate;
	private boolean  mRun;
	public MyObjeView(Drawable baseDrawable) {
		super(baseDrawable);
	}
	public MyObjeView() {
		super();
	}
	@Override
	protected void onAnimation() {
		// TODO Auto-generated method stub
		super.onAnimation();
		if(!mRun){
			return;
		}
		rotate += 1;
		if(rotate > 360){
			rotate = 0;
		}
//		Log.d("ming", "rotate "+rotate);
		setRotate(rotate, rotate,rotate);
//		setScale(rotate,rotate, 1);
	}
	public void changeState() {
//		mRun = !mRun;
	}
	public void setRotateEnable(boolean b) {
		mRun = b;
	}
}
