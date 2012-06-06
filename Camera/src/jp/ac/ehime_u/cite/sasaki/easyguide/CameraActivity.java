package jp.ac.ehime_u.cite.sasaki.easyguide;

import jp.ac.ehime_u.cite.sasaki.easyguide.R.id;
import android.app.Activity;
import android.app.AlertDialog;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

@SuppressWarnings("javadoc")
public class CameraActivity extends Activity {
	/** Called when the activity is first created. */
	private CameraPreviewSurfaceView cameraPreviewSurfaceView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		FrameLayout frame_layout = (FrameLayout) findViewById(id.frameLayoutCamera);
		this.cameraPreviewSurfaceView = new CameraPreviewSurfaceView(this,
				new Handler(), (ImageView) findViewById(R.id.imageViewOngoing),
				(ImageView) findViewById(id.imageViewProcessed),
				(EditText) findViewById(id.editTextCount),
				(EditText) findViewById(id.editTextProcessingTime),
				(EditText) findViewById(id.editTextRecognitionResult));
		frame_layout.addView(this.cameraPreviewSurfaceView);

		((Button) findViewById(id.buttonFocus))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Focus();
					}
				});
		((Button) findViewById(id.buttonAbout))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						About();
					}
				});
	}

	public void About() {
		AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(this);
		builder.setTitle("about EasyGuide Camera");
		builder.setMessage(getString(R.string.about));
		builder.show();
	}

	public void Focus() {
		Camera camera;
		try {
			camera = this.cameraPreviewSurfaceView.getCamera();
			camera.autoFocus(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
