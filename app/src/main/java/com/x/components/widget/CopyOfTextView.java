package com.x.components.widget;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Typeface;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;

import com.x.Director;
import com.x.opengl.kernel.Drawable;
import com.x.opengl.kernel.Texture;
import com.x.components.node.View;

/**
 * 文字控件
 */
public class CopyOfTextView extends View
{

	protected int		mScaleType				= 0;
	private Drawable	mTextDrawable			= null;
	protected Drawable	mTextDrawableBackGround	= null;

	// 文字属性
	/** 文字 */
	private String		mText					= "";
	/** 文字型号 */
	private int			mTextSize				= 12;
	/** 文字颜色 */
	private int			mTextColor				= Color.BLACK;
	/** 文字阴影 */
	private Shadow		mTextShadow				= null;
	/** 文字透明度 */
	private int			mTextAlpha				= 255;
	/** 文字宽度 */
	private float		mTextScaleX				= 1;
	/** 字体 */
	private Typeface	mTextTypeface			= null;
	// /** 画文字的宽度 */
	// private int mTextDrawWidth;
	/** 文字对齐方式 */
	private Alignment	mTextAlignment			= Alignment.ALIGN_NORMAL;
	/** 文字对齐方式2 */
	private Align		mTextAlign				= Align.LEFT;
	/** 相对行间距 */
	private float		mTextRelativeSpacing	= 1;
	/** 绝对行间距 */
	private float		mTextAbsoluteSpacing	= 0;
	/** 文字的垂直布局方式 */
	private int			mTextGravity			= 0;
	/** 文字面片宽度 */
	private int			mTextWidth;
	/** 文字面片的高度 */
	private int			mTextHeight;
	/** 文字垂直方向的重心 */
	private TextGravity	mTGravity				= TextGravity.TOP;
	/** 文字最大行 */
	private int			mTextLines				= 1;
	/** 是否单行显示 */
	private boolean		isSingleLine			= false;
	/** 是否有阴影 */
	private boolean		isShadow				= false;
	/** 是否设定了行数限制 */
	private boolean		isLineText				= false;
	/** 是否打开了跑马灯 */
	private boolean		isMarquee				= false;
	/** 跑马灯 */
	private Marquee		mMarquee				= null;

	private boolean		isDrawMarquee			= true;
	private boolean		mNeedUpdate				= false;
	private boolean 	mNeedRecycleLast 		= true;

	public boolean isNeedRecycleLast() {
		return mNeedRecycleLast;
	}

	public void setNeedRecycleLast(boolean needRecycleLast) {
		this.mNeedRecycleLast = needRecycleLast;
	}

	private enum TextGravity
	{
		TOP, CENTER, BOTTOM
	}

	public CopyOfTextView()
	{
		super();
		mTextDrawable = new Drawable(1);
		addDrawable(mTextDrawable);

		initTextTypeFace();
	}

	public CopyOfTextView(boolean showTextDrawableBackGround)
	{
		super();
		if (showTextDrawableBackGround)
		{
			mTextDrawableBackGround = new Drawable(0);
			addDrawable(mTextDrawableBackGround);
		}
		mTextDrawable = new Drawable(1);
		addDrawable(mTextDrawable);
		initTextTypeFace();
	}

	private void initTextTypeFace() {
		setTextTypeface(WolfTypeface.IOSSTYLE);
	}

	public CopyOfTextView(String text)
	{
		this();
		mText = text;
		setText(mText);
	}

	
	/**
	 * 每次设置文字进去会请求渲染
	 * 
	 * @param text
	 */
	public void setText(String text)
	{
		if (!mText.equals(text))
		{
			mText = text;
			mNeedUpdate = true;
			if (isMarquee)
			{
				mTextMarqueeLayout = null;
				isDrawMarquee = true;
			}
			postInvalidate();
		}
	}

	@Override
	public void draw()
	{
		if (!mIsVisible)
		{
			return;
		}
		if (mNeedUpdate)
		{
			drawText();
		}
		super.draw();
	}

