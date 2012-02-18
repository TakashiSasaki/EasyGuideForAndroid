package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class Organization extends ItemBase<Facility> {
	// private static final String organizationImageName = "organization.png";
	private static final double maxDistance = 3000.0 * 3000.0;
	// private File organizationDirectory;
	// private DirectoryImage organizationDirectoryImage;
	// private DirectoryName organizationDirectoryName;
	private String macAddress;
	private float longitude;
	private float latitude;
	private float altitude;

	/**
	 * @param organization_directory
	 */
	public Organization(File organization_directory) {
		super(organization_directory);
		// this.organizationDirectory = organization_directory;
		// this.organizationDirectoryName = new DirectoryName(
		// this.organizationDirectory.getName());
		// this.organizationDirectoryImage = new DirectoryImage(
		// this.organizationDirectory);
		// Log.v(this.getClass().getSimpleName(),
		// "Scanning facility directories in "
		// + this.organizationDirectory);
		this.EnumerateFacilities();
		this.sortByIndex();
	}// a constructor

	public Organization() {
		super();
	}

	public void EnumerateFacilities() {
		for (File facility_directory : this.listFiles()) {
			if (!facility_directory.isDirectory())
				continue;
			Log.v(this.getClass().getSimpleName(), "Facility directory "
					+ facility_directory.getAbsolutePath() + " found.");
			this.add(new Facility(facility_directory));
		}// for
	}

	// public Facility getFacilityByIndex(int facility_index) {
	// for (Facility f : this) {
	// if (f.getFacilityIndex() == facility_index) {
	// return f;
	// }
	// }
	// return null;
	// }// getFacilityByIndex

	@SuppressWarnings("javadoc")
	public String getMacAddress() {
		return this.macAddress;
	}

	@SuppressWarnings("javadoc")
	public float getLongitude() {
		return this.longitude;
	}

	@SuppressWarnings("javadoc")
	public float getLatitude() {
		return this.latitude;
	}

	@SuppressWarnings("javadoc")
	public float getAltitude() {
		return this.altitude;
	}

	public Facility getFacility(int index) {
		return this.getByIndex(index, new Organization());
	}

	public Facility getFacility(String title) {
		return this.getByTitle(title, new Organization());
	}

	// @SuppressWarnings("javadoc")
	// public String getOrganizationDomain() {
	// return this.organizationDirectoryName.getName();
	// }

	// @SuppressWarnings("javadoc")
	// public DirectoryName getOrganizationDirectoryName() {
	// return this.organizationDirectoryName;
	// }

	// public String toString() {
	// return getOrganizationDirectoryName().getName();
	// }

	// @SuppressWarnings("javadoc")
	// public Bitmap getOrganizationImage() {
	// return this.organizationDirectoryImage.getImage();
	// }

	// @SuppressWarnings("javadoc")
	// public Bitmap getOrganizationThumbnail() {
	// return this.organizationDirectoryImage.getThumbnail();
	// }

	// @SuppressWarnings("javadoc")
	// public File getOrganizationDirectory() {
	// return this.organizationDirectory;
	// }

	public Facility GetNearestFacility(ImageView image_view,
			MotionEvent motion_event) {
		Facility candidate_facility = null;
		double candidate_distance = maxDistance;
		DistanceCalculator distance_calculator = new DistanceCalculator(
				image_view);
		for (Facility facility : this) {
			double distance = distance_calculator.GetDistanceBetween(
					motion_event, facility.getX(), facility.getY());
			if (distance < candidate_distance) {
				candidate_distance = distance;
				candidate_facility = facility;
			}// if
		}// for
		return candidate_facility;
	}// GetNearestFacility

	// @SuppressWarnings("javadoc")
	// public Facility GetFacility(String facility_name) {
	// for (Facility facility : this) {
	// if (facility.getFacilityName().equals(facility_name)) {
	// return facility;
	// }
	// }// for
	// return null;
	// }// GetFacility

	// @Override
	// @Deprecated
	// public Facility get(int index) {
	// throw new Error("Organization#get is deprecated.");
	// }// get

	// public int geOrganizationtIndex() {
	// return this.getOrganizationDirectoryName().getNumber();
	// }
}// Organization

