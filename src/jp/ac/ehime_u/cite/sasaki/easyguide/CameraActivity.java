package jp.ac.ehime_u.cite.sasaki.easyguide;

import jp.ac.ehime_u.cite.sasaki.easyguide.R.id;
import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

@SuppressWarnings("javadoc")
public class CameraActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		FrameLayout frame_layout = (FrameLayout) findViewById(id.frameLayoutCamera);
		frame_layout.addView(new CameraPreviewSurfaceView(this));
	}
}
