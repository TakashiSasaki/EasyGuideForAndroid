package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ContentValues;
import android.content.Context;

public class Source {
	private Domain domain;
	private URI uri;
	private Date lastModified;
	private LastModifiedHeaderThread lastModifiedThread;
	final static public String COLUMN_domain = "domain";
	final static public String COLUMN_url = "url";
	final static public String COLUMN_downloadedFile = "downloadedFile";
	final static public String COLUMN_lastModified = "lastModified";
	private static final long DOWNLOAD_WAIT = 100;

	public Source(Domain domain_, URI uri_, File downloaded_file,
			Date last_modified) {
		this.domain = domain_;
		this.uri = uri_;
		// this.downloadedFile = downloaded_file;
		// this.SetDownloadedFile(this.downloadedFile);
		this.lastModified = last_modified;
	}// a constructor

	public Source(Domain domain_, URI uri_) throws URISyntaxException,
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

	public DownloadedItem Download(Context context_)
			throws InterruptedException, URISyntaxException {
		// SetDownloadedFile();
		DownloadedItem downloaded_item = new DownloadedItem(this.domain);
		DownloadThread download_thread = new DownloadThread(this, context_,
				downloaded_item);
		download_thread.start();
		Thread.sleep(DOWNLOAD_WAIT, 0);
		download_thread.join();
		return downloaded_item;
	}// Download

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

	public Date getLastModifiedNonBlocking() {
		return this.lastModified;
	}// getLastModifiedNonBlocking

	public ContentValues GetContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_domain, this.domain.getDomainDirectory().getName());
		try {
			cv.put(COLUMN_lastModified, this.lastModified.getTime());
		} catch (NullPointerException nurupo) {
			cv.put(COLUMN_lastModified, 0L);
		}// try
		cv.put(COLUMN_url, this.uri.toString());
		return cv;
	}// GetContentValues

}// ZipUrl
