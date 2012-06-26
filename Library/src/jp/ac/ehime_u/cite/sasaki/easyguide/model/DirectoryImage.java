package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;

import com.gmail.takashi316.lib.android.graphics.BitmapFactory;

import jp.ac.ehime_u.cite.sasaki.easyguide.R;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Classifier;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;

import android.content.Context;
import android.graphics.Bitmap;

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
	 * 
	 * @param context
	 * @param drawable_resource_id
	 *            R.drawable.something
	 */
	private static Bitmap getDefaultImage(Context context) throws Exception {
		BitmapFactory bitmap_factory = new BitmapFactory();
		bitmap_factory.loadResource(context, R.drawable.unknown);
		if (bitmap_factory.get() == null)
			throw new RuntimeException("Can't load defaut image.");
		return bitmap_factory.get();
	}// getDefaultImage

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
	 * @return the image
	 * @throws Exception
	 */
	public Bitmap getImage(Context context) throws Exception {

		BitmapFactory bitmap_factory = new BitmapFactory();
		if (this.imageFile != null) {
			bitmap_factory.loadFile(this.imageFile);
			if (bitmap_factory.get() != null) {
				return bitmap_factory.get();
			}
		}
		Log.v(new Throwable(), "Failed to decode " + this.imageFile.getPath());
		return getDefaultImage(context);
	}// getImage

	/**
	 * @return the thumbnail
	 * @throws Exception
	 */
	public Bitmap getThumbnail(Context context) throws Exception {
		Bitmap b = getImage(context);
		BitmapFactory bitmap_factory = new BitmapFactory();
		bitmap_factory.loadBitmap(b);
		bitmap_factory.resize(thumbnailWidth, thumbnailHeight);
		return bitmap_factory.get();
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
