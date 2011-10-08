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
public class Organization extends ArrayList<Facility> {
	private static final String organizationImageName = "organization.png";
	private static final int organizationThumbnailWidth = 50;
	private static final int organizationThumbnailHeight = 50;

	private File organizationDirectory;
	private DirectoryImage directoryImage;
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
		this.directoryImage = new DirectoryImage(this.organizationDirectory,
				organizationImageName, organizationThumbnailWidth,
				organizationThumbnailHeight);
		ScanFacilities();
		Log.d(this.getClass().getName(), this.toString());
	}// a constructor

	private void ScanFacilities() {
		Log.v(this.getClass().getSimpleName(),
				"Scanning facility directories in "
						+ this.organizationDirectory);
		for (File facility_directory : this.organizationDirectory.listFiles()) {
			if (!facility_directory.isDirectory()) {
				continue;
			}
			Log.v(this.getClass().getSimpleName(), "Facility directory "
					+ facility_directory.getAbsolutePath() + " found.");
			try {
				this.add(new Facility(facility_directory));
			} catch (Exception e) {
				Log.v(this.getClass().getSimpleName(),
						"an exception was catched while constructing a Facility object for "
								+ facility_directory.getAbsolutePath());
			}// try
		}// for
	}// ScanFacilities

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
