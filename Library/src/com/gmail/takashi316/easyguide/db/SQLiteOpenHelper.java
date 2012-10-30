package com.gmail.takashi316.easyguide.db;

import com.gmail.takashi316.easyguide.util.Log;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

class SQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

	Context context;

	public SQLiteOpenHelper(Context context) {
		// super(context, name, factory, version);
		super(context, "EasyGuideLib.sqlite", null, 5);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v(new Throwable(), "onCreate");
		SourceTable.createTable(db);
		DownloadedItemTable.CreateTable(db);
	}// onCreate

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v(new Throwable(), "Upgrading the database from " + oldVersion
				+ " to " + newVersion);
		SourceTable.upgradeTable(db, oldVersion, newVersion);
		DownloadedItemTable.UpgradeTable(db, oldVersion, newVersion);
	}// onUpgrade
}// SQLiteOpenHelper
