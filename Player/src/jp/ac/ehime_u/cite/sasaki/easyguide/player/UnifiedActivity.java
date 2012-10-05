package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import jp.ac.ehime_u.cite.sasaki.easyguide.content.ContentUnit;
import jp.ac.ehime_u.cite.sasaki.easyguide.content.DirectoryImage;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Building;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.DistanceCalculator;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.ItemBase;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;

import android.app.Activity;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

public class UnifiedActivity extends Activity implements SurfaceHolder.Callback {
	private ImageView imageView;
	private SurfaceView surfaceView;
	private static Bitmap star;
	private Bitmap bitmap;
	private SurfaceHolder surfaceHolder;
	// private ItemBase itemBase;
	private ContentUnit contentUnit;
	private float scaleX, scaleY;
	private float offsetX, offsetY;
	private ArrayList<Point> starPoints = new ArrayList<Point>();

	private HorizontalScrollView horizontalScrollViewSiblingsAndParents;
	private HorizontalScrollView horizontalScrollViewBreadcrumb;
	private LinearLayout layoutBreadcrumb;
	private LinearLayout layoutVideo;
	private FrameLayout frameLayoutImage;
	private LinearLayout layoutHtml;
	private HorizontalScrollView horizontalScrollViewButtons;
	private LinearLayout layoutButtons;
	private LinearLayout layoutText;
	private ImageView imageViewClickable;
	private VideoView videoView;
	private TextView textViewContent;
	private MediaController mediaController;

