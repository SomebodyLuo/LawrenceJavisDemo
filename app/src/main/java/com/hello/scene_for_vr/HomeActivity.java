package com.hello.scene_for_vr;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.widget.Toast;

import com.hello.R;
import com.hello.scene_for_vr.collisionbox.CollisionBoxView;
import com.x.EngineGLView;
import com.x.components.widget.ImageView;
import com.x.opengl.kernel.sensor.TestSensorCardboardBlock;

import java.util.Arrays;

import static android.hardware.camera2.CameraDevice.TEMPLATE_PREVIEW;

public class HomeActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback{

	private final static String TAG = "luoyouren";
	private VRGLView mVrView;
	private View coverView;

	// 用于渲染相机预览画面
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private ImageView iv_show;
	private ImageView mCapture;
	private CameraManager mCameraManager;//摄像头管理器
	private Handler childHandler, mainHandler;
	private String mCameraID;//摄像头Id 0 为后  1 为前
	private ImageReader mImageReader;
	private CameraCaptureSession mCameraCaptureSession;
	private CameraDevice mCameraDevice;

	private TextureView mTextureView;

	TestSensorCardboardBlock mTestSensorCardboardBlock ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		initCameraView();
		initView();
		time = System.currentTimeMillis();
	}	
	public static long time = System.currentTimeMillis();

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
			Toast.makeText(getApplicationContext(), "摄像头开启失败", Toast.LENGTH_SHORT).show();
		}
	};

	/**
	 * 初始化
	 */
	private void initCameraView() {
		//mSurfaceView
//		mSurfaceView = (SurfaceView) findViewById(R.id.camera_surface);

		mSurfaceView = new SurfaceView(this);
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
					mCameraDevice = null;
				}
			}
		});
	}

	/**
	 * 初始化Camera2
	 */
//	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	private void initCamera2() {
		HandlerThread handlerThread = new HandlerThread("Camera2");
		handlerThread.start();
		childHandler = new Handler(handlerThread.getLooper());
		mainHandler = new Handler(getMainLooper());
		mCameraID = "" + CameraCharacteristics.LENS_FACING_FRONT;//后摄像头
		mImageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 1);
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
		//获取摄像头管理
		mCameraManager = (CameraManager) getApplication().getSystemService(getApplication().CAMERA_SERVICE);
		try {
			if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
				//申请权限
				requestPermissions(CAMERA_PERMISSIONS, REQUEST_CAMERA_PERMISSIONS);
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
	 * Request code for camera permissions.
	 */
	private static final int REQUEST_CAMERA_PERMISSIONS = 1;

	/**
	 * Permissions required to take a picture.
	 */
	private static final String[] CAMERA_PERMISSIONS = {
			Manifest.permission.CAMERA,
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
	};

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_CAMERA_PERMISSIONS) {
			if (grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
				// Permission Granted
//				try {
//					mCameraManager.openCamera(mCameraID, stateCallback, mainHandler);
//				} catch (CameraAccessException e) {
//					e.printStackTrace();
//				} catch (SecurityException e) {
//					e.printStackTrace();
//				}
			} else {
				// Permission Denied
				//申请权限
				requestPermissions(CAMERA_PERMISSIONS, REQUEST_CAMERA_PERMISSIONS);
			}
		}
	}

	/**
	 * 开始预览
	 */
	private void takePreview() {
		try {
			// 创建预览需要的CaptureRequest.Builder
			final CaptureRequest.Builder previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
			// 将SurfaceView的surface作为CaptureRequest.Builder的目标
			previewRequestBuilder.addTarget(mSurfaceHolder.getSurface());
			// 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
			mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceHolder.getSurface(), mImageReader.getSurface()), new CameraCaptureSession.StateCallback() // ③
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
					Toast.makeText(getApplicationContext(), "配置失败", Toast.LENGTH_SHORT).show();
				}
			}, childHandler);
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 拍照
	 */
	private void takePicture() {
		if (mCameraDevice == null) return;
		// 创建拍照需要的CaptureRequest.Builder
//		final CaptureRequest.Builder captureRequestBuilder;
//		try {
//			captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
//			// 将imageReader的surface作为CaptureRequest.Builder的目标
//			captureRequestBuilder.addTarget(mImageReader.getSurface());
//			// 自动对焦
//			captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
//			// 自动曝光
//			captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
//			// 获取手机方向
//			int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
//			// 根据设备方向计算设置照片的方向
//			captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
//			//拍照
//			CaptureRequest mCaptureRequest = captureRequestBuilder.build();
//			mCameraCaptureSession.capture(mCaptureRequest, null, childHandler);
//		} catch (CameraAccessException e) {
//			e.printStackTrace();
//		}
	}

	private void initListener() {
//		mCatture.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				takePicture();
//			}
//		});
	}



	
	private void initView() {
		ViewGroup viewGroup = (RelativeLayout) findViewById(R.id.MainRelative);
		mVrView = new VRGLView(this);

		//luoyouren: Transparent
		mVrView.setBackgroundTransparentEnable(true);

//		myWolfView.setBackgroundColor(Color.CYAN);
		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		//luoyouren: Camera SurfaceView
//		viewGroup.addView(mSurfaceView, layoutParams);

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
