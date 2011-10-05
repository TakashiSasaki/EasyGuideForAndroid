package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
	public static class Exception extends RuntimeException {
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
	 * @param url_
	 * @throws IOException
	 */
	public ZipDownloader(Domain domain_, URL url_) {
		this.domainDirectory = domain_.getDomainDirectory();
		this.sourceUrl = url_;
		try {
			this.downloadedFile = File.createTempFile("temp", ".zip");
		} catch (IOException e) {
			throw new Exception("Can't create temporary file. "
					+ e.getMessage());
		}
	}

	/**
	 * @throws Exception 
	 * 
	 */
	public void GetMethod() throws Exception {
		final HttpGet http_get = new HttpGet();
		final URI source_uri;
		try {
			source_uri = sourceUrl.toURI();
		} catch (URISyntaxException e1) {
			throw new Exception(sourceUrl.toString()
					+ " seems to be malformed. " + e1.getMessage());
		}
		http_get.setURI(source_uri);

		class DownloadRunnable implements Runnable {
			public HttpResponse http_response;
			private HttpClient http_client;
			public Exception exception;

			@Override
			public void run() {
				http_client = new DefaultHttpClient();
				http_client.getParams().setParameter("http.connection.timeout",
						new Integer(10000));
				try {
					http_response = http_client.execute(http_get);
				} catch (ClientProtocolException e1) {
					this.exception = new Exception("Getting "
							+ source_uri.toString() + " failed. "
							+ e1.getMessage());
				} catch (IOException e1) {
					this.exception = new Exception("Getting "
							+ source_uri.toString() + " failed. "
							+ e1.getMessage());
				}// try
			}// run()
		}

		DownloadRunnable download_runnable = new DownloadRunnable();
		Thread thread = new Thread(download_runnable);
		Log.v(this.getClass().getSimpleName(), "Starting a thread to download "
				+ source_uri.toString());
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e1) {
			throw new Exception("Can't download " + source_uri.toString()
					+ ". " + e1.getMessage());
		}
		if(download_runnable.exception!=null){
			throw download_runnable.exception;
		}

		int http_status_code = download_runnable.http_response.getStatusLine()
				.getStatusCode();
		if (http_status_code == HttpStatus.SC_REQUEST_TIMEOUT) {
			throw new Exception("request for " + this.sourceUrl.toString()
					+ " timed out.");
		}
		if (http_status_code == HttpStatus.SC_NOT_FOUND) {
			throw new Exception(this.sourceUrl.toString() + " was not found.");
		}
		if (http_status_code != HttpStatus.SC_OK) {
			throw new Exception("response : "
					+ download_runnable.http_response.getStatusLine()
							.toString());
		}
		WriteDownloadedFile(download_runnable.http_response);
		this.downloadedDate = new Date();
		try {
			this.lastModifiedDate = ParseLastModified(download_runnable.http_response);
		} catch (ParseException e) {
			Log.v(this.getClass().getSimpleName(),
					"Can't parse Last-Modified value in HTTP response header.");
			this.lastModifiedDate = null;
		}
	}

	private void WriteDownloadedFile(HttpResponse http_response) {
		InputStream input_stream;
		try {
			input_stream = http_response.getEntity().getContent();
		} catch (IllegalStateException e1) {
			throw new Exception("Unable to get content from HTTP response. "
					+ e1.getMessage());
		} catch (IOException e1) {
			throw new Exception("Unable to get content from HTTP response. "
					+ e1.getMessage());
		}
		BufferedInputStream buffered_input_stream = new BufferedInputStream(
				input_stream, bufferSize);
		Log.v(this.getClass().getSimpleName(), "Writing downloaded file to "
				+ downloadedFile.getAbsolutePath());
		BufferedOutputStream buffered_output_stream;
		try {
			buffered_output_stream = new BufferedOutputStream(
					new FileOutputStream(downloadedFile));
		} catch (FileNotFoundException e) {
			throw new Exception("Unable to open "
					+ downloadedFile.getAbsolutePath() + " for output. "
					+ e.getMessage());
		}
		byte buffer[] = new byte[bufferSize];
		int read_size;
		try {
			while ((read_size = buffered_input_stream.read(buffer)) != -1) {
				buffered_output_stream.write(buffer, 0, read_size);
			}
		} catch (IOException e) {
			throw new Exception(
					"Unable to copy from HTTP response to output stream. "
							+ e.getMessage());
		}
		Log.v(this.getClass().getSimpleName(), "Closing downloaded file to "
				+ downloadedFile.getAbsolutePath());
		try {
			buffered_output_stream.flush();
		} catch (IOException e) {
			throw new Exception("Unable to flush output stream. "
					+ e.getMessage());
		}
		try {
			buffered_output_stream.close();
		} catch (IOException e) {
			throw new Exception("Unable to close output stream. "
					+ e.getMessage());
		}
		try {
			buffered_input_stream.close();
		} catch (IOException e) {
			throw new Exception(
					"Unable to close HTTP response as a input stream. "
							+ e.getMessage());
		}
	}

	/**
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws ParseException
	 */
	public void HeadMethod() {
		HttpClient http_client = new DefaultHttpClient();
		HttpHead http_head = new HttpHead();
		try {
			http_head.setURI(this.sourceUrl.toURI());
		} catch (URISyntaxException e1) {
			throw new Exception("URL " + this.sourceUrl.toString()
					+ " seems to be malformed. " + e1.getMessage());
		}
		HttpResponse http_response;
		try {
			http_response = http_client.execute(http_head);
		} catch (ClientProtocolException e1) {
			throw new Exception("Can't execute HEAD method to "
					+ this.sourceUrl.toString() + ". " + e1.getMessage());
		} catch (IOException e1) {
			throw new Exception("Can't execute HEAD method to "
					+ this.sourceUrl.toString() + ". " + e1.getMessage());
		}
		try {
			this.lastModifiedDate = this.ParseLastModified(http_response);
		} catch (ParseException e) {
			Log.v(this.getClass().getSimpleName(),
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
	public void Unzip() {
		ZipFile zip_file;
		try {
			zip_file = new ZipFile(this.downloadedFile);
		} catch (ZipException e) {
			throw new Exception("Unable to open "
					+ this.downloadedFile.getAbsolutePath()
					+ " as a ZIP file. " + e.getMessage());
		} catch (IOException e) {
			throw new Exception("Unable to open "
					+ this.downloadedFile.getAbsolutePath()
					+ " as a ZIP file. " + e.getMessage());
		}
		Enumeration<? extends ZipEntry> zip_entries = zip_file.entries();
		while (zip_entries.hasMoreElements()) {
			ZipEntry zip_entry = zip_entries.nextElement();
			File destination_file = new File(this.domainDirectory,
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

	/**
	 * @param http_client
	 * @return insecure HTTP client accepting invalid certification.
	 */
	public static HttpClient GetInsecureHttpClient(HttpClient http_client) {
		X509TrustManager x509_trust_manager = new X509TrustManager() {

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}

			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}
		};

		SSLContext ssl_context;
		try {
			ssl_context = SSLContext.getInstance("TLS");
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("Can't get instance of SSLContext. "
					+ e.getMessage());
		}
		try {
			ssl_context.init(null, new TrustManager[] { x509_trust_manager },
					null);
		} catch (KeyManagementException e) {
			throw new Exception("Can't initialize SSLContext. "
					+ e.getMessage());
		}

		// TODO: not implemented completely.
		return http_client;
	}

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
