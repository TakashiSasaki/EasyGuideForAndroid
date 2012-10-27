package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import java.io.FileNotFoundException;

import jp.ac.ehime_u.cite.sasaki.easyguide.exception.ItemNotFoundException;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Building;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Facility;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organization;
//import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organizations;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class FacilityActivity extends ClickableActivity<Building> {

	Facility facility;
	Intent intent;
	int organizationIndex;
	int facilityIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.intent = this.getIntent();
		this.organizationIndex = this.intent
				.getIntExtra("organizationIndex", 0);
		this.facilityIndex = this.intent.getIntExtra("facilityIndex", 0);

////		Organizations organizations;
//		try {
//			organizations = Organizations.getInstance();
//			Organization organization = organizations
//					.getOrganization(this.organizationIndex);
//			this.facility = organization.getFacility(this.facilityIndex);
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (ItemNotFoundException e) {
//			this.facility = Facility.getDummy();
//		}// try

		ArrayAdapter<Building> array_adapter = new ArrayAdapter<Building>(this,
				android.R.layout.simple_spinner_dropdown_item);
		array_adapter.add(Building.getDummy());
		for (Building b : this.facility) {
			array_adapter.add(b);
		}

		setSpinnerArrayAdapter(array_adapter);

		try {
			setImageView(this.facility);
		} catch (Exception e) {
			Log.v(new Throwable(), "Can't set image of " + facility.getTitle());
		}
		for (Building b : this.facility) {
			addStarPoint(new Point(b.getX(), b.getY()));
		}// for
	}// onCreate

	@Override
	protected void onStarTouched(Point point) {
		try {
			Building b = this.facility.getNearest(point);
			InvokeActivity(b);
		} catch (ItemNotFoundException e) {
			Log.v(new Throwable(), "No building near " + point.toString()
					+ " in facility " + this.facility.getTitle());
		}
	}

	@Override
	protected void InvokeActivity(Building selected_item) {
		Intent intent = new Intent();
		intent.setClass(this, BuildingActivity.class);
		intent.putExtra("organizationIndex", this.organizationIndex);
		intent.putExtra("facilityIndex", this.facilityIndex);
		intent.putExtra("buildingIndex", selected_item.getIndex());
		startActivity(intent);
	}

}// FacilityActivity

