package com.x.opengl.kernel.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public abstract class AbsSensorStrategy implements SensorEventListener {

	public   interface  EngineSensorListenr{
		public void onSensorEvent(float[] tmpMatrix);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	public abstract void register(Context context,EngineSensorListenr engineSensorListenr) ;
	public abstract void unRegister() ;
	public abstract void reset();
}
