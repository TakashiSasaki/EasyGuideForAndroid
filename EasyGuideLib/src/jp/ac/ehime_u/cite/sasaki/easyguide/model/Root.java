package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Environment;
import android.util.Log;

/**
 * represents the root directory of EasyGuide contents.
 * 
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 */
public class Root {
	private static final String rootDirectoryName = "EASYGUIDE";
	private static Root root;
	private File rootDirectory;
	// private ArrayList<Domain> domains;
	private File externalStorageDirectory;

	/**
	 * 
	 */
	private Root() {
		externalStorageDirectory = Environment.getExternalStorageDirectory();
		Log.v(this.getClass().getSimpleName(),
				externalStorageDirectory.getAbsolutePath());
		rootDirectory = new File(externalStorageDirectory, rootDirectoryName);
		if (!rootDirectory.exists()) {
			rootDirectory.mkdir();
			Log.v(this.getClass().getSimpleName(), "creating directory "
					+ rootDirectory.getAbsolutePath());
		}
		assert (rootDirectory.exists());
	}

	@SuppressWarnings("javadoc")
	public File getRootDirectory() {
		return rootDirectory;
	}

	/**
	 * @param file
	 * @return true if given directory name is valid for FQDN.
	 */
	public boolean IsValidDomainDirectory(File file) {
		if (!file.isDirectory())
			return false;
		if (!file.canRead())
			return false;
		Pattern pattern = Pattern
				.compile("^.{1,254}$)(^(?:(?!\\d+\\.|-)[a-zA-Z0-9_\\-]{1,63}(?<!-)\\.?)+(?:[a-zA-Z]{2,})$");
		Matcher matcher = pattern.matcher(file.getName());
		if (matcher.find())
			return true;
		else
			return false;
	}
	
	/**
	 * @return singleton isntance of class Root
	 */
	public static Root GetTheRoot() {
		if(root == null){
			root = new Root();
		}
		return root;
	}
}
