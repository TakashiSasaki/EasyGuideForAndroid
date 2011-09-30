package jp.ac.ehime_u.cite.sasaki.easyguide;

import java.net.MalformedURLException;
import java.net.URL;

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

public class EasyGuideDownloaderActivity extends Activity {

	static MyOpenHelper myOpenHelper;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		myOpenHelper = new MyOpenHelper(this);
		RenewUrlListView();

		((Button) findViewById(R.id.buttonAddUrl))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// DatabaseHelper database_helper = new
						// DatabaseHelper(arg0.getContext());
						SQLiteDatabase readable_database = myOpenHelper
								.getReadableDatabase();
						readable_database.close();
						SQLiteDatabase writable_database = myOpenHelper
								.getWritableDatabase();
						ContentValues content_values = new ContentValues();
						String url_to_be_added = ((EditText) findViewById(R.id.editTextUrl))
								.getEditableText().toString();
						try {
							URL url = new URL(url_to_be_added);
							content_values.put("zip_url", url.toString());
							content_values.put("domain", url.getHost());
							Log.d("EasyGuideDownloader",
									"domain part = " + url.getHost());
						} catch (MalformedURLException e) {
							Log.e("EasyGuideDownloader",
									"Malformed URL given, " + url_to_be_added);
						}
						// Log.d("EasyGuideDownloader",url_to_be_added);
						writable_database.insert("zip_urls", null,
								content_values);
						writable_database.close();
						EasyGuideDownloaderActivity activity = (EasyGuideDownloaderActivity) arg0
								.getContext();
						activity.RenewUrlListView();
					}
				});

		ListView list_view = (ListView) findViewById(R.id.listViewUrls);
		list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ListView list_view = (ListView)parent;
				String zip_url = (String)list_view.getItemAtPosition(position);
				
				Activity activity = (Activity)view.getContext();
				EditText edit_text = (EditText)activity.findViewById(R.id.editTextUrl);
				edit_text.setText(zip_url);
				Button button = (Button)activity.findViewById(R.id.buttonAddUrl);
				button.setClickable(false);
			}

		});
	}

	private void RenewUrlListView() {
		SQLiteDatabase readable_database = myOpenHelper.getReadableDatabase();
		Cursor cursor = readable_database.query("zip_urls",
				new String[] { "zip_url" }, null, null, null, null, null);

		ArrayAdapter<String> array_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		// array_adapter.add("test string");

		for (boolean record_exists = cursor.moveToFirst(); record_exists; record_exists = cursor
				.moveToNext()) {
			array_adapter.add(cursor.getString(0));
		}
		cursor.close();
		readable_database.close();
		// Log.d("EasyGuideDownloader", readable_database.isOpen() ?
		// "open":"close");

		ListView list_view = (ListView) findViewById(R.id.listViewUrls);
		list_view.setAdapter(array_adapter);
	}
}
