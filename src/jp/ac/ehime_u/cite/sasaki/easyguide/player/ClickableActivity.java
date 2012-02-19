package jp.ac.ehime_u.cite.sasaki.easyguide.player;

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
import android.widget.ImageView;

public abstract class ClickableActivity extends Activity implements
		SurfaceHolder.Callback {
	private ImageView imageView;
	private SurfaceView surfaceView;
	private static Bitmap star;
	private SurfaceHolder surfaceHolder;

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
				Point point_on_image_view = new Point((int) x_on_image_view,
						(int) y_on_image_view);
				Point point_on_bitmap = getPointOnBitmap(point_on_image_view);
				onStarTouched(point_on_bitmap);
				return false;
			}// onTouch
		});
	}// onCreate

	protected void setImageView(ItemBase item_base) {
		this.imageView.setImageBitmap(item_base.getImage());
		LayoutParams image_view_layout_params = this.imageView
				.getLayoutParams();
		this.surfaceView.setLayoutParams(image_view_layout_params);
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
		float x = (point_on_bitmap.x) * this.scaleX + this.offsetX;
		float y = (point_on_bitmap.y) * this.scaleY + this.offsetY;
		return new Point((int) x, (int) y);
	}

	private void initScaleAndOffset() {
		Matrix matrix = this.imageView.getImageMatrix();
		float f[] = new float[9];
		matrix.getValues(f);
		for (int i = 0; i < 9; ++i) {
			Log.v(new Throwable(), "float[" + i + "]=" + f[i]);
		}// for
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
		if (surfaceHolder == null)
			return;
		if (starPoints == null)
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
				ClickableActivity.star = BitmapFactory.decodeResource(
						getResources(), R.drawable.btn_rating_star_on_selected);
			}
			Canvas c = surfaceHolder.lockCanvas();
			c.drawBitmap(ClickableActivity.star, on_image_view.x,
					on_image_view.y, null);
			surfaceHolder.unlockCanvasAndPost(c);
		}// for
	}// drawSurface

}// ClickableActivity
