package com.x.components.widget;

import com.x.opengl.kernel.Drawable;


/**
 * 暂时作废
 * 
 * @author lic
 * 
 */
public class SelectorDrawable extends Drawable
{

	public SelectorDrawable(int depth)
	{
		super(depth);
	}

	// @Override
	// public void draw(float allUpParentApha, float viewAlpha)
	// {
	// if (isFocused)
	// {
	// if (mFocusedTexture != null)
	// {
	// mMaterial.Texture = mFocusedTexture;
	// } else
	// {
	// mMaterial.Texture = mNormalTexture;
	// }
	// } else if (isSelected)
	// {
	// if (mSelectedTexture != null)
	// {
	// mMaterial.Texture = mFocusedTexture;
	// } else
	// {
	// mMaterial.Texture = mNormalTexture;
	// }
	// } else
	// {
	// mMaterial.Texture = mNormalTexture;
	// }
	// super.draw(allUpParentApha, viewAlpha);
	// }

	// /**
	// * @param selectorResid
	// * @Deprecated 暂时没有实现此方法
	// */
	// @Deprecated
	// public void setSelectorDrawable(int selectorResid)
	// {
	// // ***
	// }
	//
	// public void setSelectorDrawable(int normalResid, int focusedResid, int
	// selectedResid)
	// {
	// mNormalTexture = Director.sResourcer.generateTexture(normalResid);
	// mFocusedTexture = Director.sResourcer.generateTexture(focusedResid);
	// mSelectedTexture = Director.sResourcer.generateTexture(selectedResid);
	// }
	//
	// public void setSelectorDrawable(int normalResid, int focusedResid)
	// {
	// mNormalTexture = Director.sResourcer.generateTexture(normalResid);
	// mFocusedTexture = Director.sResourcer.generateTexture(focusedResid);
	// }
	//
	// @Override
	// public void setTexture(Bitmap bitmap)
	// {
	// mNormalTexture = Director.sResourcer.generateTexture(bitmap);
	// }
	//
	// @Override
	// public void setTexture(int resid)
	// {
	// mNormalTexture = Director.sResourcer.generateTexture(resid);
	// }
}
