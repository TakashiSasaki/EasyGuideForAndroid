package jp.ac.ehime_u.cite.sasaki.easyguide;

import java.util.concurrent.Semaphore;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class SetImageBitmapRunnable implements Runnable {
	Bitmap bitmapToSet;
	ImageView imageView;
	Semaphore semaphore;

	public SetImageBitmapRunnable(Bitmap bitmap_to_set, ImageView image_view,
			Semaphore semaphore_) {
		bitmapToSet = bitmap_to_set;
		imageView = image_view;
		semaphore = semaphore_;
	}

	@Override
	public void run() {
		// assert (b != null);
		// Resources r = getResources();
		// Bitmap b2 = BitmapFactory.decodeResource(r,
		// R.drawable.ic_launcher);
		imageView.setImageBitmap(bitmapToSet);
		//bitmapToSet.recycle();
		semaphore.release();
	}
}// SetImageBitmapRunnable

