package jp.ac.ehime_u.cite.sasaki.easyguide.db;

import jp.ac.ehime_u.cite.sasaki.easyguide.model.DownloadedItem;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DownloadedItemTable extends TableBase	 {
	Context context;
	final static String TABLE_NAME = "DownloadedItems";

	final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
			+ DownloadedItem.COLUMN_DOWNLOADED_DATE + " LONG NOT NULL, "
			+ DownloadedItem.COLUMN_DOWNLOADED_FILE + " TEXT NOT NULL;";
	final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

	public DownloadedItemTable(Context context) {
		super(context);
		// super(context, name, factory, version);
		//super(context, "EasyGuideDownloader.sqlite", null, 5);
		this.context = context;
	} // the constructor of DownloadedItemsSQLiteOpenHelper

	static public void CreateTable(SQLiteDatabase db) {
		Log.v(DownloadedItemTable.class.getSimpleName(), "onCreate");
		db.execSQL(CREATE_TABLE);
	}// onCreatae

	static public void UpgradeTable(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v(DownloadedItemTable.class.getSimpleName(), "onUpgrade");
		db.execSQL(DROP_TABLE);
	}// onUpgrade

	public void Insert(DownloadedItem downloaded_item) {
		Log.v(this.getClass().getSimpleName(), "Insert "
				+ downloaded_item.getDownloadedFile().getPath());
		Delete(downloaded_item);
		SQLiteDatabase wdb = getWritableDatabase();
		wdb.insert(TABLE_NAME, null, downloaded_item.getContentValues());
		wdb.close();
	}

	public void Delete(DownloadedItem downloaded_item) {
		Log.v(this.getClass().getSimpleName(), "Delete "
				+ downloaded_item.getDownloadedFile().getPath());
		SQLiteDatabase wdb = getWritableDatabase();
		wdb.delete(TABLE_NAME, DownloadedItem.COLUMN_DOWNLOADED_FILE,
				new String[] { downloaded_item.getDownloadedFile().getPath() });
		wdb.close();
	}

	public Cursor Select() {
		Log.v(this.getClass().getSimpleName(), "Select: getting readable database.");
		SQLiteDatabase rdb = getReadableDatabase();
		Log.v(this.getClass().getSimpleName(), "Select: querying");
		return rdb.query(TABLE_NAME, new String[] {
				DownloadedItem.COLUMN_DOWNLOADED_DATE,
				DownloadedItem.COLUMN_DOWNLOADED_FILE }, null, null, null,
				null, null);
	}// Select
	
}// DownloadedItemsSQLiteOpenHelper
