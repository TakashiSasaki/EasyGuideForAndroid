package jp.ac.ehime_u.cite.sasaki.YondenNuclearPowerPlantTrainingFacilityNavigator;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

public class MapActivity extends Activity {
	private Activity mGestureDetector;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
	}
	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
    }
}