package test.sample;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Yoshiki IZUMI
 * @see "http://minmin-work.blogspot.com/"
 */
public class Scene implements View.OnTouchListener {

	private FloatBuffer vertexBuffer;
	private FloatBuffer colorBuffer;

	private int mWidth;
	private int mHeight;

	/**
	 * @param display
	 */
	public Scene(Display display) {
		mWidth = display.getWidth();
		mHeight = display.getHeight();

		float x = 0.0f;
		float y = 0.0f;
		float z = 0.0f;
		float sizeX = 10.0f;
		float sizeY = 10.0f;
		float sizeZ = 0.0f;
		float[] vertex = {
				// 左側の三角形
				x + sizeX, y - sizeY, z + sizeZ, x - sizeX, y - sizeY,
				z + sizeZ, x - sizeX, y + sizeY, z + sizeZ,
				// 右側の三角形
				x - sizeX, y + sizeY, z + sizeZ, x + sizeX, y + sizeY,
				z + sizeZ, x + sizeX, y - sizeY, z + sizeZ, };
		ByteBuffer vb = ByteBuffer.allocateDirect(6 * 3 * 4);
		vb.order(ByteOrder.nativeOrder()); // ビッグエンディアンかリトルエンディアンにあわせてくれる
		vertexBuffer = vb.asFloatBuffer();
		vertexBuffer.put(vertex);
		vertexBuffer.position(0);

		float[] color = {
				// 左側の三角形（青）
				0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f,
				1.0f, 1.0f,
				// 右側の三角形（緑）
				0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f,
				0.0f, 1.0f };
		ByteBuffer vbc = ByteBuffer.allocateDirect(6 * 4 * 4);
		vbc.order(ByteOrder.nativeOrder()); // ビッグエンディアンかリトルエンディアンにあわせてくれる
		colorBuffer = vbc.asFloatBuffer();
		colorBuffer.put(color);
		colorBuffer.position(0);

	}

	/**
	 * @param gl
	 */
	public void draw(GL10 gl) {
		gl.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// ビューポート行列(ウィンドウへの出力領域)設定
		gl.glViewport(0, 0, mWidth, mHeight);

		// 射影行列設定
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		// 視界(視野角、画面のアスペクト、比近視点、遠視点)の設定
		float aspect = (float) mWidth / (float) mHeight;
		GLU.gluPerspective(gl, 45.0f, aspect, 0.1f, 3000.0f);

		GLU.gluLookAt(gl, 0.0f, 0.0f, 100.0f, // 位置(視点)
				0.0f, 0.0f, 0.0f, // 見つめているところ
				0.0f, 1.0f, 0.0f // 水平
		);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);

		int triangleNum = 2;
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, triangleNum * 3);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

}
