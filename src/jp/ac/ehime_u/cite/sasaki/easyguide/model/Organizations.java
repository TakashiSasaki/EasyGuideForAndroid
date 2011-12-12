package jp.ac.ehime_u.cite.sasaki.easyguide.model;

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
		for (Domain domain : Root.GetTheRoot()) {
			for (Organization organization : domain) {
				this.add(organization);
			}// for
		}// for
	}// a constructor

	/**
	 * @return singleton object of Organizations
	 */
	public static Organizations GetTheOrganizations() {
		if (theOrganizations == null) {
			theOrganizations = new Organizations();
		}// if
		return theOrganizations;
	}// GetTheOrganizations

	public Organization GetOrganization(String organization_name) {
		for (Organization organization : this) {
			if (organization.getOrganizationDomain().equals(organization_name)) {
				return organization;
			}// if
		}// for
		return null;
	}// GetOrganization
}// Organizations
