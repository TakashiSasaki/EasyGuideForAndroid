package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DownloadedItemsSQLiteOpenHelper extends SQLiteOpenHelper {
	Context context;
	final static String TABLE_NAME = "DownloadedItems";

	final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
			+ DownloadedItem.COLUMN_DOWNLOADED_DATE + " LONG NOT NULL, "
			+ DownloadedItem.COLUMN_DOWNLOADED_FILE + " TEXT NOT NULL;";
	final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

	public DownloadedItemsSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		// super(context, name, factory, version);
		super(context, "EasyGuideDownloader.sqlite", null, 1);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TABLE);
		this.onCreate(db);
	}

	public void Insert(DownloadedItem downloaded_item) {
		Delete(downloaded_item);
		SQLiteDatabase wdb = getWritableDatabase();
		wdb.insert(TABLE_NAME, null, downloaded_item.getContentValues());
		wdb.close();
	}

	public void Delete(DownloadedItem downloaded_item) {
		SQLiteDatabase wdb = getWritableDatabase();
		wdb.delete(TABLE_NAME, DownloadedItem.COLUMN_DOWNLOADED_FILE,
				new String[] { downloaded_item.getDownloadedFile().getPath() });
		wdb.close();
	}

	public Cursor Select() {
		SQLiteDatabase rdb = getReadableDatabase();
		return rdb.query(TABLE_NAME, new String[] {
				DownloadedItem.COLUMN_DOWNLOADED_DATE,
				DownloadedItem.COLUMN_DOWNLOADED_FILE }, null, null, null,
				null, null);
	}// Select
}// DownloadedItemsSQLiteOpenHelper
