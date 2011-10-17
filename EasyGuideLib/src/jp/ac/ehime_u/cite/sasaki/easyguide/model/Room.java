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
public class Room extends ArrayList<Equipment> {
	private static final String roomImageName = "room.png";
	private File roomDirectory;
	private DirectoryName roomDirectoryName;
	private DirectoryImage roomImage;

	/**
	 * @param room_directory
	 */
	public Room(File room_directory) {
		this.roomDirectory = room_directory;
		this.roomDirectoryName = new DirectoryName(room_directory.getName());
		this.roomImage = new DirectoryImage(room_directory, Room.roomImageName);
		Log.v(this.getClass().getSimpleName(),
				"Scanning equipment directories in " + this.roomDirectory);
		for (File equipment_directory : this.roomDirectory.listFiles()) {
			if (!equipment_directory.isDirectory())
				continue;
			Log.v(this.getClass().getSimpleName(), "Equipment directory "
					+ equipment_directory.getAbsolutePath() + " was found.");
			this.add(new Equipment(equipment_directory));
		}// for
	}// a constructor

	@SuppressWarnings("javadoc")
	public String getRoomName() {
		return this.roomDirectoryName.getName();
	}

	@SuppressWarnings("javadoc")
	public int getRoomNumber() {
		return this.roomDirectoryName.getNumber();
	}

	@SuppressWarnings("javadoc")
	public Bitmap getRoomImage() {
		return this.roomImage.getImage();
	}

	@SuppressWarnings("javadoc")
	public Bitmap getRoomThumbnail() {
		return this.roomImage.getThumbnail();
	}

	@SuppressWarnings("javadoc")
	public int getRoomX() {
		return this.roomDirectoryName.getX();
	}

	@SuppressWarnings("javadoc")
	public int getRoomY() {
		return this.roomDirectoryName.getY();
	}

	@SuppressWarnings("javadoc")
	public File getRoomDirectory() {
		return this.roomDirectory;
	}

	public Equipment GetEquipment(String equipment_name) {
		for (Equipment equipment : this) {
			if (equipment.getEquipmentName().equals(equipment_name)) {
				return equipment;
			}
		}// for
		return null;
	}// GetEquipment
}// Equipment
