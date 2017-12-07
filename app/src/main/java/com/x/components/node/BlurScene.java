package com.x.components.node;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.HandlerThread;

import com.x.Director;
import com.x.opengl.kernel.EngineConstanst;
import com.x.components.task.PostRunnable;


public class BlurScene extends XScene{

	
	protected ViewGroup mViewGroup = new ViewGroup();
	protected HandlerThread	mHandlerThread;
	protected Handler	mHandlerRefresh;
	protected float mTarget = 1f;
	protected boolean mGoBlur = false;
	protected float	mAlpha;
	protected boolean	mContinue = false;;
	
	public BlurScene(){

		super();


		SetDebugName("BlurScene");
		mHandlerThread = new HandlerThread("run_loop22");
		mHandlerThread.start();
		mHandlerRefresh = new Handler(mHandlerThread.getLooper());


		mViewGroup.setWidth(EngineConstanst.REFERENCE_SCREEN_WIDTH);
		mViewGroup.setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
		
		addLayer(mViewGroup);
	}
	@Override
	public void update() {

//		Log.d("BLUR", "update() mContinue = "+mContinue+",,,mIsBlur = "+mIsBlur);
		if(mContinue){
			if(mGoBlur){
				
					mAlpha  += 0.1f;
					if(mAlpha > mTarget){
						mAlpha =  mTarget;
					};
					mViewGroup.setAlpha(mAlpha);;
					Director.getInstance().postInvalidate();
					if(mAlpha ==  mTarget){
						mContinue  = false;
					};
			}else{
				
					mAlpha  -= 0.1f;
					if(mAlpha < 0){
						mAlpha =  0;
						mTarget = 0;
					};
					
					mViewGroup.setAlpha(mAlpha);;
					Director.getInstance().postInvalidate();
					if(mAlpha == 0){
						mContinue  = false;
						setRenderable(false);
					};
			}
		}
		
		super.update();
	}
	@Override
	public void initScene() {
		
	}

	
	public void cancelBlur(final long timeDelayed) {
		
			mAlpha = mTarget;
			mGoBlur = false;
			mContinue  = true;

			Director.getInstance().postInvalidate();
	}

	public void requestBlur(final Bitmap bitmap,final float target,final long timeDelayed,final boolean recycle) {

		setRenderable(true);
		updateBitmap(bitmap, recycle);
		
		mTarget = target;
		mAlpha = 0;
		mGoBlur = true;
		mContinue  = true;
		Director.getInstance().postInvalidate();
	}
	
	
	private void updateBitmap(final Bitmap bitmap,final  boolean recycle) {

		mHandlerRefresh.post(new Runnable() {
			
			@Override
			public void run() {	
				
				Bitmap scalBitmap = getScaleBitmap(bitmap,0.10f);
			    Bitmap ffBitmap = blurImageAmeliorate_2D(scalBitmap,2,2,1,2f);
				if(recycle){
					scalBitmap .recycle();
				}
				final Bitmap finalBitmap = blurImageAmeliorate_2D(ffBitmap,2,2,1,2f);
			
				Director.getInstance().requstAsyncGeneralTask(new PostRunnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						mViewGroup.setBackground(finalBitmap,true);
					}
				});
			}
		});

	
//		scalBitmap = getScaleBitmap(finalBitmap,0.5f);
//		finalBitmap = blurImageAmeliorate_2D(scalBitmap,2,2,1,2f);
		
