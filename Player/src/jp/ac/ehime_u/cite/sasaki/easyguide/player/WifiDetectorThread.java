package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import java.util.ArrayList;
import java.util.List;

import jp.ac.ehime_u.cite.sasaki.easyguide.model.Room;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiDetectorThread extends Thread {

	WifiManager wifiManager;
	List<ScanResult> apList;
	Context context;

	private class DetectionMap {
		public String macAddress;
		public Runnable callback;
	}

	private ArrayList<DetectionMap> detectionMapList;

	public boolean enabled = true;
	private Runnable callback;

	static private WifiDetectorThread theInstance;

	static public WifiDetectorThread getInstance(Context context) {
		if (theInstance != null && theInstance.isAlive()) {
			return theInstance;
		}
		theInstance = new WifiDetectorThread(null, context);
		return theInstance;
	}// getInstance

	public void addDetectionMap(String mac_address, Runnable callback) {
		DetectionMap detection_map = new DetectionMap();
		detection_map.macAddress = mac_address;
		detection_map.callback = callback;
		detectionMapList.add(detection_map);
	}

	public WifiDetectorThread(Runnable callback, Context context) {
		this.context = context;
		this.callback = callback;
		this.wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
	}

	@Override
	public void start() {
		if (this.isAlive())
			return;
		super.start();
	}// start

	@Override
	public void run() {
		while (this.enabled) {
			if (this.wifiManager == null)
				return;
			this.wifiManager.startScan();
			this.apList = this.wifiManager.getScanResults();
			for (ScanResult sr : this.apList) {
				Log.v(new Throwable(), sr.SSID + ", " + sr.BSSID + ", "
						+ sr.level);
				if (sr.BSSID.equals("00:3a:9d:dd:a1:9a") && sr.level > -45) {
					Intent intent = new Intent();
					intent.setClassName(context, RoomActivity.class.getName());
					intent.putExtra("organizationIndex", 1);
					intent.putExtra("facilityIndex", 1);
					intent.putExtra("buildingIndex", -1);
					intent.putExtra("floorIndex", 2);
					intent.putExtra("roomIndex", 1);
					context.startActivity(intent);
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (sr.BSSID.equals("00:3a:9d:dd:37:a6") && sr.level > -60) {
					Intent intent = new Intent();
					intent.setClassName(context,
							EquipmentActivity.class.getName());
					intent.putExtra("organizationIndex", 1);
					intent.putExtra("facilityIndex", 1);
					intent.putExtra("buildingIndex", -1);
					intent.putExtra("floorIndex", 1);
					intent.putExtra("roomIndex", 1);
					intent.putExtra("equipmentIndex", 1);
					context.startActivity(intent);
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (sr.BSSID.equals("1c:b1:7f:1e:9f:9e") && sr.level > -70) {
					Intent intent = new Intent();
					intent.setClassName(context,
							EquipmentActivity.class.getName());
					intent.putExtra("organizationIndex", 1);
					intent.putExtra("facilityIndex", 1);
					intent.putExtra("buildingIndex", -1);
					intent.putExtra("floorIndex", 1);
					intent.putExtra("roomIndex", 1);
					intent.putExtra("equipmentIndex", 3);
					context.startActivity(intent);
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}// for
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}// while
	}// run

}// WifiDetectorThread
