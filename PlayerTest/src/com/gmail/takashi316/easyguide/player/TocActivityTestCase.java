package com.gmail.takashi316.easyguide.player;


import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.TextView;


public class TocActivityTestCase extends
		ActivityInstrumentationTestCase2<TocActivity> {

	TocActivity toc_activity;
	Button buttonResetUsers;
	TextView textViewDocumentationDomain;
	TextView textViewExistingDocumentationDirectory;
	TextView textViewDocumentationDirectoryCount;
	TextView textViewDocumentationFileCount;

	public TocActivityTestCase() {
		super(TocActivity.class);
	}// the constructor

	public void testDocumentationDomain() {
		assertEquals("easyguide-app.blogspot.jp",textViewDocumentationDomain.getText());
		if(textViewExistingDocumentationDirectory.getText().length()==0){
			assertEquals("/mnt/sdcard/EASYGUIDE/easyguide-app.blogspot.jp", textViewExistingDocumentationDirectory.getText());	
		}//if
		assertEquals("1", textViewDocumentationDirectoryCount.getText());
		
		this.toc_activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// OpeningActivityTestCase.this\.buttonResetUsers.requestFocus();
				// getInstrumentation().waitForIdle(null);
				//assertEquals(textViewDocumentationDomain.getText(), "easyguide-app.blogspot.jp");
			}// run
		});// runOnThread
	}// testDocumentationDomain

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// TODO Auto-generated method stub
		this.toc_activity = getActivity();
		textViewExistingDocumentationDirectory =  (TextView)toc_activity.findViewById(R.id.textViewExistingDocumentationDomainDirectory);
		textViewDocumentationDomain =  (TextView)toc_activity.findViewById(R.id.textViewDocumentationDomain);
		textViewDocumentationDirectoryCount = (TextView)toc_activity.findViewById(R.id.textViewDocumentationDirectoryCount);
		textViewDocumentationFileCount = (TextView)toc_activity.findViewById(R.id.textViewDocumentationFileCount);
	}// setUp

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}
}// OpeningActivityTestCase

