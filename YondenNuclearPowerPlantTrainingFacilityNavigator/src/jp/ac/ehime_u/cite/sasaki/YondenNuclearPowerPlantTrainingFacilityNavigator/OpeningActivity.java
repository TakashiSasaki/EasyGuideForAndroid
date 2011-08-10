package jp.ac.ehime_u.cite.sasaki.YondenNuclearPowerPlantTrainingFacilityNavigator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class OpeningActivity extends Activity {
	private static final String tag = "EasyGuide";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.opening);
		Organizations the_organizations = Organizations.GetTheOrganizations();
		Log.d(tag, "OpeningActivity#onCreate");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// タッチダウンとタッチアップの二回発生する。
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Log.d(tag, "OpeningActivity#onTouchEvent");
			Intent intent = new Intent(getApplicationContext(),
					MapActivity.class);
			startActivity(intent);
			// return super.onTouchEvent(event);
		}
		return true; //これ以降のイベントを処理しない
	}

}