//		 finalBitmap = blurImageAmeliorate_2D(finalBitmap,3,3,2,255,4f);
//		finalBitmap = blurImageAmeliorate_2D(finalBitmap,1,1,255,0.1f);

		
	}
	private Bitmap getScaleBitmap(final Bitmap bitmap,float scale) {
		
		float scaleX = scale;
		float scaleY = scale;

		int width = (int) (bitmap.getWidth() * scaleX);
		int height = (int) (bitmap.getHeight() * scaleY);
		if(width < 1 ){
			width = 1;
		}
		if(height < 1){
			height = 1;
		}
		Bitmap scalBitmap = Bitmap.createBitmap(width,height,Config.ARGB_8888);
		Canvas canvas = new Canvas(scalBitmap);
		Matrix matrix = new Matrix();
		matrix.postScale(scaleX, scaleY);
		canvas.drawBitmap(bitmap, matrix, null);
		bitmap.recycle();
		return scalBitmap;
	}
	/**
	 * (二维高斯模糊)
	 * 
	 * @param bmp
	 * @param id 
	 * @param alpha 
	 * @param rr 
	 * @return
	 */
	private Bitmap blurImageAmeliorate_2D(Bitmap bmp,int radiusX, int radiusY, int offset,  float rr) {
		long start = System.currentTimeMillis();

		float[][] gauss = new float[radiusY * 2 + 1][radiusX * 2 + 1];

		float delta = 0; // 值越小图片会越亮，越大则越暗
		String quanString = "";
		for (int i = -radiusY; i <= radiusY; i++) {
			for (int j = -radiusX; j <= radiusX; j++) {
				gauss[i + radiusY][j + radiusX] = findGauss(i,j,rr);
				
//				String string = String.valueOf(gauss[i + radius][j + radius]);
//				string = string.substring(0,4);
				quanString += gauss[i + radiusY][j + radiusX]+"\t";
//				if(i == 0 && j == 0 ){
//					continue;
//				}
				delta += gauss[i + radiusY][j + radiusX];
			}
			quanString += "\n";
		}
		quanString ="";
		for (int i = -radiusY; i <= radiusY; i++) {
			for (int j = -radiusX; j <= radiusX; j++) {
				gauss[i + radiusY][j + radiusX] /= delta;
				quanString += gauss[i + radiusY][j + radiusX]+"\t";
			}
			quanString += "\n";
		}
		
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int pixA = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;
		int newA = 0;

		int[] pixels = new int[width * height];
		int[] pixelsAfter = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 0, length = height ; i < length; i++) {
			for (int k = 0, len = width ; k < len; k++) {
				for (int m = -radiusY; m <= radiusY; m++) {
					for (int n = -radiusX; n <= radiusX; n++) {

//						if(m == 0 && n == 0){
//							continue;
//						}
						//以i和k循环的时候，二维像素点i,k点周围radius范围内进行高斯模糊的时候可能越界了，这个时候我们拷贝邻近像素
						int tI = i+m*offset;
						if(tI < 0 ){
							tI = Math.abs(tI);
						}
						if(tI >= height){
							tI = tI - (tI - (height - 2));
						}
						int tK = k + n*offset;
						if(tK < 0 ){
							tK = Math.abs(tK);
						}
						if(tK >= width){
							tK = tK - (tK - (width - 2));
						}
						pixColor = pixels[(tI) * width + tK];

						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);
						pixA = Color.alpha(pixColor);

						newR = newR + (int) (pixR * gauss[m + radiusY][n + radiusX]);
						newG = newG + (int) (pixG * gauss[m + radiusY][n + radiusX]);
						newB = newB + (int) (pixB * gauss[m + radiusY][n + radiusX]);
						newA = newA + (int) (pixA * gauss[m + radiusY][n + radiusX]);

					}
				}

				pixelsAfter[i * width + k] = Color.argb(255, newR, newG, newB);

				newR = 0;
				newG = 0;
				newB = 0;
				newA = 0;
			}
		}

		bitmap.setPixels(pixelsAfter, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		return bitmap;

	}

	/**
	 * 
	 * 正态分布高斯函数
	 * @param i
	 * @param j
	 * @param rr 
	 * 
	 * @return
	 */
	private float findGauss(int i, int j, float rr) {
//		return 1;
		float sigama = rr;
		float sig2 = sigama*sigama ;
		return (float) (1/(Math.PI * 2 *sig2) * Math.pow(Math.E , -(i * i + j * j) /(2f * sig2)));
	}

}