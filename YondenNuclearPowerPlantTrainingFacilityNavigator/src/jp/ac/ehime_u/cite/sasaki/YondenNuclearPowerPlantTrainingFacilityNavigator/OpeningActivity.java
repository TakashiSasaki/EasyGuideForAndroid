package jp.ac.ehime_u.cite.sasaki.YondenNuclearPowerPlantTrainingFacilityNavigator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class OpeningActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.opening);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Intent intent = new Intent(getApplicationContext(), MapActivity.class);
		startActivity(intent);
		return true;
	}

}
