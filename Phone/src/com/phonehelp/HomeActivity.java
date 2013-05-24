package com.phonehelp;

import com.phonehelp.activity.BlackListActivity;
import com.phonehelp.activity.BlockedLogsActivity;
import com.phonehelp.activity.CompassActivity;
import com.phonehelp.activity.SettingsActivity;
import com.phonehelp.activity.TorchActivity;
import com.phonehelp.activity.WordActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class HomeActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.home_activity);

		findViewById(R.id.interceptbutton).setOnClickListener(this);
		findViewById(R.id.blacklistbutton).setOnClickListener(this);
		findViewById(R.id.wordbutton).setOnClickListener(this);
		findViewById(R.id.compassbutton).setOnClickListener(this);
		findViewById(R.id.torchbutton).setOnClickListener(this);
		findViewById(R.id.settingsbutton).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.interceptbutton:
			intent.setClass(getApplicationContext(), BlockedLogsActivity.class);
			break;
		case R.id.blacklistbutton:
			intent.setClass(getApplicationContext(), BlackListActivity.class);
			break;
		case R.id.compassbutton:
			intent.setClass(getApplicationContext(), CompassActivity.class);
			break;
		case R.id.torchbutton:
			intent.setClass(getApplicationContext(), TorchActivity.class);
			break;
		case R.id.wordbutton:
			intent.setClass(getApplicationContext(), WordActivity.class);
			break;
		case R.id.settingsbutton:
			intent.setClass(getApplicationContext(), SettingsActivity.class);
			break;

		default:
			return;
		}

		startActivity(intent);
	}

}
