package jp.ac.ehime_u.cite.sasaki.easyguide.ui;

import jp.ac.ehime_u.cite.sasaki.easyguide.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class TocActivity extends Activity {
	private ListView tocListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.toc);

		this.tocListView = (ListView) (this.findViewById(R.id.tocListView));
	}// onCreate

}// TocActivity
