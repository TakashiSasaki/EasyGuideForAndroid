package test.sample;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.WindowManager;

public class MyApplication extends Activity {
	private GLSurfaceView mGLSurfaceView;
	private GLRenderer mGLRenderer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLSurfaceView = new GLSurfaceView(this);
        setContentView(mGLSurfaceView);

        WindowManager windowmanager = (WindowManager)getSystemService(WINDOW_SERVICE);
        mGLRenderer = new GLRenderer(this, windowmanager.getDefaultDisplay());
        mGLSurfaceView.setRenderer(mGLRenderer);

    }

	/**
	 *
	 */
	public void onPause() {
		super.onPause();
		mGLSurfaceView.onPause();
	}

	/**
	 *
	 */
	public void onResume() {
		super.onResume();
		mGLSurfaceView.onResume();
	}
}