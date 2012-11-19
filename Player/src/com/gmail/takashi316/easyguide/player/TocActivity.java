package com.gmail.takashi316.easyguide.player;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import junit.framework.Assert;

import com.gmail.takashi316.easyguide.content.ContentUnit;
import com.gmail.takashi316.easyguide.content.Root;
import com.gmail.takashi316.easyguide.ui.TocAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * 施設一覧を表示して選択させるアクティビティ
 * 
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 */
public class TocActivity extends Activity {
	/** Called when the activity is first created. */

	// private ArrayList<Building> buildingList;
	// private static Building chosenBuilding;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.toc);

		// android.R.layout.simple_list_item_1);
		// this.buildingList = new ArrayList<Building>();
		// for (Organization organization : Organizations.getInstance()) {
		// Log.v(this.getClass().getSimpleName(), "Enumerating organizations");
		// for (Facility facility : organization) {
		// Log.v(this.getClass().getSimpleName(), "Enumerating facilities");
		// for (Building building : facility) {
		// Log.v(this.getClass().getSimpleName(),
		// "Enumerating buildings. Building "
		// + building.getTitle() + " found.");
		// assert (building.getTitle() != null);
		// adapter.add(building.getTitle());
		// this.buildingList.add(building);
		// }//for
		// }//for
		// }//for
		ListView building_list_view = (ListView) findViewById(R.id.tocListView);
		// Log.v(this.getClass().getSimpleName(), "Totally " +
		// adapter.getCount()
		// + " buildings found.");
		Assert.assertNotNull(building_list_view);

		building_list_view
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						ListView list_view = (ListView) parent;
						TocAdapter.TocItem toc_item = (TocAdapter.TocItem) list_view
								.getItemAtPosition(position);
						Intent intent = new Intent(TocActivity.this,
								UnifiedActivity.class);
						intent.putExtra("root", toc_item.absolutePath);
						startActivity(intent);
					}// onItemClick
				}// OnItemClickListener
				);

		try {
			ArrayList<ContentUnit> children = Root.getTheRoot().getChildren();
			TocAdapter toc_adapter = new TocAdapter(this, children);
			building_list_view.setAdapter(toc_adapter);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// onCreate

	// private void InvokeMapActivity(Building building_) {
	// OpeningActivity.chosenBuilding = building_;
	// Intent intent = new Intent(getApplicationContext(),
	// BuildingActivity.class);
	// intent.putExtra("jp.ac.ehime_u.cite.sasaki.easyguide.model.Building",
	// building_.getTitle());
	// startActivity(intent);
	// }// InvokeMapActivity

}// OpeningActivity
