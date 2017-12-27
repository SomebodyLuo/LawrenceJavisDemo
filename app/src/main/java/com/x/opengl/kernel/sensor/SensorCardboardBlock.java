package com.x.opengl.kernel.sensor;

import java.util.concurrent.TimeUnit;

import com.x.Director;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Surface;

public class SensorCardboardBlock extends AbsSensorStrategy  {

	private SensorManager mSensorManager;
	private Object mMatrixLock = new Object();

	private Context mContext;

	private Sensor sensor1;
	private Sensor sensor2;

    //-----------------------------------------------------------------------
    //luoyouren: 扩展卡尔曼滤波
    // 给OpenGL中的Camera用
    private final OrientationEKF mTrackerForCamera = new OrientationEKF();
    private Vector3d mLatestAccForCamera = new Vector3d();
    private final Vector3d mLatestGyroForCamera = new Vector3d();
    private final Vector3d mGyroBiasForCamera = new Vector3d();
    private long mLatestGyroEventClockTimeNsForCamera;

    private float[] mTmpMatrixForCamera = new float[16];

    private EngineSensorListenr mSensorListenerForCamera;

    private float[] mResultMatrixForCamera = new float[16];

    // 给OpenGL中的Obj用
    private final OrientationEKF mTrackerForObj = new OrientationEKF();
    private Vector3d mLatestAccForObj = new Vector3d();
    private final Vector3d mLatestGyroForObj = new Vector3d();
    private final Vector3d mGyroBiasForObj = new Vector3d();
    private long mLatestGyroEventClockTimeNsForObj;

    private float[] mTmpMatrixForObj = new float[16];

    private EngineSensorListenr mSensorListenerForObj;

    private float[] mResultMatrixForObj = new float[16];
    //-----------------------------------------------------------------------

	private boolean mRegistered;

    private float[] mEkfToHeadTracker = new float[16];

    private float[] mRotateMatrix = new float[16];





	public SensorCardboardBlock() {
		mTrackerForCamera.reset();
        mTrackerForObj.reset();
	}
	public void register(Context context,EngineSensorListenr sensorListenrForCamera, EngineSensorListenr sensorListenrForObj){

		Log.d("ming", "register "+mRegistered);
		Log.d("ming", "register this "+this + ",,director = "+Director.getInstance());
        if (mRegistered) return;
		this.mContext = context;
		this.mSensorListenerForCamera = sensorListenrForCamera;
        this.mSensorListenerForObj = sensorListenrForObj;
		
		mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
		mSensorManager.unregisterListener(this);

        sensor1 = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensor2 = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if (sensor1 == null && sensor2 == null){
            Log.e("ming","TYPE_ACCELEROMETER TYPE_GYROSCOPE sensor not support!");
            return;
        }
		mSensorManager.registerListener(this, sensor1,	SensorManager.SENSOR_DELAY_GAME);
		mSensorManager.registerListener(this, sensor2,	SensorManager.SENSOR_DELAY_GAME);
		mRegistered = true;
	}
	public void unRegister() {
		Log.d("ming", "unRegister "+mRegistered);
        if (!mRegistered) return;
		mSensorManager.unregisterListener(this);
		mRegistered = false;
	}

