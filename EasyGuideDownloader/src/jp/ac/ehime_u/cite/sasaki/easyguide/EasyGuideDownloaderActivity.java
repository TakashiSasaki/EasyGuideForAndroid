package jp.ac.ehime_u.cite.sasaki.easyguide;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Domain;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Root;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.ZipDownloader;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class EasyGuideDownloaderActivity extends Activity {
	private static Domains domains;

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
		domains = new Domains(this);

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
						domains.RegisterUrl(url);
						EasyGuideDownloaderActivity activity = (EasyGuideDownloaderActivity) arg0
								.getContext();
						ListView list_view = (ListView) activity
								.findViewById(R.id.listViewUrls);
						list_view.setAdapter(domains.GetArrayAdapter(activity));
					}
				});

		ListView list_view = (ListView) findViewById(R.id.listViewUrls);
		list_view.setAdapter(domains.GetArrayAdapter(this));
		list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ListView list_view = (ListView) parent;
				String zip_url = (String) list_view.getItemAtPosition(position);

				EasyGuideDownloaderActivity activity = (EasyGuideDownloaderActivity) view
						.getContext();
				EditText edit_text = (EditText) activity
						.findViewById(R.id.editTextUrl);
				edit_text.setText(zip_url);
				Button button = (Button) activity
						.findViewById(R.id.buttonAddUrl);
				button.setClickable(false);
				activity.DownloadZipFile(zip_url);
				button.setClickable(true);
			}
		});
	}

	/**
	 * 
	 * 
	 * @param url_
	 */
	public void DownloadZipFile(String url_) {
		// ダウンロード元のURL
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
		zip_downloader.GetMethod();
		zip_downloader.Unzip();
	}
}
