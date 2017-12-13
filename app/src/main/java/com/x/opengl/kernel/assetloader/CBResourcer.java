package com.x.opengl.kernel.assetloader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.opengl.EGL14;
import android.opengl.GLES11;
import android.os.Handler;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import com.jni.assimp.AiScene;
import com.x.Director;
import com.x.GLEnvirnment;
import com.x.EngineGLView;
import com.x.opengl.kernel.ObjDrawable;
import com.x.opengl.kernel.SkyDrawable;
import com.x.opengl.kernel.Texture;
import com.x.opengl.utils.Utils;

/**
 * 资源解析器
 * @date   2013-08-08 11:10:10
 */
@SuppressLint("NewApi") public class CBResourcer {
	
	public static final String TAG = "CBResourcer";
	
	private Handler         mHandler     = null;
	private Context         mContext     = null;
	private EngineGLView       mWolfView   = null;
	private GLEnvirnment      mGLEnvirnment  = null;
	private ExecutorService mLoadingPool = Executors.newFixedThreadPool(3);
	private Texture mNoneTexture;//一个透明空白的纹理
	private Texture	mBoundTestTexture;//若你想看看你自己的控件所占的位置有多大，可以将这个纹理设置为背景 
	
	public CBResourcer(Context context) {
		mContext = context;
//		mEngineInfo = new EngineInfo();
	}
	
	
	public CBResourcer(Context context, EngineGLView GLView) {
		this(context);
		setWolfView(GLView);
	}
	
	
	public void setWolfView(EngineGLView GLView) {
		mWolfView = GLView;
	}
	
	public void setGLEnviroment(GLEnvirnment engineInfo) {
		mGLEnvirnment = engineInfo;
	}
	
	public Context getContext() {
		return mContext;
	}
	
	public EngineGLView getWolfView() {
		return mWolfView;
	}
	
	public GLEnvirnment getEngineInfo() {
		return mGLEnvirnment;
	}
	
	public long getCurrentTime() {
		return mGLEnvirnment.getCurrentTime();
	}
	
	public Texture generateTextTexture(int resid) {
		return generateTextTexture(mContext.getResources().getString(resid), 12, Color.BLACK, false);
	}
	
	public Texture generateTextTexture(String text) {
		return generateTextTexture(text, 12, Color.BLACK, false);
	}
	
	public Texture generateTextTexture(int resid, boolean isMultiLine) {
		return generateTextTexture(mContext.getResources().getString(resid), 12, Color.BLACK, isMultiLine);
	}
	
	public Texture generateTextTexture(String text, boolean isMultiLine) {
		return generateTextTexture(text, 12, Color.BLACK, isMultiLine);
	}
	
	public Texture generateTextTexture(int resid, int textSize, int textColor, boolean isMultiLine) {
		return generateTextTexture(mContext.getResources().getString(resid), textSize, textColor, Typeface.DEFAULT, isMultiLine);
	}
	
	public Texture generateTextTexture(String text, int textSize, int textColor, boolean isMultiLine) {
		return generateTextTexture(text, textSize, textColor, Typeface.DEFAULT, isMultiLine);
	}
	
	public Texture generateTextTexture(int resid, int textSize, int textColor, Typeface typeface, boolean isMultiLine) {
		return generateTextTexture(mContext.getResources().getString(resid), textSize, textColor, typeface, isMultiLine);
	}
	
	public Texture generateTextTexture(String text, int textSize, int textColor, Typeface typeface, boolean isMultiLine) {
		Rect rect = new Rect();
		
		if (text != null && !text.isEmpty()) {
			float width  = 16;
			float height = 16;
			Paint paint  = new Paint();
			
			text = text.trim();
			paint.setTextSize(textSize);
			paint.getTextBounds(text, 0, text.length(), rect);
			
			width = Utils.roundTo2Pow(rect.width());
			height = Utils.roundTo2Pow(rect.height());
			
			return generateTextTexture(text, textSize, textColor, typeface, width, height, isMultiLine);
		}else{
			return null;
		}
	}
	
	public Texture generateTextTexture(
			int resid, int textSize, int textColor, Typeface typeface, 
			float width, float height, boolean isMultiLine) {
		return generateTextTexture(mContext.getResources().getString(resid), textSize, textColor, typeface, width, height, isMultiLine, 0, 0, 0, 0, null);
	}
	
