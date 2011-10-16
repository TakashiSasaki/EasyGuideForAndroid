package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class OrganizationActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.organization);

		ImageView image_view = (ImageView) findViewById(R.id.imageViewOrganization);
		image_view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent motion_event) {
				ImageView image_view = (ImageView) arg0;
				return false;
			}// onTouch
		});// setOnTouchListener
	}// onCreate

	private static void ShowDialog(Context context, ImageView image_view) {
		Drawable drawable = image_view.getDrawable();
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

	private static void ShowDialog(Context context, MotionEvent motion_event) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("RawX=" + motion_event.getRawX() + " RawY="
				+ motion_event.getRawY() + " X=" + motion_event.getX() + " Y="
				+ motion_event.getY());
		builder.setCancelable(false);
		builder.setPositiveButton("OK", null);
		builder.show();
	}// ShowDialog for MotionEvent

	private static void ShowDialog(Context context, Rect rect) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("bottom=" + rect.bottom + " left=" + rect.left
				+ " right=" + rect.right + " top=" + rect.top);
		builder.setCancelable(false);
		builder.setPositiveButton("OK", null);
		builder.show();
	}// ShowDialog for Rect

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

}// OrganizationActivity

