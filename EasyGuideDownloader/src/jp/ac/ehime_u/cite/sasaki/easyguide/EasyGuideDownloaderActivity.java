package jp.ac.ehime_u.cite.sasaki.easyguide;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Iterator;

import jp.ac.ehime_u.cite.sasaki.easyguide.model.Domain;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Root;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.ZipDownloader;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.ZippedAssets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
		
		ZippedAssets zipped_assets = ZippedAssets.GetTheZippedAssets(this);
		ZipUrls zip_urls = ZipUrls.GetTheZipUrls(this);
		for(Iterator<URL> i=zipped_assets.iterator(); i.hasNext(); ){
			zip_urls.PutZipUrl(i.next());
		}
		
		((Button) findViewById(R.id.buttonRemember))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						String url_string = ((EditText) findViewById(R.id.editTextUrl))
								.getEditableText().toString();
						URL url;
						try {
							url = new URL(url_string);
						} catch (MalformedURLException e) {
							Log.e(this.getClass().getSimpleName(),
									"Malformed URL " + url_string);
							return;
						}
						ZipUrls.GetTheZipUrls(arg0.getContext()).PutZipUrl(url);
						EasyGuideDownloaderActivity activity = (EasyGuideDownloaderActivity) arg0
								.getContext();
						ListView list_view = (ListView) activity
								.findViewById(R.id.listViewUrls);
						list_view.setAdapter(ZipUrls.GetTheZipUrls(activity)
								.GetArrayAdapter(activity));
					}
				});

		((Button) findViewById(R.id.buttonDeploy))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						final String url_string = ((EditText) findViewById(R.id.editTextUrl))
								.getEditableText().toString();
						Handler handler = new Handler();
						handler.post(new Runnable() {
							@Override
							public void run() {
								DownloadZipFile(url_string);
							}
						});
					}
				});

		ListView list_view = (ListView) findViewById(R.id.listViewUrls);
		list_view.setAdapter(ZipUrls.GetTheZipUrls(this).GetArrayAdapter(this));
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
					}
				});
			}// onItemClick
		});
	}// onCreate

	private void SetEditableText(String url_) {
		((EditText) findViewById(R.id.editTextUrl)).setText(url_);
	}

	private void DownloadZipFile(String url_) {
		URL url;
		try {
			url = new URL(url_);
		} catch (MalformedURLException e) {
			throw new Exception(e.getMessage());
		}
		Root root = new Root();
		Domain domain;
		try {
			domain = new Domain(root, url);
		} catch (UnknownHostException e) {
			throw new Exception(e.getMessage());
		}
		ZipDownloader zip_downloader;
		zip_downloader = new ZipDownloader(domain, url);
		try {
			zip_downloader.GetMethod();
		} catch (ZipDownloader.Exception e) {
			AlertDialog.Builder alert_dialog_builder = new AlertDialog.Builder(
					this);
			alert_dialog_builder.setCancelable(false);
			alert_dialog_builder.setMessage("コンテンツをダウンロードできません");
			alert_dialog_builder.setPositiveButton("戻る",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
						}
					});
			alert_dialog_builder.show();
			return;
		}
		zip_downloader.Unzip();
	}
}
