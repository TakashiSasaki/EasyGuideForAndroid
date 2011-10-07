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
	public int floorNumber;
	public File floorDirectory;
	public String floorName;

	public Floor(File floor_directory) {
		this.floorDirectory = floor_directory;
		String floor_directory_name = floor_directory.getName();
		String[] parts = floor_directory_name.split("[ ]+");
		this.floorNumber = Integer.parseInt(parts[0]);
		this.floorName = parts[1];
		File floor_image_file = new File(floor_directory, floorImageName);
		this.floorImage = BitmapFactory.decodeFile(floor_image_file.getPath());
		ScanRooms();
	}// Floor

	private void ScanRooms() {
		Log.v(this.getClass().getSimpleName(), "Scanning room directories in "
				+ this.floorDirectory);
		File[] room_directories = this.floorDirectory.listFiles();
		for (int i = 0; i < room_directories.length; ++i) {
			File room_directory = room_directories[i];
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
			}
		}
	}// ScanRooms

	/**
	 * @return the floorImage
	 */
	public Bitmap getFloorImage() {
		return floorImage;
	}

}