	/**
	 * 重置camera eye tracer的位置
	 */
	@Override
	public void reset() {
		Log.d("ming", "reset "+mRegistered);
        if (!mRegistered) return;
		mSensorManager.unregisterListener(this);
		this.mTrackerForCamera.reset();
        this.mTrackerForObj.reset();
		mSensorManager.registerListener(this, sensor1,	SensorManager.SENSOR_DELAY_GAME);
		mSensorManager.registerListener(this, sensor2,	SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		if((mSensorListenerForCamera == null) || (mSensorListenerForObj == null)){
			return;
		}
//		Log.d("ming", "onSensorChanged "+SensorCardboardBlock.this);
        if (event.accuracy != 0){

            int type = event.sensor.getType();

            if (type == Sensor.TYPE_ACCELEROMETER){
                synchronized (mTrackerForCamera){
                    this.mLatestAccForCamera.set(event.values[0], event.values[1], event.values[2]);
                    this.mTrackerForCamera.processAcc(this.mLatestAccForCamera, event.timestamp);
                }

                synchronized (mTrackerForObj){
                    this.mLatestAccForObj.set(event.values[0], event.values[1], event.values[2]);
                    this.mTrackerForObj.processAcc(this.mLatestAccForObj, event.timestamp);
                }

            } else if(type == Sensor.TYPE_GYROSCOPE){
                synchronized (mTrackerForCamera){
                    this.mLatestGyroEventClockTimeNsForCamera = System.nanoTime();
                    this.mLatestGyroForCamera.set(event.values[0], event.values[1], event.values[2]);
                    Vector3d.sub(this.mLatestGyroForCamera, this.mGyroBiasForCamera, this.mLatestGyroForCamera);
                    this.mTrackerForCamera.processGyro(this.mLatestGyroForCamera, event.timestamp);
                }

                synchronized (mTrackerForObj){
                    this.mLatestGyroEventClockTimeNsForObj = System.nanoTime();
                    this.mLatestGyroForObj.set(event.values[0], event.values[1], event.values[2]);
                    Vector3d.sub(this.mLatestGyroForObj, this.mGyroBiasForObj, this.mLatestGyroForObj);
                    this.mTrackerForObj.processGyro(this.mLatestGyroForObj, event.timestamp);
                }
            }
        }

        // 预测角度
        checkRotation();
        runForCamera();
        runForObj();

	}
	 public void runForCamera() {
         if (!mRegistered) return;

         // mTrackerForCamera will be used in multi thread.
         synchronized (mTrackerForCamera){
             final double secondsSinceLastGyroEvent = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - mLatestGyroEventClockTimeNsForCamera);
             final double secondsToPredictForward = secondsSinceLastGyroEvent + 1.0/60;
             final double[] mat = mTrackerForCamera.getPredictedGLMatrix(secondsToPredictForward);
             for (int i = 0; i < mat.length; i++){
                 mTmpMatrixForCamera[i] = (float) mat[i];
             }
         }

         Matrix.multiplyMM(mResultMatrixForCamera, 0, mRotateMatrix, 0, mTmpMatrixForCamera, 0);
         Matrix.multiplyMM(mTmpMatrixForCamera, 0, mResultMatrixForCamera, 0, mEkfToHeadTracker, 0);

         // luoyouren: 这里把Sensor数据传给场景
         mSensorListenerForCamera.onSensorEvent(mTmpMatrixForCamera);
     }

    public void runForObj() {
        if (!mRegistered) return;

        // mTrackerForCamera will be used in multi thread.
        synchronized (mTrackerForObj){
            final double secondsSinceLastGyroEvent = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - mLatestGyroEventClockTimeNsForObj);
            final double secondsToPredictForward = secondsSinceLastGyroEvent + 1.0/60;
            final double[] mat = mTrackerForObj.getPredictedGLMatrix(secondsToPredictForward);
            for (int i = 0; i < mat.length; i++){
                mTmpMatrixForObj[i] = (float) mat[i];
            }
        }

        Matrix.multiplyMM(mResultMatrixForObj, 0, mRotateMatrix, 0, mTmpMatrixForObj, 0);
        Matrix.multiplyMM(mTmpMatrixForObj, 0, mResultMatrixForObj, 0, mEkfToHeadTracker, 0);

        // luoyouren: 这里把Sensor数据传给场景
        mSensorListenerForObj.onSensorEvent(mTmpMatrixForObj) ;
    }

    private void checkRotation()
    {
        float rotation = 0;
        switch (Surface.ROTATION_90){
            case Surface.ROTATION_0:
                rotation = 0;
                break;
            case Surface.ROTATION_90:
                rotation = 90.0f;
                break;
            case Surface.ROTATION_180:
                rotation = 180.0f;
                break;
            case Surface.ROTATION_270:
                rotation = 270.0f;
                break;
        }

        Matrix.setRotateEulerM(mRotateMatrix, 0, 0.0f, 0.0f, -rotation);
        Matrix.setRotateEulerM(mEkfToHeadTracker, 0, -90.0f, 0.0f, rotation);
    }

    private static float[] mTmp = new float[16];
    public static final float sNotHit = Float.MAX_VALUE;

    private void sensorRotationVector2Matrix(float[] values, int rotation, float[] output) {
      
        switch (rotation){
            case Surface.ROTATION_0:
            case Surface.ROTATION_180: /* Notice: not supported for ROTATION_180! */
                SensorManager.getRotationMatrixFromVector(output, values);
                break;
            case Surface.ROTATION_90:
                SensorManager.getRotationMatrixFromVector(mTmp, values);
                SensorManager.remapCoordinateSystem(mTmp, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, output);
                break;
            case Surface.ROTATION_270:
                SensorManager.getRotationMatrixFromVector(mTmp, values);
                SensorManager.remapCoordinateSystem(mTmp, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, output);
                break;
        }
        Matrix.rotateM(output, 0, 90.0F, 1.0F, 0.0F, 0.0F);
    }
}