package com.hello.scene_for_vr;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Size;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;

import com.hello.R;
import com.hello.scene_for_vr.collisionbox.CollisionBoxView;
import com.x.EngineGLView;
import com.x.opengl.kernel.sensor.TestSensorCardboardBlock;

import java.util.Arrays;

import static android.hardware.camera2.CameraDevice.TEMPLATE_PREVIEW;

public class HomeActivity extends Activity {

	private final static String TAG = "luoyouren";
	private VRGLView mVrView;
	private View coverView;

	// 用于渲染相机预览画面
	private CameraDevice mCameraDevice;
	private CameraManager mCameraManager;
	private int cameraId = 0;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private HandlerThread mThread;
	private Handler mHandler;
	private Size mPreviewSize;

	TestSensorCardboardBlock mTestSensorCardboardBlock ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		initView();
		initCameraView();
		time = System.currentTimeMillis();
	}	
	public static long time = System.currentTimeMillis();

	/**
	 * 初始化
	 */
	private void initCameraView() {
		mSurfaceView = (SurfaceView) findViewById(R.id.camera_surface);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.setKeepScreenOn(true);
		// mSurfaceView添加回调
		mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder holder) { //SurfaceView创建
				// 初始化Camera
				initCamera2();
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) { //SurfaceView销毁
				// 释放Camera资源
				if (null != mCameraDevice) {
					mCameraDevice.close();
				}
			}
		});
	}
	/**
	 * 初始化Camera2
	 */
	private void initCamera2()
	{

		try {
			if(mCameraDevice != null){
				mCameraDevice.close();
				mCameraDevice = null;
			}

			CameraCharacteristics c = mCameraManager.getCameraCharacteristics(cameraId + "");
			StreamConfigurationMap map = c.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
			assert map != null;
			Size[] sizes = map.getOutputSizes(SurfaceHolder.class);
			//自定义规则，选个大小
            /*
            luoyouren_CTR: size's lenth = 9
            luoyouren_CTR: sizes[0] = 1440 / 1080 ; 1440x1080=1555200
            luoyouren_CTR: sizes[1] = 1280 / 960
            luoyouren_CTR: sizes[2] = 1280 / 720
            luoyouren_CTR: sizes[3] = 960 / 540
            luoyouren_CTR: sizes[4] = 720 / 480
            luoyouren_CTR: sizes[5] = 640 / 480 ; 640x480=307200
            luoyouren_CTR: sizes[6] = 352 / 288
            luoyouren_CTR: sizes[7] = 320 / 240
            luoyouren_CTR: sizes[8] = 176 / 144
             */
			mPreviewSize = sizes[0];
			Log.d(TAG, "size's lenth = " + sizes.length);

			mCameraManager.openCamera(cameraId + "", stateCallback,null);

		} catch (SecurityException | CameraAccessException e) {
			e.printStackTrace();
		}

	}

	private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
		@Override
		public void onOpened(CameraDevice camera) {
			mCameraDevice = camera;
			//开启预览
			startPreview();
		}
		@Override
		public void onDisconnected(@NonNull CameraDevice cameraDevice) {
			if (null != mCameraDevice) {
				mCameraDevice.close();
			}
		}

		@Override
		public void onError(@NonNull CameraDevice cameraDevice, int i) {
//			Toast.makeText(getApplication(), "摄像头开启失败", Toast.LENGTH_SHORT).show();
		}
	};

	/**
	 * 开始预览
	 */
	private void startPreview() {
		try {
			// 创建预览需要的CaptureRequest.Builder
			final CaptureRequest.Builder previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
			// 将SurfaceView的surface作为CaptureRequest.Builder的目标
			previewRequestBuilder.addTarget(mSurfaceHolder.getSurface());
			// 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
//			mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceHolder.getSurface(), mImageReader.getSurface()), new CameraCaptureSession.StateCallback() // ③
//			{
//				@Override
//				public void onConfigured(CameraCaptureSession cameraCaptureSession) {
//					if (null == mCameraDevice) return;
//					// 当摄像头已经准备好时，开始显示预览
//					mCameraCaptureSession = cameraCaptureSession;
//					try {
//						// 自动对焦
//						previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
//						// 打开闪光灯
//						previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
//						// 显示预览
//						CaptureRequest previewRequest = previewRequestBuilder.build();
//						mCameraCaptureSession.setRepeatingRequest(previewRequest, null, childHandler);
//					} catch (CameraAccessException e) {
//						e.printStackTrace();
//					}
//				}
//
//				@Override
//				public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
//					Toast.makeText(getContext(), "配置失败", Toast.LENGTH_SHORT).show();
//				}
//			}, childHandler);
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}
	}



	
	private void initView() {
		ViewGroup viewGroup = (RelativeLayout) findViewById(R.id.MainRelative);
		mVrView = new VRGLView(this);
//		myWolfView.setBackgroundColor(Color.CYAN);
		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		viewGroup.addView(mVrView, layoutParams);

		CollisionBoxView cv = (CollisionBoxView) findViewById(R.id.CollisionView);
		((EngineGLView)mVrView).setCollisionBoxView(cv);
		// mDataTool = new DataTool(HomeActivity.this, myTarotView);
		// mStatusTool.setmDataTool(mDataTool);
		
//		new Handler().postDelayed(new Runnable() {
//			
//			@Override
//			public void run() {
//				mTestSensorCardboardBlock = new TestSensorCardboardBlock();
//				mTestSensorCardboardBlock.register(getApplicationContext(), new EngineSensorListenr() {
//					
//					@Override
//					public void onSensorEvent(float[] tmpMatrix) {
//						Director.getInstance().testSensorEvent(tmpMatrix);
//					}
//				});
//			}
//		}, 3000);
		
				
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub

		if (mVrView != null) {
			if(mVrView.dispatchGlKeyEvent(event)){
				return true;
			}
		}
		
		
		if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		
		return super.dispatchKeyEvent(event);
	}

	private void init() {
	}

	public void removeCover() {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				coverView.setVisibility(View.GONE);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		mTestSensorCardboardBlock.unRegister();
		((MyDirector)MyDirector.getInstance()).onDestory();
	}
	@Override
	protected void onPause() {
		super.onPause();
		((MyDirector)MyDirector.getInstance()).onPause();
	}

}
