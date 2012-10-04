package jp.ac.ehime_u.cite.sasaki.easyguide.content;

import java.io.File;

import jp.ac.ehime_u.cite.sasaki.easyguide.R;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class DirectoryImage {

	// private Bitmap image;
	// private Bitmap thumbnail;
	private static int thumbnailWidth = 100;
	private static int thumbnailHeight = 100;
	// private static Bitmap defaultImage;
	private File imageFile;
	private static Bitmap defaultImage;
	private Bitmap _bitmap;

	protected void finalize() throws Throwable {
		if (this._bitmap != null) {
			this._bitmap.recycle();
		}
	};

	/**
	 * loads default image and create default thumbnail. They are used when no
	 * image file is provided.
	 */
	private static Bitmap _getDefaultImage(Context context) {
		if (DirectoryImage.defaultImage != null)
			return DirectoryImage.defaultImage;
		Resources resources = context.getResources();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPurgeable = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		DirectoryImage.defaultImage = BitmapFactory.decodeResource(resources,
				R.drawable.unknown, options);
		// if (default_image == null)
		// throw new Exception("Can't load defaut image.");
		// DirectoryImage.defaultImage = default_image;
		return DirectoryImage.defaultImage;
	}// getDefaultImage

	// public DirectoryImage(File directory) {
	// Classifier classifier = new Classifier(directory);
	//
	// if (classifier.getImageFiles().size() > 0) {
	// File f = classifier.getImageFiles().get(0);
	// Log.v(new Throwable(),
	// "Image file was found, " + f.getAbsolutePath());
	// this.imageFile = f;
	// }// if
	// }// a constructor

	public DirectoryImage(ContentUnit content_unit) {
		if (this._bitmap != null) {
			this._bitmap.recycle();
			this._bitmap = null;
		}
		this.imageFile = content_unit.getImageFile();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(this.imageFile.getAbsolutePath(), options);
		int denominator = Math.min(options.outWidth / 1024,
				options.outHeight / 1024) + 1;
		options = new BitmapFactory.Options();
		options.inPurgeable = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inSampleSize = denominator;
		this._bitmap = BitmapFactory.decodeFile(this.imageFile.getPath(),
				options);
	}// a constructor

	public static Bitmap ResizeBitmap(Bitmap original_bitmap, int width,
			int height) {
		int original_height = original_bitmap.getHeight();
		int original_width = original_bitmap.getWidth();
		float height_scale = height / (float) original_height;
		float width_scale = width / (float) original_width;
		Matrix resize_matrix = new Matrix();
		resize_matrix.postScale(width_scale, height_scale);
		Bitmap resized_bitmap = Bitmap.createBitmap(original_bitmap, 0, 0,
				original_width, original_height, resize_matrix, true);
		return resized_bitmap;
	}// ResizeBitmap

	// public Bitmap getImage(Context context) throws Exception {
	//
	// if (this.imageFile != null) {
	// BitmapFactory.Options options = new BitmapFactory.Options();
	// options.inPurgeable = true;
	// options.inPreferredConfig = Bitmap.Config.RGB_565;
	// Bitmap b = BitmapFactory.decodeFile(this.imageFile.getPath(),
	// options);
	// if (b != null) {
	// return b;
	// }
	// // if (defaultImage == null || defaultThumbnail == null) {
	// // throw new DirectoryImageException("Can't load image "
	// // + image_file.getPath()
	// // + " and default image is not provided.");
	// // }
	// }
	// Log.v(new Throwable(), "Failed to decode " + this.imageFile.getPath());
	// return _getDefaultImage(context);
	// }// getImage

	public Bitmap getBitmap(Context context) {
		if (this._bitmap == null)
			return DirectoryImage._getDefaultImage(context);
		return this._bitmap;
	}

	/**
	 * @return the thumbnail
	 * @throws Exception
	 */
	public Bitmap getThumbnail(Context context) throws Exception {
		Bitmap b = getBitmap(context);
		Bitmap t = ResizeBitmap(b, thumbnailWidth, thumbnailHeight);
		return t;
	}// getThumbnail

	/**
	 * @param thumbnailWidth
	 *            the thumbnailWidth to set
	 */
	public static void setThumbnailWidth(int thumbnailWidth) {
		DirectoryImage.thumbnailWidth = thumbnailWidth;
	}

	/**
	 * @param thumbnailHeight
	 *            the thumbnailHeight to set
	 */
	public static void setThumbnailHeight(int thumbnailHeight) {
		DirectoryImage.thumbnailHeight = thumbnailHeight;
	}
}// DirectoryImage
