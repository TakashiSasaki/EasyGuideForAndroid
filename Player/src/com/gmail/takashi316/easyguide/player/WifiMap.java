package com.gmail.takashi316.easyguide.player;

import java.util.ArrayList;
import java.util.HashMap;

public class WifiMap extends HashMap<ArrayList<Integer>, WifiAps> {

	public ArrayList<Integer> getMatchedConentPath(WifiAps detected_wifi_aps) {
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
	
	public void load(){
		//TODO: load whole map
	}
	
	public void save(){
		//TODO: save whole map
	}
	
}// WifiMap
