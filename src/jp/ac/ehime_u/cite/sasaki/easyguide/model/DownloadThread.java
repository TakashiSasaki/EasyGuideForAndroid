package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

/**
 * represents both zipped and unzipped contents.
 * 
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class DownloadThread extends Thread {
	private final Source zipUrl;
	private final int bufferSize = 65536;
	private final HttpGet httpGet;
	private final HttpClient httpClient;
	private HttpResponse httpResponse;
	private Context context;
	private AssetManager assetManager;

	/**
	 * @param domain_
	 * @param url_
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public DownloadThread(Source zip_url, Context context_)
			throws URISyntaxException {
		this.zipUrl = zip_url;
		this.zipUrl.SetDownloadedFile();
		this.httpGet = new HttpGet(this.zipUrl.getUri());
		this.httpClient = new DefaultHttpClient();
		this.httpClient.getParams().setParameter("http.connection.timeout",
				new Integer(10000));
		this.context = context_;
		this.assetManager = this.context.getResources().getAssets();
	}// a constructor

	@Override
	public void run() {
		if (this.zipUrl.getUri().getHost().equals("assets")) {
			CopyFromAssets();
		} else {
			DownloadViaHttp();
		}
	}// run()

	private void DownloadViaHttp() {
		try {
			this.httpResponse = this.httpClient.execute(this.httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}// try
		int http_status_code = this.httpResponse.getStatusLine()
				.getStatusCode();
		if (http_status_code == HttpStatus.SC_REQUEST_TIMEOUT) {
			throw new RuntimeException("request for "
					+ this.httpGet.getURI().toString() + " timed out.");
		}// if
		if (http_status_code == HttpStatus.SC_NOT_FOUND) {
			throw new RuntimeException(this.httpGet.getURI().toString()
					+ " was not found.");
		}// if
		if (http_status_code != HttpStatus.SC_OK) {
			throw new RuntimeException("response : "
					+ this.httpResponse.getStatusLine().toString());
		}// if

		InputStream input_stream;
		try {
			input_stream = this.httpResponse.getEntity().getContent();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}// try
		BufferedInputStream buffered_input_stream = new BufferedInputStream(
				input_stream, this.bufferSize);
		SaveStream(buffered_input_stream);
	}// DownloadViaHttp

	private void CopyFromAssets() {
		InputStream input_stream;
		Pattern pattern = Pattern.compile("^/([0-9a-zA-Z_\\-.]+/.+\\.zip)$");
		String path_in_assets = this.zipUrl.getUri().getPath();
		Matcher matcher = pattern.matcher(path_in_assets);
		if (!matcher.find()) {
			throw new RuntimeException("Malformed path in assets : "
					+ path_in_assets);
		}// if
		String path_in_assets_without_heading_slash = matcher.group(1);
		Log.v(this.getClass().getSimpleName(), "Reading from "
				+ path_in_assets_without_heading_slash);
		try {
			input_stream = this.assetManager
					.open(path_in_assets_without_heading_slash);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}// try
		BufferedInputStream buffered_input_stream = new BufferedInputStream(
				input_stream);
		final int count = SaveStream(buffered_input_stream);
		Log.v(this.getClass().getSimpleName(), "" + count + " bytes wrote.");
	}// CopyFromAssets

	private int SaveStream(BufferedInputStream buffered_input_stream) {
		int count = 0;
		Log.v(this.getClass().getSimpleName(), "Writing downloaded file to "
				+ this.zipUrl.getDownloadedFile().getAbsolutePath());
		BufferedOutputStream buffered_output_stream;
		try {
			buffered_output_stream = new BufferedOutputStream(
					new FileOutputStream(this.zipUrl.getDownloadedFile()));
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
				+ this.zipUrl.getDownloadedFile().getAbsolutePath());
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
			throw new RuntimeException("Can't get instance of SSLContext. "
					+ e.getMessage());
		}
		try {
			ssl_context.init(null, new TrustManager[] { x509_trust_manager },
					null);
		} catch (KeyManagementException e) {
			throw new RuntimeException("Can't initialize SSLContext. "
					+ e.getMessage());
		}

		// TODO: not implemented completely.
		return http_client;
	}
}// DownloaderThread
