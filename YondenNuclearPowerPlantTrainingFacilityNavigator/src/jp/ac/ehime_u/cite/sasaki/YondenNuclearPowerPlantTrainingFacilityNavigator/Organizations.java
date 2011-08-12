package jp.ac.ehime_u.cite.sasaki.YondenNuclearPowerPlantTrainingFacilityNavigator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.os.Environment;

public class Organizations {
	public static final String tag = "EASYGUIDE";
	private static final String DIRECTORY = "EASYGUIDE";

	public File directory;
	public ArrayList<Organization> organizations = new ArrayList<Organization>();

	private static Organizations theOrganizations;

	private Organizations() throws FileNotFoundException {
		File storage_directory = Environment.getExternalStorageDirectory();
		this.directory = new File(storage_directory, DIRECTORY);
		File[] organization_directories = this.directory.listFiles();
		if (organization_directories==null){
			throw new FileNotFoundException(this.directory.getAbsolutePath());
		}
		for (int i = 0; i < organization_directories.length; ++i) {
			File organization_directory = organization_directories[i];
			if (!organization_directory.isDirectory()) {
				continue;
			}
			this.organizations.add(new Organization(organization_directory));
		}
		
		class OrganizationComparator implements Comparator<Organization> {
			public int compare(Organization object1, Organization object2) {
				if (object1.order < object2.order) {
					return -1;
				} else if (object1.order > object2.order) {
					return 1;
				} else {
					return 0;
				}
			}
		}
		
		Collections.sort(this.organizations, new OrganizationComparator());
	}

	public static Organizations GetTheOrganizations() throws FileNotFoundException {
		if(theOrganizations==null){
			theOrganizations= new Organizations();
		}
		return theOrganizations;
	}

	public static ArrayList<Organization> GetCollection() {
		try{
			return GetTheOrganizations().organizations;
		} catch (FileNotFoundException file_not_found_exception){
			return new ArrayList<Organization>();
		}
	}
}
