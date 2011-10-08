package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
@SuppressWarnings("serial")
public class Equipment extends ArrayList<Panel> {

	private static final String equipmentImageName = "equipment.png";
	private static final int equipmentThumbnailHeight = 50;
	private static final int equipmentThumbnailWidth = 50;
	private File equipmentDirectory;
	private DirectoryName equipmentDirectoryName;
	private DirectoryImage equipmentImage;

	/**
	 * @param equipment_directory
	 */
	public Equipment(File equipment_directory) {
		Log.v(this.getClass().getSimpleName(), "Constructing equipment "
				+ equipment_directory.getAbsolutePath());
		this.equipmentDirectory = equipment_directory;
		this.equipmentDirectoryName = new DirectoryName(
				equipment_directory.getName());
		this.equipmentImage = new DirectoryImage(equipment_directory,
				equipmentImageName, equipmentThumbnailWidth,
				equipmentThumbnailHeight);
		for (File panel_directory : this.equipmentDirectory.listFiles()) {
			this.add(new Panel(panel_directory));
		}// for
	}// a constructor

	/**
	 * @return the equipmentDirectory
	 */
	public File getEquipmentDirectory() {
		return equipmentDirectory;
	}

	/**
	 * @return the equipmentDirectoryName
	 */
	public DirectoryName getEquipmentDirectoryName() {
		return equipmentDirectoryName;
	}

	/**
	 * @return the equipmentImage
	 */
	public DirectoryImage getEquipmentImage() {
		return equipmentImage;
	}

}
