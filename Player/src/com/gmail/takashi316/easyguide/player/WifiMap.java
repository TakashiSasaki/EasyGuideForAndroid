package com.gmail.takashi316.easyguide.player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.gmail.takashi316.easyguide.content.ContentUnit;

public class WifiMap extends HashMap<ArrayList<Integer>, WifiAps> {

	ContentUnit rootContentUnit;

	public WifiMap(ContentUnit root_content_unit) throws IOException {
		this.rootContentUnit = root_content_unit;
		this.load();
	}

	public WifiMap() {
		rootContentUnit = null;
	}

	public ArrayList<Integer> getMatchedConentPath(WifiAps detected_wifi_aps) {
		WifiMap wifi_map = getCandidates(detected_wifi_aps);
		float min_distance = Float.MAX_VALUE;
		ArrayList<Integer> matched_content_path = null;
		for (ArrayList<Integer> content_path : wifi_map.keySet()) {
			WifiAps wifi_aps = wifi_map.get(content_path);
			float distance = wifi_aps.getDistance(detected_wifi_aps);
			if (distance >= min_distance)
				continue;
			min_distance = distance;
			matched_content_path = content_path;
		}// for
		return matched_content_path;
	}// getMatchedContentPath

	public WifiMap getCandidates(WifiAps detected_wifi_aps) {
		int max_count = 0;
		WifiMap wifi_map = new WifiMap();
		for (ArrayList<Integer> content_path : this.keySet()) {
			WifiAps wifi_aps = this.get(content_path);
			int count = wifi_aps.countMatchedAps(detected_wifi_aps);
			if (count == max_count) {
				wifi_map.put(content_path, wifi_aps);
				continue;
			}// if
			if (count > max_count) {
				max_count = count;
				wifi_map.clear();
				wifi_map.put(content_path, wifi_aps);
			}// if
		}// for
		return wifi_map;
	}// getCandidates

	public int save() {
		return _saveWifiMap(rootContentUnit, 0);
		// TODO: save whole map
	}

	private int _saveWifiMap(ContentUnit content_unit, int count) {
		File file = new File(content_unit.getDirectory(), "wifi.log");
		WifiAps wifi_aps = this.get(content_unit.getContentPath());
		if (wifi_aps == null) {
			if (file.exists())
				file.delete();
		} else {
			wifi_aps.save(file);
			++count;
		}// if
		for (ContentUnit cu : content_unit.getChildren()) {
			count = _saveWifiMap(cu, count);
		}// for
		return count;
	}// _saveWifiMap

	public void load() throws IOException {
		this.clear();
		_loadWifiMap(rootContentUnit);
	}// scanSavedWifiAps

	private void _loadWifiMap(ContentUnit content_unit) throws IOException {
		WifiAps wifi_aps = new WifiAps();
		try {
			File file = new File(content_unit.getDirectory(), "wifi.log");
			wifi_aps.load(file);
		} catch (IOException e) {
			// pass
		}
		this.put(content_unit.getContentPath(), wifi_aps);
		for (ContentUnit cu : content_unit.getChildren()) {
			_loadWifiMap(cu);
		}// for
	}// _loadWifiMap

}// WifiMap
