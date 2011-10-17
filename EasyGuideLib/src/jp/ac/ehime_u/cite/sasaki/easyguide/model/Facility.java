package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
@SuppressWarnings("serial")
public class Facility extends ArrayList<Building> {
	private static final double maxDistance = 3000.0 * 3000.0;
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
			if (building_directory.isDirectory())
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

	@SuppressWarnings("javadoc")
	public File getFacilityDirectory() {
		return this.facilityDirectory;
	}

	public Building GetNearestBuilding(ImageView image_view,
			MotionEvent motion_event) {
		Building candidate_building = null;
		double candidate_distance = maxDistance;
		DistanceCalculator distance_calculator = new DistanceCalculator(
				image_view);
		for (Building building : this) {
			double distance = distance_calculator.GetDistanceBetween(
					motion_event, building.getBuildingX(),
					building.getBuildingY());
			if (distance < candidate_distance) {
				candidate_distance = distance;
				candidate_building = building;
			}// if
		}// for
		return candidate_building;
	}// GetNearestBuilding

	public Building GetBuilding(String building_name) {
		for (Building building : this) {
			if (building.getBuildingName().equals(building_name)) {
				return building;
			}
		}// for
		return null;
	}// GetBuilding

}// Facility
