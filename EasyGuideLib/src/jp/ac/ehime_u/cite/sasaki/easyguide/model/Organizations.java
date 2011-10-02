package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.util.Log;

/**
 * 複数の施設を表すシングルトンクラス。 インスタンスを生成すると自動的に外部記憶を走査して施設の一覧を作成する。
 * 
 * @author Takashi SASAKI @
 */
@SuppressWarnings("serial")
public class Organizations extends ArrayList<Organization> {

	// public ArrayList<Organization> organizations = new
	// ArrayList<Organization>();

	private static Organizations theOrganizations;

	private Organizations(Root root_) {
		super();
		ScanRootDirectory(root_);

		class OrganizationComparator implements Comparator<Organization> {
			public int compare(Organization object1, Organization object2) {
				if (object1.getOrganizationOrder() < object2
						.getOrganizationOrder()) {
					return -1;
				} else if (object1.getOrganizationOrder() > object2
						.getOrganizationOrder()) {
					return 1;
				} else {
					return 0;
				}
			}
		}

		Collections.sort(this, new OrganizationComparator());
	}

	private void ScanRootDirectory(Root root_) {
		File[] domain_directories = root_.getRootDirectory().listFiles();
		for (int i = 0; i < domain_directories.length; ++i) {
			File domain_directory = domain_directories[i];
			Log.v(this.getClass().getName(), domain_directory.getPath());
			if (!domain_directory.isDirectory())
				continue;
			if (!domain_directory.canRead())
				continue;
			Domain domain = new Domain(domain_directory);
			ScanDomainDirectory(domain);
		}
	}

	private void ScanDomainDirectory(Domain domain_) {
		File[] organization_directories = domain_.getDomainDirectory()
				.listFiles();
		for (int i = 0; i < organization_directories.length; ++i) {
			File organization_directory = organization_directories[i];
			Log.v(this.getClass().getName(), organization_directory.getPath()
					+ " was found.");
			if (!organization_directory.isDirectory())
				continue;
			if (!organization_directory.canRead())
				continue;
			this.add(new Organization(organization_directory));
		}
	}

	/**
	 * @param root_
	 * @return singleton object of Organizations
	 */
	public static Organizations GetTheOrganizations(Root root_) {
		if (theOrganizations == null) {
			theOrganizations = new Organizations(root_);
		}
		return theOrganizations;
	}
}
