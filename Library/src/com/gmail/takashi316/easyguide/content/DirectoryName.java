package com.gmail.takashi316.easyguide.content;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmail.takashi316.easyguide.exception.InvalidDirectoryNameException;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class DirectoryName {
	private String rawName = "unknown";
	public String name = "unknown";
	public int number = -1;
	public int x = -1, y = -1;

	/**
	 * a constructor takes directory name.
	 * 
	 * @throws Exception
	 */
	public DirectoryName(String directory_name) {
		MyLogger.info("parsing " + directory_name);
		this.rawName = directory_name;
		String[] parts = directory_name.split("[ ]+");
		if (parts.length == 2 || parts.length == 4) {
			try {
				this.number = Integer.parseInt(parts[0]);
			} catch (NumberFormatException e) {
				throw new InvalidDirectoryNameException("parsing " + parts[0]
						+ " in " + directory_name);
			}// try
			if (this.number <= 0) {
				throw new InvalidDirectoryNameException("parsing" + parts[0]
						+ " and it should be positive integer");
			}
			this.name = parts[1];
			// return;
			if (parts.length == 4) {
				try {
					this.x = Integer.parseInt(parts[2]);
					this.y = Integer.parseInt(parts[3]);
					return;
				} catch (NumberFormatException e) {
					throw new InvalidDirectoryNameException(
							"Invalid number format in part 3 and 4 in the directory name "
									+ directory_name);
				}// try
			}// if
			return;
		}// if
		if (parts.length == 1) {
			this.name = directory_name;
			this.number = -1;
			this.x = -1;
			this.y = -1;
			return;
		}
		throw new InvalidDirectoryNameException("Invalid directory name "
				+ directory_name);
	}// a constructor

	static public void main(String[] args) {
		final String directory_path = "/C:/Users/sasaki/Google ドライブ/Billable/EasyGuide-contents/EASYGUIDE/www.yonden.co.jp/01 四国電力";
		final File directory = new File(directory_path);
		DirectoryName directory_name = new DirectoryName(directory.getName());
		MyLogger.info("name = " + directory_name.name);
		MyLogger.info("number = " + directory_name.number);
		MyLogger.info("x = " + directory_name.x + ", y = " + directory_name.y);
	}
}
