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
public class Building extends ArrayList<Floor> {

	private static final String buildingImageName = "building.png";
	private File buildingDirectory;
	private int buildingNumber;
	private String buildingName;
	private Bitmap buildingImage;

	/**
	 * @param building_directory
	 */
	public Building(File building_directory) {
		super();
		this.buildingDirectory = building_directory;
		// String building_directory_path = building_directory.getPath();
		String building_directory_name = building_directory.getName();
		String[] parts = building_directory_name.split("[ ]+");
		this.buildingNumber = Integer.parseInt(parts[0]);
		this.buildingName = parts[1];
		assert(parts[1] != null);
		File building_image_file = new File(building_directory,
				buildingImageName);
		this.buildingImage = BitmapFactory.decodeFile(building_image_file
				.getPath());
		ScanFloors();
	}

	private void ScanFloors() {
		Log.v(this.getClass().getSimpleName(), "Scanning floor directories in "
				+ this.buildingDirectory);
		File[] floor_directories = this.buildingDirectory.listFiles();
		for (int i = 0; i < floor_directories.length; ++i) {
			File floor_directory = floor_directories[i];
			if (!floor_directory.isDirectory()) {
				continue;
			}
			Log.v(this.getClass().getSimpleName(), "floor directory "
					+ floor_directory.getAbsolutePath() + " was found.");
			try {
				this.add(new Floor(floor_directory));
			} catch (Exception e) {
				Log.v(this.getClass().getSimpleName(),
						"an exception was chatched while constructing a Floor object for "
								+ floor_directory.getAbsolutePath());
			}
		}
	}

	/**
	 * @return the buildingNumber
	 */
	public int getBuildingNumber() {
		return buildingNumber;
	}

	/**
	 * @return the buildingName
	 */
	public String getBuildingName() {
		return buildingName;
	}

	/**
	 * @return the buildingImage
	 */
	public Bitmap getBuildingImage() {
		return buildingImage;
	}

}
