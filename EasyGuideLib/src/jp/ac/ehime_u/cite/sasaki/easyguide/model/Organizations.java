package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.util.ArrayList;
import android.util.Log;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 */
@SuppressWarnings("serial")
public class Organizations extends ArrayList<Organization> {

	private static Organizations theOrganizations;

	private Organizations() {
		super();
		ScanRootDirectory();
	}// a constructor

	private void ScanRootDirectory() {
		Log.v(this.getClass().getSimpleName(),
				"Enumerating organization directories");
		for (File organization_directory : Root.GetTheRoot().getRootDirectory()
				.listFiles()) {
			Log.v(this.getClass().getSimpleName(),
					organization_directory.getPath());
			if (!organization_directory.isDirectory()) {
				continue;
			}
			this.add(new Organization(organization_directory));
		}// for
	}// ScanRootDirectory

	/**
	 * @return singleton object of Organizations
	 */
	public static Organizations GetTheOrganizations() {
		if (theOrganizations == null) {
			theOrganizations = new Organizations();
		}
		return theOrganizations;
	}// GetTheOrganizations
}// Organizations
