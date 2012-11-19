package com.gmail.takashi316.easyguide.player;

import java.util.ArrayList;

import com.gmail.takashi316.easyguide.content.BitmapLoader;
import com.gmail.takashi316.easyguide.content.ContentUnit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImageFragment extends Fragment {
	private FrameLayout frameLayoutImage;
	private ImageView imageViewClickable;
	private ContentUnit contentUnit;
	// private Activity activity;
	private Context context;
	private Class<? extends Activity> activityClass;

	@Override
	public void onAttach(Activity activity) {
		this.context = activity.getApplicationContext();
		this.activityClass = activity.getClass();
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.image_fragment, container);
		this.frameLayoutImage = (FrameLayout) v.findViewById(
				R.id.frameLayoutImage);
		this.imageViewClickable = (ImageView) v.findViewById(
				R.id.imageViewClickable);

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

				ContentUnit nearest_child = contentUnit.getNearestChild(
						bitmap_x, bitmap_y);
				if (nearest_child == null)
					return true;

				Intent intent = new Intent(context, activityClass);
				intent.putExtra("contentPath", nearest_child.getContentPath());
				startActivity(intent);
				// setContentUnit(nearest_child);
				// videoFragment.stopPlayback();
				// videoFragment.hide();
				//onResume();

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

		//return super.onCreateView(inflater, container, savedInstanceState);
		return v;
	}

	@Override
	public void onPause() {
		super.onPause();
		this.imageViewClickable.setImageBitmap(null);
		// if (this.bitmap != null) {
		// this.bitmap.recycle();
		// this.bitmap = null;
		// }
	}// onPause

	public void update(ContentUnit content_unit) {
		this.contentUnit = content_unit;
		if (content_unit.hasImageFile()) {
			this.imageViewClickable.setImageBitmap(null);
			BitmapLoader bitmap_loader = new BitmapLoader(context);
			bitmap_loader.loadBitmapFromFile(content_unit.getImageFile());
			// this.directoryImage.setContentUnit(this.contentUnit);
			Bitmap b = bitmap_loader.getBitmap();
			//this.imageViewClickable.setScaleType(ScaleType.FIT_CENTER);
			this.imageViewClickable.setImageBitmap(b);
			// LayoutParams image_view_layout_params = this.imageViewClickable
			// .getLayoutParams();
			// this.surfaceView.setLayoutParams(image_view_layout_params);
			this.frameLayoutImage.setVisibility(View.VISIBLE);
		} else {
			this.frameLayoutImage.setVisibility(View.GONE);
			this.imageViewClickable.setImageBitmap(null);
		}// if content unit has an image

	}// update
}

class SurfaceViewFragment extends ImageFragment implements
		SurfaceHolder.Callback {

	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private static Bitmap star;
	private ArrayList<Point> starPoints = new ArrayList<Point>();
	// private float scaleX, scaleY;
	// private float offsetX, offsetY;
	private ImageView imageViewClickable;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.surfaceView = (SurfaceView) getView().findViewById(
				R.id.surfaceViewClickable);
		this.surfaceView.setZOrderOnTop(true);
		this.surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		this.surfaceView.getHolder().addCallback(this);
		star = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_rating_star_on_selected);
		this.imageViewClickable = (ImageView) getView().findViewById(
				R.id.imageViewClickable);
		LayoutParams image_view_layout_params = this.imageViewClickable
				.getLayoutParams();
		this.surfaceView.setLayoutParams(image_view_layout_params);
		return super.onCreateView(inflater, container, savedInstanceState);
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

	protected void drawSurface() {
		if (this.surfaceHolder == null)
			return;
		if (this.starPoints == null)
			return;
		for (Point p : this.starPoints) {
			// Point on_image_view = getPointOnImageView(p);
			// float x = (point_on_bitmap.x) * this.scaleX + this.offsetX;
			// float y = (point_on_bitmap.y) * this.scaleY + this.offsetY;
			// Drawable d = this.imageViewClickable.getDrawable();
			// Log.v(new Throwable(), "IntrinsicHeight=" +
			// d.getIntrinsicHeight()
			// + " IntrinsicWidth=" + d.getIntrinsicWidth());
			// if (on_image_view.x < 0 || on_image_view.x >=
			// d.getIntrinsicWidth()
			// || on_image_view.y < 0
			// || on_image_view.y >= d.getIntrinsicHeight()) {
			// return;
			// }
			Canvas c = this.surfaceHolder.lockCanvas();
			c.drawBitmap(star, p.x, p.y, null);
			this.surfaceHolder.unlockCanvasAndPost(c);
		}// for
	}// drawSurface

	// private Point getPointOnImageView(Point point_on_bitmap) {
	// return new Point((int) x, (int) y);
	// }

	protected void addStarPoint(Point point) {
		this.starPoints.add(point);
	}
}// SurfaceViewFragment

