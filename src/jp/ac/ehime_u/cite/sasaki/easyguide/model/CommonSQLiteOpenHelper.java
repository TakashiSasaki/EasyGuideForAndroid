package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CommonSQLiteOpenHelper extends SQLiteOpenHelper {

	Context context;

	public CommonSQLiteOpenHelper(Context context) {
		// super(context, name, factory, version);
		super(context, "EasyGuideLib.sqlite", null, 1);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v(this.getClass().getSimpleName(), "onCreate");
		SourceTable.CreateTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v(this.getClass().getSimpleName(), "onUpgrade from " + oldVersion + " to " + newVersion);
		SourceTable.UpgradeTable(db, oldVersion, newVersion);
	}
}
