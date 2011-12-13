package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.io.FileFilter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class DirectoryImage {

	private Bitmap image;
	private Bitmap thumbnail;
	private static int thumbnailWidth = 100;
	private static int thumbnailHeight = 100;
	private static Bitmap defaultImage;
	private static Bitmap defaultThumbnail;

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
		defaultThumbnail = ResizeBitmap(defaultImage, thumbnailWidth,
				thumbnailHeight);
		if (defaultThumbnail == null) {
			throw new DirectoryImageException(
					"Can't resize default image for thumbnail.");
		}// if
	}// SetDefaultImage

	/**
	 * @param directory
	 * @param file_name
	 * @throws DirectoryImageException
	 */
	public DirectoryImage(File directory, String file_name) {
		File image_file = new File(directory, file_name);
		if (!image_file.exists()) {
			image_file = new File(directory, "index.png");
		} else if (!image_file.exists()) {
			image_file = new File(directory, "index.jpg");
		} else if (!image_file.exists()) {
			image_file = new File(directory, "index.gif");
		} else if (!image_file.exists()) {
			for (File file : directory.listFiles(new FileFilter() {
				@Override
				public boolean accept(File arg0) {
					return arg0.getName().matches(
							"(\\.png^|\\.gif^|\\.jpg^|\\.jpeg^)");
				}// accept
			})) {
				image_file = file;
			}// for
		} else {
			Log.v(this.getClass().getSimpleName(), "No image on " + directory);
			this.image = DirectoryImage.defaultImage;
			this.thumbnail = DirectoryImage.defaultThumbnail;
			return;
		}// if

		Log.v(this.getClass().getSimpleName(),
				"Loading image " + image_file.getPath());
		this.image = BitmapFactory.decodeFile(image_file.getPath());
		if (this.image == null) {
			// if (defaultImage == null || defaultThumbnail == null) {
			// throw new DirectoryImageException("Can't load image "
			// + image_file.getPath()
			// + " and default image is not provided.");
			// }
			this.image = defaultImage;
			this.thumbnail = defaultThumbnail;
		} else {
			this.thumbnail = ResizeBitmap(this.image, thumbnailWidth,
					thumbnailHeight);
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
		return this.image;
	}

	/**
	 * @return the thumbnail
	 */
	public Bitmap getThumbnail() {
		return this.thumbnail;
	}

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
}
