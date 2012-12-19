package com.gmail.takashi316.easyguide.player;

import java.io.File;
import java.io.IOException;
import com.gmail.takashi316.easyguide.content.ContentUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WifiFragment extends Fragment {
	// private Context context;
	// private Class<? extends Activity> activityClass;
	TextView textViewWifiAps, textViewSavedWifiAps, textViewDistance;
	ContentUnit contentUnit;
	WifiThread wifiThread;
	Activity activity;
	// private boolean booleanAutomaticTransition;
	private View view;
	private Button buttonLoadWifiMap;
	private Button buttonSaveWifiMap;
	private Button buttonClearWifiMap;
	private Button buttonRegister;
	private Button buttonToggleAutomaticTransition;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// this.context = activity.getApplicationContext();
		// this.activityClass = activity.getClass();
		this.activity = activity;
	}// onAttach

	@Override
	public void onStart() {
		super.onStart();
	}// onStart

	@Override
	public void onStop() {
		super.onStop();
		wifiThread.stopScan();
		try {
			wifiThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}// try
	}// onStop

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.wifi_fragment, container);

		this.textViewSavedWifiAps = (TextView) view
				.findViewById(R.id.textViewSavedWifiAps);
		// this.textViewDistance = (TextView) view
		// .findViewById(R.id.textViewDistance);

		this.buttonLoadWifiMap = (Button) view
				.findViewById(R.id.buttonLoadWifiMap);
		this.buttonLoadWifiMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					wifiThread.loadWifiMap();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// wifiMap.load();
			}// onClick
		});

		this.buttonToggleAutomaticTransition = (Button) view
				.findViewById(R.id.buttonToggleAutomaticTransition);
		this.buttonToggleAutomaticTransition
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (wifiThread == null)
							return;
						if (wifiThread.isTransitionEnabled() == true) {
							wifiThread.enableTransition(false);
							buttonToggleAutomaticTransition
									.setText("自動遷移を許可する（現在禁止されています）");
						} else {
							wifiThread.enableTransition(true);
							buttonToggleAutomaticTransition
									.setText("自動遷移を禁止する（現在許可されています）");
						}// if
					}// onClick
				});

		this.buttonClearWifiMap = (Button) view
				.findViewById(R.id.buttonDeleteWifiMap);
		this.buttonClearWifiMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				wifiThread.clearWifiMap();
			}// onClick
		});

		this.buttonSaveWifiMap = (Button) view
				.findViewById(R.id.buttonSaveWifiMap);

		this.buttonSaveWifiMap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final File file = new File(contentUnit.getDirectory(),
						"wifi.log");
				new AlertDialog.Builder(activity)
						.setCancelable(true)
						.setTitle("Wi-Fiアクセスポイント情報を保存しますか？")
						.setPositiveButton("保存する",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										int n_saved_ap_sets = wifiThread
												.saveWifiMap();
										// wifiAps.save(file);
										textViewSavedWifiAps.setText(""
												+ n_saved_ap_sets + "地点保存しました");
									}// onClick
								}).setNegativeButton("しない", null).show();
			}// onClick
		});// setOnClickListener

		buttonRegister = (Button) view
				.findViewById(R.id.buttonRegisterApTemporaliry);
		buttonRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					wifiThread.learn(contentUnit);
					// wifiMap.put(contentUnit.getContentPath(),
					// wifiAps.clone());
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
					return;
				}// try
			}// onClick
		});// setOnClickListener

		return this.view;
	}// onCreate

	void update(ContentUnit content_unit) {
		this.contentUnit = content_unit;
	}// update

	// public void detect() {
	// int candidate_count = 0;
	// ArrayList<Integer> candidate_content_path = null;
	// for (ArrayList<Integer> content_path : wifiMap.keySet()) {
	// WifiAps wifi_aps = wifiMap.get(content_path);
	// int count = wifi_aps.countMatchedAps(this.wifiAps);
	// if (count > candidate_count) {
	// candidate_count = count;
	// candidate_content_path = content_path;
	// }
	// }// for
	// if (candidate_content_path != null) {
	// Intent intent = new Intent(context, activityClass);
	// intent.putIntegerArrayListExtra("contentPath",
	// candidate_content_path);
	// startActivity(intent);
	// }// if
	// }// detect

	public void setLastUpdated() {
		this.wifiThread.setLastUpdated();
	}// setLastUpdated

	public void startWifiThread(ContentUnit root_contet_unit)
			throws IOException {
		wifiThread = new WifiThread(this.activity.getApplicationContext(),
				this.activity.getClass(), root_contet_unit);
		wifiThread.textViewLastApSet = (TextView)view.findViewById(R.id.textViewWifiAps);
		wifiThread.textViewMatchedApSet = (TextView)view.findViewById(R.id.textViewMatchedWifiApSet);
		wifiThread.textViewMatchedContentPath = (TextView)view.findViewById(R.id.textViewMatchedContentPath);
		wifiThread.start();
	}// startWifiThread
}// WifiFragment
