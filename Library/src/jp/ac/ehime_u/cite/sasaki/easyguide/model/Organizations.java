package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.util.ArrayList;

import jp.ac.ehime_u.cite.sasaki.easyguide.download.Domain;
import jp.ac.ehime_u.cite.sasaki.easyguide.exception.ItemNotFoundException;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 */
@SuppressWarnings("serial")
public class Organizations extends ArrayList<Organization> {

	private static Organizations theOrganizations;

	private Organizations() {
		super();
		Log.v(new Throwable(), "Enumerating organization directories");
		this.EnumerateOrganizations();
	}// a constructor

	public void EnumerateOrganizations() {
		Log.v(new Throwable(), "Enumerating domains.");
		for (Domain domain : Root.GetTheRoot()) {
			Log.v(new Throwable(),
					"Enumerating organizations " + domain.getDomainDirectory());
			domain.EnumerateOrganizations();
			for (Organization organization : domain) {
				Log.v(new Throwable(),
						"Found organization " + organization.getTitle());
				Log.v(new Throwable(), organization.getTitle());
				this.add(organization);
				Log.v(new Throwable(),
						"Added organization " + organization.getTitle());
			}// for
			Log.v(new Throwable(), "scanned for organizations in domain "
					+ domain.getDomainDirectory().getName());
		}// for
		Log.v(new Throwable(), "scanned for domains in "
				+ Root.GetTheRoot().getRootDirectory().getPath());
	}// EnumerateOrganizations

	// public Organization GetOrganizationByIndex(int organization_index) {
	// for (Organization o : this) {
	// if (o.getOrganizationDirectoryName().getNumber() == organization_index)
	// return o;
	// }
	// return null;
	// }// GetOrganizationByNumber

	/**
	 * @return singleton object of Organizations
	 */
	public static Organizations getInstance() {
		if (theOrganizations == null) {
			theOrganizations = new Organizations();
		}// if
		return theOrganizations;
	}// GetTheOrganizations

	public Organization getOrganization(int index) throws ItemNotFoundException {
		for (Organization o : this) {
			if (o.getIndex() == index) {
				return o;
			}
		}
		throw new ItemNotFoundException("Can't find index " + index
				+ " in Organizations.");
	}// getOrganization

	// public Organization GetOrganization(String organization_name) {
	// for (Organization organization : this) {
	// if (organization.getOrganizationDomain().equals(organization_name)) {
	// return organization;
	// }// if
	// }// for
	// return null;
	// }// GetOrganization
}// Organizations
