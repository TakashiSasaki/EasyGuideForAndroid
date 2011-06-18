package com.blogspot.minmin_work.OpenGL_2;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * @author Yoshiki IZUMI
 * @see "http://minmin-work.blogspot.com/"
 */
public class Application extends Activity {
	private GLSurfaceView mGLSurfaceView;
	private GLRenderer mGLRenderer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGLSurfaceView = new GLSurfaceView(this);
		setContentView(mGLSurfaceView);

		WindowManager windowmanager = (WindowManager) getSystemService(WINDOW_SERVICE);
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