package jp.ac.ehime_u.cite.sasaki.easyguide.ui;

import jp.ac.ehime_u.cite.sasaki.easyguide.R;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class TocActivity extends ListActivity {
	private ListView tocListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.toc);

	}// onCreate

}// TocActivity
