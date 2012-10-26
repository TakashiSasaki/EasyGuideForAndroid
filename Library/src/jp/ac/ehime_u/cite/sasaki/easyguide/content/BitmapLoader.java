package jp.ac.ehime_u.cite.sasaki.easyguide.content;

import java.io.File;

import jp.ac.ehime_u.cite.sasaki.easyguide.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class BitmapLoader {

	Context context;
	Bitmap bitmap;

	public BitmapLoader(Context context) {
		this.context = context;
	}

	public void loadDefaultBitmap() {
		Resources resources = this.context.getResources();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPurgeable = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		this.bitmap = BitmapFactory.decodeResource(resources,
				R.drawable.unknown, options);
	}// loadDefaultBitmap

	public void loadBitmapFromFile(File file) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		int denominator = Math.min(options.outWidth / 1024,
				options.outHeight / 1024) + 1;
		options = new BitmapFactory.Options();
		options.inPurgeable = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inSampleSize = denominator;
		this.bitmap = BitmapFactory.decodeFile(file.getPath(), options);
	}// loadBitmapFromFile

	public void resizeBitmap(int width, int height) {
		int original_height = this.bitmap.getHeight();
		int original_width = this.bitmap.getWidth();
		float height_scale = height / (float) original_height;
		float width_scale = width / (float) original_width;
		Matrix resize_matrix = new Matrix();
		resize_matrix.postScale(width_scale, height_scale);
		Bitmap resized_bitmap = Bitmap.createBitmap(this.bitmap, 0, 0,
				original_width, original_height, resize_matrix, true);
		this.bitmap = resized_bitmap;
	}// resizeBitmap

	public Bitmap getBitmap() {
		if (this.bitmap == null) {
			loadDefaultBitmap();
		}
		return this.bitmap;
	}

}// BitmapLoader
