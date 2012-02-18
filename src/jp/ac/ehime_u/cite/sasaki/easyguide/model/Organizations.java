package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import jp.ac.ehime_u.cite.sasaki.easyguide.download.Domain;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 */
public class Organizations extends ItemBase<Organization> {

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
				this.add(organization);
			}// for
		}// for
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

	public Organization getOrganization(int index) {
		return this.getByIndex(index, null);
	}

	// public Organization GetOrganization(String organization_name) {
	// for (Organization organization : this) {
	// if (organization.getOrganizationDomain().equals(organization_name)) {
	// return organization;
	// }// if
	// }// for
	// return null;
	// }// GetOrganization
}// Organizations
