package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import jp.ac.ehime_u.cite.sasaki.easyguide.exception.ItemNotFoundException;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Building;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.DistanceCalculator;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Facility;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organization;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organizations;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class FacilityActivity extends ClickableActivity {

	Facility facility;
	Intent intent;
	int organizationIndex;
	int facilityIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facility);
		this.intent = this.getIntent();
		this.organizationIndex = this.intent
				.getIntExtra("organizationIndex", 0);
		this.facilityIndex = this.intent.getIntExtra("facilityIndex", 0);

		Organizations organizations = Organizations.getInstance();
		try {
			Organization organization = organizations
					.getOrganization(this.organizationIndex);
			this.facility = organization.getFacility(this.facilityIndex);
		} catch (ItemNotFoundException e) {
			this.facility = Facility.getDummy();
		}//try

		//this.imageView = (ImageView) findViewById(R.id.imageViewFacility);
		this.setImageView(this.facility);
		//image_view.setImageBitmap(this.facility.getImage());

		SetSpinnerFacilities();
	}// onCreate

	void SetSpinnerFacilities() {
		ArrayAdapter<Building> building_array_adapter = new ArrayAdapter<Building>(
				this, android.R.layout.simple_spinner_dropdown_item);
		building_array_adapter.add(Building.getDummy());
		for (Building b : this.facility) {
			building_array_adapter.add(b);
		}
		Spinner s = (Spinner) findViewById(R.id.spinnerBuildings);
		s.setAdapter(building_array_adapter);
		OnItemSelectedListener l = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Building selected_building = (Building) arg0
						.getItemAtPosition(arg2);
				if (selected_building.isEmpty())
					return;
				InvokeBuildingActivity(selected_building);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		};
		s.setOnItemSelectedListener(l);
	}

	protected void InvokeBuildingActivity(Building selected_building) {
		Intent intent = new Intent();
		intent.setClass(this, BuildingActivity.class);
		intent.putExtra("organizationIndex", this.organizationIndex);
		intent.putExtra("facilityIndex", this.facilityIndex);
		intent.putExtra("buildingIndex", selected_building.getIndex());
		startActivity(intent);
	}

	@Override
	protected void onStarTouched(Point point) {
		// TODO Auto-generated method stub
		
	}

}// FacilityActivity

