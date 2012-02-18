package jp.ac.ehime_u.cite.sasaki.easyguide.downloader;

import java.io.File;
import java.util.ArrayList;

import jp.ac.ehime_u.cite.sasaki.easyguide.db.DownloadedItemTable;
import jp.ac.ehime_u.cite.sasaki.easyguide.db.SimpleCursorLoader;
import jp.ac.ehime_u.cite.sasaki.easyguide.download.Domain;
import jp.ac.ehime_u.cite.sasaki.easyguide.download.DownloadedItem;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Root;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DownloadedItemsActivity extends CommonMenuActivity implements
		LoaderCallbacks<Cursor> {
	DownloadedItemTable downloadedItemsTable;
	ListView listViewDownloadedItems;
	// LayoutInflater layoutInflator;
	CursorAdapter cursorAdapter;
	Button buttonDeleteZips;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(new Throwable(), "onCreate() invoked");
		setContentView(R.layout.downloaded_items);
		// this.layoutInflator = (LayoutInflater) this
		// .getSystemService(LAYOUT_INFLATER_SERVICE);
		this.downloadedItemsTable = new DownloadedItemTable(this);
		Root.GetTheRoot().EnumerateDomainDirectories();
		for (Domain d : Root.GetTheRoot()) {
			ArrayList<DownloadedItem> adi = d.ScanDownloadedItems();
			DownloadedItemTable.getInstance(this).Insert(adi);
		}
		this.listViewDownloadedItems = (ListView) findViewById(R.id.listViewDownloadedItems);
		// SetSimpleArrayAdapter();
		SetCursorAdapter();
		getLoaderManager().initLoader(0, null, this);
		SetButtonDeleteZips();
	}// onCreate

	private void SetButtonDeleteZips() {
		this.buttonDeleteZips = (Button) findViewById(R.id.buttonDeleteZips);
		OnClickListener ocl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Cursor c = DownloadedItemTable.getInstance().Select();
				c.moveToFirst();
				int ci = c
						.getColumnIndex(DownloadedItemTable.COLUMN_DOWNLOADED_FILE);
				for (int i = 0; i < c.getCount(); ++i) {
					String file_path = c.getString(ci);
					Log.v(new Throwable(), "Deleting " + file_path);
					File file = new File(file_path);
					file.delete();
					DownloadedItemTable.getInstance().Delete(file_path);
					c.moveToNext();
				}// for
				getLoaderManager().restartLoader(0, null,
						DownloadedItemsActivity.this);
			}// onClick
		};
		this.buttonDeleteZips.setOnClickListener(ocl);
	}// SetButtonDeleteZips

	private void SetSimpleArrayAdapter() {
		ArrayAdapter<String> aas = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		aas.add("aaa");
		aas.add("bbb");
		aas.add("ccc");
		this.listViewDownloadedItems.setAdapter(aas);
	}

	private void SetCursorAdapter() {
		// Cursor c = downloadedItemsSQLiteOpenHelper.Select();
		// Here CursorAdapter is initialized with null cursor.
		// Actual cursor is set by onLoadFinished
		this.cursorAdapter = new CursorAdapter(this, null) {
			@Override
			public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
				LayoutInflater li = (LayoutInflater) arg0
						.getSystemService(LAYOUT_INFLATER_SERVICE);
				// arg2 is ListView
				View v = li.inflate(android.R.layout.simple_list_item_1, null);
				return v;
			}// newView

			@Override
			public void bindView(View arg0, Context arg1, Cursor arg2) {
				TextView tv = (TextView) arg0.findViewById(android.R.id.text1);
				String s = arg2
						.getString(arg2
								.getColumnIndex(DownloadedItemTable.COLUMN_DOWNLOADED_FILE));
				tv.setText(s);
			}// bindView
		};// CursorAdapter
		Log.v(new Throwable(),
				"Setting cursor adapter to list view of DownloadedItemsActivity.");
		this.listViewDownloadedItems.setAdapter(this.cursorAdapter);
	}// SetListViewDownloadedItems

	class DownloadedItemsCursorLoader extends SimpleCursorLoader {
		Context context;

		public DownloadedItemsCursorLoader(Context context) {
			super(context);
			this.context = context;
		}// a constructor of DownloadedItemsCursorLoader

		@Override
		public Cursor loadInBackground() {
			DownloadedItemTable dit = DownloadedItemTable
					.getInstance(this.context);
			return dit.Select();
		}// loadInBackground
	}// DownloadedItemsCursorLoader

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
