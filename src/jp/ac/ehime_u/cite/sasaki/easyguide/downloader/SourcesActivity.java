package jp.ac.ehime_u.cite.sasaki.easyguide.downloader;

import java.net.MalformedURLException;

import java.net.URI;
import java.net.URISyntaxException;

import jp.ac.ehime_u.cite.sasaki.easyguide.db.DownloadedItemTable;
import jp.ac.ehime_u.cite.sasaki.easyguide.db.SourceTable;
import jp.ac.ehime_u.cite.sasaki.easyguide.download.Domain;
import jp.ac.ehime_u.cite.sasaki.easyguide.download.DownloadedItem;
import jp.ac.ehime_u.cite.sasaki.easyguide.download.Source;
import jp.ac.ehime_u.cite.sasaki.easyguide.download.Asset;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ImageButton;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class SourcesActivity extends CommonMenuActivity {
	final SourcesActivity self = this;
	private ListView listViewUrls;
	private EditText editTextDomain;
	private EditText editTextUri;
	private Button buttonQuit;
	private Button buttonPlay;
	private Button buttonListContents;
	private ImageButton imageButtonGlossary;
	private ImageButton imageButtonWifi;
	// ZipUrisSQLiteOpenHelper zipUrisSQLiteOpenHelper;
	Asset asset;
	private SourceTable sourceTable;
	private DownloadedItemTable downloadedItemTable;

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
		this.setContentView(R.layout.sources);

		this.sourceTable = SourceTable.GetTheInstance(this);
		// this.zipUrisSQLiteOpenHelper = ZipUrisSQLiteOpenHelper
		// .GetTheInstance(this);
		this.asset = Asset.GetTheInstance(this);

		this.listViewUrls = (ListView) findViewById(R.id.listViewUrls);
		this.editTextDomain = (EditText) findViewById(R.id.editTextDomain);
		this.editTextUri = (EditText) findViewById(R.id.editTextUrl);
		this.buttonQuit = ((Button) findViewById(R.id.buttonQuit));
		this.buttonPlay = ((Button) findViewById(R.id.buttonPlay));
		this.buttonListContents = (Button) findViewById(R.id.buttonListContents);
		this.imageButtonGlossary = (ImageButton) findViewById(R.id.imageButtonGlossary);
		this.imageButtonWifi = (ImageButton) findViewById(R.id.imageButtonWifi);

		// this button no longer exists
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
		this.asset.EnumerateSources();
		// ClearZipFilesInAssets();
		// DownloadZipFilesInAssets(this);
		Log.v(new Throwable(), "Delete and insert " + this.asset.size()
				+ " source items.");
		this.sourceTable.Delete(this.asset);
		this.sourceTable.Insert(this.asset);
		// this.SetListViewUrlsAdapter();
		// zip_urls_helper.Insert(zipped_assets);
		// try {
		// DownloadZipInAssets();
		// } catch (StorageException e) {
		// Log.e(this.getClass().getSimpleName(), e.getMessage());
		// }// try
		this.SetListViewUrls();
	}// onCreate

	private void SetListViewUrls() {
		OnItemSelectedListener oisl = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Source source = (Source) arg0.getSelectedItem();
				DownloadFromSource(source);
			}// onItemSelected

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}// onNothingSelected
		};// OnItemSelectedListener
		this.listViewUrls.setOnItemSelectedListener(oisl);
		this.listViewUrls.setAdapter(this.sourceTable.GetArrayAdapter());
	}// SetListViewUrls

	private void DownloadFromSource(Source source) {
		try {
			DownloadedItem di = source.Download(this);
			this.downloadedItemTable.Insert(di);
		} catch (URISyntaxException e) {
			Log.v(new Throwable(), e.getMessage());
		} catch (InterruptedException e) {
			Log.v(new Throwable(), e.getMessage());
		}// try
	}

	// private void ClearZipFilesInAssets() {
	// for (Source s : this.asset) {
	// s.GetDomain().RemoveAllOrganizations();
	// s.GetDomain().RemoveAllZipFiles();
	// this.sourceTable.Delete(s);
	// }// for
	// }// ClearZipFilesInAssets

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
				String url_string = SourcesActivity.this.editTextUri
						.getEditableText().toString();
				URI uri;
				try {
					uri = new URI(url_string);
				} catch (URISyntaxException e) {
					e.printStackTrace();
					return;
				}// try

				String domain_string = SourcesActivity.this.editTextDomain
						.getEditableText().toString();
				Domain domain = new Domain(domain_string);
				try {
					Source s = new Source(domain, uri);
					SourcesActivity.this.sourceTable.Insert(s);
				} catch (MalformedURLException e) {
					ShowAlertDialog(e.getMessage());
				} catch (URISyntaxException e) {
					ShowAlertDialog(e.getMessage());
				}
				// SetListViewUrlsAdapter();
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
		this.listViewUrls.setOnItemClickListener(o);// OnItemClickListener
	}// SetListViewListeners

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
		this.buttonListContents.setOnClickListener(new OnClickListener() {
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
		this.imageButtonGlossary.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Glossary glossary = new Glossary(SourcesActivity.this.self);
				glossary.show();
			}// onClick
		});// OnClickListener()
	}// SetImageButtonglossaryListeners

	private void SetImageButtonWifiListeners() {
		this.imageButtonWifi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				WifiListDialog wifi_list_dialog = new WifiListDialog(
						SourcesActivity.this.self);
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
