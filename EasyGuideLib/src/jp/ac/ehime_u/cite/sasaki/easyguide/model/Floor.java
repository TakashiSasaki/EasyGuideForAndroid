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
public class Floor extends ArrayList<Room> {
	private static final String floorImageName = "floor.png";
	private Bitmap floorImage;
	private int floorNumber;
	private File floorDirectory;
	private String floorName;
	private int floorX, floorY;

	/**
	 * @param floor_directory
	 */
	public Floor(File floor_directory) {
		this.floorDirectory = floor_directory;
		DirectoryName directory_name = new DirectoryName(
				floor_directory.getName());
		this.floorName = directory_name.getName();
		this.floorNumber = directory_name.getNumber();
		this.floorX = directory_name.getX();
		this.floorY = directory_name.getY();
		File floor_image_file = new File(floor_directory, floorImageName);
		this.floorImage = BitmapFactory.decodeFile(floor_image_file.getPath());
		ScanRooms();
	}// Floor

	private void ScanRooms() {
		Log.v(this.getClass().getSimpleName(), "Scanning room directories in "
				+ this.floorDirectory);
		for (File room_directory : this.floorDirectory.listFiles()) {
			if (!room_directory.isDirectory()) {
				continue;
			}
			Log.v(this.getClass().getSimpleName(), "Room directory "
					+ room_directory.getAbsolutePath() + " was found.");
			try {
				this.add(new Room(room_directory));
			} catch (Exception e) {
				Log.v(this.getClass().getSimpleName(),
						"an exception was chatched while constructing Room object for "
								+ room_directory.getAbsolutePath());
			}// try
		}// for
	}// ScanRooms

	/**
	 * @return the floorImage
	 */
	public Bitmap getFloorImage() {
		return floorImage;
	}

	/**
	 * @return the floorNumber
	 */
	public int getFloorNumber() {
		return floorNumber;
	}

	/**
	 * @return the floorName
	 */
	public String getFloorName() {
		return floorName;
	}

	/**
	 * @return the floorX
	 */
	public int getFloorX() {
		return floorX;
	}

	/**
	 * @return the floorY
	 */
	public int getFloorY() {
		return floorY;
	}

}
