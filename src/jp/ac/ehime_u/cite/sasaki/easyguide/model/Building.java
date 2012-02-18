package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;

import jp.ac.ehime_u.cite.sasaki.easyguide.exception.ItemNotFoundException;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class Building extends ItemBase<Floor> {

	// private static final String buildingImageName = "building.png";
	// private File buildingDirectory;
	// private DirectoryName buildingDirectoryName;
	// private DirectoryImage buildingDirectoryImage;

	// @Override
	// public String toString() {
	// if (this.isEmpty())
	// return "";
	// return this.buildingDirectoryName.getName();
	// }// toString

	/**
	 * @param building_directory
	 */
	public Building(File building_directory) {
		super(building_directory);
		// this.buildingDirectory = building_directory;
		// this.buildingDirectoryName = new DirectoryName(
		// building_directory.getName());
		// this.buildingDirectoryImage = new
		// DirectoryImage(this.buildingDirectory);
		this.EnumerateFloors();
	}// a constructor

	private Building() {
		super();
	}
	
	static public Building getDummy() {
		return new Building();
	}

	// public boolean isEmpty() {
	// return this.buildingDirectory == null
	// || this.buildingDirectoryName == null;
	// }

	@SuppressWarnings("javadoc")
	public void EnumerateFloors() {
		for (File floor_directory : this.listFiles()) {
			if (floor_directory.isDirectory())
				this.add(new Floor(floor_directory));
		}// for
		this.sortByIndex();
	}// EnumerateFloors

	public Floor getFloor(int index) throws ItemNotFoundException {
		return (Floor) this.getByIndex(index);
	}

	public Floor getFloor(String title) throws ItemNotFoundException {
		return (Floor) this.getByTitle(title);
	}
	// public void SortByFloorNumber() {
	// Collections.sort(this, new Comparator<Floor>() {
	// @Override
	// public int compare(Floor arg0, Floor arg1) {
	// return arg0.getFloorIndex() - arg1.getFloorIndex();
	// }// compare
	// });// sort
	// }// Sort

	// @SuppressWarnings("javadoc")
	// public int getBuildingIndex() {
	// return this.buildingDirectoryName.getNumber();
	// }

	// @SuppressWarnings("javadoc")
	// public String getBuildingName() {
	// return this.buildingDirectoryName.getName();
	// }

	// @SuppressWarnings("javadoc")
	// public DirectoryName getBuildingDirectoryName() {
	// return this.buildingDirectoryName;
	// }

	// @SuppressWarnings("javadoc")
	// public Bitmap getBuildingImage() {
	// return this.buildingDirectoryImage.getImage();
	// }

	// @SuppressWarnings("javadoc")
	// public Bitmap getBuildingThumbnail() {
	// return this.buildingDirectoryImage.getThumbnail();
	// }

	// @SuppressWarnings("javadoc")
	// public int getBuildingX() {
	// return this.buildingDirectoryName.getX();
	// }

	// @SuppressWarnings("javadoc")
	// public int getBuildingY() {
	// return this.buildingDirectoryName.getY();
	// }

	// @SuppressWarnings("javadoc")
	// public File getBuildingDirectory() {
	// return this.buildingDirectory;
	// }

	// /**
	// * @param floor_name
	// * @return Floor object
	// */
	// public Floor GetFloor(String floor_name) {
	// for (Floor floor : this) {
	// if (floor.toString().equals(floor_name)) {
	// return floor;
	// }
	// }// for
	// throw new NotFoundException("Floor " + floor_name
	// + " not found in building " + this.toString());
	// }// GetFloor

	// /**
	// * @param number
	// * @return Floor object
	// */
	// public Floor GetFloor(int number) {
	// for (Floor floor : this) {
	// if (floor.getFloorIndex() == number) {
	// return floor;
	// }// if
	// }// for
	// throw new NotFoundException("Floor " + number
	// + " not found in building " + this.toString());
	// }// GetFloor

	// public static Building getEmptyBuilding() {
	// return new Building();
	// }

	// public Floor getFloorByIndex(int fi, Floor default_floor) {
	// return getFloorByIndex(fi);
	// }

	// public Floor getFloorByIndex(int fi) throws IndexOutOfBoundsException {
	// for (Floor f : this) {
	// if (f.getFloorIndex() == fi) {
	// return f;
	// }
	// }
	// throw new IndexOutOfBoundsException("Floor " + fi + " not found in "
	// + this.getBuildingName());
	// }

	// @Override
	// @Deprecated
	// public Floor get(int index) {
	// throw new Error("Building#get is deprecated");
	// }
}// Building
