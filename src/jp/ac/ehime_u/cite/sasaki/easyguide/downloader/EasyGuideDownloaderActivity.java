package jp.ac.ehime_u.cite.sasaki.easyguide.downloader;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Domain;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.DownloadThread;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.ZipUrl;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.ZippedAssets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class EasyGuideDownloaderActivity extends Activity {
	private static final long DOWNLOAD_WAIT = 100;
	final EasyGuideDownloaderActivity self = this;

	@SuppressWarnings("serial")
	static class Exception extends RuntimeException {

		public Exception(String message_) {
			super(message_);
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		DownloadZipInAssets();

		((Button) findViewById(R.id.buttonRegister))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						String url_string = ((EditText) findViewById(R.id.editTextUrl))
								.getEditableText().toString();
						URI uri;
						try {
							uri = new URI(url_string);
						} catch (URISyntaxException e) {
							e.printStackTrace();
							return;
						}// try

						String domain_string = ((EditText) findViewById(R.id.editTextDomain))
								.getEditableText().toString();
						Domain domain = new Domain(domain_string);

						ZipUrlsHelper zip_urls_helper = ZipUrlsHelper
								.GetTheZipUrls(arg0.getContext());
						ZipUrl zip_url;
						try {
							zip_url = new ZipUrl(domain, uri);
						} catch (MalformedURLException e) {
							e.printStackTrace();
							return;
						} catch (URISyntaxException e) {
							e.printStackTrace();
							return;
						}// try
						zip_urls_helper.Insert(zip_url);
						ListView list_view = (ListView) findViewById(R.id.listViewUrls);
						list_view.setAdapter(ZipUrlsHelper.GetTheZipUrls(
								EasyGuideDownloaderActivity.this)
								.GetArrayAdapter(
										EasyGuideDownloaderActivity.this));
					}// onClick
				});// OnClicKListener

		// TODO: this button no longer exists
		/*
		 * ((Button) findViewById(100)).setOnClickListener(new OnClickListener()
		 * {
		 * 
		 * @Override public void onClick(View arg0) { final String url_string =
		 * ((EditText) findViewById(R.id.editTextUrl))
		 * .getEditableText().toString(); Handler handler = new Handler();
		 * handler.post(new Runnable() {
		 * 
		 * @Override public void run() { DeployOrganization(url_string); }// run
		 * });// Runnable }// onClick });// OnClickListener
		 */

		ListView list_view = (ListView) findViewById(R.id.listViewUrls);
		list_view.setAdapter(ZipUrlsHelper.GetTheZipUrls(this).GetArrayAdapter(
				this));
		list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				ListView list_view = (ListView) parent;
				final String url_string = (String) list_view
						.getItemAtPosition(position);
				Handler handler = new Handler();
				handler.post(new Runnable() {
					@Override
					public void run() {
						SetEditableText(url_string);
					}// run
				});// Runnable
			}// onItemClick
		});// OnItemClickListener

		((Button) findViewById(R.id.buttonQuit))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(final View arg0) {
						new Handler().post(new Runnable() {
							@Override
							public void run() {
								moveTaskToBack(true);
							}// run
						});// Runnable
					}// onClick
				});// OnClickListener

		((Button) findViewById(R.id.buttonPlay))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						new Handler().post(new Runnable() {

							@Override
							public void run() {
								InvokePlayer();
							}// run
						});// post
					}// onClick
				});// OnClickListener

		((Button) findViewById(R.id.buttonListContents))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						new Handler().post(new Runnable() {
							@Override
							public void run() {
								InvokeSummary();
							}// run
						});// post
					}// onClick
				});// OnClickListener
	}// onCreate

	/*
	 * DownloadZipInAssets removes all organization directories which belongs to
	 * each domain. Then it copies ZIP stream from assets to corresponding files
	 * just each domain directory. Finally zip_urls database is updated.
	 */
	private void DownloadZipInAssets() {
		ZippedAssets zipped_assets = new ZippedAssets(this);
		for (ZipUrl zip_url : zipped_assets) {
			zip_url.GetDomain().RemoveAllOrganizations();
		}// for
		for (ZipUrl zip_url : zipped_assets) {
			try {
				zip_url.SetDownloadedFile();
				DownloadThread download_thread = new DownloadThread(zip_url,
						this);
				download_thread.start();
				Thread.sleep(DOWNLOAD_WAIT, 0);
				download_thread.join();
			} catch (URISyntaxException e) {
				e.printStackTrace();
				continue;
			} catch (InterruptedException e) {
				e.printStackTrace();
				continue;
			}// try
		}// for
		ZipUrlsHelper zip_urls_helper = ZipUrlsHelper.GetTheZipUrls(this);
		zip_urls_helper.Insert(zipped_assets);
	}// DownloadZipInAssets

	private void SetEditableText(String url_) {
		((EditText) findViewById(R.id.editTextUrl)).setText(url_);
	}

	/*
	 * private void DeployOrganization(String url_string) { URL url; try { url =
	 * new URL(url_string); } catch (MalformedURLException e) { throw new
	 * Exception(e.getMessage()); } if
	 * (url.getHost().equalsIgnoreCase("assets")) {
	 * Log.v(this.getClass().getSimpleName(),
	 * "Clicked URL represents a file in assets.");
	 * InflateZipFileInAssets(url.getFile()); } else if
	 * (url.getHost().equalsIgnoreCase("localhost")) {
	 * Log.v(this.getClass().getSimpleName(),
	 * "Clicked URL represents a file in local file system.");
	 * InflateLocalZipFile(url.getPath()); } else {
	 * Log.v(this.getClass().getSimpleName(),
	 * "Clicked URL represents a file on the Internet.");
	 * DownloadAndInflateZipFile(url); } }
	 */

	/*
	 * private void DownloadAndInflateZipFile(URL url_) { Domain domain; domain
	 * = new Domain(url_.getHost()); ZipDownloader zip_downloader;
	 * zip_downloader = new ZipDownloader(domain, url_); try {
	 * zip_downloader.GetMethod(); } catch (ZipDownloader.Exception e) {
	 * AlertDialog.Builder alert_dialog_builder = new AlertDialog.Builder(
	 * this); alert_dialog_builder.setCancelable(false);
	 * alert_dialog_builder.setMessage("コンテンツをダウンロードできません");
	 * alert_dialog_builder.setPositiveButton("戻る", new
	 * DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface arg0, int arg1) { } });
	 * alert_dialog_builder.show(); return; } ZipInflator zip_inflator; try {
	 * zip_inflator = new ZipInflator(zip_downloader.getDownloadedFile(),
	 * zip_downloader.getDomainDirectory()); } catch (FileNotFoundException e) {
	 * throw new Exception("Can't find downloaded ZIP file. " + e.getMessage());
	 * } zip_inflator.Inflate(); }
	 */

	private void InvokePlayer() {
		Intent intent = new Intent();
		intent.setClassName("jp.ac.ehime_u.cite.sasaki.easyguide.player",
				"jp.ac.ehime_u.cite.sasaki.easyguide.player.OpeningActivity");
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("EasyGuide Player is not installed");
			builder.setCancelable(false);
			builder.setPositiveButton("OK", null);
			builder.show();
		}// try
	}// InvokePlayer

	private void InvokeSummary() {
		Intent intent = new Intent();
		intent.setClass(this, SummaryActivity.class);
		startActivity(intent);
	}// InvokeSummary
}// EasyGuideDownloader