	public Texture generateTextTexture(
			String text, int textSize, int textColor, Typeface typeface, 
			float width, float height, boolean isMultiLine) {
		return generateTextTexture(text, textSize, textColor, typeface, width, height, isMultiLine, 0, 0, 0, 0, null);
	}
	
	public Texture generateTextTexture(
			int resid, int textSize, int textColor, Typeface typeface,
			boolean isMultiLine,
			float shadowRadius, float shadowDeltaX, float shadowDeltaY, int shadowColor){
		return generateTextTexture(mContext.getResources().getString(resid), textSize, textColor, typeface, isMultiLine, shadowRadius, shadowDeltaX, shadowDeltaY, shadowColor);
	}
	
	public Texture generateTextTexture(
			String text, int textSize, int textColor, Typeface typeface,
			boolean isMultiLine,
			float shadowRadius, float shadowDeltaX, float shadowDeltaY, int shadowColor){
		Rect rect = new Rect();
		
		if (text != null && !text.isEmpty()) {
			float width  = 16;
			float height = 16;
			Paint paint  = new Paint();
			
			text = text.trim();
			paint.setTextSize(textSize);
			paint.getTextBounds(text, 0, text.length(), rect);
			
			width = Utils.roundTo2Pow(rect.width());
			height = Utils.roundTo2Pow(rect.height());
			
			return generateTextTexture(text, textSize, textColor, typeface, width, height, isMultiLine, shadowRadius, shadowDeltaX, shadowDeltaY, shadowColor, null);
		}else{
			return null;
		}
	}
	
	public Texture generateTextTexture(
			int resid, int textSize, int textColor, Typeface typeface,
			boolean isMultiLine, Shader shader,
			float shadowRadius, float shadowDeltaX, float shadowDeltaY, int shadowColor){
		return generateTextTexture(mContext.getResources().getString(resid), textSize, textColor, typeface, isMultiLine, shader, shadowRadius, shadowDeltaX, shadowDeltaY, shadowColor);
	}
	
	public Texture generateTextTexture(
			String text, int textSize, int textColor, Typeface typeface,
			boolean isMultiLine, Shader shader,
			float shadowRadius, float shadowDeltaX, float shadowDeltaY, int shadowColor){
		Rect rect = new Rect();
		
		if (text != null && !text.isEmpty()) {
			float width  = 16;
			float height = 16;
			Paint paint  = new Paint();
			
			text = text.trim();
			paint.setTextSize(textSize);
			paint.getTextBounds(text, 0, text.length(), rect);
			
//			width = Utils.roundTo2Pow(rect.width());
//			height = Utils.roundTo2Pow(rect.height());
			width = rect.width() + 4 * (shadowRadius + Math.abs(shadowDeltaX));
			height = rect.height() + 4 * (shadowRadius + Math.abs(shadowDeltaY));
			
			return generateTextTexture(text, textSize, textColor, typeface, width, height, isMultiLine, shadowRadius, shadowDeltaX, shadowDeltaY, shadowColor, shader);
		}else{
			return null;
		}
	}
	
	public Texture generateTextTexture(
			int resid, int textSize, int textColor, Typeface typeface, 
			float width, float height, boolean isMultiLine,
			float shadowRadius, float shadowDeltaX, float shadowDeltaY, int shadowColor) {
		return generateTextTexture(mContext.getResources().getString(resid), textSize, textColor, typeface, width, height, isMultiLine, shadowRadius, shadowDeltaX, shadowDeltaY, shadowColor, null);
	}
	
	public Texture generateTextTexture(
			String text, int textSize, int textColor, Typeface typeface, 
			float width, float height, boolean isMultiLine,
			float shadowRadius, float shadowDeltaX, float shadowDeltaY, int shadowColor) {
		return generateTextTexture(text, textSize, textColor, typeface, width, height, isMultiLine, shadowRadius, shadowDeltaX, shadowDeltaY, shadowColor, null);
	}
	
