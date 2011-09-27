package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Building {

	public Floors floors;
	public File directory;
	public int number;
	public String name;
	public static final String IMAGE_NAME ="image.png";
	public Bitmap image;

	public Building(File building_directory){
		this.directory = building_directory;
		//String building_directory_path = building_directory.getPath();
		String building_directory_name = building_directory.getName();
		String[] parts = building_directory_name.split("[ ]+");
		this.number = Integer.parseInt(parts[0]);
		this.name = parts[1];
		File image_file = new File(building_directory,IMAGE_NAME);
		this.image= BitmapFactory.decodeFile(image_file.getPath());
		this.floors = new Floors(building_directory);
	}
}
