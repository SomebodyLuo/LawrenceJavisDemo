package com.x.opengl.kernel.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.view.Surface;

public class SensorRotationVectorBlock extends AbsSensorStrategy {

	private static final String TAG = SensorRotationVectorBlock.class.getSimpleName();
	private SensorManager mSensorManager;
	private Object mMatrixLock = new Object();
	private float[] mTmpMatrix = new float[16];
	private float[] mSensorMatrix = new float[16];
	private Context mContext;
	private EngineSensorListenr mSensorListener;
	private Sensor sensors3;
	private boolean mRegistered;
	private float[] restoreValue = new float[3];
	
	public SensorRotationVectorBlock( ) {
	}
	public void register(Context context,EngineSensorListenr sensorListenr, EngineSensorListenr sensorListenr2){

        if (mRegistered) return;
        this.mContext = context;
		this.mSensorListener = sensorListenr;
		
		mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
		mSensorManager.unregisterListener(this);
		
		sensors3 =  mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		mSensorManager.registerListener(this, sensors3,	SensorManager.SENSOR_DELAY_GAME);
		mRegistered = true;
	}
	public void unRegister() {
        if (!mRegistered) return;
		mSensorManager.unregisterListener(this );
		mRegistered = false;
	}

	@Override
	public void reset() {
		
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		if(mSensorListener == null){
			return;
		}
		
		 int type = event.sensor.getType();
		 switch (type){
              case Sensor.TYPE_ROTATION_VECTOR:
                  // post
                   sensorRotationVector2Matrix(event, Surface.ROTATION_90, mSensorMatrix);

                  // mTmpMatrix will be used in multi thread.
//	                  synchronized (mMatrixLock){
                      System.arraycopy(mSensorMatrix, 0, mTmpMatrix, 0, 16);
//	                  }
//	                  	MatrixState.setEyeMatrix(mTmpMatrix);
                      mSensorListener.onSensorEvent(mTmpMatrix) ;
              break;
          }
          
          
	}
    private static float[] mTmp = new float[16];
    public static final float sNotHit = Float.MAX_VALUE;

    private void sensorRotationVector2Matrix(SensorEvent event, int rotation, float[] output) {

		restoreValue   =   event.values;
        switch (rotation){
            case Surface.ROTATION_0:
            case Surface.ROTATION_180: /* Notice: not supported for ROTATION_180! */
                SensorManager.getRotationMatrixFromVector(output, restoreValue);
                break;
            case Surface.ROTATION_90:
                SensorManager.getRotationMatrixFromVector(mTmp, restoreValue);
                SensorManager.remapCoordinateSystem(mTmp, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, output);
                break;
            case Surface.ROTATION_270:
                SensorManager.getRotationMatrixFromVector(mTmp, restoreValue);
                SensorManager.remapCoordinateSystem(mTmp, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, output);
                break;
        }
        Matrix.rotateM(output, 0, 90.0F, 1.0F, 0.0F, 0.0F);
    }

}