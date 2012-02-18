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
public class FacilityActivity extends Activity {

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

		ImageView image_view = (ImageView) findViewById(R.id.imageViewFacility);
		image_view.setImageBitmap(this.facility.getImage());
		image_view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent motion_event) {
				ImageView image_view = (ImageView) arg0;
				DistanceCalculator distance_calculator = new DistanceCalculator(
						image_view);
				float x = distance_calculator.GetXOnDrawable(motion_event);
				float y = distance_calculator.GetYOnDrawable(motion_event);
				Log.v(this.getClass().getSimpleName(), "x=" + x + " y=" + y);
				InvokeNearestBuildingActivity(image_view, motion_event);
				return true; // stops event propagation
			}// onTouch
		});// setOnTouchListener

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

	@SuppressWarnings("unused")
	private static void ShowDialog(Context context, ImageView image_view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Bottom=" + image_view.getBottom() + " Top="
				+ image_view.getTop() + " Left=" + image_view.getLeft()
				+ " Right=" + image_view.getRight() + "\n" + " Height="
				+ image_view.getHeight() + " Width=" + image_view.getWidth());
		builder.setCancelable(false);
		builder.setPositiveButton("OK", null);
		builder.show();
	}// ShowDialog for ImageView

	@SuppressWarnings("unused")
	private static void ShowDialog(Context context, MotionEvent motion_event) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("RawX=" + motion_event.getRawX() + " RawY="
				+ motion_event.getRawY() + " X=" + motion_event.getX() + " Y="
				+ motion_event.getY());
		builder.setCancelable(false);
		builder.setPositiveButton("OK", null);
		builder.show();
	}// ShowDialog for MotionEvent

	@SuppressWarnings("unused")
	private static void ShowDialog(Context context, Rect rect) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("bottom=" + rect.bottom + " left=" + rect.left
				+ " right=" + rect.right + " top=" + rect.top);
		builder.setCancelable(false);
		builder.setPositiveButton("OK", null);
		builder.show();
	}// ShowDialog for Rect

	@SuppressWarnings("unused")
	private static void ShowDialog(Context context, Drawable drawable) {
		Rect bounds = drawable.getBounds();
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("IntrinsicHeight=" + drawable.getIntrinsicHeight()
				+ " IntrinsicWidth=" + drawable.getIntrinsicWidth()
				+ "\nMinimumHeight=" + drawable.getMinimumHeight()
				+ " MinimumWidth=" + drawable.getMinimumWidth()
				+ "\nbounds.top=" + bounds.top + " bounds.bottom="
				+ bounds.bottom + " bounds.left=" + bounds.left
				+ " bounds.right=" + bounds.right);
		builder.setCancelable(false);
		builder.setPositiveButton("OK", null);
		builder.show();
	}// ShowDialog for Drawable

	private void InvokeNearestBuildingActivity(ImageView image_view,
			MotionEvent motion_event) {
		Building building = this.facility.GetNearestBuilding(image_view,
				motion_event);
		// Log.v(this.getClass().getSimpleName(), "The nearest building is "
		// + building.getBuildingName());
		Intent intent = new Intent();
		intent.setClass(this, BuildingActivity.class);
		startActivity(intent);
	}// InvokeNearestBuildingActivity

}// FacilityActivity