	/**
	 * 绘制文字
	 */
	private void drawText()
	{
		Bitmap bitmap = null;
		if (isLineText)
		{
			bitmap = drawTextWithCount(mTextWidth, mTextHeight, mText, mTextSize, mTextColor, mTextAlpha, mTextScaleX, mTextTypeface, mTextAlign, mTextRelativeSpacing,
					mTextAbsoluteSpacing, mTextLines, isShadow);
			mNeedUpdate = false;
		} else if (isSingleLine)
		{
			if (isMarquee && isDrawMarquee)
			{
				if (mTextMarqueeLayout == null)
				{
					mTextMarqueeLayout = getMarqueeLayout(mText, mTextColor, mTextAlpha, mTextScaleX, mTextSize, mTextTypeface, mTextAlignment);
				}

				if (mMarquee.width < mTextWidth)
				{
					bitmap = drawSinglineText(mTextWidth, mTextHeight, mText, mTextSize, mTextColor, mTextAlpha, mTextScaleX, mTextTypeface, mTextAlign, isShadow);
					mNeedUpdate = false;
					if (bitmap != null)
					{
						mTextDrawable.setTexture(bitmap);
						return;
					}
				}
				mMarquee.currentTime = System.currentTimeMillis();
				mMarquee.pixels = (int) (0.001 * mMarquee.pixlesPerSecond * (mMarquee.currentTime - mMarquee.startTime));
				bitmap = marquee(mMarquee.pixels);

				if (mMarquee.pixels > mMarquee.width + 65)
				{
					if (mMarquee.times > 0)
					{
						if (mMarquee.curMarqueeTimes < mMarquee.times)
						{
							mMarquee.curMarqueeTimes++;
						} else
						{
							mTextDrawable.setTexture(bitmap, true);
							return;
						}
					}

					new Thread(new Runnable()
					{

						@Override
						public void run()
						{
							try
							{
								// 初始计时
								isDrawMarquee = false;
								Thread.sleep(3000);
								isDrawMarquee = true;
								mMarquee.startTime = mMarquee.currentTime = System.currentTimeMillis();
								postInvalidate();
							} catch (InterruptedException e)
							{
								e.printStackTrace();
							}
						}
					}).start();
				} else
				{
					postInvalidate();
				}
			} else
			{
				bitmap = drawSinglineText(mTextWidth, mTextHeight, mText, mTextSize, mTextColor, mTextAlpha, mTextScaleX, mTextTypeface, mTextAlign, isShadow);
				if (!isMarquee)
				{
					mNeedUpdate = false;
				}
			}
		} else
		{
			bitmap = drawText(mTextWidth, mTextHeight, mText, mTextSize, mTextColor, mTextAlpha, mTextScaleX, mTextTypeface, mTextAlignment, mTextRelativeSpacing,
					mTextAbsoluteSpacing, isShadow);
			mNeedUpdate = false;
		}
		if (bitmap != null)
		{
			mTextDrawable.setTexture(bitmap, mNeedRecycleLast);
		}
	}
	/**
	 * 主动调用一次更新纹理
	 */
	public void updateDrawText()
	{
		drawText();
	}
	public void setTextResource(int resId)
	{
		String text = Director.sResourcer.getContext().getString(resId);
		setText(text);
	}

	@Override
	public void setWidth(float f)
	{
		super.setWidth(f);
		mTextWidth = (int) f;
	}

	@Override
	public void setHeight(float f)
	{
		super.setHeight(f);
		mTextHeight = (int) f;
	}

	public void resetHeight(float f)
	{
		this.setHeight(f);
	}

	/**
	 * 获取文字
	 * 
	 * @return 文字字符串
	 */
	public String getText()
	{
		return mText;
	}

	/**
	 * 设置文字型号
	 * 
	 * @param textSize
	 *            字号
	 */
	public void setTextSize(int textSize)
	{
		this.mTextSize = textSize;
	}

	/**
	 * 获取文字型号
	 * 
	 * @return 字号
	 */
	public int getTextSize()
	{
		return mTextSize;
	}

