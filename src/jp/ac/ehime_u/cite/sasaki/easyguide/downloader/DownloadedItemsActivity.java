package jp.ac.ehime_u.cite.sasaki.easyguide.downloader;

import jp.ac.ehime_u.cite.sasaki.easyguide.model.DownloadedItem;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.DownloadedItemsSQLiteOpenHelper;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DownloadedItemsActivity extends Activity {
	DownloadedItemsSQLiteOpenHelper downloadedItemsSQLiteOpenHelper;
	ListView listViewDownloadedItems;
	LayoutInflater layoutInflator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.downloaded_items);
		layoutInflator = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		SetListViewDownloadedItems();
	}// onCreate

	private void SetListViewDownloadedItems() {
		listViewDownloadedItems = (ListView) findViewById(R.id.listViewDownloadedItems);
		Cursor c = downloadedItemsSQLiteOpenHelper.Select();
		CursorAdapter adapter = new CursorAdapter(this, c) {
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
		listViewDownloadedItems.setAdapter(adapter);
	}// SetListViewDownloadedItems
}// DownloadedItemsActivity

