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
		Log.v(this.getClass().getSimpleName(),
				"Enumerating organization directories");
		for (File organization_directory : Root.GetTheRoot().getRootDirectory()
				.listFiles()) {
			if (!organization_directory.isDirectory())
				continue;
			Log.v(this.getClass().getSimpleName(),
					organization_directory.getPath());
			this.add(new Organization(organization_directory));
		}// for
	}// a constructor

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
