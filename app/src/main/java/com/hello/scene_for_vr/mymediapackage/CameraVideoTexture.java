package com.hello.scene_for_vr.mymediapackage;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by pc on 2018/6/19.
 */

public class CameraVideoTexture  implements SurfaceTexture.OnFrameAvailableListener{

    // 用于渲染相机预览画面
    private CameraManager mCameraManager;//摄像头管理器
    private Handler childHandler, mainHandler;
    private String mCameraID;//摄像头Id 0 为后  1 为前
    private ImageReader mImageReader;
    private CameraCaptureSession mCameraCaptureSession;
    private CameraDevice mCameraDevice;

    private boolean frameAvailable = false;
    private SurfaceTexture videoTexture;

    private Context mContext;
    private Activity mActivity;

    public CameraVideoTexture(Context context)
    {
        mContext = context;
    }


    /**
     * 摄像头创建监听
     */
    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {//打开摄像头
            mCameraDevice = camera;
            //开启预览
            takePreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {//关闭摄像头
            if (null != mCameraDevice) {
                mCameraDevice.close();
            }
        }

        @Override
        public void onError(CameraDevice camera, int error) {//发生错误
            Toast.makeText(mContext, "摄像头开启失败", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 初始化
     */
    public void initCameraView(int texturesid) {

        videoTexture = new SurfaceTexture(texturesid);
        videoTexture.setOnFrameAvailableListener(this);
        videoTexture.setDefaultBufferSize(1920, 1080);

        initCamera2();
    }

    /**
     * 初始化Camera2
     */
//	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initCamera2() {
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        childHandler = new Handler(handlerThread.getLooper());

        HandlerThread handlerThread2 = new HandlerThread("Camera2-main");
        handlerThread2.start();
        mainHandler = new Handler(handlerThread2.getLooper());

        mCameraID = "" + CameraCharacteristics.LENS_FACING_FRONT;//后摄像头

        if (true) {
            mImageReader = ImageReader.newInstance(1920, 1080, ImageFormat.JPEG, 1);
            mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() { //可以在这里处理拍照得到的临时照片 例如，写入本地
                @Override
                public void onImageAvailable(ImageReader reader) {
                    //mCameraDevice.close();
                    // 拿到拍照照片数据
//				Image image = reader.acquireNextImage();
//				ByteBuffer buffer = image.getPlanes()[0].getBuffer();
//				byte[] bytes = new byte[buffer.remaining()];
//				buffer.get(bytes);//由缓冲区存入字节数组
//				final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//				if (bitmap != null) {
//					iv_show.setImageBitmap(bitmap);
//				}
                }
            }, mainHandler);
        }

        //获取摄像头管理
        mCameraManager = (CameraManager) mContext.getSystemService(mContext.CAMERA_SERVICE);
        try {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                //return;
            } else {
                //打开摄像头
                mCameraManager.openCamera(mCameraID, stateCallback, mainHandler);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }



    /**
     * 开始预览
     */
    private void takePreview() {
        try {
            Surface surface = new Surface(videoTexture);

            // 创建预览需要的CaptureRequest.Builder
            final CaptureRequest.Builder previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            // 将SurfaceView的surface作为CaptureRequest.Builder的目标
            previewRequestBuilder.addTarget(surface);
            // 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()), new CameraCaptureSession.StateCallback() // ③
            {
                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    if (null == mCameraDevice) return;
                    // 当摄像头已经准备好时，开始显示预览
                    mCameraCaptureSession = cameraCaptureSession;
                    try {
                        // 自动对焦
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        // 打开闪光灯
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                        // 显示预览
                        CaptureRequest previewRequest = previewRequestBuilder.build();
                        mCameraCaptureSession.setRepeatingRequest(previewRequest, null, childHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(mContext, "配置失败", Toast.LENGTH_SHORT).show();
                }
            }, childHandler);


        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void updateTexImage(float[] videoTextureTransform) {
        synchronized (this) {
            if (frameAvailable) {

                frameAvailable = false;
                videoTexture.updateTexImage();
//                videoTexture.getTransformMatrix(videoTextureTransform);
            }
        }
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
//    	Log.d("ming", "onFrameAvailable = ");
        synchronized (this) {
            frameAvailable = true;
        }
    }

}
