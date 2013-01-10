package com.gmail.takashi316.easyguide.player;


import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.TextView;


public class TocActivityTestCase extends
		ActivityInstrumentationTestCase2<TocActivity> {

	public TocActivityTestCase() {
		super(TocActivity.class);
	}// the constructor

	public void testDocumentationDomain() {
		assertEquals(textViewDocumentationDomain.getText(), "easyguide-app.blogspot.jp");
		this.toc_activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// OpeningActivityTestCase.this\.buttonResetUsers.requestFocus();
				// getInstrumentation().waitForIdle(null);
				//assertEquals(textViewDocumentationDomain.getText(), "easyguide-app.blogspot.jp");
			}// run
		});// runOnThread
	}// test1

	TocActivity toc_activity;
	Button buttonResetUsers;
	TextView textViewDocumentationDomain; 

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// TODO Auto-generated method stub
		this.toc_activity = getActivity();
		textViewDocumentationDomain =  (TextView)toc_activity.findViewById(R.id.textViewDocumentationDomain);
	}// setUp

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}
}// OpeningActivityTestCase

