package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

/**
 * represents both zipped and unzipped contents.
 * 
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class ZipDownloader {

	@SuppressWarnings({ "javadoc", "serial" })
	public class Exception extends RuntimeException {
		public Exception(String message_) {
			super(message_);
		}
	}

	private File downloadedFile;
	private File domainDirectory;
	private Date downloadedDate;
	private Date lastModifiedDate;
	private URL sourceUrl;
	private final int bufferSize = 65536;

	/**
	 * @param domain_
	 * @param file_name
	 * @param url_
	 * @throws IOException
	 */
	public ZipDownloader(Domain domain_, String file_name, URL url_)
			throws IOException {
		this.domainDirectory = domain_.getDomainDirectory();
		this.sourceUrl = url_;
		this.downloadedFile = File.createTempFile("temp", "zip");
	}

	/**
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * 
	 */
	public void GetMethod() throws URISyntaxException, ClientProtocolException,
			IOException {
		HttpClient http_client = new DefaultHttpClient();
		HttpGet http_get = new HttpGet();
		http_client.getParams().setParameter("http.connection.timeout",
				new Integer(20000));
		URI source_uri;
		source_uri = sourceUrl.toURI();
		http_get.setURI(source_uri);
		HttpResponse http_response = http_client.execute(http_get);
		int http_status_code = http_response.getStatusLine().getStatusCode();
		if (http_status_code == HttpStatus.SC_REQUEST_TIMEOUT) {
			throw new Exception("request for " + this.sourceUrl.toString()
					+ " timed out.");
		}
		if (http_status_code == HttpStatus.SC_NOT_FOUND) {
			throw new Exception(this.sourceUrl.toString() + " was not found.");
		}
		if (http_status_code != HttpStatus.SC_OK) {
			throw new Exception("response : "
					+ http_response.getStatusLine().toString());
		}
		WriteDownloadedFile(http_response);
		this.downloadedDate = new Date();
		try {
			this.lastModifiedDate = ParseLastModified(http_response);
		} catch (ParseException e) {
			Log.v(this.getClass().getName(),
					"Can't parse Last-Modified value in HTTP response header.");
			this.lastModifiedDate = null;
		}
	}

	private void WriteDownloadedFile(HttpResponse http_response)
			throws IllegalStateException, IOException {
		InputStream input_stream = http_response.getEntity().getContent();
		BufferedInputStream buffered_input_stream = new BufferedInputStream(
				input_stream, bufferSize);
		BufferedOutputStream buffered_output_stream = new BufferedOutputStream(
				new FileOutputStream(downloadedFile));
		byte buffer[] = new byte[bufferSize];
		int read_size;
		while ((read_size = buffered_input_stream.read(buffer)) != -1) {
			buffered_output_stream.write(buffer, 0, read_size);
		}
		buffered_output_stream.flush();
		buffered_output_stream.close();
		buffered_input_stream.close();
	}

	/**
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws ParseException
	 */
	public void HeadMethod() throws URISyntaxException,
			ClientProtocolException, IOException {
		HttpClient http_client = new DefaultHttpClient();
		HttpHead http_head = new HttpHead();
		http_head.setURI(this.sourceUrl.toURI());
		HttpResponse http_response = http_client.execute(http_head);
		try {
			this.lastModifiedDate = this.ParseLastModified(http_response);
		} catch (ParseException e) {
			Log.v(this.getClass().getName(),
					"Can't parse Last-Modified value in HTTP response header.");
			this.lastModifiedDate = null;
		}
	}

	private Date ParseLastModified(HttpResponse http_response)
			throws ParseException {
		Header last_modified_header = http_response
				.getFirstHeader("Last-Modified");
		String last_modified_string = last_modified_header.getValue();
		SimpleDateFormat http_date_format = new SimpleDateFormat(
				"EEE, dd MM yyyy HH:mm:ss z", Locale.ENGLISH);
		http_date_format.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date last_modified_date = http_date_format.parse(last_modified_string);
		return last_modified_date;
	}

	/**
	 * @throws IOException
	 * @throws ZipException
	 * 
	 */
	public void Unzip() throws ZipException, IOException {
		ZipFile zip_file = new ZipFile(this.downloadedFile);
		Enumeration<? extends ZipEntry> zip_entries = zip_file.entries();
		while (zip_entries.hasMoreElements()) {
			ZipEntry zip_entry = zip_entries.nextElement();
			File destination_file = new File(this.domainDirectory,
					zip_entry.getName());
			if (zip_entry.isDirectory()) {
				destination_file.mkdirs();
				continue;
			}
			BufferedInputStream buffered_input_stream = new BufferedInputStream(
					zip_file.getInputStream(zip_entry));
			if (!destination_file.getParentFile().exists()) {
				destination_file.getParentFile().mkdirs();
			}
			BufferedOutputStream buffered_output_stream = new BufferedOutputStream(
					new FileOutputStream(destination_file));
			while (buffered_input_stream.available() > 0) {
				byte[] buffer = new byte[this.bufferSize];
				buffered_input_stream.read(buffer);
				buffered_output_stream.write(buffer);
			}// while
			buffered_input_stream.close();
			buffered_output_stream.close();
		}// while
	}// UnZip()

	@Override
	protected void finalize() throws Throwable {
		this.downloadedFile.delete();
		super.finalize();
	}

	/**
	 * @return the downloadedDate
	 */
	public Date getDownloadedDate() {
		return downloadedDate;
	}

	/**
	 * @return the lastModifiedDate
	 */
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
}
