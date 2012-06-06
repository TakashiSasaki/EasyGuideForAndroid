package jp.ac.ehime_u.cite.sasaki.easyguide.downloader;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiListDialog extends AlertDialog.Builder {
	private Context context;

	public WifiListDialog(Context context_) {
		super(context_);
		this.context = context_;
		final WifiManager manager = (WifiManager) this.context
				.getSystemService(Context.WIFI_SERVICE);
		if (manager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
			List<ScanResult> results = manager.getScanResults();
			final String[] items = new String[results.size()];
			for (int i = 0; i < results.size(); ++i) {
				items[i] = results.get(i).SSID;
			}// for
			if (items.length > 0) {
				setItems(items, null);
			} else {
				setItems(new String[] { "なし" }, null);
			}// if
				// final ArrayAdapter<String> adapter = new
				// ArrayAdapter<String>(this,
				// android.R.layout.simple_list_item_1, items);
				// setListAdapter(adapter);
		}
		setTitle("Wi-Fiアクセスポイント一覧");
	}// a constructor
}// WifiListDialog
