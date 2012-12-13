package com.gmail.takashi316.easyguide.player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.gmail.takashi316.easyguide.content.ContentUnit;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

class WifiThread extends Thread {
	Date lastUpdated = Calendar.getInstance().getTime();
	WifiMap wifiMap;
	WifiAps wifiAps = new WifiAps();
	private boolean booleanAutomaticTransition = false;
	// WifiAps savedWifiAps = new WifiAps();
	final int intervalMilliseconds = 5000;
	WifiManager wifiManager;
	boolean working = false;
	Context context;
	Class<?> activityClass;
	TextView textViewWifiAps;
	TextView textViewMatchedWifiApSet;

	public WifiThread(Context context, Class<?> activity_class,
			ContentUnit root_content_unit, TextView text_view_wifi_aps,
			TextView text_view_matched_wifi_ap_set) throws IOException {
		this.textViewWifiAps = text_view_wifi_aps;
		this.textViewMatchedWifiApSet = text_view_matched_wifi_ap_set;
		this.context = context;
		this.wifiMap = new WifiMap(root_content_unit);
		this.activityClass = activity_class;
		this.wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		this.wifiManager.setWifiEnabled(true);
		this.wifiManager.startScan();
	}// a constructor

	public void stopScan() {
		this.working = false;
	}// stopScan

	protected void finalize() throws Throwable {
		this.wifiManager.setWifiEnabled(false);
	};

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
		final ArrayList<Integer> matched_content_path = wifiMap
				.getMatchedConentPath(wifiAps);
		final WifiAps matched_wifi_aps = wifiMap.get(matched_content_path);

		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {

			@Override
			public void run() {
				textViewWifiAps.setText(wifiAps.toString());
				// WifiAps registered_wifi_aps = wifiMap.get(contentUnit
				// .getContentPath());
				if (matched_content_path != null) {
					textViewMatchedWifiApSet.setText(matched_wifi_aps
							.toString()
							+ "\n"
							+ matched_content_path.toString());
				} else {
					textViewMatchedWifiApSet.setText("マッチするポイントがありません");
				}
				// if (registered_wifi_aps == null) {
				// textViewSavedWifiAps.setText("no AP is registered");
				// textViewDistance.setText("");
				// return;
				// }// return
				// textViewSavedWifiAps.setText(registered_wifi_aps.toString());
				// double distance = registered_wifi_aps.getDistance(wifiAps);
				// int count = registered_wifi_aps.countMatchedAps(wifiAps);
				// textViewDistance.setText("distance = " + distance
				// + "\ncount = " + count + "\n"
				// + matched_wifi_aps.toString());
			}// run
		});

		this.sendIntent(matched_content_path);
	}// detectAp

	public void enableTransition(boolean b) {
		this.booleanAutomaticTransition = b;
	}

	public boolean isTransitionEnabled() {
		return this.booleanAutomaticTransition;
	}

	public void loadWifiMap() throws IOException {
		this.wifiMap.load();
	}

	public int saveWifiMap() {
		return this.wifiMap.save();
	}

	public void clearWifiMap() {
		this.wifiMap.clear();
	}

	public void learn(ContentUnit content_unit)
			throws CloneNotSupportedException {
		wifiMap.put(content_unit.getContentPath(), wifiAps.clone());
	}

	public void setLastUpdated() {
		lastUpdated = Calendar.getInstance().getTime();
	}

	public boolean sendIntent(ArrayList<Integer> content_path) {
		if (content_path == null)
			return false;
		long last = lastUpdated.getTime();
		long now = Calendar.getInstance().getTimeInMillis();
		if (now - last < 10 * 1000)
			return false;
		if (booleanAutomaticTransition == false)
			return false;
		final Intent intent = new Intent(context, activityClass);
		intent.putExtra("contentPath", content_path);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// Handler handler = new Handler(Looper.getMainLooper());
		// handler.post(new Runnable() {

		// @Override
		// public void run() {
		context.startActivity(intent);
		// }
		// });
		return true;
	}// sendIntent
}// WifiThread
