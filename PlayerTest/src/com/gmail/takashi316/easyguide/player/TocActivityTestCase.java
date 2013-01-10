package com.gmail.takashi316.easyguide.player;


import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;


public class TocActivityTestCase extends
		ActivityInstrumentationTestCase2<TocActivity> {

	public TocActivityTestCase() {
		super(TocActivity.class);
	}// the constructor

	public void test1() {
		this.activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// OpeningActivityTestCase.this.buttonResetUsers.requestFocus();
				// getInstrumentation().waitForIdle(null);
			}// run
		});// runOnThread
	}// test1

	Activity activity;
	Button buttonResetUsers;

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		this.activity = getActivity();
		super.setUp();
	}// setUp

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}
}// OpeningActivityTestCase

