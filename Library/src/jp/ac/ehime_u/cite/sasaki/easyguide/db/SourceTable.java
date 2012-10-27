package jp.ac.ehime_u.cite.sasaki.easyguide.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;

import jp.ac.ehime_u.cite.sasaki.easyguide.content.Domain;
import jp.ac.ehime_u.cite.sasaki.easyguide.content.Root;
import jp.ac.ehime_u.cite.sasaki.easyguide.download.Source;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class SourceTable {
	final static String TABLE_NAME = "SOURCE";

	SQLiteOpenHelper sqliteOpenHelper;

	// final static String INSERT_TEST =
	// "INSERT INTO zip_urls(domain,zip_url) VALUES(\"lms.intdesign.org\", \"http://lms.ictdesign.org/easyguide.zip\");";

	public SourceTable(SQLiteOpenHelper sqlite_open_helper) {
		sqliteOpenHelper = sqlite_open_helper;
	}

	public void createTable() {
		SQLiteDatabase wdb = sqliteOpenHelper.getWritableDatabase();
		createTable(wdb);
		wdb.close();
	}// createTable

	public static void createTable(SQLiteDatabase db) {
		final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
				+ Source.COLUMN_DOMAIN + " TEXT NOT NULL, " + Source.COLUMN_URL
				+ " TEXT NOT NULL, " + Source.COLUMN_DOWNLOADED_FILE
				+ " TEXT, " + Source.COLUMN_LAST_MODIFIED + " LONG);";
		db.execSQL(CREATE_TABLE);
	}

	public static void upgradeTable(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		if (oldVersion != newVersion) {
			dropTable(db);
			createTable(db);
		}// if
	}

	private static void dropTable(SQLiteDatabase db) {
		final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
		db.execSQL(DROP_TABLE);
	}

	public void dropTable() {
		SQLiteDatabase wdb = sqliteOpenHelper.getWritableDatabase();
		dropTable(wdb);
		wdb.close();
	}// dropTable

	/**
	 * @param domain_
	 * @return zip url
	 */
	public Cursor select(Domain domain) {
		SQLiteDatabase rdb = sqliteOpenHelper.getReadableDatabase();
		Cursor cursor = rdb.query("zip_urls", new String[] {
				Source.COLUMN_DOMAIN, Source.COLUMN_URL,
				Source.COLUMN_DOWNLOADED_FILE, Source.COLUMN_LAST_MODIFIED },
				Source.COLUMN_DOMAIN + " = ?",
				new String[] { domain.getName() }, null, null, null);
		rdb.close();
		return cursor;
	}// select

	public void insert(Source s) {
		SQLiteDatabase wdb = sqliteOpenHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		wdb.insert(TABLE_NAME, null, cv);
		wdb.close();
	}// Insert

	public void insert(ArrayList<Source> zip_url_array_list) {
		SQLiteDatabase wdb = sqliteOpenHelper.getWritableDatabase();
		for (Source s : zip_url_array_list) {
			wdb.insert(TABLE_NAME, null, s.getContentValues());
		}// for
		wdb.close();
	}// Insert

	public String getByUrl(URI uri) {
		SQLiteDatabase rdb = sqliteOpenHelper.getReadableDatabase();
		Cursor c = rdb.query(TABLE_NAME, new String[] { Source.COLUMN_URL,
				Source.COLUMN_DOMAIN }, Source.COLUMN_URL + " = ?",
				new String[] { uri.toString() }, null, null, null);
		if (c.getCount() <= 0)
			return null;
		c.moveToFirst();
		String domain_string = c.getString(c
				.getColumnIndex(Source.COLUMN_DOMAIN));
		c.close();
		rdb.close();
		return domain_string;
	}// GetDomainByUrl

	public void Delete(Source source) {
		SQLiteDatabase wdb = sqliteOpenHelper.getWritableDatabase();
		wdb.delete(TABLE_NAME, Source.COLUMN_DOMAIN + " = ?",
				new String[] { source.getDomain() });
		wdb.close();
	}// Delete

	public void DeleteAll() {
		SQLiteDatabase wdb = sqliteOpenHelper.getWritableDatabase();
		wdb.delete(TABLE_NAME, null, null);
		wdb.close();
	}// DeleteAll

	@Deprecated
	public void DeleteZipUrl(String domain_) {
		SQLiteDatabase wdb = sqliteOpenHelper.getWritableDatabase();
		domain_ = domain_.toLowerCase();
		wdb.execSQL("DELETE FROM zip_urls WHERE domain = ?",
				new String[] { domain_ });
		wdb.close();
	}

	public void Delete(ArrayList<Source> zip_url_array_list) {
		SQLiteDatabase wdb = sqliteOpenHelper.getWritableDatabase();
		for (Source zip_url : zip_url_array_list) {
			wdb.delete(TABLE_NAME, Source.COLUMN_DOMAIN + " = ?",
					new String[] { zip_url.getDomain() });
		}// for
		wdb.close();
	}// Delete

	/**
	 * @param activity_
	 * @return array adapter for list view
	 * @throws InterruptedException
	 */
	public ArrayAdapter<String> GetArrayAdapter(Context context)
			throws InterruptedException {
		SQLiteDatabase rdb = sqliteOpenHelper.getReadableDatabase();
		Cursor cursor = rdb.query(TABLE_NAME, new String[] { Source.COLUMN_URL,
				Source.COLUMN_DOMAIN }, null, null, null, null, null);

		ArrayAdapter<String> array_adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_1);

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

	private static Source GetSource(Cursor c) throws InterruptedException {
		int di = c.getColumnIndex(Source.COLUMN_DOMAIN);
		String ds = c.getString(di);
		URI u;
		try {
			u = new URI(c.getString(c.getColumnIndex(Source.COLUMN_URL)));
		} catch (URISyntaxException e) {
			Log.v(new Throwable(), e.getMessage());
			return null;
		}// try
		Source s;
		try {
			s = new Source(ds, u);
		} catch (MalformedURLException e) {
			Log.v(new Throwable(), e.getMessage());
			return null;
		} catch (URISyntaxException e) {
			Log.v(new Throwable(), e.getMessage());
			return null;
		}// try
		return s;
	}// GetSource

	public ArrayList<Source> GetArrayList() throws FileNotFoundException,
			MalformedURLException, URISyntaxException, InterruptedException {
		SQLiteDatabase rdb = sqliteOpenHelper.getReadableDatabase();
		ArrayList<Source> array_list = new ArrayList<Source>();
		Cursor cursor = rdb.query(TABLE_NAME, new String[] {
				Source.COLUMN_DOMAIN, Source.COLUMN_URL,
				Source.COLUMN_DOWNLOADED_FILE, Source.COLUMN_LAST_MODIFIED },
				null, null, null, null, null);
		for (boolean record_exists = cursor.moveToFirst(); record_exists; record_exists = cursor
				.moveToNext()) {
			Domain domain = new Domain(Root.getTheRoot(), cursor.getString(0));
			URI uri;
			try {
				uri = new URI(cursor.getString(1));
			} catch (URISyntaxException e) {
				e.printStackTrace();
				continue;
			}// try
			File downloaded_file = new File(cursor.getString(2));
			Date last_modified = new Date(cursor.getLong(3));
			Source zip_url = new Source(domain.getName(), uri);
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
	static public SourceTable getTheSourceTable(
			SQLiteOpenHelper sqlite_open_helper) {
		if (theInstance == null) {
			theInstance = new SourceTable(sqlite_open_helper);
		}
		return theInstance;
	}// singleton factory

}// SourceTable
