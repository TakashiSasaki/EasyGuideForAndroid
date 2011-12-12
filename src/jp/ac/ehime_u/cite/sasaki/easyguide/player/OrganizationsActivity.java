package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import jp.ac.ehime_u.cite.sasaki.easyguide.model.DirectoryImage;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.DirectoryImageException;
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

		try {
			DirectoryImage.SetDefaultImage(this, R.drawable.unknown);
		} catch (DirectoryImageException e) {
			e.printStackTrace();
			throw new java.lang.Error("Can't set default image and thumbnail");
		}// try

		ArrayAdapter<String> array_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		for (Organization organization : Organizations.GetTheOrganizations()) {
			array_adapter.add(organization.getOrganizationDomain());
		}// for
		ListView list_view = (ListView) findViewById(R.id.listViewOrganizations);
		list_view.setAdapter(array_adapter);

		list_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				InvokeOrganizationActivity(position);
			}// onItemClick
		});// setOnItemClickListener
	}// onCreate

	private void InvokeOrganizationActivity(int position) {
		Intent intent = new Intent();
		intent.setClass(this, OrganizationActivity.class);
		intent.putExtra("position", position);
		startActivity(intent);
	}// InvokeOrganizationActivity
}// OrganizationActivity

