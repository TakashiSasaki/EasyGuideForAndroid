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
public class Facility extends ArrayList<Building> {

	private static final String facilityImageName = "facility.png";
	private File facilityDirectory;
	private int facilityNumber;
	private String facilityName;
	private Bitmap facilityBitmap;
	private int facilityX, facilityY;

	/**
	 * @param facility_directory
	 */
	public Facility(File facility_directory) {
		super();
		this.facilityDirectory = facility_directory;
		// String building_directory_path = building_directory.getPath();
		DirectoryName directory_name = new DirectoryName(
				facility_directory.getName());
		this.facilityNumber = directory_name.getNumber();
		this.facilityName = directory_name.getName();
		this.facilityX = directory_name.getX();
		this.facilityY = directory_name.getY();
		File facility_image_file = new File(facility_directory,
				Facility.facilityImageName);
		this.facilityBitmap = BitmapFactory.decodeFile(facility_image_file
				.getPath());
		ScanBuildings();
	}

	private void ScanBuildings() {
		Log.v(this.getClass().getSimpleName(),
				"Scanning building directories in "
						+ this.facilityDirectory.getAbsolutePath());
		for (File building_directory : this.facilityDirectory.listFiles()) {
			if (!building_directory.isDirectory()) {
				continue;
			}
			Log.v(this.getClass().getSimpleName(), "Building directory "
					+ building_directory.getAbsolutePath() + " was found.");
			try {
				Building building = new Building(building_directory);
				assert (building != null);
				this.add(building);
			} catch (Exception e) {
				Log.v(this.getClass().getSimpleName(),
						"an exception was catched while constructing a Building object for "
								+ building_directory.getAbsolutePath());
			}// try
		}// for
	}// ScanBuildings

	/**
	 * @return the facilityNumber
	 */
	public int getFacilityNumber() {
		return facilityNumber;
	}

	/**
	 * @return the facilityName
	 */
	public String getFacilityName() {
		return facilityName;
	}

	/**
	 * @return the facilityBitmap
	 */
	public Bitmap getFacilityBitmap() {
		return facilityBitmap;
	}

	/**
	 * @return the facilityX
	 */
	public int getFacilityX() {
		return facilityX;
	}

	/**
	 * @return the facilityY
	 */
	public int getFacilityY() {
		return facilityY;
	}

}