	/**
	 * 设置重心(九个方向.默认为左上)
	 * 
	 * @param gravity
	 * @see Gravity
	 */
	public void setTextGravity(int gravity)
	{
		this.mTextGravity = gravity;
		switch (mTextGravity)
		{
		case Gravity.CENTER:
			mTextAlign = Align.CENTER;
			mTextAlignment = Alignment.ALIGN_CENTER;
			mTGravity = TextGravity.CENTER;
			break;
		case Gravity.LEFT:
			mTextAlign = Align.LEFT;
			mTextAlignment = Alignment.ALIGN_NORMAL;
			break;
		case Gravity.TOP:
			mTGravity = TextGravity.TOP;
			break;
		case Gravity.RIGHT:
			mTextAlign = Align.RIGHT;
			mTextAlignment = Alignment.ALIGN_OPPOSITE;
			break;
		case Gravity.BOTTOM:
			mTGravity = TextGravity.BOTTOM;
			break;
		case Gravity.LEFT | Gravity.TOP:
			mTextAlign = Align.LEFT;
			mTextAlignment = Alignment.ALIGN_NORMAL;
			mTGravity = TextGravity.TOP;
			break;
		case Gravity.RIGHT | Gravity.TOP:
			mTextAlign = Align.RIGHT;
			mTextAlignment = Alignment.ALIGN_OPPOSITE;
			mTGravity = TextGravity.TOP;
			break;
		case Gravity.LEFT | Gravity.BOTTOM:
			mTextAlign = Align.LEFT;
			mTextAlignment = Alignment.ALIGN_NORMAL;
			mTGravity = TextGravity.BOTTOM;
			break;
		case Gravity.RIGHT | Gravity.BOTTOM:
			mTextAlign = Align.RIGHT;
			mTextAlignment = Alignment.ALIGN_OPPOSITE;
			mTGravity = TextGravity.BOTTOM;
			break;
		case Gravity.CENTER_VERTICAL:
			mTGravity = TextGravity.CENTER;
			break;
		case Gravity.CENTER_HORIZONTAL:
			mTextAlign = Align.CENTER;
			mTextAlignment = Alignment.ALIGN_CENTER;
			break;
		case Gravity.CENTER_HORIZONTAL | Gravity.TOP:
			mTextAlign = Align.CENTER;
			mTextAlignment = Alignment.ALIGN_CENTER;
			mTGravity = TextGravity.TOP;
			break;
		case Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM:
			mTextAlign = Align.CENTER;
			mTextAlignment = Alignment.ALIGN_CENTER;
			mTGravity = TextGravity.BOTTOM;
			break;
		case Gravity.CENTER_VERTICAL | Gravity.LEFT:
			mTextAlign = Align.LEFT;
			mTextAlignment = Alignment.ALIGN_NORMAL;
			mTGravity = TextGravity.CENTER;
			break;
		case Gravity.CENTER_VERTICAL | Gravity.RIGHT:
			mTextAlign = Align.RIGHT;
			mTextAlignment = Alignment.ALIGN_OPPOSITE;
			mTGravity = TextGravity.CENTER;
			break;
		default:
			break;
		}
		
	}

	/**
	 * 获取文字的重心
	 * 
	 * @return 文字的重心
	 */
	public int getTextGravity()
	{
		return mTextGravity;
	}

	/**
	 * 设置单行显示文字
	 * 
	 * @param isSingleLine
	 */
	public void setSingleLine(boolean isSingleLine)
	{
		this.isSingleLine = isSingleLine;
	}

	/**
	 * 文字是否是单行显示
	 * 
	 * @return
	 */
	public boolean isSingleLine()
	{
		return isSingleLine;
	}

	/**
	 * 设置流水灯的参数(如果调用了此方法,就不用再调用{@link #setMarquee(boolean)}了
	 * 
	 * @param times
	 *            循环多少次
	 * @param pixelsPerSecond
	 *            每秒多少像素 Pixels per second
	 */
	public void setMarquee(int times, int pixelsPerSecond)
	{
		isMarquee = true;
		mNeedUpdate = true;
		isDrawMarquee = true;
		initMarquee(times, pixelsPerSecond);
	}

