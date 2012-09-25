package jp.ac.ehime_u.cite.sasaki.easyguide.content;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Environment;

/**
 * represents the root directory of EasyGuide contents.
 * 
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 */
@SuppressWarnings("serial")
public class Root extends ArrayList<Domain> {
	// private static final String rootDirectoryName = "EASYGUIDE";
	private File _rootDirectory;
	private Pattern DOMAIN_PATTERN = Pattern
			.compile("^[a-zA-Z0-9_\\-][a-zA-Z0-9_\\-.]+[a-zA-Z0-9_\\-]$");

	// the constructor
	private Root(File root_directory) throws FileNotFoundException {
		if (root_directory.isAbsolute()) {
			if (!root_directory.exists()) throw new FileNotFoundException();
			this._rootDirectory = root_directory;
		} else {
			File external_storage_directory = Environment
					.getExternalStorageDirectory();
			this._rootDirectory = new File(external_storage_directory,
					root_directory.getName()); // note taht only the last part
												// is used
		}// if
		_enumerateDomainDirectories();
	}// an constructor
	
	public File rootDirectory(){
		return this._rootDirectory;
	}

	/**
	 * @param file
	 * @return true if given directory name is valid for FQDN.
	 */
	private boolean isValidDomainDirectory(File file) {
		if (!file.isDirectory())
			return false;
		if (!file.canRead())
			return false;
		// Pattern pattern = Pattern
		// .compile("^.{1,254}$)(^(?:(?!\\d+\\.|-)[a-zA-Z0-9_\\-]{1,63}(?<!-)\\.?)+(?:[a-zA-Z]{2,})$");
		Matcher matcher = this.DOMAIN_PATTERN.matcher(file.getName());
		if (matcher.find())
			return true;
		else
			return false;
	}// IsValidDomainDirectory

	public void _enumerateDomainDirectories() {
		File[] domain_directories = this._rootDirectory.listFiles();
		for (File domain_directory : domain_directories) {
			Matcher m = this.DOMAIN_PATTERN.matcher(domain_directory.getName());
			if (m.find()) {
				try {
					Domain domain = new Domain(domain_directory);
					Logger.getGlobal().setLevel(Level.INFO);
					Logger.getGlobal().info(domain.getDomainDirectory().getAbsolutePath());
					this.add(domain);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}// try
			}// if
		}// for
	}// enumerateDomainDirectories()

	/**
	 * Singleton pattern
	 */
	private static Root theRoot;

	public static Root getTheRoot(File contents_directory) throws FileNotFoundException {
		assert Root.theRoot == null;
		Root.theRoot = new Root(contents_directory);
		return Root.theRoot;
	}// getTheRoot

	public static Root getTheRoot() {
		assert Root.theRoot != null;
		return Root.theRoot;
	}// getTheRoot

	static public void main(String[] args) throws FileNotFoundException {
		String sample_contents_directory_path = "/C:/Users/sasaki/Google ドライブ/Billable/EasyGuide-contents/EASYGUIDE";
		File sample_contents_directory = new File(
				sample_contents_directory_path);
		Root root = Root.getTheRoot(sample_contents_directory);
	}
}// Root
