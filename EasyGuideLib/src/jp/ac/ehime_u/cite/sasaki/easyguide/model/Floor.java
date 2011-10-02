package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

@SuppressWarnings("serial")
class Floor extends ArrayList<Room> {
	private static final String floorImageName = "floor.png";
	public Bitmap floorImage;
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
		File[] room_directories = this.floorDirectory.listFiles();
		for (int i = 0; i < room_directories.length; ++i) {
			File room_directory = room_directories[i];
			if (!room_directory.isDirectory()) {
				continue;
			}
			this.add(new Room(room_directory));
		}
	}// ScanRooms
}
