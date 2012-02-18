package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class PanelText {
	static final String fileName = "index.txt";
	String textString;
	ArrayList<String> lines;

	@SuppressWarnings("javadoc")
	public PanelText(File panel_directory) throws IOException {
		this.lines = new ArrayList<String>();
		File file = new File(panel_directory, fileName);
		FileReader file_reader = new FileReader(file);
		BufferedReader buffered_reader = new BufferedReader(file_reader);
		while (true) {
			String line = buffered_reader.readLine();
			if (line == null)
				break;
			this.lines.add(line);
		}// while
	}
}
