package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import java.io.FileNotFoundException;

import jp.ac.ehime_u.cite.sasaki.easyguide.exception.ItemNotFoundException;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Facility;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organization;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organizations;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class OrganizationActivity extends ClickableActivity<Facility> {

	Organization organization;
	int organizationIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		this.organizationIndex = intent.getIntExtra("organizationIndex", 0);
		Log.v(new Throwable(),
				"OrganizationActivity was invoked with number = "
						+ this.organizationIndex);

		try {
			this.organization = Organizations.getInstance().getOrganization(
					this.organizationIndex);
		} catch (ItemNotFoundException e) {
			this.organization = Organization.getDummy();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayAdapter<Facility> facilities_array_adapter = new ArrayAdapter<Facility>(
				this, android.R.layout.simple_spinner_dropdown_item);
		facilities_array_adapter.add(Facility.getDummy());
		for (Facility f : this.organization) {
			facilities_array_adapter.add(f);
		}

		setSpinnerArrayAdapter(facilities_array_adapter);

		try {
			this.setImageView(this.organization);
		} catch (Exception e) {
			Log.v(new Throwable(),
					"Can't set image of " + this.organization.getTitle());
		}
		for (Facility f : this.organization) {
			addStarPoint(new Point(f.getX(), f.getY()));
		}

	}// onCreate

	@Override
	protected void onStarTouched(Point point) {
		try {
			Facility f = this.organization.getNearest(point);
			InvokeActivity(f);
		} catch (ItemNotFoundException e) {
			Log.v(new Throwable(), "No facility near " + point.toString()
					+ " in organization " + this.organization.getTitle());
		}// try
	}// onStarTouched

	@Override
	protected void InvokeActivity(Facility selected_item) {
		Intent intent = new Intent();
		intent.setClass(this, FacilityActivity.class);
		Log.v(new Throwable(), "Invoking FacilityActivity with number "
				+ selected_item.getIndex());
		intent.putExtra("organizationIndex", this.organizationIndex);
		intent.putExtra("facilityIndex", selected_item.getIndex());
		startActivity(intent);
	}// InvokeActivity

}// OrganizationActivity

