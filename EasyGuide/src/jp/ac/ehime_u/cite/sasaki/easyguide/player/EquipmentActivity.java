package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import jp.ac.ehime_u.cite.sasaki.easyguide.model.Equipment;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organizations;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Panel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class EquipmentActivity extends Activity {
	protected static final float SWIPE_MAX_OFF_PATH = 200;
	protected static final float SWIPE_MIN_DISTANCE = 100;
	protected static final float SWIPE_THRESHOLD_VELOCITY = 10;
	private GestureDetector mGestureDetector;

	Equipment equipment;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.equipment);
		mGestureDetector = new GestureDetector(this, mOnGestureListener);

		this.equipment = Organizations.GetTheOrganizations()
				.GetOrganization("assets").GetFacility("facility_a")
				.GetBuilding("building_a").GetFloor("floor_a")
				.GetRoom("room_a").GetEquipment("equipment_a");

		Panel panel_a = equipment.GetPanel("panel_a");
		// // サンプルコンテンツのロード
		WebView web_view = (WebView) findViewById(R.id.webView1);
		// File storage_directory = Environment.getExternalStorageDirectory();
		// File root_directory = new File(storage_directory, "EASYGUIDE");
		// File sample_directory = new File(root_directory, "sample");
		// File sample_index = new File(sample_directory, "sample.png");
		// web_view.loadUrl("file://" + sample_index.getAbsolutePath());
		Log.v(this.getClass().getSimpleName(), "URL for panel_a is "
				+ "file://" + panel_a.getPanelDirectory() + "/index.html");
		WebSettings web_settings = web_view.getSettings();
		web_settings.setDefaultTextEncodingName("utf-8");
		web_view.loadUrl("file://" + panel_a.getPanelDirectory()
				+ "/index.html");
	}// onCreate

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// return super.onTouchEvent(event);
		Log.d("onTouchEvent", "");
		return mGestureDetector.onTouchEvent(event);
	}// onTouchEvent

	private final SimpleOnGestureListener mOnGestureListener = new SimpleOnGestureListener() {
		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2,
				float velocityX, float velocityY) {

			Log.d("onFling", "X1=" + event1.getX() + ",Y1=" + event1.getY()
					+ ",X2=" + event2.getX() + ",Y2=" + event2.getY());
			try {
				if (event1.getX() - event2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					// 開始位置から終了位置の移動距離が指定値より大きい
					// X軸の移動速度が指定値より大きい
					Intent intent = new Intent(getApplicationContext(),
							BuildingActivity.class);
					startActivity(intent);
				} else if (event2.getX() - event1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					// 終了位置から開始位置の移動距離が指定値より大きい
					// X軸の移動速度が指定値より大きい
					Intent intent = new Intent(getApplicationContext(),
							BuildingActivity.class);
					startActivity(intent);
				}

			} catch (Exception e) {
				// setWallpaper(data);
				// nothing
			}
			return false;
		}// onFling
	};// SimpleOnGestureListener

}// EquipmentActivity
