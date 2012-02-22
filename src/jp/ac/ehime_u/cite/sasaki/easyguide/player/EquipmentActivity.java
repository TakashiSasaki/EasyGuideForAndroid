package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import jp.ac.ehime_u.cite.sasaki.easyguide.exception.ItemNotFoundException;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Building;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Equipment;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Facility;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Floor;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organization;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organizations;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Panel;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Room;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class EquipmentActivity extends Activity {
	protected static final float SWIPE_MAX_OFF_PATH = 200;
	protected static final float SWIPE_MIN_DISTANCE = 100;
	protected static final float SWIPE_THRESHOLD_VELOCITY = 10;
	private GestureDetector mGestureDetector;

	Equipment equipment;
	private int organizationIndex;
	private int facilityIndex;
	private int buildingIndex;
	private int floorIndex;
	private int roomIndex;
	private int equipmentIndex;

	private VideoView videoView;
	private MediaController mediaController;
	private TextView textViewVideo;
	private TextView textViewImage;
	private TextView textViewTextPanel;
	private ImageView imageView;
	private WebView webView;
	private LinearLayout videoPanel;
	private LinearLayout imagePanel;
	private LinearLayout htmlPanel;
	private LinearLayout textPanel;
	private ArrayList<Button> buttons = new ArrayList<Button>();

	private Bitmap bitmap;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.equipment);

		SelectEquipment();
		// SetImageView();
		// SetSpinnerEquipments();
		MakeButtons();

		this.mGestureDetector = new GestureDetector(this,
				this.mOnGestureListener);

		// Organizations organizations = Organizations.GetTheOrganizations();
		//
		// this.equipment =
		// Organizations.GetTheOrganizations().GetRoom("room_a")
		// .GetEquipment("equipment_a");

		// Panel panel_a = equipment.GetPanel("panel_a");
		// // サンプルコンテンツのロード
		// WebView web_view = (WebView) findViewById(R.id.webView1);
		// File storage_directory = Environment.getExternalStorageDirectory();
		// File root_directory = new File(storage_directory, "EASYGUIDE");
		// File sample_directory = new File(root_directory, "sample");
		// File sample_index = new File(sample_directory, "sample.png");
		// web_view.loadUrl("file://" + sample_index.getAbsolutePath());
		// Log.v(this.getClass().getSimpleName(), "URL for panel_a is "
		// + "file://" + panel_a.getPanelDirectory() + "/index.html");
		// WebSettings web_settings = web_view.getSettings();
		// web_settings.setDefaultTextEncodingName("utf-8");
		// web_view.loadUrl("file://" + panel_a.getPanelDirectory()
		// + "/index.html");

		this.videoView = (VideoView) findViewById(R.id.videoView1);
		this.mediaController = (MediaController) findViewById(R.id.mediaController1);
		this.imageView = (ImageView)findViewById(R.id.imageViewImagePanel);
		this.textViewVideo = (TextView) findViewById(R.id.textViewVideo);
		this.textViewImage = (TextView) findViewById(R.id.textViewImage);
		this.textViewTextPanel = (TextView) findViewById(R.id.textViewTextPanel);
		this.buttons.add((Button) findViewById(R.id.buttonPanel1));
		this.buttons.add((Button) findViewById(R.id.buttonPanel2));
		this.buttons.add((Button) findViewById(R.id.buttonPanel3));
		this.buttons.add((Button) findViewById(R.id.buttonPanel4));
		this.buttons.add((Button) findViewById(R.id.buttonPanel5));
		this.buttons.add((Button) findViewById(R.id.buttonPanel6));
		this.videoPanel = (LinearLayout) findViewById(R.id.panel_video);
		this.htmlPanel = (LinearLayout) findViewById(R.id.panel_html);
		this.imagePanel = (LinearLayout) findViewById(R.id.panel_image);
		this.textPanel = (LinearLayout) findViewById(R.id.panel_text);

		for (Button b : this.buttons) {
			b.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String title = ((Button) v).getText().toString();
					Panel p;
					try {
						p = EquipmentActivity.this.equipment.getPanel(title);
					} catch (ItemNotFoundException e) {
						Log.v(new Throwable(), "Button text '" + title
								+ "' does not have corresponding panel.");
						return;
					}// try
					if (p.hasVideo()) {
						Log.v(new Throwable(), p.getTitle() + " has a video.");
						ChooseVideo(p);
					} else if (p.hasImage()) {
						Log.v(new Throwable(), p.getTitle() + " has an image.");
						try {
							ChooseImage(p);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (p.hasHtml()) {
						Log.v(new Throwable(), p.getTitle() + " has an html.");
						ChooseHtml(p);
					} else if (p.hasText()) {
						Log.v(new Throwable(), p.getTitle() + " has an text.");
						ChooseText(p);
					} else {
						Log.v(new Throwable(), p.getTitle() + " has no content");
					}
				}// onClick
			});// OnClickListener
		}// for

		Iterator<Panel> i = this.equipment.iterator();
		Panel p = i.next();
		if (p.hasVideo()) {
			this.videoView.setVideoPath(p.getVideoPath());
		}

	}// onCreate

	private void ChooseHtml(Panel p) {
		EquipmentActivity.this.htmlPanel.bringToFront();

	}

	private void ChooseVideo(Panel p) {
		this.imageView.setImageBitmap(null);
		if (this.bitmap != null) {
			this.bitmap.recycle();
		}
		this.bitmap = null;
		this.htmlPanel.setVisibility(View.GONE);
		this.textPanel.setVisibility(View.GONE);
		this.videoPanel.setVisibility(View.VISIBLE);
		this.imagePanel.setVisibility(View.GONE);
		this.videoPanel.bringToFront();
	}

	private void ChooseText(Panel p) {
		FileInputStream fis;
		try {
			//fis = openFileInput(p.getTextFile().getPath());
			fis = new FileInputStream(p.getTextFile());
		} catch (FileNotFoundException e) {
			Log.v(new Throwable(), "Can't switch to  panel " + p.getTitle());
			return;
		}
		byte[] buffer  =  new  byte[100000];
		try {
			fis.read(buffer);
		} catch (IOException e) {
			Log.v(new Throwable(), "Can't read text file of " + p.getTitle());
			return;
		}
		String s = new String(buffer);
		this.textViewTextPanel.setText(s);
		
		this.imageView.setImageBitmap(null);
		if (this.bitmap != null) {
			this.bitmap.recycle();
		}
		this.bitmap = null;
		this.htmlPanel.setVisibility(View.GONE);
		this.textPanel.setVisibility(View.VISIBLE);
		this.videoPanel.setVisibility(View.GONE);
		this.imagePanel.setVisibility(View.GONE);
		this.videoPanel.bringToFront();

		EquipmentActivity.this.textPanel.bringToFront();

	}

	private void ChooseImage(Panel p) throws Exception {
		if (this.bitmap != null) {
			this.bitmap.recycle();
		}
		this.bitmap = p.getImage(this);
		this.imageView.setImageBitmap(this.bitmap);
		this.htmlPanel.setVisibility(View.GONE);
		this.textPanel.setVisibility(View.GONE);
		this.videoPanel.setVisibility(View.GONE);
		this.imagePanel.setVisibility(View.VISIBLE);
		this.imagePanel.bringToFront();
	}

	@Override
	public void onStart() {
		super.onStart();
		Iterator<Button> i = this.buttons.iterator();
		for (Panel p : this.equipment) {
			if (i.hasNext()) {
				Button b = i.next();
				b.setText(p.getTitle());
				b.setVisibility(View.VISIBLE);
			}
		}// for
		while (i.hasNext()) {
			Button b = i.next();
			b.setVisibility(View.GONE);
		}
	}// onStart

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		// this.videoView.setMediaController(this.mediaController);
		this.videoView.start();
		this.textViewVideo.setText("Now plyaing ...");
	}

	private void MakeButtons() {
	}

	private void SelectEquipment() {
		Intent intent = this.getIntent();
		this.organizationIndex = intent.getIntExtra("organizationIndex", 0);
		this.facilityIndex = intent.getIntExtra("facilityIndex", 0);
		this.buildingIndex = intent.getIntExtra("buildingIndex", 0);
		this.floorIndex = intent.getIntExtra("floorIndex", 0);
		this.roomIndex = intent.getIntExtra("roomIndex", 0);
		this.equipmentIndex = intent.getIntExtra("equipmentIndex", 0);

		Organizations organizations = Organizations.getInstance();
		Organization organization;
		try {
			organization = organizations
					.getOrganization(this.organizationIndex);
			Facility facility = organization.getFacility(this.facilityIndex);
			Building building = facility.getBuilding(this.buildingIndex);
			Floor floor = building.getFloor(this.floorIndex);
			Room room = floor.getRoom(this.roomIndex);
			this.equipment = room.getEquipment(this.equipmentIndex);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// SelectEquipment

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// return super.onTouchEvent(event);
		Log.v(new Throwable(), "");
		return this.mGestureDetector.onTouchEvent(event);
	}// onTouchEvent

	@Override
	protected void onPause() {
		super.onPause();
		// this.mediaController.hide();
		// this.videoView.setMediaController(null);
		this.videoView.stopPlayback();
	}

	private final SimpleOnGestureListener mOnGestureListener = new SimpleOnGestureListener() {
		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2,
				float velocityX, float velocityY) {

			Log.v(new Throwable(),
					"X1=" + event1.getX() + ",Y1=" + event1.getY() + ",X2="
							+ event2.getX() + ",Y2=" + event2.getY());
			try {
				if (event1.getX() - event2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					// 開始位置から終了位置の移動距離が指定値より大きい
					// X軸の移動速度が指定値より大きい
					Intent intent = new Intent(getApplicationContext(),
							BuildingActivity.class);
					startActivity(intent);
				} else if (event2.getX() - event1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					// 終了位置から開始位置の移動距離が指定値より大きい
					// X軸の移動速度が指定値より大きい
					Intent intent = new Intent(getApplicationContext(),
							BuildingActivity.class);
					startActivity(intent);
				}

			} catch (Exception e) {
				// setWallpaper(data);
				// nothing
			}
			return false;
		}// onFling
	};// SimpleOnGestureListener

}// EquipmentActivity
