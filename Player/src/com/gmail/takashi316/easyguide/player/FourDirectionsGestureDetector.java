package com.gmail.takashi316.easyguide.player;

import com.gmail.takashi316.easyguide.util.Log;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

class FourDirectionsOnGestureListener extends SimpleOnGestureListener {

	Runnable leftCallback;
	Runnable rightCallBack;
	Runnable upCallback;
	Runnable downCallback;

	public FourDirectionsOnGestureListener(Runnable left_callback,
			Runnable right_callback, Runnable up_callback,
			Runnable down_callback) {
		this.leftCallback = left_callback;
		this.rightCallBack = right_callback;
		this.upCallback = up_callback;
		this.downCallback = down_callback;
	}

	protected static final float SWIPE_MAX_OFF_PATH = 200;
	protected static final float SWIPE_MIN_DISTANCE = 100;
	protected static final float SWIPE_THRESHOLD_VELOCITY = 10;

	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2,
			float velocityX, float velocityY) {

		Log.v(new Throwable(), "X1=" + event1.getX() + ",Y1=" + event1.getY()
				+ ",X2=" + event2.getX() + ",Y2=" + event2.getY());
		try {
			if (event1.getX() - event2.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				if (leftCallback != null)
					leftCallback.run();
			} else if (event2.getX() - event1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				if (rightCallBack != null)
					rightCallBack.run();
			}

		} catch (Exception e) {
			// nothing
		}// try
		return false;
	}// onFling

}// FourDirectionsOnGestureListener

public class FourDirectionsGestureDetector extends GestureDetector {

	public FourDirectionsGestureDetector(Context context,
			Runnable left_callback, Runnable right_callback,
			Runnable top_callback, Runnable bottom_callback) {
		super(context, new FourDirectionsOnGestureListener(left_callback,
				right_callback, top_callback, bottom_callback));
	}// a constructor
}// FourDirctionsGestureDetector
