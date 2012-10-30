package com.gmail.takashi316.easyguide.player;

import java.io.File;
import java.io.FileNotFoundException;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;

import com.gmail.takashi316.easyguide.content.ContentUnit;

public class UnifiedActivity extends FragmentActivity {
	// private ImageView imageView;
	private Bitmap bitmap;
	private ContentUnit contentUnit;
	private GestureDetector mGestureDetector;

	private HorizontalScrollView horizontalScrollViewSiblingsAndParents;
	// private MediaController mediaController;

	private WifiManager wifiManager;
	private Handler handler;
	private FragmentManager fragmentManager;
	private BreadcrumbFragment breadcrumbFragment;
	private ButtonsFragment buttonsFragment;
	private HtmlFragment htmlFragment;
	private TextFragment textFragment;
	private VideoFragment videoFragment;
	private ImageFragment imageFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.unified);
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// this.imageView = (ImageView) findViewById(R.id.imageViewClickable);
		this.horizontalScrollViewSiblingsAndParents = (HorizontalScrollView) findViewById(R.id.horizontalScrollViewSiblingsAndParents);
		this.fragmentManager = getSupportFragmentManager();
		this.breadcrumbFragment = (BreadcrumbFragment) fragmentManager
				.findFragmentById(R.id.breadcrumbFragment);
		this.buttonsFragment = (ButtonsFragment) fragmentManager
				.findFragmentById(R.id.buttonsFragment);
		this.htmlFragment = (HtmlFragment) fragmentManager
				.findFragmentById(R.id.htmlFragment);
		this.textFragment = (TextFragment) fragmentManager
				.findFragmentById(R.id.textFragment);
		this.videoFragment = (VideoFragment) fragmentManager
				.findFragmentById(R.id.videoFragment);
		this.imageFragment = (ImageFragment) fragmentManager
				.findFragmentById(R.id.imageFragment);

		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		File external_storage_directory = Environment
				.getExternalStorageDirectory();
		File content_root_directory = new File(external_storage_directory,
				"EASYGUIDE/www.yonden.co.jp/01 四国電力");
		try {
			this.contentUnit = new ContentUnit(content_root_directory, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		this.wifiManager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
		this.wifiManager.startScan();

		this.mGestureDetector = new FourDirectionsGestureDetector(this, null,
				null, null, null);

	}// onCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menu_inflater = getMenuInflater();
		menu_inflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}// onCreateOptionsMenu

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemExit:
			this.setResult(RESULT_OK);
			this.finish();
			return true;
		case R.id.itemWifiSense:
			new WifiSenseAndShowDialog(this, this.wifiManager,
					this.contentUnit.getDirectory());
			return true;
		case R.id.itemWifiLocate:
			AlertDialog.Builder alert_dialog = new AlertDialog.Builder(this);
			alert_dialog.setPositiveButton("OK", null);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}// onOptionsItemSelected

	@Override
	protected void onResume() {
		super.onResume();
		htmlFragment.update(this.contentUnit);
		textFragment.update(this.contentUnit);
		videoFragment.update(this.contentUnit, this);

		if (this.contentUnit.getChildren().size() == 0) {
			// this.layoutButtons.setVisibility(View.GONE);
		} else if (this.contentUnit.getChildren().size() > 0) {
			buttonsFragment.showButtons(this, contentUnit, videoFragment);
			buttonsFragment.show();
		}// if content unit has children

		if (this.contentUnit.getAncestors().size() == 0) {
			this.breadcrumbFragment.hide();
		} else if (this.contentUnit.getAncestors().size() > 0) {
			this.breadcrumbFragment.showParents(
					this.contentUnit.getAncestors(), this.contentUnit, this,
					this, videoFragment);
			this.breadcrumbFragment.show();
		}// if content unit has parents
	}// onResume

	public void setContentUnit(ContentUnit cu) {
		this.contentUnit = cu;
		this.handler.post(new Runnable() {
			@Override
			public void run() {
				onResume();
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		// drawSurface();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.mGestureDetector.onTouchEvent(event);
	}// onTouchEvent

}// class UnifiedActivity
