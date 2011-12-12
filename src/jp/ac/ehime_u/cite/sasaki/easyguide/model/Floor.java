package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import android.content.res.Resources.NotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
		super();
		this.floorDirectory = floor_directory;
		this.floorDirectoryName = new DirectoryName(floor_directory.getName());
		this.floorImage = new DirectoryImage(floorDirectory, floorImageName);
		Log.v(this.getClass().getSimpleName(), "Scanning room directories in "
				+ this.floorDirectory);
		for (File room_directory : this.floorDirectory.listFiles()) {
			if (!room_directory.isDirectory())
				continue;
			Log.v(this.getClass().getSimpleName(), "Room directory "
					+ room_directory.getAbsolutePath() + " was found.");
			this.add(new Room(room_directory));
		}// for
		this.Sort();
	}// a constructor

	@SuppressWarnings("javadoc")
	public void Sort() {
		Collections.sort(this, new Comparator<Room>() {
			@Override
			public int compare(Room arg0, Room arg1) {
				return arg0.getRoomNumber() - arg1.getRoomNumber();
			}// compare
		});// sort
	}// Sort

	@SuppressWarnings("javadoc")
	public Bitmap getFloorImage() {
		return this.floorImage.getImage();
	}

	@SuppressWarnings("javadoc")
	public Bitmap getFloorThumbnail() {
		return this.floorImage.getThumbnail();
	}

	@SuppressWarnings("javadoc")
	public int getFloorNumber() {
		return this.floorDirectoryName.getNumber();
	}

	@SuppressWarnings("javadoc")
	public String getFloorName() {
		return this.floorDirectoryName.getName();
	}

	@SuppressWarnings("javadoc")
	public int getFloorX() {
		return this.floorDirectoryName.getX();
	}

	@SuppressWarnings("javadoc")
	public int getFloorY() {
		return this.floorDirectoryName.getY();
	}

	@SuppressWarnings("javadoc")
	public File getFloorDirectory() {
		return this.floorDirectory;
	}

	@SuppressWarnings("javadoc")
	public DirectoryName getFloorDirectoryName() {
		return this.floorDirectoryName;
	}

	@SuppressWarnings("javadoc")
	public Room GetRoom(String room_name) {
		for (Room room : this) {
			if (room.getRoomName().equals(room_name)) {
				return room;
			}
		}// for
		throw new NotFoundException("Room " + room_name
				+ " not found in Floor " + this.getFloorName());
	}// GetRoom

	@SuppressWarnings("javadoc")
	public Room GetRoom(int room_number) {
		for (Room room : this) {
			if (room.getRoomName().equals(room_number)) {
				return room;
			}
		}// for
		throw new NotFoundException("Room " + room_number
				+ " not found in Floor " + this.getFloorName());
	}// GetRoom

	@Override
	@Deprecated
	public Room get(int index) {
		throw new Error("Floor#get was deprecated");
	}// get
}// Floor
