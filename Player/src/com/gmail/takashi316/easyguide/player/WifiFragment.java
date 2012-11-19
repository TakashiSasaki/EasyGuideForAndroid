package com.gmail.takashi316.easyguide.player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gmail.takashi316.easyguide.content.ContentUnit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
	Button buttonLocateAp, buttonDeleteAp, buttonDetectAp, buttonRecordAp;
	TextView textViewWifi;
	File directory;

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

		this.buttonDeleteAp = (Button) v.findViewById(R.id.buttonDeleteAp);
		this.buttonDetectAp = (Button) v.findViewById(R.id.buttonDetectAp);
		this.buttonLocateAp = (Button) v.findViewById(R.id.buttonLocateAp);
		this.buttonRecordAp = (Button) v.findViewById(R.id.buttonRecordAp);
		this.buttonRecordAp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				recordAp();
			}
		});
		this.textViewWifi = (TextView) v.findViewById(R.id.textViewWifi);

		return v;
	}

	void update(File directory) {
		this.directory = directory;
	}

	void recordAp() {
		File file = new File(directory, "wifi.log");
		// this.directory.setWritable(true);
		FileWriter file_writer;
		try {
			if (file.exists()) {
				file_writer = new FileWriter(file, true);
			} else {
				file_writer = new FileWriter(file);
			}
			List<ScanResult> sr_list = this.wifiManager.getScanResults();
			if (sr_list == null) {
				this.textViewWifi.setText("Wi-Fiアクセスポイントを検出できません");
				return;
			}
			StringBuilder sb = new StringBuilder();
			for (ScanResult sr : sr_list) {
				String ssid = sr.SSID;
				String bssid = sr.BSSID;
				int level = sr.level;
				String s = new Date().toString() + ", " + sr.SSID + ", "
						+ sr.BSSID + ", " + sr.level + "\n";
				sb.append(s);
			}
			file_writer.write(sb.toString());
			file_writer.flush();
			file_writer.close();
			this.textViewWifi.setText(sb.toString());
			return;
		} catch (IOException e) {
			this.textViewWifi.setText("検出もしくは記録できませんでした。");
			return;
		}// try
	} // recordAp

	public void hide() {
		this.horizontalScrollViewWifi.setVisibility(View.GONE);
	}

	public void show() {
		this.horizontalScrollViewWifi.setVisibility(View.VISIBLE);
	}

	public void update(ContentUnit content_unit) {
	}
}
