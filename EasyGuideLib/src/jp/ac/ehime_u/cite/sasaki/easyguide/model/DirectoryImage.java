package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class DirectoryImage {

	private Bitmap image;
	private Bitmap thumbnail;

	/**
	 * @param directory
	 * @param file_name
	 * @param thumbnail_x
	 * @param thumbnail_y
	 */
	public DirectoryImage(File directory, String file_name, int thumbnail_x,
			int thumbnail_y) {
		File image_file = new File(directory, file_name);
		this.image = BitmapFactory.decodeFile(image_file.getPath());
		this.thumbnail = ResizeBitmap(this.image, thumbnail_x, thumbnail_y);
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
		return image;
	}

	/**
	 * @return the thumbnail
	 */
	public Bitmap getThumbnail() {
		return thumbnail;
	}
}
