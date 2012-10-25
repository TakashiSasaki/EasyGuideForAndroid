package com.gmail.takashi316.sandbox.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.TextView;
import jp.ac.ehime_u.cite.sasaki.easyguide.player.R;

import com.gmail.takashi316.sandbox.HelloAndroidActivity;

/**
 * @author Takashi SASAKI {@link http://twitter.com/TakashiSasaki}
 */
public class HelloAndroidTestCase extends
		ActivityInstrumentationTestCase2<HelloAndroidActivity> {

	public HelloAndroidTestCase() {
		super(HelloAndroidActivity.class);
	}// UsersActivityTestCase

	public void testTextViewHello() {
		this.activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				getInstrumentation().waitForIdle(null);
			}
		});// runOnUiThread
		Log.v("testTextViewHello", (String) this.textView.getText());
		assertEquals(this.textView.getText(), "Hello World, HelloAndroidActivity!");
	}// testTextViewHello

	Activity activity;
	TextView textView;

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		this.activity = getActivity();
		this.textView = (TextView) this.activity
				.findViewById(R.id.textViewHello);
		super.setUp();
	}// setUp

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}// tearDown
}// UsersActivityTestCase

