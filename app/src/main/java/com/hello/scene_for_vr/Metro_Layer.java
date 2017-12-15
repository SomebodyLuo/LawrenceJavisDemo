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
import android.util.Log;
import android.view.Gravity;

import com.hello.R;
import com.x.Director;
import com.x.opengl.kernel.EngineConstanst;
import com.x.opengl.kernel.Texture;
import com.x.components.listener.OnAnimationListener;
import com.x.components.node.View;
import com.x.components.node.View.OnClickListener;
import com.x.components.node.View.OnStareAtListener;
import com.x.components.node.ViewGroup;
import com.x.components.widget.ImageView;
import com.x.components.widget.TextView;

public class Metro_Layer {

    private MyRotateViewGroup mLayer;
    int messageIndex = 0;
    private Texture mTextureFlower;
    private Texture mTextureStar;
    private Texture mTextureWindmill;
    private Texture mTextureBubble;
    private MainDialogScene mMainDialogScene;
    private int mShowIndex = 0 ;

    private boolean isBackgroundInit = false;
    private int mBackgroundWallIndex;

    public Metro_Layer(MainDialogScene mainDialogScene) {

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


        // luoyouren: 增加背景墙
        InitBackgroundWall();

        InitFront();

        InitBack();

        InitLeft();

        InitRight();

        InitTop();

        InitBottom();
    }



    int touchCounts = 0;
    public void InitBackgroundWall()
    {
        // 纹理ID
        int sourceIds1[] = {
                R.drawable.redsunset_ft,
                R.drawable.redsunset_bk,
                R.drawable.redsunset_lf,
                R.drawable.redsunset_rt,
                R.drawable.redsunset_up,
                R.drawable.redsunset_dn,

//                R.drawable.bluenebula1024_front,
//                R.drawable.bluenebula1024_back,
//                R.drawable.bluenebula1024_left,
//                R.drawable.bluenebula1024_right,
//                R.drawable.bluenebula1024_top,
//                R.drawable.bluenebula1024_bottom,


//                R.drawable.ic_launcher,
//                R.drawable.ic_launcher,
//                R.drawable.ic_launcher,
//                R.drawable.ic_launcher,
//                R.drawable.ic_launcher,
//                R.drawable.ic_launcher,
        };
        if (true)
        {
            //背景墙 前
            MyRotateViewGroup viewGroup2 = new MyRotateViewGroup();
            viewGroup2.SetDebugName("mBackGroundBox");
            viewGroup2.setWidth(EngineConstanst.BOX_LENGTH);
            viewGroup2.setHeight(EngineConstanst.BOX_LENGTH);
//			viewGroup2.setBackgroundColor(Color.parseColor("#2200ffff"));
            viewGroup2.setVisibility(true);
            viewGroup2.setTranslate(0, 0 ,0);
            viewGroup2.setFocusable(false);

            View backgroundView = null;

            // front
            backgroundView = makeImageView(sourceIds1[0], EngineConstanst.BOX_LENGTH, EngineConstanst.BOX_LENGTH, 0, 0, -EngineConstanst.BOX_LENGTH/2);
            viewGroup2.addChild(backgroundView);
            Log.i("luoyouren", "viewGroup2's child = " + viewGroup2.getChildCount());

            // back
            backgroundView = makeImageView(sourceIds1[1], EngineConstanst.BOX_LENGTH, EngineConstanst.BOX_LENGTH, 0, 0, EngineConstanst.BOX_LENGTH/2);
            backgroundView.setRotate(0, 180, 0);
            viewGroup2.addChild(backgroundView);
            Log.i("luoyouren", "viewGroup2's child = " + viewGroup2.getChildCount());

            // left
            backgroundView = makeImageView(sourceIds1[2], EngineConstanst.BOX_LENGTH, EngineConstanst.BOX_LENGTH, -EngineConstanst.BOX_LENGTH/2, 0, 0);
            backgroundView.setRotate(0, 90, 0);
            viewGroup2.addChild(backgroundView);
            Log.i("luoyouren", "viewGroup2's child = " + viewGroup2.getChildCount());

            // right
            backgroundView = makeImageView(sourceIds1[3], EngineConstanst.BOX_LENGTH, EngineConstanst.BOX_LENGTH, EngineConstanst.BOX_LENGTH/2, 0, 0);
            backgroundView.setRotate(0, -90, 0);
            viewGroup2.addChild(backgroundView);
            Log.i("luoyouren", "viewGroup2's child = " + viewGroup2.getChildCount());

            // top
            backgroundView = makeImageView(sourceIds1[4], EngineConstanst.BOX_LENGTH, EngineConstanst.BOX_LENGTH, 0, EngineConstanst.BOX_LENGTH/2, 0);
            backgroundView.setRotate(90, 0, 0);
            viewGroup2.addChild(backgroundView);
            Log.i("luoyouren", "viewGroup2's child = " + viewGroup2.getChildCount());

            // bottom
            backgroundView = makeImageView(sourceIds1[5], EngineConstanst.BOX_LENGTH, EngineConstanst.BOX_LENGTH, 0, -EngineConstanst.BOX_LENGTH/2, 0, "#ffffffff");
            backgroundView.setRotate(-90, 0, 0);
            viewGroup2.addChild(backgroundView);
            Log.i("luoyouren", "viewGroup2's child = " + viewGroup2.getChildCount());


            mLayer.addChild(viewGroup2);
            mBackgroundWallIndex = mLayer.indexOf(viewGroup2);
        }

        Log.i("luoyouren", "mLayer's child = " + mLayer.getChildCount());
        isBackgroundInit = true;
    }

