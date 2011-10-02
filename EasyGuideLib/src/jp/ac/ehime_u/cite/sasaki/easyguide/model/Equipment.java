package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class Equipment {

	private static final String equipmentImageName = "equipment.png";
	private static final int equipmentThumbnailHeight = 50;
	private static final int equipmentThumbnailWidth = 50;
	private Bitmap equipmentImage;
	private Bitmap equipmentThumbnail;

	/**
	 * @param equipment_directory
	 */
	public Equipment(File equipment_directory) {
		File equipment_image_file = new File(equipment_directory,
				Equipment.equipmentImageName);
		this.equipmentImage = BitmapFactory.decodeFile(equipment_image_file
				.getPath());
		this.equipmentThumbnail = Organization.ResizeBitmap(
				this.equipmentImage, Equipment.equipmentThumbnailWidth,
				Equipment.equipmentThumbnailHeight);
	}

	/**
	 * @return the equipmentThumbnail
	 */
	public Bitmap getEquipmentThumbnail() {
		return equipmentThumbnail;
	}

}
