package jp.ac.ehime_u.cite.sasaki.easyguide.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TableBase {
	Context context;

	public TableBase(Context context) {
		this.context = context;
	}

	protected SQLiteDatabase getReadableDatabase() {
		CommonSQLiteOpenHelper oh = new CommonSQLiteOpenHelper(this.context);
		return oh.getReadableDatabase();
	}

	protected SQLiteDatabase getWritableDatabase() {
		CommonSQLiteOpenHelper oh = new CommonSQLiteOpenHelper(this.context);
		return oh.getWritableDatabase();
	}
}