	public Texture generateTextTexture(
			int resid, int textSize, int textColor, Typeface typeface,
			float width, float height, boolean isMultiLine,
			float shadowRadius, float shadowDeltaX, float shadowDeltaY, int shadowColor,
			Shader textShader) {
		return generateTextTexture(
				mContext.getResources().getString(resid), textSize, textColor, typeface, width, height, isMultiLine, shadowRadius, shadowDeltaX, shadowDeltaY, shadowColor, textShader);
	}
	
	/**
	 * 生成文字贴图
	 * @param text 文字
	 * @param textSize 字号
	 * @param textColor 文字颜色
	 * @param typeface 文字样式
	 * @param width 贴图宽度
	 * @param height 贴图高度
	 * @param isMultiLine 自动换行，如果启用自动换行，则文字高度会被覆盖
	 * @param shadowRadius 阴影半径
	 * @param shadowDeltaX 阴影偏移
	 * @param shadowDeltaY 阴影偏移
	 * @param shadowColor 阴影颜色
	 * @param textShader 渲染器
	 * @return
	 */
	public Texture generateTextTexture(
			String text, int textSize, int textColor, Typeface typeface,
			float width, float height, boolean isMultiLine,
			float shadowRadius, float shadowDeltaX, float shadowDeltaY, int shadowColor,
			Shader textShader) {
		Texture         texture    = null;
		Bitmap          bitmap     = null;
		Canvas          canvas     = null;
		Paint           paint      = null;
		Vector<Integer> lines      = new Vector<Integer>();
		
		if (text != null && !text.isEmpty()) {
			text.trim();
			
			paint  = new Paint();

			if (isMultiLine) {
				int          size       = 0;
				int          loopSize   = 0;
				int          lineWidth  = 0;
				int          lineHeight = 0;
				int          lineSpace  = 0;
				Rect         rect       = new Rect();
				String       subString  = text;
				TextPaint    textPaint  = null;
				StaticLayout textLayout = null;
				
				while (true) {
					int length = subString.length();
					if (size < length) {
						subString = subString.substring(size);
						size = paint.breakText(subString, true, width, null);
						lines.add(size);
					}else if (length > 0){
						lines.add(length);
						break;
					}else{
						break;
					}
				}
				
				paint.setAntiAlias(true);
				paint.setColor(textColor);
				paint.setTypeface(typeface);
				paint.setDither(true);
				paint.setTextSize(textSize);
				paint.setShader(textShader);
				paint.setShadowLayer(shadowRadius, shadowDeltaX, shadowDeltaY, shadowColor);
				paint.getTextBounds(text, 0, text.length(), rect);
				
				textPaint  = new TextPaint(paint);
				loopSize   = lines.size();
				lineWidth  = (int) Math.floor(width);
				lineHeight = rect.height();
				lineSpace  = (int) paint.getFontSpacing();
				bitmap     = Bitmap.createBitmap((int)width, (int)(rect.height() * loopSize + paint.getFontSpacing() * (loopSize - 1)) , Config.ARGB_8888);
				textLayout = new StaticLayout(text, textPaint, bitmap.getWidth(), Alignment.ALIGN_CENTER, 1.25f, 0, false);
				canvas     = new Canvas(bitmap);
				
				textLayout.draw(canvas);
				
//				int progress   = 0;
//				int lineCharSize = 0;
//				for (int i = 0; i < loopSize; i++) {
//					lineCharSize = lines.get(i);
//					canvas.drawText(
//							text.subSequence(progress, progress + lineCharSize).toString(),
//							2 * (shadowRadius + Math.abs(shadowDeltaX)),
//							(float)(lineHeight * (i + 1) + lineSpace * i) - 2 * (shadowRadius - Math.abs(shadowDeltaY)),
//							paint);
//					progress = lineWidth;
//				}
			}else{
				Rect        lineRect    = new Rect();
				FontMetrics fontMetrics = paint.getFontMetrics();
				
				paint.setAntiAlias(true);
				paint.setColor(textColor);
				paint.setTypeface(typeface);
				paint.setDither(false);
				paint.setTextAlign(Align.CENTER);
				paint.setTextSize(textSize);
				paint.setShader(textShader);
				paint.setShadowLayer(shadowRadius, shadowDeltaX, shadowDeltaY, shadowColor);
				paint.getTextBounds(text, 0, text.length(), lineRect);
				
				//float baselineOffset = fontMetrics.bottom;
				bitmap = Bitmap.createBitmap(lineRect.width() + (int)(2 * (shadowRadius + Math.abs(shadowDeltaX)) + 0.5), lineRect.height() + (int)(2 * (shadowRadius + Math.abs(shadowDeltaY)) + 0.5), Config.ARGB_8888);
				canvas = new Canvas(bitmap);
				canvas.drawText(
						text, 
						bitmap.getWidth() / 2f,
						(bitmap.getHeight() / 2f - lineRect.height() / 2f) + lineRect.height() / 2f + (lineRect.height() / 2f - Math.abs(fontMetrics.bottom)),
						paint);
			}
			
			texture = generateTexture(bitmap);
		}
		
		return texture;
	}
	
