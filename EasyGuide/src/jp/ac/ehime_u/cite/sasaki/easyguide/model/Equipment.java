package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Equipment {

	public static final String THUMBNAIL_NAME = "thumbnail.png";
	public Bitmap thumbnail;

	public static final String IMAGE_NAME = "image.png";
	public Bitmap image;

	public Equipment(File equipment_directory) {
		File thumbnail_file = new File(equipment_directory, THUMBNAIL_NAME);
		this.thumbnail = BitmapFactory.decodeFile(thumbnail_file.getPath());
		File image_file = new File(equipment_directory, IMAGE_NAME);
		this.image = BitmapFactory.decodeFile(image_file.getPath());
	}
}
