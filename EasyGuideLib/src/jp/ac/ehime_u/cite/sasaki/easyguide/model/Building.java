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
	private int x;
	private int y;

	/**
	 * @param building_directory
	 */
	public Building(File building_directory) {
		super();
		this.buildingDirectory = building_directory;
		DirectoryName directory_name = new DirectoryName(
				building_directory.getName());
		this.buildingNumber = directory_name.getNumber();
		this.buildingName = directory_name.getName();
		this.x = directory_name.getX();
		this.x = directory_name.getY();
		File building_image_file = new File(building_directory,
				buildingImageName);
		this.buildingImage = BitmapFactory.decodeFile(building_image_file
				.getPath());
		ScanFloors();
	}

	private void ScanFloors() {
		Log.v(this.getClass().getSimpleName(), "Scanning floor directories in "
				+ this.buildingDirectory);
		for (File floor_directory : this.buildingDirectory.listFiles()) {
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
			}// try
		}// for
	}// ScanFloors

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

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

}
