package com.hello.scene_for_vr;

import com.x.opengl.kernel.Drawable;
import com.x.components.node.View;

public class MyTransView extends View{

	private float rotate;
	private boolean  mRun;
	public MyTransView(Drawable baseDrawable) {
		super(baseDrawable);
	}
	public MyTransView() {
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
		setRotate(0, rotate,0);
//		setScale(rotate,rotate, 1);
	}
	public void changeState() {
//		mRun = !mRun;
	}
	public void setRotateEnable(boolean b) {
		mRun = b;
	}
}
