package com.x.components.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.x.Director;
import com.x.opengl.kernel.Drawable;
import com.x.opengl.kernel.Texture;
import com.x.components.node.View;

/**
 * 图片控件
 */
public class ImageView extends View
{

	public Drawable mImageDrawable = null;
	private int mScaleType = 0;
	/** 不按比例拉伸到和控件的大小和致 */
	public static final int SCALETYPE_FIT_XY = 0;
	/** 按比例拉伸图片 并使得图片的宽高小于或等于控件的宽高,长和宽中至少有一个与控件相对应的宽和高相等 */
	public static final int SCALETYPE_CENTER_INSIDE = 1;

	// public static final int SCALETYPE_MATRIX = 0;
	// public static final int SCALETYPE_FIT_CENTER = 0;
	// public static final int SCALETYPE_FIT_START = 0;
	// public static final int SCALETYPE_FIT_END = 0;
	// public static final int SCALETYPE_CENTER = 0;
	// public static final int SCALETYPE_CENTER_CROP = 0;

	public ImageView()
	{
		super();
		SetDebugName("ImageView");
		mImageDrawable = new Drawable(1);
		addDrawable(mImageDrawable);
	}

	public ImageView(int width, int height)
	{
		super(width, height);
		SetDebugName("ImageView");
		mImageDrawable = new Drawable(1);
		addDrawable(mImageDrawable);
	}

	/**
	 * 创建一个以图片资源文件的宽和高为作为控件的宽和高的ImageView
	 * 
	 * @param bitmap
	 *            图片资源文件的id
	 */
	public ImageView(int resid)
	{
		super();
		SetDebugName("ImageView");
		Bitmap bitmap = BitmapFactory.decodeResource(Director.sResourcer.getContext().getResources(), resid);
		mImageDrawable = new Drawable(1);
		addDrawable(mImageDrawable);
		mViewWidth = bitmap.getWidth();
		mViewHeight = bitmap.getHeight();
		mImageDrawable.setTexture(bitmap);
	}

	/**
	 * 创建一个以图片的宽和高为作为控件的宽和高的ImageView
	 * 
	 * @param bitmap
	 *            图片
	 */
	public ImageView(Bitmap bitmap)
	{
		super();
		SetDebugName("ImageView");
		mImageDrawable = new Drawable(1);
		addDrawable(mImageDrawable);
		mViewWidth = bitmap.getWidth();
		mViewHeight = bitmap.getHeight();
		mImageDrawable.setTexture(bitmap);
	}

	/**
	 * 设置图片
	 * 
	 * @param resid
	 *            资源文件的id
	 */
	public void setImageResource(int resid)
	{
		Bitmap bitmap = BitmapFactory.decodeResource(Director.sResourcer.getContext().getResources(), resid);
		setImageBitmap(bitmap);
	}

	/**
	 * 设置图片
	 * 
	 * @param bitmap
	 *            图片
	 */
	public void setImageBitmap(Bitmap bitmap)
	{
		mImageDrawable.setTexture(bitmap);
		setImageDrawable(mScaleType, bitmap);
	}
	/**
	 * 设置图片
	 * 
	 * @param bitmap
	 *            图片
	 */
	public void setImageBitmap(Bitmap bitmap,boolean recycleLast)
	{
		mImageDrawable.setTexture(bitmap,recycleLast);
		setImageDrawable(mScaleType, bitmap);
	}
	/**
	 * 设置图片
	 * 
	 * @param resid
	 *            资源文件的id
	 */
	public void setImageResource(int resid,boolean recycleLast)
	{
		Bitmap bitmap = BitmapFactory.decodeResource(Director.sResourcer.getContext().getResources(), resid);
		setImageBitmap(bitmap,recycleLast);
	}
	/**
	 * 设置图片
	 * 
	 * @param bitmap
	 *            图片
	 */
	public void setImageBitmap(Texture text)
	{
		mImageDrawable.setTexture(text);
		setImageDrawable(mScaleType, text);
	}

	/**
	 * 设置图片在控件的显示方式 只有在初始化的时候,并设置了控件的宽和高之后调用才会生效
	 * 
	 * @param type
	 *            布局类型
	 */
	public void setScaleType(int type)
	{
		mScaleType = type;
	}

	private void setImageDrawable(int scaletype, Texture texture)
	{
		int w = (int) texture.getWidth();
		int h = (int) texture.getHeight();
		float b = (float) w / h;
		mImageDrawable.setHeight(h);
		mImageDrawable.setWidth(w);
		switch (scaletype)
		{
		case 1:
			if (b > (float) mViewWidth / mViewHeight)
			{
			} else
			{
			}
			break;
		default:
			break;
		}
	}
	
	private void setImageDrawable(int scaletype, Bitmap bitmap)
	{
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		float b = (float) w / h;
		if(mViewWidth != 1){
			mImageDrawable.setWidth(mViewWidth);
		}else{
			mImageDrawable.setWidth(w);
		}
		if(mViewHeight != 1){
			mImageDrawable.setHeight(mViewHeight);
		}else{
			mImageDrawable.setHeight(h);
		}
//		switch (scaletype)
//		{
//		case 1:
//			if (b > (float) mWidth / mHeight)
//			{
//				mImageDrawable.setHeightRatio(1 / b);
//			} else
//			{
//				mImageDrawable.setWidthRatio(b);
//			}
//			break;
//		default:
//			break;
//		}
	}

	public void setImageBitmapScale(float x, float y, int z) {
		mImageDrawable.setScale(x, y, z);
	}
	public void setImageBitmapAlpha(float alpha) {
		mImageDrawable.setAlpha(alpha);
	}

}
