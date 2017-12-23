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
import com.x.opengl.kernel.Material;
import com.x.opengl.kernel.MaterialGroup;
import com.x.opengl.kernel.ObjDrawable;
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
    private MyObjeView mSphere;
    private View mVideoWall;
    private MainDialogScene mMainDialogScene;

    private boolean isBackgroundInit = false;
    private int mBackgroundWallIndex;

    public Metro_Layer(MainDialogScene mainDialogScene) {

        this.mMainDialogScene = mainDialogScene;

        mLayer = new MyRotateViewGroup();
        mLayer.SetDebugName("Metro_Layer");
        mLayer.setWidth(EngineConstanst.REFERENCE_SCREEN_WIDTH);
        mLayer.setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
        mLayer.setBackgroundColor(Color.parseColor("#55000000"));
        mLayer.setVisibility(true);
        mLayer.setTranslate(0, 0, 0);
        mLayer.setFocusable(false);

        // luoyouren: 初始化背景球
//        InitSphere();

        // luoyouren: 增加背景墙
        InitBackgroundWall();

        InitFront();

        InitBack();

        InitLeft();

        InitRight();

        InitTop();

        InitBottom();
    }

    // 纹理ID
    int sphereBG[] = {
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

    public void InitSphere()
    {
        if (false)
        {
            //Front: 背景与画布
            MyRotateViewGroup viewGroup2 = new MyRotateViewGroup();
            viewGroup2.SetDebugName("SphereBackgroundViewGroup");
            viewGroup2.setWidth(EngineConstanst.REFERENCE_SCREEN_WIDTH);
            viewGroup2.setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
//			viewGroup2.setBackgroundColor(Color.parseColor("#2200ffff"));
            viewGroup2.setVisibility(true);
            viewGroup2.setTranslate(0, 0 ,0);
            viewGroup2.setFocusable(false);


            mSphere = (MyObjeView) makeSphereView(R.drawable.gray_shadow);
            mSphere.setFocusable(false);
            mSphere.setTouchAble(false);
            mSphere.setClickable(false);
            viewGroup2.addChild(mSphere);

            mLayer.addChild(viewGroup2);
        }
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

//                R.raw.palace_01,
//                R.raw.palace_02,
//                R.raw.palace_03,
//                R.raw.palace_04,
//                R.raw.palace_05,
//                R.raw.palace_06,


//                R.drawable.ic_launcher,
//                R.drawable.ic_launcher,
//                R.drawable.ic_launcher,
//                R.drawable.ic_launcher,
//                R.drawable.ic_launcher,
//                R.drawable.ic_launcher,
        };

        // 天空盒
        if (true)
        {
            //背景墙
            MyRotateViewGroup viewGroup2 = new MyRotateViewGroup();
            viewGroup2.SetDebugName("InitBackgroundWall");
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
//            backgroundView = makeImageView(sourceIds1[5], EngineConstanst.BOX_LENGTH, EngineConstanst.BOX_LENGTH, 0, -EngineConstanst.BOX_LENGTH/2, 0, "#ffffffff");
            backgroundView = makeImageView(sourceIds1[5], EngineConstanst.BOX_LENGTH, EngineConstanst.BOX_LENGTH, 0, -EngineConstanst.BOX_LENGTH/2, 0);
            backgroundView.setRotate(-90, 0, 0);
            viewGroup2.addChild(backgroundView);
            Log.i("luoyouren", "viewGroup2's child = " + viewGroup2.getChildCount());


            mLayer.addChild(viewGroup2);
            mBackgroundWallIndex = mLayer.indexOf(viewGroup2);
            isBackgroundInit = true;
        } else {
            // 天空球
            final float diameter = EngineConstanst.REFERENCE_SCREEN_HEIGHT;
//		ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/MySphere.obj");
            ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/MySkySphere.obj");
//		ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/TankWorld.obj");
//		ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/MyCube.obj");
//		ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/sphere02.obj");
            mSphere = new MyObjeView(objDrawable);
            mSphere.setWidth(diameter);
            mSphere.setHeight(diameter);
            mSphere.setThickness(diameter);
//		mSphere.setScale(2, 2, 2);
            mSphere.setRotate(0, 90, 0);

            mSphere.setTranslate(0, 0, 0);
            mSphere.setFocusable(false);
            mSphere.setTouchAble(false);
            mSphere.setCullFrontFace(true);

            mLayer.addChild(mSphere);
        }

        Log.i("luoyouren", "mLayer's child = " + mLayer.getChildCount());

    }

    private void InitFront()
    {
        if (true)
        {
            //Front: 背景与画布
            MyRotateViewGroup viewGroup2 = new MyRotateViewGroup();
            viewGroup2.SetDebugName("InitFront1");
            viewGroup2.setWidth(EngineConstanst.REFERENCE_SCREEN_WIDTH);
            viewGroup2.setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
//			viewGroup2.setBackgroundColor(Color.parseColor("#0000ffff"));
            viewGroup2.setVisibility(true);
            viewGroup2.setTranslate(0, 0 ,0);
            viewGroup2.setFocusable(false);
            viewGroup2.setClickable(false);
            viewGroup2.setTouchAble(false);


//            View canvasView = makeImageView(R.drawable.gray_shadow, 1773, 913, 0, 0, 4);
//            canvasView.setFocusable(false);
//            canvasView.setTouchAble(false);
//            canvasView.setClickable(false);
//            viewGroup2.addChild(canvasView);

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
            metroViewGroup.SetDebugName("InitFront2");
            metroViewGroup.setWidth(330 * 4 + 20 * 3);
            metroViewGroup.setHeight(200 + 25 + 200);
//            metroViewGroup.setBackgroundColor(Color.parseColor("#0000ffff"));
            metroViewGroup.setVisibility(true);
            metroViewGroup.setTranslate(0, 0, 0);
            metroViewGroup.setFocusable(false);
            metroViewGroup.setClickable(false);
            metroViewGroup.setTouchAble(false);

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
        if (true) {
            mVideoWall = new View();
            mVideoWall.SetDebugName("mVideoWall");
            mVideoWall.setWidth(EngineConstanst.REFERENCE_SCREEN_WIDTH);
            mVideoWall.setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT);

            // luoyouren:
            // 注意：某个View的mBGDrawable 没有任何贴图时，焦点扫描不到它，也就不会产生凝视点事件
//            mVideoWall.setBackgroundColor(Color.parseColor("#ffff0000"));
            mVideoWall.setBackgroundResource(R.raw.preview);

            mVideoWall.setTranslate(0, 0, EngineConstanst.REFERENCE_SCREEN_HEIGHT * 2.3f);
            mVideoWall.setRotate(0, 180, 0);

            mLayer.addChild(mVideoWall);
        }
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
            viewGroup2.setTranslate(0, EngineConstanst.REFERENCE_SCREEN_HEIGHT - 100, EngineConstanst.REFERENCE_SCREEN_HEIGHT * 1.0f);
            viewGroup2.setFocusable(false);

            View canvasView = makeImageView(R.drawable.weather_board, WEATHER_PIC_WIDTH, WEATHER_PIC_HEIGHT, 0, 0, 0);
            canvasView.setRotate(90, 0, 0);
            canvasView.setFocusable(false);
            canvasView.setTouchAble(false);
            canvasView.setClickable(false);
            viewGroup2.addChild(canvasView);

            View change = makeImageView(R.drawable.test22, 48, 48, -WEATHER_PIC_WIDTH/2 + 100, 0, -(WEATHER_PIC_HEIGHT/2+50));
            change.setRotate(90, 0, 0);
            change.setOnFocusListener(new View.OnFocusListener() {
                @Override
                public boolean onFocus(View v) {
                    touchCounts++;
                    updateBackground(touchCounts % 4);
                    return false;
                }

                @Override
                public boolean onRemoveFocus(View v) {
                    return false;
                }
            });

            viewGroup2.addChild(change);

            // 调整到45°
//            viewGroup2.setTranslate(0, -EngineConstanst.REFERENCE_SCREEN_HEIGHT * 0.5f, +EngineConstanst.REFERENCE_SCREEN_HEIGHT * 0.2f);
//            viewGroup2.setRotate(-40, 0, 0);

            // luoyouren: 让某些场景跟随视线移动
            viewGroup2.setEyeMatrixUpdate(true);


            mLayer.addChild(viewGroup2);

        }
    }

    private void InitBottom()
    {
        if (true)
        {
            //Bottom: LOGO
            MyRotateViewGroup viewGroup2 = new MyRotateViewGroup();
            viewGroup2.SetDebugName("LOGOViewGroup");
            viewGroup2.setWidth(EngineConstanst.REFERENCE_SCREEN_WIDTH);
            viewGroup2.setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
//			viewGroup2.setBackgroundColor(Color.parseColor("#0000ffff"));
            viewGroup2.setVisibility(true);
            viewGroup2.setTranslate(0, -EngineConstanst.REFERENCE_SCREEN_HEIGHT + 100, EngineConstanst.REFERENCE_SCREEN_HEIGHT);
            viewGroup2.setFocusable(false);

            final int side_length = 256 * 6;
            View circleView = makeImageView(R.drawable.circle3, side_length, side_length, 0, -EngineConstanst.REFERENCE_SCREEN_HEIGHT, EngineConstanst.REFERENCE_SCREEN_HEIGHT);
            circleView.setRotate(-90, 0, 0);
            circleView.setFocusable(false);
            circleView.setTouchAble(false);
            circleView.setClickable(false);
//            viewGroup2.addChild(circleView);

            View canvasView = makeImageView(R.drawable.pacific_logo, 1147, 306, 0, 0, 0);
            canvasView.setRotate(-90, 0, 0);
            canvasView.setFocusable(false);
            canvasView.setTouchAble(false);
            canvasView.setClickable(false);
            viewGroup2.addChild(canvasView);

            View change = makeImageView(R.drawable.pacific_site, 812, 79, 0, 0, WEATHER_PIC_HEIGHT/2 + 50);
            change.setRotate(-90, 0, 0);
            change.setFocusable(false);
            change.setTouchAble(false);
            change.setClickable(false);
            viewGroup2.addChild(change);

            // luoyouren: 让某些场景跟随视线移动
            viewGroup2.setEyeMatrixUpdate(true);

            mLayer.addChild(viewGroup2);

        }
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

                {R.raw.palace_01,
                R.raw.palace_02,
                R.raw.palace_03,
                R.raw.palace_04,
                R.raw.palace_05,
                R.raw.palace_06},



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
            for (int i = 0; i < 6; i++)
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

    public View makeCircleView(int sourceId)
    {
//        final View vv = new View();
//        vv.SetDebugName("makeCircleView");
//        vv.setWidth(2736);
//        vv.setHeight(2736);
//        vv.setTranslate(0, -EngineConstanst.REFERENCE_SCREEN_HEIGHT, EngineConstanst.REFERENCE_SCREEN_HEIGHT);
//        Bitmap bitmap = createCircleImage(R.drawable.circle, 2736);
//        vv.setBackground(bitmap);
////        vv.setBackgroundResource(R.drawable.circle);
////        vv.setBackgroundColor(Color.parseColor("#ffff0000"));
//        vv.setRotate(-90, 0, 0);
//
//        return vv;


        final ImageView vv = new ImageView();
        vv.SetDebugName("makeCircleView");
        vv.setWidth(2736);
        vv.setHeight(2736);

        // luoyouren:
        // 注意：某个View的mBGDrawable 没有任何贴图时，焦点扫描不到它，也就不会产生凝视点事件
        vv.setBackgroundColor(Color.parseColor("#00000000"));
        vv.setImageResource(sourceId);

        vv.setTranslate(0, -EngineConstanst.REFERENCE_SCREEN_HEIGHT, EngineConstanst.REFERENCE_SCREEN_HEIGHT);
//        vv.setOnStareAtListener(OnStareAtListener);
        return vv;
    }

    public View makeSphereView(int sourceId)
    {
            MyObjeView view = null;
//		ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/MySphere.obj");
                    ObjDrawable            objDrawable = Director.getInstance().sResourcer.loadObjModel("models/MySkySphere.obj");
//		ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/TankWorld.obj");
//		ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/MyCube.obj");
            mSphere = new MyObjeView(objDrawable);
            mSphere.setWidth(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
            mSphere.setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
            mSphere.setThickness(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
            mSphere.setScale(2, 2, 2);

            mSphere.setTranslate(0, 0, EngineConstanst.REFERENCE_SCREEN_HEIGHT);
            mSphere.setFocusable(false);
            mSphere.setTouchAble(false);
            mSphere.setCullFrontFace(true);
            mSphere.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mSphere.changeState();
                }
            });

        return mSphere;
    }


    final int ImageWidth = 326;
    final int ImageHeight = 199;
    final int ImageStride = 60;
    final int COLUMNS = 4;
    public View makeImageView(int sourceId, int index)
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

    public View makeImageView(int sourceId, float imageWidth, float imageHeight, float x, float y, float z)
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

    public View makeImageView(int sourceId, float imageWidth, float imageHeight, float x, float y, float z, int xxx)
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

    public View makeImageView(int sourceId, float imageWidth, float imageHeight, float x, float y, float z, String color)
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

    public View makeImageView(final int i ){

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
    public int getVideoTextureID() {
        mRestoreTextre = mVideoWall.getBackground();
        Texture texture  = Director.sResourcer.generateOES_Texture();
        mVideoWall.setBackground(texture, false);

        return texture.getTextureID();
    }
    public void restoreTexture() {
        // TODO Auto-generated method stub
        if(mRestoreTextre != null){
            mVideoWall.setBackground(mRestoreTextre);
        }
    }

}
