package jp.ac.ehime_u.cite.sasaki.YondenNuclearPowerPlantTrainingFacilityNavigator;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

class Floor {
	public Rooms rooms;
	public static final String IMAGE_NAME = "image.png";
	public Bitmap image;
	public int number;
	public File directory;
	public String name;

	public Floor(File floor_directory) {
		this.directory = floor_directory;
		String floor_directory_name = floor_directory.getName();
		String[] parts = floor_directory_name.split("[ ]+");
		this.number = Integer.parseInt(parts[0]);
		this.name = parts[1];
		File image_file = new File(floor_directory, IMAGE_NAME);
		this.image = BitmapFactory.decodeFile(image_file.getPath());

		this.rooms = new Rooms(floor_directory);
	}
}
