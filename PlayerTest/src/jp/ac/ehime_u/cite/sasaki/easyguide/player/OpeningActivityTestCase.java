package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import jp.ac.ehime_u.cite.sasaki.easyguide.player.OpeningActivity;

public class OpeningActivityTestCase extends
		ActivityInstrumentationTestCase2<OpeningActivity> {

	public OpeningActivityTestCase(String pkg,
			Class<OpeningActivity> activityClass) {
		super(pkg, activityClass);
		// TODO Auto-generated constructor stub
	}

	public OpeningActivityTestCase() {
		super("jp.ac.ehime_u.cite.sasaki.easyguide.player.OpeningActivit",
				OpeningActivity.class);
	}

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

