package jp.ac.ehime_u.cite.sasaki.easyguide.downloader;

import jp.ac.ehime_u.cite.sasaki.easyguide.ui.TocActivity;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class CommonMenuActivity extends Activity {
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.common, menu);
		return true;
	}// onCreateOptionsMenu

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.itemSources: {
			Log.v(this.getClass().getSimpleName(), "R.id.itemSources");
			Intent intent = new Intent(CommonMenuActivity.this,
					SourcesActivity.class);
			intent.setAction(Intent.ACTION_VIEW);
			startActivity(intent);
			return true;
		}
		case R.id.itemDownloadedItems: {
			Log.v(this.getClass().getSimpleName(), "R.id.itemDownloadedItems");
			Intent intent = new Intent(CommonMenuActivity.this,
					DownloadedItemsActivity.class);
			intent.setAction(Intent.ACTION_VIEW);
			startActivity(intent);
			return true;
		}
		case R.id.itemToc: {
			Log.v(this.getClass().getSimpleName(), "R.id.itemToc");
			Intent intent = new Intent(CommonMenuActivity.this,
					TocActivity.class);
			intent.setAction(Intent.ACTION_VIEW);
			startActivity(intent);
			return true;
		}
		default:
			return false;
		}// switch
	}// onOptionsItemSelected
}// CommonMenuActivity
