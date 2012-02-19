package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import jp.ac.ehime_u.cite.sasaki.easyguide.exception.ItemNotFoundException;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class Room extends ItemBase<Equipment> {
	// private File roomDirectory;
	// private DirectoryName roomDirectoryName;
	// private DirectoryImage roomImage;
	//

	private static Room theDummy = new Room();

	static public Room getDummy() {
		return theDummy;
	}

	private Room() {
		super();
	}

	/**
	 * @param room_directory
	 */
	public Room(File room_directory) {
		super(room_directory);
		// this.roomDirectory = room_directory;
		// this.roomDirectoryName = new DirectoryName(room_directory.getName());
		// this.roomImage = new DirectoryImage(room_directory);
		Log.v(new Throwable(),
				"Scanning equipment directories in " + this.getDirectory());
		this.EnumerateEquipments();
	}// a constructor

	@SuppressWarnings("javadoc")
	public void EnumerateEquipments() {
		for (File equipment_directory : this.listFiles()) {
			if (!equipment_directory.isDirectory())
				continue;
			Log.v(new Throwable(),
					"Equipment directory "
							+ equipment_directory.getAbsolutePath()
							+ " was found.");
			this.add(new Equipment(equipment_directory));
		}// for
	}// EnumerateEquipments

	public Equipment getEquipment(int index) throws ItemNotFoundException {
		return this.getByIndex(index);
	}

	public Equipment getEquipment(String title) throws ItemNotFoundException {
		return this.getByTitle(title);
	}
	// @SuppressWarnings("javadoc")
	// public String getRoomName() {
	// return this.roomDirectoryName.getName();
	// }

	// @SuppressWarnings("javadoc")
	// public DirectoryName getRoomDirectoryName() {
	// return this.roomDirectoryName;
	// }

	// public String getIndex() {
	// return this.roomDirectoryName.getName();
	// }

	// @Override
	// public String toString() {
	// if (this.isEmpty())
	// return "";
	// return this.roomDirectoryName.getName();
	// }

	// public int getRoomIndex() {
	// return this.roomDirectoryName.getNumber();
	// }

	// public Bitmap getRoomImage() {
	// return this.roomImage.getImage();
	// }

	// public Bitmap getRoomThumbnail() {
	// return this.roomImage.getThumbnail();
	// }

	// public int getRoomX() {
	// return this.roomDirectoryName.getX();
	// }

	// public int getRoomY() {
	// return this.roomDirectoryName.getY();
	// }

	// public File getRoomDirectory() {
	// return this.roomDirectory;
	// }

	// public Equipment getEquipmentByIndex(int equipment_index) {
	// for (Equipment e : this) {
	// if (e.getIndex() == equipment_index) {
	// return e;
	// }
	// }
	// return null;
	// }// getEquipmentByIndex

	// public Equipment GetEquipment(String equipment_name) {
	// for (Equipment equipment : this) {
	// if (equipment.getTitle().equals(equipment_name)) {
	// return equipment;
	// }
	// }// for
	// return null;
	// }// GetEquipment

	// public static Room getEmptyRoom() {
	// return new Room();
	// }
}// Equipment
