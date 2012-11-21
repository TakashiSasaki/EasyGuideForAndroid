package com.gmail.takashi316.easyguide.player;

import com.gmail.takashi316.easyguide.content.BitmapLoader;
import com.gmail.takashi316.easyguide.content.ContentUnit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class ScalableImageFragment extends Fragment {
	private FrameLayout frameLayoutImage;
	private ImageView imageViewScalable;
	private ContentUnit contentUnit;
	private Context context;
	private Class<? extends Activity> activityClass;

	@Override
	public void onAttach(Activity activity) {
		this.context = activity.getApplicationContext();
		this.activityClass = activity.getClass();
		super.onAttach(activity);
	}// onAttach

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.scalable_image_fragment, container);
		this.frameLayoutImage = (FrameLayout) v
				.findViewById(R.id.frameLayoutScalableImage);
		this.imageViewScalable = (ImageView) v
				.findViewById(R.id.imageViewScalable);
		return v;
	}// onCreateView

	@Override
	public void onPause() {
		super.onPause();
		this.imageViewScalable.setImageBitmap(null);
	}// onPause

	public void update(ContentUnit content_unit) {
		this.contentUnit = content_unit;
		if (content_unit.hasImageFile()) {
			this.imageViewScalable.setImageBitmap(null);
			BitmapLoader bitmap_loader = new BitmapLoader(context);
			bitmap_loader.loadBitmapFromFile(content_unit.getImageFile());
			Bitmap b = bitmap_loader.getBitmap();
			this.imageViewScalable.setImageBitmap(b);
			this.frameLayoutImage.setVisibility(View.VISIBLE);
		} else {
			this.frameLayoutImage.setVisibility(View.GONE);
			this.imageViewScalable.setImageBitmap(null);
		}// if content unit has an image
	}// update

	public void deleteBitmap() {
		this.imageViewScalable.setImageBitmap(null);
	}
}// ScalableImageFragment
