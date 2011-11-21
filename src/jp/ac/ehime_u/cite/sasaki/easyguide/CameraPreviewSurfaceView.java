package jp.ac.ehime_u.cite.sasaki.easyguide;

import java.text.SimpleDateFormat;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressWarnings("javadoc")
public class CameraPreviewSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback, PreviewCallback, AutoFocusCallback {

	// private SurfaceHolder surfaceHolder;
	protected Camera camera;
	private byte[] rawImage;
	private int[] rgbImage;
	private Bitmap bitmapImage;
	private int width;
	private int height;

	public Camera getCamera() throws Exception {
		if (camera == null) {
			throw new Exception("the camera is not initialized.");
		}
		return camera;
	}

	public CameraPreviewSurfaceView(Context context) {
		super(context);
		SurfaceHolder surface_holder = this.getHolder();
		surface_holder.addCallback(this);
		surface_holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceChanged(SurfaceHolder surfacee_holder, int format,
			int width, int height) {
		this.camera.startPreview();
		Camera.Parameters params = this.camera.getParameters();
		params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
		params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		this.camera.setParameters(params);
	}

	@Override
	public void surfaceCreated(SurfaceHolder surface_holder) {

		// this is a test for SurfaceView
		// Canvas canvas = arg0.lockCanvas();
		// Paint paint = new Paint();
		// paint.setColor(Color.RED);
		// canvas.drawRect(0, 0, 50, 50, paint);
		// arg0.unlockCanvasAndPost(canvas);

		this.camera = Camera.open();
		try {
			Camera.Parameters params = this.camera.getParameters();
			// params.setPreviewSize(this.getWidth(), this.getHeight());
			// params.setRotation(270);
			this.camera.setParameters(params);
			this.camera.setDisplayOrientation(90);
			this.camera.setPreviewDisplay(surface_holder);
			this.camera.setPreviewCallback(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		Camera.Parameters params = this.camera.getParameters();
		params.setFlashMode(Parameters.FLASH_MODE_OFF);
		this.camera.setParameters(params);
		this.camera.release();
		this.camera = null;
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		// TODO Auto-generated method stub
		this.camera.setPreviewCallback(null);
		this.camera.stopPreview();
		this.rawImage = data;
		this.width = getWidth();
		this.height = getHeight();
		this.rgbImage = new int[(width * height)];
		DecodeYuvToRgb(this.rgbImage, this.rawImage, this.width, this.height);
		this.bitmapImage = Bitmap.createBitmap(this.width, this.height,
				Bitmap.Config.ARGB_8888);
		this.bitmapImage.setPixels(this.rgbImage, 0, this.width, 0, 0,
				this.width, this.height);
		this.camera.setPreviewCallback(this);
		this.camera.startPreview();
	}// onPreviewFrame

	/**
	 * Convert YUV420 to Bitmap This code fragment comes from
	 * {@link "http://code.google.com/p/android/issues/detail?id=823"} posted by
	 * justinbonnar. THX.
	 * 
	 * @param rgb
	 * @param yuv420sp
	 * @param width
	 * @param height
	 */
	// YUV420 to BMP
	public static final void DecodeYuvToRgb(int[] rgb, byte[] yuv420sp,
			int width, int height) {
		final int frameSize = width * height;
		for (int j = 0, yp = 0; j < height; j++) {
			int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
			for (int i = 0; i < width; i++, yp++) {
				int y = (0xff & ((int) yuv420sp[yp])) - 16;
				if (y < 0)
					y = 0;
				if ((i & 1) == 0) {
					v = (0xff & yuv420sp[uvp++]) - 128;
					u = (0xff & yuv420sp[uvp++]) - 128;
				}
				int y1192 = 1192 * y;
				int r = (y1192 + 1634 * v);
				int g = (y1192 - 833 * v - 400 * u);
				int b = (y1192 + 2066 * u);
				if (r < 0)
					r = 0;
				else if (r > 262143)
					r = 262143;
				if (g < 0)
					g = 0;
				else if (g > 262143)
					g = 262143;
				if (b < 0)
					b = 0;
				else if (b > 262143)
					b = 262143;
				rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000)
						| ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
			}
		}
	}// DecodeYuvToRgb

	public void GetPreviewImage() {

	}

	@Override
	public void onAutoFocus(boolean success, Camera camera) {

	}

}