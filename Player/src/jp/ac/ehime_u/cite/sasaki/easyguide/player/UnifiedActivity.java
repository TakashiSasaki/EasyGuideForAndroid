package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import java.io.File;
import java.util.ArrayList;

import jp.ac.ehime_u.cite.sasaki.easyguide.content.ContentUnit;
import jp.ac.ehime_u.cite.sasaki.easyguide.content.Contents;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.ItemBase;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UnifiedActivity extends Activity implements SurfaceHolder.Callback {
	private ImageView imageView;
	private SurfaceView surfaceView;
	private static Bitmap star;
	private Bitmap bitmap;
	private SurfaceHolder surfaceHolder;
	private ItemBase itemBase;
	private Contents contents;
	private ContentUnit contentUnit;
	private float scaleX, scaleY;
	private float offsetX, offsetY;
	private ArrayList<Point> starPoints = new ArrayList<Point>();

	private HorizontalScrollView horizontalScrollViewSiblingsAndParents;
	private LinearLayout layoutVideo;
	private FrameLayout frameLayoutImage;
	private LinearLayout layoutHtml;
	private LinearLayout layoutButtons;
	private TextView textViewStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unified);
		this.imageView = (ImageView) findViewById(R.id.imageViewClickable);
		this.surfaceView = (SurfaceView) findViewById(R.id.surfaceViewClickable);
		this.horizontalScrollViewSiblingsAndParents = (HorizontalScrollView) findViewById(R.id.horizontalScrollViewSiblingsAndParents);
		this.layoutVideo = (LinearLayout) findViewById(R.id.layoutVideo);
		this.frameLayoutImage = (FrameLayout) findViewById(R.id.frameLayoutImage);
		this.layoutHtml = (LinearLayout) findViewById(R.id.layoutHtml);
		this.layoutButtons = (LinearLayout) findViewById(R.id.layoutButtons);
		this.textViewStatus = (TextView) findViewById(R.id.textViewStatus);

		this.surfaceView.setZOrderOnTop(true);
		this.surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		this.surfaceView.getHolder().addCallback(this);

		this.imageView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				float x_on_image_view = event.getX();
				float y_on_image_view = event.getY();
				Point point_on_image_view = new Point((int) x_on_image_view,
						(int) y_on_image_view);
				Point point_on_bitmap = getPointOnBitmap(point_on_image_view);
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

	}// onCreate

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
		this.itemBase = item_base;
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
		if(this.contentUnit.hasHtml() || this.contentUnit.hasText()){
			this.layoutHtml.setVisibility(View.VISIBLE);
		} else {
			this.layoutHtml.setVisibility(View.INVISIBLE);
		}
		if(this.contentUnit.hasImage()){
			this.frameLayoutImage.setVisibility(View.VISIBLE);
		} else {
			this.frameLayoutImage.setVisibility(View.INVISIBLE);
		}
		if(this.contentUnit.hasMovie()) {
			this.layoutVideo.setVisibility(View.VISIBLE);
		} else {
			this.layoutVideo.setVisibility(View.INVISIBLE);
		}
	}// onResume

	protected void addStarPoint(Point point) {
		this.starPoints.add(point);
	}

	// protected abstract void onStarTouched(Point point);

	private Point getPointOnBitmap(Point point_on_image_view) {
		initScaleAndOffset();
		float x = (point_on_image_view.x - this.offsetX) / this.scaleX;
		float y = (point_on_image_view.y - this.offsetY) / this.scaleY;
		return new Point((int) x, (int) y);
	}

	private Point getPointOnImageView(Point point_on_bitmap) {
		initScaleAndOffset();
		float x = (point_on_bitmap.x) * this.scaleX + this.offsetX;
		float y = (point_on_bitmap.y) * this.scaleY + this.offsetY;
		return new Point((int) x, (int) y);
	}

	private void initScaleAndOffset() {
		Matrix matrix = this.imageView.getImageMatrix();
		float f[] = new float[9];
		matrix.getValues(f);
		this.scaleX = f[0];
		this.scaleY = f[4];
		this.offsetX = f[2];
		this.offsetY = f[5];
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

}// class UnifiedActivity
