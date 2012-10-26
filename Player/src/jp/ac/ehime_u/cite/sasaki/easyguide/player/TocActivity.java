package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import java.util.ArrayList;

import jp.ac.ehime_u.cite.sasaki.easyguide.content.ContentUnit;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.*;
import jp.ac.ehime_u.cite.sasaki.easyguide.ui.TocAdapter;
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
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 */
public class TocActivity extends Activity {
	/** Called when the activity is first created. */

	private ArrayList<Building> buildingList;
	private static Building chosenBuilding;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.toc);
		TocAdapter toc_adapter = new TocAdapter(this, ContentUnit.);
				android.R.layout.simple_list_item_1);
		this.buildingList = new ArrayList<Building>();
		for (Organization organization : Organizations.getInstance()) {
			Log.v(this.getClass().getSimpleName(), "Enumerating organizations");
			for (Facility facility : organization) {
				Log.v(this.getClass().getSimpleName(), "Enumerating facilities");
				for (Building building : facility) {
					Log.v(this.getClass().getSimpleName(),
							"Enumerating buildings. Building "
									+ building.getTitle() + " found.");
					assert (building.getTitle() != null);
					adapter.add(building.getTitle());
					this.buildingList.add(building);
				}//for
			}//for
		}//for
		ListView building_list_view = (ListView) findViewById(R.id.listViewOrganizations);
		Log.v(this.getClass().getSimpleName(), "Totally " + adapter.getCount()
				+ " buildings found.");
		building_list_view.setAdapter(adapter);

		building_list_view
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						ListView list_view = (ListView) parent;
						Log.v(this.getClass().getSimpleName(),
								"Chosen position is "
										+ position
										+ ", building name in ArrayAdapter is "
										+ (String) list_view
												.getItemAtPosition(position)
										+ ", building name in buildingList is "
										+ OpeningActivity.this.buildingList
												.get(position).getTitle());
						InvokeMapActivity(OpeningActivity.this.buildingList
								.get(position));
					}// onItemClick
				}// OnItemClickListener
				);
	}// onCreate

	private void InvokeMapActivity(Building building_) {
		OpeningActivity.chosenBuilding = building_;
		Intent intent = new Intent(getApplicationContext(),
				BuildingActivity.class);
		intent.putExtra("jp.ac.ehime_u.cite.sasaki.easyguide.model.Building",
				building_.getTitle());
		startActivity(intent);
	}// InvokeMapActivity

	/**
	 * @return the chosenBuilding
	 */
	static public Building getChosenBuilding() {
		return chosenBuilding;
	}

}// OpeningActivity
