package jp.ac.ehime_u.cite.sasaki.YondenNuclearPowerPlantTrainingFacilityNavigator;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Room {
	public Equipments equipments;
	public File directory;
	public String name;
	public int number;
	public static final String IMAGE_NAME = "image.png";
	public Bitmap image;

	public Room(File room_directory) {
		this.directory = room_directory;
		String room_directory_name = room_directory.getName();
		String[] parts = room_directory_name.split("[ ]+");
		this.number = Integer.parseInt(parts[0]);
		this.name = parts[1];
		File image_file = new File(room_directory, IMAGE_NAME);
		this.image = BitmapFactory.decodeFile(image_file.getPath());
		this.equipments = new Equipments(room_directory);
	}
}
