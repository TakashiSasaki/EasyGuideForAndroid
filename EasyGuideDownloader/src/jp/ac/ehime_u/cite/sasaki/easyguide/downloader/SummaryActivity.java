package jp.ac.ehime_u.cite.sasaki.easyguide.downloader;

import android.app.ListActivity;
import android.os.Bundle;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class SummaryActivity extends ListActivity {

	private SummaryArrayAdapter summary_array_adapter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		summary_array_adapter = new SummaryArrayAdapter(this, R.layout.summary);
	}// onCreate

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}// onStart

}// SummaryActivity
