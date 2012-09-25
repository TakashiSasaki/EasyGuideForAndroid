package jp.ac.ehime_u.cite.sasaki.easyguide.downloader;

import java.net.MalformedURLException;

import java.net.URI;
import java.net.URISyntaxException;

import com.gmail.takashi316.lib.android.wifi.WifiListDialog;

import jp.ac.ehime_u.cite.sasaki.easyguide.db.DownloadedItemTable;
import jp.ac.ehime_u.cite.sasaki.easyguide.db.SourceTable;
import jp.ac.ehime_u.cite.sasaki.easyguide.download.Domain;
import jp.ac.ehime_u.cite.sasaki.easyguide.download.DownloadedItem;
import jp.ac.ehime_u.cite.sasaki.easyguide.download.Source;
import jp.ac.ehime_u.cite.sasaki.easyguide.download.Asset;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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
public class SourcesActivity extends CommonMenuActivity {
	final SourcesActivity self = this;
	private ListView listViewUrls;
	private EditText editTextDomain;
	private EditText editTextUri;
	private ImageButton imageButtonWifi;
	// ZipUrisSQLiteOpenHelper zipUrisSQLiteOpenHelper;
	Asset asset;
	private SourceTable sourceTable;

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
		this.asset = Asset.GetTheInstance(this);

		this.listViewUrls = (ListView) findViewById(R.id.listViewUrls);
		this.editTextDomain = (EditText) findViewById(R.id.editTextDomain);
		this.editTextUri = (EditText) findViewById(R.id.editTextUrl);

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

		Refresh();
		this.SetButtonRegisterListeners();
		this.SetListViewUrls();
		this.SetButtonRefresh();
	}// onCreate

	private void Refresh() {
		this.asset.EnumerateSources();
		Log.v(new Throwable(), "Delete and insert " + this.asset.size()
				+ " source items.");
		this.sourceTable.DeleteAll();
		this.sourceTable.Delete(this.asset);
		this.sourceTable.Insert(this.asset);
		SetListViewUrls();
	}

	private void SetButtonRefresh() {
		Button b = (Button) findViewById(R.id.buttonRefresh);
		OnClickListener l = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Refresh();
			}
		};
		b.setOnClickListener(l);
	}

	private void SetListViewUrls() {
		OnItemClickListener l = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String uri_string = (String) parent.getItemAtPosition(position);
				String domain_string;
				try {
					domain_string = SourcesActivity.this.sourceTable
							.GetDomainByUrl(new URI(uri_string));
				} catch (URISyntaxException e) {
					Log.v(new Throwable(), e.getMessage());
					return;
				}
				Source s;
				try {
					s = new Source(new Domain(domain_string), new URI(
							uri_string));
				} catch (MalformedURLException e) {
					Log.v(new Throwable(), e.getMessage());
					return;
				} catch (URISyntaxException e) {
					Log.v(new Throwable(), e.getMessage());
					return;
				}
				DownloadFromSource(s);
			}// onItemClick
		};// OnItemClickListener
		this.listViewUrls.setOnItemClickListener(l);
		this.listViewUrls.setAdapter(this.sourceTable.GetArrayAdapter());
	}// SetListViewUrls

	private void DownloadFromSource(Source source) {
		try {
			DownloadedItem di = source.Download(this);
			DownloadedItemTable.getInstance(this).Insert(di);
		} catch (URISyntaxException e) {
			Log.v(new Throwable(), e.getMessage());
		} catch (InterruptedException e) {
			Log.v(new Throwable(), e.getMessage());
		}// try
	}

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