	/**
	 * 设置是否是流水灯(如果调用了此方法,就不用再调用{@link #setMarquee(int, int)}了
	 * 
	 * @param isMarquee
	 */
	public void setMarquee(boolean isMarquee)
	{
		this.isMarquee = isMarquee;
		mNeedUpdate = true;
		isDrawMarquee = true;
		initMarquee(1, 100);
	}

	/**
	 * 是否设置了流水灯
	 * 
	 * @return 是否是流水灯
	 */
	public boolean isMarquee()
	{
		return isMarquee;
	}

	/**
	 * 设置文字是否有行数限定
	 * 
	 * @param isLineText
	 */
	public void setLineText(boolean isLineText)
	{
		this.isLineText = isLineText;
	}

	/**
	 * 返回文字是否限定了行数
	 * 
	 * @return 如果文字限定了显示行数,则返回true,否则返回false;
	 */
	public boolean isLineText()
	{
		return isLineText;
	}

	/**
	 * 设置文字最大行数(调用了此方法可以不用调用{@link #setLineText(boolean)})
	 * 
	 * @param mTextLines
	 */
	public void setTextMaxLines(int mTextLines)
	{
		this.mTextLines = mTextLines;
		isLineText = true;
	}

	/**
	 * 返回文字的行数(设定的最大行,并不是实际行数)
	 * 
	 * @return
	 */
	public int getTextLines()
	{
		return mTextLines;
	}

	/**
	 * 设置文字颜色
	 * 
	 * @param textColor
	 *            文字的颜色值
	 * @see Color
	 */
	public void setTextColor(int textColor)
	{
		this.mTextColor = textColor;
		mNeedUpdate = true;
	}

	/**
	 * 获取文字的颜色
	 * 
	 * @return 文字的颜色值
	 */
	public int getTextColor()
	{
		return mTextColor;
	}

	/**
	 * 设置文字颜色,调用该方法会主动将文字阴影设置为ture
	 * 
	 * @param shadowColor
	 *            文字的颜色值
	 * @see Color
	 */
	public void setTextShadowColor(int shadowColor)
	{
		isShadow = true;
		if (mTextShadow == null)
		{
			mTextShadow = new Shadow();
		}
		mTextShadow.color = shadowColor;
	}

	/**
	 * 设置文字阴影,调用该方法会主动将文字阴影设置为ture
	 * 
	 * @param radius
	 * @param dx
	 * @param dy
	 * @param color
	 */
	public void setTextShadow(float radius, float dx, float dy, int color)
	{
		isShadow = true;
		mTextShadow = new Shadow(radius, dx, dy, color);
	}

	/**
	 * 获取文字的颜色
	 * 
	 * @return 文字的颜色值
	 */
	public int getTextShadowColor()
	{
		return mTextShadow != null ? mTextShadow.color : Color.BLACK;
	}

	/**
	 * 设置是否有文字阴影
	 * 
	 * @param shadow
	 *            是否阴影
	 */
	public void setShadow(boolean shadow)
	{
		isShadow = shadow;
		if (isShadow && mTextShadow == null)
		{
			mTextShadow = new Shadow();
		}
	}

	/**
	 * 返回文字是否有阴影
	 * 
	 * @return 文字是否有阴影
	 */
	public boolean isShadow()
	{
		return isShadow;
	}

	/**
	 * 获取文字面片
	 * 
	 * @return 文字面片
	 */
	public Drawable getTextDrawable()
	{
		return mTextDrawable;
	}

	/**
	 * 设置文字面片
	 * 
	 * @param textDrawable
	 *            文字面片
	 */
	public void setTextDrawable(Drawable textDrawable)
	{
		this.mTextDrawable = textDrawable;
	}

	/**
	 * 获取文字透明度
	 * 
	 * @return 文字透明度
	 */
	public int getTextAlpha()
	{
		return mTextAlpha;
	}

