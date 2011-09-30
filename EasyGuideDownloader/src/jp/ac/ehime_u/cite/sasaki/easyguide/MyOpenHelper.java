package jp.ac.ehime_u.cite.sasaki.easyguide;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyOpenHelper extends SQLiteOpenHelper {
	final static String CREATE_TABLE = "CREATE TABLE zip_urls(domain TEXT NOT NULL, zip_url TEXT NOT NULL);";
	final static String DROP_TABLE = "DROP TABLE IF EXISTS zip_urls;";
	final static String INSERT_TEST = "INSERT INTO zip_urls(domain,zip_url) VALUES(\"sasakinas.cite.ehime-u.ac.jp\", \"http://sasakinas.cite.ehime-u.ac.jp/easyguide/easyguide.zip\");";

	public MyOpenHelper(Context context_) {
		super(context_, "EasyGuideDownloader.sqlite", null, 4);
	}

	@Override
	public void onCreate(SQLiteDatabase sqlite_database) {
		Log.d("EasyGuideDownloader",
				"MyOpenHelper#onCreate" + sqlite_database.getPath());
		Log.d("EasyGuideDownloader", CREATE_TABLE);
		sqlite_database.execSQL(CREATE_TABLE);
		Log.d("EasyGuideDownloader", INSERT_TEST);
		sqlite_database.execSQL(INSERT_TEST);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqlite_database, int old_version,
			int new_version) {
		Log.d("EasyGuideDownloader", "MyOpenHelper#onUpgrade");
		if (old_version != new_version) {
			Log.d("EasyGuideDownloader", DROP_TABLE + " old_version="
					+ old_version + ", new_version=" + new_version);
			sqlite_database.execSQL(DROP_TABLE);
			this.onCreate(sqlite_database);
		}
	}
}
