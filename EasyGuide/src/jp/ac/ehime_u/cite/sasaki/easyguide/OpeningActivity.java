package jp.ac.ehime_u.cite.sasaki.easyguide;

import java.util.Iterator;

import jp.ac.ehime_u.cite.sasaki.easyguide.model.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * 施設一覧を表示して選択させるアクティビティ
 * 
 * @author Takashi SASAKI
 */
public class OpeningActivity extends Activity {
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.opening);
		// Organizations the_organizations =
		// Organizations.GetTheOrganizations();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		for (Iterator<Organization> i = Organizations.GetCollection()
				.iterator(); i.hasNext();) {
			adapter.add(i.next().name);
		}
		ListView organization_list_view = (ListView) findViewById(R.id.listViewOrganizations);
		organization_list_view.setAdapter(adapter);

		organization_list_view
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						ListView list_view = (ListView) parent;
						String selected_organization = (String) list_view.getItemAtPosition(position);
						Log.v("OpeningActivity", "selected = " + selected_organization);
						Intent intent = new Intent(getApplicationContext(),
								OrganizationActivity.class);
						intent.putExtra("jp.ac.ehime_u.cite.sasaki.easyguide.organization", selected_organization);
						startActivity(intent);
					}
				});
	}
}
