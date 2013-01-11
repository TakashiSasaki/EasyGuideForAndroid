package com.gmail.takashi316.sandbox.test;

import com.gmail.takashi316.sandbox.MapsV2Activity;

import android.support.v4.app.FragmentActivity;
import android.test.ActivityInstrumentationTestCase2;

public class MapsV2ActivityTestCase extends
		ActivityInstrumentationTestCase2<MapsV2Activity> {

	FragmentActivity maps_v2_activity;

	public MapsV2ActivityTestCase() {
		super(MapsV2Activity.class);
	}// the constructor

	public void testDryRun() {

		this.maps_v2_activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// OpeningActivityTestCase.this\.buttonResetUsers.requestFocus();
				// getInstrumentation().waitForIdle(null);
				// assertEquals(textViewDocumentationDomain.getText(),
				// "easyguide-app.blogspot.jp");
			}// run
		});// runOnThread
	}// testDocumentationDomain

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// TODO Auto-generated method stub
		this.maps_v2_activity = getActivity();
	}// setUp

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}
}// MapsV2ActivityTestCase