	public Texture generateTexture(int resid) {
		Texture texture   = null;
		Bitmap  bitmap    = null;

		bitmap = BitmapFactory.decodeResource(mContext.getResources(), resid);

		
		texture = generateTexture(bitmap);
		
		return texture;
	}
	
	public Texture generateTextureAsyn(Bitmap bmp) {
		Texture texture = new Texture();
		if(bmp == null)
			return null;
		
		texture.setWidth(bmp.getWidth());
		texture.setHeight(bmp.getHeight());
		mLoadingPool.execute(new AsyncTextureThreadRunnable(Director.sResourcer.getContext(), bmp, texture));
		
		return texture;
	}

	public Texture generateTexture(Bitmap bmp) {
		return Director.sGLESVersion.generateTexture(bmp);
	}
	public Texture generateOES_Texture( ) {
		return Director.sGLESVersion.generateOES_Texture( );
	}
	public Texture generateTexture(Bitmap bmp ,boolean recycle ) {
		return Director.sGLESVersion.generateTexture(bmp,recycle);
	}
	public Texture generateTextureButNotRecycleBitmap(Bitmap bmp) {
		return Director.sGLESVersion.generateTexture(bmp,false);
	}
	
	public Texture generateTextureOldVersion(int resid) {
		
		Texture texture   = null;
		Bitmap  bitmap    = null;
		
		bitmap = BitmapFactory.decodeResource(mContext.getResources(), resid);
		texture = generateTextureOldVersion(bitmap);
		
		return texture;
	}
	
