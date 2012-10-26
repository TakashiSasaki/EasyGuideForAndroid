package jp.ac.ehime_u.cite.sasaki.easyguide.downloader;

import jp.ac.ehime_u.cite.sasaki.easyguide.download.AssetsArrayAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class MainActivity extends Activity {

	private ListView listViewZipFiles;
	private Button buttonCleanup;
	private Button buttonInflate;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);

		this.listViewZipFiles = (ListView) findViewById(R.id.listViewZipFiles);
		this.buttonCleanup = (Button) findViewById(R.id.buttonCleanup);
		this.buttonInflate = (Button) findViewById(R.id.buttonInflate);

		this.buttonCleanup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				cleanupContent();
			}
		});

		this.buttonInflate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				inflateContent();
			}
		});

		this.listViewZipFiles.setAdapter(new AssetsArrayAdapter(this,
				android.R.layout.simple_expandable_list_item_1));
		
		this.listViewZipFiles.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String asset_path = (String) parent.getItemAtPosition(position);
			}
		});

	}// onCreate

	private void cleanupContent() {

	}

	private void inflateContent() {

	}

}// MainActivity

