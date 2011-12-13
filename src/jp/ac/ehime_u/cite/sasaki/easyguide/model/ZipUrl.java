package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ContentValues;

public class ZipUrl {
	private Domain domain;
	private URI uri;
	private Date downloadedDate;
	private File downloadedFile;
	private Date lastModified;
	private LastModifiedHeaderThread lastModifiedThread;
	final static public String COLUMN_domain = "domain";
	final static public String COLUMN_url = "url";
	final static public String COLUMN_downloadedFile = "downloadedFile";
	final static public String COLUMN_lastModified = "lastModified";

	public ZipUrl(Domain domain_, URI uri_, File downloaded_file,
			Date last_modified) throws FileNotFoundException {
		this.domain = domain_;
		this.uri = uri_;
		this.downloadedFile = downloaded_file;
		this.SetDownloadedFile(this.downloadedFile);
		this.lastModified = last_modified;
	}// a constructor

	public ZipUrl(Domain domain_, URI uri_) throws URISyntaxException,
			MalformedURLException {
		this.uri = uri_;
		this.domain = domain_;
		if (!this.uri.getHost().equals("assets")) {
			CheckLastModified();
		}// if
	} // a constructor

	public void CheckLastModified() {
		if (this.uri.getHost() != "assets") {
			try {
				this.lastModifiedThread = new LastModifiedHeaderThread(this.uri);
			} catch (URISyntaxException e) {
				e.printStackTrace();
				return;
			}
			this.lastModifiedThread.start();
		}// if
	}// SetLastModified

	public void SetDownloadedFile(File downloaded_file)
			throws FileNotFoundException {
		if (!downloaded_file.exists()) {
			throw new FileNotFoundException(downloaded_file.getPath());
		}
		Pattern pattern = Pattern.compile("([0-9+]).zip");
		Matcher matcher = pattern.matcher(downloaded_file.getName());
		String time_in_milliseconds;
		time_in_milliseconds = matcher.group(1);
		this.downloadedDate = new Date(Long.parseLong(time_in_milliseconds));
	}// SetDownloadedFile

	/*
	 * Set local file name in which downloaded byte stream is stored.
	 */
	public void SetDownloadedFile() {
		Calendar calendar = Calendar.getInstance();
		this.downloadedDate = calendar.getTime();
		this.downloadedFile = new File(this.domain.getDomainDirectory(), ""
				+ this.downloadedDate.getTime() + ".zip");
	}// SetDownloadedFile

	public Domain GetDomain() {
		return this.domain;
	}

	public String GetDomainByString() {
		return this.domain.getDomainDirectory().getName();
	}// GetDomainByString

	public File GetDomainDirectory() {
		return this.domain.getDomainDirectory();
	}

	public URI getUri() {
		return this.uri;
	}

	public Date getDownloadedDate() {
		return this.downloadedDate;
	}

	public File getDownloadedFile() {
		return this.downloadedFile;
	}

	public Date getLastModified() throws InterruptedException {
		if (this.lastModified != null) {
			return this.lastModified;
		} else {
			this.lastModifiedThread.join();
			this.lastModified = this.lastModifiedThread.getLastModified();
			this.lastModifiedThread = null; // release LastModifiedThread object
			return this.lastModified;
		}// getLastModified
	}// getLastModified

	public ContentValues GetContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_domain, this.domain.getDomainDirectory().getName());
		cv.put(COLUMN_downloadedFile, this.downloadedFile.getName());
		try {
			cv.put(COLUMN_lastModified, this.lastModified.getTime());
		} catch (NullPointerException nurupo) {
			cv.put(COLUMN_lastModified, 0L);
		}// try
		cv.put(COLUMN_url, this.uri.toString());
		return cv;
	}// GetContentValues

}// ZipUrl
