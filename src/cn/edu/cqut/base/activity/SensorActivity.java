package cn.edu.cqut.base.activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import cn.edu.cqut.base.util.MediaUtil;

/**
 * 处理摇晃手机Activity
 * 
 * @author chenliang
 * @version v1.0
 * @date 2014-3-18
 * 
 */
public abstract class SensorActivity extends BaseActivity implements
		SensorEventListener {
	// Sensor管理器
	private SensorManager mSensorManager = null;
	private boolean isShake = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	/** 处理震动 */
	public abstract void handleSersorChanged(SensorEvent event);

	/** 震动完成 */
	public void shakeCompleted() {
		isShake = false;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		int sensorType = event.sensor.getType();
		float[] values = event.values;
		if (sensorType == Sensor.TYPE_ACCELEROMETER) {
			if (Math.abs(values[0]) > 14 || Math.abs(values[1]) > 14
					|| Math.abs(values[2]) > 14) {
				if (!isShake) {
					isShake = true;
					MediaUtil.vibrate(getApplicationContext(), 500);
					handleSersorChanged(event);
				}
			}
		}
	}

	@Override
	protected void onResume() {
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mSensorManager.unregisterListener(this);
		super.onDestroy();
	}
}
