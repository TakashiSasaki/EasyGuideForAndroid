package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class UnzippedDirectory {

	private File unzippedDirectory;

	@SuppressWarnings({ "javadoc", "serial" })
	public class Exception extends RuntimeException {
		public Exception(String message_) {
			super(message_);
		}
	}

	/**
	 * @param file
	 * @throws Exception 
	 */
	public UnzippedDirectory(File file) throws Exception {
		if (!file.isDirectory()) {
			throw new Exception(file.getPath() + " is not a directory.");
		}
		if (!file.canRead()) {
			throw new Exception(file.getPath() + " is not readable.");
		}
		this.unzippedDirectory = file;
	}

	/**
	 * @return the unzippedDirectory
	 */
	public File getUnzippedDirectory() {
		return unzippedDirectory;
	}

}
