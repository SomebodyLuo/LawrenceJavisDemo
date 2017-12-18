package com.hello.scene_for_vr;

import android.util.Log;

import com.x.Director;
import com.x.opengl.kernel.EngineConstanst;
import com.x.opengl.kernel.Material;
import com.x.opengl.kernel.MaterialGroup;
import com.x.opengl.kernel.ObjDrawable;
import com.x.opengl.kernel.Texture;
import com.x.components.node.View;
import com.x.components.node.View.OnClickListener;
import com.x.components.node.ViewGroup;

public class SkySphere {

	private MyObjeView mSphere;
	private ViewGroup mViewGroup;
	public View getLayer() {
		return mSphere;
	}
	public SkySphere() {
		

//		ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/MySphere.obj");
		ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/MySkySphere.obj");
//		ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/TankWorld.obj");
//		ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/MyCube.obj");
//		ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/sphere02.obj");
		mSphere = new MyObjeView(objDrawable);
		mSphere.setWidth(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
		mSphere.setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
		mSphere.setThickness(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
		mSphere.setScale(2, 2, 2);
		
		mSphere.setTranslate(0, 0, 0);
		mSphere.setFocusable(false);
		mSphere.setTouchAble(false);
		mSphere.setCullFrontFace(true);
		mSphere.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSphere.changeState();
			}
		});
	}


	private Texture mRestoreTextre;
	public int getVeodioTextureID() {
		MaterialGroup mGroup = ((ObjDrawable)mSphere.getBaseDrawable()).mMaterials;
		for (int i = 0; i < mGroup.size(); i++) {
			Material material = mGroup.getMaterial(i );
			Log.d("ming", "getVeodioTextureID material = "+material.Name);
		}
		if(mGroup.size() > 0){
			Material material = mGroup.getMaterial(0);
			mRestoreTextre = material.Texture;
	        material.Texture = Director.sResourcer.generateOES_Texture();
			return material.Texture.getTextureID();
		}
		return -1;
	} 

	public void restoreTexture() {

		MaterialGroup mGroup = ((ObjDrawable)mSphere.getBaseDrawable()).mMaterials;
		if(mGroup.size() > 0){
			Material material = mGroup.getMaterial(0);
	    	if(mRestoreTextre != null){
				material.Texture = mRestoreTextre;
			}
		}
	}
}
