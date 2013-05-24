package com.phonehelp.activity;

import com.phonehelp.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		this.setTitle(R.string.settings);

	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if ("incomedisable".equals(preference.getKey())) {
			SharedPreferences settings = preference.getSharedPreferences();
			boolean incomedisable = settings.getBoolean(preference.getKey(),
					false);
			if (incomedisable) {
			} else {

			}

		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);

	}

}
