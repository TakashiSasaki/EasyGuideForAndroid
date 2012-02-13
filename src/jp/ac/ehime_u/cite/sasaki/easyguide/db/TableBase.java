package jp.ac.ehime_u.cite.sasaki.easyguide.db;

import jp.ac.ehime_u.cite.sasaki.easyguide.model.CommonSQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
