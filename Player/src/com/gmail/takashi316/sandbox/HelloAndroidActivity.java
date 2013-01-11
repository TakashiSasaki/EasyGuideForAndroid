package com.gmail.takashi316.sandbox;

import com.gmail.takashi316.easyguide.player.R;
import android.app.Activity;
import android.os.Bundle;

public class HelloAndroidActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hello_activity);
	}// onCreate
}// HelloAndroidActivity
