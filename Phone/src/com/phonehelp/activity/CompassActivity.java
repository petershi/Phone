package com.phonehelp.activity;

import com.phonehelp.R;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class CompassActivity extends Activity {

	private TextView compassTextView;
	private SensorManager sensorManager;
	private Sensor sensor;
	private ImageView compassImageView;
	private float fromDegrees = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle(R.string.compass);
		this.setContentView(R.layout.compass_activity);

		compassTextView = (TextView) findViewById(R.id.compasstextView);
		compassImageView = (ImageView) findViewById(R.id.compassimageView);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensorManager.registerListener(sensorListener, sensor,
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	@Override
	protected void onPause() {
		sensorManager.unregisterListener(sensorListener);
		super.onPause();
	}

	private SensorEventListener sensorListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			float[] values = event.values;
			setText(values[0]);
			// compassTextView.setText("X--->"+values[0] );
			if (fromDegrees != values[0]) {
				AnimationSet animationSet = new AnimationSet(true);
				RotateAnimation rotateAnimation = new RotateAnimation(
						fromDegrees, 359 - values[0],
						AnimationSet.RELATIVE_TO_SELF, 0.5f,
						AnimationSet.RELATIVE_TO_SELF, 0.5f);

				animationSet.addAnimation(rotateAnimation);
				animationSet.setDuration(10);
				animationSet.setFillAfter(true);
				compassImageView.startAnimation(animationSet);
			}

			fromDegrees = 359 - values[0];
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}

	};

	private void setText(float value) {
		if (value < 22.5 || value >= 337.5) {
			compassTextView.setText("北" + value);
		} else if (value >= 22.5 && value < 67.5) {
			compassTextView.setText("东北" + value);
		} else if (value >= 67.5 && value < 112.5) {
			compassTextView.setText("东" + value);
		} else if (value >= 112.5 && value < 157.5) {
			compassTextView.setText("东南" + value);
		} else if (value >= 157.5 && value < 202.5) {
			compassTextView.setText("南" + value);
		} else if (value >= 202.5 && value < 247.5) {
			compassTextView.setText("西南" + value);
		} else if (value >= 247.5 && value < 292.5) {
			compassTextView.setText("西" + value);
		} else if (value >= 292.5 && value < 337.5) {
			compassTextView.setText("西北" + value);
		}
	}

}
