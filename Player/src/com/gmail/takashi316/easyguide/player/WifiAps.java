package com.gmail.takashi316.easyguide.player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.net.wifi.ScanResult;
import android.util.FloatMath;

public class WifiAps {
	HashMap<String, Float> bssidToLevel = new HashMap<String, Float>();

	public float getDistance(WifiAps their_wifi_aps) {
		float distance_squared = 0.0f;
		for (String bssid : bssidToLevel.keySet()) {
			Float my_level = this.bssidToLevel.get(bssid);
			Float their_level = their_wifi_aps.bssidToLevel.get(bssid);
			if (their_level == null)
				continue;
			distance_squared += (their_level - my_level)
					* (their_level - my_level);
		}
		return FloatMath.sqrt(distance_squared);
	}// getDistance

	public int countMatchedAps(WifiAps their_wifi_aps) {
		int count = 0;
		for (String bssid : bssidToLevel.keySet()) {
			Float my_level = this.bssidToLevel.get(bssid);
			Float their_level = their_wifi_aps.bssidToLevel.get(bssid);
			if (their_level == null)
				continue;
			count += 1;
		}
		return count;
	}// countMatchedAps

	public void setScanResults(List<ScanResult> scan_results) {
		this.bssidToLevel.clear();
		if (scan_results == null)
			return;
		for (ScanResult scan_result : scan_results) {
			String bssid = scan_result.BSSID;
			int level = scan_result.level;
			bssidToLevel.put(bssid, Float.valueOf(level));
		}// for
	}// setScanResults

	public String toString() {
		StringBuilder string_builder = new StringBuilder();
		for (String bssid : this.bssidToLevel.keySet()) {
			string_builder.append(bssid);
			Float level = bssidToLevel.get(bssid);
			string_builder.append(" " + level + "\n");
		}
		return string_builder.toString();
	}// toString

	public void save(File file) {
		// File file = new File(directory, "wifi.log");
		// this.directory.setWritable(true);
		FileWriter file_writer;
		try {
			file_writer = new FileWriter(file);
			file_writer.write(this.toString());
			file_writer.flush();
			file_writer.close();
			// this.textViewWifi.setText(sb.toString());
			return;
		} catch (IOException e) {
			return;
		}// try
	}// save

	public void load(File file) throws IOException {
		this.bssidToLevel.clear();
		Pattern pattern = Pattern.compile("([0-9a-f:]+)[ ]+([-0-9.]+)");
		FileReader file_reader = new FileReader(file);
		BufferedReader buffered_reader = new BufferedReader(file_reader);
		for (;;) {
			String line = buffered_reader.readLine();
			if (line == null)
				break;
			Matcher matcher = pattern.matcher(line);
			if (!matcher.matches())
				continue;
			String bssid = matcher.group(1);
			String level = matcher.group(2);
			bssidToLevel.put(bssid, Float.valueOf(level));
		}// for
	} // load
}// WifiAps
