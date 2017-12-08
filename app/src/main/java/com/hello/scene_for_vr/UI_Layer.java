package com.hello.scene_for_vr;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.Gravity;

import com.hello.R;
import com.x.Director;
import com.x.opengl.kernel.EngineConstanst;
import com.x.opengl.kernel.Texture;
import com.x.components.listener.OnAnimationListener;
import com.x.opengl.kernel.particles.ParticleSystemScene;
import com.x.components.node.View;
import com.x.components.node.View.OnClickListener;
import com.x.components.node.View.OnStareAtListener;
import com.x.components.node.ViewGroup;
import com.x.components.widget.ImageView;
import com.x.components.widget.TextView;

public class UI_Layer {

	private MyRotateViewGroup mLayer;
	  int messageIndex = 0;
   private Texture mTextureFlower;
   private Texture mTextureStar;
   private Texture mTextureWindmill;
   private Texture mTextureBubble;
	private MainDialogScene mMainDialogScene;
	private int mShowIndex = 0 ;
	
	public UI_Layer(MainDialogScene mainDialogScene) {

		this.mMainDialogScene = mainDialogScene;
		if(mTextureFlower == null){
			mTextureFlower = Director.getInstance().sResourcer.generateTexture(R.drawable.flower200_200);
			mTextureStar = Director.getInstance().sResourcer.generateTexture(R.drawable.star);
			mTextureWindmill = Director.getInstance().sResourcer.generateTexture(R.drawable.windmill);
			mTextureBubble = Director.getInstance().sResourcer.generateTexture(R.drawable.bubble);
		}

		mLayer = new MyRotateViewGroup();
		mLayer.SetDebugName("mUpmDownViewGroup");
		mLayer.setWidth(EngineConstanst.REFERENCE_SCREEN_WIDTH);
		mLayer.setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
		mLayer.setBackgroundColor(Color.parseColor("#55000000"));
		mLayer.setVisibility(true);
		mLayer.setTranslate(0, 0, 0);
		mLayer.setFocusable(false);
		
		{
			//界面1
			MyRotateViewGroup viewGroup = new MyRotateViewGroup();
			viewGroup.SetDebugName("mUpmDownViewGroup");
			viewGroup.setWidth(720);
			viewGroup.setHeight(90);
			//viewGroup.setBackgroundColor(Color.parseColor("#33ff00ff"));
			viewGroup.setVisibility(true);
			viewGroup.setTranslate(0, -(EngineConstanst.REFERENCE_SCREEN_HEIGHT - 100)/2+150,0);
			viewGroup.setFocusable(false);

			for (int i = 0; i < 2; i++) {
				TextView tv = (TextView) makeSendGiftView(i);
				int ii = i+100;
				if(ii % 4 == 0){
					tv.setText("玫瑰");
					tv.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Director.getInstance().playParticleEffect(ParticleSystemScene.EFFECT_1,mTextureFlower);
						}
					});
				}else if(ii % 4 == 1){
					tv.setText("气泡");
					tv.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Director.getInstance().playParticleEffect(ParticleSystemScene.EFFECT_3,mTextureBubble);
						}
					});
				}else if(ii % 4 == 2){
					tv.setText("流星");
					tv.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Director.getInstance().playParticleEffect(ParticleSystemScene.EFFECT_0,mTextureStar);
						}
					});
				}else if(ii % 4 == 3){
					tv.setText("风车");
					tv.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Director.getInstance().playParticleEffect(ParticleSystemScene.EFFECT_2,mTextureWindmill);
						}
					});
					
				}
				viewGroup.addChild(tv);
			}
			mLayer.addChild(viewGroup);
		}
		

		{
			//界面2
			MyRotateViewGroup viewGroup2 = new MyRotateViewGroup();
			viewGroup2.SetDebugName("mUpmDownViewGroup");
			viewGroup2.setWidth(720);
			viewGroup2.setHeight(90);
//			viewGroup2.setBackgroundColor(Color.parseColor("#2200ffff"));
			viewGroup2.setVisibility(true);
			viewGroup2.setTranslate(0, -(EngineConstanst.REFERENCE_SCREEN_HEIGHT - 100)/2+50 ,0);
			viewGroup2.setFocusable(false);

			for (int i = -5; i < 5; i++) {
				View view = makeHeadView(i);
				viewGroup2.addChild(view);
				int ii = i + 100;
				if(ii %4 == 1){
					Bitmap bitmap = createCircleImage(R.drawable.head,150);
					view.setBackground(bitmap);
				}else if(ii %4 == 2){
					Bitmap bitmap = createCircleImage(R.drawable.head2,80);
					view.setBackground(bitmap);
				}else if(ii %4 == 3){
					Bitmap bitmap = createCircleImage(R.drawable.head3,80);
					view.setBackground(bitmap);
				}else if(ii %4 == 0){
					Bitmap bitmap = createCircleImage(R.drawable.head4,80);
					view.setBackground(bitmap);
				}
			}
			mLayer.addChild(viewGroup2);

		}

		{
			
			//右侧界面
			MyRotateViewGroup viewGroup2 = new MyRotateViewGroup();
			viewGroup2.SetDebugName("mUpmDownViewGroup");
			viewGroup2.setWidth(500);
			viewGroup2.setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
			//viewGroup2.setBackgroundColor(Color.parseColor("#2200ffff"));
			viewGroup2.setVisibility(true);
			viewGroup2.setTranslate( (EngineConstanst.REFERENCE_SCREEN_WIDTH - 500)/2 ,0,0);
			viewGroup2.setFocusable(false);

			final List<String> messageList = new ArrayList<String>();
			messageList.add(0, "推送消息"+(int)(Math.random()*1000));//初始化推送消息
			messageIndex = 0;
			final List<View> mViewLists = new ArrayList<View>();
			
			for (int i = 0; i <6; i++) {
				View view = makeMessage(i);
				view.setTranslate(0, -220+i * (100+10),  0);
				view.setAlpha(0);
				
				mViewLists.add(view);
				viewGroup2.addChild(view);
			}

			final TextView te = (TextView) makeTextView(-4);
			te.setText(messageList.get(0));
			te.setBackgroundColor(Color.WHITE);
			te.setTextColor(Color.BLACK);
			te.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mViewLists.get(0).setOnAnimationListenner(new OnAnimationListener() {
						
						@Override
						public void onAnimationStart(View view) {
							
						}
						
						@Override
						public void onAnimationRepeat(View view) {
							
						}
						
						@Override
						public void onAnimationEnd(View v) {

							
							mViewLists.get(0).setOnAnimationListenner(null);
							for (int i = 0; i < mViewLists.size(); i++) {
								View view1 = mViewLists.get(i);
								//view1.translateTo(0, -220+(i + 1)* (100+10), 0);
								if(i  > messageList.size() - 1){ //如果没有消息直接设置为透明
									view1.setAlpha(0);
								}else{
									if(i == 0 ){
										view1.setAlpha(0);
									}else{
										ViewGroup vg = (ViewGroup) view1;
										TextView tv = (TextView) vg.get(1);
										tv.setText(messageList.get(i));
										view1.setAlpha(1);
									}
								} 
								view1.setTranslate(0, -220+i * (100+10),  	0);
							}
						}
					});
					
					for (int i = 0; i < mViewLists.size(); i++) {
						View view = mViewLists.get(i);
						
						if(i  > messageList.size() - 1){ //如果没有消息直接设置为透明
							view.setAlpha(0);
						}else{

							if(i == 0 ){
								view.setAlpha(0);
								view.alphaTo(1);
							}else if(i == mViewLists.size() - 1 ){
								view.setAlpha(1);
								view.alphaTo(0);
							} 
							
							ViewGroup vg = (ViewGroup) view;
							TextView tv = (TextView) vg.get(1);
							tv.setText(messageList.get(i));
						}

						view.setTranslate(0, -220+i * (100+10),  	0);
						view.translateTo(0, -220+(i +1)* (100+10), 0);
						
					}
					
					messageList.add(0, "推送消息"+(int)(Math.random()*1000));//初始化推送消息
					messageIndex++;//
					te.setText(messageList.get(0));
				}
			});
			viewGroup2.addChild(te);
			
			mLayer.addChild(viewGroup2);

		}
		
		{//左侧切换界面
			
			//界面1
			MyRotateViewGroup viewGroup = new MyRotateViewGroup();
			viewGroup.SetDebugName("mUpmDownViewGroup");
			viewGroup.setWidth(300);
			viewGroup.setHeight(90);
//			viewGroup.setBackgroundColor(Color.parseColor("#33ff00ff"));
			viewGroup.setVisibility(true);
			viewGroup.setTranslate(-(EngineConstanst.REFERENCE_SCREEN_WIDTH-300)/2, 0,0);
			viewGroup.setFocusable(false);

			
			for (int i = 0; i < 2; i++) {
			
			}
			TextView tv = (TextView) makeChangeShowPosition(0);
			tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mMainDialogScene.changeShowPosition((mShowIndex++)%3);
//					Director.getInstance().playParticleEffect(ParticleSystemScene.EFFECT_1,mTextureFlower);
				}
			});
			viewGroup.addChild(tv);
			mLayer.addChild(viewGroup);
			
		}
