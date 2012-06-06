package jp.ac.ehime_u.cite.sasaki.easyguide.ui;

import jp.ac.ehime_u.cite.sasaki.easyguide.R;
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
