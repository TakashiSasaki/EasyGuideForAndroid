package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.util.ArrayList;

import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;

import android.graphics.Bitmap;

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
		Log.v(new Throwable(), "Scanning equipment directories in "
				+ this.roomDirectory);
		this.EnumerateEquipments();
	}// a constructor

	private Room() {
		super();
	}

	@SuppressWarnings("javadoc")
	public void EnumerateEquipments() {
		for (File equipment_directory : this.roomDirectory.listFiles()) {
			if (!equipment_directory.isDirectory())
				continue;
			Log.v(new Throwable(),
					"Equipment directory "
							+ equipment_directory.getAbsolutePath()
							+ " was found.");
			this.add(new Equipment(equipment_directory));
		}// for
	}// EnumerateEquipments

//	@SuppressWarnings("javadoc")
//	public String getRoomName() {
//		return this.roomDirectoryName.getName();
//	}

	@SuppressWarnings("javadoc")
	public DirectoryName getRoomDirectoryName() {
		return this.roomDirectoryName;
	}

	@Override
	public String toString() {
		if (this.isEmpty())
			return "";
		return this.roomDirectoryName.getName();
	}

	public int getRoomIndex() {
		return this.roomDirectoryName.getNumber();
	}

	public Bitmap getRoomImage() {
		return this.roomImage.getImage();
	}

	public Bitmap getRoomThumbnail() {
		return this.roomImage.getThumbnail();
	}

	public int getRoomX() {
		return this.roomDirectoryName.getX();
	}

	public int getRoomY() {
		return this.roomDirectoryName.getY();
	}

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

	public static Room getEmptyRoom() {
		return new Room();
	}
}// Equipment