//		
//		MyRotateViewGroup viewGroup2 = new MyRotateViewGroup();
//		viewGroup2.SetDebugName("mUpmDownViewGroup");
//		viewGroup2.setWidth(520);
//		viewGroup2.setHeight(800);
//		viewGroup2.setBackgroundColor(Color.parseColor("#55ffffff"));
//		viewGroup2.setVisibility(true);
//		viewGroup2.setTranslate(500, 0,0);
//		viewGroup2.setFocusable(false);
//		
//		viewGroup2.addChild(makeView2(0));
//		viewGroup2.addChild(makeView2(1));
//
//		mLayer.addChild(viewGroup2);
		
	}
	public View getLayer() {
		return mLayer;
	}

	View makeChangeShowPosition(final int i ){

		final TextView vv = new TextView();
		vv.SetDebugName("makeView  "+i);
		vv.setWidth(200);
		vv.setHeight(60);
		vv.setTextSize(30);
		vv.setTextColor(Color.BLACK);
		vv.setText("切换显示位置");
		vv.setTextGravity(Gravity.CENTER);
		vv.setBackgroundColor(Color.WHITE);
		vv.setTranslate((i) * (150+10), 0  ,  0);
		vv.setOnStareAtListener(OnStareAtListener);
		
		return vv;
	}
	View makeSendGiftView(final int i ){

		final TextView vv = new TextView();
		vv.SetDebugName("makeView  "+i);
		vv.setWidth(150);
		vv.setHeight(60);
		vv.setTextSize(30);
		vv.setTextColor(Color.WHITE);
		vv.setText("点我送花");
		vv.setTextGravity(Gravity.CENTER);
		vv.setBackgroundColor(Color.parseColor("#44000000"));
		vv.setTranslate((i-1) * (150+10), 0  ,  0);
		vv.setOnStareAtListener(OnStareAtListener);
		
		return vv;
	}
	View makeTextView(final int i ){

		final TextView vv = new TextView();
		vv.SetDebugName("makeView  "+i);
		vv.setWidth(400);
		vv.setHeight(100);
		vv.setTextSize(50);
		vv.setTextColor(Color.WHITE);
		vv.setText("光能蜗牛");
		vv.setTextGravity(Gravity.CENTER);
		vv.setBackgroundColor(Color.BLACK);
		vv.setTranslate(0 ,  i * 110,  0);
		vv.setOnStareAtListener(OnStareAtListener);
		return vv;
	}
	View makeMessage(final int i ){

		//右侧界面
		ViewGroup viewgroup = new ViewGroup();
		viewgroup.SetDebugName("mUpmDownViewGroup");
		viewgroup.setWidth(500);
		viewgroup.setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
		//viewGroup2.setBackgroundColor(Color.parseColor("#2200ffff"));
		viewgroup.setVisibility(true);
		viewgroup.setFocusable(false);
		

		final View headview = new View();
		headview.SetDebugName("makeView  "+i);
		headview.setWidth(80);
		headview.setHeight(80);
		headview.setTranslate( -400/2 -60 , 0,  0);
		Bitmap bitmap = createCircleImage(R.drawable.head,150);
		headview.setBackground(bitmap);
		viewgroup.addChild(headview);
		
		final TextView vv = new TextView();
		vv.SetDebugName("makeView  "+i);
		vv.setWidth(400);
		vv.setHeight(100);
		vv.setTextSize(30);
		vv.setTextColor(Color.WHITE);
		vv.setText("光能蜗牛");
		vv.setTextGravity(Gravity.CENTER);
		vv.setBackgroundColor(Color.parseColor("#44000000"));
		vv.setTranslate(0 ,  0,  0);
		vv.setOnStareAtListener(OnStareAtListener);
		viewgroup.addChild(vv);
		
		return viewgroup;
	}
	View makeImageView222(final int i ){

		final ImageView vv = new ImageView();
		vv.SetDebugName("makeImageView  "+i);
		vv.setWidth(1000);
		vv.setHeight(200);
		vv.setBackgroundColor(Color.parseColor("#ff777777"));
		vv.setImageResource(R.drawable.ic_launcher);
		
		vv.setTranslate(0 + i * 450,0,  -EngineConstanst .REFERENCE_SCREEN_HEIGHT/2);
		vv.setOnStareAtListener(OnStareAtListener);
		return vv;
	}
	View makeImageView(final int i ){

		final ImageView vv = new ImageView();
		vv.SetDebugName("makeImageView  "+i);
		vv.setWidth(400);
		vv.setHeight(200);
		vv.setBackgroundColor(Color.parseColor("#ff777777"));
		vv.setImageResource(R.drawable.ic_launcher);
		
		vv.setTranslate(0 + i * 450,0,  -EngineConstanst .REFERENCE_SCREEN_HEIGHT/2);
		vv.setOnStareAtListener(OnStareAtListener);
		return vv;
	}
	/** 
     * 根据原图和变长绘制圆形图片 
     *  
     * @param source 
     * @param min 
     * @return 
     */  
    private Bitmap createCircleImage(int  sourceId, int min)  
    {  
    	Bitmap source = BitmapFactory.decodeResource(Director.getInstance().getContext().getResources(), sourceId);
    	min = source.getWidth();
        final Paint paint = new Paint();  
        paint.setAntiAlias(true);  
        Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);  
        /** 
         * 产生一个同样大小的画布 
         */  
        Canvas canvas = new Canvas(target);  
        /** 
         * 首先绘制圆形 
         */  
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);  
        /** 
         * 使用SRC_IN 
         */  
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));  
        /** 
         * 绘制图片 
         */  
        canvas.drawBitmap(source, 0, 0, paint);  
        return target;  
    }  
	View makeHeadView(final int i ){

		final View vv = new View();
		vv.SetDebugName("makeView  "+i);
		vv.setWidth(80);
		vv.setHeight(80);
		vv.setBackgroundResource(R.drawable.t034);	  
		vv.setTranslate(  i * (80+10), 0 , 0);
		vv.setOnStareAtListener(OnStareAtListener);
		vv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(i == 0){
					Director.getInstance().resetSensor();	
				}else  {
					vv.setRotate((float)Math.random() * 360, (float)Math.random() * 360, (float)Math.random() * 360);
					vv.rotateTo(0, 0, 0);
				}
			}
		});
		vv.setOnStareAtListener(new OnStareAtListener() {
			
			long time = 0 ;
			@Override
			public void onStareAt(View v) {
//				if(System.currentTimeMillis() - time > 2000){
//					Director.getInstance().playParticleEffect(ParticleSystemScene.EFFECT_1,mTextureStar);
//					time = System.currentTimeMillis();
//				}
			}
		});
		return vv;
	}
	OnStareAtListener OnStareAtListener = new OnStareAtListener() {
		 

		@Override
		public void onStareAt(View v) {
			
		}
	}; 
	private Texture mRestoreTextre;
	public int getVeodioTextureID() { 
		mRestoreTextre = mLayer.getBackground();
		Texture texture  = Director.sResourcer.generateOES_Texture();
        mLayer.setBackground(texture,false);
		return texture.getTextureID();
	}
	public void restoreTexture() {
		// TODO Auto-generated method stub
		if(mRestoreTextre != null){
			mLayer.setBackground(mRestoreTextre);
		}
	}
 
}