	/**
	 * 设置文字透明度[0-1]
	 * 
	 * @param textAlpha
	 *            文字透明度值
	 */
	public void setTextAlpha(int textAlpha)
	{
		this.mTextAlpha = textAlpha;
	}

	/**
	 * 获取文字宽度
	 * 
	 * @return 文字的宽度值
	 */
	public float getTextScaleX()
	{
		return mTextScaleX;
	}

	/**
	 * 设置文字宽度(1为原来宽度,小于1为缩小,大于1为放大)
	 * 
	 * @param textScaleX
	 */
	public void setTextScaleX(float textScaleX)
	{
		this.mTextScaleX = textScaleX;
	}

	/**
	 * 获取字体
	 * 
	 * @return 字体
	 */
	public Typeface getTextTypeface()
	{
		return mTextTypeface;
	}

	/**
	 * 设置字体
	 * 
	 * @param textTypeface
	 *            字体
	 */
	public void setTextTypeface(int textTypefaceId)
	{
		this.mTextTypeface = WolfTypeface.getTypeface(textTypefaceId);;
	}

	// public int getTextDrawWidth()
	// {
	// return mTextDrawWidth; 
	// }
	//
	// public void setTextDrawWidth(int textDrawWidth)
	// {
	// this.mTextDrawWidth = textDrawWidth;
	// }

	// public Alignment getTextAlignment()
	// {
	// return mTextAlignment;
	// }
	//
	// public void setTextAlignment(Alignment textAlignment)
	// {
	// this.mTextAlignment = textAlignment;
	// }

	/**
	 * 获取文字相对行间距
	 * 
	 * @return 文字相对行间距
	 */
	public float getTextRelativeSpacing()
	{
		return mTextRelativeSpacing;
	}

	/**
	 * 设置文字相对行间距(1为倍行间距,效果为没有间距,2为两倍间距,效果为1倍字高的间距).实际行间距为绝对行间距加上相对行间距!
	 * 
	 * @param textRelativeSpacing
	 *            相对行间距倍数
	 * @see #setTextAbsoluteSpacing(float)
	 */
	public void setTextRelativeSpacing(float textRelativeSpacing)
	{
		this.mTextRelativeSpacing = textRelativeSpacing;
	}

	/**
	 * 获取绝对行间距
	 * 
	 * @return 绝对行间距像素值
	 */
	public float getTextAbsoluteSpacing()
	{
		return mTextAbsoluteSpacing;
	}

	/**
	 * 设置绝对行间距(像素),实际行间距为绝对行间距加上相对行间距!
	 * 
	 * @param textAbsoluteSpacing
	 *            绝对行间距像素值
	 * 
	 * @see #setTextRelativeSpacing(float)
	 * 
	 */
	public void setTextAbsoluteSpacing(float textAbsoluteSpacing)
	{
		this.mTextAbsoluteSpacing = textAbsoluteSpacing;
	}

	/**
	 * 设置文字与控件边缘之间的距离,请在调用setWidth(float)和setHeight(float)方法之前调用该方法,否则文字会被拉伸
	 * 
	 * @param left
	 *            文字离控件左边的距离
	 * @param top
	 *            文字离控件顶部的距离
	 * @param right
	 *            文字离控件右边的距离
	 * @param bottom
	 *            文字离控件底部的距离
	 */
	public void setTextPadding(int left, int top, int right, int bottom)
	{
		mTextWidth = (int) mViewWidth - left - right;
		mTextHeight = (int) mViewHeight - top - bottom;
		mTextDrawable.setWidth(mTextWidth / mViewWidth);
		mTextDrawable.setHeight(mTextHeight / mViewHeight);
		mTextDrawable.setTranslate((float) (left - right) / 2 / mViewWidth, (float) (bottom - top) / mViewHeight / 2, 0);
	}

