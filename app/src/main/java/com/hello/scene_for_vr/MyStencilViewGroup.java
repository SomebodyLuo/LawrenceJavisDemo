package com.hello.scene_for_vr;

import android.graphics.Color;
import android.opengl.GLES20;

import com.x.Director;
import com.x.components.node.View;
import com.x.components.node.ViewGroup;
import com.x.components.widget.ImageView;

public class MyStencilViewGroup extends ViewGroup{

	private float rotate;
	private float rotateY;
	private float rotateZ;
	private View vv  ;
	private View vv2  ;
	private View vv3  ;
	public MyStencilViewGroup() {
		super();

		vv = makeImageView222(0,1600,900);
		vv.setBackgroundColor(Color.YELLOW);
		vv2 = makeImageView222(-3,400,400);
		vv2.setBackgroundColor(Color.GREEN);
		vv3 = makeImageView222(2,400,400);
		vv3.setBackgroundColor(Color.CYAN);
		
		
//		GLES20.glStencilFunc(GLES20.GL_EQUAL, StencilValueStatck.getStackSize()-1 , 0xff);
//		GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_INCR);
//
//		mBGDrawable.draw();
//		
//		//view实际绘制全部委派给drawable列表
//		for (int i = 1; i < mDrawableList.size(); i++) {  
//			Drawable drawable = mDrawableList.get(i);
//			if(drawable.isRenderable()){
//				GLES20.glStencilFunc(GLES20.GL_EQUAL, StencilValueStatck.getStackSize(), 0xff);
//				GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_KEEP);
//				drawable.draw( );
//			}
//		}
	}
	View makeImageView222(final int i, int width, int height ){

		final ImageView vv = new ImageView();
		vv.SetDebugName("makeImageView  "+i);
		vv.setWidth(width);
		vv.setHeight(height);
		vv.setBackgroundColor(Color.parseColor("#ff777777"));
		vv.setTranslate(i*250, 0, 0);
		return vv;
	}
	@Override
	protected void onChildDraw() {
		super.onChildDraw();
		
		Director.sGLESVersion.openStencilTest();
		
		GLES20.glStencilFunc(GLES20.GL_ALWAYS, 1 , 0xff);
		GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_REPLACE);
		vv.draw();

		GLES20.glStencilFunc(GLES20.GL_GREATER, 2 , 0xff); 
		GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_REPLACE);
		vv2.draw();

		GLES20.glStencilFunc(GLES20.GL_EQUAL, 1 , 0xff); 
		GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_KEEP);
		vv3.draw();
		
		
		Director.sGLESVersion.closeStencilTest();

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
