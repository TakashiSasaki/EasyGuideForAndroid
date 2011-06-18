package test.sample;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.view.Display;

/**
 * @author Yoshiki IZUMI
 * @see "http://minmin-work.blogspot.com/"
 */
public class GLRenderer implements GLSurfaceView.Renderer {

	private Scene mScene;

	/**
	 * @param activity
	 * @param display
	 */
	public GLRenderer(Activity activity, Display display) {
		mScene = new Scene(display);
	}

	@Override
	public void onDrawFrame(GL10 gl) {// 毎フレーム呼ばれるメインループ
		mScene.draw(gl);
	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int arg1, int arg2) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		/*
		 * gl.glDisable(GL10.GL_DITHER);
		 * gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		 * 
		 * gl.glEnable(GL10.GL_CULL_FACE); gl.glFrontFace(GL10.GL_CW);
		 * 
		 * gl.glEnable(GL10.GL_DEPTH_TEST); gl.glEnable(GL10.GL_ALPHA_TEST);
		 * gl.glEnable(GL10.GL_BLEND); gl.glBlendFunc(GL10.GL_SRC_ALPHA,
		 * GL10.GL_ONE_MINUS_SRC_ALPHA);
		 * 
		 * gl.glEnable(GL10.GL_TEXTURE_2D);
		 */
		// mScene.setTexture(gl, mResource,R.drawable.image);

		// gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		// gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		// gl.glBindTexture(GL10.GL_TEXTURE_2D, mOpening.getTextureNumber());
		// gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
		// GL10.GL_MODULATE);

		// ビューポート行列(ウィンドウへの出力領域)設定
		/*
		 * gl.glViewport(0,0,mWidth,mHeight);
		 * 
		 * //射影行列設定 gl.glMatrixMode(GL10.GL_PROJECTION); gl.glLoadIdentity();
		 * //視界(視野角、画面のアスペクト、比近視点、遠視点)の設定 float aspect = (float)mWidth /
		 * (float)mHeight; GLU.gluPerspective(gl, 45.0f, aspect, 0.1f, 3000.0f);
		 */
	}

}