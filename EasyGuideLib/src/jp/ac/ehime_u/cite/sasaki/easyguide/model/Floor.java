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
public class Floor extends ArrayList<Room> {
	private static final String floorImageName = "floor.png";
	private DirectoryName floorDirectoryName;
	private DirectoryImage floorImage;
	private File floorDirectory;

	/**
	 * @param floor_directory
	 */
	public Floor(File floor_directory) {
		this.floorDirectory = floor_directory;
		this.floorDirectoryName = new DirectoryName(floor_directory.getName());
		this.floorImage = new DirectoryImage(floorDirectory, floorImageName);
		Log.v(this.getClass().getSimpleName(), "Scanning room directories in "
				+ this.floorDirectory);
		for (File room_directory : this.floorDirectory.listFiles()) {
			Log.v(this.getClass().getSimpleName(), "Room directory "
					+ room_directory.getAbsolutePath() + " was found.");
			this.add(new Room(room_directory));
		}// for
	}// a constructor

	@SuppressWarnings("javadoc")
	public Bitmap getFloorImage() {
		return floorImage.getImage();
	}

	@SuppressWarnings("javadoc")
	public Bitmap getFloorThumbnail() {
		return floorImage.getThumbnail();
	}

	@SuppressWarnings("javadoc")
	public int getFloorNumber() {
		return floorDirectoryName.getNumber();
	}

	@SuppressWarnings("javadoc")
	public String getFloorName() {
		return floorDirectoryName.getName();
	}

	@SuppressWarnings("javadoc")
	public int getFloorX() {
		return floorDirectoryName.getX();
	}

	@SuppressWarnings("javadoc")
	public int getFloorY() {
		return floorDirectoryName.getY();
	}

}
