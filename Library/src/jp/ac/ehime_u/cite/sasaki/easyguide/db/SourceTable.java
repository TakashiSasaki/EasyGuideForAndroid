package jp.ac.ehime_u.cite.sasaki.easyguide.db;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;

import jp.ac.ehime_u.cite.sasaki.easyguide.content.Domain;
import jp.ac.ehime_u.cite.sasaki.easyguide.download.Source;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class SourceTable extends TableBase {
	final static String TABLE_NAME = "SOURCE";
	final static public String COLUMN_DOMAIN = "domain";
	final static public String COLUMN_URL = "url";
	final static public String COLUMN_DOWNLOADED_FILE = "downloadedFile";
	final static public String COLUMN_LAST_MODIFIED = "lastModified";
	final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
			+ COLUMN_DOMAIN + " TEXT NOT NULL, " + COLUMN_URL
			+ " TEXT NOT NULL, " + COLUMN_DOWNLOADED_FILE + " TEXT, "
			+ COLUMN_LAST_MODIFIED + " LONG);";
	final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

	// final static String INSERT_TEST =
	// "INSERT INTO zip_urls(domain,zip_url) VALUES(\"lms.intdesign.org\", \"http://lms.ictdesign.org/easyguide.zip\");";

	public SourceTable(Context context) {
		super(context);
	}

	static public void CreateTable(SQLiteDatabase db) {
		Log.v(new Throwable(), db.getPath());
		Log.v(new Throwable(), CREATE_TABLE);
		db.execSQL(CREATE_TABLE);
		// Log.d(this.getClass().getSimpleName(), INSERT_TEST);
		// sqlite_database.execSQL(INSERT_TEST);
		// db.close();
	}// CreateTable

	static public void UpgradeTable(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		Log.v(new Throwable(), DROP_TABLE + " old_version=" + oldVersion
				+ ", new_version=" + newVersion);
		if (oldVersion != newVersion) {
			db.execSQL(DROP_TABLE);
			CreateTable(db);
		}// if
			// db.close();
	}// UpgradeTable

	/**
	 * @param domain_
	 * @return zip url
	 */
	public ArrayList<Source> Select(Domain domain_, SQLiteDatabase rdb) {
		Cursor cursor = rdb.query("zip_urls", new String[] { COLUMN_DOMAIN,
				COLUMN_URL, COLUMN_DOWNLOADED_FILE, COLUMN_LAST_MODIFIED },
				COLUMN_DOMAIN + " = ?", new String[] { domain_
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
			Log.v(new Throwable(), "Zip URL for domain " + zip_url.toString());
			zip_url_array_list.add(zip_url);
			if (cursor.isLast()) {
				break;
			} else {
				cursor.moveToNext();
			}
		}// while
		cursor.close();
		rdb.close();
		return zip_url_array_list;
	}// GetZipUrls

	public void Insert(Source s) {
		SQLiteDatabase wdb = getWritableDatabase();
		wdb.insert(TABLE_NAME, null, GetContentValues(s));
		wdb.close();
	}// Insert

	public static ContentValues GetContentValues(Source s) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_DOMAIN, s.getDomain().getDomainDirectory().getName());
		try {
			cv.put(COLUMN_LAST_MODIFIED, s.getLastModified().getTime());
		} catch (NullPointerException nurupo) {
			cv.put(COLUMN_LAST_MODIFIED, 0L);
		}// try
		catch (InterruptedException e) {
			cv.put(COLUMN_LAST_MODIFIED, 0L);
		}
		cv.put(COLUMN_URL, s.getUri().toString());
		return cv;
	}// GetContentValues

	public void Insert(ArrayList<Source> zip_url_array_list) {
		SQLiteDatabase wdb = getWritableDatabase();
		for (Source s : zip_url_array_list) {
			wdb.insert(TABLE_NAME, null, GetContentValues(s));
		}// for
		wdb.close();
	}// Insert

	public String GetDomainByUrl(URI uri) {
		SQLiteDatabase rdb = getReadableDatabase();
		Cursor c = rdb.query(TABLE_NAME, new String[] { COLUMN_URL,
				COLUMN_DOMAIN }, COLUMN_URL + " = ?",
				new String[] { uri.toString() }, null, null, null);
		if (c.getCount() <= 0)
			return null;
		c.moveToFirst();
		String domain_string = c.getString(c.getColumnIndex(COLUMN_DOMAIN));
		c.close();
		rdb.close();
		return domain_string;
	}// GetDomainByUrl

	public void Delete(Source zip_uri) {
		SQLiteDatabase wdb = getWritableDatabase();
		wdb.delete(TABLE_NAME, COLUMN_DOMAIN + " = ?", new String[] { zip_uri
				.getDomain().getDomainDirectory().getName() });
		wdb.close();
	}// Delete

	public void DeleteAll() {
		SQLiteDatabase wdb = getWritableDatabase();
		wdb.delete(TABLE_NAME, null, null);
		wdb.close();
	}// DeleteAll

	@Deprecated
	public void DeleteZipUrl(String domain_) {
		SQLiteDatabase wdb = getWritableDatabase();
		domain_ = domain_.toLowerCase();
		wdb.execSQL("DELETE FROM zip_urls WHERE domain = ?",
				new String[] { domain_ });
		wdb.close();
	}

	public void Delete(ArrayList<Source> zip_url_array_list) {
		SQLiteDatabase wdb = getWritableDatabase();
		for (Source zip_url : zip_url_array_list) {
			wdb.delete(TABLE_NAME, COLUMN_DOMAIN + " = ?",
					new String[] { zip_url.getDomainByString() });
		}// for
		wdb.close();
	}// Delete

	/**
	 * @param activity_
	 * @return array adapter for list view
	 */
	public ArrayAdapter<String> GetArrayAdapter() {
		SQLiteDatabase rdb = getReadableDatabase();
		Cursor cursor = rdb.query(TABLE_NAME, new String[] { COLUMN_URL,
				COLUMN_DOMAIN }, null, null, null, null, null);

		ArrayAdapter<String> array_adapter = new ArrayAdapter<String>(
				this.context, android.R.layout.simple_list_item_1);

		for (boolean record_exists = cursor.moveToFirst(); record_exists; record_exists = cursor
				.moveToNext()) {
			array_adapter.add(GetSource(cursor).getUri().toString());
		}
		Log.v(new Throwable(), "" + array_adapter.getCount()
				+ " items in array adapter.");
		cursor.close();
		rdb.close();
		return array_adapter;
	}// GetArrayAdapter

	private static Source GetSource(Cursor c) {
		int di = c.getColumnIndex(COLUMN_DOMAIN);
		String ds = c.getString(di);
		Domain d = new Domain(ds);
		URI u;
		try {
			u = new URI(c.getString(c.getColumnIndex(COLUMN_URL)));
		} catch (URISyntaxException e) {
			Log.v(new Throwable(), e.getMessage());
			return null;
		}// try
		Source s;
		try {
			s = new Source(d, u);
		} catch (MalformedURLException e) {
			Log.v(new Throwable(), e.getMessage());
			return null;
		} catch (URISyntaxException e) {
			Log.v(new Throwable(), e.getMessage());
			return null;
		}// try
		return s;
	}// GetSource

	public ArrayList<Source> GetArrayList() {
		SQLiteDatabase rdb = getReadableDatabase();
		ArrayList<Source> array_list = new ArrayList<Source>();
		Cursor cursor = rdb.query(TABLE_NAME, new String[] { COLUMN_DOMAIN,
				COLUMN_URL, COLUMN_DOWNLOADED_FILE, COLUMN_LAST_MODIFIED },
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
		rdb.close();
		return array_list;
	}// GetArrayList()

	static SourceTable theInstance;

	/**
	 * singleton factories
	 * 
	 * @return singleton instance of class Source.
	 * 
	 */
	static public SourceTable GetTheInstance() {
		return theInstance;
	}// singleton factory

	static public SourceTable GetTheInstance(Context context) {
		if (theInstance == null) {
			theInstance = new SourceTable(context);
		}
		return theInstance;
	}// singleton factory

}// SourceTable
