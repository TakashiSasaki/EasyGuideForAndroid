package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
@SuppressWarnings("serial")
public class Organization extends ArrayList<Facility> {
	private static final String organizationImageName = "organization.png";
	private File organizationDirectory;
	private DirectoryImage organizationDirectoryImage;
	private DirectoryName organizationDirectoryName;
	private String macAddress;
	private float longitude;
	private float latitude;
	private float altitude;

	/**
	 * @param organization_directory
	 */
	public Organization(File organization_directory) {
		super();
		this.organizationDirectory = organization_directory;
		this.organizationDirectoryName = new DirectoryName(
				this.organizationDirectory.getName());
		this.organizationDirectoryImage = new DirectoryImage(
				this.organizationDirectory, Organization.organizationImageName);
		Log.v(this.getClass().getSimpleName(),
				"Scanning facility directories in "
						+ this.organizationDirectory);
		for (File facility_directory : this.organizationDirectory.listFiles()) {
			Log.v(this.getClass().getSimpleName(), "Facility directory "
					+ facility_directory.getAbsolutePath() + " found.");
			this.add(new Facility(facility_directory));
		}// for
	}// a constructor

	@SuppressWarnings("javadoc")
	public String getMacAddress() {
		return macAddress;
	}

	@SuppressWarnings("javadoc")
	public float getLongitude() {
		return longitude;
	}

	@SuppressWarnings("javadoc")
	public float getLatitude() {
		return latitude;
	}

	@SuppressWarnings("javadoc")
	public float getAltitude() {
		return altitude;
	}

	@SuppressWarnings("javadoc")
	public String getOrganizationDomain() {
		return this.organizationDirectoryName.getName();
	}

	@SuppressWarnings("javadoc")
	public Bitmap getOrganizationImage() {
		return this.organizationDirectoryImage.getImage();
	}

	@SuppressWarnings("javadoc")
	public Bitmap getOrganizationThumbnail() {
		return this.organizationDirectoryImage.getThumbnail();
	}

	@SuppressWarnings("javadoc")
	public String getOrganizatonName() {
		return this.organizationDirectoryName.getName();
	}
}
