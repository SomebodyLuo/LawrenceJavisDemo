package com.x.components.widget;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

import com.x.opengl.kernel.Drawable;
import com.x.opengl.kernel.Texture;

/**
 * 扩展文字控件
 */
public class ExpandTextView extends TextView
{
	
	public ExpandTextView(boolean flag){
		super(flag);
	}
	public ExpandTextView() {
		super();
	}
	public ExpandTextView(String text) {
		super(text);
	}


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
	public void setImageBitmapAlpha(float alpha,int level) {
		mImageDrawables.get(level).setAlpha(alpha);
	}
	public void imageBitmapAlphaTo(float alpha,int level) {
		mImageDrawables.get(level).alphaTo(alpha);
	}

	public void setTextDrawableBackground(Texture texture) {
		if(mTextDrawableBackGround == null){
			throw new RuntimeException("please use the constructer fuction  public ExpandTextView(boolean flag)  and set the  flag true");
		}
		this.mTextDrawableBackGround.setTexture(texture);
	}
	public void setTextDrawableBackgroundScale(float x, float y, int z) {
		if(mTextDrawableBackGround == null){
			throw new RuntimeException("please use the constructer fuction  public ExpandTextView(boolean flag)  and set the  flag true");
		}
		this.mTextDrawableBackGround.setScale(x, y, z);
	}
	public void setTextDrawableBackgroundTranslate(float x, float y, int z) {
		if(mTextDrawableBackGround == null){
			throw new RuntimeException("please use the constructer fuction  public ExpandTextView(boolean flag)  and set the  flag true");
		}
		this.mTextDrawableBackGround.setTranslate(x, y, z);
	}
	
	public void setTextDrawableBackgroundAlpha(float alpha){
		if(mTextDrawableBackGround == null){
			throw new RuntimeException("please use the constructer fuction  public ExpandTextView(boolean flag)  and set the  flag true");
		}
		this.mTextDrawableBackGround.setAlpha(alpha);
	}
	
	public void textDrawableBackgroundScaleTo(float x, float y, int z) {
		if(mTextDrawableBackGround == null){
			throw new RuntimeException("please use the constructer fuction  public ExpandTextView(boolean flag)  and set the  flag true");
		}
		this.mTextDrawableBackGround.scaleTo(x, y, z);
	}
	public void textDrawableBackgroundScaleTo(float x, float y, int z,int time) {
		if(mTextDrawableBackGround == null){
			throw new RuntimeException("please use the constructer fuction  public ExpandTextView(boolean flag)  and set the  flag true");
		}
		this.mTextDrawableBackGround.scaleTo(x, y, z,time);
	}
	public void textDrawableBackgroundTranslateTo(float x, float y, int z) {
		if(mTextDrawableBackGround == null){
			throw new RuntimeException("please use the constructer fuction  public ExpandTextView(boolean flag)  and set the  flag true");
		}
		this.mTextDrawableBackGround.translateTo(x, y, z);
	}
	public void textDrawableBackgroundAlphaTo(float alpha) {
		if(mTextDrawableBackGround == null){
			throw new RuntimeException("please use the constructer fuction  public ExpandTextView(boolean flag)  and set the  flag true");
		}
		this.mTextDrawableBackGround.alphaTo(alpha);
	}

	public void textDrawableBackgroundAlphaTo(float alpha ,int time) {
		if(mTextDrawableBackGround == null){
			throw new RuntimeException("please use the constructer fuction  public ExpandTextView(boolean flag)  and set the  flag true");
		}
		this.mTextDrawableBackGround.alphaTo(alpha,time);
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
	public void setImageBitmapRenderable(boolean b, int level) {

			mImageDrawables.get(level).setRenderable(b);
	}
	
}
