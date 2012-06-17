package jp.ac.ehime_u.cite.sasaki.easyguide.downloader;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class ExtractedItemsActivity extends ListActivity {

	/*
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			this.setListAdapter(new SummaryArrayAdapter(this, R.layout.extracted_items));
		} catch (RuntimeException e) {
			Log.d(this.getClass().getSimpleName(),
					"Can't create instance of SummaryArrayAdapter.");
		}
	}// onCreate

	/*
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
	}// onStart

}// SummaryActivity
