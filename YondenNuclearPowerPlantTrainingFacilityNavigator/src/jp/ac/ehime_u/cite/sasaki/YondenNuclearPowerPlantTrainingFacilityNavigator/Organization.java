package jp.ac.ehime_u.cite.sasaki.YondenNuclearPowerPlantTrainingFacilityNavigator;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Organization {
	public static final String tag ="EasyGuide";
	public static final String IMAGE_NAME="image.png";
	public File directory;
	public Bitmap image;
	public int order;
	public String name;
	public String macAddress;
	public float longitude;
	public float latitude;
	public float altitude;
	public Buildings buildings;
	
	public Organization(File organization_directory) {
		this.directory = organization_directory;
		String organization_directory_name = organization_directory.getName();
		String[] parts = organization_directory_name.split("[ ]+");
		this.order = Integer.parseInt(parts[0]);
		this.name= parts[1];
		File image_file = new File(this.directory, IMAGE_NAME);
		this.image = BitmapFactory.decodeFile(image_file.getPath());
		this.buildings = new Buildings(organization_directory);
		Log.d(tag, this.toString());
	}
	
	public String toString(){
		return "Organization:"+this.name;
	}
}
