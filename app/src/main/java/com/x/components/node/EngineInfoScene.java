package com.x.components.node;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;

import com.hello.scene_for_vr.Tools;
import com.x.opengl.kernel.EngineConstanst;
import com.x.components.widget.TextView;


/**
 * 用于显示opengl的状态信息的类
 * @author Administrator
 *
 */
public final class EngineInfoScene extends XScene{

	private ViewGroup mViewGroup = new ViewGroup();
	private TextView 	mView;
	private int	mcount;

	private float	mRealTimeFrame; //实时帧率
	private float	mAverageFrame; //平均帧率
	private float	mAllFrame;
	private int		mFrameCount;
	private long	mLastTime = 0;
	public EngineInfoScene() {

		SetDebugName("EngineInfoScene");
	}
	@Override
	public void initScene() {
		
		
		mView = new TextView();
		mView.setWidth(300);
		mView.setHeight(100);
		mView.setTextSize(30);
		mView.setTextColor(Color.RED);
		mView.setTextGravity(Gravity.CENTER);
		

		Bitmap centerBitmap  = Bitmap.createBitmap(1, 1, Config.ARGB_8888);
		centerBitmap.eraseColor(Color.argb(255, 255, 255, 255));
		mViewGroup.setWidth(300);
		mViewGroup.setHeight(300);
		mViewGroup.setTranslate(-(EngineConstanst.REFERENCE_SCREEN_WIDTH- 300)/2, -(EngineConstanst.REFERENCE_SCREEN_HEIGHT- 300)/2, 0);
		mViewGroup.setBackground(centerBitmap);
		
		mViewGroup.addChild(mView);
		
		addLayer(mViewGroup);
		
	}
	
	@Override
	public void update() {
		super.update();

		long time = System.currentTimeMillis();
		if(mLastTime == 0){

			this.mRealTimeFrame = 0;
			this.mAverageFrame = 0;
			
		}else{
			
			this.mRealTimeFrame = 1000f /(time - mLastTime);
			this.mAllFrame += mRealTimeFrame;
			this.mFrameCount ++;
			this.mAverageFrame = mAllFrame/mFrameCount;
			if(mFrameCount > 400 ){
				mFrameCount = 0;
				mAllFrame = 0;
			}
		
		}
		mLastTime = time;
		if(mFrameCount % 100 == 0){
			String ss = 	TextUtils.join("\n", new String[]{
					(int)(mAverageFrame*100)/100f+"",
					"vc = "+Tools.float_2_degree(mSFTF[0]) +","+Tools.float_2_degree(mSFTF[1])+","+Tools.float_2_degree(mSFTF[2])+","
			});
			mView.setText(ss);
			Log.d("frame","frame = "+(int)(mAverageFrame*100)/100);
		}
		
	}
	private float[] mSFTF = new float[3];
	public void requestText(float[] startFromSensorTransformation) {
		this.mSFTF = startFromSensorTransformation;
	}
	 
}
