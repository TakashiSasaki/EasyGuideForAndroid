package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
@SuppressWarnings("serial")
public class Organization extends ArrayList<Building> {
	private static final String organizationImageName = "organization.png";
	private static final int organizationThumbnailWidth = 50;
	private static final int organizationThumbnailHeight = 50;

	private File organizationDirectory;
	private Bitmap organizationImage;
	private Bitmap organizationThumbnail;
	private int organizationOrder;

	private String organizationName;

	private String macAddress;
	private float longitude;
	private float latitude;
	private float altitude;

	/**
	 * @param organization_directory
	 */
	public Organization(File organization_directory) {
		super();
		organizationDirectory = organization_directory;
		String organization_directory_name = organization_directory.getName();
		String[] parts = organization_directory_name.split("[ ]+");
		if (parts.length == 2) {
			this.organizationOrder = Integer.parseInt(parts[0]);
			this.organizationName = parts[1];
		} else {
			this.organizationOrder = 9999;
			this.organizationName = parts[0];
		}

		LoadOrganizationImage();
		CreateOrganizationThumbnail();
		ScanBuildings();
		Log.d(this.getClass().getName(), this.toString());
	}

	private void ScanBuildings() {
		File[] building_directories = this.organizationDirectory.listFiles();
		for (int i = 0; i < building_directories.length; ++i) {
			File building_directory = building_directories[i];
			if (!building_directory.isDirectory()) {
				continue;
			}
			this.add(new Building(building_directory));
		}
	}

	private void LoadOrganizationImage() {
		File image_file = new File(this.organizationDirectory,
				organizationImageName);
		this.organizationImage = BitmapFactory.decodeFile(image_file.getPath());
	}

	/**
	 * @param original_bitmap 
	 * @param width
	 * @param height
	 * @return resized bitmap
	 */
	public static Bitmap ResizeBitmap(Bitmap original_bitmap, int width,
			int height) {
		int original_height = original_bitmap.getHeight();
		int original_width = original_bitmap.getWidth();
		float height_scale = height / (float) original_height;
		float width_scale = width / (float) original_width;
		Matrix resize_matrix = new Matrix();
		resize_matrix.postScale(width_scale, height_scale);
		Bitmap resized_bitmap = Bitmap.createBitmap(original_bitmap, 0, 0,
				original_width, original_height, resize_matrix, true);
		return resized_bitmap;
	}

	private void CreateOrganizationThumbnail() {
		this.organizationThumbnail = ResizeBitmap(this.organizationImage,
				Organization.organizationThumbnailWidth,
				Organization.organizationThumbnailHeight);
	}

	@Override
	public String toString() {
		return "Organization:" + this.organizationName;
	}

	/**
	 * @return the macAddress
	 */
	public String getMacAddress() {
		return macAddress;
	}

	/**
	 * @return the longitude
	 */
	public float getLongitude() {
		return longitude;
	}

	/**
	 * @return the latitude
	 */
	public float getLatitude() {
		return latitude;
	}

	/**
	 * @return the altitude
	 */
	public float getAltitude() {
		return altitude;
	}

	/**
	 * @return the organization order
	 */
	public int getOrganizationOrder() {
		return organizationOrder;
	}

	/**
	 * @return the organization name
	 */
	public String getOrganizationName() {
		return organizationName;
	}

	/**
	 * @return the organizationThumbnail
	 */
	public Bitmap getOrganizationThumbnail() {
		return organizationThumbnail;
	}

}