    private void InitFront()
    {
        if (true)
        {
            //Front: 背景与画布
            MyRotateViewGroup viewGroup2 = new MyRotateViewGroup();
            viewGroup2.SetDebugName("FrontBackgroundViewGroup");
            viewGroup2.setWidth(EngineConstanst.REFERENCE_SCREEN_WIDTH);
            viewGroup2.setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
//			viewGroup2.setBackgroundColor(Color.parseColor("#2200ffff"));
            viewGroup2.setVisibility(true);
            viewGroup2.setTranslate(0, 0 ,0);
            viewGroup2.setFocusable(false);


            View canvasView = makeImageView(R.drawable.gray_shadow, 1773, 913, 0, 0, 4);
            canvasView.setFocusable(false);
            canvasView.setTouchAble(false);
            canvasView.setClickable(false);
            viewGroup2.addChild(canvasView);

            View leftArrow = makeImageView(R.drawable.arrow_left, 36, 59, -(1773 / 2 - 100), 0, 8);
            View rightArrow = makeImageView(R.drawable.arrow_right, 36, 59, (1773 / 2 - 100), 0, 8);
            viewGroup2.addChild(leftArrow);
            viewGroup2.addChild(rightArrow);


            mLayer.addChild(viewGroup2);

        }

        if (true)
        {
            //Front: 图标
            MyRotateViewGroup metroViewGroup = new MyRotateViewGroup();
            metroViewGroup.SetDebugName("FrontMetroViewGroup");
            metroViewGroup.setWidth(330 * 4 + 20 * 3);
            metroViewGroup.setHeight(200 + 25 + 200);
//			viewGroup2.setBackgroundColor(Color.parseColor("#2200ffff"));
            metroViewGroup.setVisibility(true);
            metroViewGroup.setTranslate(0, 0, 0);
            metroViewGroup.setFocusable(false);

            int sourceIds[] = {
                    R.drawable.metro0101the_evil, R.drawable.metro0102the_surge, R.drawable.metro0103warface, R.drawable.metro0104simcity,
                    R.drawable.metro0201jungle, R.drawable.metro0202nineparchment, R.drawable.metro0203fate_hand, R.drawable.metro0204fishing};

            final int ImageWidth = 326;
            final int ImageHeight = 199;
            final int ImageStride = 60;
            final int COLUMNS = 4;

            for (int i = 0; i  < sourceIds.length; i++)
            {
                final View view = makeImageView(sourceIds[i], ImageWidth, ImageHeight, (2 * (i % COLUMNS) - 3) * (ImageWidth + ImageStride / 2) * 0.5f, (2 * (i / COLUMNS) - 1) * (ImageHeight + ImageStride / 2) * 0.5f,  12);

                view.setOnFocusListener(new View.OnFocusListener() {
                    @Override
                    public boolean onFocus(View v) {
                        view.translateZTo(ImageHeight);

                        return false;
                    }

                    @Override
                    public boolean onRemoveFocus(View v) {

                        view.translateZTo(12);

                        return false;
                    }
                });

                metroViewGroup.addChild(view);
            }

            mLayer.addChild(metroViewGroup);
        }
    }

    private void InitBack()
    {

    }

    private void InitLeft()
    {

    }

    private void InitRight()
    {

    }

    final float WEATHER_PIC_WIDTH = 1343;
    final float WEATHER_PIC_HEIGHT = 685;
    private void InitTop()
    {
        if (true)
        {
            //Top: 天气
            MyRotateViewGroup viewGroup2 = new MyRotateViewGroup();
            viewGroup2.SetDebugName("WeatherViewGroup");
            viewGroup2.setWidth(EngineConstanst.REFERENCE_SCREEN_WIDTH);
            viewGroup2.setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
//			viewGroup2.setBackgroundColor(Color.parseColor("#2200ffff"));
            viewGroup2.setVisibility(true);
            viewGroup2.setTranslate(0, 0 ,0);
            viewGroup2.setFocusable(false);

            View canvasView = makeImageView(R.drawable.weather_board, WEATHER_PIC_WIDTH, WEATHER_PIC_HEIGHT, 0, EngineConstanst.REFERENCE_SCREEN_HEIGHT, EngineConstanst.REFERENCE_SCREEN_HEIGHT + 0);
            canvasView.setAlpha(1.0f);
            canvasView.setRotate(90, 0, 0);
            canvasView.setFocusable(false);
            canvasView.setTouchAble(false);
            canvasView.setClickable(false);
            viewGroup2.addChild(canvasView);

            View change = makeImageView(R.drawable.test22, 48, 48, -WEATHER_PIC_WIDTH/2 + 100, EngineConstanst.REFERENCE_SCREEN_HEIGHT, EngineConstanst.REFERENCE_SCREEN_HEIGHT - (WEATHER_PIC_HEIGHT/2+50));
            change.setRotate(90, 0, 0);
            change.setOnFocusListener(new View.OnFocusListener() {
                @Override
                public boolean onFocus(View v) {
                    touchCounts++;
                    updateBackground(touchCounts % 3);
                    return false;
                }

                @Override
                public boolean onRemoveFocus(View v) {
                    return false;
                }
            });

            viewGroup2.addChild(change);


            mLayer.addChild(viewGroup2);

        }
    }

