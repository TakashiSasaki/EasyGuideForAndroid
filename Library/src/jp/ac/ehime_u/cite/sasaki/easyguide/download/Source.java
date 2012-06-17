package jp.ac.ehime_u.cite.sasaki.easyguide.download;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import android.content.Context;

public class Source {
	private Domain domain;
	private URI uri;
	private Date lastModified;
	private LastModifiedHeaderThread lastModifiedThread;
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
			checkLastModified();
		}// if
	} // a constructor

	public void checkLastModified() {
		if (this.uri.getHost() != "assets") {
			try {
				this.lastModifiedThread = new LastModifiedHeaderThread(this.uri);
			} catch (URISyntaxException e) {
				e.printStackTrace();
				return;
			}
			this.lastModifiedThread.start();
		}// if
	}// checkLastModified

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

	public Domain getDomain() {
		return this.domain;
	}// getDomain

	public String getDomainByString() {
		return this.domain.getDomainDirectory().getName();
	}// getDomainByString

	public File getDomainDirectory() {
		return this.domain.getDomainDirectory();
	}// getDomainDirectory

	public URI getUri() {
		return this.uri;
	}// getUri

	public Date getLastModified() throws InterruptedException {
		if (this.lastModified != null) {
			return this.lastModified;
		} else {
			this.lastModifiedThread.join();
			this.lastModified = this.lastModifiedThread.getLastModified();
			this.lastModifiedThread = null; // release LastModifiedThread object
			return this.lastModified;
		}
	}// getLastModified

	public Date getLastModifiedNonBlocking() {
		return this.lastModified;
	}// getLastModifiedNonBlocking

}// Source
