package com.gmail.takashi316.easyguide.player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import com.gmail.takashi316.easyguide.content.ContentUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
//import android.widget.HorizontalScrollView;
//import android.widget.LinearLayout;
import android.widget.TextView;

public class WifiFragment extends Fragment {
	// private HorizontalScrollView horizontalScrollViewWifi;
	// private LinearLayout linearLayoutWifi;
	private Context context;
	private Class<? extends Activity> activityClass;
	Button buttonLoadAllAps, buttonSaveApFile, buttonDeleteApFile,
			buttonDetectAp, buttonRegisterApTemporaliry, buttonLocateAp;
	TextView textViewWifiAps, textViewSavedWifiAps, textViewDistance;
	// File directory;
	WifiAps wifiAps = new WifiAps();
	WifiAps savedWifiAps = new WifiAps();
	HashMap<ArrayList<Integer>, WifiAps> wifiMap = new HashMap<ArrayList<Integer>, WifiAps>();
	ContentUnit contentUnit;
	WifiThread wifiThread;

	class WifiThread extends Thread {
		final int intervalMilliseconds = 5000;
		WifiManager wifiManager;
		boolean working = false;

		public WifiThread() {
			this.wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			this.wifiManager.startScan();
		}// a constructor

		public void stopScan() {
			this.working = false;
		}// stopScan

		@Override
		public void run() {
			super.run();
			working = true;
			while (true) {
				wifiManager.startScan();
				try {
					Thread.sleep(intervalMilliseconds);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}// try
				if (!working)
					return;
				detectAp();
			}// while
		}// run

		public void detectAp() {
			wifiAps.setScanResults(wifiManager.getScanResults());
			textViewWifiAps.setText(wifiAps.toString());
			double distance = savedWifiAps.getDistance(wifiAps);
			int count = savedWifiAps.countMatchedAps(wifiAps);
			textViewDistance.setText("distance = " + distance + "\ncount = "
					+ count);
		}// detectAp
	}// WifiThread

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = activity.getApplicationContext();
		this.activityClass = activity.getClass();

	}// onAttach

	@Override
	public void onStart() {
		super.onStart();
		wifiThread = new WifiThread();
		wifiThread.run();
	}// onStart

	@Override
	public void onStop() {
		super.onStop();
		wifiThread.stop();
		try {
			wifiThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}// try
	}// onStop

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.wifi_fragment, container);

		this.textViewWifiAps = (TextView) v.findViewById(R.id.textViewWifiAps);
		this.textViewSavedWifiAps = (TextView) v
				.findViewById(R.id.textViewSavedWifiAps);
		this.textViewDistance = (TextView) v
				.findViewById(R.id.textViewDistance);
		this.buttonDeleteApFile = (Button) v
				.findViewById(R.id.buttonDeleteApFile);
		this.buttonDeleteApFile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final File file = new File(contentUnit.getDirectory(),
						"wifi.log");
				if (file.exists()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							context);
					builder.setCancelable(true);
					builder.setTitle("Wi-Fiアクセスポイントの記録"
							+ file.getAbsolutePath() + " を削除しますか？");
					builder.setPositiveButton("削除する",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									file.delete();
								}// onClick
							});
					builder.show();
				}// if
				try {
					savedWifiAps.load(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// try
				textViewSavedWifiAps.setText(savedWifiAps.toString());
			}// onClick
		});

		this.buttonDetectAp = (Button) v.findViewById(R.id.buttonDetectAp);
		this.buttonDetectAp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				wifiThread.detectAp();
			}// onClick
		});

		this.buttonLocateAp = (Button) v.findViewById(R.id.buttonLocateAp);
		this.buttonLocateAp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				detect();
			}
		});

		this.buttonSaveApFile = (Button) v.findViewById(R.id.buttonSaveApFile);
		this.buttonSaveApFile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final File file = new File(contentUnit.getDirectory(),
						"wifi.log");
				new AlertDialog.Builder(context)
						.setCancelable(true)
						.setTitle("Wi-Fiアクセスポイント情報を保存しますか？")
						.setPositiveButton("保存する",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										wifiAps.save(file);
									}// onClick
								}).show();
				try {
					savedWifiAps.load(file);
					textViewSavedWifiAps.setText(savedWifiAps.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}// try

			}// onClick
		});

		buttonRegisterApTemporaliry = (Button) v
				.findViewById(R.id.buttonRegisterApTemporaliry);
		buttonRegisterApTemporaliry.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					wifiMap.put(contentUnit.getContentPath(), wifiAps.clone());
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
					return;
				}// try
			}// onClick
		});

		return v;
	}

	void update(ContentUnit content_unit) {
		this.contentUnit = content_unit;
		// this.directory = directory;
		try {
			File file = new File(contentUnit.getDirectory(), "wifi.log");
			savedWifiAps.load(file);
			textViewSavedWifiAps.setText(savedWifiAps.toString());
			wifiMap.put(contentUnit.getContentPath(), savedWifiAps);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// try
	}// update

	// public void hide() {
	// this.horizontalScrollViewWifi.setVisibility(View.GONE);
	// }// hide
	//
	// public void show() {
	// this.horizontalScrollViewWifi.setVisibility(View.VISIBLE);
	// }// show
	//
	public void loadWifiMap(ContentUnit content_unit) throws IOException {
		wifiMap.clear();
		_loadWifiMap(content_unit);
	}// scanSavedWifiAps

	private void _loadWifiMap(ContentUnit content_unit) throws IOException {
		WifiAps wifi_aps = new WifiAps();
		File file = new File(content_unit.getDirectory(), "wifi.log");
		wifi_aps.load(file);
		wifiMap.put(content_unit.getContentPath(), wifi_aps);
		for (ContentUnit cu : content_unit.getChildren()) {
			_loadWifiMap(cu);
		}// for
	}// _loadWifiMap

	public void detect() {
		int candidate_count = 0;
		ArrayList<Integer> candidate_content_path = null;
		for (ArrayList<Integer> content_path : wifiMap.keySet()) {
			WifiAps wifi_aps = wifiMap.get(content_path);
			int count = wifi_aps.countMatchedAps(this.wifiAps);
			if (count > candidate_count) {
				candidate_count = count;
				candidate_content_path = content_path;
			}
		}// for
		if (candidate_content_path != null) {
			Intent intent = new Intent(context, activityClass);
			intent.putIntegerArrayListExtra("contentPath",
					candidate_content_path);
			startActivity(intent);
		}// if
	}// detect
}// WifiFragment
