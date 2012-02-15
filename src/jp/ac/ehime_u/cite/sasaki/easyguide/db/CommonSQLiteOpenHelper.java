package jp.ac.ehime_u.cite.sasaki.easyguide.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CommonSQLiteOpenHelper extends SQLiteOpenHelper {

	Context context;

	public CommonSQLiteOpenHelper(Context context) {
		// super(context, name, factory, version);
		super(context, "EasyGuideLib.sqlite", null, 3);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v(this.getClass().getSimpleName(), "onCreate");
		SourceTable.CreateTable(db);
		DownloadedItemTable.CreateTable(db);
	}// onCreate

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v(this.getClass().getSimpleName(), "onUpgrade from " + oldVersion
				+ " to " + newVersion);
		SourceTable.UpgradeTable(db, oldVersion, newVersion);
		DownloadedItemTable.UpgradeTable(db, oldVersion, newVersion);
	}// onUpgrade
}//CommonSQLiteOpenHelper
