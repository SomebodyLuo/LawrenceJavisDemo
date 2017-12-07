package com.x.components.node;

import java.util.ArrayList;
import java.util.List;

import android.opengl.Matrix;

import com.x.Director;
import com.x.opengl.kernel.EngineConstanst;
import com.x.opengl.kernel.ObjDrawable;


/**
 * 
 * 注意调试光照系统一定要把物体放在原点实验效果，不然坑死你
 * 
 */
public class LightingScene extends XScene{

	private List<LightView> mLightViews = new ArrayList<LightView>();

	private final int MaxLightNumber = 10;
	public static float POINT_LIGHT = 0 ;//点光源
	public static float PARALLEL_LIGHT = 1;//平行光
	private float lightStyle = PARALLEL_LIGHT;//默认平行光

	@Override
	public void initScene() {
		
//		for (int i = 0; i < MaxLightNumber; i++) {
			
//			LightView view = new LightView();
//			view.setWidth(70);
//			view.setHeight(70);
//			view.setTextSize(30);
//			view.setTextGravity(Gravity.CENTER);
//			view.setBackgroundColor(Color.WHITE);
//			view.setTextColor(Color.BLACK);
//			view.setTranslate(EngineConstanst.REFERENCE_SCREEN_HEIGHT/4, 0, EngineConstanst.REFERENCE_SCREEN_HEIGHT/4);
//			view.setText("灯光"+0);
//			view.setVisibility(false);
//			
//			addChild(view);
//			mLightViews.add(view);
//			
//			if(0== 0 ){
//				final View vi = view;	
//				Director.getInstance().getGLHandler().postDelay(new Runnable() {
//					
//					@Override
//					public void run() {
//						float x =0.9f* (float) (EngineConstanst.REFERENCE_SCREEN_HEIGHT*(Math.random() - 0.5));
//						float y =0.9f* (float) (EngineConstanst.REFERENCE_SCREEN_WIDTH*(Math.random() - 0.5));
//						float z =0.9f* (float) (EngineConstanst.REFERENCE_SCREEN_HEIGHT*(Math.random() - 0.5));
//						
//						vi.translateTo(x, y,-EngineConstanst.REFERENCE_SCREEN_HEIGHT * 2 +2*z, 800);
//						Director.getInstance().getGLHandler().postDelay(this,3000);
//						
//					}
//				},3000);
//			}
//		}

//			ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/MySphere.obj");
		ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/MyCube.obj");
		LightView mSphere = new LightView(objDrawable);
//			LightView mSphere = new LightView();
			mSphere.setWidth(EngineConstanst.REFERENCE_SCREEN_HEIGHT*0.1f);
			mSphere.setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT*0.1f);
			mSphere.setThickness(EngineConstanst.REFERENCE_SCREEN_HEIGHT*0.1f);
			//mSphere.setScale(0.1f,0.1f,0.1f);
//			mSphere.setWidth(70);
//			mSphere.setHeight(70);
//			mSphere.setTextSize(30);
//			mSphere.setText("灯光"+0);
//			mSphere.setBackgroundColor(Color.WHITE);
//			mSphere.setScale(2, 2, 2);
//			mSphere.setRotate(0, 10, 0);
			
			mSphere.setTranslate(0, 0, -EngineConstanst.REFERENCE_SCREEN_HEIGHT*2);
			mSphere.setFocusable(false);
			mSphere.setTouchAble(false);
			mSphere.setRotateEnable(true);
			final View vi = mSphere;	
			Director.getInstance().getGLHandler().postDelay(new Runnable() {
				
				@Override
				public void run() {
					float x =0.5f* (float) (EngineConstanst.REFERENCE_SCREEN_HEIGHT*(Math.random() - 0.5));
					float y =0.5f* (float) (EngineConstanst.REFERENCE_SCREEN_WIDTH*(Math.random() - 0.5));
					float z =Math.abs(0.5f* (float) (EngineConstanst.REFERENCE_SCREEN_HEIGHT*(Math.random() - 0.5)));
					
					//平行光使用的方向向量为指向原点，要测试平行光，则将目标物体放在原点，让光照的位置绕原点移动，可以看到正确的效果。
					vi.translateTo(x, y,0+2*z, 800);
					Director.getInstance().getGLHandler().postDelay(this,3000);
					
				}
			},3000);
			addChild(mSphere);
			mLightViews.add(mSphere);
	
	}

	public void enableLight(int i) {
		mLightViews.get(i).setVisibility(true);
	}
	public void disableLight(int i){
		mLightViews.get(i).setVisibility(false);
	}

	public boolean isLightEnable() { 
		return mLightViews.get(0).isVisiable();
	}

	public float[] getLightPosition() {
		float[] resultMatrix = new float[4];
		float[]  matrix = mLightViews.get(0).getFinalModelMatrix();
		Matrix.multiplyMV(resultMatrix, 0, matrix, 0, new float[]{0,0,0,1}, 0);
		float[] resultposition = new float[3];
		resultposition[0] = resultMatrix[0];
		resultposition[1] = resultMatrix[1];
		resultposition[2] = resultMatrix[2];
		return resultposition;
//		return new float[]{mTransform.Position.X,mTransform.Position.Y,mTransform.Position.Z};
	}

	public float[] getAmbient() {
		return mLightViews.get(0).getAmbient();
	}

	public float[] getSpecular() {
		return mLightViews.get(0).getSpecular();
	}

	public float[] getDiffuse() {
		return mLightViews.get(0).getDiffuse();
	}

	public float[] getLightMMatrix() {
		return mLightViews.get(0).getLightMMatrix();
	}

	public float getLightStyle() {
		return lightStyle;
	}

}

