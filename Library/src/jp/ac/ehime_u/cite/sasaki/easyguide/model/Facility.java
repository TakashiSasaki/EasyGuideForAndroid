package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import jp.ac.ehime_u.cite.sasaki.easyguide.exception.ItemNotFoundException;

import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class Facility extends ItemBase<Facility, Building> {
	private static final double maxDistance = 3000.0 * 3000.0;

	// private static final String facilityImageName = "facility.png";
	// private File facilityDirectory;
	// private DirectoryName facilityDirectoryName;
	// private DirectoryImage facilityDirectoryImage;

	/**
	 * @param facility_directory
	 */
	public Facility(File facility_directory) {
		super(facility_directory);
		// this.facilityDirectory = facility_directory;
		// // String building_directory_path = building_directory.getPath();
		// this.facilityDirectoryName = new DirectoryName(
		// facility_directory.getName());
		// this.facilityDirectoryImage = new DirectoryImage(facilityDirectory);
		// Log.v(this.getClass().getSimpleName(),
		// "Scanning building directories in "
		// + this.facilityDirectory.getAbsolutePath());
		this.EnumerateBuildings();
		this.sortByIndex();
	}// a constructor

	private Facility() {
		super();
	}

	private static Facility theDummy = new Facility();

	public static Facility getDummy() {
		return theDummy;
	}

	// public static Facility GetEmptyFacility() {
	// return new Facility();
	// }

	// public boolean isEmpty() {
	// return facilityDirectory == null || facilityDirectoryName == null;
	// }

	public void EnumerateBuildings() {
		for (File building_directory : this.listFiles()) {
			if (building_directory.isDirectory())
				this.add(new Building(building_directory));
		}// for
	}// EnumerateBuildings

	// @Override
	// public String toString() {
	// if (this.isEmpty())
	// return "";
	// return this.getFacilityDirectoryName().getName();
	// }// toString

	// /**
	// * Sort buildings by building number.
	// */
	// public void SortByBuildingNumber() {
	// Collections.sort(this, new Comparator<Building>() {
	// @Override
	// public int compare(Building arg0, Building arg1) {
	// return arg0.getBuildingIndex() - arg1.getBuildingIndex();
	// }// compare
	// });// sort
	// }// SortByBuildingNumber

	// @SuppressWarnings("javadoc")
	// public int getFacilityIndex() {
	// return this.facilityDirectoryName.getNumber();
	// }
	//
	// @SuppressWarnings("javadoc")
	// public String getFacilityName() {
	// return this.facilityDirectoryName.getName();
	// }
	//
	// @SuppressWarnings("javadoc")
	// public DirectoryName getFacilityDirectoryName() {
	// return this.facilityDirectoryName;
	// }
	//
	// @SuppressWarnings("javadoc")
	// public int getFacilityX() {
	// return this.facilityDirectoryName.getX();
	// }
	//
	// @SuppressWarnings("javadoc")
	// public int getFacilityY() {
	// return this.facilityDirectoryName.getY();
	// }
	//
	// @SuppressWarnings("javadoc")
	// public Bitmap getFacilityImage() {
	// return this.facilityDirectoryImage.getImage();
	// }
	//
	// @SuppressWarnings("javadoc")
	// public Bitmap getFacilityThumbnail() {
	// return this.facilityDirectoryImage.getThumbnail();
	// }
	//
	// @SuppressWarnings("javadoc")
	// public File getFacilityDirectory() {
	// return this.facilityDirectory;
	// }
	//
	public Building GetNearestBuilding(ImageView image_view,
			MotionEvent motion_event) {
		Building candidate_building = null;
		double candidate_distance = maxDistance;
		DistanceCalculator distance_calculator = new DistanceCalculator(
				image_view);
		for (Building building : this) {
			double distance = distance_calculator.GetDistanceBetween(
					motion_event, building.getX(), building.getY());
			if (distance < candidate_distance) {
				candidate_distance = distance;
				candidate_building = building;
			}// if
		}// for
		return candidate_building;
	}// GetNearestBuilding

	public Building getBuilding(String title) throws ItemNotFoundException {
		return (Building) getByTitle(title);
	}

	public Building getBuilding(int index) throws ItemNotFoundException {
		return (Building) getByIndex(index);
	}

	// @SuppressWarnings("javadoc")
	// public Building GetBuilding(String building_name) {
	// for (Building building : this) {
	// if (building.getBuildingName().equals(building_name)) {
	// return building;
	// }
	// }// for
	// throw new NotFoundException("Bulding " + building_name
	// + " not found in the facility " + this.getFacilityName());
	// }// GetBuilding

	// @SuppressWarnings("javadoc")
	// public Building GetBuilding(int building_number) {
	// for (Building building : this) {
	// if (building.getBuildingIndex() == building_number) {
	// return building;
	// }
	// }
	// throw new NotFoundException("Bulding " + building_number
	// + " not found in the facility " + this.getFacilityName());
	// }// GetBuilding

	// @Override
	// @Deprecated
	// public Building get(int index) {
	// throw new Error("Facility#get is deprecated.");
	// }// get

	// public Building getBuildingByIndex(int bi) {
	// for (Building b : this) {
	// if (b.getBuildingIndex() == bi) {
	// return b;
	// }
	// }
	// return null;
	// }
}// Facility
