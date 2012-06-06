package jp.ac.ehime_u.cite.sasaki.easyguide.download;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;

public class LastModifiedHeaderThread extends Thread {

	private HttpResponse httpResponse;
	final private HttpClient httpClient;
	final private URI uri;
	final private HttpHead httpHead;
	final SimpleDateFormat simpleDataFormat = new SimpleDateFormat(
			"EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
	private Date lastModified;
	private Header lastModifiedHeader;

	public LastModifiedHeaderThread(URI uri_) throws URISyntaxException {
		// this.simpleDataFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		this.uri = uri_;
		this.httpHead = new HttpHead(this.uri);
		this.httpClient = new DefaultHttpClient();
		this.httpClient.getParams().setParameter("http.connection.timeout",
				new Integer(10000));
	}// a constructor

	@Override
	public void run() {
		try {
			this.httpResponse = this.httpClient.execute(this.httpHead);
			this.lastModifiedHeader = this.httpResponse
					.getLastHeader("Last-Modified");
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
			return;
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return;
		}// try

		try {
			this.lastModified = this.simpleDataFormat
					.parse(this.lastModifiedHeader.getValue());
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}
		int http_status_code = this.httpResponse.getStatusLine()
				.getStatusCode();
		if (http_status_code == HttpStatus.SC_REQUEST_TIMEOUT) {
			throw new RuntimeException("request for " + this.uri.toString()
					+ " timed out.");
		}
		if (http_status_code == HttpStatus.SC_NOT_FOUND) {
			throw new RuntimeException(this.uri.toString() + " was not found.");
		}
		if (http_status_code != HttpStatus.SC_OK) {
			throw new RuntimeException("response : "
					+ this.httpResponse.getStatusLine().toString());
		}
		assert (this.lastModified != null);
	}// run

	public Date getLastModified() {
		return this.lastModified;
	}// getLastModified
}// LastModifiedHeader
