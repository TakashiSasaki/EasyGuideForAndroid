package jp.ac.ehime_u.cite.sasaki.easyguide.download;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.ac.ehime_u.cite.sasaki.easyguide.content.Domain;

import android.util.Log;

public class DownloadedItem {
	private File downloadedFile;
	private final int bufferSize = 65536;

	public File getDownloadedFile() {
		return this.downloadedFile;
	}

	public DownloadedItem(File downloaded_file) {
		this.downloadedFile = downloaded_file;
	}// a constructor

	public DownloadedItem(Domain domain) {
		this.downloadedFile = new File(domain.getDirectory(), ""
				+ Calendar.getInstance().getTime() + ".zip");
	}// a constructor

	public Date getDateFromFileName() {
		Pattern pattern = Pattern.compile("([0-9]+).zip");
		Matcher matcher = pattern.matcher(this.downloadedFile.getName());
		String time_in_milliseconds = matcher.group(1);
		return new Date(Long.parseLong(time_in_milliseconds));
	}

	@Deprecated
	public int SaveStream(BufferedInputStream buffered_input_stream) {
		int count = 0;
		Log.v(this.getClass().getSimpleName(), "Writing downloaded file to "
				+ this.downloadedFile.getAbsolutePath());
		BufferedOutputStream buffered_output_stream;
		try {
			buffered_output_stream = new BufferedOutputStream(
					new FileOutputStream(this.downloadedFile));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return count;
		}// try

		byte buffer[] = new byte[this.bufferSize];
		int read_size;
		try {
			while ((read_size = buffered_input_stream.read(buffer)) != -1) {
				buffered_output_stream.write(buffer, 0, read_size);
				count += read_size;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return count;
		}// try
		Log.v(this.getClass().getSimpleName(), "Closing downloaded file to "
				+ this.downloadedFile.getAbsolutePath());
		try {
			buffered_output_stream.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return count;
		}// try
		try {
			buffered_output_stream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return count;
		}// try
		try {
			buffered_input_stream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return count;
		}// try
		return count;
	}// SaveStream

}// DownloadedItem
