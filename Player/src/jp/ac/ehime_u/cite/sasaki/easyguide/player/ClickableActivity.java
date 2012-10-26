package jp.ac.ehime_u.cite.sasaki.easyguide.player;

//import com.gmail.takashi316.lib.android.activity.SmartActivity;

import java.util.ArrayList;

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
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public abstract class ClickableActivity<T extends ItemBase> extends
		Activity implements SurfaceHolder.Callback {
	private ImageView imageView;
	private SurfaceView surfaceView;
	private static Bitmap star;
	private Bitmap bitmap;
	private SurfaceHolder surfaceHolder;
	private ItemBase itemBase;

	private float scaleX, scaleY;
	private float offsetX, offsetY;
	private ArrayList<Point> starPoints = new ArrayList<Point>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clickable);
		this.imageView = (ImageView) findViewById(R.id.imageViewClickable);
		this.surfaceView = (SurfaceView) findViewById(R.id.surfaceViewClickable);

		this.surfaceView.setZOrderOnTop(true);
		this.surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		this.surfaceView.getHolder().addCallback(this);

		this.imageView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				float x_on_image_view = event.getX();
				float y_on_image_view = event.getY();
				Log.v(new Throwable(), "on ImageView x=" + x_on_image_view
						+ " y=" + y_on_image_view);
				Point point_on_image_view = new Point((int) x_on_image_view,
						(int) y_on_image_view);
				Point point_on_bitmap = getPointOnBitmap(point_on_image_view);
				onStarTouched(point_on_bitmap);
				return false;
			}// onTouch
		});
	}// onCreate

	protected void setSpinnerArrayAdapter(ArrayAdapter<T> array_adapter) {
		Spinner s = (Spinner) findViewById(R.id.spinner);
		s.setAdapter(array_adapter);
		OnItemSelectedListener l = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				T selected_item = (T) parent.getItemAtPosition(position);
				if (selected_item.isEmpty())
					return;
				Log.v(new Throwable(), "Facility " + selected_item.getTitle()
						+ ", " + selected_item.getIndex() + " was selected");
				InvokeActivity(selected_item);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		};// OnItemSelectedListener
		s.setSelection(0);
		s.setSelected(false);
		s.setOnItemSelectedListener(l);
	}// setSpinnerArrayAdapter

	protected abstract void InvokeActivity(T selected_item);

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
		if (this.itemBase == null)
			return;
		try {
			setImageView(this.itemBase);
		} catch (Exception e) {
			Log.v(new Throwable(),
					"Can't set image of " + this.itemBase.getTitle());
			e.printStackTrace();
		}
	}

	protected void addStarPoint(Point point) {
		this.starPoints.add(point);
	}

	protected abstract void onStarTouched(Point point);

	private Point getPointOnBitmap(Point point_on_image_view) {
		initScaleAndOffset();
		float x = (point_on_image_view.x - this.offsetX) / this.scaleX;
		float y = (point_on_image_view.y - this.offsetY) / this.scaleY;
		return new Point((int) x, (int) y);
	}

	private Point getPointOnImageView(Point point_on_bitmap) {
		initScaleAndOffset();
		Log.v(new Throwable(), "offsetX=" + this.offsetX + " offsetY="
				+ this.offsetY + " bitmapX=" + point_on_bitmap.x + " bitmapY="
				+ point_on_bitmap.y + " scaleX=" + this.scaleX + " scaleY="
				+ this.scaleY);
		float x = (point_on_bitmap.x) * this.scaleX + this.offsetX;
		float y = (point_on_bitmap.y) * this.scaleY + this.offsetY;
		return new Point((int) x, (int) y);
	}// getPointOnImageView

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
		Log.v(new Throwable(), "Points = " + this.starPoints.size());
		Canvas c = this.surfaceHolder.lockCanvas();
		for (Point p : this.starPoints) {
			Point on_image_view = getPointOnImageView(p);
			Log.v(new Throwable(), "starX=" + on_image_view.x + " starY="
					+ on_image_view.y);
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
				ClickableActivity.star = BitmapFactory.decodeResource(
						getResources(), R.drawable.btn_rating_star_on_selected);
			}
			//c.drawBitmap(ClickableActivity.star, on_image_view.x,
			//		on_image_view.y, null);
		}// for
		this.surfaceHolder.unlockCanvasAndPost(c);
	}// drawSurface

}// ClickableActivity
