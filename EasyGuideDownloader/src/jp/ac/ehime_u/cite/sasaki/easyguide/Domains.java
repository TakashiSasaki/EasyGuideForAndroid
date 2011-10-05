package jp.ac.ehime_u.cite.sasaki.easyguide;

import java.net.URL;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ArrayAdapter;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class Domains {
	static MyOpenHelper myOpenHelper;

	/**
	 * @param context_
	 * 
	 */
	public Domains(Context context_) {
		myOpenHelper = new MyOpenHelper(context_);
	}

	/**
	 * @param url_
	 */
	public void RegisterUrl(URL url_) {
		// SQLiteDatabase readable_database =
		// myOpenHelper.getReadableDatabase();
		// readable_database.close();
		SQLiteDatabase writable_database = myOpenHelper.getWritableDatabase();
		ContentValues content_values = new ContentValues();
		content_values.put("zip_url", url_.toString());
		content_values.put("domain", url_.getHost());
		Log.d(this.getClass().getSimpleName(),
				"domain part = " + url_.getHost());
		writable_database.insert("zip_urls", null, content_values);
		writable_database.close();
	}

	/**
	 * @param activity_
	 * @return array adapter for list view
	 */
	public ArrayAdapter<String> GetArrayAdapter(Activity activity_) {
		SQLiteDatabase readable_database = myOpenHelper.getReadableDatabase();
		Cursor cursor = readable_database.query("zip_urls",
				new String[] { "zip_url" }, null, null, null, null, null);

		ArrayAdapter<String> array_adapter = new ArrayAdapter<String>(activity_,
				android.R.layout.simple_list_item_1);

		for (boolean record_exists = cursor.moveToFirst(); record_exists; record_exists = cursor
				.moveToNext()) {
			array_adapter.add(cursor.getString(0));
		}
		cursor.close();
		readable_database.close();
		return array_adapter;
	}
}
