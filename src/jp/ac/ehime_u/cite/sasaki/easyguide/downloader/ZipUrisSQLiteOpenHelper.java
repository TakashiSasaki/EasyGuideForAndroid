package jp.ac.ehime_u.cite.sasaki.easyguide.downloader;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;

import jp.ac.ehime_u.cite.sasaki.easyguide.model.Domain;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Source;
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
public class ZipUrisSQLiteOpenHelper extends SQLiteOpenHelper {
	Context context;
	final static String TABLE_zip_urls = "zip_urls";
	final static String CREATE_TABLE = "CREATE TABLE " + TABLE_zip_urls + "("
			+ Source.COLUMN_domain + " TEXT NOT NULL, " + Source.COLUMN_url
			+ " TEXT NOT NULL, " + Source.COLUMN_downloadedFile + " TEXT, "
			+ Source.COLUMN_lastModified + " LONG);";
	final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_zip_urls
			+ ";";

	// final static String INSERT_TEST =
	// "INSERT INTO zip_urls(domain,zip_url) VALUES(\"lms.intdesign.org\", \"http://lms.ictdesign.org/easyguide.zip\");";

	private ZipUrisSQLiteOpenHelper(Context context_) {
		super(context_, "EasyGuideDownloader.sqlite", null, 5);
		this.context = context_;
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
	public ArrayList<Source> Select(Domain domain_) {
		SQLiteDatabase readable_database = this.getReadableDatabase();
		Cursor cursor = readable_database.query("zip_urls", new String[] {
				Source.COLUMN_domain, Source.COLUMN_url,
				Source.COLUMN_downloadedFile, Source.COLUMN_lastModified },
				Source.COLUMN_domain + " = ?", new String[] { domain_
						.getDomainDirectory().getName() }, null, null, null);

		ArrayList<Source> zip_url_array_list = new ArrayList<Source>();
		cursor.moveToFirst();
		while (true) {
			Domain domain = new Domain(cursor.getString(0));
			URI uri;
			try {
				uri = new URI(cursor.getString(1));
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
				continue;
			}// try
			File downloaded_file = new File(cursor.getString(3));
			Date last_modified_time = new Date(cursor.getLong(4));
			Source zip_url;
			zip_url = new Source(domain, uri, downloaded_file,
					last_modified_time);
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

	public void Insert(Source zip_url) {
		SQLiteDatabase writable = this.getWritableDatabase();
		writable.insert(TABLE_zip_urls, null, zip_url.GetContentValues());
		writable.close();
	}// Insert

	public void Insert(ArrayList<Source> zip_url_array_list) {
		SQLiteDatabase writable = this.getWritableDatabase();
		for (Source zip_url : zip_url_array_list) {
			writable.insert(TABLE_zip_urls, null, zip_url.GetContentValues());
		}// for
		writable.close();
	}// Insert

	public void Delete(Source zip_uri) {
		SQLiteDatabase writable_database = this.getWritableDatabase();
		writable_database.delete(TABLE_zip_urls, Source.COLUMN_domain + " = ?",
				new String[] { zip_uri.GetDomain().getDomainDirectory()
						.getName() });
		writable_database.close();
	}// Delete

	@Deprecated
	public void DeleteZipUrl(String domain_) {
		SQLiteDatabase sqlite_database = this.getWritableDatabase();
		domain_ = domain_.toLowerCase();
		sqlite_database.execSQL("DELETE FROM zip_urls WHERE domain = ?",
				new String[] { domain_ });
	}

	public void Delete(ArrayList<Source> zip_url_array_list) {
		SQLiteDatabase writable = this.getWritableDatabase();
		for (Source zip_url : zip_url_array_list) {
			writable.delete(TABLE_zip_urls, Source.COLUMN_domain + " = ?",
					new String[] { zip_url.GetDomainByString() });
		}// for
		writable.close();
	}// Delete

	/**
	 * @param activity_
	 * @return array adapter for list view
	 */
	public ArrayAdapter<String> GetArrayAdapter() {
		SQLiteDatabase readable_database = this.getReadableDatabase();
		Cursor cursor = readable_database.query(TABLE_zip_urls,
				new String[] { Source.COLUMN_url }, null, null, null, null,
				null);

		ArrayAdapter<String> array_adapter = new ArrayAdapter<String>(
				this.context, android.R.layout.simple_list_item_1);

		for (boolean record_exists = cursor.moveToFirst(); record_exists; record_exists = cursor
				.moveToNext()) {
			array_adapter.add(cursor.getString(0));
		}
		cursor.close();
		readable_database.close();
		return array_adapter;
	}// GetArrayAdapter

	public ArrayList<Source> GetArrayList() {
		ArrayList<Source> array_list = new ArrayList<Source>();
		SQLiteDatabase readable_database = this.getReadableDatabase();
		Cursor cursor = readable_database.query(TABLE_zip_urls, new String[] {
				Source.COLUMN_domain, Source.COLUMN_url,
				Source.COLUMN_downloadedFile, Source.COLUMN_lastModified },
				null, null, null, null, null);
		for (boolean record_exists = cursor.moveToFirst(); record_exists; record_exists = cursor
				.moveToNext()) {
			Domain domain = new Domain(cursor.getString(0));
			URI uri;
			try {
				uri = new URI(cursor.getString(1));
			} catch (URISyntaxException e) {
				e.printStackTrace();
				continue;
			}// try
			File downloaded_file = new File(cursor.getString(2));
			Date last_modified = new Date(cursor.getLong(3));
			Source zip_url = new Source(domain, uri, downloaded_file,
					last_modified);
			array_list.add(zip_url);
		}// for
		cursor.close();
		readable_database.close();
		return array_list;
	}// GetArrayList()

	static ZipUrisSQLiteOpenHelper theZipUrlsSQLiteOpenHelper;

	/**
	 * singleton factories
	 * 
	 * @return singleton instance of class Domains.
	 * 
	 */
	static public ZipUrisSQLiteOpenHelper GetTheInstance() {
		return theZipUrlsSQLiteOpenHelper;
	}// singleton factory

	static public ZipUrisSQLiteOpenHelper GetTheInstance(Context context_) {
		if (theZipUrlsSQLiteOpenHelper == null) {
			theZipUrlsSQLiteOpenHelper = new ZipUrisSQLiteOpenHelper(context_);
		}
		return theZipUrlsSQLiteOpenHelper;
	}// singleton factory

}// ZipUrls

