package com.gmail.takashi316.easyguide.ui;

import com.gmail.takashi316.easyguide.lib.R;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

public class TocActivity extends ListActivity {
	private ListView tocListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.toc);

	}// onCreate

}// TocActivity
