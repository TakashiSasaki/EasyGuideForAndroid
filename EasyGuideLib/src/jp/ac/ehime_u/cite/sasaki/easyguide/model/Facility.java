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
public class Facility extends ArrayList<Building> {

	private static final String facilityImageName = "facility.png";
	private File facilityDirectory;
	private DirectoryName facilityDirectoryName;
	private DirectoryImage facilityDirectoryImage;

	/**
	 * @param facility_directory
	 */
	public Facility(File facility_directory) {
		super();
		this.facilityDirectory = facility_directory;
		// String building_directory_path = building_directory.getPath();
		this.facilityDirectoryName = new DirectoryName(
				facility_directory.getName());
		this.facilityDirectoryImage = new DirectoryImage(facilityDirectory,
				facilityImageName);
		Log.v(this.getClass().getSimpleName(),
				"Scanning building directories in "
						+ this.facilityDirectory.getAbsolutePath());
		for (File building_directory : this.facilityDirectory.listFiles()) {
			this.add(new Building(building_directory));
		}// for
	}// a constructor

	@SuppressWarnings("javadoc")
	public int getFacilityNumber() {
		return this.facilityDirectoryName.getNumber();
	}

	@SuppressWarnings("javadoc")
	public String getFacilityName() {
		return this.facilityDirectoryName.getName();
	}

	@SuppressWarnings("javadoc")
	public int getFacilityX() {
		return this.facilityDirectoryName.getX();
	}

	@SuppressWarnings("javadoc")
	public int getFacilityY() {
		return this.facilityDirectoryName.getY();
	}

	@SuppressWarnings("javadoc")
	public Bitmap getFacilityImage() {
		return this.facilityDirectoryImage.getImage();
	}

	@SuppressWarnings("javadoc")
	public Bitmap getFacilityThumbnail() {
		return this.facilityDirectoryImage.getThumbnail();
	}
}// Facility
