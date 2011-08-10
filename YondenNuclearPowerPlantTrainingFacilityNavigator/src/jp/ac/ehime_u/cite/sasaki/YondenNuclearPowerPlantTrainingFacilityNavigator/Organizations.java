package jp.ac.ehime_u.cite.sasaki.YondenNuclearPowerPlantTrainingFacilityNavigator;

import java.io.File;
import java.util.ArrayList;

import android.os.Environment;
import android.util.Log;

public class Organizations {
	public static final String tag = "EASYGUIDE";
	private static final String DIRECTORY = "EASYGUIDE";

	public File directory;
	public ArrayList<Organization> organizations = new ArrayList<Organization>();

	public Organizations() {
		File storage_directory = Environment.getExternalStorageDirectory();
		this.directory = new File(storage_directory, DIRECTORY);
		File[] organization_directories = this.directory.listFiles();
		for (int i = 0; i < organization_directories.length; ++i) {
			File organization_directory = organization_directories[i];
			if (!organization_directory.isDirectory()) {
				continue;
			}
			this.organizations.add(new Organization(organization_directory));
		}
	}
}