    private void InitBottom()
    {

    }

    private void updateBackground(int index)
    {
        // 纹理ID
        int sourceIds2[][] = {


                {R.drawable.redsunset_ft,
                R.drawable.redsunset_bk,
                R.drawable.redsunset_lf,
                R.drawable.redsunset_rt,
                R.drawable.redsunset_up,
                R.drawable.redsunset_dn},

                {R.drawable.desert_front,
                R.drawable.desert_back,
                R.drawable.desert_left,
                R.drawable.desert_right,
                R.drawable.desert_top,
                R.drawable.redsunset_dn},

                {R.drawable.bluenebula1024_front,
                R.drawable.bluenebula1024_back,
                R.drawable.bluenebula1024_left,
                R.drawable.bluenebula1024_right,
                R.drawable.bluenebula1024_top,
                R.drawable.bluenebula1024_bottom},


//                R.drawable.ic_launcher,
//                R.drawable.ic_launcher,
//                R.drawable.ic_launcher,
//                R.drawable.ic_launcher,
//                R.drawable.ic_launcher,
//                R.drawable.ic_launcher,
        };
        if (isBackgroundInit)
        {
            for (int i = 0; i < 5; i++)
            {
                ViewGroup viewGroup = (ViewGroup)(mLayer.get(mBackgroundWallIndex));
                ImageView imageView = (ImageView)viewGroup.get(i);
                imageView.setImageResource(sourceIds2[index][i], true);
            }
        }
    }


    public View getLayer() {
        return mLayer;
    }




    final int ImageWidth = 326;
    final int ImageHeight = 199;
    final int ImageStride = 60;
    final int COLUMNS = 4;
    View makeImageView(int sourceId, int index)
    {
        final ImageView vv = new ImageView();
        vv.SetDebugName("makeImageView  "+ index);
        vv.setWidth(ImageWidth);
        vv.setHeight(ImageHeight);

        vv.setImageResource(sourceId);

        vv.setTranslate((2 * (index % COLUMNS) - 3) * (ImageWidth + ImageStride / 2) * 0.5f, (2 * (index / COLUMNS) - 1) * (ImageHeight + ImageStride / 2) * 0.5f,  8);
//        vv.setOnStareAtListener(OnStareAtListener);
        return vv;
    }

    View makeImageView(int sourceId, float imageWidth, float imageHeight, float x, float y, float z)
    {
        final ImageView vv = new ImageView();
        vv.SetDebugName("makeImageView  ");
        vv.setWidth(imageWidth);
        vv.setHeight(imageHeight);

        // luoyouren:
        // 注意：某个View的mBGDrawable 没有任何贴图时，焦点扫描不到它，也就不会产生凝视点事件
        vv.setBackgroundColor(Color.parseColor("#00000000"));
        vv.setImageResource(sourceId);

        vv.setTranslate(x, y, z);
//        vv.setOnStareAtListener(OnStareAtListener);
        return vv;
    }

    View makeImageView(int sourceId, float imageWidth, float imageHeight, float x, float y, float z, int xxx)
    {
        final View vv = new View();
        vv.SetDebugName("makeImageView");
        vv.setWidth(imageWidth);
        vv.setHeight(imageHeight);

//        vv.setBackgroundColor(Color.parseColor("#ff777777"));
        vv.setBackgroundResource(sourceId);

        vv.setTranslate(x, y, z);
//        vv.setOnStareAtListener(OnStareAtListener);
        return vv;
    }

    View makeImageView(int sourceId, float imageWidth, float imageHeight, float x, float y, float z, String color)
    {
        final ImageView vv = new ImageView();
        vv.SetDebugName("makeImageView  ");
        vv.setWidth(imageWidth);
        vv.setHeight(imageHeight);

        vv.setBackgroundColor(Color.parseColor(color));
//        vv.setImageResource(sourceId);

        vv.setTranslate(x, y, z);
//        vv.setOnStareAtListener(OnStareAtListener);
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
