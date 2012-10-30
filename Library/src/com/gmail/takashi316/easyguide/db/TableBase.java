package com.gmail.takashi316.easyguide.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TableBase {
	Context context;

	public TableBase(Context context) {
		this.context = context;
	}

	protected SQLiteDatabase getReadableDatabase() {
		SQLiteOpenHelper oh = new SQLiteOpenHelper(this.context);
		return oh.getReadableDatabase();
	}// getReadableDatabase

	protected SQLiteDatabase getWritableDatabase() {
		SQLiteOpenHelper oh = new SQLiteOpenHelper(this.context);
		return oh.getWritableDatabase();
	}// getWritableDatabase
}// tableBase
