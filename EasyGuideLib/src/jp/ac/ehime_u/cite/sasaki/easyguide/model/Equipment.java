package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
@SuppressWarnings("serial")
public class Equipment extends ArrayList<Panel> {

	private static final String equipmentImageName = "equipment.png";
	private File equipmentDirectory;
	private DirectoryName equipmentDirectoryName;
	private DirectoryImage equipmentDirectoryImage;

	/**
	 * @param equipment_directory
	 */
	public Equipment(File equipment_directory) {
		Log.v(this.getClass().getSimpleName(), "Constructing equipment "
				+ equipment_directory.getAbsolutePath());
		this.equipmentDirectory = equipment_directory;
		this.equipmentDirectoryName = new DirectoryName(
				equipment_directory.getName());
		this.equipmentDirectoryImage = new DirectoryImage(equipment_directory,
				equipmentImageName);
		for (File panel_directory : this.equipmentDirectory.listFiles()) {
			this.add(new Panel(panel_directory));
		}// for
	}// a constructor

	@SuppressWarnings("javadoc")
	public File getEquipmentDirectory() {
		return equipmentDirectory;
	}

	@SuppressWarnings("javadoc")
	public String getEquipmentName() {
		return equipmentDirectoryName.getName();
	}

	@SuppressWarnings("javadoc")
	public int getEquipmentNumber() {
		return equipmentDirectoryName.getNumber();
	}

	@SuppressWarnings("javadoc")
	public int getEquipmentX() {
		return equipmentDirectoryName.getX();
	}

	@SuppressWarnings("javadoc")
	public int getEquipmentY() {
		return equipmentDirectoryName.getY();
	}

	@SuppressWarnings("javadoc")
	public Bitmap getEquipmentImage() {
		return equipmentDirectoryImage.getImage();
	}

	@SuppressWarnings("javadoc")
	public Bitmap getEquipmentThumbnail() {
		return equipmentDirectoryImage.getThumbnail();
	}

}
