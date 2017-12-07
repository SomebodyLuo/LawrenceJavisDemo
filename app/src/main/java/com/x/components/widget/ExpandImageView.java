package com.x.components.widget;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

import com.x.opengl.kernel.Drawable;
import com.x.opengl.kernel.Texture;

public class ExpandImageView extends ImageView{


	protected int mScaleType = 0;
	
	private List<Drawable> mImageDrawables = new ArrayList<Drawable>();

	public void imageBitmapScaleTo(float x, float y, int z,int level) {
		mImageDrawables.get(level).scaleTo(x, y, z);
	}
	public void setImageBitmapScale(float x, float y, int z,int level) {
		mImageDrawables.get(level).setScale(x, y, z);
	}
	public void setImageBitmapTranslate(float x, float y, float z, int level) {
		mImageDrawables.get(level).setTranslate(x, y, z);
	}
	public void imageBitmapTranslateTo(float x, float y, float z, int level) {
		mImageDrawables.get(level).translateTo(x, y, z);
	}


	/**
	 * 设置图片
	 * 
	 * @param bitmap
	 *            图片
	 */
	public void setImageBitmap(Bitmap bitmap,int level)
	{
		if(mImageDrawables.size() <= level){
			Drawable drawable = new Drawable(1);
			addDrawable(drawable);
		};

		mImageDrawables.get(level).setTexture(bitmap);
		setImageDrawable(mScaleType, bitmap,level);
	}

	/**
	 * 设置图片
	 * 
	 * @param bitmap
	 *            图片
	 */
	public void setImageBitmap(Texture text,int level)
	{

		if(mImageDrawables.size() <= level){
			Drawable drawable = new Drawable(1);
			addDrawable(drawable);
			mImageDrawables.add(drawable);
		};

		mImageDrawables.get(level).setTexture(text);
		setImageDrawable(mScaleType, text,level);
	}

	public void setImageBitmapRenderable(boolean b, int level) {

		mImageDrawables.get(level).setRenderable(b);
	}
	private void setImageDrawable(int scaletype, Bitmap bitmap, int level)
	{
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		float b = (float) w / h;
		switch (scaletype)
		{
		case 1:
			if (b > (float) mViewWidth / mViewHeight)
			{
				mImageDrawables.get(level).setHeight(1 / b);
			} else
			{
				mImageDrawables.get(level).setWidth(b);
			}
			break;
		default:
			break;
		}
	}
	private void setImageDrawable(int scaletype, Texture texture, int level)
	{
		int w = (int) texture.getWidth();
		int h = (int) texture.getHeight();
		float b = (float) w / h;
		switch (scaletype)
		{
		case 1:
			if (b > (float) mViewWidth / mViewHeight)
			{
				mImageDrawables.get(level).setHeight(1 / b);
			} else
			{
				mImageDrawables.get(level).setWidth(b);
			}
			break;
		default:
			break;
		}
	}
}
