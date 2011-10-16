package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organization;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organizations;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class OrganizationsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.organizations);
		ArrayAdapter<String> array_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		for (Organization organization : Organizations.GetTheOrganizations()) {
			array_adapter.add(organization.getOrganizationDomain());
		}// for
		ListView list_view = (ListView) findViewById(R.id.listViewOrganizations);
		list_view.setAdapter(array_adapter);

		list_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				InvokeOrganizationActivity();
			}// onItemClick
		});// setOnItemClickListener
	}// onCreate

	private void InvokeOrganizationActivity() {
		Intent intent = new Intent();
		intent.setClass(this, OrganizationActivity.class);
		startActivity(intent);
	}
}// OrganizationActivity

