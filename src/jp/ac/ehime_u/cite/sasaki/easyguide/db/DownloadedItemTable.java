package jp.ac.ehime_u.cite.sasaki.easyguide.db;

import java.util.ArrayList;

import jp.ac.ehime_u.cite.sasaki.easyguide.download.DownloadedItem;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DownloadedItemTable extends TableBase {
	Context context;
	final static String TABLE_NAME = "DownloadedItems";

	public static final String COLUMN_DOWNLOADED_DATE = "DownloadedDate";
	public static final String COLUMN_DOWNLOADED_FILE = "DownloadedFile";
	final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
			+ COLUMN_DOWNLOADED_DATE + " LONG NOT NULL, "
			+ COLUMN_DOWNLOADED_FILE + " TEXT NOT NULL);";
	final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

	public DownloadedItemTable(Context context) {
		super(context);
		// super(context, name, factory, version);
		// super(context, "EasyGuideDownloader.sqlite", null, 5);
		// this.context = context;
	} // the constructor of DownloadedItemsSQLiteOpenHelper

	static public void CreateTable(SQLiteDatabase db) {
		Log.v(new Throwable(), CREATE_TABLE);
		db.execSQL(CREATE_TABLE);
		// db.close();
	}// UpgradeTable

	static public ContentValues getContentValues(DownloadedItem di) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_DOWNLOADED_DATE, di.getDownloadedDate().getTime());
		cv.put(COLUMN_DOWNLOADED_FILE, di.getDownloadedFile().getPath());
		return cv;
	}

	static public void UpgradeTable(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		Log.v(new Throwable(), "onUpgrade from " + oldVersion + " to "
				+ newVersion);
		if (oldVersion != newVersion) {
			db.execSQL(DROP_TABLE);
			CreateTable(db);
		}
		// db.close();
	}// UpgradeTable

	public void Insert(DownloadedItem downloaded_item) {
		Log.v(new Throwable(), "aaa");
		Delete(downloaded_item);
		SQLiteDatabase wdb = getWritableDatabase();
		wdb.insert(TABLE_NAME, null, getContentValues(downloaded_item));
		wdb.close();
	}// Insert

	public void Insert(ArrayList<DownloadedItem> a) {
		// TODO: to be in batch
		for (DownloadedItem d : a) {
			Insert(d);
		}
	}// Insert

	public void Delete(DownloadedItem downloaded_item) {
		Log.v(new Throwable(), "Delete "
				+ downloaded_item.getDownloadedFile().getPath());
		SQLiteDatabase wdb = getWritableDatabase();
		wdb.delete(TABLE_NAME, COLUMN_DOWNLOADED_FILE + " = ?",
				new String[] { downloaded_item.getDownloadedFile().getPath() });
		wdb.close();
	}// Delete

	public void Delete(ArrayList<DownloadedItem> a) {
		// TODO: to be in batch
		for (DownloadedItem d : a) {
			Delete(d);
		}
	}// Delete

	public Cursor Select() {
		Log.v(new Throwable(), "Select: getting readable database.");
		SQLiteDatabase rdb = getReadableDatabase();
		Log.v(new Throwable()						, "Select: querying");
		return rdb.query(TABLE_NAME, new String[] { COLUMN_DOWNLOADED_DATE,
				COLUMN_DOWNLOADED_FILE }, null, null, null, null, null);
	}// Select

	static DownloadedItemTable theInstance;

	/**
	 * singleton factories
	 * 
	 * @return singleton instance of class Source.
	 * 
	 */
	static public DownloadedItemTable getInstance() {
		return theInstance;
	}// singleton factory

	static public DownloadedItemTable getInstance(Context context) {
		if (theInstance == null) {
			theInstance = new DownloadedItemTable(context);
		}
		return theInstance;
	}// singleton factory

}// DownloadedItemsSQLiteOpenHelper
