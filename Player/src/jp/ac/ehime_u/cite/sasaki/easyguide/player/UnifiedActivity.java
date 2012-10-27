package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import jp.ac.ehime_u.cite.sasaki.easyguide.content.BitmapLoader;
import jp.ac.ehime_u.cite.sasaki.easyguide.content.ContentUnit;
import jp.ac.ehime_u.cite.sasaki.easyguide.content.TextLoader;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
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
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class UnifiedActivity extends FragmentActivity implements
		SurfaceHolder.Callback {
	private ImageView imageView;
	private SurfaceView surfaceView;
	private static Bitmap star;
	private Bitmap bitmap;
	private SurfaceHolder surfaceHolder;
	private ContentUnit contentUnit;
	private float scaleX, scaleY;
	private float offsetX, offsetY;
	private GestureDetector mGestureDetector;
	private ArrayList<Point> starPoints = new ArrayList<Point>();

	private HorizontalScrollView horizontalScrollViewSiblingsAndParents;
	LinearLayout layoutVideo;
	private FrameLayout frameLayoutImage;
	private LinearLayout layoutHtml;
	private LinearLayout layoutText;
	private ImageView imageViewClickable;
	VideoView videoView;
	private TextView textViewContent;
	private MediaController mediaController;

	private WifiManager wifiManager;
	private WebView webView;
	private Handler handler;
	private FragmentManager fragmentManager;
	private BreadcrumbFragment breadcrumbFragment;
	private ButtonsFragment buttonsFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.unified);
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.imageView = (ImageView) findViewById(R.id.imageViewClickable);
		this.surfaceView = (SurfaceView) findViewById(R.id.surfaceViewClickable);
		this.horizontalScrollViewSiblingsAndParents = (HorizontalScrollView) findViewById(R.id.horizontalScrollViewSiblingsAndParents);
		this.layoutText = (LinearLayout) findViewById(R.id.layoutText);
		this.layoutVideo = (LinearLayout) findViewById(R.id.layoutVideo);
		this.frameLayoutImage = (FrameLayout) findViewById(R.id.frameLayoutImage);
		this.layoutHtml = (LinearLayout) findViewById(R.id.layoutHtml);
		this.imageViewClickable = (ImageView) findViewById(R.id.imageViewClickable);
		this.videoView = (VideoView) findViewById(R.id.videoView1);
		this.textViewContent = (TextView) findViewById(R.id.textViewContent);
		this.webView = (WebView) findViewById(R.id.webView);
		this.fragmentManager = getSupportFragmentManager();
		this.breadcrumbFragment = (BreadcrumbFragment) fragmentManager
				.findFragmentById(R.id.breadcrumbFragment);
		this.buttonsFragment = (ButtonsFragment) fragmentManager
				.findFragmentById(R.id.buttonsFragment);

		this.surfaceView.setZOrderOnTop(true);
		this.surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		this.surfaceView.getHolder().addCallback(this);
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		this.imageViewClickable.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				float x_on_image_view = event.getX();
				float y_on_image_view = event.getY();

				Matrix matrix = imageViewClickable.getImageMatrix();
				float f[] = new float[9];
				matrix.getValues(f);
				float scale_x = f[0];
				float scale_y = f[4];
				float offset_x = f[2];
				float offset_y = f[5];

				float bitmap_x = (x_on_image_view - offset_x) / scale_x;
				float bitmap_y = (y_on_image_view - offset_y) / scale_y;

				ContentUnit nearest_child = getNearestChild(bitmap_x, bitmap_y);
				if (nearest_child == null)
					return true;

				setContentUnit(nearest_child);
				videoView.stopPlayback();
				layoutVideo.setVisibility(View.GONE);
				onResume();

				// Point point_on_image_view = new Point((int) x_on_image_view,
				// (int) y_on_image_view);
				// Point point_on_bitmap =
				// getPointOnBitmap(point_on_image_view);
				//
				// onStarTouched(point_on_bitmap);
				// TODO: should be implemented
				return false;
			}// onTouch
		});

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
	protected void onPause() {
		super.onPause();
		this.imageView.setImageBitmap(null);
		if (this.bitmap != null) {
			this.bitmap.recycle();
			this.bitmap = null;
		}
	}// onPause

	@Override
	protected void onResume() {
		super.onResume();
		if (this.contentUnit.hasHtml()) {
			this.webView.loadUrl("file://" + this.contentUnit.getDirectory()
					+ "/index.html");
			this.layoutHtml.setVisibility(View.VISIBLE);
		} else {
			this.layoutHtml.setVisibility(View.GONE);
		}// if contentUnit.has HTML

		if (this.contentUnit.hasText()) {
			try {
				TextLoader text_loader = new TextLoader();
				text_loader.loadTextFromFile(this.contentUnit.getTextFile());
				this.textViewContent.setText(text_loader.getText());
				this.textViewContent.setTextSize(35);
				this.textViewContent.setBackgroundColor(Color.WHITE);
				this.textViewContent.setTextColor(Color.BLACK);
				this.layoutText.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				this.layoutText.setVisibility(View.GONE);
			}// try
		} else {
			this.layoutText.setVisibility(View.GONE);
		}// if contentUnit has a text

		if (this.contentUnit.hasMovie()) {
			// this.layoutVideo.bringToFront();
			this.layoutVideo.setBackgroundColor(Color.WHITE);
			if (!this.videoView.isPlaying()) {
				this.videoView.setVideoPath(this.contentUnit.getMovieFile()
						.getAbsolutePath());
				this.videoView.setMediaController(new MediaController(this));
				this.videoView.start();
			}
			this.layoutVideo.setVisibility(View.VISIBLE);
		} else {
			this.layoutVideo.setVisibility(View.GONE);
		}// if contentunit has a movie

		if (this.contentUnit.hasImageFile()) {
			this.imageViewClickable.setImageBitmap(null);
			BitmapLoader bitmap_loader = new BitmapLoader(this);
			bitmap_loader.loadBitmapFromFile(this.contentUnit.getImageFile());
			// this.directoryImage.setContentUnit(this.contentUnit);
			this.imageViewClickable.setImageBitmap(bitmap_loader.getBitmap());
			LayoutParams image_view_layout_params = this.imageView
					.getLayoutParams();
			this.surfaceView.setLayoutParams(image_view_layout_params);
			this.frameLayoutImage.setVisibility(View.VISIBLE);
		} else {
			this.frameLayoutImage.setVisibility(View.GONE);
			this.imageView.setImageBitmap(null);
		}// if content unit has an image

		if (this.contentUnit.getChildren().size() == 0) {
			// this.layoutButtons.setVisibility(View.GONE);
		} else if (this.contentUnit.getChildren().size() > 0) {
			buttonsFragment.showButtons(this, contentUnit);
			buttonsFragment.show();
		}// if content unit has children

		if (this.contentUnit.getAncestors().size() == 0) {
			this.breadcrumbFragment.hide();
		} else if (this.contentUnit.getAncestors().size() > 0) {
			this.breadcrumbFragment.showParents(
					this.contentUnit.getAncestors(), this.contentUnit, this,
					this);
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

	protected void addStarPoint(Point point) {
		this.starPoints.add(point);
	}

	private Point getPointOnImageView(Point point_on_bitmap) {
		float x = (point_on_bitmap.x) * this.scaleX + this.offsetX;
		float y = (point_on_bitmap.y) * this.scaleY + this.offsetY;
		return new Point((int) x, (int) y);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}// surfaceChanged

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.surfaceHolder = holder;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		this.surfaceHolder = null;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		drawSurface();
	}

	protected void drawSurface() {
		if (this.surfaceHolder == null)
			return;
		if (this.starPoints == null)
			return;
		for (Point p : this.starPoints) {
			Point on_image_view = getPointOnImageView(p);
			Drawable d = this.imageView.getDrawable();
			Log.v(new Throwable(), "IntrinsicHeight=" + d.getIntrinsicHeight()
					+ " IntrinsicWidth=" + d.getIntrinsicWidth());
			// if (on_image_view.x < 0 || on_image_view.x >=
			// d.getIntrinsicWidth()
			// || on_image_view.y < 0
			// || on_image_view.y >= d.getIntrinsicHeight()) {
			// return;
			// }
			if (star == null) {
				UnifiedActivity.star = BitmapFactory.decodeResource(
						getResources(), R.drawable.btn_rating_star_on_selected);
			}
			Canvas c = this.surfaceHolder.lockCanvas();
			c.drawBitmap(UnifiedActivity.star, on_image_view.x,
					on_image_view.y, null);
			this.surfaceHolder.unlockCanvasAndPost(c);
		}// for
	}// drawSurface

	public ContentUnit getNearestChild(float bitmap_x, float bitmap_y) {
		ContentUnit nearest_child = null;
		float min_distance_squared = 2000 * 2000;
		for (ContentUnit child : this.contentUnit.getChildren()) {
			if (child.getX() < 0 || child.getY() < 0)
				continue;
			float dx = child.getX() - bitmap_x;
			float dy = child.getY() - bitmap_y;
			float distance = dx * dx + dy * dy;
			if (min_distance_squared > distance) {
				min_distance_squared = distance;
				nearest_child = child;
			}
		}
		if (nearest_child != null)
			return nearest_child;
		if (this.contentUnit.getChildren().size() == 0)
			return null;
		return this.contentUnit.getChild(1);
	}// getNearestChild

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.mGestureDetector.onTouchEvent(event);
	}// onTouchEvent

}// class UnifiedActivity
