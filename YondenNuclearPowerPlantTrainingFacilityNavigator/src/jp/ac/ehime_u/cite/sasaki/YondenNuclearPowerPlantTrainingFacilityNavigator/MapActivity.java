package jp.ac.ehime_u.cite.sasaki.YondenNuclearPowerPlantTrainingFacilityNavigator;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.app.AlertDialog;

public class MapActivity extends Activity {
	protected static final float SWIPE_MAX_OFF_PATH = 200;
	protected static final float SWIPE_MIN_DISTANCE = 100;
	protected static final float SWIPE_THRESHOLD_VELOCITY = 10;
	private GestureDetector mGestureDetector;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		mGestureDetector = new GestureDetector(this, mOnGestureListener);

		((ImageView) findViewById(R.id.imageViewBuilding))
				.setOnTouchListener(new OnTouchListener() {

					public boolean onTouch(View v, MotionEvent event) {
						AlertDialog.Builder alert_dialog_builder = new AlertDialog.Builder(
								MapActivity.this);
						alert_dialog_builder.setTitle("ダイアログボックスのタイトル");
						alert_dialog_builder.setPositiveButton("いえす",
								new OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

									}
								});
						alert_dialog_builder.setNegativeButton("のお",
								new OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

									}
								});
						alert_dialog_builder.setNeutralButton("しらん",
								new OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

									}
								});
						alert_dialog_builder.setCancelable(true);
						alert_dialog_builder.show();
						return false;
					}
				});

		((ImageView) findViewById(R.id.imageViewFloor))
				.setOnTouchListener(new OnTouchListener() {

					public boolean onTouch(View v, MotionEvent event) {
						List<FloorContent> dataList = new ArrayList<FloorContent>();
						dataList.add(new FloorContent(MapActivity.this));
						dataList.add(new FloorContent(MapActivity.this));
						dataList.add(new FloorContent(MapActivity.this));

						FloorArrayAdapter floor_list_adapter = new FloorArrayAdapter(
								MapActivity.this, R.layout.floor_list_item,
								dataList);
						ListView list_view = (ListView) MapActivity.this
								.findViewById(R.id.listViewFloor);
						assert (list_view != null);
						list_view.setAdapter(floor_list_adapter);

						// コンテキストからインフレータを取得
						LayoutInflater layout_infrater = LayoutInflater
								.from(MapActivity.this);
						// レイアウトをインフレとしてビューを取得
						View view = layout_infrater.inflate(
								R.layout.floor_list, null);

						new AlertDialog.Builder(MapActivity.this).setView(view)
								.show();

						return false;
					}
				});

	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater menu_infrater = getMenuInflater();
		menu_infrater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menu_item) {
		super.onOptionsItemSelected(menu_item);
		switch (menu_item.getItemId()) {
		case R.id.itemDebug:
			Intent intent = new Intent(getApplicationContext(),
					DebugActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// return super.onTouchEvent(event);
		Log.d("onTouchEvent", "");
		return mGestureDetector.onTouchEvent(event);
	}

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
							MediaActivity.class);
					startActivity(intent);
				} else if (event2.getX() - event1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					// 終了位置から開始位置の移動距離が指定値より大きい
					// X軸の移動速度が指定値より大きい
					Intent intent = new Intent(getApplicationContext(),
							MediaActivity.class);
					startActivity(intent);
				}

			} catch (Exception e) {
				// nothing
			}
			return false;
		}
	};
}