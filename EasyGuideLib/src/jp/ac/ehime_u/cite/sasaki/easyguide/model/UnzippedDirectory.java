package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class UnzippedDirectory {

	private File unzippedDirectory;

	@SuppressWarnings({ "javadoc", "serial" })
	public static class Exception extends RuntimeException {
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

	/**
	 * this function was copied a code fragment on
	 * http://www.dreamincode.net/code/snippet1444.htm
	 * 
	 * @param path
	 *            to be deleted
	 * @return true if succeeded
	 */
	static public boolean RemoveAllFiles(File path) {
		if (path.exists()) {
			if (path.isDirectory()) {
				File[] files = path.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						RemoveAllFiles(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}
		return (path.delete());
	}

	/**
	 * remove all files recursively
	 */
	public void RemoveAllFiles() {
		RemoveAllFiles(this.unzippedDirectory);
	}
}
