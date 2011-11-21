package jp.ac.ehime_u.cite.sasaki.easyguide;

import android.graphics.Bitmap;

public class RecognitionThread extends Thread {

	/*
	 * The result is expected to be one integer value. Store it to equipmentId
	 * member at the end of run() method.
	 */
	private Integer equipmentId;

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
	public RecognitionThread(Integer equipment_id, byte[] yuv_byte_array,
			int[] rgb_int_array, Bitmap android_bitmap, int width_, int height_) {
		equipmentId = equipment_id;
		yuvByteArray = yuv_byte_array;
		rgbIntArray = rgb_int_array;
		androidBitmap = android_bitmap;
		width = width_;
		height = height_;
	}// constructor

	public void run() {

		// TODO: recognition algorithm is to be implemented here.
		// The image is given in three data formats,
		// YUV420, ARGB_8888 and android.graphics.Bitmap .
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// TODO: set the result to equipmentNumber.
		equipmentId = 9999;

		// that'll be all
	}// run
}// RecognitionThread