	public Texture generateTextureAsynOldVersion(final int resid) {
		final Texture texture = new Texture();
		
		// 异步加载
		mLoadingPool.execute(new Runnable() {
			
			@Override
			public void run() {
				final Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), resid);
				Director.sResourcer.getWolfView().queueEvent(new Runnable() {
					
					@Override
					public void run() {
						Texture tempTexture = generateTextureOldVersion(bitmap);
						texture.setWidth((int)tempTexture.getWidth());
						texture.setHeight((int)tempTexture.getHeight());
						texture.setTextureID(tempTexture.getTextureID());
					}
				});
			}
			
		});
		
		// 同步返回
		return texture;
	}
	
	public Texture generateTextureOldVersion(Bitmap bmp) {
		return Director.sGLESVersion.generateTextureOldVersion(bmp);
	}
	
	public Texture generateVideoTexture() {
		return Director.sGLESVersion.generateVideoTexture();
	}
	
	public void recycleTexture(Texture texture) {
		if (texture != null) {
			recycleTextures(new int[]{texture.getTextureID()});
		}
	}
	
	public void recycleTexture(int textureID) {
		recycleTextures(new int[]{textureID});
	}
	
	public void recycleTextures(List<Texture> textures) {
		if (textures != null && !textures.isEmpty()) {
			int[] textureIDs = new int[textures.size()];
			for (int i = 0; i < textureIDs.length; i++) {
				Texture texture = textures.get(i);
				if (texture != null) {
					textureIDs[i] = texture.getTextureID();
					texture.setTextureID(0);
				}else{
					textureIDs[i] = 0;
				}
			}
			
			recycleTextures(textureIDs);
		}
	}
	
	public void recycleTextures(Texture[] textures) {
		if (textures != null && textures.length > 0) {
			int[] textureIDs = new int[textures.length];
			for (int i = 0; i < textureIDs.length; i++) {
				if (textures[i] != null) {
					textureIDs[i] = textures[i].getTextureID();
					textures[i].setTextureID(0);
				}else{
					textureIDs[i] = 0;
				}
			}
			
			recycleTextures(textureIDs);
		}
	}
	
	public void recycleTextures(int[] textureIDs) {
		if (textureIDs != null && textureIDs.length > 0) {
			
			Director.getInstance().sGLESVersion.glDeleteTextures(textureIDs);
			
		}
	}
	
	public FloatBuffer floatBuffer(float[] src) {
		FloatBuffer floatBuffer;
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(src.length * 4);  
		byteBuffer.order(ByteOrder.nativeOrder());  
		floatBuffer = byteBuffer.asFloatBuffer();  
		floatBuffer.put(src);  
		floatBuffer.position(0);  

		return floatBuffer;  
	}  

	public IntBuffer intBuffer(int[] src) {  
		IntBuffer intBuffer;
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(src.length * 4);  
		byteBuffer.order(ByteOrder.nativeOrder());  
		intBuffer = byteBuffer.asIntBuffer();  
		intBuffer.put(src);  
		intBuffer.position(0);  
		return intBuffer;  
	}  
	
	public ShortBuffer shortBuffer(short[] src) {
		ShortBuffer shortBuffer;
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(src.length * 4);  
		byteBuffer.order(ByteOrder.nativeOrder());  
		shortBuffer = byteBuffer.asShortBuffer();  
		shortBuffer.put(src);  
		shortBuffer.position(0);  
		return shortBuffer;  
	}
	
	public ByteBuffer byteBuffer(byte[] src) {  
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(src.length);  
		byteBuffer.order(ByteOrder.nativeOrder());    
		byteBuffer.put(src);  
		byteBuffer.position(0);  
		return byteBuffer;  
	}
	
	public ObjDrawable loadObjModel(String assetFile) {
		ObjLoader objLoader = new ObjLoader(this, mContext);
		objLoader.loadAsset(assetFile);
		Log.d("ming", "going -     loadObjModel = " );
		return  Model.assembleObj(objLoader);
	}

	public SkyDrawable loadSkyBox(String assetFile) {
		ObjLoader objLoader = new ObjLoader(this, mContext);
		objLoader.loadAsset(assetFile);
		Log.d("ming", "going -     loadObjModel = " );
		return  Model.assembleSkyBox(objLoader);
	}

	public SkyDrawable generateSkyBox()
	{
		ObjLoader objLoader = new ObjLoader(this, mContext);
		objLoader.loadSkyBox();
		Log.d("ming", "going -     generateSkyBox = " );

		return Model.assembleSkyBox(objLoader);
	}


	public ObjDrawable loadModelByAssimpAPI(Context context ,String[] assetFiles){
		AssimpLoader assimpLoader = new AssimpLoader();

		Log.d("ming", "loadModelByAssimpAPI =============== = " );
		AiScene aiScene = assimpLoader.loadAsset(assetFiles);
		Log.d("ming", "assembleScene =============== = " );
		return  Model.assembleScene(context,aiScene);
	}
	

	public ObjLoader getObjLoader(String assetFile) {
		ObjLoader objLoader = new ObjLoader(this, mContext  );
		return objLoader ;
	}

	///////////////////////////////////////////////////////////////////////
	//  异步贴图加载线程
	///////////////////////////////////////////////////////////////////////
	class AsyncTextureThreadRunnable implements Runnable {
		private boolean          mIsInitialized   = false;
	    private GLEnvirnment     mGLEnvirnment    = null;
	    private Context          mContext         = null;
		private Bitmap           mBitmap          = null;
		private Texture          mTexture         = null;
		private int              mBufferAttribs[] = { 
										EGL10.EGL_WIDTH,  1,
										EGL10.EGL_HEIGHT, 1,
										EGL14.EGL_TEXTURE_TARGET, EGL14.EGL_NO_TEXTURE, 
						                EGL14.EGL_TEXTURE_FORMAT, EGL14.EGL_NO_TEXTURE,
						                EGL10.EGL_NONE };
		
		public AsyncTextureThreadRunnable(Context context, Bitmap bitmap, Texture texture) {
			mBitmap    = bitmap;
			mTexture   = texture;
			mContext   = context;
		}
		
		private boolean start() {
			GLEnvirnment wolfGLEnvirnment = Director.sResourcer.getWolfView().getGLEnvirnment();
			if (wolfGLEnvirnment != null) {
				mGLEnvirnment = new GLEnvirnment();
				mGLEnvirnment.mEgl = wolfGLEnvirnment.mEgl;
				mGLEnvirnment.mEglConfig = wolfGLEnvirnment.mEglConfig;
				mGLEnvirnment.mEglDisplay = wolfGLEnvirnment.mEglDisplay;
				mGLEnvirnment.mEglContext = mGLEnvirnment.mEgl.eglCreateContext(wolfGLEnvirnment.mEglDisplay, wolfGLEnvirnment.mEglConfig, wolfGLEnvirnment.mEglContext, null);
			
				EGLSurface localSurface = mGLEnvirnment.mEgl.eglCreatePbufferSurface(mGLEnvirnment.mEglDisplay, mGLEnvirnment.mEglConfig, mBufferAttribs);
				mGLEnvirnment.mEgl.eglMakeCurrent(mGLEnvirnment.mEglDisplay, localSurface, localSurface, mGLEnvirnment.mEglContext);
				
				return true;
			}else{
				return false;
			}
		}
		
		@Override
		public void run() {
			if (!mIsInitialized) {
				mIsInitialized = start();
			}
			
			if (mTexture == null || mBitmap == null) {
				return;
			}
			
			if (!mBitmap.isRecycled()) {
				int[] textureID = new int[1];
				
				GLES11.glEnable(GL10.GL_TEXTURE_2D);
				GLES11.glActiveTexture(GL10.GL_TEXTURE0);
				GLES11.glGenTextures(1, textureID, 0);
				GLES11.glBindTexture(GL10.GL_TEXTURE_2D, textureID[0]);
				GLES11.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
				GLES11.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR);
				GLES11.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
				GLES11.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
				GLES11.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
				
				// 输出像素
				int   x = 0, y = 0; 
				int   width     = mBitmap.getWidth(); 
				int   height    = mBitmap.getHeight();
				int   byteIndex = 0; 
				int[] pixels    = new int[width * height]; 
				mBitmap.getPixels(pixels, 0, width, x, y, width, height);
				
			    byte[] pixelComponents = new byte[pixels.length * 4]; 
			    for (int i = 0; i < pixels.length; i++) { 
			        int pixel = pixels[i]; 
					pixelComponents[byteIndex++] = (byte) ((pixel >> 16) & 0xFF); // red 
					pixelComponents[byteIndex++] = (byte) ((pixel >> 8) & 0xFF);  // green 
					pixelComponents[byteIndex++] = (byte) ((pixel) & 0xFF);       // blue 
					pixelComponents[byteIndex++] = (byte) (pixel >> 24);          // alpha 
				} 
				ByteBuffer pixelBuffer = ByteBuffer.wrap(pixelComponents); 
				GLES11.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, mBitmap.getWidth(), mBitmap.getHeight(), 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, pixelBuffer);
				GLES11.glDisable(GL10.GL_TEXTURE_2D);
				mBitmap.recycle();
				
				mTexture.setWidth(width);
				mTexture.setHeight(height);
				mTexture.setTextureID(textureID[0]);
			}else{
			}
		}
	}
	public Texture getNoneTexture() {
		if(mNoneTexture == null){
			mNoneTexture	= Director.getInstance().sResourcer.generateTexture(Bitmap.createBitmap(1, 1, Config.ARGB_8888));
			mNoneTexture.setCanRecycle(false);
		}
		return mNoneTexture	;
	}
	
	public Texture getBoundTestTexture() {
		if(mBoundTestTexture == null){
			Bitmap bitmap = Bitmap.createBitmap(1, 1, Config.ARGB_8888);
			bitmap.eraseColor(Color.argb(60, 255, 0, 0));
			mBoundTestTexture	= Director.getInstance().sResourcer.generateTexture(bitmap);
			mBoundTestTexture.setCanRecycle(false);
		}
		return mBoundTestTexture	;
	}
}
