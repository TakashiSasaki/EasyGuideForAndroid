package jp.ac.ehime_u.cite.sasaki.easyguide;

import java.util.Date;
import java.util.concurrent.Semaphore;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.Handler;
import android.graphics.Bitmap;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("javadoc")
public class CameraPreviewSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback, PreviewCallback, AutoFocusCallback {

	// private SurfaceHolder surfaceHolder;
	protected Camera camera;
	private byte[] yuvByteArray;
	private int[] rgbIntArray;
	private Bitmap ongoingBitmap;
	private Bitmap processedBitmap;
	private int previewWidth;
	private int previewHeight;
	private Handler handler;
	private ImageView ongoingImageView;
	private Semaphore ongoingImageSemaphore = new Semaphore(1);
	private ImageView processedImageView;
	private Semaphore processedImageSemaphore = new Semaphore(1);
	private EditText editTextProcessingTime;
	private EditText editTextCount;
	private EditText editTextRecognitionResult;
	private int count;
	private long processingTime;
	private int recognitionResult;

	public Camera getCamera() throws Exception {
		if (camera == null) {
			throw new Exception("the camera is not initialized.");
		}
		return camera;
	}

	public CameraPreviewSurfaceView(Context context, Handler handler_,
			ImageView ongoing_image, ImageView processed_image,
			EditText edit_text_count, EditText edit_text_processing_time,
			EditText edit_text_recognition_result) {
		super(context);
		this.handler = handler_;
		this.ongoingImageView = ongoing_image;
		this.processedImageView = processed_image;
		this.editTextCount = edit_text_count;
		this.editTextProcessingTime = edit_text_processing_time;
		this.editTextRecognitionResult = edit_text_recognition_result;
		SurfaceHolder surface_holder = this.getHolder();
		surface_holder.addCallback(this);
		surface_holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}// constructor

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
			Log.v(this.getClass().getSimpleName(),
					"preview format=" + params.getPreviewFormat() + " width="
							+ params.getPreviewSize().width + " height="
							+ params.getPreviewSize().height);
			this.previewWidth = params.getPreviewSize().width;
			this.previewHeight = params.getPreviewSize().height;
			// params.setPreviewSize(this.getWidth(), this.getHeight());
			// params.setRotation(270);
			this.camera.setParameters(params);
			this.camera.setDisplayOrientation(90);
			this.camera.setPreviewDisplay(surface_holder);
			this.camera.setPreviewCallback(this);
			rgbIntArray = new int[(previewWidth * previewHeight)];
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
	public void onPreviewFrame(final byte[] data, final Camera camera) {
		// this.camera.stopPreview();
		camera.setPreviewCallback(null);
		this.yuvByteArray = data;
		// Log.v(this.getClass().getSimpleName(), "raw data length " +
		// data.length
		// + " width " + camera.getParameters().getPreviewFormat()
		// + " height " + getHeight());
		final PreviewCallback preview_callback = this;
		Thread format_conversion_thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// rgbIntArray = new int[(previewWidth * previewHeight)];
				DecodeYuvToRgb(rgbIntArray, yuvByteArray, previewWidth,
						previewHeight);
				ongoingBitmap = Bitmap.createBitmap(previewWidth,
						previewHeight, Bitmap.Config.ARGB_4444);
				ongoingBitmap.setPixels(rgbIntArray, 0, previewWidth, 0, 0,
						previewWidth, previewHeight);
				SetOngoingImageView();
				RecognitionThread recognition_thread = new RecognitionThread(
						yuvByteArray, rgbIntArray, ongoingBitmap, previewWidth,
						previewHeight);
				recognition_thread.start();
				try {
					recognition_thread.join();
					processingTime = recognition_thread.endDateTime
							- recognition_thread.startDateTime;
					recognitionResult = recognition_thread.equipmentId;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Log.v(this.getClass().getSimpleName(), "equipmentId = "
						+ recognition_thread.equipmentId);
				recognition_thread = null;
				processedBitmap = ongoingBitmap;
				SetProcessedImageView();
				SetEditText();
				if (ongoingBitmap != null) {
					ongoingBitmap.recycle();
				}
				if (processedBitmap != null) {
					processedBitmap.recycle();
				}
				camera.setPreviewCallback(preview_callback);
				// camera.startPreview();
			}
		});
		if (ongoingBitmap != null) {
			ongoingBitmap.recycle();
		}
		if (processedBitmap != null) {
			processedBitmap.recycle();
		}
		format_conversion_thread.start();
		count += 1;
		// invalidate();
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
	public static final void DecodeYuvToRgb(int[] rgb, final byte[] yuv420sp,
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

	public void SetOngoingImageView() {
		try {
			ongoingImageSemaphore.acquire();
			handler.post(new SetImageBitmapRunnable(ongoingBitmap,
					ongoingImageView, ongoingImageSemaphore));
		} catch (InterruptedException e) {
			Log.v(this.getClass().getSimpleName(),
					"SetOngoingImageRunnable#run is in the UI thread queue.");
		}
	}// SetOngoingImage

	public void SetProcessedImageView() {
		try {
			processedImageSemaphore.acquire();
			handler.post(new SetImageBitmapRunnable(processedBitmap,
					processedImageView, processedImageSemaphore));
		} catch (InterruptedException e) {
			Log.v(this.getClass().getSimpleName(),
					"SetProcessedImageRunnable#run is in the UI thread queue.");
		}
	}

	private void SetEditText() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				editTextCount.setText("" + count);
				editTextProcessingTime.setText("" + (int) processingTime);
				editTextRecognitionResult.setText("" + recognitionResult);
			}
		});
	}

	@Override
	public void onAutoFocus(boolean success, Camera camera) {

	}
}
