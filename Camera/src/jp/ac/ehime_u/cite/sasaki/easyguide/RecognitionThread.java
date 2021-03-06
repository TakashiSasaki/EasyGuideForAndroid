package jp.ac.ehime_u.cite.sasaki.easyguide;

import java.util.Calendar;
import java.util.Date;

import android.graphics.Bitmap;
import android.text.method.DateTimeKeyListener;

public class RecognitionThread extends Thread {

	/*
	 * The result is expected to be one integer value. Store it to equipmentId
	 * member at the end of run() method.
	 */
	public int equipmentId;
	public long startDateTime;
	public long endDateTime;

	private int width;
	private int height;

	/*
	 * yuvByteArray has pixel data in YUV420 format.
	 */
	private byte[] yuvByteArray;

	/*
	 * rgbIntArray has pixel data in ARGB_8888 format.
	 */
	private int[] rgbIntArray;

	/*
	 * androidBitmap is an instance of android.graphics.Bitmap class.
	 */
	private Bitmap androidBitmap;

	/**
	 * RecognitionThread is instantiated and run from CameraPreviewSurfaceView.
	 * Implementers of recognition algorithms need not care about it.
	 * 
	 * @param context
	 * @param equipment_id
	 */
	public RecognitionThread(byte[] yuv_byte_array, int[] rgb_int_array,
			Bitmap android_bitmap, int width_, int height_) {
		this.equipmentId = 0;
		this.yuvByteArray = yuv_byte_array;
		this.rgbIntArray = rgb_int_array;
		this.androidBitmap = android_bitmap;
		this.width = width_;
		this.height = height_;
	}// constructor

	@Override
	public void run() {
		Calendar startCalendar = Calendar.getInstance();
		this.startDateTime = startCalendar.getTimeInMillis();
		this.equipmentId = 9999;

		// TODO: recognition algorithm is to be implemented here.
		// The image is given in three data formats,
		// YUV420, ARGB_8888 and android.graphics.Bitmap .
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// TODO: set the result to equipmentNumber.
		this.equipmentId = 9999;

		// that'll be all
		Calendar endCalendar = Calendar.getInstance();
		this.endDateTime = endCalendar.getTimeInMillis();
	}// run
}// RecognitionThread

