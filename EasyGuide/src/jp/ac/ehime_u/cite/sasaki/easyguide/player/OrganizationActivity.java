package jp.ac.ehime_u.cite.sasaki.easyguide.player;

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
import android.widget.ImageView;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class OrganizationActivity extends Activity {

	Organization organization;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.organization);

		organization = Organizations.GetTheOrganizations().GetOrganization(
				"assets");

		ImageView image_view = (ImageView) findViewById(R.id.imageViewOrganization);
		image_view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent motion_event) {
				ImageView image_view = (ImageView) arg0;
				DistanceCalculator distance_calculator = new DistanceCalculator(
						image_view);
				float x = distance_calculator.GetXOnDrawable(motion_event);
				float y = distance_calculator.GetYOnDrawable(motion_event);
				Log.v(this.getClass().getSimpleName(), "x=" + x + " y=" + y);
				InvokeNearestFacilityActivity(image_view, motion_event);
				return true; // stops event propagation
			}// onTouch
		});// setOnTouchListener
	}// onCreate

	@SuppressWarnings("unused")
	private static void ShowDialog(Context context, ImageView image_view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Bottom=" + image_view.getBottom() + " Top="
				+ image_view.getTop() + " Left=" + image_view.getLeft()
				+ " Right=" + image_view.getRight() + "\n" + " X="
				+ image_view.getX() + " Y=" + image_view.getY() + " Height="
				+ image_view.getHeight() + " Width=" + image_view.getWidth()
				+ "\nScaleX=" + image_view.getScaleX() + " ScaleY="
				+ image_view.getScaleY());
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
		Log.v(this.getClass().getSimpleName(), "The nearest facility is "
				+ facility.getFacilityName());
		Intent intent = new Intent();
		intent.setClass(this, FacilityActivity.class);
		startActivity(intent);
	}// InvokeNearestFacilityActivity

}// OrganizationActivity

