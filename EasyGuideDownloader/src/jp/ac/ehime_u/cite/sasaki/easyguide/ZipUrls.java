package jp.ac.ehime_u.cite.sasaki.easyguide;

import java.net.URL;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class ZipUrls extends SQLiteOpenHelper {

	final static String CREATE_TABLE = "CREATE TABLE zip_urls(domain TEXT NOT NULL, zip_url TEXT NOT NULL);";
	final static String DROP_TABLE = "DROP TABLE IF EXISTS zip_urls;";
	final static String INSERT_TEST = "INSERT INTO zip_urls(domain,zip_url) VALUES(\"sasakinas.cite.ehime-u.ac.jp\", \"http://sasakinas.cite.ehime-u.ac.jp/easyguide/easyguide.zip\");";

	@Override
	public void onCreate(SQLiteDatabase sqlite_database) {
		Log.d("EasyGuideDownloader",
				"MyOpenHelper#onCreate" + sqlite_database.getPath());
		Log.d("EasyGuideDownloader", CREATE_TABLE);
		sqlite_database.execSQL(CREATE_TABLE);
		Log.d("EasyGuideDownloader", INSERT_TEST);
		sqlite_database.execSQL(INSERT_TEST);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqlite_database, int old_version,
			int new_version) {
		Log.d("EasyGuideDownloader", "MyOpenHelper#onUpgrade");
		if (old_version != new_version) {
			Log.d("EasyGuideDownloader", DROP_TABLE + " old_version="
					+ old_version + ", new_version=" + new_version);
			sqlite_database.execSQL(DROP_TABLE);
			this.onCreate(sqlite_database);
		}
	}

	/**
	 * @param domain_
	 * @return zip url
	 */
	public String GetZipUrl(String domain_) {
		SQLiteDatabase readable_database = this.getReadableDatabase();
		domain_ = domain_.toLowerCase();
		Cursor cursor = readable_database.query("zip_urls",
				new String[] { "zip_url" }, "zip_url = ?",
				new String[] { domain_ }, null, null, null);
		cursor.moveToFirst();
		if (cursor.isLast()) {
			String zip_url = cursor.getString(0);
			Log.v(this.getClass().getSimpleName(), "Zip URL for domain "
					+ domain_ + " is " + zip_url);
			cursor.close();
			return zip_url;
		}
		Log.v(this.getClass().getSimpleName(), "No ZIP URL for domain "
				+ domain_);
		cursor.close();
		return null;
	}

	/**
	 * @param domain_
	 */
	public void DeleteZipUrl(String domain_) {
		SQLiteDatabase sqlite_database = this.getWritableDatabase();
		domain_ = domain_.toLowerCase();
		sqlite_database.execSQL("DELETE FROM zip_urls WHERE domain = ?",
				new String[] { domain_ });
	}

	/**
	 * @param url_
	 */
	public void PutZipUrl(URL url_) {
		String domain = url_.getHost().toLowerCase();
		DeleteZipUrl(domain);
		SQLiteDatabase sqlite_database = this.getWritableDatabase();
		ContentValues content_values = new ContentValues();
		content_values.put("zip_url", url_.toString());
		content_values.put("domain", domain);
		Log.d(this.getClass().getSimpleName(), "domain part = " + domain);
		sqlite_database.insert("zip_urls", null, content_values);
		sqlite_database.close();
	}

	/**
	 * @param activity_
	 * @return array adapter for list view
	 */
	public ArrayAdapter<String> GetArrayAdapter(Activity activity_) {
		SQLiteDatabase readable_database = this.getReadableDatabase();
		Cursor cursor = readable_database.query("zip_urls",
				new String[] { "zip_url" }, null, null, null, null, null);

		ArrayAdapter<String> array_adapter = new ArrayAdapter<String>(
				activity_, android.R.layout.simple_list_item_1);

		for (boolean record_exists = cursor.moveToFirst(); record_exists; record_exists = cursor
				.moveToNext()) {
			array_adapter.add(cursor.getString(0));
		}
		cursor.close();
		readable_database.close();
		return array_adapter;
	}

	static ZipUrls theDomains;

	private ZipUrls(Context context_) {
		super(context_, "EasyGuideDownloader.sqlite", null, 4);
	}

	/**
	 * singleton factory
	 * 
	 * @param context_
	 * @return singleton instance of class Domains.
	 * 
	 */
	static public ZipUrls GetTheZipUrls(Context context_) {
		if (theDomains == null) {
			theDomains = new ZipUrls(context_);
		}
		return theDomains;
	}

}
