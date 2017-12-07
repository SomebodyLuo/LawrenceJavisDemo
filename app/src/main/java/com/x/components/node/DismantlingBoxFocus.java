package com.x.components.node;

import com.x.Director;
import com.x.opengl.kernel.Quat;
import com.x.opengl.kernel.Transform;

/**
 * 将模型视图矩阵进行拆解，得到实际的x,y,z位移，以及实际的x,y,z缩放，但是实际的x,y,z上各自的旋转角度却不好拆解，暂时无突破，
 * 所以使用临时的一个rotate（详见代码中）
 * 
 * 
 */
public class DismantlingBoxFocus extends MoveBox{

	private Transform mTransform = new Transform();
	private Transform mAnimationPackage = new Transform(); // 动画封装类
	private Transform mDestinationTransform = new Transform();

	@Override
	protected void focusDraw() {
		Director.sGLESVersion.pushMatrix();

		onAnimation();
		Director.sGLESVersion.onViewTransform(mTransform);
//		mBGDrawable.draw(AttributeStatck.getFinalAlpha(), 1, mPrivateFlag);
		mBGDrawable.draw();

		Director.sGLESVersion.popMatrix();
	}
	@Override
	protected boolean focusHasMoreAnimation() {
		return mAnimationPackage.hasMoreAnimation();
	}
	@Override
	protected void focusOnAnimation() {

		mAnimationPackage.run();	
		
//		mAnimationSet.normalFocus(mTransform,mScaleX, mScaleY, /*1.1f, 1.1f, */ mThickness, 1,1,1,mBindView.getDrawableCount());
		mAnimationPackage.normalFocus(mTransform,mScaleX, mScaleY, /*1.1f, 1.1f, */ mViewThickness, 1,1,1,0);
		if (mAnimationPackage.hasMoreAnimation())
		{
			postInvalidate();
		}
	}
	public void focusUpdateState(View view,boolean byView)
	{

//    	float[][] a2 = new float[4][4];
//    	int index = 0;
//    	for (int j = 0; j < a2.length; j++) {
//	    	for (int i = 0; i < a2[j].length; i++) {
//					a2[i][j] = view.getFinalModelViewMatrix()[index++];
//			}
//    	}
		;
    	
    	//缩放因子x,y,z分别是第一，二，三列的范数
//    	float scaleX = (float)Math.sqrt(
//    			 Math.pow(a2[0][0], 2) 
//     			+Math.pow(a2[1][0], 2) 
//    			+Math.pow(a2[2][0], 2) 
//    			);
//    	float scaleY = (float)Math.sqrt(
//    			Math.pow(a2[0][1], 2) 
//    			+Math.pow(a2[1][1], 2) 
//    			+Math.pow(a2[2][1], 2) 
//    			);
//    	float scaleZ = (float)Math.sqrt(
//    			Math.pow(a2[0][2], 2) 
//    			+Math.pow(a2[1][2], 2) 
//    			+Math.pow(a2[2][2], 2) 
//    			);
    	
    	
    	////视图对rotate的拆解 ,以下为注释语句 -------分割线开始
//    	float[][] aRotate = new float[3][3];
//
//    	String string = "\n";
//    	for (int i = 0; i < a2.length-1; i++) {
//			for (int j = 0; j < a2[i].length-1; j++) {
//				if(j == 0){
//					aRotate[i][j] = a2[i][j]/scaleX;
//				}else if(j == 1){
//					aRotate[i][j] = a2[i][j]/scaleY;
//				}else{
//					aRotate[i][j] = a2[i][j]/scaleZ;
//				}
//				string += "\t\t"+((int)(aRotate[i][j]*1000))/1000f ;//取小数点后两位
//			}
//			string += "\n";
//		}
//    	Log.d("rotate", "string = "+string);
//    	Quat quat = fromRotationMatrix(aRotate);
////    	AngleAxis axis = quat.toAngleAxis();
//    	Log.d("rotate", "quat = "+quat);
////    	Log.d("rotate", "axis = "+axis);
//    	Log.d("rotate", ",,,quat.getPitch() = "+quat.getPitch()*PP+",,quat.getYaw() = "+quat.getYaw()*PP+",,quat.getRoll() = "+quat.getRoll()*PP);
    	
    	/////------分割线结束
    	
		boolean flag1 = mDestinationTransform.Position.equals(view.getFinalModelViewTranslate());
		boolean flag2 = mDestinationTransform.Rotate.equals(view.getFinalModelViewRotate());
		boolean flag3 = mDestinationTransform.Scale.equals(view.getFinalModelViewScale());
		boolean flag4 = mDestinationTransform.Alpha == view.getFinalAlpha();
		if (flag1 && flag2 && flag3 && flag4)
		{
			return;
		}
		mDestinationTransform.Position.set(view.getFinalModelViewTranslate());
		mDestinationTransform.Rotate.set(view.getFinalModelViewRotate());
		mDestinationTransform.Scale.set(view.getFinalModelViewScale());
		mDestinationTransform.Alpha = (view.getFinalAlpha());
		
		mAnimationPackage.transformTo(mDestinationTransform,byView);
	}
	
	float PP = (float) (180f/Math.PI);
	float fRoot = 0;
	float x, y, z, w;
	Quat fromRotationMatrix (final float[][]  rot)
	{
		float fTrace = rot[0][0]+rot[1][1]+rot[2][2];
		float fRoot;
		if ( fTrace > 0.0 )
		{
			fRoot = (float) Math.sqrt(fTrace + 1.0f);
			w = 0.5f*fRoot;
			fRoot = 0.5f/fRoot;
			x = (rot[2][1]-rot[1][2])*fRoot;
			y = (rot[0][2]-rot[2][0])*fRoot;
			z = (rot[1][0]-rot[0][1])*fRoot;
		}
		else
		{
			int[] s_iNext = { 1, 2, 0 };
			int  i = 0;
			if ( rot[1][1] > rot[0][0] )
				i = 1;
			if ( rot[2][2] > rot[i][i] )
				i = 2;
			int j = s_iNext[i];
			int k = s_iNext[j];

			fRoot = (float) Math.sqrt(rot[i][i]-rot[j][j]-rot[k][k] + 1.0f);
			float[] apkQuat = { x, y, z };
			apkQuat[i] = 0.5f*fRoot;
			fRoot = 0.5f/fRoot;
			w = (rot[k][j]-rot[j][k])*fRoot;
			apkQuat[j] = (rot[j][i]+rot[i][j])*fRoot;
			apkQuat[k] = (rot[k][i]+rot[i][k])*fRoot;
			
		}
		
		return new Quat(w,x, y, z);
	}
	@Override
	protected void setSpeed(float speed) {
		// TODO Auto-generated method stub
		
	}
}
