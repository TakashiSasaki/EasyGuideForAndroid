package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
@SuppressWarnings("serial")
public class Building extends ArrayList<Floor> {

	private static final String buildingImageName = "building.png";
	private File buildingDirectory;
	private DirectoryName buildingDirectoryName;
	private DirectoryImage buildingDirectoryImage;

	@Override
	public String toString() {
		if (this.isEmpty())
			return "";
		return this.buildingDirectoryName.getName();
	}// toString

	/**
	 * @param building_directory
	 */
	public Building(File building_directory) {
		super();
		this.buildingDirectory = building_directory;
		this.buildingDirectoryName = new DirectoryName(
				building_directory.getName());
		this.buildingDirectoryImage = new DirectoryImage(
				this.buildingDirectory, buildingImageName);
		this.EnumerateFloors();
	}// a constructor

	Building() {
		super();
	}

	public boolean isEmpty() {
		return this.buildingDirectory == null || this.buildingDirectoryName == null;
	}

	@SuppressWarnings("javadoc")
	public void EnumerateFloors() {
		for (File floor_directory : this.buildingDirectory.listFiles()) {
			if (floor_directory.isDirectory())
				this.add(new Floor(floor_directory));
		}// for
		this.SortByFloorNumber();
	}// EnumerateFloors

	public void SortByFloorNumber() {
		Collections.sort(this, new Comparator<Floor>() {
			@Override
			public int compare(Floor arg0, Floor arg1) {
				return arg0.getFloorIndex() - arg1.getFloorIndex();
			}// compare
		});// sort
	}// Sort

	@SuppressWarnings("javadoc")
	public int getBuildingIndex() {
		return this.buildingDirectoryName.getNumber();
	}

	@SuppressWarnings("javadoc")
	public String getBuildingName() {
		return this.buildingDirectoryName.getName();
	}

	@SuppressWarnings("javadoc")
	public DirectoryName getBuildingDirectoryName() {
		return this.buildingDirectoryName;
	}

	@SuppressWarnings("javadoc")
	public Bitmap getBuildingImage() {
		return this.buildingDirectoryImage.getImage();
	}

	@SuppressWarnings("javadoc")
	public Bitmap getBuildingThumbnail() {
		return this.buildingDirectoryImage.getThumbnail();
	}

	@SuppressWarnings("javadoc")
	public int getBuildingX() {
		return this.buildingDirectoryName.getX();
	}

	@SuppressWarnings("javadoc")
	public int getBuildingY() {
		return this.buildingDirectoryName.getY();
	}

	@SuppressWarnings("javadoc")
	public File getBuildingDirectory() {
		return this.buildingDirectory;
	}

	/**
	 * @param floor_name
	 * @return Floor object
	 */
	public Floor GetFloor(String floor_name) {
		for (Floor floor : this) {
			if (floor.toString().equals(floor_name)) {
				return floor;
			}
		}// for
		throw new NotFoundException("Floor " + floor_name
				+ " not found in building " + this.toString());
	}// GetFloor

	/**
	 * @param number
	 * @return Floor object
	 */
	public Floor GetFloor(int number) {
		for (Floor floor : this) {
			if (floor.getFloorIndex() == number) {
				return floor;
			}// if
		}// for
		throw new NotFoundException("Floor " + number
				+ " not found in building " + this.toString());
	}// GetFloor

	public static Building getEmptyBuilding() {
		return new Building();
	}

	public Floor getFloorByIndex(int fi) {
		for (Floor f : this) {
			if (f.getFloorIndex() == fi) {
				return f;
			}
		}
		return null;
	}

	// @Override
	// @Deprecated
	// public Floor get(int index) {
	// throw new Error("Building#get is deprecated");
	// }
}// Building
