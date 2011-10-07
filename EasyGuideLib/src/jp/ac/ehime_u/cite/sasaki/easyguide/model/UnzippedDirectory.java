package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.util.Date;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class UnzippedDirectory {

	private File unzippedDirectory;
	private Date modfiedDateTime;

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
		this.modfiedDateTime = new Date(file.lastModified());
	}// an constructor

	/**
	 * this function was copied a code fragment on
	 * http://www.dreamincode.net/code/snippet1444.htm
	 * 
	 * @param path
	 *            to be deleted
	 * @return true if succeeded
	 */
	static public boolean RemoveAllFiles(File path) {
		if (!path.exists()) {
			return true;
		}
		if (path.isDirectory()) {
			for (File file : path.listFiles()) {
				if (file.isDirectory()) {
					RemoveAllFiles(file);
				} else {
					file.delete();
				}// if
			}// for
		}// if
		return (path.delete());
	}// RemoveAllFiles

	/**
	 * remove all files recursively
	 */
	public void RemoveAllFiles() {
		RemoveAllFiles(this.unzippedDirectory);
	}// RemoveAllFiles

	/**
	 * @return the modfiedDateTime
	 */
	public Date getModfiedDateTime() {
		return modfiedDateTime;
	}

	/**
	 * @return the unzippedDirectory
	 */
	public File getUnzippedDirectory() {
		return unzippedDirectory;
	}

}
