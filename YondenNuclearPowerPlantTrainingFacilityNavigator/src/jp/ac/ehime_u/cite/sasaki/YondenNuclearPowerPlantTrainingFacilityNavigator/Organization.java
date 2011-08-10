package jp.ac.ehime_u.cite.sasaki.YondenNuclearPowerPlantTrainingFacilityNavigator;

import java.io.File;

import android.graphics.Bitmap;

public class Organization {
	public static final String IMAGE_NAME="image.png";
	public File directory;
	public Bitmap image;
	public String name;
	public String macAddress;
	public float longitude;
	public float latitude;
	public Organization(File organization_directory) {
		this.directory = organization_directory;
		String organization_directory_name = organization_directory.getName();
		String[] parts = organization_directory_name.split("[ ]+");
		this.name= parts[0];
	}
}
