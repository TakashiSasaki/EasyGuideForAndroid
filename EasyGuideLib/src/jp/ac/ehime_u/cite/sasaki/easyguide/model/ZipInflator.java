package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class ZipInflator {

	private File zipFile;
	private File destinationDirectory;
	private final int bufferSize = 65536;

	@SuppressWarnings("serial")
	static class Exception extends RuntimeException {
		public Exception(String message) {
			super(message);
		}
	}

	/**
	 * @param zip_file
	 * @param destination_directory
	 */
	public ZipInflator(File zip_file, File destination_directory) {
		this.zipFile = zip_file;
		this.destinationDirectory = destination_directory;
	}

	/**
	 * @throws IOException
	 * @throws ZipException
	 * 
	 */
	public void Inflate() {
		ZipFile zip_file;
		try {
			zip_file = new ZipFile(this.zipFile);
		} catch (ZipException e) {
			throw new Exception("Unable to open "
					+ this.zipFile.getAbsolutePath() + " as a ZIP file. "
					+ e.getMessage());
		} catch (IOException e) {
			throw new Exception("Unable to open "
					+ this.zipFile.getAbsolutePath() + " as a ZIP file. "
					+ e.getMessage());
		}
		Enumeration<? extends ZipEntry> zip_entries = zip_file.entries();
		while (zip_entries.hasMoreElements()) {
			ZipEntry zip_entry = zip_entries.nextElement();
			File destination_file = new File(this.destinationDirectory,
					zip_entry.getName());
			if (zip_entry.isDirectory()) {
				destination_file.mkdirs();
				continue;
			}
			BufferedInputStream buffered_input_stream;
			try {
				buffered_input_stream = new BufferedInputStream(
						zip_file.getInputStream(zip_entry));
			} catch (IOException e) {
				throw new Exception("Unable to get an entry "
						+ zip_entry.getName() + " in the ZIP file. "
						+ e.getMessage());
			}
			if (!destination_file.getParentFile().exists()) {
				destination_file.getParentFile().mkdirs();
			}
			BufferedOutputStream buffered_output_stream;
			try {
				buffered_output_stream = new BufferedOutputStream(
						new FileOutputStream(destination_file));
			} catch (FileNotFoundException e) {
				throw new Exception("Unable to get output stream of "
						+ destination_file.getAbsolutePath() + ". "
						+ e.getMessage());
			}
			try {
				while (buffered_input_stream.available() > 0) {
					byte[] buffer = new byte[this.bufferSize];
					buffered_input_stream.read(buffer);
					buffered_output_stream.write(buffer);
				}
			} catch (IOException e) {
				throw new Exception(
						"Unable to copy data from an ZIP entry to a file. "
								+ e.getMessage());
			}// while
			try {
				buffered_input_stream.close();
			} catch (IOException e) {
				throw new Exception("Failed to close input stream. "
						+ e.getMessage());
			}
			try {
				buffered_output_stream.close();
			} catch (IOException e) {
				throw new Exception("Failed to close output stream. "
						+ e.getMessage());
			}
		}// while
	}// UnZip()
}
