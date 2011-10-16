package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.util.ArrayList;

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
		for (File floor_directory : this.buildingDirectory.listFiles()) {
			this.add(new Floor(floor_directory));
		}// for
	}// a constructor

	@SuppressWarnings("javadoc")
	public int getBuildingNumber() {
		return buildingDirectoryName.getNumber();
	}

	@SuppressWarnings("javadoc")
	public String getBuildingName() {
		return buildingDirectoryName.getName();
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

}
