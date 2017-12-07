package com.x.components.node;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;


/**
 * @hide
 *
 */
public class VrStarePointScene extends XScene{

	
	private ViewGroup mViewGroup = new ViewGroup();
	
	public VrStarePointScene() {
		super();

		SetDebugName("VrStarePointScene");
	}
	@Override
	public void initScene() {
		
		Bitmap bitmap = Bitmap.createBitmap(1,1,Config.ARGB_8888);
		bitmap.eraseColor(Color.argb(255, 0,255 , 0));

		mViewGroup.setWidth( 10);
		mViewGroup.setHeight( 10);
		mViewGroup.setTranslate(0, 0, 0);
		mViewGroup.setBackground(bitmap);
		
		addLayer(mViewGroup);
	}


}
