package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiSenseAndShowDialog {

	WifiManager wifiManager;
	AlertDialog.Builder alertDialog;
	File directory;

	public WifiSenseAndShowDialog(Activity activity, WifiManager wifi_manager,
			File directory) {
		this.wifiManager = wifi_manager;
		this.alertDialog = new AlertDialog.Builder(activity);
		this.directory = directory;
		this.alertDialog.setTitle("検出されたWi-Fiアクセスポイント");
		this.alertDialog.setMessage("まだ検出されていません");
		this.alertDialog.setPositiveButton("OK", null);
	}

	public void sense() {
		File file = new File(this.directory, "wifi.log");
		this.directory.setWritable(true);
		FileWriter file_writer;
		try {
			if (file.exists()) {
				file_writer = new FileWriter(file, true);
			} else {
				file_writer = new FileWriter(file);
			}
			List<ScanResult> sr_list = this.wifiManager.getScanResults();
			if (sr_list == null) {
				this.alertDialog.setMessage("Wi-Fiアクセスポイントを検出できません");
				this.alertDialog.show();
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
			this.alertDialog.setMessage(sb.toString());
			this.alertDialog.show();
			return;
		} catch (IOException e) {
			this.alertDialog.setMessage("検出もしくは記録できませんでした。");
			this.alertDialog.show();
			return;
		}// try
	}// sense
}// WifiSenseAndShowDialog
