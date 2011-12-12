package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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

	public LastModifiedHeaderThread(URL url) throws URISyntaxException {
		// this.simpleDataFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		uri = url.toURI();
		httpHead = new HttpHead(uri);
		httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter("http.connection.timeout",
				new Integer(10000));
	}// a constructor

	@Override
	public void run() {
		try {
			httpResponse = httpClient.execute(httpHead);
			lastModifiedHeader = httpResponse.getLastHeader("Last-Modified");
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
			this.lastModified = simpleDataFormat.parse(lastModifiedHeader
					.getValue());
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}
		int http_status_code = httpResponse.getStatusLine().getStatusCode();
		if (http_status_code == HttpStatus.SC_REQUEST_TIMEOUT) {
			throw new RuntimeException("request for " + this.uri.toString()
					+ " timed out.");
		}
		if (http_status_code == HttpStatus.SC_NOT_FOUND) {
			throw new RuntimeException(uri.toString() + " was not found.");
		}
		if (http_status_code != HttpStatus.SC_OK) {
			throw new RuntimeException("response : "
					+ httpResponse.getStatusLine().toString());
		}
		assert (this.lastModified != null);
	}// run

	public Date getLastModified() {
		return lastModified;
	}
}// LastModifiedHeader
