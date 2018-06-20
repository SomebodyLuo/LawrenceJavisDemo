package com.hello.scene_for_vr;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
import com.hello.scene_for_vr.mymediapackage.CameraViewManager;
import com.x.EngineGLView;
import com.x.components.widget.ImageView;
import com.x.opengl.kernel.sensor.TestSensorCardboardBlock;

import java.util.Arrays;

import static android.hardware.camera2.CameraDevice.TEMPLATE_PREVIEW;

public class HomeActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback{

	private final static String TAG = "luoyouren";
	private Context mContext;
	private VRGLView mVrView;
	private View coverView;

	private CameraViewManager mCameraViewManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = getApplicationContext();

		requestCameraPermissions();

//		mCameraViewManager = new CameraViewManager(mContext, this);
//		mCameraViewManager.initCameraView();

		initView();
		time = System.currentTimeMillis();
	}

	public static long time = System.currentTimeMillis();


	private void requestCameraPermissions()
	{
		if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
			//申请权限
			requestPermissions(CAMERA_PERMISSIONS, REQUEST_CAMERA_PERMISSIONS);
			//return;
		} else {

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

			} else {
				// Permission Denied
				//申请权限
				requestPermissions(CAMERA_PERMISSIONS, REQUEST_CAMERA_PERMISSIONS);
			}
		}
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
//		viewGroup.addView(mCameraViewManager.getSurfaceView(), layoutParams);

		viewGroup.addView(mVrView, layoutParams);

		CollisionBoxView cv = (CollisionBoxView) findViewById(R.id.CollisionView);
		((EngineGLView)mVrView).setCollisionBoxView(cv);
		// mDataTool = new DataTool(HomeActivity.this, myTarotView);
		// mStatusTool.setmDataTool(mDataTool);
		
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
