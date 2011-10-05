package jp.ac.ehime_u.cite.sasaki.easyguide;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Domain;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Root;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.ZipDownloader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

		((Button) findViewById(R.id.buttonAddUrl))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						String url_to_be_added = ((EditText) findViewById(R.id.editTextUrl))
								.getEditableText().toString();

						URL url;
						try {
							url = new URL(url_to_be_added);
						} catch (MalformedURLException e) {
							throw new Exception("Malformed URL "
									+ url_to_be_added + ". " + e.getMessage());
						}
						EasyGuideDownloaderActivity activity = (EasyGuideDownloaderActivity) arg0
								.getContext();
						ZipUrls.GetTheZipUrls(activity).PutZipUrl(url);
						ListView list_view = (ListView) activity
								.findViewById(R.id.listViewUrls);
						list_view.setAdapter(ZipUrls.GetTheZipUrls(activity)
								.GetArrayAdapter(activity));
					}
				});

		ListView list_view = (ListView) findViewById(R.id.listViewUrls);
		list_view.setAdapter(ZipUrls.GetTheZipUrls(this).GetArrayAdapter(this));
		list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ListView list_view = (ListView) parent;
				final String zip_url = (String) list_view
						.getItemAtPosition(position);
				Handler handler = new Handler();
				handler.post(new Runnable() {
					@Override
					public void run() {
						DownloadZipFile(zip_url);
					}
				});
			}// onItemClick
		});
	}// onCreate

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
