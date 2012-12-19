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
	WifiManager wifiManager;
	boolean scanEnabled = false;
	Context context;
	Class<?> activityClass;
	final int intervalMilliseconds = 5000;
	private int guardMilliseconds = 10000;
	private ArrayList<Integer> matchedContentPath;

	public TextView textViewLastApSet;
	public TextView textViewMatchedApSet;
	public TextView textViewMatchedContentPath;

	public WifiThread(Context context, Class<?> activity_class,
			ContentUnit root_content_unit) throws IOException {
		this.context = context;
		this.activityClass = activity_class;
		this.wifiMap = new WifiMap(root_content_unit);
		this.wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		this.wifiManager.setWifiEnabled(true);
		this.wifiManager.startScan();
	}// a constructor

	public void stopScan() {
		this.scanEnabled = false;
	}// stopScan

	protected void finalize() throws Throwable {
		this.wifiManager.setWifiEnabled(false);
	}// finalize

	@Override
	public void run() {
		super.run();
		scanEnabled = true;
		while (true) {
			wifiManager.startScan();
			try {
				Thread.sleep(intervalMilliseconds);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}// try
			if (!scanEnabled)
				return;
			wifiAps.setScanResults(wifiManager.getScanResults());
			matchedContentPath = wifiMap.getMatchedConentPath(wifiAps);
			updateViews();
		}// while
	}// run

	public void updateViews() {
		// final ArrayList<Integer> matched_content_path = wifiMap
		// .getMatchedConentPath(wifiAps);
		// final WifiAps matched_wifi_aps = wifiMap.get(matched_content_path);
		//
		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {

			@Override
			public void run() {
				if (textViewLastApSet != null)
					textViewLastApSet.setText(wifiAps.toString());
				// WifiAps registered_wifi_aps = wifiMap.get(contentUnit
				// .getContentPath());
				if (matchedContentPath != null) {
					if (textViewMatchedContentPath != null)
						textViewMatchedContentPath.setText(matchedContentPath
								.toString());
					if (textViewMatchedApSet != null) {
						textViewMatchedApSet.setText(wifiMap.get(
								matchedContentPath).toString());
					}
				} else {
					if (textViewMatchedContentPath != null)
						textViewMatchedContentPath.setText("N/A");
					if (textViewMatchedApSet != null)
						textViewMatchedApSet.setText("N/A");
				}
			}// run
		});

		this.sendIntent();
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
	}// setLastUpdated

	public void setGuardMilliseconds(int guard_milliseconds) {
		this.guardMilliseconds = guard_milliseconds;
	}// setGuardMillisecondsd

	public boolean sendIntent() {
		if (matchedContentPath == null)
			return false;
		long last = lastUpdated.getTime();
		long now = Calendar.getInstance().getTimeInMillis();
		if (now - last < guardMilliseconds)
			return false;
		if (booleanAutomaticTransition == false)
			return false;
		final Intent intent = new Intent(context, activityClass);
		intent.putExtra("contentPath", matchedContentPath);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		return true;
	}// sendIntent
}// WifiThread