	// private DirectoryImage directoryImage = new DirectoryImage();
	private WifiManager wifiManager;
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.unified);
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.imageView = (ImageView) findViewById(R.id.imageViewClickable);
		this.surfaceView = (SurfaceView) findViewById(R.id.surfaceViewClickable);
		this.horizontalScrollViewSiblingsAndParents = (HorizontalScrollView) findViewById(R.id.horizontalScrollViewSiblingsAndParents);
		this.horizontalScrollViewBreadcrumb = (HorizontalScrollView) findViewById(R.id.scrollViewBreadcrumb);
		this.layoutBreadcrumb = (LinearLayout) findViewById(R.id.layoutBreadcrumb);
		this.layoutText = (LinearLayout) findViewById(R.id.layoutText);
		this.layoutVideo = (LinearLayout) findViewById(R.id.layoutVideo);
		this.frameLayoutImage = (FrameLayout) findViewById(R.id.frameLayoutImage);
		this.layoutHtml = (LinearLayout) findViewById(R.id.layoutHtml);
		this.horizontalScrollViewButtons = (HorizontalScrollView) findViewById(R.id.horizontalScrollViewButtons);
		this.layoutButtons = (LinearLayout) findViewById(R.id.layoutButtons);
		this.imageViewClickable = (ImageView) findViewById(R.id.imageViewClickable);
		this.videoView = (VideoView) findViewById(R.id.videoView1);
		this.textViewContent = (TextView) findViewById(R.id.textViewContent);
		this.mediaController = (MediaController) findViewById(R.id.mediaController1);
		this.webView = (WebView) findViewById(R.id.webView);
		this.layoutBreadcrumb = (LinearLayout) findViewById(R.id.layoutBreadcrumb);

		// this.videoView.setMediaController(this.mediaController);

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
		this.contentUnit = new ContentUnit(content_root_directory, null);

		this.wifiManager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
		this.wifiManager.startScan();

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

	// protected void setSpinnerArrayAdapter(ArrayAdapter<T> array_adapter) {
	// Spinner s = (Spinner) findViewById(R.id.spinner);
	// s.setAdapter(array_adapter);
	// OnItemSelectedListener l = new OnItemSelectedListener() {
	// @Override
	// public void onItemSelected(AdapterView<?> parent, View arg1,
	// int position, long arg3) {
	// T selected_item = (T) parent.getItemAtPosition(position);
	// if (selected_item.isEmpty())
	// return;
	// Log.v(new Throwable(), "Facility " + selected_item.getTitle()
	// + ", " + selected_item.getIndex() + " was selected");
	// InvokeActivity(selected_item);
	// }
	//
	// @Override
	// public void onNothingSelected(AdapterView<?> arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	// };// OnItemSelectedListener
	// s.setSelection(0);
	// s.setSelected(false);
	// s.setOnItemSelectedListener(l);
	// }// setSpinnerArrayAdapter

	// protected abstract void InvokeActivity(T selected_item);

	protected void setImageView(ItemBase item_base) throws Exception {
		this.imageView.setImageBitmap(null);
		if (this.bitmap != null) {
			this.bitmap.recycle();
			this.bitmap = null;
		}
		// this.itemBase = item_base;
		this.bitmap = item_base.getImage(this);
		this.imageView.setImageBitmap(this.bitmap);
		LayoutParams image_view_layout_params = this.imageView
				.getLayoutParams();
		this.surfaceView.setLayoutParams(image_view_layout_params);
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.imageView.setImageBitmap(null);
		if (this.bitmap != null) {
			this.bitmap.recycle();
			this.bitmap = null;
		}
	}

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
				FileReader file_reader = new FileReader(
						this.contentUnit.getTextFile());
				BufferedReader buffer_reader = new BufferedReader(file_reader);
				StringBuilder sb = new StringBuilder();
				String s;
				while ((s = buffer_reader.readLine()) != null) {
					sb.append(s + "\n");
				}// while
				this.textViewContent.setText(sb.toString());
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
			DirectoryImage directory_image = new DirectoryImage(
					this.contentUnit);
			// this.directoryImage.setContentUnit(this.contentUnit);
			this.imageViewClickable.setImageBitmap(directory_image
					.getBitmap(this));
			this.frameLayoutImage.setVisibility(View.VISIBLE);
		} else {
			this.frameLayoutImage.setVisibility(View.GONE);
			this.imageView.setImageBitmap(null);
		}// if content unit has an image

		if (this.contentUnit.getChildren().length == 0) {
			// this.layoutButtons.setVisibility(View.GONE);
		} else if (this.contentUnit.getChildren().length > 0) {
			this._showButtons();
			this.layoutButtons.setVisibility(View.VISIBLE);
		}// if content unit has children

		if (this.contentUnit.getAncestors().size() == 0) {
			this.layoutBreadcrumb.setVisibility(View.GONE);
		} else if (this.contentUnit.getAncestors().size() > 0) {
			this._showParents();
			this.layoutBreadcrumb.setVisibility(View.VISIBLE);
		}// if content unit has parents
	}// onResume

	public void setContentUnit(ContentUnit cu) {
		this.contentUnit = cu;
	}

	private void _showButtons() {
		this.layoutButtons.removeAllViews();
		final UnifiedActivity ua = this;

		for (final ContentUnit cu : this.contentUnit.getChildren()) {
			Button b = new Button(this);
			b.setText(cu.getName());
			b.setTextSize(30);
			// b.setBackgroundColor(Color.YELLOW);
			b.setTextColor(Color.BLACK);
			b.setMinWidth(30);
			b.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ua.setContentUnit(cu);
					ua.onResume();
				}
			});
			this.layoutButtons.addView(b);

		}// for
		final ContentUnit parent_cu = this.contentUnit.getParent();
		if (parent_cu != null) {
			Button b = new Button(this);
			b.setText("もどる");
			b.setTextSize(30);
			b.setBackgroundColor(Color.GREEN);
			// b.setTextColor(Color.WHITE);
			b.setGravity(Gravity.RIGHT);
			b.setMinWidth(30);
			b.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ua.setContentUnit(parent_cu);
					ua.videoView.stopPlayback();
					ua.layoutVideo.setVisibility(View.GONE);
					ua.onResume();
				}// onClick
			});
			 this.layoutButtons.addView(b);
		}// if
		this.layoutButtons.setMinimumWidth(this.horizontalScrollViewButtons
				.getWidth());
	}// _showButtons

	private void _showParents() {
		this.layoutBreadcrumb.removeAllViews();
		final UnifiedActivity unified_activity = this;

		for (int i = this.contentUnit.getAncestors().size() - 1; i >= 0; --i) {
			Button b = new Button(this);
			b.setText(this.contentUnit.getAncestors().get(i).getName());
			b.setTextSize(30);
			b.setTextColor(Color.BLACK);
			b.setMinWidth(30);
			final ContentUnit content_unit = this.contentUnit.getAncestors()
					.get(i);
			b.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					unified_activity.setContentUnit(content_unit);
					unified_activity.videoView.stopPlayback();
					unified_activity.layoutVideo.setVisibility(View.GONE);
					unified_activity.onResume();
				}// onClick
			});// onClickListener
			this.layoutBreadcrumb.addView(b);
		}// for

		Button b = new Button(this);
		b.setText(this.contentUnit.getName());
		b.setTextSize(30);
		b.setTextColor(Color.BLACK);
		b.setMinWidth(30);
		final ContentUnit content_unit = this.contentUnit;
		b.setBackgroundColor(Color.CYAN);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				unified_activity.setContentUnit(content_unit);
				unified_activity.videoView.stopPlayback();
				unified_activity.layoutVideo.setVisibility(View.GONE);
				unified_activity.onResume();
			}// onClick
		});// onClickListener
		this.layoutBreadcrumb.addView(b);

		this.horizontalScrollViewBreadcrumb.smoothScrollTo(
				this.layoutBreadcrumb.getWidth(), 0);
		this.horizontalScrollViewBreadcrumb.smoothScrollBy(200, 0);
	}// _showParents

	protected void addStarPoint(Point point) {
		this.starPoints.add(point);
	}

	// protected abstract void onStarTouched(Point point);

	// private Point getPointOnBitmap(Point point_on_image_view) {
	// initScaleAndOffset();
	// float x = (point_on_image_view.x - this.offsetX) / this.scaleX;
	// float y = (point_on_image_view.y - this.offsetY) / this.scaleY;
	// return new Point((int) x, (int) y);
	// }

	private Point getPointOnImageView(Point point_on_bitmap) {
		// initScaleAndOffset();
		float x = (point_on_bitmap.x) * this.scaleX + this.offsetX;
		float y = (point_on_bitmap.y) * this.scaleY + this.offsetY;
		return new Point((int) x, (int) y);
	}

	// private void initScaleAndOffset() {
	// Matrix matrix = this.imageView.getImageMatrix();
	// float f[] = new float[9];
	// matrix.getValues(f);
	// this.scaleX = f[0];
	// this.scaleY = f[4];
	// this.offsetX = f[2];
	// this.offsetY = f[5];
	// }

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
		if (this.contentUnit.getChildren().length == 0)
			return null;
		return this.contentUnit.getChild(1);
	}// getNearestChild

}// class UnifiedActivity
