package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZipUrl {
	private Domain domain;
	private URL url;
	private Date downloadedDate;
	private File downloadedFile;
	private Date lastModified;
	private LastModifiedHeaderThread lastModifiedThread;

	public ZipUrl(Domain domain_, URL url_) throws URISyntaxException,
			MalformedURLException {
		this.url = url_;
		this.domain = domain_;
		CheckLastModified();
	} // a constructor

	public void CheckLastModified() {
		if (url.getHost() != "assets") {
			try {
				this.lastModifiedThread = new LastModifiedHeaderThread(url);
			} catch (URISyntaxException e) {
				e.printStackTrace();
				return;
			}
			lastModifiedThread.start();
		}// if
	}// SetLastModified

	public Date getLastModified() throws InterruptedException {
		if (lastModified != null) {
			return lastModified;
		} else {
			lastModifiedThread.join();
			this.lastModified = this.lastModifiedThread.getLastModified();
			return this.lastModified;
		}// getLastModified
	}// getLastModified

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

	public Date getDownloadedDate() {
		return this.downloadedDate;
	}

	public URL getUrl() {
		return this.url;
	}
	
	public File getDownloadedFile(){
		return this.downloadedFile;
	}
	
}// ZipUrl
