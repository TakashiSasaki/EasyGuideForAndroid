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

	/**
	 * loads default image and create default thumbnail. They are used when no
	 * image file is provided.
	 */
	public static Bitmap getDefaultImage(Context context) throws Exception {
		Resources resources = context.getResources();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPurgeable = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		Bitmap default_image = BitmapFactory.decodeResource(resources,
				R.drawable.unknown, options);
		if (default_image == null)
			throw new Exception("Can't load defaut image.");
		return default_image;
	}// getDefaultImage

	public DirectoryImage(File directory) {
		Classifier classifier = new Classifier(directory);

		if (classifier.getImageFiles().size() > 0) {
			File f = classifier.getImageFiles().get(0);
			Log.v(new Throwable(),
					"Image file was found, " + f.getAbsolutePath());
			this.imageFile = f;
		}// if
	}// a constructor

	/**
	 * @param original_bitmap
	 * @param width
	 * @param height
	 * @return resized bitmap
	 */
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

	/**
	 * @return the image
	 * @throws Exception
	 */
	public Bitmap getImage(Context context) throws Exception {

		if (this.imageFile != null) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPurgeable = true;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			Bitmap b = BitmapFactory.decodeFile(this.imageFile.getPath(),options);
			if (b != null) {
				return b;
			}
			// if (defaultImage == null || defaultThumbnail == null) {
			// throw new DirectoryImageException("Can't load image "
			// + image_file.getPath()
			// + " and default image is not provided.");
			// }
		}
		Log.v(new Throwable(),
				"Failed to decode " + this.imageFile.getPath());
		return getDefaultImage(context);
	}// getImage

	/**
	 * @return the thumbnail
	 * @throws Exception 
	 */
	public Bitmap getThumbnail(Context context) throws Exception {
		Bitmap b = getImage(context);
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
