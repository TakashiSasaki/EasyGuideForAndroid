package jp.ac.ehime_u.cite.sasaki.easyguide.detector;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class DetectorActivity extends Activity {
	/** Called when the activity is first created. */

	class Ap {
		public Ap(String ssid, int dbm) {
			this.ssid = ssid;
			this.dbm = dbm;
		}// a constructor

		public String ssid;
		public int dbm;
	}// Ap

	private ArrayList<Ap> aps = new ArrayList<Ap>();

	private Button buttonDetect;
	private EditText editTextOrganizationIndex;
	private EditText editTextFacilityIndex;
	private EditText editTextBuildingIndex;
	private EditText editTextFloorIndex;
	private EditText editTextRoomIndex;
	private EditText editTextEquipmentIndex;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.editTextBuildingIndex = (EditText) findViewById(R.id.editTextBuildingIndex);
		this.editTextEquipmentIndex = (EditText) findViewById(R.id.editTextEquipmentIndex);
		this.editTextFacilityIndex = (EditText) findViewById(R.id.editTextFacilityIndex);
		this.editTextFloorIndex = (EditText) findViewById(R.id.editTextFloorIndex);
		this.editTextOrganizationIndex = (EditText) findViewById(R.id.editTextOrganizationIndex);
		this.editTextRoomIndex = (EditText) findViewById(R.id.editTextRoomIndex);

		this.buttonDetect = (Button) findViewById(R.id.button1);
		this.buttonDetect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int organization_index = Integer
						.parseInt(editTextOrganizationIndex.getText()
								.toString());
				int facility_index = Integer.parseInt(editTextFacilityIndex
						.getText().toString());
				int building_index = Integer.parseInt(editTextBuildingIndex
						.getText().toString());
				int floor_index = Integer.parseInt(editTextFloorIndex.getText()
						.toString());
				int room_index = Integer.parseInt(editTextRoomIndex.getText()
						.toString());
				int equipment_index = Integer.parseInt(editTextEquipmentIndex
						.getText().toString());
			}// onClick
		});

	}// onCreate

	@Override
	public void onStart() {
		super.onStart();
	}// onStart

	void getSsids() {
		final WifiManager manager = (WifiManager) getSystemService(WIFI_SERVICE);
		if (manager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
			List<ScanResult> results = manager.getScanResults();
			// final String[] items = new String[results.size()];
			for (int i = 0; i < results.size(); ++i) {
				this.aps.add(new Ap(results.get(i).SSID, results.get(i).level));
			}// for
		}// if
	}// getSsids()

	int getDbm(String ssid) {
		for (Ap ap : this.aps) {
			if (ssid.equals(ap.ssid)) {
				return ap.dbm;
			}
		}
		throw new IndexOutOfBoundsException("SSID " + ssid
				+ " was not detected");
	}// getDbm

}// DetectorActivity
