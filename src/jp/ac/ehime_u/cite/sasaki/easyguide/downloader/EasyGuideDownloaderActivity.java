package jp.ac.ehime_u.cite.sasaki.easyguide.downloader;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import jp.ac.ehime_u.cite.sasaki.easyguide.exception.StorageException;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Domain;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.DownloadThread;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Source;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.ZipFilesInAssets;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ImageButton;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class EasyGuideDownloaderActivity extends Activity {
	private static final long DOWNLOAD_WAIT = 100;
	final EasyGuideDownloaderActivity self = this;
	private ListView listViewUrls;
	private EditText editTextDomain;
	private EditText editTextUri;
	private Button buttonQuit;
	private Button buttonPlay;
	private Button buttonListContents;
	private ImageButton imageButtonGlossary;
	private ImageButton imageButtonWifi;
	ZipUrisSQLiteOpenHelper zipUrisSQLiteOpenHelper;
	ZipFilesInAssets zipFilesInAssets;

	@SuppressWarnings("serial")
	static class Exception extends RuntimeException {

		public Exception(String message_) {
			super(message_);
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v(this.getClass().getSimpleName(), "onCreate");
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.sources);

		this.zipUrisSQLiteOpenHelper = ZipUrisSQLiteOpenHelper
				.GetTheInstance(this);
		this.zipFilesInAssets = ZipFilesInAssets.GetTheInstance(this);

		this.listViewUrls = (ListView) findViewById(R.id.listViewUrls);
		this.editTextDomain = (EditText) findViewById(R.id.editTextDomain);
		this.editTextUri = (EditText) findViewById(R.id.editTextUrl);
		this.buttonQuit = ((Button) findViewById(R.id.buttonQuit));
		this.buttonPlay = ((Button) findViewById(R.id.buttonPlay));
		this.buttonListContents = (Button) findViewById(R.id.buttonListContents);
		this.imageButtonGlossary = (ImageButton) findViewById(R.id.imageButtonGlossary);
		this.imageButtonWifi = (ImageButton) findViewById(R.id.imageButtonWifi);

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

		this.SetListViewListeners();
		this.SetButtonRegisterListeners();
		this.SetButtonQuitListeners();
		this.SetButtonPlayListeners();
		this.SetButtonListContentsListeners();
		this.SetImageButtonGlossaryListeners();

		// ZipUrlsHelper zip_urls_helper = ZipUrlsHelper.GetTheZipUrls(this);
		ClearZipFilesInAssets();
		DownloadZipFilesInAssets(this);
		this.zipFilesInAssets.ScanAssets();
		this.zipUrisSQLiteOpenHelper.Insert(this.zipFilesInAssets);
		this.SetListViewUrlsAdapter();
		// zip_urls_helper.Insert(zipped_assets);
		// try {
		// DownloadZipInAssets();
		// } catch (StorageException e) {
		// Log.e(this.getClass().getSimpleName(), e.getMessage());
		// }// try
	}// onCreate

	private void ClearZipFilesInAssets() {
		for (Source zip_uri : zipFilesInAssets) {
			zip_uri.GetDomain().RemoveAllOrganizations();
			zip_uri.GetDomain().RemoveAllZipFiles();
			zipUrisSQLiteOpenHelper.Delete(zip_uri);
		}// for
	}// ClearZipFilesInAssets

	/*
	 * DownloadZipInAssets removes all organization directories which belongs to
	 * each domain. Then it copies ZIP stream from assets to corresponding files
	 * just each domain directory. Finally zip_urls database is updated.
	 */
	private void DownloadZipFilesInAssets(Context context_) {
		for (Source zip_uri : zipFilesInAssets) {
			try {
				zip_uri.SetDownloadedFile();
				DownloadThread download_thread = new DownloadThread(zip_uri,
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
	}// DownloadZipInAssets

	private void SetEditableText(String url_) {
		this.editTextUri.setText(url_);
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
		intent.setClass(this, ExtractedItemsActivity.class);
		startActivity(intent);
	}// InvokeSummary

	private void SetButtonRegisterListeners() {
		Button b = (Button) findViewById(R.id.buttonRegister);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String url_string = editTextUri.getEditableText().toString();
				URI uri;
				try {
					uri = new URI(url_string);
				} catch (URISyntaxException e) {
					e.printStackTrace();
					return;
				}// try

				String domain_string = editTextDomain.getEditableText()
						.toString();
				Domain domain = new Domain(domain_string);
				try {
					Source zip_uri = new Source(domain, uri);
					zipUrisSQLiteOpenHelper.Insert(zip_uri);
				} catch (MalformedURLException e) {
					ShowAlertDialog(e.getMessage());
				} catch (URISyntaxException e) {
					ShowAlertDialog(e.getMessage());
				}
				SetListViewUrlsAdapter();
			}// onClick
		});// OnClicKListener
	}// SetButtonRegisterListeners

	private void SetListViewListeners() {
		OnItemClickListener o;
		o = new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				ListView list_view = (ListView) parent;
				final String url_string = (String) list_view
						.getItemAtPosition(position);
				// Handler handler = new Handler();
				// handler.post(new Runnable() {
				// @Override
				// public void run() {
				// SetEditableText(url_string);
				// }// run
				// });// Runnable
				SetEditableText(url_string);
			}// onItemClick
		};
		listViewUrls.setOnItemClickListener(o);// OnItemClickListener
	}// SetListViewListeners

	private void SetListViewUrlsAdapter() {
		this.listViewUrls.setAdapter(this.zipUrisSQLiteOpenHelper
				.GetArrayAdapter());
	}// SetListViewUrlsAdapter

	private void SetButtonQuitListeners() {
		this.buttonQuit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						moveTaskToBack(true);
					}// run
				});// Runnable
			}// onClick
		});// OnClickListener
	}// SetButtonQuitListeners

	private void SetButtonPlayListeners() {
		this.buttonPlay.setOnClickListener(new OnClickListener() {
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
	}// SetButtonPlayListeners

	private void SetButtonListContentsListeners() {
		buttonListContents.setOnClickListener(new OnClickListener() {
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
	}// SetButtonListContentsListeners

	private void SetImageButtonGlossaryListeners() {
		imageButtonGlossary.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Glossary glossary = new Glossary(
						EasyGuideDownloaderActivity.this.self);
				glossary.show();
			}// onClick
		});// OnClickListener()
	}// SetImageButtonglossaryListeners

	private void SetImageButtonWifiListeners() {
		imageButtonWifi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				WifiListDialog wifi_list_dialog = new WifiListDialog(
						EasyGuideDownloaderActivity.this.self);
				wifi_list_dialog.show();
			}// onClick
		});// onClickListener
	}// SetImageButtonWifiListeners

	private void ShowAlertDialog(String s) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(s);
		alertDialogBuilder.create().show();
	}// ShowAlertDialog

}// EasyGuideDownloaderActivity
