package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import jp.ac.ehime_u.cite.sasaki.easyguide.exception.ItemNotFoundException;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Facility;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.ItemBase;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organization;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organizations;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class OrganizationActivity extends ClickableActivity {

	Organization organization;
	int organizationIndex;
	ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.organization);
		Intent intent = getIntent();
		this.organizationIndex = intent.getIntExtra("organizationIndex", 0);
		Log.v(new Throwable(),
				"OrganizationActivity was invoked with number = "
						+ this.organizationIndex);

		// organization = Organizations.GetTheOrganizations().GetOrganization(
		// "assets");
		try {
			this.organization = Organizations.getInstance().getOrganization(
					this.organizationIndex);
		} catch (ItemNotFoundException e) {
			this.organization = Organization.getDummy();
		}// try

		SetSpinnerFacilities();

		this.imageView = (ImageView) findViewById(R.id.imageViewOrganization);
		// this.imageView.setImageBitmap(this.organization.getImage());
		this.setImageView(this.organization);
		for (Facility f : this.organization) {
			addStarPoint(new Point(f.getX(), f.getY()));
		}
		// this.imageView.setOnTouchListener(new OnTouchListener() {
		// @Override
		// public boolean onTouch(View arg0, MotionEvent motion_event) {
		// ImageView image_view = (ImageView) arg0;
		// DistanceCalculator distance_calculator = new DistanceCalculator(
		// image_view);
		// float x = distance_calculator.GetXOnDrawable(motion_event);
		// float y = distance_calculator.GetYOnDrawable(motion_event);
		// Log.v(new Throwable(), "x=" + x + " y=" + y);
		// InvokeNearestFacilityActivity(image_view, motion_event);
		//
		// // test code
		// Matrix m = OrganizationActivity.this.imageView.getMatrix();
		// Log.v(new Throwable(), "getMatrix returns " + m.toString());
		// Matrix im = OrganizationActivity.this.imageView
		// .getImageMatrix();
		// Log.v(new Throwable(), "getImageMatrix returns " + m.toString());
		//
		// return true; // stops event propagation
		// }// onTouch
		// });// setOnTouchListener

	}// onCreate

	@Override
	protected void onStart() {
		super.onStart();
		// this.drawSurface();
	}

	protected void onResume() {
		super.onResume();
		this.drawSurface();
	}

	void SetSpinnerFacilities() {
		ArrayAdapter<Facility> facilities_array_adapter = new ArrayAdapter<Facility>(
				this, android.R.layout.simple_spinner_dropdown_item);
		facilities_array_adapter.add(Facility.getDummy());
		for (Facility f : this.organization) {
			facilities_array_adapter.add(f);
		}
		Spinner s = (Spinner) findViewById(R.id.spinnerFacilities);
		s.setAdapter(facilities_array_adapter);
		OnItemSelectedListener l = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				Facility selected_faciliity = (Facility) parent
						.getItemAtPosition(position);
				if (selected_faciliity.isEmpty())
					return;
				Log.v(new Throwable(),
						"Facility " + selected_faciliity.getTitle() + ", "
								+ selected_faciliity.getIndex()
								+ " was selected");
				InvokeFacilityActivity(selected_faciliity);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		};// OnItemSelectedListener
		s.setSelection(0);
		s.setSelected(false);
		s.setOnItemSelectedListener(l);
	}// SetSpinnerFacilities

	void InvokeFacilityActivity(Facility facility) {
		Intent intent = new Intent();
		intent.setClass(this, FacilityActivity.class);
		Log.v(new Throwable(), "Invoking FacilityActivity with number "
				+ facility.getIndex());
		intent.putExtra("organizationIndex", this.organizationIndex);
		intent.putExtra("facilityIndex", facility.getIndex());
		startActivity(intent);
	}// InvokeFacilityActivity

	@SuppressWarnings("unused")
	private static void ShowDialog(Context context, ImageView image_view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Bottom=" + image_view.getBottom() + " Top="
				+ image_view.getTop() + " Left=" + image_view.getLeft()
				+ " Right=" + image_view.getRight() + "\nHeight="
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

	private void InvokeNearestFacilityActivity(ImageView image_view,
			MotionEvent motion_event) {
		Facility facility = this.organization.GetNearestFacility(image_view,
				motion_event);
		Log.v(new Throwable(), "The nearest facility is " + facility.getTitle());
		Intent intent = new Intent();
		intent.setClass(this, FacilityActivity.class);
		startActivity(intent);
	}// InvokeNearestFacilityActivity

	@Override
	protected void onStarTouched(Point point) {
		try {
			Facility f = this.organization.getNearest(point);
			InvokeFacilityActivity(f);
		} catch (ItemNotFoundException e) {
			Log.v(new Throwable(), "No facility in this organization "
					+ this.organization.getTitle());
		}// try
	}// onStarTouched

}// OrganizationActivity

