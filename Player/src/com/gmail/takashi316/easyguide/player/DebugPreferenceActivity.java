package com.gmail.takashi316.easyguide.player;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class DebugPreferenceActivity extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.debug_preference_screen);
	}// onCreate
}// DebugPreferenceActivity
