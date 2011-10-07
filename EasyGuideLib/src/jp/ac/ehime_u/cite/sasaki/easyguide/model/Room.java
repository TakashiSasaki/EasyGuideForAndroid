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

	/**
	 * @param room_directory
	 */
	public Room(File room_directory) {
		this.roomDirectory = room_directory;
		String room_directory_name = room_directory.getName();
		String[] parts = room_directory_name.split("[ ]+");
		this.number = Integer.parseInt(parts[0]);
		this.roomName = parts[1];
		File room_image_file = new File(room_directory, roomImageName);
		this.roomImage = BitmapFactory.decodeFile(room_image_file.getPath());
		// this.equipments = new Equipments(room_directory);
		ScanEquipments();
	}

	private void ScanEquipments() {
		Log.v(this.getClass().getSimpleName(),
				"Scanning equipment directories in " + this.roomDirectory);
		File[] equipment_directories = this.roomDirectory.listFiles();
		for (int i = 0; i < equipment_directories.length; ++i) {
			File equipment_directory = equipment_directories[i];
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
			}
		}
	}

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
}
