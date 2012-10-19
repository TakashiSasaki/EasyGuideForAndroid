package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import java.util.ArrayList;
import java.util.List;

import jp.ac.ehime_u.cite.sasaki.easyguide.content.ContentUnit;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Room;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;

public class WifiDetectorThread extends Thread {

	WifiManager wifiManager;
	List<ScanResult> apList;
	Context context;

	private class DetectionMap {
		public String macAddress;
		public Runnable callback;
	}

	final String simulatorRoomAp1Mac = "00:3a:9d:dd:37:a6";
	final int simulatorRoomAp1RssiThreashold = -85;
	int simulatorRoomAp1Rssi;
	final String simulatorRoomAp2Mac = "02:3a:9d:dd:37:a6";
	final int simulatorRoomAp2RssiThreashold = -85;
	int simulatorRoomAp2Rssi;

	final String showRoomAp1Mac = "1c:b1:7f:1e:9f:a6";
	final int showRoomAp1RssiThreashold = -90;
	int showRoomAp1Rssi;
	final String showRoomAp2Mac = "1e:b1:7f:1e:9f:a6";
	final int showRoomAp2RssiThreashold = -92;
	int showRoomAp2Rssi;

	final String trainingRoomAp1Mac = "1c:b1:7f:1e:9f:9e";
	final int trainingRoomAp1RssiThreashold = -80;
	int trainingRoomAp1Rssi;
	final String trainingRoomAp2Mac = "1e:b1:7f:1e:9f:9e";
	final int trainingRoomAp2RssiThreashold = -79;
	int trainingRoomAp2Rssi;

	private ArrayList<DetectionMap> detectionMapList;

	public boolean enabled = true;
	private ContentUnit rootContentUnit;
	private ContentUnit showRoomContentUnit;
	private ContentUnit trainingRoomContentUnit;
	private ContentUnit simulatorRoomContentUnit;

	private Handler handler;
	private UnifiedActivity unifiedActivity;

	public void addDetectionMap(String mac_address, Runnable callback) {
		DetectionMap detection_map = new DetectionMap();
		detection_map.macAddress = mac_address;
		detection_map.callback = callback;
		detectionMapList.add(detection_map);
	}

	public WifiDetectorThread(WifiManager wifi_manager,
			UnifiedActivity unifiedActivity, ContentUnit root_content_unit,
			Handler handler) {
		this.handler = handler;
		this.rootContentUnit = root_content_unit;
		this.showRoomContentUnit = this.rootContentUnit.getChild(1).getChild(1)
				.getChild(2).getChild(2);
		this.trainingRoomContentUnit = this.rootContentUnit.getChild(1)
				.getChild(1).getChild(1).getChild(1);
		this.simulatorRoomContentUnit = this.rootContentUnit.getChild(1)
				.getChild(1).getChild(2).getChild(1);
		this.wifiManager = wifi_manager;
		this.wifiManager.startScan();
	}// a constructor

	@Override
	public void run() {
		if (this.wifiManager == null)
			return;
		while (true) {
			if (this.enabled == false) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}// if
			this.apList = this.wifiManager.getScanResults();
			this.trainingRoomAp1Rssi = Integer.MIN_VALUE;
			this.trainingRoomAp2Rssi = Integer.MIN_VALUE;
			this.showRoomAp1Rssi = Integer.MIN_VALUE;
			this.showRoomAp2Rssi = Integer.MIN_VALUE;
			this.simulatorRoomAp1Rssi = Integer.MIN_VALUE;
			this.simulatorRoomAp2Rssi = Integer.MIN_VALUE;

			for (ScanResult sr : this.apList) {
				if (sr.BSSID.equals(this.trainingRoomAp1Mac))
					this.trainingRoomAp1Rssi = sr.level;
				else if (sr.BSSID.equals(this.trainingRoomAp2Mac))
					this.trainingRoomAp2Rssi = sr.level;
				else if (sr.BSSID.equals(this.showRoomAp1Mac))
					this.showRoomAp1Rssi = sr.level;
				else if (sr.BSSID.equals(this.showRoomAp2Mac))
					this.showRoomAp2Rssi = sr.level;
				else if (sr.BSSID.equals(this.simulatorRoomAp1Mac))
					this.simulatorRoomAp1Rssi = sr.level;
				else if (sr.BSSID.equals(this.simulatorRoomAp2Mac))
					this.simulatorRoomAp2Rssi = sr.level;

				Log.v(new Throwable(), sr.SSID + ", " + sr.BSSID + ", "
						+ sr.level);

				if (this.trainingRoomAp1Rssi > this.trainingRoomAp1RssiThreashold
						&& this.trainingRoomAp2Rssi > this.trainingRoomAp2RssiThreashold) {
					unifiedActivity.setContentUnit(trainingRoomContentUnit);
				} else if(this.showRoomAp1Rssi > this.showRoomAp1RssiThreashold && 
						this.trainingRoomAp2Rssi > this.trainingRoomAp2RssiThreashold){
					unifiedActivity.setContentUnit(showRoomContentUnit);
				} else if(this.simulatorRoomAp1Rssi >this.simulatorRoomAp1RssiThreashold &&
						this.simulatorRoomAp2Rssi>this.simulatorRoomAp2RssiThreashold){
					unifiedActivity.setContentUnit(simulatorRoomContentUnit);
				}
			}// for
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}// while
	}// run
}// WifiDetectorThread
