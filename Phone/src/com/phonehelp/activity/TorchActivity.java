package com.phonehelp.activity;

import com.phonehelp.R;
import android.app.Activity;
import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class TorchActivity extends Activity {

	private Camera camera;
	private Parameters params;
	private ToggleButton torchToggleButton;
	private Boolean changeSwitch = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.torch_activity);
		this.setTitle(R.string.torch);
		Log.i("isSupportLed", isSupportLed(this.getApplicationContext()) + "");

		torchToggleButton = (ToggleButton) findViewById(R.id.torchtoggleButton);
		if (isSupportLed(this.getApplicationContext())) {
			torchToggleButton
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								buttonView
										.setButtonDrawable(R.drawable.torch_on);
								camera = Camera.open();
								params = camera.getParameters();

								params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
								camera.setParameters(params);

							} else {
								buttonView
										.setButtonDrawable(R.drawable.torch_off);
								params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
								camera.setParameters(params);
								camera.release();
							}

						}
					});
		} else {
			torchToggleButton.setButtonDrawable(R.drawable.white);
			torchToggleButton.setClickable(false);
			changeSwitch = true;

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, R.string.change);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {
			if (changeSwitch) {
				torchToggleButton.setButtonDrawable(R.drawable.torch_off);
				torchToggleButton.setClickable(true);
				changeSwitch = false;
			} else {
				torchToggleButton.setButtonDrawable(R.drawable.white);
				torchToggleButton.setClickable(false);
				changeSwitch = true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private boolean isSupportLed(Context paramContext) {
		Boolean isSupport = false;
		FeatureInfo[] arrayOfFeatureInfo = paramContext.getPackageManager()
				.getSystemAvailableFeatures();
		for (FeatureInfo f : arrayOfFeatureInfo) {
			Log.i("FeatureInfo", f.name + "");
			if (PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) {
				isSupport = true;
			}
		}

		return isSupport;
	}

}
