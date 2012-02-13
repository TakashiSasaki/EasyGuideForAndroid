package jp.ac.ehime_u.cite.sasaki.easyguide.downloader;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class CommonSQLiteOpenHelper extends SQLiteOpenHelper {

	Context context;

	public CommonSQLiteOpenHelper(Context context) {
		// super(context, name, factory, version);
		super(context, "EasyGuideDownloader.sqlite", null, 5);
		this.context = context;
	}
}
