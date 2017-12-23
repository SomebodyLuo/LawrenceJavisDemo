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
	private float[] mTmpMatrix = new float[16];
	private float[] mSensorMatrix = new float[16];
	private Context mContext;
	private EngineSensorListenr mSensorListener;
	private Sensor sensor1;
	private Sensor sensor2;

    //luoyouren: 扩展卡尔曼滤波
    private final OrientationEKF mTracker = new OrientationEKF();
    private Vector3d mLatestAcc = new Vector3d();
    private final Vector3d mLatestGyro = new Vector3d();
    private final Vector3d mGyroBias = new Vector3d();
    private long mLatestGyroEventClockTimeNs;
	private boolean mRegistered;
    

    private float[] mEkfToHeadTracker = new float[16];

    private float[] mResultMatrix = new float[16];


    private float[] mRotateMatrix = new float[16];
    
	public SensorCardboardBlock() {
		mTracker.reset();
	}
	public void register(Context context,EngineSensorListenr sensorListenr){

		Log.d("ming", "register "+mRegistered);
		Log.d("ming", "register this "+this + ",,director = "+Director.getInstance());
        if (mRegistered) return;
		this.mContext = context;
		this.mSensorListener = sensorListenr;
		
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
		this.mTracker.reset();
		mSensorManager.registerListener(this, sensor1,	SensorManager.SENSOR_DELAY_GAME);
		mSensorManager.registerListener(this, sensor2,	SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		if(mSensorListener == null){
			return;
		}
//		Log.d("ming", "onSensorChanged "+SensorCardboardBlock.this);
		  if (event.accuracy != 0){
	
	            int type = event.sensor.getType();
	
	            if (type == Sensor.TYPE_ACCELEROMETER){
	                synchronized (mTracker){
	                    this.mLatestAcc.set(event.values[0], event.values[1], event.values[2]);
	                    this.mTracker.processAcc(this.mLatestAcc, event.timestamp);
	                }
	
	            } else if(type == Sensor.TYPE_GYROSCOPE){
	                synchronized (mTracker){
	                    this.mLatestGyroEventClockTimeNs = System.nanoTime();
	                    this.mLatestGyro.set(event.values[0], event.values[1], event.values[2]);
	                    Vector3d.sub(this.mLatestGyro, this.mGyroBias, this.mLatestGyro);
	                    this.mTracker.processGyro(this.mLatestGyro, event.timestamp);
	                }
	            }
	
	        }
	      run();
	}
	 public void run() {
         if (!mRegistered) return;

         // mTracker will be used in multi thread.
         synchronized (mTracker){
             final double secondsSinceLastGyroEvent = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - mLatestGyroEventClockTimeNs);
             final double secondsToPredictForward = secondsSinceLastGyroEvent + 1.0/60;
             final double[] mat = mTracker.getPredictedGLMatrix(secondsToPredictForward);
             for (int i = 0; i < mat.length; i++){
                 mTmpMatrix[i] = (float) mat[i];
             }
         }

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

         Matrix.multiplyMM(mResultMatrix, 0, mRotateMatrix, 0, mTmpMatrix , 0);
         Matrix.multiplyMM(mTmpMatrix, 0, mResultMatrix, 0, mEkfToHeadTracker, 0);

         // luoyouren: 这里把Sensor数据传给场景
         mSensorListener.onSensorEvent(mTmpMatrix) ;
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