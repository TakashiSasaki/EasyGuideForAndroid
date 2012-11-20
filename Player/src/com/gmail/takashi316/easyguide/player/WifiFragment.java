package com.gmail.takashi316.easyguide.player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gmail.takashi316.easyguide.content.ContentUnit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WifiFragment extends Fragment {
	private HorizontalScrollView horizontalScrollViewWifi;
	private LinearLayout linearLayoutWifi;
	private Context context;
	private Class<? extends Activity> activityClass;
	WifiManager wifiManager;
	Button buttonLocateAp, buttonDeleteAp, buttonDetectAp, buttonSaveAp;
	TextView textViewWifiAps, textViewSavedWifiAps, textViewDistance;
	File directory;
	WifiAps wifiAps = new WifiAps();
	WifiAps savedWifiAps = new WifiAps();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = activity.getApplicationContext();
		this.activityClass = activity.getClass();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.wifi_fragment, container);

		this.wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		this.wifiManager.startScan();

		this.textViewWifiAps = (TextView) v.findViewById(R.id.textViewWifiAps);
		this.textViewSavedWifiAps = (TextView) v
				.findViewById(R.id.textViewSavedWifiAps);
		this.textViewDistance = (TextView) v
				.findViewById(R.id.textViewDistance);
		this.buttonDeleteAp = (Button) v.findViewById(R.id.buttonDeleteAp);
		this.buttonDeleteAp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File file = new File(directory, "wifi.log");
				if (file.exists()) {
					file.delete();
				}
				try {
					savedWifiAps.load(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				textViewSavedWifiAps.setText(savedWifiAps.toString());
			}
		});

		this.buttonDetectAp = (Button) v.findViewById(R.id.buttonDetectAp);
		this.buttonDetectAp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				wifiManager.startScan();
				List<ScanResult> sr_list = wifiManager.getScanResults();
				wifiAps.setScanResults(sr_list);
				textViewWifiAps.setText(wifiAps.toString());
				double distance = savedWifiAps.getDistance(wifiAps);
				int count = savedWifiAps.countMatchedAps(wifiAps);
				textViewDistance.setText("distance = " + distance
						+ "\ncount = " + count);
			}// onClick
		});

		this.buttonLocateAp = (Button) v.findViewById(R.id.buttonLocateAp);
		this.buttonLocateAp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				detect();
			}
		});

		this.buttonSaveAp = (Button) v.findViewById(R.id.buttonSaveAp);
		this.buttonSaveAp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				File file = new File(directory, "wifi.log");
				wifiAps.save(file);
				try {
					savedWifiAps.load(file);
					textViewSavedWifiAps.setText(savedWifiAps.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		return v;
	}

	void update(File directory) {
		this.directory = directory;
		try {
			File file = new File(directory, "wifi.log");
			savedWifiAps.load(file);
			textViewSavedWifiAps.setText(savedWifiAps.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// update

	public void hide() {
		this.horizontalScrollViewWifi.setVisibility(View.GONE);
	}

	public void show() {
		this.horizontalScrollViewWifi.setVisibility(View.VISIBLE);
	}

	public void update(ContentUnit content_unit) {
	}

	HashMap<ArrayList<Integer>, WifiAps> m = new HashMap<ArrayList<Integer>, WifiAps>();

	public void scanSavedWifiAps(ContentUnit root) throws IOException {
		m.clear();
		for (ContentUnit content_unit : root.getChildren()) {
			WifiAps wifi_aps = new WifiAps();
			File file = new File(content_unit.getDirectory(), "wifi.log");
			wifi_aps.load(file);
			m.put(content_unit.getContentPath(), wifi_aps);
		}// for
	}// scanSavedWifiAps

	public void detect() {
		int candidate_count = 0;
		ArrayList<Integer> candidate_content_path = null;
		for (ArrayList<Integer> content_path : m.keySet()) {
			WifiAps wifi_aps = m.get(content_path);
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
}
