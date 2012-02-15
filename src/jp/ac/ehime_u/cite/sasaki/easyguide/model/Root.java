package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.ac.ehime_u.cite.sasaki.easyguide.download.Domain;
import jp.ac.ehime_u.cite.sasaki.easyguide.exception.StorageException;

import android.os.Environment;
import android.util.Log;

/**
 * represents the root directory of EasyGuide contents.
 * 
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 */
@SuppressWarnings("serial")
public class Root extends ArrayList<Domain> {
	private static final String rootDirectoryName = "EASYGUIDE";
	private static Root root;
	private File rootDirectory;
	private Pattern DOMAIN_PATTERN = Pattern
			.compile("^[a-zA-Z0-9_\\-][a-zA-Z0-9_\\-.]+[a-zA-Z0-9_\\-]$");

	// private File externalStorageDirectory;

	private Root() throws StorageException {
		File external_storage_directory = Environment
				.getExternalStorageDirectory();
		Log.v(this.getClass().getSimpleName(),
				external_storage_directory.getAbsolutePath());
		rootDirectory = new File(external_storage_directory, rootDirectoryName);
		if (!rootDirectory.exists()) {
			rootDirectory.mkdir();
		}
		if (!rootDirectory.exists()) {
			throw new StorageException(rootDirectory.getPath()
					+ " can not be created.");
		}
		/*
		 * if (!rootDirectory.exists()) { rootDirectory.mkdir();
		 * Log.v(this.getClass().getSimpleName(), "creating directory " +
		 * rootDirectory.getAbsolutePath()); }
		 */
		EnumerateDomainDirectories();
		assert (rootDirectory.exists());
	}// an constructor

	@SuppressWarnings("javadoc")
	public File getRootDirectory() {
		return rootDirectory;
	}// getRootDirectory

	/**
	 * @param file
	 * @return true if given directory name is valid for FQDN.
	 */
	public boolean IsValidDomainDirectory(File file) {
		if (!file.isDirectory())
			return false;
		if (!file.canRead())
			return false;
		// Pattern pattern = Pattern
		// .compile("^.{1,254}$)(^(?:(?!\\d+\\.|-)[a-zA-Z0-9_\\-]{1,63}(?<!-)\\.?)+(?:[a-zA-Z]{2,})$");
		Matcher matcher = DOMAIN_PATTERN.matcher(file.getName());
		if (matcher.find())
			return true;
		else
			return false;
	}// IsValidDomainDirectory

	public void EnumerateDomainDirectories() {
		Log.d(this.getClass().getSimpleName(),
				"Enumerating dommain directories in " + this.getRootDirectory());
		File[] domain_directories = this.getRootDirectory().listFiles();
		for (File domain_directory : domain_directories) {
			Log.d(this.getClass().getSimpleName(), "Found domain directory "
					+ domain_directory.getAbsolutePath());
			Matcher m = DOMAIN_PATTERN.matcher(domain_directory.getName());
			if (m.find()) {
				Log.d(this.getClass().getSimpleName(), "Matched. "
						+ domain_directory.getName()
						+ " is a domain directory.");
				try {
					this.add(new Domain(domain_directory));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}// try
			}// if
		}// for
	}// EnumerateDomainDirectories()

	/**
	 * @return singleton instance of class Root
	 */
	public static Root GetTheRoot() {
		if (root == null) {
			root = new Root();
		}
		return root;
	}// GetTheRoot
}// Root
