package com.phonehelp.activity;

import com.phonehelp.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BlockedLogsActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.blockedlogs_activity);

	}

	@Override
	public void onBackPressed() {
		this.finish();
	}

}
