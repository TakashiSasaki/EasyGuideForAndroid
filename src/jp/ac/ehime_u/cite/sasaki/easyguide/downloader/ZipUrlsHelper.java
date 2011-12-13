package jp.ac.ehime_u.cite.sasaki.easyguide.downloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import jp.ac.ehime_u.cite.sasaki.easyguide.model.Domain;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.ZipUrl;

import android.app.Activity;
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
public class ZipUrlsHelper extends SQLiteOpenHelper {
	final static String TABLE_zip_urls = "zip_urls";
	final static String CREATE_TABLE = "CREATE TABLE "
			+ TABLE_zip_urls
			+ "(domain TEXT NOT NULL, url TEXT NOT NULL,downloadedFile TEXT,lastModified LONG);";
	final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_zip_urls
			+ ";";

	// final static String INSERT_TEST =
	// "INSERT INTO zip_urls(domain,zip_url) VALUES(\"lms.intdesign.org\", \"http://lms.ictdesign.org/easyguide.zip\");";

	private ZipUrlsHelper(Context context_) {
		super(context_, "EasyGuideDownloader.sqlite", null, 4);
	}// a constructor

	@Override
	public void onCreate(SQLiteDatabase sqlite_database) {
		Log.d(this.getClass().getSimpleName(), sqlite_database.getPath());
		Log.d(this.getClass().getSimpleName(), CREATE_TABLE);
		sqlite_database.execSQL(CREATE_TABLE);
		// Log.d(this.getClass().getSimpleName(), INSERT_TEST);
		// sqlite_database.execSQL(INSERT_TEST);
	}// onCreate

	@Override
	public void onUpgrade(SQLiteDatabase sqlite_database, int old_version,
			int new_version) {
		Log.d(getClass().getSimpleName(), "MyOpenHelper#onUpgrade");
		if (old_version != new_version) {
			Log.d(getClass().getSimpleName(), DROP_TABLE + " old_version="
					+ old_version + ", new_version=" + new_version);
			sqlite_database.execSQL(DROP_TABLE);
			this.onCreate(sqlite_database);
		}// if
	}// onUpgrade

	/**
	 * @param domain_
	 * @return zip url
	 */
	public ArrayList<ZipUrl> Select(Domain domain_) {
		SQLiteDatabase readable_database = this.getReadableDatabase();
		Cursor cursor = readable_database.query("zip_urls", new String[] {
				ZipUrl.COLUMN_domain, ZipUrl.COLUMN_url,
				ZipUrl.COLUMN_downloadedFile, ZipUrl.COLUMN_lastModified },
				ZipUrl.COLUMN_domain + " = ?", new String[] { domain_
						.getDomainDirectory().getName() }, null, null, null);

		ArrayList<ZipUrl> zip_url_array_list = new ArrayList<ZipUrl>();
		cursor.moveToFirst();
		while (true) {
			Domain domain = new Domain(cursor.getString(0));
			URL url;
			try {
				url = new URL(cursor.getString(1));
			} catch (MalformedURLException e) {
				e.printStackTrace();
				break;
			}
			File downloaded_file = new File(cursor.getString(3));
			Date last_modified_time = new Date(cursor.getLong(4));
			ZipUrl zip_url;
			try {
				zip_url = new ZipUrl(domain, url, downloaded_file,
						last_modified_time);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				break;
			}
			Log.v(this.getClass().getSimpleName(), "Zip URL for domain "
					+ zip_url.toString());
			zip_url_array_list.add(zip_url);
			if (cursor.isLast()) {
				break;
			} else {
				cursor.moveToNext();
			}
		}// while
		cursor.close();
		return zip_url_array_list;
	}// GetZipUrls

	public void Insert(ZipUrl zip_url) {
		SQLiteDatabase writable = this.getWritableDatabase();
		writable.insert(TABLE_zip_urls, null, zip_url.GetContentValues());
		writable.close();
	}

	public void Insert(ArrayList<ZipUrl> zip_url_array_list) {
		SQLiteDatabase writable = this.getWritableDatabase();
		for (ZipUrl zip_url : zip_url_array_list) {
			writable.insert(TABLE_zip_urls, null, zip_url.GetContentValues());
		}// for
		writable.close();
	}// Insert

	public void Delete(Domain domain_) {
		SQLiteDatabase writable_database = this.getWritableDatabase();
		writable_database.delete(TABLE_zip_urls, ZipUrl.COLUMN_domain + " = ?",
				new String[] { domain_.getDomainDirectory().getName() });
		writable_database.close();
	}// Delete

	@Deprecated
	public void DeleteZipUrl(String domain_) {
		SQLiteDatabase sqlite_database = this.getWritableDatabase();
		domain_ = domain_.toLowerCase();
		sqlite_database.execSQL("DELETE FROM zip_urls WHERE domain = ?",
				new String[] { domain_ });
	}

	public void Delete(ArrayList<ZipUrl> zip_url_array_list) {
		SQLiteDatabase writable = this.getWritableDatabase();
		for (ZipUrl zip_url : zip_url_array_list) {
			writable.delete(TABLE_zip_urls, ZipUrl.COLUMN_domain + " = ?",
					new String[] { zip_url.GetDomainByString() });
		}// for
		writable.close();
	}// Delete

	/**
	 * @param activity_
	 * @return array adapter for list view
	 */
	public ArrayAdapter<String> GetArrayAdapter(Activity activity_) {
		SQLiteDatabase readable_database = this.getReadableDatabase();
		Cursor cursor = readable_database.query(TABLE_zip_urls,
				new String[] { ZipUrl.COLUMN_url }, null, null, null, null,
				null);

		ArrayAdapter<String> array_adapter = new ArrayAdapter<String>(
				activity_, android.R.layout.simple_list_item_1);

		for (boolean record_exists = cursor.moveToFirst(); record_exists; record_exists = cursor
				.moveToNext()) {
			array_adapter.add(cursor.getString(0));
		}
		cursor.close();
		readable_database.close();
		return array_adapter;
	}// GetArrayAdapter

	static ZipUrlsHelper theDomains;

	/**
	 * singleton factory
	 * 
	 * @param context_
	 * @return singleton instance of class Domains.
	 * 
	 */
	static public ZipUrlsHelper GetTheZipUrls(Context context_) {
		if (theDomains == null) {
			theDomains = new ZipUrlsHelper(context_);
		}
		return theDomains;
	}// singleton factory

}// ZipUrls