	private Bitmap drawText(int width, int height, String text, int textSize, int color, int alpha, float textScaleX, Typeface textTypeface, Alignment textAlignment,
			float textRelaSpac, float textAbsSpac, boolean isShadow)
	{
		Bitmap textBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		textBitmap.eraseColor(Color.TRANSPARENT);// 文字背景色
		Canvas canvas = new Canvas(textBitmap);
		TextPaint textPaint = new TextPaint();
		// textPaint.setStyle(null);
		textPaint.setColor(color);// 设置文字颜色
		textPaint.setAntiAlias(true);// 抗锯齿
		textPaint.setAlpha(alpha);// 设置透明度
		// textPaint.setTextAlign(Align.CENTER);//设置绘制文字的重心.(对齐方式)
		textPaint.setTextScaleX(textScaleX);// 设置文字宽度
		textPaint.setTextSize(textSize);// 文字型号
		textPaint.setTypeface(textTypeface);// 设置字体
		if (isShadow)
		{
			setPaintShowdow(textPaint);
		}
		StaticLayout sl = new StaticLayout(text, textPaint, width, textAlignment, textRelaSpac, textAbsSpac, false);

		if (mTGravity == TextGravity.CENTER)
		{
			int h = sl.getHeight();
			if (h < height)
			{
				canvas.translate(0, (height - h) / 2);
			}
		} else if (mTGravity == TextGravity.BOTTOM)
		{
			int h = sl.getHeight();
			canvas.translate(0, height - h);
		}

		sl.draw(canvas);
		return textBitmap;
	}

	public int getAllTextHeight(String srcString){
		return getAllTextHeight(mTextWidth, mTextHeight, srcString, mTextSize, mTextColor, mTextAlpha, mTextScaleX, mTextTypeface, mTextAlign, mTextRelativeSpacing,
				mTextAbsoluteSpacing, mTextLines, isShadow);
	}
	public static List<String>   getLineStringList(Paint textPaint ,final String text,float width){
		
		String operateText  = text;
		List<String> mLineList = new ArrayList<String>();
		for (int i = 0; ; i++)
		{
			int n = textPaint.breakText(operateText, true, width, null);
			if (n == 0)
			{
				break;
			}
			if(n > operateText.length()){
				n = operateText.length();
			}
			String temp = operateText.substring(0, n);
			if (temp.contains("\n"))
			{
				n = temp.indexOf("\n");
				temp = temp.substring(0, n);
				operateText = operateText.substring(n + 1);
			} else
			{
				operateText = operateText.substring(n);
//				if (count - 1 == i)
//				{// 如果是最后一行，进行省略号处理
//					int t = textPaint.breakText(temp, true, width, null);// 测量文字,一行有多少个
//					if (0 < text.length()) // 剩余字符的长度大于0就进行省略号处理
//					{
//						temp = temp.substring(0, t - 2) + "...";// 末尾以".."结束
//					}
//				}
			}
			mLineList.add(temp);
		}
		return mLineList;
	}
	private int getAllTextHeight(int width, int height, String text, int textSize, int color, int alpha, float textScaleX, Typeface textTypeface, Align align,
			float textRelaSpac, float textAbsSpac, int count, boolean isShadow)
	{
		
		TextPaint textPaint = new TextPaint();
		textPaint.setTextScaleX(textScaleX);
		textPaint.setTextAlign(align);
		textPaint.setTextSize(textSize);
		textPaint.setTypeface(textTypeface);
		FontMetricsInt fontMetricsInt = textPaint.getFontMetricsInt();
		int h = fontMetricsInt.descent - fontMetricsInt.ascent;
		int c = getLineStringList(textPaint, text, width).size();
		
		Log.d("debug","lines = "+c);
		int H = 0;
		if (c > 0)
		{
			// H = (int) (c * h + (c - 1) * (h * (textRelaSpac - 1) +
			// textAbsSpac));
			H = (int) (c * h * textRelaSpac - h * (textRelaSpac - 1) + (c - 1) * textAbsSpac);
		}
		return 	H;
	}

