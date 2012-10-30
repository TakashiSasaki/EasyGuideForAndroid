package com.gmail.takashi316.easyguide.model;

import com.gmail.takashi316.easyguide.content.DirectoryName;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class DistanceCalculator {
	float scalingMultiplier;
	float topOnDrawingRect;
	float leftOnDrawingRect;

	/**
	 * @param image_view
	 */
	public DistanceCalculator(ImageView image_view) {
		float drawable_height = image_view.getDrawable().getIntrinsicHeight();
		float drawable_width = image_view.getDrawable().getIntrinsicWidth();
		Rect rect = new Rect();
		image_view.getDrawingRect(rect);
		float drawing_rect_width = rect.right - rect.left + 1;
		float scaling_rect_width = drawing_rect_width
				- image_view.getPaddingLeft() - image_view.getPaddingRight();
		scaling_rect_width = image_view.getMeasuredWidth(); // it is simpler
		float drawing_rect_height = rect.bottom - rect.top + 1;
		float scaling_rect_height = drawing_rect_height
				- image_view.getPaddingBottom() - image_view.getPaddingTop();
		scaling_rect_height = image_view.getMeasuredHeight();
		Log.v(this.getClass().getSimpleName(), "drawing_rect_width="
				+ drawing_rect_width + " scaling_rect_width="
				+ scaling_rect_width + " drawing_rect_height="
				+ drawing_rect_height + " scaling_rect_height="
				+ scaling_rect_height);
		float horizontal_multiplier = scaling_rect_width / drawable_width;
		float vertical_multiplier = scaling_rect_height / drawable_height;
		this.scalingMultiplier = Math.min(horizontal_multiplier,
				vertical_multiplier);
		Log.v(this.getClass().getSimpleName(), "horizontal multiplier="
				+ horizontal_multiplier + " vertical multiplier="
				+ vertical_multiplier);
		float scaled_height = drawable_height * this.scalingMultiplier;
		float scaled_width = drawable_width * this.scalingMultiplier;
		Log.v(this.getClass().getSimpleName(), "scaled height=" + scaled_height
				+ " scaled width=" + scaled_width);
		Log.v(this.getClass().getSimpleName(),
				"MeasuredHeight=" + image_view.getMeasuredHeight()
						+ " MeasuredWidth=" + image_view.getMeasuredWidth());
		this.topOnDrawingRect = (scaling_rect_height - scaled_height) / 2
				+ image_view.getPaddingTop();
		this.leftOnDrawingRect = (scaling_rect_width - scaled_width) / 2
				+ image_view.getPaddingLeft();
	}// a constructor

	public float GetXOnDrawable(MotionEvent motion_event) {
		float left_on_image_view = motion_event.getX();
		float left_on_scaled_drawable = left_on_image_view
				- this.leftOnDrawingRect;
		return left_on_scaled_drawable / this.scalingMultiplier;
	}// GetXOnDrawable

	public float GetYOnDrawable(MotionEvent motion_event) {
		float top_on_image_view = motion_event.getY();
		float top_on_scaled_drawable = top_on_image_view
				- this.topOnDrawingRect;
		return top_on_scaled_drawable / this.scalingMultiplier;
	}// GetYOnDrawable

	public double GetDistanceBetween(MotionEvent motion_event,
			int x_on_drawable, int y_on_drawable) {
		float touched_x_on_drawable = GetXOnDrawable(motion_event);
		float touched_y_on_drawable = GetYOnDrawable(motion_event);
		return Math.sqrt(Math.pow(
				touched_x_on_drawable - (float) x_on_drawable, 2)
				+ Math.pow(touched_y_on_drawable - (float) y_on_drawable, 2));
	}// GetDistanceBetween

	public double GetDistanceBetween(MotionEvent motion_event,
			DirectoryName directory_name) {
		return GetDistanceBetween(motion_event, directory_name.x,
				directory_name.y);
	}
} // DistanceCalculator
