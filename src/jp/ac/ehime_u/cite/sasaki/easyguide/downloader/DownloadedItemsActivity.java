package jp.ac.ehime_u.cite.sasaki.easyguide.downloader;

import jp.ac.ehime_u.cite.sasaki.easyguide.model.DownloadedItem;
import jp.ac.ehime_u.cite.sasaki.easyguide.db.SimpleCursorLoader;
import jp.ac.ehime_u.cite.sasaki.easyguide.db.DownloadedItemTable;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DownloadedItemsActivity extends CommonMenuActivity implements
		LoaderCallbacks<Cursor> {
	DownloadedItemTable downloadedItemsTable;
	ListView listViewDownloadedItems;
	LayoutInflater layoutInflator;
	CursorAdapter cursorAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(this.getClass().getSimpleName(), "onCreate() invoked");
		setContentView(R.layout.downloaded_items);
		this.layoutInflator = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		this.downloadedItemsTable = new DownloadedItemTable(this);
		SetListViewDownloadedItems();
	}// onCreate

	private void SetListViewDownloadedItems() {
		this.listViewDownloadedItems = (ListView) findViewById(R.id.listViewDownloadedItems);
		// Cursor c = downloadedItemsSQLiteOpenHelper.Select();
		// Here CursorAdapter is initialized with null cursor.
		// Actual cursor is set by onLoadFinished
		this.cursorAdapter = new CursorAdapter(this, null) {
			@Override
			public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
				View v = DownloadedItemsActivity.this.layoutInflator.inflate(
						android.R.layout.simple_list_item_1, arg2);
				return v;
			}// newView

			@Override
			public void bindView(View arg0, Context arg1, Cursor arg2) {
				TextView tv = (TextView) arg0.findViewById(android.R.id.text1);
				String s = arg2.getString(arg2
						.getColumnIndex(DownloadedItemTable.COLUMN_DOWNLOADED_FILE));
				tv.setText(s);
			}// bindView
		};// CursorAdapter
		this.listViewDownloadedItems.setAdapter(this.cursorAdapter);
	}// SetListViewDownloadedItems

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new SimpleCursorLoader(this) {
			@Override
			public Cursor loadInBackground() {
				return DownloadedItemsActivity.this.downloadedItemsTable
						.Select();
			}// loadInBackground
		};// DownloadedItemsCursorLoader(this);
	}// onCreateLoader

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		this.cursorAdapter.swapCursor(arg1);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		this.cursorAdapter.swapCursor(null);
	}
}// DownloadedItemsActivity
