package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
		File facility_image_file = new File(facility_directory,
				Facility.facilityImageName);
		this.facilityBitmap = BitmapFactory.decodeFile(facility_image_file
				.getPath());
		ScanBuildings();
	}

	private void ScanBuildings() {
		for (File building_directory : this.facilityDirectory.listFiles()) {
			if (!building_directory.isDirectory()) {
				continue;
			}
			this.add(new Building(building_directory));

		}
	}

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

	
}
