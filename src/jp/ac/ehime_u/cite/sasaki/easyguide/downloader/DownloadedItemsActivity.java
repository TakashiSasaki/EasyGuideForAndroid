package jp.ac.ehime_u.cite.sasaki.easyguide.downloader;

import java.awt.Robot;

import jp.ac.ehime_u.cite.sasaki.easyguide.download.DownloadedItem;
import jp.ac.ehime_u.cite.sasaki.easyguide.download.DownloadedItemsSQLiteOpenHelper;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Root;
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
	DownloadedItemsSQLiteOpenHelper downloadedItemsSQLiteOpenHelper;
	ListView listViewDownloadedItems;
	LayoutInflater layoutInflator;
	CursorAdapter cursorAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(this.getClass().getSimpleName(), "onCreate() invoked");
		setContentView(R.layout.downloaded_items);
		layoutInflator = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		downloadedItemsSQLiteOpenHelper = new DownloadedItemsSQLiteOpenHelper(
				this);
		Root.GetTheRoot().EnumerateDomainDirectories();
		SetListViewDownloadedItems();
	}// onCreate

	private void SetListViewDownloadedItems() {
		listViewDownloadedItems = (ListView) findViewById(R.id.listViewDownloadedItems);
		//Cursor c = downloadedItemsSQLiteOpenHelper.Select();
		// Here CursorAdapter is initialized with null cursor.
		// Actual cursor is set by onLoadFinished  
		this.cursorAdapter = new CursorAdapter(this, null) {
			@Override
			public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
				View v = layoutInflator.inflate(
						android.R.layout.simple_list_item_1, arg2);
				return v;
			}// newView

			@Override
			public void bindView(View arg0, Context arg1, Cursor arg2) {
				TextView tv = (TextView) arg0.findViewById(android.R.id.text1);
				String s = arg2.getString(arg2
						.getColumnIndex(DownloadedItem.COLUMN_DOWNLOADED_FILE));
				tv.setText(s);
			}// bindView
		};// CursorAdapter
		listViewDownloadedItems.setAdapter(this.cursorAdapter);
	}// SetListViewDownloadedItems

	class DownloadedItemsCursorLoader extends SimpleCursorLoader {
		Context context;

		public DownloadedItemsCursorLoader(Context context) {
			super(context);
			this.context = context;
		}// a constructor of DownloadedItemsCursorLoader

		@Override
		public Cursor loadInBackground() {
			DownloadedItemsSQLiteOpenHelper oh = new DownloadedItemsSQLiteOpenHelper(
					this.context);
			return oh.Select();
		}// loadInBackground
	}// DownloadedItemsCursorLoader

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new DownloadedItemsCursorLoader(this);
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
