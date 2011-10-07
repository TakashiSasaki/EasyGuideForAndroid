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
public class Room extends ArrayList<Equipment> {
	// public Equipments equipments;
	private File roomDirectory;
	private String roomName;
	private int number;
	private static final String roomImageName = "room.png";
	private Bitmap roomImage;
	private int roomX, roomY;

	/**
	 * @param room_directory
	 */
	public Room(File room_directory) {
		this.roomDirectory = room_directory;
		DirectoryName directory_name = new DirectoryName(
				room_directory.getName());
		this.roomName = directory_name.getName();
		this.number = directory_name.getNumber();
		this.roomX = directory_name.getX();
		this.roomY = directory_name.getY();
		File room_image_file = new File(room_directory, roomImageName);
		this.roomImage = BitmapFactory.decodeFile(room_image_file.getPath());
		ScanRoomForEquipments();
	}// a constructor

	private void ScanRoomForEquipments() {
		Log.v(this.getClass().getSimpleName(),
				"Scanning equipment directories in " + this.roomDirectory);
		for (File equipment_directory : this.roomDirectory.listFiles()) {
			if (!equipment_directory.isDirectory()) {
				continue;
			}
			Log.v(this.getClass().getSimpleName(), "Equipment directory "
					+ equipment_directory.getAbsolutePath() + " was found.");
			try {
				this.add(new Equipment(equipment_directory));
			} catch (Exception e) {
				Log.v(this.getClass().getSimpleName(),
						"Exception catched while constructing an equipment on "
								+ equipment_directory.getAbsolutePath());
			}// try
		}// for
	}// ScanRoomForEquipments

	/**
	 * @return the roomName
	 */
	public String getRoomName() {
		return roomName;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @return the roomImage
	 */
	public Bitmap getRoomImage() {
		return roomImage;
	}

	/**
	 * @return the roomX
	 */
	public int getRoomX() {
		return roomX;
	}

	/**
	 * @return the roomY
	 */
	public int getRoomY() {
		return roomY;
	}
}
