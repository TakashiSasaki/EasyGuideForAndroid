package jp.ac.ehime_u.cite.sasaki.easyguide.download;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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

public class Source {
	private URI uri;
	private String domain;
	private Date lastModified;

	public Source(String domain, URI uri_) throws URISyntaxException,
			MalformedURLException, InterruptedException {
		this.uri = uri;
		this.domain = domain;
		if (!this.uri.getHost().equals("assets")) {
			if (this.uri.getHost() != "assets") {
				updateLastModified();
			}
		}// if
	} // a constructor

	public void updateLastModified() throws InterruptedException {
		new Thread() {
			@Override
			public void run() {
				HttpClient http_client = new DefaultHttpClient();
				http_client.getParams().setParameter("http.connection.timeout",
						new Integer(10000));
				HttpHead http_head = new HttpHead(uri);
				HttpResponse http_response;
				try {
					http_response = http_client.execute(http_head);
					Header last_modified_header = http_response
							.getLastHeader("Last-Modified");
					final SimpleDateFormat simple_date_format = new SimpleDateFormat(
							"EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
					lastModified = simple_date_format
							.parse(last_modified_header.getValue());
					int http_status_code = http_response.getStatusLine()
							.getStatusCode();
					if (http_status_code == HttpStatus.SC_REQUEST_TIMEOUT) {
						throw new RuntimeException("request for "
								+ uri.toString() + " timed out.");
					}
					if (http_status_code == HttpStatus.SC_NOT_FOUND) {
						throw new RuntimeException(uri.toString()
								+ " was not found.");
					}
					if (http_status_code != HttpStatus.SC_OK) {
						throw new RuntimeException("response : "
								+ http_response.getStatusLine().toString());
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				assert (lastModified != null);
			}// run
		}.start();
	}// updateLastModified

	public URI getUri() {
		return this.uri;
	}
}// Source
