package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Classifier;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class DirectoryImage {

	// private Bitmap image;
	// private Bitmap thumbnail;
	private static int thumbnailWidth = 100;
	private static int thumbnailHeight = 100;
	private static Bitmap defaultImage;
	private File imageFile;

	/**
	 * loads default image and create default thumbnail. They are used when no
	 * image file is provided.
	 * 
	 * @param context
	 * @param drawable_resource_id
	 *            R.drawable.something
	 * @throws DirectoryImageException
	 */
	public static void SetDefaultImage(Context context, int drawable_resource_id)
			throws DirectoryImageException {
		Resources resources = context.getResources();
		defaultImage = BitmapFactory.decodeResource(resources,
				drawable_resource_id);
		if (defaultImage == null) {
			throw new DirectoryImageException("Can't load defaut image.");
		}// if
	}// SetDefaultImage

	/**
	 * @param directory
	 */
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
	 */
	public Bitmap getImage() {

		if (this.imageFile != null) {
			Bitmap b = BitmapFactory.decodeFile(this.imageFile.getPath());
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
				"Default image is used because image file was not found.");
		return this.defaultImage;
	}// getImage

	/**
	 * @return the thumbnail
	 */
	public Bitmap getThumbnail() {
		Bitmap b = getImage();
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