	private Bitmap drawTextWithCount(int width, int height, String text, int textSize, int color, int alpha, float textScaleX, Typeface textTypeface, Align align,
			float textRelaSpac, float textAbsSpac, int count, boolean isShadow)
	{
		Bitmap textBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		textBitmap.eraseColor(Color.TRANSPARENT);// 文字背景色
		Canvas canvas = new Canvas(textBitmap);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG|Paint.ANTI_ALIAS_FLAG));
		
		TextPaint textPaint = new TextPaint();
		textPaint.setColor(color);
		textPaint.setAntiAlias(true);
		textPaint.setAlpha(alpha);
		textPaint.setTextScaleX(textScaleX);
		textPaint.setTextAlign(align);
		textPaint.setTextSize(textSize);
		textPaint.setTypeface(textTypeface);
		if (isShadow)
		{
			setPaintShowdow(textPaint);
		}

		int baseX = 0;
		if (align == Align.CENTER)
		{
			baseX = width / 2;
		} else if (align == Align.RIGHT)
		{
			baseX = width;
		}

		FontMetricsInt fontMetricsInt = textPaint.getFontMetricsInt();
		int h = fontMetricsInt.descent - fontMetricsInt.ascent;
		ArrayList<String> texts = new ArrayList<String>(count);
		int c = 0;
		for (int i = 0; i < count; i++)
		{
			int n = textPaint.breakText(text, true, width, null);
			if (n == 0)
			{
				break;
			}
			String temp = text.substring(0, n);
			if (temp.contains("\n"))
			{
				n = temp.indexOf("\n");
				temp = temp.substring(0, n);
				text = text.substring(n + 1);
			} else
			{
				text = text.substring(n);
				if (count - 1 == i)
				{// 如果是最后一行，进行省略号处理
					int t = textPaint.breakText(temp, true, width, null);// 测量文字,一行有多少个
					if (0 < text.length()) // 剩余字符的长度大于0就进行省略号处理
					{
						temp = temp.substring(0, t - 2) + "...";// 末尾以".."结束
					}
				}
			}
			texts.add(temp);
			c++;
		}
		int H = 0;
		if (c > 0)
		{
			// H = (int) (c * h + (c - 1) * (h * (textRelaSpac - 1) +
			// textAbsSpac));
			H = (int) (c * h * textRelaSpac - h * (textRelaSpac - 1) + (c - 1) * textAbsSpac);
		}
		int baseY = 0;
		if (mTGravity == TextGravity.TOP || H > height)
		{
			baseY = -fontMetricsInt.ascent;
		} else
		{
			if (mTGravity == TextGravity.CENTER)
			{
				baseY = (height - H) / 2 - fontMetricsInt.ascent;
			} else if (mTGravity == TextGravity.BOTTOM)
			{
				baseY = height - H - fontMetricsInt.ascent;
			}
		}
		for (int i = 0; i < texts.size(); i++)
		{
			canvas.drawText(texts.get(i), baseX, baseY, textPaint);
			baseY += h * textRelaSpac + textAbsSpac;
		}
		return textBitmap;
	}

	private Bitmap drawSinglineText(int width, int height, String text, int textSize, int color, int alpha, float textScaleX, Typeface textTypeface, Align align, boolean isShadow)
	{

		if (isMarquee && !isDrawMarquee)
		{
			align = Align.LEFT;
		}

		TextPaint textPaint = new TextPaint();
		textPaint.setColor(color);
		textPaint.setAntiAlias(true);
		textPaint.setAlpha(alpha);
		textPaint.setTextAlign(align);
		textPaint.setTextScaleX(textScaleX);
		textPaint.setTextSize(textSize);
		textPaint.setTypeface(textTypeface);
		if (isShadow)
		{
			setPaintShowdow(textPaint);
		}

		int baseX = 0;
		int baseY = 0;
		FontMetricsInt fontMetricsInt = textPaint.getFontMetricsInt();
		if (align == Align.CENTER)
		{
			baseX = width / 2;
		} else if (align == Align.RIGHT)
		{
			baseX = width;
		}

		if (mTGravity == TextGravity.TOP)
		{
			baseY = -fontMetricsInt.ascent;
		} else if (mTGravity == TextGravity.CENTER)
		{
			int h = fontMetricsInt.descent - fontMetricsInt.ascent;
			baseY = height - (height - h) / 2 - fontMetricsInt.bottom;
		} else
		{
			baseY = height - fontMetricsInt.bottom;
		}
		int t = textPaint.breakText(text, true, width, null);// 测量文字,一行有多少个
		if (t < text.length() && !isMarquee)
		{
			text = text.substring(0, t - 2) + "...";// 末尾以".."结束
		}
		Bitmap textBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		textBitmap.eraseColor(Color.TRANSPARENT);// 文字背景色
		Canvas canvas = new Canvas(textBitmap);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG|Paint.ANTI_ALIAS_FLAG));
		canvas.drawText(text, baseX, baseY, textPaint);

		return textBitmap;
	}

	private void setPaintShowdow(Paint textPaint) {
		textPaint.setShadowLayer(mTextShadow.radius, mTextShadow.dx, mTextShadow.dy, mTextShadow.color);	
		
	}

	private StaticLayout getMarqueeLayout(String text, int color, int alpha, float textScaleX, int textSize, Typeface textTypeface, Alignment textAlignment)
	{
		text = text.replaceAll("[\r\n]|[\n]", "");
		StaticLayout sl = null;
		TextPaint textPaint = new TextPaint();
		textPaint.setColor(color);
		textPaint.setAntiAlias(true);
		textPaint.setAlpha(alpha);
		textPaint.setTextScaleX(textScaleX);
		textPaint.setTextSize(textSize);
		textPaint.setTypeface(textTypeface);
		System.out.println("text:" + text);
		mMarquee.width = (int) textPaint.measureText(text);
		System.out.println("marquee width" + mMarquee.width);
		sl = new StaticLayout(text, textPaint, mMarquee.width, textAlignment, 1.0f, 0, false);
		return sl;
	}

	private StaticLayout	mTextMarqueeLayout	= null;

	private Bitmap marquee(int x)
	{
		Bitmap textBitmap = Bitmap.createBitmap(mTextWidth, mTextHeight, Config.ARGB_8888);
		// textBitmap.eraseColor(Color.GREEN);
		Canvas canvas = new Canvas(textBitmap);
		int baseY = 0;
		int h = mTextMarqueeLayout.getHeight();
		if (h < mTextHeight)
		{
			if (mTGravity == TextGravity.CENTER)
			{
				baseY = (mTextHeight - h) / 2;
			} else if (mTGravity == TextGravity.BOTTOM)
			{
				baseY = mTextHeight - h;
			}
		}
		canvas.translate(-x, baseY);
		mTextMarqueeLayout.draw(canvas);

		if (x > mMarquee.width + 70 - mTextWidth)
		{
			int nextX = mMarquee.width + 70;
			canvas.translate(nextX, 0);
			mTextMarqueeLayout.draw(canvas);
		}
		return textBitmap;
	}

	class Shadow
	{
		public Shadow()
		{
			radius = 3;
			dx = 2;
			dy = 2;
			color = Color.BLACK;
		}

		public Shadow(float radius, float dx, float dy, int color)
		{
			this.radius = radius;
			this.dx = dx;
			this.dy = dy;
			this.color = color;
		}

		float	radius;
		float	dx;
		float	dy;
		int		color;
	}

	class Marquee
	{
		int		times			= 1;
		int		pixlesPerSecond	= 100;
		int		pixels			= 0;
		long	startTime		= 0;
		long	currentTime		= 0;
		int		width			= 0;
		int		curMarqueeTimes	= 0;
	}

	private void initMarquee(int times, int pps)
	{
		mMarquee = new Marquee();
		if (mMarquee.times > 0)
		{
			mMarquee.times = times;
		} else
		{
			mMarquee.times = 0;
		}
		mMarquee.curMarqueeTimes = 1;
		mMarquee.pixlesPerSecond = pps;
		mTextMarqueeLayout = null;
		mMarquee.startTime = mMarquee.currentTime = System.currentTimeMillis();
	}

	public Texture getTexture() {
		return mTextDrawable.getTexture();
	}


}
