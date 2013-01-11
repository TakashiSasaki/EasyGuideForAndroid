package com.gmail.takashi316.easyguide.player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.gmail.takashi316.easyguide.content.ContentUnit;
import com.gmail.takashi316.easyguide.content.Root;

public class UnifiedActivity extends FragmentActivity {
	// private ImageView imageView;
	// private Bitmap bitmap;
	// private Root root;
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
	private LinearLayout htmlLinearLayout;
	private TextFragment textFragment;
	private LinearLayout textLinearLayout;
	private VideoFragment videoFragment;
	private LinearLayout videoLinearLayout;
	private ImageFragment imageFragment;
	private ScalableImageFragment scalableImageFragment;
	private LinearLayout linearLayoutScalableImage;
	private LinearLayout imageLinearLayout;
	private WifiFragment wifiFragment;
	private LinearLayout linearLayoutWifi;

	private ContentUnit rootContentUnit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.unified);
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// this.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		this.horizontalScrollViewSiblingsAndParents = (HorizontalScrollView) findViewById(R.id.horizontalScrollViewSiblingsAndParents);
		this.fragmentManager = getSupportFragmentManager();
		this.breadcrumbFragment = (BreadcrumbFragment) fragmentManager
				.findFragmentById(R.id.breadcrumbFragment);
		this.buttonsFragment = (ButtonsFragment) fragmentManager
				.findFragmentById(R.id.buttonsFragment);
		this.htmlFragment = (HtmlFragment) fragmentManager
				.findFragmentById(R.id.htmlFragment);
		this.htmlLinearLayout = (LinearLayout) findViewById(R.id.htmlLinearLayout);
		this.textFragment = (TextFragment) fragmentManager
				.findFragmentById(R.id.textFragment);
		this.textLinearLayout = (LinearLayout) findViewById(R.id.textLinearLayout);
		this.videoFragment = (VideoFragment) fragmentManager
				.findFragmentById(R.id.videoFragment);
		this.videoLinearLayout = (LinearLayout) findViewById(R.id.videoLinearLayout);
		this.imageFragment = (ImageFragment) fragmentManager
				.findFragmentById(R.id.imageFragment);
		this.imageLinearLayout = (LinearLayout) findViewById(R.id.imageLinearLayout);
		this.scalableImageFragment = (ScalableImageFragment) fragmentManager
				.findFragmentById(R.id.fragmentScalableImage);
		this.linearLayoutScalableImage = (LinearLayout) findViewById(R.id.linearLayoutScalableImage);
		this.imageLinearLayout = (LinearLayout) findViewById(R.id.imageLinearLayout);
		this.wifiFragment = (WifiFragment) fragmentManager
				.findFragmentById(R.id.wifiFragment);
		this.linearLayoutWifi = (LinearLayout) findViewById(R.id.linearLayoutWifi);

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
		}// swtich
		return super.onOptionsItemSelected(item);
	}// onOptionsItemSelected

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		wifiFragment.setLastUpdated();
		;
		if (intent != null) {
			ArrayList<Integer> content_path = intent
					.getIntegerArrayListExtra("contentPath");
			if (content_path != null) {
				this.contentUnit = this.rootContentUnit
						.getDescendant(content_path);
			}// if
		}// if
			// this.updateFragments();
		skipEmptyFolder();
	}// onNewIntent

	private void skipEmptyFolder() {
		for (;;) {
			if (this.contentUnit.getChildren().size() != 1)
				break;
			if (this.contentUnit.hasContent())
				break;
			this.contentUnit = this.contentUnit.getChild(1);
		}// for
	}// skipEmptyFolder

	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = getIntent();
		final String root = intent.getExtras().getString("root");
		if (root != null) {
			final File root_directory = new File(root);
			if (root_directory != null) {
				try {
					this.rootContentUnit = new ContentUnit(root_directory, null);
					this.rootContentUnit.enumerateChildren(true);
					this.contentUnit = this.rootContentUnit;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// try
			}// if
		} else {
			try {
				this.rootContentUnit = new Root();
				this.contentUnit = this.rootContentUnit;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// try
		}// if
		skipEmptyFolder();
		try {
			// wifiFragment.loadWifiMap(rootContentUnit);
			wifiFragment.startWifiThread(rootContentUnit);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// try
	}// onStart

	@Override
	protected void onResume() {
		super.onResume();
		this.updateFragments();

	}// onResume

	private void updateFragments() {
		htmlFragment.update(this.contentUnit);
		if (this.contentUnit.hasHtml()) {
			htmlLinearLayout.setVisibility(View.VISIBLE);
		} else {
			htmlLinearLayout.setVisibility(View.GONE);
		}// if
		textFragment.update(this.contentUnit);
		if (this.contentUnit.hasText()) {
			textLinearLayout.setVisibility(View.VISIBLE);
		} else {
			textLinearLayout.setVisibility(View.GONE);
		}// if
		videoFragment.update(this.contentUnit);
		if (this.contentUnit.hasMovie()) {
			videoLinearLayout.setVisibility(View.VISIBLE);
		} else {
			videoLinearLayout.setVisibility(View.GONE);
		}// if
		if (this.contentUnit.hasImageFile()) {
			if (this.contentUnit.getChildren().size() == 0) {
				scalableImageFragment.update(this.contentUnit);
				linearLayoutScalableImage.setVisibility(View.VISIBLE);
				imageFragment.deleteBitmap();
				imageLinearLayout.setVisibility(View.GONE);
			} else {
				imageFragment.update(this.contentUnit);
				imageLinearLayout.setVisibility(View.VISIBLE);
				scalableImageFragment.deleteBitmap();
				linearLayoutScalableImage.setVisibility(View.GONE);
			}
		} else {
			scalableImageFragment.deleteBitmap();
			linearLayoutScalableImage.setVisibility(View.GONE);
			imageFragment.deleteBitmap();
			imageLinearLayout.setVisibility(View.GONE);
		}// if
		buttonsFragment.update(this.contentUnit);
		breadcrumbFragment.update(this.contentUnit);
		wifiFragment.update(this.contentUnit);
	}// updateFragments

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		// drawSurface();
	}// onWindowFocusChanged

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// return this.mGestureDetector.onTouchEvent(event);
		return true;
	}// onTouchEvent

	public void toggleWifiLayout() {
		if (this.linearLayoutWifi.getVisibility() == View.VISIBLE) {
			this.linearLayoutWifi.setVisibility(View.GONE);
		} else {
			this.linearLayoutWifi.setVisibility(View.VISIBLE);
		}// if
	}// toggleWifiLayout
}// class UnifiedActivity
